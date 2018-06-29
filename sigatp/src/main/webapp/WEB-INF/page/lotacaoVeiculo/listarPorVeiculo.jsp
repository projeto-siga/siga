<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA::Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${veiculo.dadosParaExibicao}</h2>
			<h3>Lota&ccedil;&otilde;es</h3>

			<jsp:include page="../veiculo/menu.jsp"></jsp:include>

			<c:choose>
				<c:when test="${lotacoesVeiculo.size()>0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr style="font-weight: bold;">
									<th>Lota&ccedil;&atilde;o</th>
									<th>Data e Hora In&iacute;cio</th>
									<th>Data e Hora Fim</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${lotacoesVeiculo}" var="lotacaoVeiculo">
									<tr>
										<td>${lotacaoVeiculo.lotacao.descricaoAmpliada}</td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm"
												value="${lotacaoVeiculo.dataHoraInicio.time}" /></td>
										<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm"
												value="${lotacaoVeiculo.dataHoraFim.time}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem lota&ccedil;&otide;es cadastradas para este ve&iacute;culo.</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</siga:pagina>