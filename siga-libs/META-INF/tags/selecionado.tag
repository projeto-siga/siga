<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="sigla" %>
<%@ attribute name="descricao" %>

<c:if test="${empty sigla}">
<c:set var="sigla" value="&nbsp;" />
</c:if>

<span ${estilo} title="${descricao}">${sigla}</span>