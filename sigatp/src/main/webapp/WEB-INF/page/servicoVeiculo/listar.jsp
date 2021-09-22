<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA-Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<c:if test="${!menuServicosVeiculoMostrarTodos}">
				<h2>Servi&ccedil;os de Ve&iacute;culos</h2>			
			</c:if>
			
			<c:if test="${!menuServicosVeiculoMostrarAgendados}">
				<h2>Servi&ccedil;os de Ve&iacute;culos Agendados</h2>
			</c:if>
			
			<c:if test="${!menuServicosVeiculoMostrarIniciados}">
				<h2>Servi&ccedil;os de Ve&iacute;culos Iniciados</h2>
			</c:if>
			
			<c:if test="${!menuServicosVeiculoMostrarRealizados}">
				<h2>Servi&ccedil;os de Ve&iacute;culos Realizados</h2>
			</c:if>
			
			<c:if test="${!menuServicosVeiculoMostrarCancelados}">
				<h2>Servi&ccedil;os de Ve&iacute;culos Cancelados</h2>
			</c:if>
			
			<jsp:include page="menu.jsp" />
			<c:choose>
				<c:when test="${servicos.size() > 0}">
					<div class="gt-content-box gt-for-table">
						<table id="htmlgrid" class="gt-table tablesorter">
							<thead>
							<tr style="font-weight: bold;">
								<th width="1%" class="noSort"></th>
								<th width="1%" class="noSort"></th>
								<th width="15%">N&uacute;mero</th>
								<th width="8%">Situa&ccedil;&atilde;o</th>
								<th width="14%">Req. Transp.</th>
								<th width="8%">Tipo de Servi&ccedil;o</th>
								<th width="20%">Ve&iacute;culo</th>
								<th width="10%">In&iacute;cio</th>
								<th width="10%">T&eacute;rmino</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach items="${servicos}" var="item">
								<tr>
									<td width="10%">
									<c:if test="${!item.situacaoServico.equals('REALIZADO') && !item.situacaoServico.equals('CANCELADO')}">
										<sigatp:formatarColuna operacao="editar" href="${linkTo[ServicoVeiculoController].editar(item.id)}" titulo="servi&ccedil;o" />
									</c:if>	
									</td>
									<td width="10%">
									<c:if test="${item.situacaoServico.equals('AGENDADO')}">
										<sigatp:formatarColuna operacao="excluir" href="${linkTo[ServicoVeiculoController].excluir(item.id)}" titulo="servi&ccedil;o"
										onclick="javascript:return confirm('Tem certeza de que deseja excluir os dados deste servi&ccedil;o?');"/>
									</c:if>	
									</td>
									<td><nobr><a href="${linkTo[ServicoVeiculoController].buscarServico(popUp,sequence)}?popUp=true&sequence=${item.sequence}">${item.sequence}</a> 
									<a href="#" onclick="javascript:window.open('${linkTo[ServicoVeiculoController].buscarServico(popUp,sequence)}?popUp=true&sequence=${item.sequence}');">
									<img src="/sigatp/public/images/linknovajanelaicon.png" 
									alt="Abrir em uma nova janela" title="Abrir em uma nova janela"></a></nobr></td>				
									<td>${item.situacaoServico}</td>
									<td>${item.requisicaoTransporte != null? item.requisicaoTransporte.buscarSequence() : ""}</td>
									<td>${item.tiposDeServico}</td>
									<td>${item.veiculo != null ? item.veiculo.dadosParaExibicao : ""}</td>
									<td><fmt:formatDate pattern="dd/MM/yyyy" value="${item.dataHoraInicioPrevisto.time}" /></td>
									<td><fmt:formatDate pattern="dd/MM/yyyy" value="${item.dataHoraFimPrevisto.time}" /></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />    
				</c:when>
				<c:otherwise>
					<br />
					<h3>N&atilde;o existem servi&ccedil;os cadastrados.</h3>
				</c:otherwise>
			</c:choose>
			<div class="gt-table-buttons">
			<tptags:link texto="<fmt:message key="views.botoes.incluir"/>" parteTextoLink="<fmt:message key="views.botoes.incluir"/>" comando="${linkTo[ServicoVeiculoController].incluir}">
			  	<a href="${linkTo[ServicoVeiculoController].incluir}" 
					class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
			</tptags:link> 
			</div>
		</div>
	</div>
</siga:pagina>