<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="br.gov.jfrj.siga.sr.model.SrPrioridade"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrGravidade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<c:set var="gravidade_normal">
    <%=SrGravidade.NORMAL.name()%>
</c:set>

<c:set var="sem_gravidade">
    <%=SrGravidade.SEM_GRAVIDADE.name()%>
</c:set>

<c:set var="prioridade_baixo">
    <%=SrPrioridade.BAIXO.name()%>
</c:set>

<c:set var="prioridade_desc">
    <%=SrPrioridade.BAIXO.getDescPrioridade()%>
</c:set>

<siga:pagina titulo="Cadastro de Solicitação">
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/cronometro.js"></script>
	
	<style>
	.barra-subtitulo {
		color: #365b6d !important;
		border-bottom: 1px solid #ccc;
		border-radius: 0 !important;
		margin: 0 -15px 10px -15px;
	}
	
	.barra-subtitulo-top {
		border-radius: 5px 5px 0 0 !important;
		margin-top: -15px !important;
	}
	
	.tempo h3 {
		color: #365b6d;
		font-weight: normal;
		margin-bottom: 0px;
		font-size: 115.0%;
		border: 0;
	}
	</style>
	
	<script>
		var item_default = "";
		jQuery(document).ready(function($) {
			$('#gravar').click(function() {
				if ($('#siglaCadastrante').val() != $('#formulario_solicitacaosolicitante_sigla')[0].value) {
					var stringDt= $('#calendarioComunicacao').val() + ' ' + $('#horarioComunicacao').val();
					$('#stringDtMeioContato').val(stringDt);
				} 
			}); 
	
			$('#checkRascunho').change(function() {
				if(this.checked) {
					$('#checkRascunho').prop('value', 'true');
					return;
				}
				$('#checkRascunho').prop('value', 'false');
			});
			//inicializa valores default para serem usados na function valorInputMudou()
			item_default = $("#formulario_solicitacaoitemConfiguracao_id").val();
		});

		function postbackURL(){
			return '${linkTo[SolicitacaoController].editar}?'+$('#formSolicitacao').serialize();
		}
	
		// param_1: id do input que deseja verificar se mudou do valor default
		// param_2: tipo de input (solicitante, item...)
		function valorInputMudou(id, tipo){
			var input = $("#"+ id);
			if (input.val() != window[tipo + '_default']) {
				window[tipo + '_default'] = input.val(); 
				return true;
			}
			return false;
		}
		
		
		function toggleInterlocutorMeioComunicacaoEDataOrigem() {
			var siglaCadastrante = $('#siglaCadastrante').val();
			var siglaSolicitante = $('#formulario_solicitacaosolicitante_sigla')[0].value;
	
			if (siglaCadastrante == siglaSolicitante) {
				$('#interlocutor')[0].style.display='none';
				$('#meioComunicacaoEDataOrigem')[0].style.display='none';
			}
			else {
				$('#interlocutor')[0].style.display='inline-block';
				$('#meioComunicacaoEDataOrigem')[0].style.display='inline-block';
			}
			
			toggleDataOrigem();
		}

		function toggleDataOrigem(){
			if ($("#meioComunicacao").val() == 'EMAIL')
				$('#dataOrigem')[0].style.display='inline-block';
			else $('#dataOrigem')[0].style.display='none';
		}
		
	</script>
	
	<div class="gt-bd gt-cols clearfix">
		<div class="gt-content clearfix">
			<h2>
				<c:choose>
					<c:when test="${solicitacao.idSolicitacao != null || solicitacao.solicitacaoPai != null}">
						${solicitacao.codigo}
					</c:when>
					<c:otherwise>
						Cadastro de Solicita&ccedil;&atilde;o
					</c:otherwise>
				</c:choose>
			</h2>
			<div class="gt-content-box gt-for-table gt-form" style="margin-top: 15px;">
	
				<form action="${linkTo[SolicitacaoController].gravar}" method="post"
					enctype="multipart/form-data" id="formSolicitacao" onsubmit="javascript:return block();"> 
					<c:if test="${solicitacao.solicitacaoPai != null}">
						<input type="hidden" name="solicitacao.solicitacaoPai.id" 
						value="${solicitacao.solicitacaoPai.idSolicitacao}" /> 
					</c:if>
					
					<input type="hidden" name="solicitacao.id" id="idSol" value="${solicitacao.idSolicitacao}" /> 
					<input type="hidden" name="solicitacao.dtIniEdicaoDDMMYYYYHHMMSS" value="${solicitacao.dtIniEdicaoDDMMYYYYHHMMSS}" /> 
					<input type="hidden" name="solicitacao.numSolicitacao" value="${solicitacao.numSolicitacao}" />
					<input type="hidden" name="solicitacao.numSequencia" value="${solicitacao.numSequencia}" />
					
					<div class="gt-form-table">
						<div class="barra-subtitulo barra-subtitulo-top header" align="center" valign="top">
							Dados B&aacute;sicos
						</div>
					</div>
					
					<c:if test="${errors != null && !erros.isEmpty()}">
						<div class="gt-form-table">
					        <p class="gt-error">Alguns campos obrigat&oacute;rios n&atilde;o foram preenchidos</p>
						    <c:forEach items="${errors}" var="erro">
								<p class="gt-error">${erro.message}</p>
						    </c:forEach>
						</div>
					</c:if>
					
					<div class="gt-form-row gt-width-99">
						<label>Cadastrante</label> ${cadastrante.nomePessoa} <input
								type="hidden" id="siglaCadastrante" name="solicitacao.cadastrante.sigla"
								value="${cadastrante.sigla}" />
								<input type="hidden" id="idCadastrante" name="solicitacao.cadastrante.id" value="${cadastrante.idPessoa}" />
								<input type="hidden" name="solicitacao.lotaTitular.id" value="${lotaTitular != null ? lotaTitular.idLotacao:''}" />
								<input type="hidden" name="solicitacao.titular.id" value="${titular!= null ? titular.idPessoa:''}" />
					</div>	 
		
					<div class="gt-form-row gt-width-99">
						<label>Solicitante</label> 
						<siga:selecao2 tipo="pessoa"
							propriedade="solicitacao.solicitante"
							tema="simple"
							modulo="siga"
							reler="ajax"
							onchange="toggleInterlocutorMeioComunicacaoEDataOrigem();" />
						<siga:error name="solicitacao.solicitante"/>
					</div>
					<div class="gt-form-row gt-width-99" id="interlocutor" style="display: none;">
						<label>Interlocutor (Opcional)</label> 
						<siga:selecao2 tipo="pessoa"
							propriedade="solicitacao.interlocutor" 
							tema="simple"
							modulo="siga"/>
					</div>
					
					<div id="divLocalRamalEMeioContato" depende="solicitacao.solicitante">
						<script>

							//Edson: talvez fosse possível fazer de um modo melhor, mas assim é mais prático
							$("#solicitacaosolicitanteSpan").html("${solicitacao.solicitante.descricaoCompleta}");

							$("#calendarioComunicacao").datepicker({
					        	showOn: "button",
					        	buttonImage: "/siga/css/famfamfam/icons/calendar.png",
					        	buttonImageOnly: true,
						        dateFormat: 'dd/mm/yy'
					    	});
							$("#calendarioComunicacao").mask("99/99/9999");
							$("#horarioComunicacao").mask("99:99");
						</script>
						<c:if test="${locaisDisponiveis.size() > 1}">
							<div class="gt-form-row gt-width-99">
								<label>Local</label>
								<select name="solicitacao.local.id" id="local" onchange="sbmt('solicitacao.local')">
        						<c:forEach items="${locaisDisponiveis.keySet()}" var="orgao">
                						<optgroup label="${orgao.acronimoOrgaoUsu}">
                						<c:forEach items="${locaisDisponiveis.get(orgao)}" var="local">
                        						<option value="${local.idComplexo}" ${solicitacao.local.idComplexo.equals(local.idComplexo) ? 'selected' : ''}>${local.nomeComplexo}</option>
                						</c:forEach>
                						</optgroup>
        						</c:forEach>
								</select>
							</div>
							</c:if>
						
						<div class="gt-form-row gt-width-66">
							<label>Telefone</label> 
							<input type="text" name="solicitacao.telPrincipal" id="telPrincipal" value="${solicitacao.telPrincipal}" maxlength="255" />
						</div>

						<div id="meioComunicacaoEDataOrigem">
							<div class="gt-form-row gt-width-66">
								<label>Origem da Demanda</label>
							
								<siga:select id="meioComunicacao" 
                     				name="solicitacao.meioComunicacao" 
                     				list="meiosComunicadaoList" 
                     				listValue="descrMeioComunicacao" 
                     				listKey="idTipoContato"
                     				value="${solicitacao.meioComunicacao}"
                     				isEnum="true"
                     				onchange="toggleDataOrigem();" />
							</div>
							<div id="dataOrigem" class="gt-form-row gt-width-66" style="margin-left: 15px;">
								<div >
									<label>Contato inicial</label> 
									<label>Data</label> 
									<input type="text" name="calendario" id="calendarioComunicacao" value="${solicitacao.dtOrigemDDMMYYYY}" /> 
									<siga:error name="calendario"/>
								</div>
								<div style="padding-top: 5px;">
									<label>Hora</label> 
		    						<input type="text" name="horario" id="horarioComunicacao" value="${solicitacao.dtOrigemHHMM}" />
		    						<siga:error name="horario"/>
								</div>
							</div>
							<input type="hidden" name="solicitacao.dtOrigemString" id="stringDtMeioContato" value="${solicitacao.dtOrigemDDMMYYYYHHMM}" />
						</div>
						<script>
						toggleInterlocutorMeioComunicacaoEDataOrigem();
						</script>
					</div>

					<div class="gt-form-row gt-width-66">
						<label>Quando deseja receber notifica&ccedil;&atilde;o dessa solicita&ccedil;&atilde;o por e-mail?</label>
						<siga:select name="solicitacao.formaAcompanhamento" list="formaAcompanhamentoList"
							listValue="descrFormaAcompanhamento" listKey="idFormaAcompanhamento" isEnum="true"
							value="${solicitacao.formaAcompanhamento != null ? solicitacao.formaAcompanhamento : ''}" />
					</div>	
						
					<div class="gt-form-table">
						<div class="barra-subtitulo header" align="center" valign="top">
							Detalhamento
						</div>
					</div>
		
					<div id="divItem" depende="solicitacao.solicitante;solicitacao.local">
						<c:if test="${not empty solicitacao.solicitante}">
							<script>
							function dispararFuncoesOnBlurItem() {
								if (valorInputMudou('formulario_solicitacaoitemConfiguracao_id', 'item')) {
									sbmt("solicitacao.itemConfiguracao");
									removeSelectedDuplicado();
								}	
							}
							
							</script>
							
							<div class="gt-form-row gt-width-66" >
								<label>Produto, Servi&ccedil;o ou Sistema relacionado à Solicita&ccedil;&atilde;o</label>
								<siga:selecao2 propriedade="solicitacao.itemConfiguracao" 
											tipo="itemConfiguracao" 
											tema="simple" 
											modulo="sigasr" 
											tamanho="grande"
											onchange="dispararFuncoesOnBlurItem();"
											paramList="sol.solicitante.id=${solicitacao.solicitante.idPessoa};sol.local.id=${solicitacao.local.idComplexo};sol.titular.id=${titular.idPessoa};sol.lotaTitular.id=${lotaTitular.idLotacao}"/>
								<siga:error name="solicitacao.itemConfiguracao"/>
							</div>
							
							<div id="divAcao" depende="solicitacao.itemConfiguracao">
								<script>						
								function removeSelectedDuplicado() {
									//solucao de contorno temporaria para op??es no select com mesmo value.
									var primeiro = $("#selectAcao option:eq(0)");
									var segundo = $("#selectAcao option:eq(1)");
									if (primeiro.val() == segundo.val()) {
										segundo.prop("selected", false);
										primeiro.prop("selected", true);
									}
								}
								
								function carregarLotacaoDaAcao(){
									//preenche o campo atendente com a lotacao designada a cada alteracao da acao 
									var opcaoSelecionada = $("#selectAcao option:selected");
									var idAcao = opcaoSelecionada.val();
									try{
										var siglaLotacao = opcaoSelecionada.html().split("(")[1].slice(0,-2);
								
										var spanLotacao = $(".lotacao-" + idAcao + ":contains(" + siglaLotacao + ")");
										var descLotacao = spanLotacao.html();
										var idDesignacaoDaAcao = spanLotacao.prev().html();
								
										$("#solicitacaoDesignacao").val(idDesignacaoDaAcao);
										$("#atendentePadrao").html(descLotacao);
										$("#labelAtendentePadrao").show();
									} catch(err) {
										$("#solicitacaoDesignacao").val('');
										$("#atendentePadrao").html('');
										$("#labelAtendentePadrao").hide();
									}
								}
								</script>
								<c:if test="${solicitacao.itemConfiguracao != null && podeUtilizarServicoSigaGC}">
									<div style="display: inline-block" >
										<div id="gc-ancora-item"></div>
									</div>
									<!-- CONHECIMENTOS RELACIONADOS -->
									<script type="text/javascript">
									var url = "/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&podeCriar=${exibirMenuConhecimentos}&msgvazio=empty&titulo=${f:urlEncode(solicitacao.itemConfiguracao.tituloItemConfiguracao)}${solicitacao.itemConfiguracao.gcTagAbertura}";
									Siga.ajax(url, null, "GET", function(response){
										$("#gc-ancora-item").html(response);
									});
									</script>
								</c:if>
								
								<c:set var="acoesEAtendentes" value="${solicitacao.acoesEAtendentes}" />
								<c:if test="${not empty acoesEAtendentes}" > 
									<div class="gt-form-row gt-width-66" style="margin-top: 10px;">
										<label>A&ccedil;&atilde;o</label>   
										<select name="solicitacao.acao.id" id="selectAcao" onchange="carregarLotacaoDaAcao();sbmt('solicitacao.acao');">
											<option value=""></option>
											<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
												<optgroup label="${cat.tituloAcao}">
													<c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
														<option value="${tarefa.acao.idAcao}" ${solicitacao.acao.idAcao.equals(tarefa.acao.idAcao) ? 'selected' : ''}>${tarefa.acao.tituloAcao} (${tarefa.conf.atendente.siglaCompleta})</option>
													</c:forEach>
												</optgroup>                  
											</c:forEach>
										</select> 
										<siga:error name="solicitacao.acao"/>
									</div>
									<div class="gt-form-row gt-width-66" style="margin-top: 10px;">
										<%-- Necessario listar novamente a lista "acoesEAtendentes" para ter a lotacao designada da cada acao
												ja que acima no select nao tem como "esconder" essa informacao --%>
										<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
											<c:forEach items="${acoesEAtendentes.get(cat)}" var="t">
												<span class="idDesignacao-${t.acao.idAcao}" style="display:none;">${t.conf.idConfiguracao}</span>
												<span class="lotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.siglaCompleta} - ${t.conf.atendente.descricao}</span>
											</c:forEach>
										</c:forEach>
										<label id="labelAtendentePadrao" style="display: none">Atendente</label>
										<span id="atendentePadrao" style="display:block;" style="margin-bottom: 10px;">
										</span>
										<input type="hidden" id="solicitacaoDesignacao" name="solicitacao.designacao.id" value="${idDesignacao}" />
										<script>carregarLotacaoDaAcao();</script>
									</div>
								</c:if>
								
								<div id="divAtributos" depende="solicitacao.acao">
									<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
									
									<c:if test="${not empty solicitacao.itemConfiguracao && not empty solicitacao.acao && podeUtilizarServicoSigaGC}">
										<div style="display: inline-block" >
											<div id="gc-ancora-item-acao"></div>
										</div>
										<script type="text/javascript">
										var url = "/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&podeCriar=${exibirMenuConhecimentos}&msgvazio=empty&titulo=${f:urlEncode(solicitacao.gcTituloAbertura)}${solicitacao.gcTagAbertura}";
											Siga.ajax(url, null, "GET", function(response){
												document.getElementById('gc-ancora-item-acao').innerHTML = response;
											});
										</script>
									</c:if>
									
									<c:set var="atributoSolicitacaoMap" value="${solicitacao.atributoSolicitacaoMap}"/>
									
									<div id="atributos" class="gt-form-row gt-width-66" style="margin-top: 10px;">
										<c:forEach items="${solicitacao.atributoAssociados}" var="atributo" varStatus="loop">
											<div class="gt-form-row gt-width-66">
												<label>
													${atributo.nomeAtributo} 
													<c:if test="${atributo.descrAtributo != null && atributo.descrAtributo != ''}">
														(${atributo.descrAtributo})
													</c:if>
												</label>
												<c:if test="${atributo.tipoAtributo != null}">
													<c:if test="${atributo.tipoAtributo.name() == 'TEXTO'}">
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" class="${atributo.idAtributo}" size="70" maxlength="255" />
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo"/>
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'TEXT_AREA'}">
													
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<textarea cols="85" rows="10" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" class="${atributo.idAtributo}" maxlength="255">${solicitacao.atributoSolicitacaoMap[atributo.idAtributo]}</textarea>
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'DATA'}">
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<siga:dataCalendar nome="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" id="calendarioAtributo${atributo.idAtributo}"
															value="${atributoSolicitacaoMap[loop.index].valorAtributo}" cssClass="${atributo.idAtributo}"/>
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'NUM_INTEIRO'}">
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<input type="text" class="${atributo.idAtributo}"
															onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
															name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" maxlength="9"/>
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'NUM_DECIMAL'}">
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" 
															id="numDecimal" pattern="^\d*(\,\d{2}$)?" title="Somente número e com duas casas decimais EX: 222,22" class="${atributo.idAtributo}" maxlength="9"/>
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'HORA'}">
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" id="horarioAtributo${atributo.idAtributo}" class="${atributo.idAtributo}" />
														<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
														<span style="color: red; display: none;" id="erroHoraAtributo${atributo.idAtributo}">Hor&aacute;rio inv&aacute;lido</span>
														<script>
															$(function() {
																$("#horarioAtributo${atributo.idAtributo}").mask("99:99");
																$("#horarioAtributo${atributo.idAtributo}").blur(function() {
																	var hora = this.value;
																	var array = hora.split(':');
																	if (array[0] > 23 || array[1] > 59) {
																		$('#erroHoraAtributo${atributo.idAtributo}').show(); 
																		return;
																	}
																	$('#erroHoraAtributo${atributo.idAtributo}').hide();    
																});
															});
														</script>
													</c:if>
													<c:if test="${atributo.tipoAtributo.name() == 'VL_PRE_DEFINIDO'}" >
														<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
														<select name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" class="${atributo.idAtributo}" >
															<c:forEach items="${atributo.preDefinidoSet}" var="valorAtributoSolicitacao">
																<option value="${valorAtributoSolicitacao}" <c:if test="${atributoSolicitacaoMap[loop.index].valorAtributo == valorAtributoSolicitacao}">selected</c:if> >
																	${valorAtributoSolicitacao}
																</option>
															</c:forEach>
														</select>
													</c:if>
												</c:if>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</c:if>
					</div>
		
					<div class="gt-form-row gt-width-66">
						<label>Descri&ccedil;&atilde;o</label>
						<textarea cols="85" rows="10" name="solicitacao.descrSolicitacao"
							id="descrSolicitacao" maxlength="8192">${solicitacao.descrSolicitacao}</textarea>
							<siga:error name="solicitacao.descrSolicitacao"/>
					</div>
					<div class="gt-form-row gt-width-66">
						<label>Anexar arquivo</label>
						<input type="file" name="solicitacao.arquivo" />
					</div>
		
					<div class="gt-form-table">
						<div class="barra-subtitulo header" align="center" valign="top">
							Prioridade</div>
					</div>
					<div class="gt-form-row gt-width-33">
							<label>Gravidade</label> 
							<select name="solicitacao.gravidade" id="gravidade" style="width:235px" onchange="$('#prioridade').html($('#gravidade option:selected').attr('prioridade'))">
								<c:forEach items="${gravidadeList.keySet()}" var="gravidade">
									<c:set var="prioridade" value="${gravidadeList.get(gravidade)}" />
										<option value="${gravidade}" prioridade="${prioridade.descPrioridade}" ${solicitacao.gravidade == gravidade ? 'selected' : ''}>${gravidade.descrGravidade}</option>
								</c:forEach>
							</select>
					</div>
					<div class="gt-form-row gt-width-66" >
						<label style="float: left">Prioridade: &nbsp;</label>
						<span id="prioridade">${solicitacao.prioridade.descPrioridade}</span>
					</div>
		
		
					<c:if test="${solicitacao.podeAbrirJaFechando(titular, lotaTitular)}">
						<div class="gt-form-row gt-width-66">
							<label><siga:checkbox name="solicitacao.fecharAoAbrir" value="${solicitacao.fecharAoAbrir}"
									disabled="alterando"/>Fechar o chamado logo ap&oacute;s a abertura</label>
						</div>
					</c:if>
		
					<div class="gt-form-row gt-width-66" id="motivoFechamento"
						style="display: none">
						<label>Motivo do fechamento</label> <input type="text" size="100"
							name="motivoFechamentoAbertura"
							id="motivoFechamentoAbertura" />
					</div>
					<c:choose>
						<c:when test="${!solicitacao.jaFoiDesignada()}">
							<br />
							<div class="gt-form-row">
								<label> <siga:checkbox name="solicitacao.rascunho"
									value="${solicitacao.rascunho}"/> Rascunho </label>
							</div>
						</c:when>
						<c:when test="${solicitacao.isPai() && solicitacao.idSolicitacao != null}">
							<div class="gt-form-table">
								<div class="barra-subtitulo header" align="center" valign="top"> Fechamento Autom&aacute;tico</div>
							</div>
							<p> <siga:checkbox name="solicitacao.fechadoAutomaticamente"
								value="${solicitacao.fechadoAutomaticamente}"/>Fechar automaticamente a solicita&ccedil;&atilde;o <b>${solicitacao.codigo}</b>.</p>
							<br />
						</c:when>
					</c:choose>
					<div class="gt-form-row">
						<input type="submit" value="Gravar"
							class="gt-btn-medium gt-btn-left" id="gravar" /> 
						<c:if test="${not empty solicitacao.id}">
						<a href="${linkTo[SolicitacaoController].excluir}?sigla=${solicitacao.siglaCompacta}" class="gt-btn-alternate-medium gt-btn-left">Descartar</a>
						</c:if>
					</div>
				</form>
			</div>
		</div>
		
		<div class="gt-sidebar">
 		<jsp:include page="exibirCronometro.jsp"/>
		<jsp:include page="exibirPendencias.jsp"/>
		
		<div id="divConhecimentosRelacionados" depende="solicitacao.local;solicitacao.solicitante;solicitacao.acao;solicitacao.itemConfiguracao">
			<jsp:include page="exibirConhecimentosRelacionados.jsp"/>
		</div>
		
		<div id="divSolicitacoesRelacionadas" depende="solicitacao.local;solicitacao.solicitante;solicitacao.acao;solicitacao.itemConfiguracao">
			<div id="divInternaSolicitacoesRelacionadas"></div>
			<script type="text/javascript">
				function carregar(){
					//Edson: verifica se há algum campo no formulário para o qual não existe checkbox no quadro de 
					//solicitações relacionadas (!.length), ou então existe *e* está marcado, e envia na url como true. Veja no 
					//comentário de listarSolicitacoesRelacionadas.jsp o motivo pelo qual esta verificação tem de ser feita aqui:
					var camposFiltraveis = ['solicitante', 'itemConfiguracao', 'acao'];
					var url = '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas}?'+$('#formSolicitacao').serialize();
					for (var i = 0; i < camposFiltraveis.length; i++){
						var campo = $(" #formSolicitacao [name='solicitacao."+camposFiltraveis[i]+".id']");
						var filtro = $(" #formRelacionadas [name='filtro."+camposFiltraveis[i]+".id']");
						if (campo.val() && (!filtro.length || filtro.is(":checked")))
							url += '&' + 'filtro.'+camposFiltraveis[i]+'.id=' + campo.val();
					}
					SetInnerHTMLFromAjaxResponse(url,"divInternaSolicitacoesRelacionadas");
				}
				carregar();
			</script>
		</div>
		</div>
	</div>
</siga:pagina>