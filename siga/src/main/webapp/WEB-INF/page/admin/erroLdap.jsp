<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${status == 'erro'}">
	<div>
		Erro: <p>${err}</p>
	</div>
	<div>
		Mensagem: <p>${message}</p>
	</div>
	<div>
		Causa: <p>${cause}</p>
	</div>
	<div>
		Stacktrace: <p><a id="linkShowStack-${nomeDaFuncionalidade}" href="#" onclick="showStacktrace('${nomeDaFuncionalidade}')">exibir</a><span id="stacktrace-${nomeDaFuncionalidade}" style="display: none">${stacktrace}</span></p>
	</div>
</c:if>
