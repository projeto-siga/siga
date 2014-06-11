<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Entrar como substituto">
	<h1>Utilizar o sistema substituindo ou em nome de:</h1>
	<table class="list">
		<tr class="header">
			<td align="right">Descrição</td>
			<td align="right">Data Inicial</td>
			<td align="right">Data Final</td>
		</tr>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="substituicao" items="${itens}">
			<tr class="${evenorodd}">
				<td align="right"><ww:url id="url" action="substituir_gravar"
					namespace="/substituicao">
					<ww:param name="idTitular">${substituicao.titular.idPessoa}</ww:param>
					<ww:param name="idLotaTitular">${substituicao.lotaTitular.idLotacao}</ww:param>
				</ww:url> <ww:a href="%{url}">
					<c:choose>
						<c:when test="${not empty substituicao.titular}">
						${substituicao.titular.nomePessoa}
					</c:when>
						<c:otherwise>
						${substituicao.lotaTitular.nomeLotacao}
					</c:otherwise>
					</c:choose>
				</ww:a></td>
				<td align="right">${substituicao.dtIniSubstDDMMYY}</td>
				<td align="right">${substituicao.dtFimSubstDDMMYY}</td>
			</tr>
			<c:choose>
				<c:when test='${evenorodd == "even"}'>
					<c:set var="evenorodd" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="evenorodd" value="even" />
				</c:otherwise>
			</c:choose>
			<c:set var="tamanho" value="${tamanho + 1 }" />
		</c:forEach>
		<tr class="footer">
			<td align="right">Total Listado: ${tamanho}</td>
		</tr>
	</table>
	<br />
</siga:pagina>
