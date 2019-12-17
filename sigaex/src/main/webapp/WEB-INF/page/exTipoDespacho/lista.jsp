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
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Listagem dos tipos de despacho</h5></div>

			<div class="card-body">
				<table border="0" class="table table-sm table-striped">
					<thead class="${thead_color}">
						<th align="right">N&uacute;mero</th>
						<th colspan="3">Descri&ccedil;&atilde;o</th>
					</thead>
					<tbody class="table-bordered">
					<c:forEach var="tipoDespacho" items="${tiposDespacho}">
						<tr>
							<td>
								<a href="editar?id=${tipoDespacho.idTpDespacho}">
									<fmt:formatNumber pattern="0000000" value="${tipoDespacho.idTpDespacho}" />
								</a>
							</td>
							<td>${tipoDespacho.descTpDespacho}</td>
							<td>
								<input type="button" value="Apagar" class="btn btn-primary" onclick="javascript:excluir(${tipoDespacho.idTpDespacho})" />
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<form name="frm" id="frm" action="editar" theme="simple" method="get">
				<input type="submit" id="editar_0" value="Novo" class="btn btn-primary"/>
			</form>
			</div>
			
			
		</div>
		
	</div>
</siga:pagina>
