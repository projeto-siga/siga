<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${null != sel}">1;${sel.id};${sel.sigla}#</c:if>
<c:if test="${null == sel}">0;#</c:if>
