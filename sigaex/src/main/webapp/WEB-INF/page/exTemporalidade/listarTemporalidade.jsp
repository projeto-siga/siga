<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<siga:pagina titulo="Temporalidade Documental">
	<script type="text/javascript">
		function novaTemporalidade() {
			$('#frmNovaTemporalidade').submit();
		}
	</script>
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Temporalidade Documental</h5>
			</div>
			<div class="card-body">
				<form id="frmNovaTemporalidade" action="editar" method="get"
					class="mb-0">
					<div class="gt-table-buttons">
						<button type="button" class="btn btn-primary"
							onclick="javascript:novaTemporalidade()">Nova
							Temporalidade</button>
					</div>
					<input type="hidden" id="acao" name="acao"
						value="nova_temporalidade" />
				</form>
			</div>
		</div>


		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr>
					<th>Descrição</th>
					<th>Valor</th>
					<th>Unid. Medida</th>
				</tr>
			</thead>
			<tbody class="table-bordered">
				<c:forEach items="${temporalidadeVigente}" var="t">
					<tr>
						<td><a
							href="editar?idTemporalidade=${t.idTemporalidade}&acao=editar_temporalidade">${t.descTemporalidade}</a></td>
						<td>${t.valorTemporalidade}</td>
						<td>${t.cpUnidadeMedida.descricao}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

</siga:pagina>
