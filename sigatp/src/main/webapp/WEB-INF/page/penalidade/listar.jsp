<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Penalidades</h2>

			<jsp:include page="menu.jsp" />

			<c:choose>
				<c:when test="${penalidades.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
								<tr>
									<th width="1%" class="noSort"></th>
									<th width="1%" class="noSort"></th>
									<th width="12%">C&oacute;digo da Infra&ccedil;&atilde;o</th>
									<th>Descri&ccedil;&atilde;o da Infra&ccedil;&atilde;o</th>
									<th>Artigo CTB</th>
									<th style="text-align: center;">Valor</th>
									<th>Infrator</th>
									<th>Classifica&ccedil;&atilde;o</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${penalidades}" var="penalidade">
									<tr>
										<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[PenalidadeController].editar(penalidade.id)}" titulo="penalidade" />
										</td>
										<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[PenalidadeController].excluir(penalidade.id)}" 
											onclick="javascript:return confirm('Tem certeza de que deseja excluir os dados desta penalidade?');" titulo="penalidade"/>
										</td>
									
										<td>${penalidade.codigoInfracao}</td>
										<td>${penalidade.descricaoInfracao}</td>
										<td>${penalidade.artigoCTB}</td>
										<td style="text-align: right;"><fmt:formatNumber type="currency">${penalidade.valor}</fmt:formatNumber> </td>
										<td>${penalidade.infrator.descricao}</td>
										<td>${penalidade.classificacao.descricao}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem penalidades cadastradas.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
				<a href="${linkTo[PenalidadeController].incluir}" id="botaoIncluirPenalidade"
					class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
			</div>
		</div>
	</div>
</siga:pagina>
