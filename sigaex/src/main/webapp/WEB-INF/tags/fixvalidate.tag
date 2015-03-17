<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- FIX -->
<jsp:doBody var="fixvalidate" scope="request" />
<%
	String s = (String) request.getAttribute("fixvalidate");
	//s = s.replace(" else ", "//else");
	//s = s.replace("<!-- Begin", "");
	//s = s.replace("ExDocumentoForm", "");
	//s = s.replace("function validate(", "function validateExDocumentoForm(");
	
	request.setAttribute("fixvalidate", s);
%>
${requestScope.fixvalidate}
<!-- /FIX -->
