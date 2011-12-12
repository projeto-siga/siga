<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="url"%>
<%@ attribute name="nome"%>
<%@ attribute name="tipo"%>

<span><ww:if
	test="${tipo == 'application/pdf'}">
	<img src="<c:url value="/imagens/pdf.gif"/>" />
</ww:if> <ww:if test="${tipo == 'application/msword'}">
	<img src="<c:url value="/imagens/doc.gif"/>" />
</ww:if> <ww:if test="${tipo == 'application/octet-stream'}">
	<img src="<c:url value="/imagens/xls.gif"/>" />
</ww:if> <a href="${url}" target="_blank">${nome}</a></span>
