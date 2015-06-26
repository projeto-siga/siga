<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="relertab"%>
<%@ attribute name="buscarFechadas" required="false"%>
<%@ attribute name="obrigatorio"%>
<%@ attribute name="idAjax"%>

<c:if test="${buscarFechadas eq true}">
	<c:set var="paramList" value="buscarFechadas=true" />
</c:if>

<c:if test="${reler == 'ajax'}">
	<c:set var="jreler"> onchange="javascript: sbmt('${idAjax}');"</c:set>
</c:if>

<c:if test="${reler == 'sim'}">
	<c:set var="jreler"> onchange="javascript: sbmt();"</c:set>
</c:if>

<c:if test="${not empty onkeypress}">
    <c:set var="jonkeypress"> onkeypress="${onkeypress}"</c:set>
</c:if>

<mod:selecionavel tipo="pessoa" titulo="${titulo}" var="${var}" reler="${reler}" relertab="${relertab}" paramList="${paramList}" obrigatorio="${obrigatorio}"/>