<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<c:set var="titulo_pagina" scope="request">Cadastro de simulação</c:set>
<c:import url="/paginas/cabecalho.jsp" />


<ww:url id="url" action="simular" namespace="/substituicao">
</ww:url>

<script type="text/javascript" language="Javascript1.1">

function sbmt() {
	simular_gravar.action='<ww:property value="%{url}"/>';
	simular_gravar.submit();
}

</script>
<body>
<table width="100%">
	<tr>
		<td>
		<form action="simular_gravar.action">
		<input type="hidden" name="postback" value="1" /> <ww:hidden
			name="id" /> 
		<h1>Efetuar simulação</h1>
		<table class="form" width="100%">
			<tr class="header">
				<td colspan="2"></td>
			</tr>

			<tr>
				<td>Matrícula do Usuário:</td>

				<td>
					<span id="spanTitular" style=""> <siga:selecao
					propriedade="titular" tema="simple" modulo="siga" /> </span>
				</td>
			</tr>
			<tr class="button">
				<td></td>
				<td><input type="submit" value="Ok" /> <input type="button"
					value="Cancela" onclick="javascript:history.back();" />
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
<body>
<br />
<!--  tabela do rodapé -->
<c:import url="/paginas/rodape.jsp" />