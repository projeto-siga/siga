<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<siga:pagina titulo="Trasnporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${controleGabinete.id > 0}">
					<h2>Editar Uso de Transporte pelo Gabinete</h2>
				</c:when>
				<c:otherwise>
					<h2>Incluir Uso de Transporte pelo Gabinete </h2>
				</c:otherwise>
			</c:choose>
			<sigatp:erros />
			<br />
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>




