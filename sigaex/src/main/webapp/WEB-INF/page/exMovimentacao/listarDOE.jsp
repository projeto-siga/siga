<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<siga:pagina titulo="Movimentação">
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/sdkdoe/base64js.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/sdkdoe/text-encoder-lite.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/sdkdoe/sdk-desktop.js"></script>
	
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
			$('#confirmacaoModalCancelar').modal('show');
		}
		
		function confirmarCancelarMov() {
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
			
			if(texto.indexOf("Resolução") != -1 && primeiro != "") {
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
			} else {
				document.getElementById("linha2").value = "";
				document.getElementById("linha1").value = "";
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
			
			if (idMateria==0) {									
				sigaModal.alerta("Atenção! O campo Tipo de Matéria precisa ser preenchido.");				
				document.getElementById('idMateria').focus();
				return;	
			}
			
			var aChk = document.getElementsByName("movSelecionados");
			var boolArquivo = true;
			for (var i=0;i<aChk.length;i++){
				 var item = aChk[i];
			    if (item.type == "checkbox" && item.checked) {
			    	boolArquivo = false;
					break;
			    }
			}

			if (boolArquivo) {									
				sigaModal.alerta("Atenção! Selecione pelo menos um arquivo.");
				return;	
			}
			$('#confirmacaoModal').modal('show');
		}
		
		function abrirLogin() {
			$('#confirmacaoModal').modal('hide');
		}
		
		function carregarAnunciante(lista) {
			var jaExiste = false;
			var selectAnunciante = document.getElementById("idAnunciante");
			
			selectAnunciante.innerHTML = "";
			selectAnunciante.add(new Option("Selecione", "0"));
			
			if(lista != null) {
				for (var i = 0; i < lista.length; i++) {
					jaExiste = false;
					for (a = 0; a < selectAnunciante.length; a = a + 1) {
						if(selectAnunciante.options[a].value == lista[i].anuncianteId) {
							jaExiste = true;
					    	break;
						}
					}
					if(!jaExiste) {
					    var option = new Option(lista[i].anuncianteRazaoSocial, lista[i].anuncianteId);
					    selectAnunciante.add(option);
					}
				}
			}
		}
		
		function carregarCaderno(lista) {
			var jaExiste = false;
			var selectAnunciante = document.getElementById("idAnunciante");
			var selectCaderno = document.getElementById("idCaderno");
			
			selectCaderno.innerHTML = "";
			selectCaderno.add(new Option("Selecione", "0"));
			
			if(lista != null) {
				for (var i = 0; i < lista.length; i++) {
					jaExiste = false;
					for (a = 0; a < selectCaderno.length; a = a + 1) {
						if(selectCaderno.options[a].value == lista[i].cadernoIdentificador) {
							jaExiste = true;
					    	break;
						}
					}
					if(!jaExiste && selectAnunciante.value == lista[i].anuncianteId) {
					    var option = new Option(lista[i].cadernoDescricao, lista[i].cadernoIdentificador);
					    selectCaderno.add(option);
					}
				}
			}
		}
		
		function carregarSecao(lista) {
			var jaExiste = false;
			var selectSecao = document.getElementById("idSecao");
			var selectAnunciante = document.getElementById("idAnunciante");
			var selectCaderno = document.getElementById("idCaderno");
			
			selectSecao.innerHTML = "";
			selectSecao.add(new Option("Selecione", "0"));
			for (var i = 0; i < lista.length; i++) {
				jaExiste = false;
				for (a = 0; a < selectSecao.length; a = a + 1) {
					if(selectSecao.options[a].value == lista[i].retrancaCodigo) {
						jaExiste = true;
				    	break;
					}
				}
				if(!jaExiste && selectAnunciante.value == lista[i].anuncianteId 
									&& selectCaderno.value == lista[i].cadernoIdentificador) {
				    var option = new Option(lista[i].retrancaDescricao, lista[i].retrancaCodigo);
				    selectSecao.add(option);
				}
			}	
			
		}
		
		
		function carregarMateria(lista) {
			var jaExiste = false;
			var selectAnunciante = document.getElementById("idAnunciante");
			var selectSecao = document.getElementById("idSecao");
			var selectCaderno = document.getElementById("idCaderno");
			var selectMateria = document.getElementById("idMateria");
			
			selectMateria.innerHTML = "";
			selectMateria.add(new Option("Selecione", "0"));
			
			for (var i = 0; i < lista.length; i++) {
				jaExiste = false;
				for (a = 0; a < selectMateria.length; a = a + 1) {
					if(selectMateria.options[a].value == lista[i].tipoMateriaCodigo) {
						jaExiste = true;
				    	break;
					}
				}
				if(!jaExiste && selectAnunciante.value == lista[i].anuncianteId 
									&& selectCaderno.value == lista[i].cadernoIdentificador
									&& selectSecao.value == lista[i].retrancaCodigo) {
				    var option = new Option(lista[i].tipoMateriaDescr, lista[i].tipoMateriaCodigo);
				    selectMateria.add(option);
				}
			}	
			
		}
		
		function setAnuncianteTipoMaterial(lista) {
			var selectMateria = document.getElementById("idMateria");
			for (var i = 0; i < lista.length; i++) {
				if(selectMateria.value == lista[i].tipoMateriaCodigo) {
// 					document.getElementById("idAnuncianteId").value = lista[i].anuncianteId;
					document.getElementById("idTipoMateriaIdr").value = lista[i].tipoMateriaIdr;
					break;
				}
			}	
		}
		
		function montarReciboArquivoDOE(){
			limparCamposAlertaErro();
			
			var anuncianteId = document.getElementById("idAnunciante").value;
			var cadernoId = document.getElementById("idCaderno").value;
			var retrancaCod = document.getElementById("idSecao").value;
			var tipoMaterialId = document.getElementById("idTipoMateriaIdr").value;
			var idMov = obterCheckmov();
			
			sigaSpinner.mostrar();
			$("#btEnviarArquivo").prop("disabled", true);
			$.ajax({				     				  
				  url:'${pageContext.request.contextPath}/app/exMovimentacao/montarReciboArquivoDOE',
				  type: "POST",
				  data: {anuncianteId : anuncianteId, cadernoId : cadernoId, retrancaCod : retrancaCod, 
					  			tipoMaterialId : tipoMaterialId, idMov : idMov},
				  success: function(data) {
					  try {
						  var montaRecibo = JSON.parse(data);
						  document.getElementById("idReciboHash").value = montaRecibo.hashRecibo;
						  document.getElementById("idSequencial").value = montaRecibo.proximoSequencial;
						  var sMensagem = document.getElementById("idTextoRecibo").value = montaRecibo.textoRecibo;
						  chamarAssinatura(sMensagem);
					  } catch(err){
						  inserirValueDivAlertaError(data);
						  //Necesario para nao travar plugin de assinatura
						  atualizarTela(30000);
						  sigaSpinner.ocultar();
					  }
					  $('#confirmacaoModal').modal('hide');
			 	  }			 	 
			});
		}
		
		function obterCheckmov(){
			var primeiro = "";
			var aChk = document.getElementsByName("movSelecionados");
			
			for (var i=0;i<aChk.length;i++){
				 var item = aChk[i];
			     if (item.type == "checkbox" && item.checked) {
			     	primeiro = item.value;
			     	break;
			     }
			}
			return primeiro;
		}
		
		//Sdk para Doe
		var parameters =  {
				"config.type" : "local",
				"detachedSignature" : "false",
				"colCount" : "1",
				"colName.0" : "Arquivo",
				"colAlias.0" : "$arquivo",
				"digestAlgorithm" : "SHA256",
				"signingAlgorithm" : "SHA256WithRSA"};			

		function chamarAssinatura(sMensagem) {
			sdkDesktop.signPureContent(sMensagem, doSomethingWithSignature);			
		}
		
		function doSomethingWithSignature(signature){	
			limparCamposAlertaErro();
			
			//console.log(signature);			
			var json = JSON.parse(signature);
			
			var anuncianteId = document.getElementById("idAnunciante").value;
			var cadernoId = document.getElementById("idCaderno").value;
			var retrancaCod = document.getElementById("idSecao").value;
			var tipoMaterialId = document.getElementById("idTipoMateriaIdr").value;
			var sequencial = document.getElementById("idSequencial").value;
			var idMov = obterCheckmov();
			
			var reciboAssinado = json.signature;
			var reciboHash = document.getElementById("idReciboHash").value;
			var reciboTexto = document.getElementById("idTextoRecibo").value;
			
			limparInputsHiddenAposAssinatura();
			
			$.ajax({				     				  
				  url:'${pageContext.request.contextPath}/app/exMovimentacao/enviarPublicacaoArquivoDOE',
				  type: "POST",
				  data: {anuncianteId : anuncianteId, cadernoId : cadernoId, retrancaCod : retrancaCod, 
					  			tipoMaterialId : tipoMaterialId, sequencial: sequencial, idMov : idMov, 
					  			reciboAssinado : reciboAssinado, reciboHash : reciboHash, reciboTexto : reciboTexto},
				  success: function(data) {
					  try {
						 console.log(JSON.parse(data));
						 sigaSpinner.ocultar();
						 sigaModal.alerta("Envio realizado com sucesso!");
						 atualizarTela(5000);
					  } catch(err){
						  inserirValueDivAlertaError(data);
						  //Necesario para nao travar plugin de assinatura
						  atualizarTela(30000);
						  sigaSpinner.ocultar();
					  }
					  $('#confirmacaoModal').modal('hide');
			 	  }
			});
		}
		
		function fecharAlertaModalRedirect() {
			$('#alertaModalRedirect').modal('hide');
			sigaSpinner.mostrar();
			alterouMod();
		}
		
		function limparCamposAlertaErro() {
			document.getElementById("alertError").classList.remove('alert', 'alert-warning');
			document.getElementById("alertError").innerHTML = "";
		}
		
		function inserirValueDivAlertaError(data) {
			if(data != null || data != ""){
				console.log(data);
				document.getElementById("alertError").classList.add('alert', 'alert-warning');
				document.getElementById("alertError").innerHTML  = data;
				window.scrollTo(0, 0);
			}
		}
		
		function limparInputsHiddenAposAssinatura() {
// 			document.getElementById("idAnuncianteId").value="";
			document.getElementById("idReciboHash").value = "";
			document.getElementById("idTipoMateriaIdr").value = "";
		}
		
		function atualizarTela(tempo){
			window.setTimeout( function() {
				window.location.reload();
				sigaSpinner.mostrar();
			}, tempo);
		}
		
		sdkDesktop.checkStarted(limparInputsHiddenAposAssinatura);
		sdkDesktop.setParameters(parameters);

	</script>
	
	<div class="container-fluid" id="content-doe">
		<div id="alertError"></div>
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
					<input type="hidden" name="reciboHash" id="idReciboHash" value="" />
					<input type="hidden" name="textoRecibo" id="idTextoRecibo" value="" />
<!-- 					<input type="hidden" name="anuncianteId" id="idAnuncianteId" value="" /> -->
					<input type="hidden" name="tipoMateriaIdr" id="idTipoMateriaIdr" value="" />
					<input type="hidden" name="sequencial" id="idSequencial" value="" />
					
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
									<th class="text-left" style="width: 2%;"></th>
									<th class="text-left" style="width: 25%;">Número</th>
									<th class="text-left" style="width: 10%;">Data</th>
									<th class="text-left" style="width: 55%;">Descrição</th>
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
									 <td class="text-center align-middle"><input type="checkbox" name="movSelecionados" id="${x}" class="chk" value="${mov.idMov}" ${x_checked} onclick="atualizarTipoAto(this)"/></td>
     			        			 <td class="text-left align-middle">
     			        			 	<span data-toggle="tooltip" data-placement="bottom" title="${mov.exMobil.exDocumento.descrDocumento}">
     			        			 		<a href="/sigaex/app/arquivo/exibir?id=${mov.idMov}&arquivo=${mov.exMobil.exDocumento.sigla}:${mov.idMov}&sigla=${mov.exMobil.exDocumento.sigla}">${mov.exMobil.exDocumento.sigla}.txt</a>
     			        			 	</span>
     			        			 </td>
     			        			 <td class="text-left align-middle"><fmt:formatDate pattern = "dd/MM/yyyy" value="${mov.dtIniMov}"/></td>
     			        			 <th class="text-left align-middle" style="font-weight: normal;">${mov.exMobil.exDocumento.descrDocumento}</th>
     			        			 <td class="text-right">
     			        			 <div class="btn-group">
     			        			 	<a href="javascript:cancelarMov(${mov.idMov}, '${mov.exMobil.exDocumento.sigla}')" onclick="cancelarMov(${mov.idMov}, '${mov.exMobil.exDocumento.sigla}')" class="btn btn-primary" role="button" aria-pressed="true" style="min-width: 80px;">Cancelar</a>
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
									<select class="form-control siga-select2" id="idAnunciante" name="idAnunciante" onchange='carregarCaderno(${listaPermissoes})'>
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Caderno</label>
									<select class="form-control siga-select2" id="idCaderno" name="idCaderno" onchange='carregarSecao(${listaPermissoes})'>
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Seção</label>
									<select class="form-control siga-select2" id="idSecao" name="idSecao" onchange='carregarMateria(${listaPermissoes})'>
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<label>Tipo de Matéria</label>
									<select class="form-control siga-select2" id="idMateria" name="idMateria" onchange='setAnuncianteTipoMaterial(${listaPermissoes})'>
										<option value="0">Selecione</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group row">
							<div class="col-sm">
								<input type="button" value="Enviar Arquivo Selecionado" class="btn btn-primary" id="btEnviarArquivo" onclick="javascript:validarCampos();"/>
							</div>				
						</div>
						</div>
					</c:if>
					<siga:siga-modal id="confirmacaoModal" exibirRodape="true" tituloADireita="Confirmação"
						descricaoBotaoFechaModalDoRodape="Cancelar" linkBotaoDeAcao="javascript:montarReciboArquivoDOE();">
						<div class="modal-body">O arquivo selecionado será enviado ao PubNet! Prosseguir?</div>
					</siga:siga-modal>
					<siga:siga-modal id="confirmacaoModalCancelar" exibirRodape="true" tituloADireita="Confirmação"
						descricaoBotaoFechaModalDoRodape="Cancelar" linkBotaoDeAcao="javascript:confirmarCancelarMov();">
						<div class="modal-body">Cancelar arquivo de agendamento DOE! Prosseguir?</div>
					</siga:siga-modal>
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
		if(texto.indexOf("Resolução") != -1) {
			document.getElementById("div1").style.display = "block";
		    document.getElementById("div2").style.display = "block";
		}
		carregarAnunciante(${listaPermissoes});
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
	
	$('input.chk').on('change', function() {
	    $('input.chk').not(this).prop('checked', false);  
	});
</script>