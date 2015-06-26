<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="tipo"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="intervalo"%>
<%@ attribute name="largura"%>
<%@ attribute name="reler"%>
<%@ attribute name="relertab"%>
<%@ attribute name="maxcaracteres"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="obrigatorio"%>
<%@ attribute name="onkeypress"%>
<%@ attribute name="valor"%>

<c:if test="${reler == 'ajax'}">
	<c:set var="jreler"> onchange="javascript: sbmt('${idAjax}');"</c:set>
</c:if>

<c:if test="${reler == 'sim'}">
	<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${not empty onkeypress}">
    <c:set var="jonkeypress"> onkeypress="${onkeypress}"</c:set>
</c:if>

<c:if test="${relertab == 'sim'}">
	<c:set var="jrelertab"> onblur="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${not empty largura}">
	<c:set var="jlargura"> size="${largura}"</c:set>
</c:if>

<c:if test="${not empty maxcaracteres}">
	<c:set var="jmaxcaracteres"> maxlength="${maxcaracteres}"</c:set>
</c:if>

<c:if test="${empty v}">
	<c:set var="v" value="${param[var]}" />
</c:if>
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}"/>
</c:if>

<c:if test="${empty v}">
<c:set var="v" value="${valor}" />
</c:if>

<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:if test="${alerta eq 'Sim' and empty v}">
	<c:forEach var="campo" items="${paramValues.obrigatorios}">
		<c:if test="${campo eq var}">
			<c:set var="vermelho">color:red</c:set>
		</c:if>
	</c:forEach>
</c:if>


<c:if test="${obrigatorio eq 'Sim'}">
	<c:set var="negrito">font-weight:bold</c:set>
	<input type="hidden" name="obrigatorios" value="${var}" />
</c:if>

<c:if test="${not empty titulo}">
	<span style="${negrito};${vermelho}">
	${titulo}:
	</span>
</c:if>
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="text" name="${var}" value="${v}" ${jreler}${jrelertab}${jlargura}${jmaxcaracteres}${jonkeypress}/>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
