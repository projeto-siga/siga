<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty modelo.nmArqMod}">
	<c:set var="jsp" scope="request" value="${modelo.nmArqMod}" />
</c:if>
<c:if test="${(empty jsp) and (not empty nmArqMod)}">
	<c:set var="jsp" scope="request" value="${nmArqMod}" />
</c:if>
<c:import url="/paginas/expediente/modelos/${jsp}" />