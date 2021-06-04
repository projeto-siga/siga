<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<siga:pagina titulo="SIGA-Transportes">
	<style>
		table, tr, th, td {
			background-color: white;
			padding: 1px;
			wborder: 0px solid white;
			-moz-border-radius: 6px;
			-webkit-border-radius: 6px;
			border-radius: 6px;
		}
	
		table tr.header {
			background-color: white;
		}
	</style>

	<div class="gt-bd">
		<div class="gt-content">
			<h2>Relat&oacute;rio de Miss&otilde;es Pendentes Por Condutor</h2>

			<div style="width: 100%; display: block;">
				<div style="width: 49%; float: left; clear: both; padding: 0; margin: 0;">
					<div style="width: 100%; padding: 0; margin: 0;">
						<table border="0" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="30%">Condutor</th>
									<th width="5%" style="text-align: right">Total de Miss&otilde;es</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${missoesPendentes != null}">
									<c:forEach items="${missoesPendentes}" var="item">
										<tr>
											<td>${item.condutor.dadosParaExibicao}</td>
											<td align="right"><a href="${linkTo[MissaoController].listarMissoesPendentesPorCondutor(item.condutor.id)}">${item.totalMissoes}</a></td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>