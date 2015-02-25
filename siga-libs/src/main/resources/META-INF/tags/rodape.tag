<%@ tag body-content="scriptless" import="br.gov.jfrj.siga.libs.design.SigaDesign"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="popup"%>
<%@ attribute name="pagina_de_erro"%>

<%= SigaDesign.rodape(
		"true".equals(jspContext.getAttribute("popup")),
		false)
%>