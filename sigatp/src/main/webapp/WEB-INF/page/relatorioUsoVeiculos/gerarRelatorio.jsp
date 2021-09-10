<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

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
			<h2>
				Uso de ve&iacute;culos no per&iacute;odo de <fmt:formatDate value="${relatorioUsoVeiculos.dataInicio.time}" pattern="dd/MM/yyyy" />
				a
				<fmt:formatDate value="${relatorioUsoVeiculos.dataFim.time}" pattern="dd/MM/yyyy" />
			</h2>

				<div style="width: 90%; float: left; clear: both; padding: 0; margin: 0;">
					<div style="width: 100%; padding: 0; margin: 0;">
						<div id="imprimir" style="width: 100%; padding: 0; margin: 0;">
						<table border="0" class="gt-table" id="tabelaImprimir">
							<thead>
								<tr>
									<th width="10%">Data</th>
									<th width="20%">Ve&iacute;culo</th>
									<th width="7%">Sa&iacute;da</th>
									<th width="7%">Retorno</th>
									<th width="30%">Finalidade</th>
									<th width="10%">Dist&acirc;ncia percorrida</th>
									<th width="16%">Miss&atilde;o</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${relatorioUsoVeiculos.linhas != null}">
									<c:forEach items="${relatorioUsoVeiculos.linhas}" var="item">
										<tr>
											<td><fmt:formatDate value="${item.dataHoraSaida.time}" pattern="dd/MM/yyyy" /></td>
											<td>${item.descricaoVeiculo}</td>
											<td><center><fmt:formatDate value="${item.dataHoraSaida.time}" pattern="HH:mm" /></center></td>
											<td><center><fmt:formatDate value="${item.dataHoraRetorno.time}" pattern="HH:mm" /></center></td>
											<td>${item.descricaoFinalidade}</td>
											<td><fmt:formatNumber minFractionDigits="2" value="${item.distanciaPercorrida}" /> km</td>
											<td><nobr>${item.sequenceMissao}&nbsp;<a href="#" onclick="javascript:window.open('${linkTo[MissaoController].buscarPelaSequence(true,item.sequenceMissao)}');"><img class="icone" src="/sigatp/public/images/linknovajanelaicon.png" alt="Abrir em uma nova janela" title="Abrir em uma nova janela"></a></nobr></td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
						</div>
			<div class="gt-table-buttons">
				<input type="button" value=<fmt:message key="views.botoes.voltar"/> onClick="javascript:location.href='${linkTo[RelatorioUsoVeiculosController].consultar}'" class="gt-btn-medium gt-btn-left" />
				<input type="button" value=<fmt:message key="views.botoes.imprimir"/> onClick="javascript:imprimirTabela();" class="gt-btn-medium gt-btn-left btnSelecao" />
			</div>
		</div>
	</div>
</siga:pagina>

<script type="text/javascript">

function imprimirTabela() {
	var w = window.open();
	w.document.body.innerHTML = window.document.getElementById('imprimir').innerHTML;
	w.document.getElementById('tabelaImprimir').removeAttribute("border");
	w.document.getElementById('tabelaImprimir').setAttribute("border", "1");
	w.document.getElementById('tabelaImprimir').setAttribute("cellspacing", "0");
	var paras = w.document.getElementsByClassName('icone');
	while(paras[0]) {
	    paras[0].parentNode.removeChild(paras[0]);
	}
}

</script>