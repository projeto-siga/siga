<%@ tag body-content="scriptless"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:doBody var="fixeditor" scope="request" />
<%
	String s = (String) request.getAttribute("fixeditor");
	s = s.replace("\r\f", "<br/>");
	s = s.replace("\n", "<br/>");
	request.setAttribute("fixeditor", s);
%>
${requestScope.fixeditor}