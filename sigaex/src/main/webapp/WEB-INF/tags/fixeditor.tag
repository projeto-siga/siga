<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- FIX -->
<%@ attribute name="var"%>
<jsp:doBody var="fixeditor" scope="request" />
<%
	String s = (String) request.getAttribute("fixeditor");
    //String var1 = (String) request.getAttribute("var");
    String var1 = (String) jspContext.getAttribute("var");
	s = s.replace("xxxeditorxxx", var1);
	//s = s.replace("<!-- Begin", "");
	//s = s.replace("ExDocumentoForm", "");
	//s = s.replace("function validate(", "function validateExDocumentoForm(");
	
	request.setAttribute("fixeditor", s);
%>
${requestScope.fixeditor}
<!-- /FIX -->
