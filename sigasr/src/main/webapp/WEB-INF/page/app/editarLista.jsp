<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Cadastro de Lista">

<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>

<style>
	tr {
		cursor : pointer !important;
	}
</style>

<script language="javascript">
	var prioridadesTable, 
		isEditing;
	
	//removendo a referencia de '$' para o jQuery
	$.noConflict();
	
	jQuery( document ).ready(function( $ ) {
		/* Table initialization */
		prioridadesTable = $('#prioridades_table').DataTable({
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
			        "last":       "Último",
			        "next":       "Próximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordenação crescente",
			        "sortDescending": ": clique para ordenação decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [0, 2, 4, 5, 7, 8, 10, 12, 13, 15, 16, 17, 18],
				"visible": false,
				"searchable": false
			},
			{
				"targets": [20],
				"sortable" : false,
				"searchable": false
			}
			]
		});
		
		$('#prioridades_table tbody').on('click" 'tr" function () {
			if (prioridadesTable.row(this).data() != undefined) {
	        	prioridadesTable.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	
				atualizarControleAcessoModal(prioridadesTable.row(this).data());
			    controleAcessoModalAbrir(true);
			}
		});
	});

	function desativarPermissaoUsoListaEdicao(event, idPermissaoDesativar) {
		event.stopPropagation();
		
		var me = $(this),
			idPermissao = idPermissaoDesativar ? idPermissaoDesativar : me.find('.idPermissao').val(),
			idLista = $("#idLista").val();

		location.href='@{Application.desativarPermissaoUsoListaEdicao()}?' + jQuery.param({idLista : idLista, idPermissao : idPermissao});
	}
	
	function atualizarControleAcessoModal(itemArray) {
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		
		jOrgaoUsuarioCbb.selectedIndex = findSelectedIndexByValue(jOrgaoUsuarioCbb, itemArray[0]);
		jComplexoCbb.selectedIndex = findSelectedIndexByValue(jComplexoCbb, itemArray[2]);
		$("#lotacao").val(itemArray[4]);
		$("#lotacao_descricao").val(itemArray[5]);
		$("#lotacaoSpan").html(itemArray[5]);
		$("#lotacao_sigla").val(itemArray[6]);
		$("#dpPessoa").val(itemArray[7]);
		$("#dpPessoa_descricao").val(itemArray[8]);
		$("#dpPessoaSpan").html(itemArray[8]);
		$("#dpPessoa_sigla").val(itemArray[9]);
		$("#cargo").val(itemArray[10]);
		$("#cargo_descricao").val(itemArray[11]);
		$("#cargoSpan").html(itemArray[11]);
		$("#cargo_sigla").val(itemArray[12]);
		$("#funcaoConfianca").val(itemArray[13]);
		$("#funcaoConfianca_descricao").val(itemArray[14]);
		$("#funcaoConfiancaSpan").html(itemArray[14]);
		$("#funcaoConfianca_sigla").val(itemArray[15]);
		atualizarListasTipoPermissaoEdicao(itemArray);
		$("#idConfiguracao").val(itemArray[17]);
	};
	
	function findSelectedIndexByValue(comboBox, value) {
		for (var i = 0; i < comboBox.options.length; i++) {
			if (comboBox.options[i].value == value)
				return i;
		}
		
		return 0;
	};
	
	function controleAcessoModalAbrir(isEdicao){
		isEditing = isEdicao;
		
		if (isEdicao)
			$("#controleAcesso_dialog").dialog('option" 'title" 'Alterar Permissão');
		else
			$("#controleAcesso_dialog").dialog('option" 'title" 'Incluir Permissão');

		if ($("#idLista").val() != undefined)
			$("#controleAcesso_dialog").dialog('open');

	}
	
	$("#controleAcesso_dialog").dialog({
	    autoOpen: false,
	    height: 'auto"
	    width: 'auto"
	    modal: true,
	    resizable: false
	  });
	
	function controleAcessoModalFechar() {
		isEditing = false;
		$("#controleAcesso_dialog").dialog("close");
		
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
		jComplexoCbb = document.getElementsByName("complexo")[0];
		
		jOrgaoUsuarioCbb.selectedIndex = 0;
		jComplexoCbb.selectedIndex = 0;
		$("#lotacao").val('');
		$("#lotacao_descricao").val('');
		$("#lotacao_sigla").val('');
		$("#lotacaoSpan").html('');
		$("#dpPessoa").val('');
		$("#dpPessoa_descricao").val('');
		$("#dpPessoa_sigla").val('');
		$("#dpPessoaSpan").html('');
		$("#cargo").val('');
		$("#cargo_descricao").val('');
		$("#cargo_sigla").val('');
		$("#cargoSpan").html('');
		$("#funcaoConfianca").val('');
		$("#funcaoConfianca_descricao").val('');
		$("#funcaoConfiancaSpan").html('');
		$("#funcaoConfianca_sigla").val('');
		removerItensListaTipoPermissao();
		$("#idConfiguracao").val('');

		$('#modal-permissao-error').css('display" 'none');
		$('#modal-permissao').css('display" 'block');
	};

	function serializePermissao(row) {
		var params = "";
		
		// caso exista algum item na tabela
		if (row[0] != '' && row[0] > 0)
			params += '&permissao.orgaoUsuario.idOrgaoUsu=' + row[0];
		
		if (row[2] != '' && row[2] > 0)
        	params += '&permissao.complexo.idComplexo=' + row[2];
		
		if (row[4] != '')
        	params += '&permissao.lotacao.id=' + row[4];
		
		if (row[7] != '')
        	params += '&permissao.dpPessoa.id=' + row[7];
		
		if (row[10] != '')
        	params += '&permissao.cargo.id=' + row[10];
		
		if (row[13] != '')
        	params += '&permissao.funcaoConfianca.idFuncao=' + row[13];
		
		if (row[17] != '')
			params += '&permissao.id=' + row[17];

		if ($("#idLista").val() != undefined && $("#idLista").val() != '')
			params += '&permissao.listaPrioridade.id=' + $("#idLista").val();

		// lista de TipoPermissao
		params += row[18];

		return params;
	};
	
	function inserirAcesso() {
		var jOrgaoUsuarioCbb = document.getElementsByName("orgaoUsuario")[0],
			jOrgaoUsuario = jOrgaoUsuarioCbb.options[jOrgaoUsuarioCbb.selectedIndex],
			jComplexoCbb = document.getElementsByName("complexo")[0],
			jComplexo = jComplexoCbb.options[jComplexoCbb.selectedIndex];
		
		var row = [ jOrgaoUsuario.value,
          			jOrgaoUsuario.text, 
          			jComplexo.value,
          			jComplexo.text,
          			$("#lotacao").val(),
          			$("#lotacao_descricao").val(),
          			$("#lotacao_sigla").val(),
          			$("#dpPessoa").val(),
          			$("#dpPessoa_descricao").val(),
          			$("#dpPessoa_sigla").val(),
          			$("#cargo").val(),
          			$("#cargo_descricao").val(),
          			$("#cargo_sigla").val(),
          			$("#funcaoConfianca").val(),
          			$("#funcaoConfianca_descricao").val(),
          			$("#funcaoConfianca_sigla").val(),
          			atualizaTipoPermissaoJson(),			//16
          			$("#idConfiguracao").val(),				//17    
          			getListasAsString(),					//18
          			getDescricaoTipoPermissao()				//19	
          			];

		$.ajax({
	         type: "POST",
	         url: "@{Application.gravarPermissaoUsoLista()}",
	         data: serializePermissao(row),
	         dataType: "text",
	         success: function(response) {
		        row[17] = response;
				var html = 
				'<td class="gt-celula-nowrap" style="cursor:pointer; font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">' + 
					'<a class="once desassociarPermissao" title="Remover permissão">' + 
						'<input class="idPermissao" type="hidden" value="' + response + '"/>' + 
						'<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">' + 
					'</a>' + 
				'</td>';
						         
		        row[20] = html;

	          	if (isEditing) {
	          		prioridadesTable.row('.selected').data(row);
			    }
			    else {
			    	prioridadesTable.row.add(row).draw();
			    }
				controleAcessoModalFechar();
				$('.desassociarPermissao').bind('click" desativarPermissaoUsoListaEdicao);
	         },
	         error: function(response) {
	        	$('#modal-permissao').hide(); 

	        	var modalErro = $('#modal-permissao-error');
	        	modalErro.find("h3").html(response.responseText);
	        	modalErro.show(); 
	         }
        });
	};

	function atualizarListasTipoPermissaoEdicao(itemArray) {
		var tipoPermissaoJson;

		// transforma o valor String em objeto, caso necessário
		// Edson: comentando, pois nao funciona no IE
		try{

			designacaoJson = JSON.parse(itemArray[16]);
		} catch(e){
			designacaoJson = itemArray[16];
		}
		
		if (designacaoJson) {
			
			if (designacaoJson.listaTipoPermissaoListaVO) {
				// cria os itens da lista de TipoPermissao, e adiciona na tela
				for (i = 0; i < designacaoJson.listaTipoPermissaoListaVO.length; i++) {
					var lista = designacaoJson.listaTipoPermissaoListaVO[i];
					$("#permissoes")[0].incluirItem(lista.idTipoPermissaoLista, lista.descrTipoPermissaoLista, "");
				}
			}
		}
	};

	function getDescricaoTipoPermissao() {
		var descricao = $('#permissoes').find("li:first-child").find("span:eq(0)").html();
		
		if (descricao != null) {
			return descricao + " ...";
		}
		return descricao;
	}

	function removerItensListaTipoPermissao() {
    	$("#permissoes").find("li").each(function(i){
            $(this).remove();
            $("#permissoes")[0]["index"]--;
        });
    };

	function atualizaTipoPermissaoJson() {
		var listaTipoPermissaoListaVO = [];
    	
		// Percorre listas de TipoPermissao
    	$("#permissoes").find("li").each(function(i){
            var jDivs=$(this).find("span");
            listaTipoPermissaoListaVO.push({
            	"idTipoPermissaoLista": jDivs[0].id,
            	"descrTipoPermissaoLista": jDivs[0].innerHTML
            });
        });

    	var tipoPermissaoJson = {
   			"listaTipoPermissaoListaVO": listaTipoPermissaoListaVO
   		};
   		
   		return tipoPermissaoJson;
   	}

    function getListasAsString() {
		var params = '';
	
		// Percorre listas de TipoPermissao
		$("#permissoes").find("li").each(function(i){
	        var jDivs=$(this).find("span");
	        
	        // Atualiza a string serializada
        	params += '&permissao.tipoPermissaoSet[' + i + '].idTipoPermissaoLista=' + jDivs[0].id;
	    });
		return params;
    };
	
	$(function() {
		
		var jPermissoes = $("#permissoes"),
		permissoes = jPermissoes[0],
        jDialog = $("#dialog"),
        dialog = jDialog[0],
        jTipoPermissao = $("#tipoPermissao"),
		jTipoPermissaoSelecionada = $("#itemTipoPermissao"),
		listaTipoPermissaoAux;
       
        $( "#permissoes" ).sortable({placeholder: "ui-state-highlight"});
        $( "#permissoes" ).disableSelection();
       
        $("#botaoIncluir").click(function(){
                jDialog.data('acao"permissoes.incluirItem).dialog('open');
        });
       
        jDialog.dialog({
                autoOpen: false,
                height: 'auto"
                width: 'auto"
                modal: true,
                resizable: false,
                close: function() {
                        jDialog.data('tipoPermissao"'');
                },
                open: function(){
                        jDialog.dialog('option" 'title" 'Incluir Tipo de Permissão');                  
                        jTipoPermissao.find("option[value=" + jDialog.data("tipoPermissao") + "]").prop('selected" true);
                }
        });
        $("#modalOk").click(function(){
                var acao = jDialog.data('acao');
                var jTipoPermissaoEscolhido = jTipoPermissaoSelecionada.find("option:selected");
                acao(jTipoPermissaoEscolhido.val(), jTipoPermissaoEscolhido.text(), jDialog.data("id"));
                jTipoPermissaoSelecionada[0].remove(jTipoPermissaoSelecionada[0].selectedIndex);
                jDialog.dialog('close');
        });
        $("#modalCancel").click(function(){
                jDialog.dialog('close');
        });

        permissoes["index"] = 0;
        permissoes.incluirItem = function(idTipo, descrTipo, id){
                if (!id)
                        id = 'novo_' + ++permissoes["index"];
                jPermissoes.append("<li style=\"cursor: move\" id =\"" + id + "\"></li>");
                var jNewTr = jPermissoes.find("li:last-child");
                jNewTr.append("<span style=\"display: inline-block\" id=\"" + idTipo + "\">"
                                + descrTipo + "</span>");
                jNewTr.append("&nbsp;&nbsp;<img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: hidden; cursor: pointer\" />");
                jNewTr.find("img:eq(0)").click(function(){
                	permissoes.removerItem(jNewTr.attr("id"));
                });
                jNewTr.mouseover(function(){
                        jNewTr.find("img").css("visibility", "visible");
                });
                jNewTr.mouseout(function(){
                        jNewTr.find("img").css("visibility", "hidden");
                });
        }
       
        permissoes.removerItem = function(idItem){
                $("#"+idItem).remove();
                permissoes["index"]--;
        }

		$("[value='Gravar']").click(function(){
			if (!block())
				return false;
			var jForm = $("form"),
            	params = jForm.serialize();
			
            location.href = '@{Application.gravarLista()}?' + params;
		});

		$("[title='close']").click(function(){
			controleAcessoModalFechar();
		});
	});
</script>

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Cadastro de Lista</h2>
		<div class="gt-form gt-content-box">
			<form id="formLista" enctype="multipart/form-data"> 
				<c:if test="${lista?.idLista}"> 
					<input type="hidden" id="idLista" name="lista.idLista" value="${lista.idLista}">
				</c:if>
				<div class="gt-form-row gt-width-66">
					<label>Nome</label> <input type="text"
						name="lista.nomeLista"
						value="${lista?.nomeLista}" size="98"/>
				</div>
				<div class="gt-form-row gt-width-66">
					<label>Abrangência</label>
					<textarea cols="100" rows="5" name="lista.descrAbrangencia"
						id="descrAbrangencia" maxlength="8192">${lista.descrAbrangencia}</textarea>
					<span style="color: red"><sigasr:error
						nome="lista.descrAbrangencia" /></span>
				</div>
				<div class="gt-form-row gt-width-66">
					<label>Justificativa</label>
					<textarea cols="100" rows="5" name="lista.descrJustificativa"
						id="descrJustificativa" maxlength="8192">${lista.descrJustificativa}</textarea>
					<span style="color: red"><sigasr:error
						nome="lista.descrJustificativa" /></span>
				</div>
				<div class="gt-form-row gt-width-66">
					<label>Priorização</label>
					<textarea cols="100" rows="5" name="lista.descrPriorizacao"
						id="descrPriorizacao" maxlength="8192">${lista.descrPriorizacao}</textarea>
					<span style="color: red"><sigasr:error
						nome="lista.descrPriorizacao" /></span>
				</div>
				
				<div class="container">
					<div class="title-table">
						<h3 style="padding-top: 25px;">Permissões</h3>
					</div>
				</div>
				<div class="gt-content-box gt-for-table dataTables_div">
					<table id="prioridades_table" border="0" class="gt-table display">
						<thead>
							<tr>
								<th>ID Orgão</th>
								<th>Orgão</th>
								<th>ID Local</th>
								<th>Local</th>
								<th>ID Lotação</th>
								<th>Nome Lotação</th>
								<th>Lotação</th>
								<th>ID Pessoa</th>
								<th>Nome Pessoa</th>
								<th>Pessoa</th>
								<th>ID Cargo</th>
								<th>Cargo</th>
								<th>Cargo</th>
								<th>ID Função</th>
								<th>Função</th>
								<th>Função</th>
								<th>Tipo Permissão</th>
								<th>ID Tipo Permissao</th>
								<th>Tipo Permissão</th>	
								<th>Tipo Permissão</th>	
								<th>Ações</th>
							</tr>
						</thead>
		
						<tbody>
							<c:forEach var="perm" items="${permissoes}" >
								<tr style="cursor: pointer;">
									<td>${perm.orgaoUsuario?.id}</td>
									<td>${perm.orgaoUsuario?.acronimoOrgaoUsu}</td>
									<td>${perm.complexo?.idComplexo}</td>
									<td>${perm.complexo?.nomeComplexo}</td>
									<td>${perm.lotacao?.lotacaoAtual?.id}</td>
									<td>${perm.lotacao?.lotacaoAtual?.nomeLotacao}</td>
									<td>${perm.lotacao?.lotacaoAtual?.siglaLotacao}</td>
									<td>${perm.dpPessoa?.pessoaAtual?.id}</td>
									<td>${perm.dpPessoa?.pessoaAtual?.nomePessoa}</td>
									<td>${perm.dpPessoa?.pessoaAtual?.nomeAbreviado}</td>
									<td>${perm.cargo?.id}</td>
									<td>${perm.cargo?.sigla}</td>
									<td>${perm.cargo?.descricao}</td>
									<td>${perm.funcaoConfianca?.id}</td>
									<td>${perm.funcaoConfianca?.sigla}</td>
									<td>${perm.funcaoConfianca?.descricao}</td>
									<td>${perm.getSrConfiguracaoTipoPermissaoJson()}</td>
									<td>${perm.id}</td>
									<td></td>
									<td>${perm.descrTipoPermissao}</td>
									<td class="gt-celula-nowrap" style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
										<a class="once desassociarPermissao" onclick="desativarPermissaoUsoListaEdicao(event, ${perm.id})" title="Remover permissão">
											<input class="idPermissao" type="hidden" value="${perm.id}"/>
											<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"> 
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="gt-table-buttons">
							<a href="javascript: controleAcessoModalAbrir(false)" class="gt-btn-medium gt-btn-left">Incluir</a>
						</div>
				
				<div class="gt-form-row">
					<input type="hidden" name="lotaTitular" value="${lotaTitular.idLotacao}">
					<input type="hidden" name="lista.lotaCadastrante" value="${lotaTitular.idLotacao}">
					<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left"  />
					<c:choose>
					<c:when test="${lista?.idLista}"><a href="@{Application.exibirLista(lista.idLista)}" class="gt-btn-medium gt-btn-left">Cancelar</a></c:when>
					<c:otherwise><a href="@{Application.listarlista}" class="gt-btn-medium gt-btn-left">Cancelar</a></c:otherwise>
					</c:choose>
				</div>
			</form>
		</div>
	</div>
</div>



<sigasr:modal nome="controleAcesso" titulo="Cadastrar Permissão">
	<div class="gt-form gt-content-box" id="modal-permissao">
		<div>
			<input id="idConfiguracao" type="hidden" name="idConfiguracao"> 
			
			<c:if test="${not empty errors}">
				<p class="gt-error">Alguns campos obrigatórios não foram preenchidos ${error}</p>
			</c:if>
			<div class="gt-form-row div-modal-table">
				<label>Órgão</label> 
				<sigasr:select name="orgaoUsuario" items="${orgaos}" valueProperty="idOrgaoUsu"
				labelProperty="nmOrgaoUsu" value="${orgaoUsuario?.idOrgaoUsu}">
				<sigasr:opcao valor="0"> Nenhum</sigasr:opcao> 
				</sigasr:select>
			</div>
			<span style="color: red"><sigasr:error
					nome="orgaoUsuario" /></span>
			<div class="gt-form-row div-modal-table">		
				<label>Local</label> 
				<sigasr:select name="complexo" items="${locais}" valueProperty="idComplexo"
				labelProperty="nomeComplexo" value="${complexo?.idComplexo}">
				<sigasr:opcao valor="0">Nenhum</sigasr:opcao> 
				</sigasr:select>
			</div>		
			<div class="gt-form-row div-modal-table">
				<label>Lotação</label><sigasr:selecao
					tipo="lotacao" nome="lotacao"
					value="${lotacao?.lotacaoAtual}"" />
			</div>
			
			<div class="gt-form-row div-modal-table">
				<label>Pessoa</label><sigasr:selecao
					tipo="pessoa" nome="dpPessoa"
					value="${dpPessoa?.pessoaAtual}" />
			</div>
			
			<div class="gt-form-row div-modal-table">
				<label>Cargo</label><sigasr:selecao
					tipo="cargo" nome="cargo"
					value:"${cargo} />
			</div>
			
			<div class="gt-form-row div-modal-table">
				<label>Função</label><sigasr:selecao
					tipo="funcao" nome="funcaoConfianca"
					value="${funcaoConfianca}" />
			</div>
			
			<div class="gt-form-row div-modal-table">
				<label>Tipo Permissão</label>
					<ul id="permissoes" style="color: #365b6d"></ul>
					<input type="button" value="Incluir" id="botaoIncluir"
		                      class="gt-btn-small gt-btn-left" style="font-size: 10px;" />
				</div>
			</div>
			
			<div class="gt-form-row">
				<a href="javascript: inserirAcesso()" class="gt-btn-medium gt-btn-left">Gravar</a>
				<a href="javascript: controleAcessoModalFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
				<c:if test="${idConfiguracao}"> 
					<input type="button" value="Desativar"
						class="gt-btn-medium gt-btn-left"
						onclick="location.href='@{Application.desativarPermissaoUsoLista()}?id=${idConfiguracao}'" />
				</c:if>
			</div>
		</div>
	</div>
	<div id="dialog">
		<div class="gt-content">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row">
					<div class="gt-form-row gt-width-66">
						<label>Tipo de Permissão</label> <sigasr:select name="itemTipoPermissao" id="itemTipoPermissao">
							 <c:forEach items="${tiposPermissao}" var="item"> <sigasr:opcao nome="item.idTipoPermissaoLista"> ${item.descrTipoPermissaoLista} </sigasr:opcao> </c:forEach> </sigasr:select>
					</div>
					<div class="gt-form-row">
						<input type="button" id="modalOk" value="Ok"
	                           class="gt-btn-medium gt-btn-left" />
						<input type="button" value="Cancelar" id="modalCancel" class="gt-btn-medium gt-btn-left" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="gt-content-box" id="modal-permissao-error" style="display: none;">
		<table width="100%">
			<tr>
				<td align="center" valign="middle">
					<table class="form" width="50%">
						<tr>
							<td style="text-align: center; padding-top: 10px;">
								<h3></h3>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</sigasr:modal>