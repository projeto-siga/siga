<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>



<siga:pagina titulo="Classificação Documental">

<script src="/siga/javascript/mascara.js"></script>
<script type="text/javascript">
	var elementosComMascara = ['#codificacao'];		
</script>



<script type="text/javascript">

	//funções de dados
	function gravarClassificacao(){
		if($('#codificacaoAntiga').val() != "" && $('#codificacao').val() != $('#codificacaoAntiga').val()){
			confirma = window.confirm('Você está alterando o código da classificação fazendo com que esta subárvore seja movida para outro ponto. Você tem ABSOLUTA certeza?');
			if (!confirma){
				return;
			}
		}
		
		$('#frmClassificacao').submit();
		
	}
	
	function gravarExVia(idVia){
		if (idVia==null || idVia==0){
			$('#frmNovaVia').submit();
		}else{
			$('#frmEdicaoVia_'+idVia).submit();
		}
	}

	//funções de exibição
	var delay = 400;
	function adicionarVia(){
		ocultarBotoesClassificacao();
		ocultarBotoesViasExistentes();
		$('#divNovaVia').show(delay);

		var targetOffset = $('#divNovaVia').offset().top;
		$('html,body').animate({scrollTop: targetOffset}, 1000);
				
	}

	function cancelarNovaVia(){
		exibirBotoesClassificacao();
		$('#divNovaVia').hide(delay);
		exibirBotoesViasExistentes();
		return false;
	}
	function cancelarEdicaoVia(idVia){
		$('#divViaEdicao_' + idVia).hide(delay);
		$('#divViaExibicao_' + idVia).show(delay);
		exibirBotoesClassificacao();
		exibirBotoesViasExistentes();
		
		return false;
	}
	function editarExVia(idVia){
		ocultarBotoesClassificacao();
		ocultarBotoesViasExistentes();
		$('#divViaExibicao_' + idVia).hide(delay);
		$('#divViaEdicao_' + idVia).show(delay);
	}
	function ocultarBotoesClassificacao(){
		$('.botoesClassificacao').hide(delay);
		$('#divClassificacao :input').attr("disabled", true);
	}
	function exibirBotoesClassificacao(){
		$('.botoesClassificacao').show(delay);
		$('#divClassificacao :input').attr("disabled", false);
	}
	function ocultarBotoesViasExistentes(){
		$('#divVias .botoesVias').hide(delay);
	}
	function exibirBotoesViasExistentes(){
		$('#divVias .botoesVias').show(delay);
	}
</script>

<input id="mask_in" type="hidden" value="${mascaraEntrada}"/>
<input id="mask_out" type="hidden" value="${mascaraSaida}">
<input id="mask_js" type="hidden" value="${mascaraJavascript}">

<c:choose>
	<c:when test="${acao == 'editar_classificacao'}">
		<c:set var="titulo_pagina" value="Editar Classificação - ${exClassificacao.codificacao}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="titulo_pagina" value="Nova Classificação"></c:set>
	</c:otherwise>
</c:choose>

	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-form-head">${titulo_pagina}</h2>

			<div class="gt-form gt-content-box">
				<div id="divClassificacao" class="clearfix">
					<!-- left column -->
					<form id="frmClassificacao" action="gravar" method="get">
						<input type="hidden" id="acao" name="acao" value="${acao}" />
						<div class="gt-left-col">
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Codificação</label><input id="codificacao" name="exClassificacao.codificacao" type="text" value="${exClassificacao.codificacao}" onblur="javascript:aplicarMascara(this)" size="11"/>
								<input type="hidden" id="codificacaoAntiga" name="codificacaoAntiga" value="${exClassificacao.codificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descrClassificacao" name="exClassificacao.descrClassificacao" type="text" value="${exClassificacao.descrClassificacao}" size="100"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Observação</label> <input id="obs" name="exClassificacao.obs" type="text" value="${exClassificacao.obs}" size="100"/>
							</div>
							<!-- /form row -->
						</div>
					</form>
				</div>
				<div class="botoesClassificacao">
					<!-- form row -->
					<div class="gt-form-row">
						<a id="btGravarClassificacao" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarClassificacao()">Gravar</a>
							<p class="gt-cancel">
							<c:choose>
								<c:when test="${acao == 'editar_classificacao'}">
										ou <a href="excluir?codificacao=${exClassificacao.codificacao}">excluir</a>
								</c:when>
								<c:otherwise>
										ou <a href="listar">cancelar</a>
								</c:otherwise>
							</c:choose>
							</p>
						
					</div>
					<!-- /form row -->
					<c:if test="${acao == 'editar_classificacao'}">
						<c:if test="${exibirAdicaoDeVia}">
							<!-- form row -->
							<div class="gt-form-row">
								<a id="btAddVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:adicionarVia()">Adicionar Via</a>
							</div>
						</c:if>
						<!-- /form row -->
					</c:if>
				</div>
			</div>
		</div>
	</div>
	
	<c:if test="${acao == 'editar_classificacao'}">
		<c:if test="${exClassificacao.numVias > 0}">
			<div class="gt-bd clearfix">
				<div class="gt-content">
					<h2 class="gt-form-head">Vias</h2>
				</div>
			</div>
		</c:if>
		
		<div id="divVias">
		
			<c:forEach items="${exClassificacao.exViaSet}" var="via">
			
			
			<!--  VIA EXISTENTE -->
			<div id="divVia_${via.id}" class="viasExistentes">
			
			<!-- EXIBICAO VIA -->
				<div id="divViaExibicao_${via.id}" class="gt-bd clearfix">
					<div class="gt-content">
			
						<div class="gt-form gt-content-box">
							<div class="clearfix">
								<!-- left column -->
									<div class="gt-left-col">
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<h3 class="gt-form-head">Via ${via.codVia}</h3>
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destino</label>${via.exTipoDestinacao.descrTipoDestinacao}
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Corrente</label>
											<c:choose>
												<c:when test="${empty via.temporalidadeCorrente.descTemporalidade}">
												[Sem valor]
												</c:when>
												<c:otherwise>${via.temporalidadeCorrente.descTemporalidade}</c:otherwise>
											</c:choose>
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Intermediário</label>
											<c:choose>
												<c:when test="${empty via.temporalidadeIntermediario.descTemporalidade}">
												[Sem valor]
												</c:when>
												<c:otherwise>${via.temporalidadeIntermediario.descTemporalidade}</c:otherwise>
											</c:choose>
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destinação Final</label>${via.exDestinacaoFinal.descrTipoDestinacao}
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Observação</label>
											<c:choose>
												<c:when test="${empty via.obs or via.obs==''}">
													Sem observações definidas
												</c:when>
												<c:otherwise>
													${via.obs}
												</c:otherwise>
											</c:choose>
										</div>
										<!-- /form row -->
									</div>
							</div>
							<!-- form row -->
							<div class="gt-form-row botoesVias">
								<a id="btEditarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:editarExVia(${via.id})">Editar Via</a>
								<c:if test="${via.codVia == exClassificacao.numVias}">
									<p class="gt-cancel">
										ou <a href="excluirVia?idVia=${via.id}&codificacao=${exClassificacao.codificacao}&acao=${acao}">excluir via</a>
									</p>
								</c:if>
							</div>
							<!-- /form row -->
						</div>
					</div>
				</div>
				
				<!-- EXIBICAO VIA -->
				
				<!-- EDICAO VIA -->
				<div id="divViaEdicao_${via.id}" class="gt-bd clearfix" style="display: none">
					<div class="gt-content">
						<div class="gt-form gt-content-box">
							<div class="clearfix">
								<!-- left column -->
									<div class="gt-left-col">
									<form id="frmEdicaoVia_${via.id}" action="gravarVia" method="post">
										<input type="hidden" id="acao" name="acao" value="${acao}" />
										<input type="hidden" name="codificacao" value="${exClassificacao.codificacao}"/>
										<input type="hidden" name="via.id" value="${via.id}"/>
										<input type="hidden" name="via.codVia" value="${via.codVia}"/>
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<h3 class="gt-form-head">Edição da Via ${via.codVia}</h3>
										</div>
										<!-- /form row -->

										<!-- form row -->
										<div class="gt-form-row gt-width-66">

											<label>Destino</label>
											<select id="idDestino" name="idDestino"> 
												<option value="-1">[Escolha uma opção]</option>
												<c:forEach items="${listaExTipoDestinacao}" var="itemLista">
													<c:choose>
														<c:when test="${itemLista.idTpDestinacao == via.exTipoDestinacao.idTpDestinacao}">
															<option value="${itemLista.idTpDestinacao}" selected="selected">${itemLista.descrTipoDestinacao}</option>
														</c:when>
														<c:otherwise>
															<option value="${itemLista.idTpDestinacao}">${itemLista.descrTipoDestinacao}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
											
										</div>
										<!-- /form row -->
										
										<!-- form row -->
										<div class="gt-form-row gt-width-66">

											<label>Arquivo Corrente</label>
											<select id="idTemporalidadeArqCorr" name="idTemporalidadeArqCorr"> 
												<option value="-1">[Sem valor]</option>
												<c:forEach items="${listaExTemporalidade}" var="itemLista">
													<c:choose>
														<c:when test="${itemLista.idTemporalidade == via.temporalidadeCorrente.idTemporalidade}">
															<option value="${itemLista.idTemporalidade}" selected="selected">${itemLista.descTemporalidade}</option>
														</c:when>
														<c:otherwise>
															<option value="${itemLista.idTemporalidade}">${itemLista.descTemporalidade}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
											
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">

											<label>Arquivo Intermediário</label>
											<select id="idTemporalidadeArqInterm" name="idTemporalidadeArqInterm"> 
												<option value="-1">[Sem valor]</option>
												<c:forEach items="${listaExTemporalidade}" var="itemLista">
													<c:choose>
														<c:when test="${itemLista.idTemporalidade == via.temporalidadeIntermediario.idTemporalidade}">
															<option value="${itemLista.idTemporalidade}" selected="selected">${itemLista.descTemporalidade}</option>
														</c:when>
														<c:otherwise>
															<option value="${itemLista.idTemporalidade}">${itemLista.descTemporalidade}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>

										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
										
											<label>Destinação Final</label>
											<select id="idDestinacaoFinal" name="idDestinacaoFinal"> 
												<option value="-1">[Sem valor]</option>
												<c:forEach items="${listaExTipoDestinacao}" var="itemLista">
													<c:choose>
														<c:when test="${itemLista.idTpDestinacao == via.exDestinacaoFinal.idTpDestinacao}">
															<option value="${itemLista.idTpDestinacao}" selected="selected">${itemLista.descrTipoDestinacao}</option>
														</c:when>
														<c:otherwise>
															<option value="${itemLista.idTpDestinacao}">${itemLista.descrTipoDestinacao}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
											
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Observação</label> <input id="obs" name="via.obs" type="text" value="${via.obs}" size="100"/>
										</div>
										<!-- /form row -->
									</form>
								</div>
							</div>
							<!-- form row -->
							<div class="gt-form-row">
								<a id="btGravarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarExVia(${via.idVia})">Gravar Via</a>
								<p class="gt-cancel">
									ou <a href="javascript:void(0);" onclick="javascript:cancelarEdicaoVia(${via.idVia});" style="cursor: pointer;">cancelar edição</a>
								</p>
							</div>
							<!-- /form row -->
						</div>
					</div>
				</div>
				
				<!-- EDICAO VIA -->
			</div>	
			<!-- VIA EXISTENTE -->


			</c:forEach>
			
			<!--      NOVA VIA		-->
			<div id="divNovaVia" style="display: none;">
				<div class="gt-bd clearfix">
					<div class="gt-content">
						<div class="gt-form gt-content-box">
							<div class="clearfix">
								<!-- left column -->
									<div class="gt-left-col">
									<form id="frmNovaVia" action="gravarVia" method="post">
										<input type="hidden" id="acao" name="acao" value="${acao}" />
										<input type="hidden" name="codificacao" value="${exClassificacao.codificacao}"/>
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<h3 class="gt-form-head">Nova Via</h3>
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destino</label>
														
											<select  name="idDestino" theme="simple">
												<option value="-1" ${-1 == idTpDestinacao ? 'selected' : ''}>
													[Escolha uma opção]
												</option>  											
												<c:forEach items="${listaExTipoDestinacao}" var="item">
													<option value="${item.idTpDestinacao}" ${item.idTpDestinacao == idTpDestinacao ? 'selected' : ''}>
														${item.descrTipoDestinacao}
													</option>  
												</c:forEach>
											</select>														
														
														
														
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Corrente</label>
											<select  name="idTemporalidadeArqCorr" theme="simple">
												<option value="-1" ${-1 == idTemporalidadeArqCorr ? 'selected' : ''}>
													[Sem valor]
												</option>  											
												<c:forEach items="${listaExTemporalidade}" var="item">
													<option value="${item.idTemporalidade}" ${item.idTemporalidade == idTemporalidadeArqCorr ? 'selected' : ''}>
														${item.descTemporalidade}
													</option>  
												</c:forEach>
											</select>											
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Intermediário</label>
										
											<select  name="idTemporalidadeArqInterm" theme="simple">
												<option value="-1" ${-1 == idTemporalidadeArqInterm ? 'selected' : ''}>
													[Sem valor]
												</option>  											
												<c:forEach items="${listaExTemporalidade}" var="item">
													<option value="${item.idTemporalidade}" ${item.idTemporalidade == idTemporalidadeArqInterm ? 'selected' : ''}>
														${item.descTemporalidade}
													</option>  
												</c:forEach>
											</select>											
											
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destinação Final</label>
											<select  name="idDestinacaoFinal" theme="simple">
												<option value="-1" ${-1 == idTpDestinacaoFinal ? 'selected' : ''}>
													[Sem valor]
												</option>  											
												<c:forEach items="${listaExTipoDestinacao}" var="item">
													<option value="${item.idTpDestinacao}" ${item.idTpDestinacao == idTpDestinacaoFinal ? 'selected' : ''}>
														${item.descrTipoDestinacao}
													</option>  
												</c:forEach>
											</select>											
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Observação</label> <input id="obs" name="via.obs" type="text" />
										</div>
										<!-- /form row -->
									</form>
								</div>
							</div>
							<!-- form row -->
							<div class="gt-form-row">
								<a id="btGravarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarExVia(0)">Gravar Via</a>
								<p class="gt-cancel">
									ou <a href="javascript:void(0);" onclick="javascript:cancelarNovaVia();" style="cursor: pointer;">cancelar via</a>
								</p>
							</div>
							<!-- /form row -->
						</div>
					</div>
				</div>
			</div>	
			<!--      NOVA VIA		-->
			
		</div>
	</c:if>
	

	
	
</siga:pagina>