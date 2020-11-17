<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${condutor.dadosParaExibicao}</h2>
			<h3><fmt:message key="afastamento.relacao" /></h3>
			<jsp:include page="../condutor/menu.jsp"></jsp:include>
			<c:choose>
				<c:when test="${afastamentos.size()>0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr style="font-weight: bold;">
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
									<th>Descri&ccedil;&atilde;o</th>
									<th>In&iacute;cio</th>
									<th>Fim</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${afastamentos}" var="afastamento">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[AfastamentoController].editar(afastamento.condutor.id,afastamento.id)}" titulo="afastamento" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[AfastamentoController].excluir(afastamento.id)}" titulo="afastamento"
											onclick="javascript:return confirm('Tem certeza de que deseja excluir este afastamento?');"/> 
										</td>								
										<td>
											${afastamento.descricao}
										</td>
										<td>
											<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${afastamento.dataHoraInicio.time}" />
										</td>
										<td>
											<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${afastamento.dataHoraFim.time}" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem afastamentos cadastrados para este condutor.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[AfastamentoController].incluir(condutor.id)}" id="botaoIncluirCondutor" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
			</div>
		</div>
	</div>
</siga:pagina>