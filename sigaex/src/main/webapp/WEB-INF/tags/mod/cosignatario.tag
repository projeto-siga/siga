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

<c:if test="${buscarFechadas eq true}">
	<c:set var="paramList" value="buscarFechadas=true" />
</c:if>

<mod:selecionavel tipo="cosignatario" titulo="${titulo}" var="${var}" reler="${reler}" relertab="${relertab}" paramList="${paramList}" obrigatorio="${obrigatorio}"/>