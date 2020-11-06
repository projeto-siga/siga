<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<siga:pagina titulo="Classificação Documental">
	<script type="text/javascript">
		function novaClassificacao() {
			$('#frmNovaClassif').submit();
		}
	</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Classificação Documental</h5>
			</div>
			<div class="card-body">
				<form id="frmNovaClassif" action="editar" method="get" class="mb-0">
					<button type="button" id="btNovaClassif" class="btn btn-primary"
						style="cursor: pointer;" onclick="javascript:novaClassificacao()">
						<u>N</u>ova Classificação
					</button>
					<input type="hidden" id="acao" name="acao"
						value="nova_classificacao" />
				</form>

			</div>
		</div>

		<table class="table table-hover table-sm">
			<thead class="${thead_color} align-middle text-center">
				<tr>
					<th>Codificação</th>
					<th class="text-left">Descrição</th>
					<th>Numero Via</th>
					<th>Destino</th>
					<th>Arq. Corrente</th>
					<th>Arq. Interm.</th>
					<th>Dest. Final</th>
				</tr>
			</thead>
			<tbody class="table-bordered align-middle text-center">
				<c:forEach items="${classificacaoVigente}" var="cla">
					<c:set var="nivel" value="${cla.nivel}" />					
					<c:remove var="viasDefault" />
					<c:choose>
						<c:when test="${cla.numVias <= 0}">
							<c:set var="viasDefault" value="1" />
						</c:when>
						<c:otherwise>
							<c:set var="viasDefault" value="0" />
						</c:otherwise>
					</c:choose>
					<tr>

						<td rowspan="${cla.numVias + viasDefault}" class="align-middle"><a
							title="${cla.descricao}"
							href="editar?codificacao=${cla.codificacao}&acao=editar_classificacao">${cla.codificacao}</a></td>
						<td rowspan="${cla.numVias + viasDefault}"
							class="align-middle text-left">${cla.descrClassificacao}</td>
						<c:choose>
							<c:when test="${numVias > 0}">
 								<c:forEach items="${cla.exViaSet}" var="via" varStatus="loop">
 									<c:if test="${loop.index>0}">
										<tr>
									</c:if>
									<td>${via.codVia}</td>
									<td>${via.exTipoDestinacao.descrTipoDestinacao}</td>
									<td>${via.temporalidadeCorrente.descTemporalidade}</td>
									<td>${via.temporalidadeIntermediario.descTemporalidade}</td>
									<td>${via.exDestinacaoFinal.descrTipoDestinacao}</td>
									<c:if test="${loop.index<numVias0}">
					 					</tr>
									</c:if>
				 				</c:forEach>
			 				</c:when>
							<c:otherwise>
								<td />
								<td />
								<td />
								<td />
								<td />
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</siga:pagina>
