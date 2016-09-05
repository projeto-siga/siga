<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div>
	Resultado do teste: ${status}
</div>

<c:set var="nomeDaFuncionalidade" value="testarConexao"/>
<jsp:include page="./erroLdap.jsp"></jsp:include>