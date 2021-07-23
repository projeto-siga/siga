<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="SIGA - Transporte">
	<script type="text/javascript" src="/sigatp/public/javascripts/jquery-1.6.4.min.js"></script>
	
	<script type="text/javascript">
       $(function() {
     		$('.missao').hide();
			$('.bt_missao').css('display','none');

	       	$('#missao_criar').click( function() {
	       		$('.bt_missao').css('display','block');
	       		$('.bt_edicao').css('display','none');
	       		$('.missao').show();
	       		$('.edicao').hide();
	    	});
	
	       	$('#missao_cancelar').click( function() {
	       		$('.bt_missao').css('display','none');
	       		$('.bt_edicao').css('display','block');
	       		$('.missao').hide();
	       		$('.edicao').show();
	       		$("#divErros").hide();
	   	    });
	
	        $('#missao_programar').click( function() {	
				if($('form').serialize().indexOf("req") != -1) {
					$('form').submit();
				} else {
					$("#divErros").show();
					var validacaoRepetida = $("#divErros").text().search("<fmt:message key='requisicaotransporte.selecionar'/>");
					if (validacaoRepetida == -1) {
					   $('#divErros').append("<li><fmt:message key="requisicaotransporte.selecionar"/></li>");
					}	
				}
		 	});
			
	       	$('#missao_inicioRapido').click( function() {
				if($('form').serialize().indexOf("req") != -1) {
					var formulario = document.getElementById('formIncluirMissao');
					var destino = formulario.action;
					formulario.action = destino.replace("incluirComRequisicoes", "incluirInicioRapido"); 
					formulario.submit();
				} else {
					$("#divErros").show();
					var validacaoRepetida = $("#divErros").text().search("<fmt:message key='requisicaotransporte.selecionar'/>");
					if (validacaoRepetida == -1) {
						$('#divErros').append("<li><fmt:message key="requisicaotransporte.selecionar"/></li>");
					}
				}
			});
      });
	</script>
	
	<style type="text/css">
			.status_N, .status_P, .status_A, .status_E, .status_C {
				border: 2px solid;
				border-radius: 1em;
				text-transform: uppercase;
				padding: 0 .25em;
				margin-left: .2em;
				font:normal 70% verdana, arial, sans-serif;
				font-weight: 900;
				position: relative;
				top: -.2em;
			}
			.status_N {
				border-color: #FF2000;
				color: #FF2000;
			}
			.status_C {
				border-color: #FF2000;
				color: #FF2000;
			}
			.status_P {
				border-color: #33EE00;
				color: #33EE00;
			}
			.status_A {
				border-color: #33EE00;
				color: #33EE00;
			}
			.status_E {
				border-color: #FFFF00;
				color: #FFFF00;
			}
	</style>
	

	<sigatp:toggleTexto tipoLista="missao" tagSeparador="<br>" incluirNoFinal="sim"/>
	<input type="hidden" id="txtToggleTexto">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:choose>
				<c:when test="${!menuRequisicoesMostrarTodas && !menuRequisicoesMostrarAutorizadasENaoAtendidas && !menuRequisicoesMostrarAbertas &&
								!menuRequisicoesMostrarAutorizadas && !menuRequisicoesMostrarRejeitadas && !menuRequisicoesMostrarProgramadas &&
								!menuRequisicoesMostrarEmAtendimento && !menuRequisicoesMostrarAtendidas && !menuRequisicoesMostrarNaoAtendidas &&
								!menuRequisicoesMostrarCanceladas}">
					<h2>Filtro Avan&ccedil;ado de Requisi&ccedil;&otilde;es</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarTodas}">
					<h2>Lista de Todas as Requisi&ccedil;&otilde;es</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarAutorizadasENaoAtendidas && menuRequisicoesMostrarAutorizadas && menuRequisicoesMostrarNaoAtendidas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Autorizadas e N&atilde;o Atendidas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarAbertas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Abertas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarAutorizadas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Autorizadas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarRejeitadas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Rejeitadas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarProgramadas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Programadas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarEmAtendimento}">
					<h2>Lista de Requisi&ccedil;&otilde;es em Atendimento</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarAtendidas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Atendidas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarNaoAtendidas}">
					<h2>Lista de Requisi&ccedil;&otilde;es N&atilde;o Atendidas</h2>
				</c:when>
				<c:when test="${!menuRequisicoesMostrarCanceladas}">
					<h2>Lista de Requisi&ccedil;&otilde;es Canceladas</h2>
				</c:when>
			</c:choose>

			<jsp:include page="menuListar.jsp"></jsp:include>
			<br/>

			<form id="formIncluirMissao" method="post" action="${linkTo[MissaoController].incluirComRequisicoes}" enctype="multipart/form-data">
				<sigatp:erros/>
				
				<p>			
					<c:choose>
						<c:when test="${requisicoesTransporte.size() > 0}">
							<div class="gt-content-box gt-for-table tabelaFormGrande">
				 				<table id="htmlgrid" class="gt-table tablesorter" >
				    				<thead>
								    	<tr style="font-weight: bold;">
								    		<th class="edicao noSort" width="1%"></th>
											<th class="edicao noSort" width="1%"></th>
											<th class="missao noSort" width="1%"></th>
								    	    <th width="15%">Sa&iacute;da</th>
									   		<th width="15%">Retorno</th>
									   		<th width="30%">Outros Dados</th>
									   		<th width="20%">Miss&otilde;es</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${requisicoesTransporte}" var="requisicaoTransporte" varStatus="idx">
										   	<tr id ="row_${requisicaoTransporte.id}">
												<td class="edicao">
													<c:if test="${requisicaoTransporte.podeAlterar}">
										    			<sigatp:formatarColuna operacao="editar" href="${linkTo[RequisicaoController].editar(requisicaoTransporte.id)}" titulo="requisi&ccedil;&atilde;o" />
													</c:if>
												</td>
												
									    		<td class="edicao">
									    			<c:if test="${requisicaoTransporte.podeExcluir}">
									    				<sigatp:formatarColuna operacao="excluir" href="${linkTo[RequisicaoController].excluir(requisicaoTransporte.id)}" titulo="requisi&ccedil;&atilde;o"
														onclick="javascript:return confirm('Tem certeza de que deseja excluir esta requisi&ccedil;&atilde;o ?');" />
													</c:if>
									    		</td>
									    		
									    		<td class="missao" valign="middle" colspan="1">
									    			<c:if test="${requisicaoTransporte.podeAgendar}">
									    				<input type="checkbox" name="req[${idx.count - 1}]" value="${requisicaoTransporte.id}" class="missao reqs">
									    			</c:if>
									    		</td>
	
							 			   	   	<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraSaidaPrevista.time}" /></td>
									    		
									    		<td>
									    			<c:choose>
									    				<c:when test="${requisicaoTransporte.dataHoraRetornoPrevisto != null}">
									    					<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraRetornoPrevisto.time}" />
									    				</c:when>
									    				<c:otherwise>
									    					<fmt:message key="no"/>
									    				</c:otherwise>
									    			</c:choose>
									    		</td>
										   	    
										   	    <td>
										   	    	<tptags:link texto="${requisicaoTransporte.descricaoCompleta}"
										   	    				 parteTextoLink="${requisicaoTransporte.buscarSequence()}"
										   	    				 comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}">
										   	    				 ehEditavel="true">
										   	    	</tptags:link>
												</td>
												
												<td class="tdListaMissoes">
												    <div style="display: none;" class="toggleTexto">
														<c:choose>
															<c:when test="${requisicaoTransporte.missoesOrdenadas.size() > 0}">
																<c:forEach items="${requisicaoTransporte.missoesOrdenadas}" var="missao">
																	${missao.sequence}
																	<span class="status_${requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id).primeiraLetra()}" alt="${requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id)}" title="${requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id)}">
																		${requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id).primeiraLetra()}
																	</span>
																	
																	<a href="#" onclick="javascript:window.open('${linkTo[MissaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${missao.sequence}');">
																		<img src="/sigatp/public/images/linknovajanelaicon.png" alt="Abrir em uma nova janela" title="Abrir em uma nova janela">
																	</a>
																	<br />
																</c:forEach>
															</c:when>
															<c:otherwise>
																N&Atilde;O ALOCADA
															</c:otherwise>
														</c:choose>
													</div>
													<jsp:include page="../tags/observacao.jsp" />
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
								<c:when test="${!menuRequisicoesMostrarAvancado}">
									<h3>N&atilde;o existem requisi&ccedil;&otilde;es para o per&iacute;odo informado.</h3>
								</c:when>
								<c:otherwise>
									<h3>N&atilde;o existem requisi&ccedil;&otilde;es neste estado.</h3>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</p>
				
				<div class="gt-table-buttons">
					<a href="${linkTo[RequisicaoController].incluir}" class="gt-btn-medium gt-btn-left bt_edicao"><fmt:message key="views.botoes.incluir"/></a>
					<c:if test="${(exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo) && requisicoesTransporte.size() > 0}">
						<a href="#" id="missao_criar" class="gt-btn-medium gt-btn-left bt_edicao"><fmt:message key="views.botoes.criarMissao"/></a>
						<a href="#" id="missao_inicioRapido" class="gt-btn-medium gt-btn-left bt_missao"><fmt:message key="views.botoes.inicioRapido"/></a>
					</c:if>
					<a href="#" id="missao_programar" class="gt-btn-medium gt-btn-left bt_missao"><fmt:message key="views.botoes.programar"/></a>
					<a href="#" id="missao_cancelar" class="gt-btn-medium gt-btn-left bt_missao"><fmt:message key="views.botoes.cancelar"/></a>
				</div>
			</form>
			
		</div>
	</div>
</siga:pagina>