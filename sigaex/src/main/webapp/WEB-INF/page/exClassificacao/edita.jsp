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
		$('#botoesClassificacao').hide(delay);
		$('#divClassificacao :input').attr("disabled", true);
	}
	function exibirBotoesClassificacao(){
		$('#botoesClassificacao').show(delay);
		$('#divClassificacao :input').attr("disabled", false);
	}
	function ocultarBotoesViasExistentes(){
		$('#divVias .botoesVias').hide(delay);
	}
	function exibirBotoesViasExistentes(){
		$('#divVias .botoesVias').show(delay);
	}
</script>

	<input id="mask_in" type="hidden" value="${mascaraEntrada}" />
	<input id="mask_out" type="hidden" value="${mascaraSaida}">
	<input id="mask_js" type="hidden" value="${mascaraJavascript}">

	<c:choose>
		<c:when test="${acao == 'editar_classificacao'}">
			<c:set var="titulo_pagina"
				value="Editar Classificação - ${exClassificacao.codificacao}"></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="titulo_pagina" value="Nova Classificação"></c:set>
		</c:otherwise>
	</c:choose>


	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo_pagina}</h5>
			</div>
			<div id="divClassificacao" class="card-body">
				<!-- left column -->
				<form id="frmClassificacao" action="gravar" method="get">
					<input type="hidden" id="acao" name="acao" value="${acao}" />
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label>Codificação</label> <input id="codificacao"
									class="form-control" name="exClassificacao.codificacao"
									type="text" value="${exClassificacao.codificacao}"
									onblur="javascript:aplicarMascara(this)" /> <input
									type="hidden" id="codificacaoAntiga" name="codificacaoAntiga"
									value="${exClassificacao.codificacao}" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Descrição</label> <input id="descrClassificacao"
									name="exClassificacao.descrClassificacao" type="text"
									value="${exClassificacao.descrClassificacao}"
									class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Observação</label> <input id="obs"
									name="exClassificacao.obs" type="text"
									value="${exClassificacao.obs}" class="form-control" />
							</div>
						</div>
					</div>
				</form>
				<div id="botoesClassificacao">
					<div class="row">
						<div class="col-sm">
							<button type="button" class="btn btn-primary"
								id="btGravarClassificacao"
								onclick="javascript:gravarClassificacao()">Gravar</button>
							<c:choose>
								<c:when test="${acao == 'editar_classificacao'}">
									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao"
										onclick="javascript:location.href='excluir?codificacao=${exClassificacao.codificacao}'">Excluir</button>
									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao"
										onclick="javascript:location.href='listar'">Voltar</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao"
										onclick="javascript:location.href='listar'">Cancelar</button>
								</c:otherwise>
							</c:choose>
							<c:if test="${acao == 'editar_classificacao'}">
								<c:if test="${exibirAdicaoDeVia}">

									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao" onclick="javascript:adicionarVia()">Adicionar
										Via</button>

								</c:if>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>

		<c:if test="${acao == 'editar_classificacao'}">
			<c:if test="${exClassificacao.numVias gt 0}">
				<h5>Vias</h5>

				<div id="divVias">
					<table class="table table-striped table-hover pt-2">
						<thead class="${thead_color}">
							<th>Via</th>
							<th>Destino</th>
							<th>Arquivo Corrente</th>
							<th>Arquivo Intermediário</th>
							<th>Destinação Final</th>
							<th>Observação</th>
							<th>Ação</th>
						</thead>

						<tbody>
							<c:forEach items="${exClassificacao.exViaSet}" var="via">
								<tr id="divVia_${via.id}" class="viasExistentes">
									<td>${via.codVia}</td>
									<td>${via.exTipoDestinacao.descrTipoDestinacao}</td>
									<td><c:choose>
											<c:when
												test="${empty via.temporalidadeCorrente.descTemporalidade}">
											[Sem valor]
											</c:when>
											<c:otherwise>${via.temporalidadeCorrente.descTemporalidade}</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when
												test="${empty via.temporalidadeIntermediario.descTemporalidade}">
											[Sem valor]
											</c:when>
											<c:otherwise>${via.temporalidadeIntermediario.descTemporalidade}</c:otherwise>
										</c:choose></td>
									<td>${via.exDestinacaoFinal.descrTipoDestinacao}</td>
									<td><c:choose>
											<c:when test="${empty via.obs or via.obs==''}">
												Sem observações definidas
											</c:when>
											<c:otherwise>
												${via.obs}
											</c:otherwise>
										</c:choose></td>
									<td><a id="btEditarVia" class="btn btn-warning text-white"
										style="cursor: pointer;"
										onclick="javascript:editarExVia(${via.id})" title="Editar Via"><i
											class="fa fa-edit"></i></a> <c:if
											test="${via.codVia == exClassificacao.numVias}">
											<a class="btn btn-danger"
												href="excluirVia?idVia=${via.id}&codificacao=${exClassificacao.codificacao}&acao=${acao}"
												title="Excluir Via"><i class="fa fa-trash-alt"></i></a>
										</c:if></td>
								</tr>

								<!-- EDICAO VIA -->
								<div id="divViaEdicao_${via.id}" style="display: none">
									<div class="card bg-light mb-3">
										<div class="card-header bg-light">
											<h5>Edição da Via ${via.codVia}</h5>
										</div>
										<div class="card-body">
											<form id="frmEdicaoVia_${via.id}" action="gravarVia"
												method="post">
												<input type="hidden" id="acao" name="acao" value="${acao}" />
												<input type="hidden" name="codificacao"
													value="${exClassificacao.codificacao}" /> <input
													type="hidden" name="idVia" value="${via.id}" />

												<div class="pr-2 pl-2">


													<div class="row">
														<div class="col-sm">
															<div class="form-group">
																<label>Destino</label> <select id="idDestino"
																	name="idDestino" class="custom-select">
																	<option value="-1">[Escolha uma opção]</option>
																	<c:forEach items="${listaExTipoDestinacao}"
																		var="itemLista">
																		<c:choose>
																			<c:when
																				test="${itemLista.idTpDestinacao == via.exTipoDestinacao.idTpDestinacao}">
																				<option value="${itemLista.idTpDestinacao}"
																					selected="selected">${itemLista.descrTipoDestinacao}</option>
																			</c:when>
																			<c:otherwise>
																				<option value="${itemLista.idTpDestinacao}">${itemLista.descrTipoDestinacao}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</select>
															</div>
														</div>
													</div>
													<div class="row">
														<div class="col-sm-6">
															<div class="form-group">
																<label>Arquivo Corrente</label> <select
																	id="idTemporalidadeArqCorr"
																	name="idTemporalidadeArqCorr" class="custom-select">
																	<option value="-1">[Sem valor]</option>
																	<c:forEach items="${listaExTemporalidade}"
																		var="itemLista">
																		<c:choose>
																			<c:when
																				test="${itemLista.idTemporalidade == via.temporalidadeCorrente.idTemporalidade}">
																				<option value="${itemLista.idTemporalidade}"
																					selected="selected">${itemLista.descTemporalidade}</option>
																			</c:when>
																			<c:otherwise>
																				<option value="${itemLista.idTemporalidade}">${itemLista.descTemporalidade}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</select>
															</div>
														</div>
														<div class="col-sm-6">
															<div class="form-group">
																<label>Arquivo Intermediário</label> <select
																	id="idTemporalidadeArqInterm"
																	name="idTemporalidadeArqInterm" class="custom-select">
																	<option value="-1">[Sem valor]</option>
																	<c:forEach items="${listaExTemporalidade}"
																		var="itemLista">
																		<c:choose>
																			<c:when
																				test="${itemLista.idTemporalidade == via.temporalidadeIntermediario.idTemporalidade}">
																				<option value="${itemLista.idTemporalidade}"
																					selected="selected">${itemLista.descTemporalidade}</option>
																			</c:when>
																			<c:otherwise>
																				<option value="${itemLista.idTemporalidade}">${itemLista.descTemporalidade}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</select>
															</div>
														</div>
													</div>
													<div class="row">
														<div class="col-sm">
															<div class="form-group">
																<label>Destinação Final</label> <select
																	id="idDestinacaoFinal" name="idDestinacaoFinal"
																	class="custom-select">
																	<option value="-1">[Sem valor]</option>
																	<c:forEach items="${listaExTipoDestinacao}"
																		var="itemLista">
																		<c:choose>
																			<c:when
																				test="${itemLista.idTpDestinacao == via.exDestinacaoFinal.idTpDestinacao}">
																				<option value="${itemLista.idTpDestinacao}"
																					selected="selected">${itemLista.descrTipoDestinacao}</option>
																			</c:when>
																			<c:otherwise>
																				<option value="${itemLista.idTpDestinacao}">${itemLista.descrTipoDestinacao}</option>
																			</c:otherwise>
																		</c:choose>
																	</c:forEach>
																</select>

															</div>
														</div>
													</div>
													<div class="row">
														<div class="col-sm">
															<div class="form-group">
																<label>Observação</label> <input id="obs" name="obsVia"
																	type="text" value="${via.obs}" size="100"
																	class="form-control" />
															</div>
														</div>
													</div>

													<div class="row pb-3 mb-3">
														<div class="col-sm">
															<button type="button" class="btn btn-primary"
																id="btGravarClassificacao"
																onclick="javascript:gravarExVia(${via.idVia});">Gravar
																Via</button>
															<button type="button" class="btn btn-primary"
																id="btGravarClassificacao"
																onclick="javascript:cancelarEdicaoVia(${via.idVia});">Cancelar</button>
														</div>
													</div>
												</div>
											</form>
										</div>
									</div>
								</div>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>





			<!--      NOVA VIA		-->
			<div id="divNovaVia" style="display: none;">
				<div class="card bg-light mb-3">
					<div class="card-header bg-light">
						<h5>Nova Via</h5>
					</div>
					<div class="card-body">
						<form id="frmNovaVia" action="gravarVia" method="post">
							<input type="hidden" id="acao" name="acao" value="${acao}" /> <input
								type="hidden" name="codificacao"
								value="${exClassificacao.codificacao}" />
							<div class="row">
								<div class="col-sm">
									<div class="form-group">
										<label>Destino</label> <select name="idDestino" theme="simple"
											class="custom-select">
											<option value="-1" ${-1 == idTpDestinacao ? 'selected' : ''}>
												[Escolha uma opção]</option>
											<c:forEach items="${listaExTipoDestinacao}" var="item">
												<option value="${item.idTpDestinacao}"
													${item.idTpDestinacao == idTpDestinacao ? 'selected' : ''}>
													${item.descrTipoDestinacao}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm">
									<div class="form-group">
										<label>Arquivo Corrente</label> <select
											name="idTemporalidadeArqCorr" theme="simple"
											class="custom-select">
											<option value="-1"
												${-1 == idTemporalidadeArqCorr ? 'selected' : ''}>
												[Sem valor]</option>
											<c:forEach items="${listaExTemporalidade}" var="item">
												<option value="${item.idTemporalidade}"
													${item.idTemporalidade == idTemporalidadeArqCorr ? 'selected' : ''}>
													${item.descTemporalidade}</option>
											</c:forEach>
										</select>

									</div>
								</div>
								<div class="col-sm">
									<div class="form-group">
										<label>Arquivo Intermediário</label> <select
											name="idTemporalidadeArqInterm" theme="simple"
											class="custom-select">
											<option value="-1"
												${-1 == idTemporalidadeArqInterm ? 'selected' : ''}>
												[Sem valor]</option>
											<c:forEach items="${listaExTemporalidade}" var="item">
												<option value="${item.idTemporalidade}"
													${item.idTemporalidade == idTemporalidadeArqInterm ? 'selected' : ''}>
													${item.descTemporalidade}</option>
											</c:forEach>
										</select>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm">
									<div class="form-group">
										<label>Destinação Final</label> <select
											name="idDestinacaoFinal" theme="simple" class="custom-select">
											<option value="-1"
												${-1 == idTpDestinacaoFinal ? 'selected' : ''}>[Sem
												valor]</option>
											<c:forEach items="${listaExTipoDestinacao}" var="item">
												<option value="${item.idTpDestinacao}"
													${item.idTpDestinacao == idTpDestinacaoFinal ? 'selected' : ''}>
													${item.descrTipoDestinacao}</option>
											</c:forEach>
										</select>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm">
									<div class="form-group">
										<label>Observação</label> <input id="obs" name="obsVia"
											type="text" class="form-control" />

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm">
									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao" onclick="javascript:gravarExVia(0)">Gravar</button>
									<button type="button" class="btn btn-primary"
										id="btGravarClassificacao"
										onclick="javascript:cancelarNovaVia();">Cancelar</button>
								</div>

							</div>
						</form>
					</div>
				</div>
				<!--      NOVA VIA		-->
		</c:if>

	</div>


	</div>
</siga:pagina>