<%@ page language="java" contentType="text/html; charset=UTF-8"	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>


<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Editar Penalidade: ${penalidade.codigoInfracao}</h2>
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>