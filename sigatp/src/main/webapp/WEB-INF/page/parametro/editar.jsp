<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<siga:pagina>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${parametro.id > 0 }">
					<h2>Editar Parametriza&ccedil;&atilde;o: ${parametro.nomeParametro}</h2>
				</c:when>
				<c:otherwise>
					<h2>Incluir Parametriza&ccedil;&atilde;o</h2>
				</c:otherwise>
			</c:choose>
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>
