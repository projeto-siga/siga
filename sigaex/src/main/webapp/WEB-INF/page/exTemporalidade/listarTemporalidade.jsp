<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<siga:pagina titulo="Temporalidade Documental">
<script type="text/javascript">
	function novaTemporalidade(){
		$('#frmNovaTemporalidade').submit();
	}
</script>
<div class="gt-bd clearfix">
	<div class="gt-content">
	<h2 class="gt-table-head">Temporalidade Documental</h2>
	
		<div class="gt-content-box">
			<table border="0" class="gt-table">
				<thead>
					<tr>
						<th>Descrição</th>
						<th>Valor</th>
						<th>Unid. Medida</th>
					</tr>
				</thead>
	
				<tbody>
					<c:forEach items="${temporalidadeVigente}" var="t">
						<tr>
							<td><a href="editar?idTemporalidade=${t.idTemporalidade}&acao=editar_temporalidade">${t.descTemporalidade}</a></td>
							<td>
								${t.valorTemporalidade}
							</td>
							<td>
								${t.cpUnidadeMedida.descricao}
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<form id="frmNovaTemporalidade" action="editar" method="get">
			<div class="gt-table-buttons">
				<a id="btNovaTemporalidade" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:novaTemporalidade()">Nova Temporalidade</a>
			</div>
			<input type="hidden" id="acao" name="acao" value="nova_temporalidade"/>
		</form>

</siga:pagina>
