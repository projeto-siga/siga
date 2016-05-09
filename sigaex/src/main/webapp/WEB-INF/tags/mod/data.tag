<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="alerta"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="obrigatorio"%>
<%@ attribute name="valor"%>

<c:if test="${reler == 'sim'}">
<c:set var="jreler">sbmt();</c:set>
</c:if>
<c:if test="${reler == 'ajax'}">
<c:set var="jreler"> sbmt('${idAjax}')</c:set>
</c:if>
<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}"></c:set>
</c:if>
<c:if test="${empty v}">
	<c:set var="v" value="${valor}" />
</c:if>
<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<%-- 
${titulo}:
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="text" name="${var}" value="${v}" size="10" maxlength="10"
			onblur="javascript:verifica_data(this);" ${jreler}/>
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
--%>

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
		<c:choose>
			<c:when test="${alerta eq 'Nao'}">
				<input type="text" name="${var}" value="${v}" size="10" maxlength="10" 
				onblur="javascript:verifica_data(this, 'Sim');${jreler}" /></c:when>
			<c:otherwise>	
				<input type="text" name="${var}" value="${v}" size="10" maxlength="10"
				onblur="javascript:verifica_data(this);${jreler}" />
			</c:otherwise>
		</c:choose>	
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
