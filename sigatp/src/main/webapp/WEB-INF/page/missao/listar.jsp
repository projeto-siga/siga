<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA - Transporte">
	<style>
		.borderBottom {
			border-bottom: solid 1px #BBB;
		}
	</style>
	
 	<sigatp:toggleTexto tipoLista="missao" tagSeparador="</tr>" tagInicial="<table><tbody>"/>
	<input type="hidden" id="txtToggleTexto">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<sigatp:erros/>
			<c:choose>
				<c:when test="${!menuMissoesMostrarTodas && !menuMissoesMostrarProgramadas && !menuMissoesMostrarIniciadas &&
							    !menuMissoesMostrarFinalizadas && !menuMissoesMostrarCanceladas}">
					<h2>Filtro Avan&ccedil;ado de Miss&otilde;es</h2>
				</c:when>
				<c:when test="${!menuMissoesMostrarTodas}">
					<h2>Lista de Todas as Miss&otilde;es</h2>
				</c:when>
				<c:when test="${!menuMissoesMostrarProgramadas}">
					<h2>Lista de Miss&otilde;es Programadas</h2>
				</c:when>
				<c:when test="${!menuMissoesMostrarIniciadas}">
					<h2>Lista de Miss&otilde;es Iniciadas</h2>
				</c:when>
				<c:when test="${!menuMissoesMostrarFinalizadas}">
					<h2>Lista de Miss&otilde;es Finalizadas</h2>
				</c:when>
				<c:when test="${!menuMissoesMostrarCanceladas}">
					<h2>Lista de Miss&otilde;es Canceladas</h2>
				</c:when>
			</c:choose>

			<jsp:include page="menu.jsp"></jsp:include>

			<p>			
				<c:choose>
					<c:when test="${missoes.size() > 0}">
						<div class="gt-content-box gt-for-table tabelaFormGrande">
				 			<table id="htmlgrid" class="gt-table tablesorter" >
				    			<thead>
							    	<tr style="font-weight: bold;">
							    		<th width="1%" class="noSort"></th>
										<th width="1%" class="noSort"></th>
							    		<th width="15%">N&uacute;mero</th>
							    	    <th width="10%">Data Sa&iacute;da</th>
							    	    <th width="10%">Estado</th>
							    	    <th width="15%">Ve&iacute;culo</th>
							    	    <th width="10%">Condutor</th>
								   		<th width="30%">Requisi&ccedil;&otilde;es</th>
										<th width="1%" class="noSort"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${missoes}" var="missao">
									   	<tr>
						 		    		<td>
						 		    			<c:if test="${!'FINALIZADA' == missao.estadoMissao.descricao && !'CANCELADA' == missao.estadoMissao.descricao}">
						 		    				<sigatp:formatarColuna operacao="editar" href="${linkTo[MissaoController].editar(missao.id)}" titulo="miss&atilde;o" />
						 		    			</c:if>
						 		    		</td>
	
								    		<td>
								    			<c:if test="${'PROGRAMADA' == missao.estadoMissao.descricao}">
											    	<sigatp:formatarColuna operacao="cancelar" href="${linkTo[MissaoController].cancelar(missao.id)}" titulo="miss&atilde;o"/>
								    			</c:if>
								    		</td>
	
								    	    <td>
								    	    	<c:choose>
								    	    		<c:when test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo || exibirMenuAgente}">
								    	    			<nobr><a href="${linkTo[MissaoController].buscarPelaSequence(popUp,sequence)}?popUp=false&sequence=${missao.sequence}">${missao.sequence}</a> <a href="#" onclick="javascript:window.open('${linkTo[MissaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${missao.sequence}');"><img src="/sigatp/public/images/linknovajanelaicon.png" alt="Abrir em uma nova janela" title="Abrir em uma nova janela"></a></nobr>
								    	    		</c:when>
								    	    		<c:otherwise>
								    	    			<nobr>${missao.sequence}<a href="#" onclick="javascript:window.open('${linkTo[MissaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${missao.sequence}');"><img src="/sigatp/public/images/linknovajanelaicon.png" alt="Abrir em uma nova janela" title="Abrir em uma nova janela"></a></nobr>
								    	    		</c:otherwise>
								    	    	</c:choose>
							    	    	</td>
								    		<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraSaida.time}" /></td>
								    		<td>${missao.estadoMissao}</td>
								    		<td>${missao.veiculo.dadosParaExibicao}</td>
								    		<td>${missao.condutor.dadosParaExibicao}</td>
								    		<td class="tdListaRequisicoes">
											    <div style="display: none;" class="toggleTexto">
										    		<c:choose>
										    			<c:when test="${missao.requisicoesTransporte.size() > 0}">
										    				<table>
										    					<c:forEach items="${missao.requisicoesTransporte}" var="requisicaoTransporte" varStatus="status" >
									    							<c:if test="${not status.last}">
									    								<tr class="borderBottom">
								    								</c:if>
									    							<c:if test="${status.last}">
								    									<tr>
									    							</c:if>
								    								<td>
							    										<tptags:link texto="${requisicaoTransporte.descricaoCompleta}"
							    											 parteTextoLink="${requisicaoTransporte.buscarSequence()}"
							    											 comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}">
							    										</tptags:link>
							    									</td>
							    									</tr>
								    							</c:forEach>
							    							</table>
										    			</c:when>
										    			<c:otherwise>
												    		<table border="0"><tr><td style="white-space: pre;">SEM REQUISI&Ccedil;&Otilde;ES</td></tr></table>
										    			</c:otherwise>
										    		</c:choose>
									    		</div>
 									    		<jsp:include page="../tags/observacao.jsp" /> 
								    		</td>
	
								    		<td>
								    			<sigatp:formatarColuna operacao="imprimir" href="#" onclick="javascript:window.open('${linkTo[RelatorioController].listarDadosDaMissao(missao.id)}');" titulo="miss&atilde;o" />
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
						<c:choose>
							<c:when test="${!menuMissoesMostrarFiltrarPorCondutor}">
								<h3>N&atilde;o h&aacute; miss&otilde;es a exibir com os dados informados.</h3>
							</c:when>
							<c:otherwise>
								<h3>N&atilde;o h&aacute; miss&otilde;es a exibir.</h3>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</p>
			
			<c:if test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo || exibirMenuAgente}">
				<div class="gt-table-buttons">
					<a href="${linkTo[MissaoController].incluir}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir" /></a>
				</div>
			</c:if>
		</div>
	</div>
</siga:pagina>