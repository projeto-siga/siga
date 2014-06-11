<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
						<th colspan="3">Descrição</th>
					</thead>
					<c:forEach var="tipoDespacho" items="${tiposDespacho}">
						<tr>
							<td><ww:url id="url" action="editar"
									namespace="/despacho/tipodespacho">
									<ww:param name="id">${tipoDespacho.idTpDespacho}</ww:param>
								</ww:url> <ww:a href="%{url}">
									<fmt:formatNumber pattern="0000000"
										value="${tipoDespacho.idTpDespacho}" />
								</ww:a></td>
							<td>${tipoDespacho.descTpDespacho}</td>
							<td><ww:url id="url" action="apagar"
									namespace="/despacho/tipodespacho">
									<ww:param name="id">${tipoDespacho.idTpDespacho}</ww:param>
								</ww:url> <ww:a href="%{url}">apagar</ww:a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<br />
			<ww:form name="frm" action="editar"
				namespace="/despacho/tipodespacho" theme="simple" method="POST">
				<ww:submit value="Novo" cssClass="gt-btn-medium" />
			</ww:form>

		</div>
	</div>
</siga:pagina>
