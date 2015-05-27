<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<script type="text/javascript" language="Javascript1.1">
	function excluir(id) {
        var form  = document.getElementById("frm");
		if (confirm("Deseja realmente excluir?")){
			location.href = "apagar?id="+id;
		}
	}
</script>
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
							<td>
								<a href="editar?id=${tipoDespacho.idTpDespacho}">
									<fmt:formatNumber pattern="0000000" value="${tipoDespacho.idTpDespacho}" />
								</a>
							</td>
							<td>${tipoDespacho.descTpDespacho}</td>
							<td>
								<a href="javascript:excluir(${tipoDespacho.idTpDespacho})">apagar</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<br />
			<form name="frm" id="frm" action="editar" theme="simple" method="get">
				<input type="submit" id="editar_0" value="Novo" class="gt-btn-medium">
			</form>

		</div>
	</div>
</siga:pagina>
