<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<table class="table  table-sm table-striped ">
	<thead class="thead-dark" width="100%">
		<tr>
			<th align="center">Nome</th>
			<th align="center">Órgão</th>
			<th align="center"><fmt:message key="usuario.lotacao"/></th>
			<th align="center">Função</th>
			<th align="center">Cargo</th>
		</tr>
	</thead>
	<c:forEach items="${pessoas}" var="item">
		<tr class="${evenorodd}">
			<td align="left">${item.nomePessoa}</td>
			<td algin="left">${item.orgaoUsuario.descricao }</td>
			<td align="left">${item.lotacao.siglaLotacao}</td>
			<td align="left">${item.funcaoConfianca.nomeFuncao}</td>
			<td align="left">${item.cargo.nomeCargo}</td>
		</tr>
	</c:forEach>
</table>