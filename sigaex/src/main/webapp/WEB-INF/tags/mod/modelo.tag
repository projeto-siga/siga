<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="urlBase"%>
<%@ attribute name="salvarViaAjax"%>
<%@ attribute name="acaoGravar"%>
<%@ attribute name="acaoExcluir"%>
<%@ attribute name="acaoCancelar"%>
<%@ attribute name="acaoFinalizar"%>

<c:forEach var="v" items="${paramsEntrevista}">
	<c:set var="key" value="${v.key}" />
	<c:set var="value" value="${v.value}" />
	<%
			request.setAttribute((String) jspContext.getAttribute("key"),
			jspContext.getAttribute("value"));
	%>
</c:forEach>

<!-- modelo -->
<%-- <form name="test" method="GET"> --%>
<jsp:doBody />

<c:if test="${not empty urlBase}">
<c:import url="${urlBase}" />
</c:if>

<c:if test="${not empty salvarViaAjax}">
	<input type="hidden" name="salvarViaAjax" id="salvarViaAjax" value="${salvarViaAjax}" />
</c:if>

<c:if test="${not empty acaoGravar}">
	<input type="hidden" name="acaoGravar" id="acaoGravar" value="${acaoGravar}" />
	<input type="hidden" name="vars" value="acaoGravar" />
</c:if>

<c:if test="${not empty acaoExcluir}">
	<input type="hidden" name="acaoExcluir" id="acaoExcluir" value="${acaoExcluir}" />
	<input type="hidden" name="vars" value="acaoExcluir" />
</c:if>

<c:if test="${not empty acaoCancelar}">
	<input type="hidden" name="acaoCancelar" id="acaoCancelar" value="${acaoCancelar}" />
	<input type="hidden" name="vars" value="acaoCancelar" />
</c:if>

<c:if test="${not empty acaoFinalizar}">
	<input type="hidden" name="acaoFinalizar" id="acaoFinalizar" value="${acaoFinalizar}" />
	<input type="hidden" name="vars" value="acaoFinalizar" />
</c:if>


<%-- 
<input type="hidden" name="entrevista" value="${param.entrevista}"/>
<input type="submit" value="Entrevista" name="Entrevista" onclick="javascript: test.entrevista.value='1';"/>
<input type="submit" value="Documento" name="Documento" onclick="javascript: test.entrevista.value='';"/>
</form>
--%>
<!-- /modelo -->
