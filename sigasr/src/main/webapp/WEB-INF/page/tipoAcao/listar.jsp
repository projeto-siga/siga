<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Ações">

	<jsp:include page="../main.jsp"></jsp:include>

	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>
	
	<div class="container-fluid mb-2">
		<h2>Tipos de A&ccedil;&atilde;o</h2>
		
		<div class="card card-body mb-2">
			<label>
				<siga:checkbox name="mostrarDesativados" value="${mostrarDesativados}"></siga:checkbox>
				<b>Incluir Inativas</b>
			</label>
			<div class="table-responsive">
				<table id="tiposAcao_table" class="table">
					<thead>
						<tr>
							<th>C&oacute;digo</th>
							<th>T&iacute;tulo</th>
							<th>Descri&ccedil;&atilde;o</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${tiposAcao}" var="tipoAcao">
							<tr data-json-id="${tipoAcao.idTipoAcao}" data-json='${tipoAcao.toJson()}' onclick="tipoAcaoService.editar($(this).data('json'), 'Alterar Tipo de A&ccedil;&atilde;o')" style="cursor: pointer;">
								<td>${tipoAcao.siglaTipoAcao}</td>
								<td>
									<span style="margin-left: ${(tipoAcao.nivel-1)*2}em; ${tipoAcao.nivel == 1 ? 'font-weight: bold;' : ''}">
										<siga:selecionado sigla="${tipoAcao.atual.tituloTipoAcao}" descricao="${tipoAcao.atual.descricao}"></siga:selecionado>
									</span>
								</td>
								<td>${tipoAcao.descrTipoAcao}</td>
								<td class="acoes">
									<sigasr:desativarReativar id="${tipoAcao.idTipoAcao}" onReativar="tipoAcaoService.reativar" onDesativar="tipoAcaoService.desativar" isAtivo="${tipoAcao.isAtivo()}"></sigasr:desativarReativar>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<a onclick="tipoAcaoService.cadastrar('Incluir Tipo de A&ccedil;&atilde;o')" class="btn btn-primary" style="color: #fff">Incluir</a>
	</div>
	
	<sigasr:modal nome="tipoAcao" titulo="Cadastrar Tipo de A&ccedil;&atilde;o" largura="80%">
		<div id="divEditarTipoAcaoForm"><jsp:include page="editar.jsp"></jsp:include></div>
	</sigasr:modal>
	
</siga:pagina>
	
<script type="text/javascript">
	var validatorTipoAcaoForm;
	var colunasTipoAcao = {
			codigo: 0,
			titulo: 1,
			decricao: 2,
			acoes: 3
		};
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

	var opts = {
			 urlDesativar : '${linkTo[TipoAcaoController].desativar}',
			 urlReativar : '${linkTo[TipoAcaoController].reativar}',
			 urlGravar : '${linkTo[TipoAcaoController].gravar}',
			 dialogCadastro : $('#tipoAcao_dialog'),
			 tabelaRegistros : $('#tiposAcao_table'),
			 objectName : 'tipoAcao',
			 formCadastro : $('#tipoAcaoForm'),
			 mostrarDesativados : $('#checkmostrarDesativados').attr('checked') ? true : false, //QueryString.mostrarDesativados,
			 colunas : colunasTipoAcao.acoes
	};
	
	$(document).ready(function() {
		$("#checkmostrarDesativados").click(function() {
			jQuery.blockUI(objBlock);

			if (document.getElementById('checkmostrarDesativados').checked)
				location.href = '${linkTo[TipoAcaoController].listar}' + '?mostrarDesativados=true';
			else
				location.href = '${linkTo[TipoAcaoController].listar}' + '?mostrarDesativados=false';
		});
		
		/* Table initialization */
		opts.dataTable = $('#tiposAcao_table').dataTable({
			stateSave : true,
			"language": {
				"emptyTable":     "N&atilde;o existem resultados",
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
			        "last":       "&Uacute;ltimo",
			        "next":       "Pr&oacute;ximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordena&ccedil;&atilde;o crescente",
			        "sortDescending": ": clique para ordena&ccedil;&atilde;o decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [colunasTipoAcao.acoes],
				"searchable": false,
				"sortable" : false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
                var tipoAcao = undefined;

                try {
                	tipoAcao = JSON.parse($(nRow).data('json'));
                }
                catch(err) {
                	tipoAcao = $(nRow).data('json');
                }

                if (tipoAcao) {
                    if (tipoAcao.ativo == false)
                        $('td', nRow).addClass('item-desativado');
                    else
                        $('td', nRow).removeClass('item-desativado');
                }
            }
		});
	});

	// Define a "classe" AcaoService
	function TipoAcaoService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// AcaoService extends BaseService
	TipoAcaoService.prototype = Object.create(BaseService.prototype);
	
	var tipoAcaoService = new TipoAcaoService(opts);
	
	tipoAcaoService.getId = function(tipoAcao) {
		return tipoAcao.idTipoAcao || tipoAcao['tipoAcao.idTipoAcao'];
	}

	tipoAcaoService.serializar = function(obj) {
        var serializado = BaseService.prototype.serializar.call(this, obj);
        return serializado + "&tipoAcao=" + this.getId(obj);
    }

	tipoAcaoService.getRow = function(tipoAcao) {
		var marginLeft = (tipoAcao.nivel-1) * 2,
			fontWeight = (tipoAcao.nivel == 1) ? 'bold' : 'normal',
			span = $('<span></span>');

		var spanHTML = '<span style="margin-left:{margin-left};font-weight:{font-weight}">{descricao}</span>';
		spanHTML = spanHTML.replace('{margin-left}', marginLeft + 'em');
		spanHTML = spanHTML.replace('{font-weight}', fontWeight);
		spanHTML = spanHTML.replace('{descricao}', tipoAcao.tituloTipoAcao);

		var descrTipoAcao = tipoAcao.descrTipoAcao == undefined || tipoAcao.descrTipoAcao == null ? "" : tipoAcao.descrTipoAcao;
	
		return [tipoAcao.siglaTipoAcao, spanHTML, descrTipoAcao, 'COLUNA_ACOES'];
	}
	tipoAcaoService.onRowClick = function(tipoAcao) {
		tipoAcaoService.editar(tipoAcao, 'Alterar Tipo de A&ccedil;&atilde;o');
	}
</script>