<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- FIX -->
<jsp:doBody var="fixdocumenthtml" scope="request" />
<%
	String s = (String) request.getAttribute("fixdocumenthtml");
	s = s.replace("<!-- INICIO PRIMEIRO CABECALHO", "<!-- INICIO PRIMEIRO CABECALHO -->");
	s = s.replace("FIM PRIMEIRO CABECALHO -->", "<!-- FIM PRIMEIRO CABECALHO -->");
	s = s.replace("<!-- INICIO PRIMEIRO RODAPE", "<!-- INICIO PRIMEIRO RODAPE -->");
	s = s.replace("FIM PRIMEIRO RODAPE -->", "<!-- FIM PRIMEIRO RODAPE-->");
	//s = s.replace("http://localhost:8080/siga/", "/siga/");
	s = s.replace("contextpath", request.getContextPath());

	request.setAttribute("fixdocumenthtml", s);
%>
${requestScope.fixdocumenthtml}
<!-- /FIX -->
