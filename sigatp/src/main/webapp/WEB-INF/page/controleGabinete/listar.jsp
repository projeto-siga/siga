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
			<h2>Controles do Gabinete</h2>
			<c:choose>
				<c:when test="${controlesGabinete.size()>0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
									<th>Data e Hora Sa&iacute;da</th>
									<th>Od&ocirc;metro Sa&iacute;da (Km)</th>
									<th>Data e Hora Retorno</th>
									<th>Od&ocirc;metro Retorno (Km)</th>
									<th>Agente</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${controlesGabinete}" var="controleGabinete">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[ControleGabineteController].editar(controleGabinete.id)}" titulo="controle" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[ControleGabineteController].excluir(controleGabinete.id)}" titulo="controle"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir este controle?');"/>
										</td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${controleGabinete.dataHoraSaida.time}" /></td>
										<td>${controleGabinete.odometroEmKmSaida}</td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${controleGabinete.dataHoraRetorno.time}" /></td>
										<td>${controleGabinete.odometroEmKmRetorno}</td>
										<td>${controleGabinete.condutor.getDadosParaExibicao()}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<sigatp:paginacao />
					</div>
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem controles cadastrados.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[ControleGabineteController].incluir}"
					class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
			</div>
		</div>
	</div>

</siga:pagina>




