<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>

<siga:pagina titulo="SIGA - Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2><fmt:message key="views.label.editar"/> <fmt:message key="views.label.missao"/>: ${missao.sequence}</h2>
			<jsp:include page="form.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>