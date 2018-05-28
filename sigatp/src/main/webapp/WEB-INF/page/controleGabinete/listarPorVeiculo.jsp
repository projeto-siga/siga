<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${veiculo.dadosParaExibicao}- Abastecimentos</h2>

			<jsp:include page="../veiculo/menu.jsp" /></jsp:include>

			<c:choose>
				<c:when test="${abastecimentos.size()>0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table">
							<thead>
								<tr style="font-weight: bold;">
									<th>Data e Hora</th>
									<th>Nivel de Comb. (l)</th>
									<th>Qtd.(l)</th>
									<th>Odômetro (Km)</th>
									<th>Dist. Percorrida (Km)</th>
									<th>Consumo Médio (Km/l)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${abastecimentos}" var="abastecimento">
									<tr>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${controleGabinete.dataHora.time}" /></td>
										<td>${abastecimento.nivelDeCombustivel != null ? abastecimento.nivelDeCombustivel.descricao : ""}</td>
										<td>${abastecimento.quantidadeEmLitros.formataMoedaBrasileiraSemSimbolo()}</td>
										<td>${abastecimento.odometroEmKm.formataMoedaBrasileiraSemSimbolo()}</td>
										<td>${abastecimento.distanciaPercorridaEmKm.formataMoedaBrasileiraSemSimbolo()}</td>
										<td>${abastecimento.consumoMedioEmKmPorLitro.formataMoedaBrasileiraSemSimbolo()}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<sigatp:paginacao />
					</div>
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem abastecimentos cadastrados para este
						ve&iacute;culo.</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</siga:pagina>





