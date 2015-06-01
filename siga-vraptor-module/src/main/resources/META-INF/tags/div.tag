<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="depende"%>
<%@ attribute name="suprimirIndependente"%>
<c:choose>
<c:when test="${suprimirIndependente != 'sim' or not empty depende}">
<div id="${id}" depende=";${depende};"><!--ajax:${id}--><jsp:doBody/><!--/ajax:${id}--></div>
</c:when>
<c:otherwise><jsp:doBody/></c:otherwise>
</c:choose>
