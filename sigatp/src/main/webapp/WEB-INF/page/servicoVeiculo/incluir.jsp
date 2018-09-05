<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal/>

<siga:pagina titulo="SIGA::Transportes">
	<c:set var="mostrarBotoesEditar" value="true"></c:set>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Incluir Servi&ccedil;os de Ve&iacute;culos</h2>
			<jsp:include page="form.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>