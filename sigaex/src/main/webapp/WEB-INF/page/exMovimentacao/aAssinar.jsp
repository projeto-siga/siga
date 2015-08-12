<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Documento" onLoad="javascript: TestarAssinaturaDigital();" compatibilidade="IE=EmulateIE9">
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

	<c:if test="${not doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<div class="gt-bd" style="padding-bottom: 0px;">
		<div class="gt-content">

			<h2>Confirme os dados do documento abaixo:</h2>

			<div class="gt-content-box" style="padding: 10px;">
				<table class="message" width="100%">
					<tr class="header">
						<td width="50%"><b>Documento
								${doc.exTipoDocumento.descricao}:</b> ${doc.codigo}</td>
						<td><b>Data:</b> ${doc.dtDocDDMMYY}</td>
					</tr>
					<tr class="header">
						<td><b>De:</b> ${doc.subscritorString}</td>
						<td><b>Classificação:</b>
							${doc.exClassificacao.descricaoCompleta}</td>
					</tr>
					<tr class="header">
						<td><b>Para:</b> ${doc.destinatarioString}</td>
						<td><b>Descrição:</b> ${doc.descrDocumento}</td>
					</tr>
					<c:if test="${doc.conteudo != ''}">
						<tr>
							<td colspan="2">
								<div id="conteudo" style="padding-top: 10px;">
									<tags:fixdocumenthtml>
										${doc.conteudoBlobHtmlStringComReferencias}
									</tags:fixdocumenthtml>
								</div>
							</td>
						</tr>
					</c:if>
				</table>

			</div>

			<c:set var="acao" value="assinar_gravar" />
			<div class="gt-form-row gt-width-100" style="padding-top: 10px;">
				<div id="dados-assinatura" style="visible: hidden">
					<input type="hidden" id="pdfchk_0" name="pdfchk_${doc.idDoc}"
						value="${sigla}" /> <input type="hidden" id="urlchk_0"
						name="urlchk_${doc.idDoc}"
						value="/app/arquivo/exibir?arquivo=${doc.codigoCompacto}.pdf" />

					<c:set var="jspServer"
						value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.request.contextPath}/app/expediente/mov/assinar_gravar" />
					<c:set var="nextURL"
						value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" />
					<c:set var="urlPath" value="${pageContext.request.contextPath}" />

					<input type="hidden" id="jspserver" name="jspserver" value="${jspServer}" />
					<input type="hidden" id="nexturl" name="nextUrl" value="${nextURL}" />
					<input type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="url">${request.requestURL}</c:set>
					<c:set var="uri" value="${request.requestURI}" />
					<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
					<input type="hidden" id="urlbase" name="urlbase" value="${urlBase}" />

					<c:set var="botao" value="" />
					<c:if test="${autenticando}">
						<c:set var="botao" value="autenticando" />
					</c:if>
					<c:set var="lote" value="false" />
				</div>
				
				<tags:assinatura_botoes
					autenticar="${autenticando}"
					assinarComSenha="${f:podeAssinarComSenha(titular,lotaTitular,doc.mobilGeral)}"/>
			</div>
		</div>
	</div>
	<c:if test="${not autenticando}">
		<c:if
			test="${f:podeAssinarComSenha(titular,lotaTitular,doc.mobilGeral)}">
			<a id="bot-assinar-senha" href="#"
				onclick="javascript: assinarComSenha();"
				class="gt-btn-large gt-btn-left">Assinar com Senha</a>

			<div id="dialog-form" title="Assinar com Senha">
				<form id="form-assinarSenha" method="post"
					action="/sigaex/app/expediente/mov/assinar_senha_gravar">
					<input type="hidden" id="sigla" name="sigla" value="${sigla}" />
					<fieldset>
						<label>Matrícula</label> <br /> <input id="nomeUsuarioSubscritor"
							type="text" name="nomeUsuarioSubscritor"
							class="text ui-widget-content ui-corner-all"
							onblur="javascript:converteUsuario(this)" /><br /> <br /> <label>Senha</label>
						<br /> <input type="password" name="senhaUsuarioSubscritor"
							class="text ui-widget-content ui-corner-all" autocomplete="off" />
					</fieldset>
				</form>
			</div>

			<script>
				dialog = $("#dialog-form").dialog({
					autoOpen : false,
					height : 210,
					width : 350,
					modal : true,
					buttons : {
						"Assinar" : assinarGravar,
						"Cancelar" : function() {
							dialog.dialog("close");
						}
					},
					close : function() {

					}
				});

				function assinarComSenha() {
					dialog.dialog("open");
				}

				function assinarGravar() {
					$("#form-assinarSenha").submit();
				}
			</script>
		</c:if>
	</c:if>


	<tags:assinatura_rodape/>
</siga:pagina>