<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ attribute name="titulo"%>
<%@ attribute name="largura"%>
<%@ attribute name="depende"%>

<siga:div id="div${depende}" depende="${depende}" suprimirIndependente="sim">
<c:if test="${not empty largura}">
<c:if test="${empty grupoLarguraTotal}">
<c:set var="grupoLarguraTotal" value="${0}" scope="request"/>
<table width="100%">
<tr>
</c:if>
<c:set var="grupoLarguraTotal" value="${grupoLarguraTotal + largura}" scope="request"/>
<td width="${largura}%" valign="top">
</c:if>
<table class="entrevista" width="100%">
	<c:if test="${not empty pageScope.titulo}">
		<tr class="header">
			<td>${pageScope.titulo}</td>
		</tr>
	</c:if>
	<tr>
		<td><jsp:doBody /></td>
	</tr>
</table>
<c:if test="${not empty largura}">
</td>
<c:if test="${grupoLarguraTotal >= 100}">
</td>
</table>
<c:remove var="grupoLarguraTotal" scope="request"/>
</c:if>
</c:if>
</siga:div>

