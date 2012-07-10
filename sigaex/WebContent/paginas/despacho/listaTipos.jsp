<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Lista Tipo de Despacho">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Listagem dos tipos de despacho</h2>
			
	<div class="gt-content-box gt-for-table">
		<table class="gt-table">
		<thead>
			<th align="right">Número</th>
			<th colspan="3" align="center">Descrição</th>
		</thead>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="tipoDespacho" items="${tiposDespacho}">
			<tr class="${evenorodd}">
				<td align="right"><ww:url id="url" action="editar"
					namespace="/despacho/tipodespacho">
					<ww:param name="id">${tipoDespacho.idTpDespacho}</ww:param>
				</ww:url> <ww:a href="%{url}">
					<fmt:formatNumber pattern="0000000"
						value="${tipoDespacho.idTpDespacho}" />
				</ww:a></td>
				<td align="center">${tipoDespacho.descTpDespacho}</td>
				<td><ww:url id="url" action="apagar"
					namespace="/despacho/tipodespacho">
					<ww:param name="id">${tipoDespacho.idTpDespacho}</ww:param>
				</ww:url> <ww:a href="%{url}">apagar</ww:a></td>
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
			<td>Pag: 1</td>
			<td align="right">Total Listado: ${tamanho}</td>
		</tr>
	</table>
	<br />
	<ww:form name="frm" action="editar" namespace="/despacho/tipodespacho"
		theme="simple" method="POST">
		<ww:submit value="Novo" />
	</ww:form>

</siga:pagina>
