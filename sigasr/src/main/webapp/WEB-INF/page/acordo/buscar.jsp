<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<head><title>Acordos</title></head>

<jsp:include page="../popupHeader.jsp"></jsp:include>
<!-- <script src="//code.jquery.com/jquery-1.11.0.min.js"></script> -->
<jsp:include page="../main.jsp"></jsp:include>

<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
<script src="/sigasr/javascripts/jquery.populate.js"></script>
<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
<script src="/sigasr/javascripts/base-service.js"></script>
<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>

<div class="gt-bd clearfix">
    <div class="gt-content">
        <h2>Acordos</h2>
        <!-- content bomex -->
        <div class="gt-content-box dataTables_div">
            <div class="gt-form-row dataTables_length">
                <label>
                    <siga:checkbox name="mostrarDesativado" value="${mostrarDesativado}"/>
                    <b>Incluir Inativas</b>
                </label>
            </div>
            
            <table id="acordo_table" border="0" class="gt-table display">
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Descrição</th>
                        <th></th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${acordos}" var="acordo">
                    <tr
	                    <c:choose>
	                        <c:when test="${popup}">
	                            onclick="javascript:parent.retorna_${param.propriedade}('${acordo.id}','${acordo.nomeAcordo}','${acordo.descrAcordo}');window.close()"
	                        </c:when>
	                        <c:otherwise>
	                            data-json-id="${acordo.id}" data-json='${acordo.toJson()}' onclick="acordoService.editar($(this).data('json'), 'Alterar Acordo')"
	                        </c:otherwise>
	                    </c:choose>
                            style="cursor: pointer;">
                        <td >${acordo.nomeAcordo}</td>
                        <td>${acordo.descrAcordo}</td>
                        <td class="acoes">
                            <sigasr:desativarReativar id="acordo.id" 
                                                    onDesativar="acordoService.desativar" 
                                                    onReativar="acordoService.reativar" 
                                                    isAtivo="${acordo.isAtivo()}"/>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- /content box -->
        <div class="gt-table-buttons">
            <a onclick="acordoService.cadastrar('Incluir Acordo')" class="gt-btn-medium gt-btn-left">Incluir</a>
        </div>

    </div>
</div>

<sigasr:modal nome="editarAcordo" titulo="Editar Acordo">
    <div id="divEditarAcordo"><jsp:include page="editar.jsp"></jsp:include></div>
</sigasr:modal>

<script>
    var acordoTable,
        colunasAcordo = {};

    colunasAcordo.nome =    0;
    colunasAcordo.lotacao = 1;
    colunasAcordo.acoes =   2;
    
    var QueryString = function () {
        // This function is anonymous, is executed immediately and
        // the return value is assigned to QueryString!
        var query_string = {};
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            // If first entry with this name
            if (typeof query_string[pair[0]] === "undefined") {
                query_string[pair[0]] = pair[1];
                // If second entry with this name
            } else if (typeof query_string[pair[0]] === "string") {
                var arr = [ query_string[pair[0]], pair[1] ];
                query_string[pair[0]] = arr;
                // If third or later entry with this name
            } else {
                query_string[pair[0]].push(pair[1]);
            }
        }
        return query_string;
    }();

    jQuery(document).ready(function($) {
        if (QueryString.mostrarDesativados != undefined) {
            document.getElementById('checkmostrarDesativado').checked = QueryString.mostrarDesativados == 'true';
            document.getElementById('checkmostrarDesativado').value = QueryString.mostrarDesativados == 'true';
        }
            
        $("#checkmostrarDesativado").click(function() {
            if (document.getElementById('checkmostrarDesativado').checked)
                location.href = '${linkTo[AcordoController].buscar}?mostrarDesativados=true&popup=true&propriedade=${propriedade}';
            else
                location.href = '${linkTo[AcordoController].buscar}?popup=true&propriedade=${propriedade}';
        });
        
        optsAcordo.acordoTable = $('#acordo_table').dataTable({
        	stateSave : true,
            "language": {
                "emptyTable":     "Não existem resultados",
                "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
                "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
                "infoFiltered":   "(filtrando do total de _MAX_ registros)",
                "infoPostFix":    "",
                "thousands":      ".",
                "lengthMenu":     "Mostrar _MENU_ registros",
                "loadingRecords": "Carregando...",
                "processing":     "Processando...",
                "search":         "Filtrar:",
                "zeroRecords":    "Nenhum registro encontrado",
                "paginate": {
                    "first":      "Primeiro",
                    "last":       "Último",
                    "next":       "Próximo",
                    "previous":   "Anterior"
                },
                "aria": {
                    "sortAscending":  ": clique para ordenação crescente",
                    "sortDescending": ": clique para ordenação decrescente"
                }
            },
            "columnDefs": [{
                "targets": [colunasAcordo.acoes],
                "searchable": false,
                "sortable" : false
            }],
            "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                var acordo = undefined;
                
                try {
                    acordo = JSON.parse($(nRow).data('json'));
                }
                catch(err) {
                    acordo = $(nRow).data('json');
                }
                
                if (acordo) {
                    if (acordo.ativo == false)
                        $('td', nRow).addClass('item-desativado');
                    else
                        $('td', nRow).removeClass('item-desativado');
                }
            }
        });

        optsAcordo.dataTable = optsAcordo.acordoTable;
    });

    var optsAcordo = {
             urlDesativar : '${linkTo[AcordoController].desativar}?',
             urlReativar : '${linkTo[AcordoController].reativar}?',
             urlGravar : '${linkTo[AcordoController].gravarAcordo}',
             dialogCadastro : $('#editarAcordo_dialog'),
             tabelaRegistros : $('#acordo_table'),
             objectName : 'acordo',
             formCadastro : jQuery('#acordoForm'),
             mostrarDesativados : QueryString.mostrarDesativados,
             colunas : colunasAcordo.acoes
    };

    // Define a "classe" AcordoService
    function AcordoService(optsAcordo) {
        // super(opts)
        BaseService.call(this, optsAcordo);
    }

    // AcordoService extends BaseService
    AcordoService.prototype = Object.create(BaseService.prototype);
    
    var acordoService = new AcordoService(optsAcordo);
    
    acordoService.getId = function(acordo) {
    	return acordo.idAcordo || acordo['acordo.idAcordo'] || acordo['id'] || '';
    }

    acordoService.getRow = function(acordo) {
        return [acordo.nomeAcordo, acordo.descrAcordo, 'COLUNA_ACOES'];
    }
    
    acordoService.onRowClick = function(acordo) {
        acordoService.editar(acordo, 'Alterar Acordo');
    }

    function isValidForm() {
        return jQuery("#acordoForm").valid();
    }
    
    function gravarAplicar(baseService, isAplicar) {
        if (!baseService.isValidForm())
            return false;
        
        var obj = baseService.getObjetoParaGravar(),
            url = baseService.opts.urlGravar,
            wrapper = {},
            success = function(objSalvo) {
                if(baseService.onGravar) {
                    baseService.onGravar(obj, JSON.parse(objSalvo));
                }
                
                if (isAplicar) {
                    baseService.formularioHelper.populateFromJson(JSON.parse(objSalvo));
                    
                    alert("Cadastro salvo com sucesso.");
                }
                else
                    baseService.opts.dialogCadastro.dialog("close");
            }
            
        wrapper[baseService.opts.objectName] = obj;
        var params = jQuery.param(wrapper) + "&" + serializeParametrosAcordo();
        
        $.ajax({
            type: "POST",
            url: url,
            data: params,
            dataType: "text",
            success: success,
            error: baseService.errorHandler
        });
    }

    acordoService.onGravar = function(obj, objSalvo) {
        var row = BaseService.prototype.onGravar.call(this, obj, objSalvo);
        var tr = $(row);
        tr.attr('onclick','').unbind('click');
        tr.off('click');
        tr.attr('onclick', 'javascript:opener.retorna_${propriedade}("'+objSalvo.id+'","'+objSalvo.nomeAcordo+'","'+objSalvo.descrAcordo+'");window.close()');
    }
    
    /**
     * Customiza o metodo editar
     */
    acordoService.editar = function(obj, title) {
        BaseService.prototype.editar.call(this, obj, title); // super.editar();

        // Atualiza a lista de parÃ¢metros
        atualizarParametrosAcordo(obj);

        // carrega a AbrangÃªncias do Acordo
        carregarAbrangenciasAcordo(obj.id);
    }

    /**
     * Sobescreve o metodo cadastrar para limpar a tela.
     */
    acordoService.cadastrar = function(title) {
        // Atualiza a lista de parÃ¢metros
        atualizarParametrosAcordo();

        // carrega a AbrangÃªncias do Acordo
        carregarAbrangenciasAcordo();
        
        BaseService.prototype.cadastrar.call(this, title); // super.editar();
    }   

    acordoService.serializar = function(obj) {
        var query = BaseService.prototype.serializar.call(this, obj) + serializeParametrosAcordo();
        return query + "&acordo=" + this.getId(obj);
    }

    function carregarAbrangenciasAcordo(id) {

        if (id) {
            $.ajax({
                type: "GET",
                url: "${linkTo[AcordoController].buscarAbrangenciasAcordo}?id=" + id,
                dataType: "text",
                success: function(lista) {
                    var listaJSon = JSON.parse(lista);
                    acordoService.populateFromJSonList(listaJSon, associacaoTable);
                },
                error: function(error) {
                    alert("Não foi possível carregar as Abrangências deste Acordo.");
                }
            });
        }
        else {
            associacaoTable.clear();
        }
    }

    acordoService.populateFromJSonList = function(listaJSon, dataTable) {
        this.limparDadosAssociacoes();

        var table = associacaoTable.table;
        
        for (var i = 0; i < listaJSon.length; i++) {
            var abrangencia = listaJSon[i];
                row = [
                        '',                                                                    // colunas.botaoExpandir
                        abrangencia.orgaoUsuario ? abrangencia.orgaoUsuario.id : '',
                        abrangencia.orgaoUsuario ? abrangencia.orgaoUsuario.sigla : '', 
                        abrangencia.complexo ? abrangencia.complexo.id : '',
                        abrangencia.complexo ? abrangencia.complexo.descricao : '', 
                        abrangencia.solicitante ? abrangencia.solicitante.sigla : '',
                        abrangencia.solicitante ? abrangencia.solicitante.id : '',
                        abrangencia.solicitante ? abrangencia.solicitante.descricao : '',
                        abrangencia.solicitante ? abrangencia.solicitante.sigla : '',
                        abrangencia.atendente ? abrangencia.atendente.id : '',
                        abrangencia.atendente ? abrangencia.atendente.descricao : '',
                        abrangencia.atendente ? abrangencia.atendente.sigla : '',
                        abrangencia.prioridade ? abrangencia.prioridade : '',
                        abrangencia.descPrioridade ? abrangencia.descPrioridade : '',
                        abrangencia.idConfiguracao,                                             // colunas.idAssociacao
                        abrangencia.hisIdIni,                                                   // colunas.idAssociacao
                        'COLUNA_ACOES',                                                         // colunas.botaoExcluir
                        JSON.stringify(abrangencia)                                             // colunas.jSon
                    ];

            var tr = TableHelper.criarTd(row);
            
            acordoService.adicionarFuncionalidadesNaLinhaDeAssociacao(tr, abrangencia, row);
            table.append(tr);

            if (!abrangencia.ativo) {
                $('td', $(tr)).addClass('item-desativado');
            }    
            
        }
        acordoService.configurarAssociacaoDataTable();
    }

    acordoService.adicionarFuncionalidadesNaLinhaDeAssociacao = function(node, assoc, row) {
        node.data('json', assoc);
        node.data('json-id', assoc.idConfiguracao);
        node.attr('data-json', JSON.stringify(assoc));
        node.attr('data-json-id', assoc.idConfiguracao);
        node.find('td:first').addClass('details-control');

        var indiceAcoes = this.indiceAcoes(node),
            tdAcoes = node.find('td:nth(' + indiceAcoes + ')').addClass('acoes');
        tdAcoes.html(acordoService.conteudoColunaAcao(assoc));
        
        node.on('click', function() {
            var associacao = node.data('json');
            associacaoTable.table.find('.selected').removeClass('selected');
            node.addClass('selected');
            
            row[colunasAssociacao.jSon] = JSON.stringify(associacao);
            atualizarAssociacaoModal(row, associacao);
            associacaoModalAbrir(true);
        });
    }
    
    acordoService.configurarAssociacaoDataTable = function() {
        associacaoTable = new SigaTable('#associacao_table')
            .configurar("columnDefs", [{
                "targets": [colunasAssociacao.idOrgao, 
                            colunasAssociacao.idLocal, 
                            colunasAssociacao.tipoSolicitante, 
                            colunasAssociacao.idSolicitante, 
                            colunasAssociacao.descricaoSolicitante, 
                            colunasAssociacao.idAtendente, 
                            colunasAssociacao.idPrioridade, 
                            colunasAssociacao.descricaoAtendente, 
                            colunasAssociacao.idAssociacao,
                            colunasAssociacao.hisIdIni,
                            colunasAssociacao.jSon],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [colunasAssociacao.botaoExpandir],
                "sortable": false,
                "searchable" : false
            }])
            .configurar("fnRowCallback", associacaoRowCallback)
            .criar()
            .detalhes(detalhesListaAssociacao);
    }

    acordoService.limparDadosAssociacoes = function() {
        if(associacaoTable) {
            associacaoTable.destruir();
        }
    }
    
    acordoService.conteudoColunaAcao = function(abrangencia){
        if (abrangencia.ativo) {          
            return '<a class="once desassociar" onclick="desassociar(event, ' + abrangencia.idConfiguracao + ')" title="Remover permiss&atilde;o">' +
                    '<input class="idAssociacao" type="hidden" value="'+abrangencia.idConfiguracao+'"/>' +
                    '<img id="imgCancelar" src="/siga/css/famfamfam/icons/cancel_gray.png" style="margin-right: 5px;">' + 
                    '</a>'; 
        }
        return ' ';
    }   

    // Atualizando lista de ParÃ¢metros de Acordo
    function atualizarParametrosAcordo(acordo) {
        removerItensLista('parametrosAcordo');

        if (acordo) {
            for (var i = 0; i < acordo.parametroAcordoSet.length; i++) {
                var item = acordo.parametroAcordoSet[i];

                $("#parametrosAcordo")[0].incluirItem(item.atributo.idAtributo, item.atributo.nomeAtributo, item.operador, item.operadorNome, item.valor, item.unidadeMedida.idUnidadeMedida, item.unidadeMedidaPlural, item.idParametroAcordo);
            }
        }
    }

    function removerItensLista(nomeLista) {
        $("#"+nomeLista).find("li").each(function(i){
            this.remove();
            $("#"+nomeLista)[0]["index"]--;
        });
    }
</script>