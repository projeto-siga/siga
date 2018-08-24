<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Transportes">
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2><fmt:message key="${modo}" /> <fmt:message key="finalidades" /></h2>
			<jsp:include page="form.jsp" />			
		</div>
	</div>
</siga:pagina>