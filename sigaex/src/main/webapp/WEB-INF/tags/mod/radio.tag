<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="titulo"%>
<%@ attribute name="marcado"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="onclique"%>
<%@ attribute name="valor"%>
<%@ attribute name="gerarHidden"%>
<%@ attribute name="idAjax"%>

<c:if test="${reler == 'sim'}">
	<%--<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>--%>
	<c:set var="jreler"> sbmt();</c:set>
</c:if>
<c:if test="${reler == 'ajax'}">
	<c:set var="jreler"> sbmt('${idAjax}');</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />

<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>


<c:if
	test="${(v eq valor) || ((marcado == 'Sim') and (empty v))}">
	<c:set var="jmarcado" value="checked" />
</c:if>

<c:if test="${empty valor}">
	<c:set var="valor" value="Sim" />
</c:if>

<c:if
	test="${(empty v) and (marcado == 'Sim')}">
	<c:set var="v" value="${valor}" />
</c:if>

<%--<c:if test="${empty v}">
	<c:if test="${marcado == 'Sim'}">
		<c:set var="v" value="Sim" />
	</c:if>
	<c:if test="${marcado != 'Sim'}">
		<c:set var="v" value="N&atilde;o" />
	</c:if>
</c:if>
--%>

<%--
<c:if test="${v == 'Sim'}">
	<c:set var="jmarcado" value="checked" />
</c:if>--%>

<input type="hidden" name="vars" value="${var}" />
<c:if test="${gerarHidden != 'não'}">
	<input type="hidden" id="${var}" name="${var}" value="${v}" />
</c:if>
<%--
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
--%>

<c:choose>
	<c:when test="${param.entrevista == 1}">
	<table>
	<tr>
	<td>
		<input type="radio" name="${var}_chk" value="${valor}"
			${jmarcado}
			onclick="javascript: if (this.checked) document.getElementById('${var}').value = '${valor}';
									 else document.getElementById('${var}').value = 'N&atilde;o'; 
									 ${jreler}" /> 
	</td>
	<td>${titulo}</td>
	</tr>
	</table>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
