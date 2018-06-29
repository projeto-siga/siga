<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${escala.condutor.dadosParaExibicao} - <fmt:message key="${modo}" /> <fmt:message key="escalaDeTrabalho" /></h2>
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>