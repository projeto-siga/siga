<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<jsp:include page="../tags/calendario.jsp" />
<jsp:include page="../tags/decimal.jsp" />

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${abastecimento.id > 0}">
					<h2>Editar Abastecimento</h2>
				</c:when>
				<c:otherwise>
					<h2>Incluir Abastecimento</h2>
				</c:otherwise>			
			</c:choose>
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>



