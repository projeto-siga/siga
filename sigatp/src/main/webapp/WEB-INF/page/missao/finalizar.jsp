<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="SIGA - Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2><fmt:message key="views.label.finalizar"/> <fmt:message key="views.label.missao"/>: ${missao.sequence}</h2>
			<jsp:include page="formFinalizar.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>