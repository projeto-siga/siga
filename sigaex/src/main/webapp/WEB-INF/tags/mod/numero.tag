<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="intervalo"%>
<%@ attribute name="reler"%>
<%@ attribute name="maxcaracteres"%>
<%@ attribute name="largura"%>
<%@ attribute name="extensoNum"%>

<c:if test="${reler == 'sim'}">
<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${extensoNum=='sim'}">
 <c:set var="jNumExtenso">
    <span id="${var}numeroextenso"></span>
    <input id="${var}numextenso" type="hidden" name="${var}numextenso"/>
 </c:set>  	 
 <c:set var="jExtensoEvnt"> onBlur="numeroExtenso('${var}',${var});"</c:set> 
</c:if>

<c:if test="${not empty largura}">
<c:set var="jlargura"> size="${largura}"</c:set>
</c:if>

<c:if test="${not empty maxcaracteres}">
<c:set var="jmaxcaracteres"> maxlength="${maxcaracteres}"</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}"></c:set>
</c:if>

<input type="hidden" name="vars" value="${var}" />
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

${titulo}:
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="text" name="${var}" value="${v}" 
		${jreler}${jlargura}${jmaxcaracteres}${jExtensoEvnt}/>
		${jNumExtenso} 
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
