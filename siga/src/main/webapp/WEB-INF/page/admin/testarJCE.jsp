<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div>
	<p>JCE Strenght: ${jceStrength} bits (${jceStrengthLimit})</p>
	<p>Java: ${javaHome}</p>
</div>

<c:set var="funcionalidade" value="testarJCE"/>
<jsp:include page="./erroLdap.jsp"></jsp:include>