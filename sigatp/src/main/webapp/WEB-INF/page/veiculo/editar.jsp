<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
			
<siga:pagina titulo="SIGA-Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${veiculo.dadosParaExibicao}</h2>
			<h3><fmt:message key="${tipoCadastro}"/></h3>
			
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>

