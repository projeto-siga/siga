<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ attribute name="var"%>
<%@ attribute name="quantidade"%>
<%@ attribute name="reler"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="depende"%>

<c:if test="${not empty idAjax}">
	<c:set var="idAjaxPlic" value="'${idAjax}'" />
</c:if>
<mod:ler var="${quantidade}" />
<c:if test="${empty quantidade}">
<%
	String var = (String) jspContext.getAttribute("quantidade");
	request.setAttribute(var, Long.valueOf(0));
%>
</c:if>
<mod:oculto var="${quantidade}" valor="${requestScope[quantidade]}" />

<c:forEach var="i" begin="1" end="${requestScope[quantidade]}">
	${i}) <jsp:doBody />
	<input type="button" name="Excluir" value="Excluir"
		onclick="javascript:var f=document.getElementById('${quantidade}'); f.value=parseInt(f.value)+1;sbmt(${idAjaxPlic});" />
	<br />
</c:forEach>
<input type="button" name="Acrescentar" value="Acrescentar"
	onclick="javascript:var f=document.getElementById('${quantidade}'); f.value=parseInt(f.value)+1;sbmt(${idAjaxPlic});" />


