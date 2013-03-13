<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Movimentação" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Autenticação de Documentos</h2>
			<div>
				<c:url	var='pdfAssinado' value='arquivoAutenticado.action?n=${n}&answer=${answer}&assinado=true' /> 
				<c:url	var='pdf' value='arquivoAutenticado.action?n=${n}&answer=${answer}&assinado=false' />
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
						<c:url	var='arqAssinatura' value='arquivoAutenticado.action?n=${n}&answer=${answer}&idMov=${assinatura.idMov}' /> 
						<li><a href="${arqAssinatura}" target="_blank">${assinatura.descrMov}</a></li>
					</c:forEach>
				</ul>
				<br/>
				<a href="/sigaex/autenticar.action" class="gt-btn-large gt-btn-left">Autenticar outro Documento</a>
			</div>
		</div>
	</div>
</siga:pagina>





