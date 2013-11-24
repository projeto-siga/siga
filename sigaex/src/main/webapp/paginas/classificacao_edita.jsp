<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
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
		<c:set var="titulo_pagina" value="Editar Classificação - ${codificacao}"></c:set>
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
					<ww:form id="frmClassificacao" action="gravar" method="aGravar">
						<input type="hidden" id="acao" name="acao" value="${acao}" />
						<div class="gt-left-col">
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Codificação</label><input id="codificacao" name="codificacao" type="text" value="${exClass.codificacao}" onblur="javascript:aplicarMascara(this)" size="11"/>
								<input type="hidden" id="codificacaoAntiga" name="codificacaoAntiga" value="${exClass.codificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descrClassificacao" name="descrClassificacao" type="text" value="${exClass.descrClassificacao}" size="100"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Observação</label> <input id="obs" name="obs" type="text" value="${exClass.obs}" size="100"/>
							</div>
							<!-- /form row -->
						</div>
					</ww:form>
				</div>
				<div class="botoesClassificacao">
					<!-- form row -->
					<div class="gt-form-row">
						<a id="btGravarClassificacao" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarClassificacao()">Gravar</a>
							<p class="gt-cancel">
							<c:choose>
								<c:when test="${acao == 'editar_classificacao'}">
										ou <ww:a href="excluir.action?codificacao=${exClass.codificacao}">excluir</ww:a>
								</c:when>
								<c:otherwise>
										ou <ww:a href="listar.action">cancelar</ww:a>
								</c:otherwise>
							</c:choose>
							</p>
						
					</div>
					<!-- /form row -->
					<c:if test="${acao == 'editar_classificacao'}">
						<ww:set value="%{exibirAdicaoDeVia()}" name="exibirBotaoAddVia" />
						<c:if test="${exibirBotaoAddVia}">
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
		<c:if test="${numeroDeVias > 0}">
			<div class="gt-bd clearfix">
				<div class="gt-content">
					<h2 class="gt-form-head">Vias</h2>
				</div>
			</div>
		</c:if>
		
		<div id="divVias">
		
			<c:forEach items="${exClass.exViaSet}" var="via">
			
			
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
								<c:if test="${via.codVia == numeroDeVias}">
									<p class="gt-cancel">
										ou <ww:a href="excluirVia.action?idVia=${via.id}&codificacao=${codificacao}">excluir via</ww:a>
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
									<ww:form id="frmEdicaoVia_${via.id}" action="gravarVia" method="aGravarVia">
										<input type="hidden" name="codificacao" value="${exClass.codificacao}"/>
										<input type="hidden" name="idVia" value="${via.id}"/>
										<input type="hidden" name="codigoVia" value="${via.codVia}"/>
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
											<label>Observação</label> <input id="obs" name="obs" type="text" value="${via.obs}" size="100"/>
										</div>
										<!-- /form row -->
									</ww:form>
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
									<ww:form id="frmNovaVia" action="gravarVia" method="aGravarVia">
										<input type="hidden" name="codificacao" value="${exClass.codificacao}"/>
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<h3 class="gt-form-head">Nova Via</h3>
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destino</label><ww:select name="idDestino" list="listaExTipoDestinacao"
														listKey="idTpDestinacao" listValue="descrTipoDestinacao" headerKey="-1" headerValue="[Escolha uma opção]"  
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Corrente</label><ww:select name="idTemporalidadeArqCorr" list="listaExTemporalidade"
														listKey="idTemporalidade" listValue="descTemporalidade" headerKey="-1" headerValue="[Sem valor]"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Intermediário</label><ww:select name="idTemporalidadeArqInterm" list="listaExTemporalidade"
														listKey="idTemporalidade" listValue="descTemporalidade" headerKey="-1" headerValue="[Sem valor]"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destinação Final</label><ww:select name="idDestinacaoFinal" list="listaExTipoDestinacao"
														listKey="idTpDestinacao" listValue="descrTipoDestinacao" headerKey="-1" headerValue="[Sem valor]"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Observação</label> <input id="obs" name="obs" type="text" />
										</div>
										<!-- /form row -->
									</ww:form>
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
