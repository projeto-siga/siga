<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="titulo"%>
<%@ attribute name="marcado"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="onclique"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="obrigatorio"%>

<c:if test="${reler == 'ajax'}">
<c:set var="jreler"> sbmt('${idAjax}')</c:set>
</c:if>

<c:if test="${reler == 'sim'}">
	<%--<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>--%>
	<c:set var="jreler"> sbmt();</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />

<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<c:if test="${empty v}">
	<c:if test="${marcado == 'Sim'}">
		<c:set var="v" value="Sim" />
	</c:if>
	<c:if test="${marcado != 'Sim'}">
		<c:set var="v" value="Nao" />
	</c:if>
</c:if>
<c:if test="${v == 'Sim'}">
	<c:set var="jmarcado" value="checked" />
</c:if>

<input type="hidden" name="vars" value="${var}" />
<input type="hidden" id="${var}" name="${var}" value="${v}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:if test="${alerta eq 'Sim' && ((empty v) || (v eq 'Nao'))}">
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




<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="checkbox" name="${var}_chk" value="Sim"
			${jmarcado}
			onclick="javascript: if (this.checked) document.getElementById('${var}').value = 'Sim'; else document.getElementById('${var}').value = 'Nao'; ${onclique}${jreler} " /> <span style="${negrito};${vermelho}">${pageScope.titulo}</span>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
