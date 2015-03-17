<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ attribute name="tipo"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="intervalo"%>
<%@ attribute name="largura"%>
<%@ attribute name="reler"%>
<%@ attribute name="relertab"%>
<%@ attribute name="extensoNum"%>
<%@ attribute name="verificaNum"%>
<%@ attribute name="formataNum"%>
<%@ attribute name="maxcaracteres"%>
<%@ attribute name="idAjax"%>
<%@ attribute name="obrigatorio"%>

<c:if test="${reler == 'sim'}">
<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>
<c:if test="${reler == 'ajax'}">
<c:set var="jreler"> onblur="javascript: sbmt('${idAjax}');"</c:set>
</c:if>

<c:if test="${relertab == 'sim'}">
<c:set var="jrelertab"> onblur="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${extensoNum=='sim'}">
 <c:set var="jExtensoNum">
    <span id="${var}valorextenso">${param[f:concat(var, 'vrextenso')]}</span>
    <input id="${var}vrextenso" type="hidden" name="${var}vrextenso" value="${param[f:concat(var, 'vrextenso')]}"/>
 </c:set>  	 
 <c:set var="jExtensoEvnt"> onBlur="extenso('${var}',${var});"</c:set> 
</c:if>

<c:if test="${formataNum=='sim'}">
 <c:set var="jFormataNum"> onKeyPress="return(formataReais(${var},'.',',',event));"</c:set> 
</c:if>

<c:if test="${verificaNum=='sim'}">
 <c:set var="jVerificaNum"> onKeyPress="return(verificaNumero(event));"</c:set> 
</c:if>

<c:if test="${not empty largura}">
<c:set var="jlargura"> size="${largura}"</c:set>
</c:if>

<c:if test="${not empty maxcaracteres}">
<c:set var="jmaxcaracteres"> maxlength="${maxcaracteres}"</c:set>
</c:if>

<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
	<c:if test="${empty v}">
		<c:set var="v" value="0,00" />
	</c:if>
</c:if>

<input type="hidden" name="vars" value="${var}" />
<c:if test="${extensoNum=='sim'}">
<input type="hidden" name="vars" value="${var}vrextenso" />
</c:if>
<%
	String var = (String) jspContext.getAttribute("var");
	request.setAttribute(var, jspContext.getAttribute("v"));
%>

<c:if test="${alerta eq 'Sim'and empty v}">
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

<span style="${negrito};${vermelho}">
${titulo}<c:if test="${not empty titulo}">:</c:if>
</span>

<c:choose>
	<c:when test="${param.entrevista == 1}">
		<input type="text" name="${var}" value="${v}" 
		 ${jlargura}${jmaxcaracteres}${jVerificaNum}${jFormataNum}${jExtensoEvnt}${jreler}/>      
         ${jExtensoNum} 		
	</c:when>
	<c:otherwise>
		<span class="valor">${v}</span>
	</c:otherwise>
</c:choose>
