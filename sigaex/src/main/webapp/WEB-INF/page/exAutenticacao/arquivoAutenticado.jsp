<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Movimentação" desabilitarmenu="sim" onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};" incluirJs="/sigaex/javascript/assinatura.js">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Autenticação de Documentos</h2>
			<div>
				<c:url	var='pdfAssinado' value='/app/externo/arquivoAutenticado_stream?n=${n}&answer=${answer}&assinado=true' /> 
				<c:url	var='pdf' value='/app/externo/arquivoAutenticado_stream?n=${n}&answer=${answer}&assinado=false' />
				<iframe	src="${pdfAssinado}" width="100%" height="600" align="center" style="margin-top: 10px;"> </iframe>
			</div>
			<div>
				<br/>
				<h2>Arquivos para Download</h2>
				<ul>
					<li><a href="${pdf}" target="_blank">PDF do documento</a></li>
				</ul>
				<br/>
				<h3>Assinaturas:</h3>
				<ul>
					<c:forEach var="assinatura" items="${assinaturas}" varStatus="loop">
						<c:url	var='arqAssinatura' value='/app/externo/arquivoAutenticado_stream?n=${n}&answer=${answer}&idMov=${assinatura.idMov}' /> 
						<li><a href="${arqAssinatura}" target="_blank">${assinatura.descrMov}</a></li>
					</c:forEach>
				</ul>
				<br/>
				<a href="${request.contextPath}/app/externo/autenticar" class="gt-btn-large gt-btn-left">Autenticar outro Documento</a>
				<c:if test="${mostrarBotaoAssinarExterno}">
				<div style="padding-left: 10;">
					<div id="dados-assinatura" style="visible: hidden">
						<input type="hidden" name="pdfchk_${mov.idMov}" value="${mov.referencia}" />
						<input type="hidden" name="urlchk_${mov.idMov}" value="/app/externo/arquivoAutenticado_stream?n=${n}&answer=${answer}&assinado=false" />

						<c:set var="jspServer" value="${request.contextPath}/app/externo/autenticar?n=${n}&ass=1&answer=${answer}" />
						<c:set var="nextURL" value="${request.contextPath}/app/externo/autenticar" />
						<c:set var="urlPath" value="${request.contextPath}" />
					
						<input type="hidden" id="jspserver" name="jspserver" value="${jspServer}" />
						<input type="hidden" id="nexturl" name="nextUrl" value="${nextURL}" />
						<input type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
						<c:set var="url">${request.requestURL}</c:set>
						<c:set var="uri" value="${request.requestURI}" />
						<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
						<input type="hidden" id="urlbase" name="urlbase" value="${urlBase}" />
						<c:set var="lote" value="false" />
					</div>		
					<c:import url="/javascript/inc_assina_js.jsp" />
					<div id="capicom-div">
						<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false');" class="gt-btn-alternate-large gt-btn-left">Assinar</a> 
					</div>
					<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. </p>
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

					<%--<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
	    				${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}
			  	  	</c:if> --%>
				</div>
				</c:if>
			</div>
		</div>
	</div>
</siga:pagina>





