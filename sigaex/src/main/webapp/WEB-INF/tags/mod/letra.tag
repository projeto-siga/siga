<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="tamanho"%>

<jsp:doBody var="fixFontSize" scope="request" />
<%
	String s = (String) request.getAttribute("fixFontSize");
    String tamanho = (String) jspContext.getAttribute("tamanho");
    
	s = br.gov.jfrj.siga.ex.util.FuncoesEL.fixFontSize(s, tamanho);
    
	request.setAttribute("fixFontSize", s);
%>
<span style="font-size:${tamanho}">
${requestScope.fixFontSize}
</span>