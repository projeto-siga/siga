<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<siga:pagina titulo="Classificação Documental">
<script type="text/javascript">
	function novaClassificacao(){
		$('#frmNovaClassif').submit();
	}
</script>
<div class="gt-bd clearfix">
	<div class="gt-content">
	<h2 class="gt-table-head">Classificação Documental</h2>
	
		<div class="gt-content-box">
			<table border="0" class="gt-table">
				<thead>
					<tr>
						<th>Codificação</th>
						<th>Descrição</th>
						<th>Numero Via</th>
						<th>Destino</th>
						<th>Arq. Corrente</th>
						<th>Arq. Interm.</th>
						<th>Dest. Final</th>
					</tr>
				</thead>
	
				<tbody>
					<c:forEach items="${classificacaoVigente}" var="cla">
						<c:set var="nivel" value="${cla.nivel}"/>
						<tr>
							<td style="width: 100px"><a title="${cla.descricao}" href="editar?codificacao=${cla.codificacao}&acao=editar_classificacao">${cla.codificacao}</a></td>
							<td rowspan="${fn:length(cla.exViaSet)}">
								<c:forEach begin="1" end="${nivel*nivel}">&nbsp;</c:forEach>
								<span style="font-size: ${16-nivel}">${cla.descrClassificacao}</span>
							</td>
						<c:choose>
							<c:when test="${fn:length(cla.exViaSet)>0}">
								<c:forEach items="${cla.exViaSet}" var="via">
									<c:if test="${via.codVia==1}">
										<td>${via.codVia}</td>
											<td>${via.exTipoDestinacao.descrTipoDestinacao}</td>
											<td>${via.temporalidadeCorrente.descTemporalidade}</td>
											<td>${via.temporalidadeIntermediario.descTemporalidade}</td>
											<td>${via.exDestinacaoFinal.descrTipoDestinacao}</td>
										
									</c:if>
									<c:if test="${via.codVia>1}">
										<tr>
											<td>${via.exClassificacao.codificacao}-${via.letraVia}</td>
											<td>${via.codVia}</td>
											<td>${via.exTipoDestinacao.descrTipoDestinacao}</td>
											<td>${via.temporalidadeCorrente.descTemporalidade}</td>
											<td>${via.temporalidadeIntermediario.descTemporalidade}</td>
											<td>${via.exDestinacaoFinal.descrTipoDestinacao}</td>
											
										</tr>
										
									</c:if>
									
								</c:forEach>
							</c:when>
							<c:otherwise>
								<td/>
								<td/>
								<td/>
								<td/>
								<td/>
							</c:otherwise>
						</c:choose>
							
						</tr>
						
					</c:forEach>
				</tbody>
			</table>
		</div>
		<form id="frmNovaClassif" action="editar" method="get">
			<div class="gt-table-buttons">
				<a id="btNovaClassif" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:novaClassificacao()">Nova Classificação</a>
			</div>
			<input type="hidden" id="acao" name="acao" value="nova_classificacao"/>
		</form>

</siga:pagina>
