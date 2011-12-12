<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Lista Substituições">
	<h1>Substituições cadastradas:</h1>
	<ww:url id="url" action="editar" namespace="/substituicao">
	</ww:url>
	<input type="button" value="Incluir"
		onclick="javascript:window.location.href='${url}'">
	<br>
	<br>
	<table class="list" width="100%">
		<tr class="header">
			<td align="right">Titular</td>
			<td align="right">Substituto</td>
			<td align="right">Data inicial</td>
			<td align="right">Data final</td>
			<td align="center">Opções</td>
		</tr>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="substituicao" items="${itens}">
			<tr class="${evenorodd}">
				<td align="right"><c:choose>
					<c:when test="${not empty substituicao.titular}">
						${substituicao.titular.nomePessoa}
					</c:when>
					<c:otherwise>
						${substituicao.lotaTitular.nomeLotacao}
					</c:otherwise>
				</c:choose></td>
				<td align="right"><c:choose>
					<c:when test="${not empty substituicao.substituto}">
						${substituicao.substituto.nomePessoa}
					</c:when>
					<c:otherwise>
						${substituicao.lotaSubstituto.nomeLotacao}
					</c:otherwise>
				</c:choose></td>
				<td align="right">${substituicao.dtIniSubstDDMMYY}</td>
				<td align="right">${substituicao.dtFimSubstDDMMYY}</td>
				<td align="center">
				<c:if test="${!substituicao.terminada && !substituicao.excluida}">
					<ww:url id="url" action="editar" namespace="/substituicao">
						<ww:param name="id">${substituicao.idSubstituicao}</ww:param>
					</ww:url>
					<ww:a href="%{url}">Alterar</ww:a>
					<c:if test="${!substituicao.emVoga && !substituicao.excluida}">
						<ww:url id="url" action="excluir" namespace="/substituicao">
							<ww:param name="id">${substituicao.idSubstituicao}</ww:param>
						</ww:url>
						<ww:a href="%{url}"> | Excluir</ww:a>
				    </c:if>
			    </c:if></td>
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
