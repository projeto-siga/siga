<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="relertab"%>
<%@ attribute name="tipo" required="true"%>
<%@ attribute name="paramList" required="false"%>
<%@ attribute name="obrigatorio"%>

<c:set var="tipoSel" value="_${tipo}" />

<c:set var="varName" value="${var}${tipoSel}Sel.id" />
<c:set var="v" value="${param[varName]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[varName]}" />
</c:if>
<input type="hidden" name="vars" value="${varName}" />
<%
			request.setAttribute((String) jspContext.getAttribute("varName"),
			jspContext.getAttribute("v"));
%>

<c:set var="varName" value="${var}${tipoSel}Sel.sigla" />
<c:set var="v" value="${param[varName]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[varName]}" />
</c:if>
<input type="hidden" name="vars" value="${varName}" />
<%
			request.setAttribute((String) jspContext.getAttribute("varName"),
			jspContext.getAttribute("v"));
%>
<c:set var="vSigla" value="${v}" />

<c:set var="varName" value="${var}${tipoSel}Sel.descricao" />
<c:set var="v" value="${param[varName]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[varName]}" />
</c:if>
<input type="hidden" name="vars" value="${varName}" />
<%
			request.setAttribute((String) jspContext.getAttribute("varName"),
			jspContext.getAttribute("v"));
%>

<c:if test="${alerta eq 'Sim' and empty v}">
	<c:forEach var="campo" items="${paramValues.obrigatorios}">
		<c:if test="${campo eq varName}">
			<c:set var="vermelho">color:red</c:set>
		</c:if>
	</c:forEach>
</c:if>


<c:if test="${obrigatorio eq 'Sim'}">
	<c:set var="negrito">font-weight:bold</c:set>
	<input type="hidden" name="obrigatorios" value="${varName}" />
</c:if>


<c:if test="${not empty titulo}">
	<span style="${negrito};${vermelho}">
	${titulo}:
	</span>
</c:if>
<c:choose>
	<c:when test="${param.entrevista == 1}">
		<siga:selecao propriedade="${var}" tipo="${tipo}" tema="simple" modulo="siga"
			reler="${reler}" relertab="${relertab}" paramList="${paramList}"/>
		<%-- <input type="text" name="${var}" value="${v}"
			${jreler}${jrelertab}${jlargura}${jmaxcaracteres}/> --%>
	</c:when>
	<c:otherwise>
		<span class="valor"><c:if test="${not empty vSigla}">${vSigla} - </c:if>${v}</span>
	</c:otherwise>
</c:choose>
