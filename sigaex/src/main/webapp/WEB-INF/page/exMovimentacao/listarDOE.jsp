<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<siga:pagina titulo="Movimentação">

	<script type="text/javascript" language="Javascript1.1">
		function alterouMod(idMov) {
			frm.idMov.value = idMov;
			frm.method = "GET";
			frm.action = 'listarDOE';
			frm.submit();
			frm.method = "POST";
		}
		
		function checkUncheckAll(theElement) {
			var theForm = theElement.form, z = 0;
			for(z=0; z<theForm.length;z++) {
		    	if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall') {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				}
			}
		}
		
		function cancelarMov(id, sigla) {
			frm.id.value = id;
			frm.sigla.value = sigla;
			frm.action = '/sigaex/app/expediente/mov/cancelar_movimentacao_gravar';
			frm.submit();
		}
		
		function visualizarGrupo() {
			var aChk = document.getElementsByName("movSelecionados");
			var idsMov = "";
			var linha1 = document.getElementById("linha1").value;
			var linha2 = document.getElementById("linha2").value;
			
			for (var i=0;i<aChk.length;i++){
				 var item = aChk[i];
			     if (item.type == "checkbox" && item.checked) {
			     	idsMov += item.value + ";";
			     }
			}
			$.ajax({				     				  
				  url:'${pageContext.request.contextPath}/app/expediente/mov/visualizar_grupo',
				  type: "GET",
				  data: {ids : idsMov , linha1: linha1, linha2: linha2},
				  success: function(data) {
					  retornoVisualizarGrupo(data);
			 	 }
			});
		}
		
		function retornoVisualizarGrupo(response,param){
			sigaModal.enviarHTMLEAbrir('grupoModal', response);
		}
		
		function atualizarTipoAto() {
			var select = frm.idModelo;
			var option = select.children[select.selectedIndex];
			var texto = option.textContent;
			
			var primeiro = "";
			var aChk = document.getElementsByName("movSelecionados");
			
			for (var i=0;i<aChk.length;i++){
				 var item = aChk[i];
			     if (item.type == "checkbox" && item.checked) {
			     	primeiro = item.value;
			     	break;
			     }
			}
			
			if(texto == "Resolução" && primeiro != "") {
				var hoje = new Date;
				var mes = hoje.getMonth()+1;
				hojeFormatado = hoje.getDate() + "-" + mes + "-" + hoje.getFullYear();
				
				$.ajax({				     				  
					  url:'${pageContext.request.contextPath}/app/expediente/mov/atualizar_tipo_ato',
					  type: "GET",
					  data: {idMov : primeiro},
					  success: function(data) {
						document.getElementById("linha2").value = data;
						document.getElementById("linha1").value = "Resoluções de " +  hojeFormatado;
				 	 }
				});
			}
		}
		
		function validarCampos() {
			var idAnunciante = document.getElementsByName('idAnunciante')[0].value;
			var idMateria = document.getElementsByName('idMateria')[0].value;	
			var idCaderno = document.getElementsByName('idCaderno')[0].value;
			var idSecao = document.getElementsByName('idSecao')[0].value;
			
			if (idAnunciante==0) {									
				sigaModal.alerta("Atenção! O campo Anunciante precisa ser preenchido.");				
				document.getElementById('idAnunciante').focus();
				return;	
			}
			
			if (idMateria==0) {									
				sigaModal.alerta("Atenção! O campo Matéria precisa ser preenchido.");				
				document.getElementById('idMateria').focus();
				return;	
			}
			
			if (idCaderno==0) {									
				sigaModal.alerta("Atenção! O campo Caderno precisa ser preenchido.");				
				document.getElementById('idCaderno').focus();
				return;	
			}
			
			if (idSecao==0) {									
				sigaModal.alerta("Atenção! O campo Seção precisa ser preenchido.");				
				document.getElementById('idCaderno').focus();
				return;	
			}
			
			$('#myModal').modal('show');
		}		
	</script>
	
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Publicação Pendente ${tst}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="${request.contextPath}/app/expediente/mov/" method="post">
					<input type="hidden" name="idMov" value="" />
					<input type="hidden" name="id" value="" />
					<input type="hidden" name="sigla" value="" />
					<input type="hidden" name="urlRedirecionar" value="/app/exMovimentacao/listarDOE"/>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Tipo de Publicação</label>
								<select class="form-control siga-select2" id="idModelo" name="idModelo" onchange="javascrpt:alterouMod()">
									<option value="0">Selecione</option>
									<c:forEach items="${listModelos}" var="item">
										<option value="${item.id}" ${item.id == idModelo ? 'selected' : ''}>${item.nmMod}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-3" id="div1" style="display: none;">
							<div class="form-group">
								<label>Resolução/Data (1ª linha)</label>
								<input type="text" id="linha1" name="linha1" value="" maxlength="60" class="form-control"/>
							</div>
						</div>
						<div class="col-sm-3" id="div2" style="display: none;">
							<div class="form-group">
								<label>Tipo de Ato (2ª linha)</label>
								<input type="text" id="linha2" name="linha2" value="" maxlength="60" class="form-control"/>
							</div>
						</div>
					</div>
					<c:if test="${(not empty listMov)}">
						<table border="0" class="table table-sm table-striped">
							<thead class="${thead_color}">
								<tr>
									<th class="text-center align-middle" style="width: 2%;"><input type="checkbox" name="checkall" onclick="checkUncheckAll(this)" /></th>
									<th class="text-left" style="width: 25%;">Número</th>
									<th class="text-left" style="width: 10%;">Data</th>
									<th class="text-left" style="width: 55%;"></th>
									<th class="text-right" style="width: 10%;"></th>	
								</tr>
								
							</thead>
				
						    <c:forEach var="mov" items="${listMov}"> 
						    	<c:url var="urlAlterar" value="/app/expediente/mov/agendar_publicacao_doe">
									<c:param name="id" value="${mov.idMov}"></c:param>
									<c:param name="sigla" value="${mov.exMobil.exDocumento.sigla}"></c:param>
									<c:param name="urlRedirecionar" value="/app/exMovimentacao/listarDOE"/>
								</c:url>
							
						    	<tr class="even">
									 <td class="text-center align-middle"><input type="checkbox" name="movSelecionados" id="${x}" value="${mov.idMov}" ${x_checked} onclick="atualizarTipoAto(this)"/></td>
     			        			 <td class="text-left align-middle">
     			        			 	<span data-toggle="tooltip" data-placement="bottom" title="${mov.exMobil.exDocumento.descrDocumento}">
     			        			 		<a href="/sigaex/app/arquivo/exibir?id=${mov.idMov}&arquivo=${mov.exMobil.exDocumento.sigla}:${mov.idMov}&sigla=${mov.exMobil.exDocumento.sigla}">${mov.exMobil.exDocumento.sigla}.txt</a>
     			        			 	</span>
     			        			 </td>
     			        			 <td class="text-left align-middle"><fmt:formatDate pattern = "dd/MM/yyyy" value="${mov.dtIniMov}"/></td>
     			        			 <th class="text-left" style="width: 65%;">
     			        			 	<button type="button" class="btn up" data-toggle="tooltip" data-placement="top">
											<i class="fas fa-caret-up"></i>
										</button>
										<button type="button" class="btn down" data-toggle="tooltip" data-placement="top">
											<i class="fas fa-caret-down"></i>
										</button>
     			        			 </th>
     			        			 <td class="text-right">
     			        			 <div class="btn-group">
     			        			 	<a href="javascript:cancelarMov(${mov.idMov}, '${mov.exMobil.exDocumento.sigla}')" onclick="cancelarMov(${mov.idMov}, '${mov.exMobil.exDocumento.sigla}')" class="btn btn-primary" role="button" aria-pressed="true" data-siga-modal-abrir="confirmacaoModal" style="min-width: 80px;">Cancelar</a>
										<button type="button" class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										    <span class="sr-only"></span>
									    </button>
												  
									  <div class="dropdown-menu">						  
									  	<a href="${urlAlterar}" class="dropdown-item" role="button" aria-pressed="true">Alterar</a>	
									  </div>
    			        			 
     			        			 </div>
     			        			 </td>
						    	</tr>
						    </c:forEach>
						   
						</table>
						<div class="form-group row">
							<div class="col-sm-3">
								<div class="form-group">
									<label>Anunciante</label>
									<select class="form-control siga-select2" id="idAnunciante" name="idAnunciante">
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Tipo de Matéria</label>
									<select class="form-control siga-select2" id="idMateria" name="idMateria">
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Caderno</label>
									<select class="form-control siga-select2" id="idCaderno" name="idCaderno">
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Seção</label>
									<select class="form-control siga-select2" id="idSecao" name="idSecao">
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-sm">
								<input type="button" value="Enviar Arquivos Selecionados" class="btn btn-primary" onclick="javascript:validarCampos();"/>
								<input type="button" value="Visualizar Grupo de Arquivos" class="btn btn-primary" onclick="javascript:visualizarGrupo();"/>
							</div>				
						</div>
						</div>
					</c:if>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					  <div class="modal-dialog">
					    <div class="modal-content">
					    	<div class="modal-header"><div class="col-6  p-0"><img src="${uri_logo_siga_pequeno}" class="siga-modal__logo" alt="logo siga"></div>			
								<button type="button" class="close  p-0  m-0  siga-modal__btn-close" data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>'
							</div>
					    <div class="modal-body">
					        <div class="form-group">
							<label for="siglaLotacao">Usuário</label>
							<input type="text" id="usuario" name="usuario" class="form-control"/>	
						</div>
						<div class="form-group">
							<label for="siglaLotacao">Senha</label>
							<input type="password" id="senha" name="senha" class="form-control"/>
						</div>

					      </div>
					      <div class="modal-footer">
					      	<button type="button" class="btn btn-primary">OK</button>
					        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					      </div>
					    </div>
					  </div>
					</div>
				</form>
			</div>
		</div>
		<siga:siga-modal id="grupoModal" tamanhoGrande="true" exibirRodape="true" descricaoBotaoFechaModalDoRodape="Voltar">
			<div class="modal-body"></div>			
		</siga:siga-modal>	
	</div>
</siga:pagina>

<script>
	window.onload = function () { 
		var select = frm.idModelo;
		var option = select.children[select.selectedIndex];
		var texto = option.textContent;
		if(texto == "Resolução") {
			document.getElementById("div1").style.display = "block";
		    document.getElementById("div2").style.display = "block";
		}
	} 
	
	$(document).ready(function() {
	   $(".up,.down").click(function() {
	      var row = $(this).parents("tr:first");
	      if ($(this).is(".up")) {
	         row.insertBefore(row.prev());
	      } else if ($(this).is(".down")) {
	         row.insertAfter(row.next());
	      }
	      atualizarTipoAto();
	   });
	});
</script>