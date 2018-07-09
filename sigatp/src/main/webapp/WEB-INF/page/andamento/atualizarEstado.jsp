<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal/>

<siga:pagina titulo="SIGA-Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${acao} Requisi&ccedil;&atilde;o</h2>
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>