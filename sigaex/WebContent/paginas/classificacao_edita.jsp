<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Classificação Documental">
<script type="text/javascript">
	//funções de dados
	function gravarClassificacao(){
		$('#frmClassificacao').submit();
	}
	function gravarExVia(){
		$('#frmNovaVia').submit();
	}

	//funções de exibição
	var delay = 400;
	function adicionarVia(){
		ocultarBotoesClassificacao();
		$('#divNovaVia').show(delay);
		ocultarBotoesViasExistentes();
	}

	function cancelarNovaVia(){
		exibirBotoesClassificacao();
		$('#divNovaVia').hide(delay);
		exibirBotoesViasExistentes();
		return false;
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
<c:choose>
	<c:when test="${acao == 'editar_classificacao'}">
		<c:set var="titulo_pagina" value="Editar Classificação - ${codificacao}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="titulo_pagina" value="Nova Classificação"></c:set>
	</c:otherwise>
</c:choose>

	<h2 class="gt-table-head">Classificação Documental</h2>
	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-form-head">${titulo_pagina}</h2>

			<div class="gt-form gt-content-box">
				<div id="divClassificacao" class="clearfix">
					<!-- left column -->
					<ww:form id="frmClassificacao" action="gravar" method="aGravar">
						<div class="gt-left-col">
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Codificação</label><input id="codificacao" name="codificacao" type="text" class="gt-form-text" value="${exClass.codificacao}"/>
								<input type="hidden" id="codificacaoAntiga" name="codificacaoAntiga" value="${exClass.codificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Descrição</label> <input id="descrClassificacao" name="descrClassificacao" type="text" class="gt-form-text" value="${exClass.descrClassificacao}"/>
							</div>
							<!-- /form row -->
	
							<!-- form row -->
							<div class="gt-form-row gt-width-66">
								<label>Observação</label> <input id="obs" name="obs" type="text" class="gt-form-text" value="${exClass.obs}"/>
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
						<!-- form row -->
						<div class="gt-form-row">
							<a id="btAddVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:adicionarVia()">Adicionar Via</a>
						</div>
						<!-- /form row -->
					</c:if>
				</div>
			</div>
		</div>
	</div>
	
	<c:if test="${acao == 'editar_classificacao'}">
		<c:if test="${numeroDeVias > 0}">
			<h2 class="gt-form-head">Vias</h2>
		</c:if>
		
		<div id="divVias">
		
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
														listKey="idTpDestinacao" listValue="descrTipoDestinacao" headerKey="-1" headerValue="Escolha uma opção"  
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Corrente</label><ww:select name="idTemporalidadeArqCorr" list="listaExTemporalidade"
														listKey="idTemporalidade" listValue="descTemporalidade" headerKey="-1" headerValue="Escolha uma opção"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Intermediário</label><ww:select name="idTemporalidadeArqInterm" list="listaExTemporalidade"
														listKey="idTemporalidade" listValue="descTemporalidade" headerKey="-1" headerValue="Escolha uma opção"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Destinação Final</label><ww:select name="idDestinacaoFinal" list="listaExTipoDestinacao"
														listKey="idTpDestinacao" listValue="descrTipoDestinacao" headerKey="-1" headerValue="Escolha uma opção"
														theme="simple" />
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Observação</label> <input id="obs" name="obs" type="text" class="gt-form-text"/>
										</div>
										<!-- /form row -->
									</ww:form>
								</div>
							</div>
							<!-- form row -->
							<div class="gt-form-row">
								<a id="btGravarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:gravarExVia()">Gravar Via</a>
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
			
			
			<c:forEach items="${exClass.exViaSet}" var="via">
			
			
			<!--  VIA EXISTENTE -->
			<div id="divVia_${via.id}" class="viasExistentes">
				<div class="gt-bd clearfix">
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
											<label>Arquivo Corrente</label>${via.temporalidadeCorrente.descTemporalidade}
										</div>
										<!-- /form row -->
										<!-- form row -->
										<div class="gt-form-row gt-width-66">
											<label>Arquivo Intermediário</label>${via.temporalidadeIntermediario.descTemporalidade}
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
								<a id="btEditarVia" class="gt-btn-large gt-btn-left" style="cursor: pointer;" onclick="javascript:editarVia()">Editar Via</a>
								<p class="gt-cancel">
									ou <ww:a href="excluirVia.action?idVia=${via.id}&codificacao=${codificacao}">excluir via</ww:a>
								</p>
							</div>
							<!-- /form row -->
						</div>
					</div>
				</div>
			</div>	
			<!-- VIA EXISTENTE -->


			</c:forEach>
		</div>
	</c:if>
	

	
	
</siga:pagina>
