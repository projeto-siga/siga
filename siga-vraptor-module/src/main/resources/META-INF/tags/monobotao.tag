<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="inputType" required="true"%>
<%@ attribute name="cssClass" required="false"%>
<%@ attribute name="value" required="true"%>
<%@ attribute name="onclique" required="false"%>

<input type="${inputType}" onclick="javascript: ${onclique} if (isCarregando()) return false; carrega();"
	class="${cssClass}" value="${value}" />
