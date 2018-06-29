<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA-Transportes">

	<style>
		table, tr, th, td  {
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
	
	<form enctype='multipart/form-data'>
	<div class="gt-bd">
		<div class="gt-content">
			<h2>Relat&oacute;rio de Consumo M&eacute;dio de Combust&iacute;vel do per&iacute;odo de 
				<fmt:formatDate pattern="dd/MM/yyyy" type="date" value="${relatoriocm.abastecimentoInicial.dataHora.time}" /> a
				<fmt:formatDate pattern="dd/MM/yyyy" type="date" value="${relatoriocm.abastecimentoFinal.dataHora.time}" /> 
		    </h2>
		
			<div style="width: 100%; display: block;">
				<div style="float: left; clear: both; padding: 0; margin: 0;">
					<div style="width: 50%; padding: 0; margin: 0;">	
						<table border="0" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="20%">Ve&iacute;culo</th>
									<th width="15%" style="text-align: right">Km percorridos</th>
									<th width="15%" style="text-align: right">Consumo m&eacute;dio</th>
									<th width="5%" style="text-align: right">Miss&otilde;es</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										${relatoriocm.veiculo.dadosParaExibicao}
									</td>
									<td align="right" >
										${relatoriocm.kmPercorridos}
									</td>
									<td align="right" >
										${relatoriocm.consumoMedio}
									</td>
									<td align="right" >
										${relatoriocm.missoes != null ? relatoriocm.missoes.size() :''}
									</td>
								</tr>
							</tbody>
						</table>
						
						<br>
						<br>
						</div>
				</div>
			</div>
	
			<div class="gt-table-buttons">
				<input type="button" value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[RelatorioConsumoMedioController].consultar}'" class="gt-btn-medium gt-btn-left" />
			</div>
		</div>
	</div>
	</form>
</siga:pagina>