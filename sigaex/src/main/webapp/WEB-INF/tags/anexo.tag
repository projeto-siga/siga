<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="url"%>
<%@ attribute name="nome"%>
<%@ attribute name="tipo"%>

<span>
	<c:if test="${tipo == 'application/pdf'}">
		<img src="<c:url value="/imagens/pdf.gif"/>" />
	</c:if> 
	
	<c:if test="${tipo == 'application/msword'}">
		<img src="<c:url value="/imagens/doc.gif"/>" />
	</c:if> 
	
	<c:if test="${tipo == 'application/octet-stream'}">
		<img src="<c:url value="/imagens/xls.gif"/>" />
	</c:if> 
	
	<a href="${url}" target="_blank">${nome}</a>
</span>
