<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Assinatura em Lote"
	compatibilidade="IE=EmulateIE9">
	<script type="text/javascript" language="Javascript1.1">
		/*  converte para maiúscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
	</script>

	<script type="text/javascript" language="Javascript1.1">
		function checkUncheckAll(theElement) {
			var theForm = theElement.form, z = 0;
			for (z = 0; z < theForm.length; z++) {
				if (theForm[z].type == 'checkbox'
						&& theForm[z].name != 'checkall' && theForm[z].name != 'ad_password_0') {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				}
			}
		}

		function displaySel(ad_chk, el) {
			document.getElementById('div_' + el).style.display = ad_chk.checked ? ''
					: 'none';
			if (ad_chk.checked == -2)
				document.getElementById(el).focus();
		}

		function displayTxt(sel, el) {
			document.getElementById('div_' + el).style.display = sel.value == -1 ? ''
					: 'none';
			document.getElementById(el).focus();
		}
		
		function somenteLetrasNumeros(){
			tecla = event.keyCode;
			if ((tecla >= 48 && tecla <= 57) || (tecla >= 65 && tecla <= 90) || (tecla >= 97 && tecla <= 122)){
			    return true;
			}else{
			   return false;
			}
		}
		$(document).ready(
			function() {
				$("#bot-assinar").click(function() {
					var theForm = document.getElementById('frm'), z = 0, check = 0;
					for (z = 0; z < theForm.length; z++) {
						if (theForm[z].type == 'checkbox'
								&& theForm[z].name != 'checkall' && theForm[z].name != 'ad_password_0') {
							if(theForm[z].checked) {
								check = 1;
								break;
							}
						}
					}
					if(check == 0) {
						sigaModal.alerta("Selecione um ou mais documento(s).");
						gAssinando = false;
						return;
					} else {
						sessionStorage.removeItem('timeout' + document.getElementById('cadastrante').title);
					}
				})
			})
	</script>
	<script
		src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"
		type="text/javascript"></script>
	<div class="container-fluid">
		<form name="frm" id="frm" cssClass="form" theme="simple">
			<input type="hidden" name="postback" value="1" />
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>Assinatura em Lote</h5>
				</div>
				<div class="card-body">
					<div id="dados-assinatura" style="visible: hidden">
						<input type="hidden" name="ad_url_base" value="" /> <input
								type="hidden" name="ad_url_next" value="/siga/app/principal" />
						<c:set var="botao" value="" />
							<c:if test="${autenticando}">
								<c:set var="botao" value="autenticando" />
							</c:if>
							<c:set var="lote" value="false" />
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">

								<c:set var="podeAssinarComSenha" value="${(not empty documentosQuePodemSerAssinadosComSenha)}" />
								<c:set var="defaultAssinarComSenha" value="true" />

								<c:set var="podeUtilizarSegundoFatorPin" value="${f:podeUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao)}" />
								<c:set var="obrigatorioUtilizarSegundoFatorPin" value="${f:deveUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao)}" />
								<c:set var="defaultUtilizarSegundoFatorPin" value="${f:defaultUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao) }" />
								
								<tags:assinatura_botoes assinar="true" 
									assinarComSenha="${podeAssinarComSenha and not obrigatorioUtilizarSegundoFatorPin}"
									assinarComSenhaChecado="${podeAssinarComSenha and defaultAssinarComSenha and not defaultUtilizarSegundoFatorPin}"
									assinarComSenhaPin="${podeAssinarComSenha and podeUtilizarSegundoFatorPin}"
									assinarComSenhaPinChecado="${podeAssinarComSenha and podeUtilizarSegundoFatorPin and defaultUtilizarSegundoFatorPin}"/>

							</div>
						</div>
					</div>
				</div>
			</div>
			<c:if test="${(not empty itensSolicitados)}">
				<h5>Documentos pendentes de assinatura: <fmt:message key="tela.assina.lote.subscritor" /></h5>
				<div>
					<table class="table table-hover table-striped">
						<thead class="${thead_color} align-middle text-center">
							<tr>
								<th width="3%"></th>
								<th width="13%" align="left">Número</th>
								<th width="5%"></th>
								<th width="15%" colspan="2" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cadastrante</th>
								<th width="15%"></th>
								<th width="49%"></th>
							</tr>
							<tr>
								<th width="3%" align="right"><input type="checkbox"
									name="checkall" onclick="checkUncheckAll(this)" /></th>
								<th width="13%"></th>
								<th width="5%" align="center">Data</th>
								<th width="10%" align="right"><fmt:message
										key="usuario.lotacao" /></th>
								<th width="5%" align="left"><fmt:message
										key="usuario.pessoa2" /></th>
								<th width="15%" align="left">Tipo</th>
								<th width="49%" align="left">Descrição</th>
							</tr>
						</thead>
						<tbody class="table-bordered">
							<c:forEach var="doc" items="${itensSolicitados}">
								<c:set var="x" scope="request">ad_chk_${doc.idDoc}</c:set>
								<c:remove var="x_checked" scope="request" />
								<c:if test="${param[x] == 'true'}">
									<c:set var="x_checked" scope="request">checked</c:set>
								</c:if>
								<tr class="even">
									<td width="3%" align="center">
										<input type="checkbox" name="${x}" value="true" ${x_checked} />
									</td>
									<td width="13%" align="left"><a
										href="/sigaex/app/expediente/doc/exibir?sigla=${doc.sigla}">${doc.codigo}</a>
									</td>
									<td width="5%" align="center">${doc.dtDocDDMMYY}</td>
									<td width="10%" align="center">${doc.lotaCadastrante.siglaLotacao}</td>
									<td width="5%" align="left">${doc.cadastrante.sigla}</td>
									<td width="15%" align="left">${doc.descrFormaDoc}</td>
									<td width="49%" align="left">${doc.descrDocumento}</td>
								</tr>
								<input type="hidden" name="pdf${x}" value="${doc.sigla}" />
								<input type="hidden" name="url${x}"
									value="/app/arquivo/exibir?arquivo=${doc.codigoCompacto}.pdf" />
								<input type="hidden" name="ad_tramitar_${doc.idDoc}"
									value="false" />
								<input type="hidden" name="ad_descr_${doc.idDoc}"
									value="${doc.sigla}" />
								<input type="hidden" name="ad_url_pdf_${doc.idDoc}"
									value="/sigaex/app/arquivo/exibir?arquivo=${doc.codigoCompacto}.pdf" />
								<input type="hidden" name="ad_url_post_${doc.idDoc}"
									value="/sigaex/app/expediente/mov/assinar_gravar" />
								<input type="hidden"
									name="ad_url_post_password_${doc.idDoc}"
									value="/sigaex/app/expediente/mov/assinar_senha_gravar" />
								
								<input type="hidden" name="ad_id_${doc.idDoc}"
									value="${doc.codigoCompacto}" />
								<input type="hidden" name="ad_description_${doc.idDoc}"
									value="${doc.descrDocumento}" />
								<input type="hidden" name="ad_kind_${doc.idDoc}"
									value="${doc.descrFormaDoc}" />
							
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</form>
	</div>
	<tags:assinatura_rodape />
</siga:pagina>
