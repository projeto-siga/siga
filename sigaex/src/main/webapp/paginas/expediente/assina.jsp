<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<siga:pagina titulo="Documento" onLoad="javascript: TestarAssinaturaDigital();">
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
		<script type="text/javascript">$("html").addClass("fisico");</script>
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
									<tags:fixdocumenthtml>${doc.conteudoBlobHtmlStringComReferencias}</tags:fixdocumenthtml>
								</div></td>
						</tr>
					</c:if>
				</table>

			</div>

			<c:set var="acao" value="assinar_gravar" />
			<div class="gt-form-row gt-width-100" style="padding-top: 10px;">
				<div id="dados-assinatura" style="visible: hidden">
					<ww:hidden id="pdfchk_0" name="pdfchk_${doc.idDoc}"
						value="${sigla}" />
					<ww:hidden id="urlchk_0" name="urlchk_${doc.idDoc}"
						value="/arquivo/exibir.action?arquivo=${doc.codigoCompacto}.pdf" />
				
					<c:set var="jspServer"
						value="${request.scheme}://${request.serverName}:${request.localPort}${request.contextPath}/expediente/mov/assinar_gravar.action" />
					<c:set var="nextURL"
						value="${request.scheme}://${request.serverName}:${request.localPort}${request.contextPath}/expediente/doc/exibir.action?sigla=${sigla}" />
					<c:set var="urlPath" value="${request.contextPath}" />

					<ww:hidden id="jspserver" name="jspserver" value="${jspServer}" />
					<ww:hidden id="nexturl" name="nextUrl" value="${nextURL}" />
					<ww:hidden id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="urlBase"
						value="${request.scheme}://${request.serverName}:${request.serverPort}" />
					<ww:hidden id="urlbase" name="urlbase" value="${urlBase}" />

					<c:set var="botao" value="" />
					<c:set var="lote" value="false" />
				</div>
				
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
					<c:import url="/paginas/expediente/inc_assina_js.jsp" />
					<div id="capicom-div">
					<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);"
						class="gt-btn-alternate-large gt-btn-left">Assinar Documento</a>
					</div>
					<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
					<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://code.google.com/p/projeto-siga/downloads/detail?name=Capicom.zip&can=2&q=#makechanges">aqui</a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
				<script type="text/javascript">
					 if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
						 document.getElementById("capicom-div").style.display = "block";
						 document.getElementById("ie-missing").style.display = "none";
					} else {
						 document.getElementById("capicom-div").style.display = "none";
						 document.getElementById("ie-missing").style.display = "block";
					}
				 </script>
				</c:if>
				 
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
					${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}	
				</c:if>
			</div>
		</div>
	</div>
		<c:if test="${f:podeAssinarComLoginESenha(titular,lotaTitular,mob)}">
		<br/><br/><br/>
        <div class="gt-bd" style="padding-bottom: 0px;">
		   <div class="gt-content">

			<h2>Assinar com Login e Senha</h2>
            <!-- login box -->
			<div class="gt-mylogin-box">
				<!-- login form -->
				<form method="post" action="/sigaex/expediente/mov/assinar_login_senha_gravar.action" class="gt-form">
				    <ww:hidden id="sigla" name="sigla"
						value="${sigla}" />
					<!-- form row -->
					<div class="gt-form-row">
						<label class="gt-label">Matrícula</label> <input id="nomeUsuarioSubscritor"
							type="text" name="nomeUsuarioSubscritor"
							onblur="javascript:converteUsuario(this)" class="gt-form-text">
							
					</div>
					<!-- /form row -->

					<!-- form row -->
					<div class="gt-form-row">
						<label class="gt-label">Senha</label> <input type="password"
							name="senhaUsuarioSubscritor" class="gt-form-text">
					</div>
					<!-- /form row -->

					<!-- form row -->
					<div class="gt-form-row">
						<input type="submit" value="Assinar"
							class="gt-btn-medium gt-btn-right">
					</div>
					<!-- /form row -->
				</form>
				<!-- /login form -->
			</div>
			<!-- /login box -->
			</div>
		</div>
		</c:if>	
</siga:pagina>