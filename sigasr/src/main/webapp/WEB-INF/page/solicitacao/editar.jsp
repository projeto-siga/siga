<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="br.gov.jfrj.siga.sr.model.SrPrioridade"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrGravidade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="Cadastro de Solicitação">
	<link rel="stylesheet" href="/siga/javascript/hierarchy-select/hierarchy-select.css" type="text/css" media="screen, projection" />
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/cronometro.js"></script>
	<script src="/siga/javascript/siga.js"></script>
	
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
		const toRemoveFromParams = ['solicitacao.solicitante.sigla',
			 'solicitacao.solicitante.descricao',			 
			 'solicitacao.cadastrante.sigla',
			 'solicitacao.interlocutor.sigla', 
			 'solicitacao.interlocutor.descricao'];
	
		jQuery(document).ready(function($) {
			
			$('#gravar').click(function() {
				if ($('#siglaCadastrante').val() != $('#formulario_solicitacaosolicitante_sigla')[0].value) {
					var stringDt= $('#calendarioComunicacao').val() + ' ' + $('#horarioComunicacao').val();
					$('#stringDtMeioContato').val(stringDt);
				}

				// Removendo entradas que causam excecoes no vraptor:
				toRemoveFromParams.forEach(e => {
					$("input[name='" + e + "']", "#formSolicitacao ").prop("disabled", true);
				})

				gravar();

				// Retornando as entradas caso de erro de validacao: 
				toRemoveFromParams.forEach(e => {
					$("input[name='" + e + "']", "#formSolicitacao ").prop("disabled", false);
				}) 
			}); 
	
			$('#checkRascunho').change(function() {
				if(this.checked) {
					$('#checkRascunho').prop('value', 'true');
					return;
				}
				$('#checkRascunho').prop('value', 'false');
			});
			$('#prioridade').text($('#gravidade option:selected').attr('prioridade'));
		});

		function filtraURL(url) {			
			return url.split('&').filter(e => toRemoveFromParams.indexOf(e.substring(0,e.indexOf('='))) < 0).join('&');
		}
		
		function postbackURL(){
			return filtraURL('${linkTo[SolicitacaoController].editar}?'+$('#formSolicitacao').serialize());			
		}

		function submitURL() {
			return '${linkTo[SolicitacaoController].gravar}';
		}
		
		function toggleInterlocutorMeioComunicacaoEDataOrigem() {
			var siglaCadastrante = $('#siglaCadastrante').val();
			var siglaSolicitante = $('#formulario_solicitacaosolicitante_sigla')[0].value;
	
			if (siglaCadastrante == siglaSolicitante) {
				$('#interlocutor')[0].style.display='none';
				$('#meioComunicacaoEDataOrigem')[0].style.display='none';
			}
			else {
				$('#interlocutor')[0].style.display='block';
				$('#meioComunicacaoEDataOrigem')[0].style.display='block';
			}
			
			toggleDataOrigem();
		}

		function toggleDataOrigem(){
			if ($("#meioComunicacao").val() == 'EMAIL')
				$('#dataOrigem')[0].style.display='inline-block';
			else $('#dataOrigem')[0].style.display='none';
		}
	</script>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-8">
				<div class="card bg-light mb-3" >
					<div class="card-header">
						<h5>
							<c:choose>
								<c:when test="${solicitacao.idSolicitacao != null || solicitacao.solicitacaoPai != null}">
									${solicitacao.codigo}
								</c:when>
								<c:otherwise>
									Cadastro de Solicita&ccedil;&atilde;o
								</c:otherwise>
							</c:choose>
						</h5>
					</div>
					
					<div class="card-body">
						<form action="#" method="post" enctype="multipart/form-data" id="formSolicitacao" class="mb-0">
						
							<c:if test="${solicitacao.solicitacaoPai != null}">
								<input type="hidden" name="solicitacao.solicitacaoPai.id" 
									value="${solicitacao.solicitacaoPai.idSolicitacao}" /> 
							</c:if>
					
							<input type="hidden" name="solicitacao.id" id="idSol" value="${solicitacao.idSolicitacao}" /> 
							<input type="hidden" name="solicitacao.dtIniEdicaoDDMMYYYYHHMMSS" value="${solicitacao.dtIniEdicaoDDMMYYYYHHMMSS}" /> 
							<input type="hidden" name="solicitacao.numSolicitacao" value="${solicitacao.numSolicitacao}" />
							<input type="hidden" name="solicitacao.numSequencia" value="${solicitacao.numSequencia}" />
							<input type="hidden" id="isDirty" value="false" />
						
							<div class="">
								<div style="margin-top: 5px">
									<h5>Dados B&aacute;sicos</h5>
								</div>
							
								<div class="error-message gt-form-table">
									<p class="alert alert-danger" role="alert" style="display: none">
									</p>
								</div>
							
							

							
								
								<!--  Cadastrante -->
								<div class="row">
									<div class="col-sm-12">								
										<div class="form-group">
											<label>Cadastrante</label>
											<input class="form-control" type="text" placeholder="${cadastrante.nomePessoa}" readonly /> 
											 
											<input type="hidden" id="siglaCadastrante" name="solicitacao.cadastrante.sigla"
													value="${cadastrante.sigla}" />
											<input type="hidden" id="idCadastrante" name="solicitacao.cadastrante.id" value="${cadastrante.idPessoa}" />
											<input type="hidden" name="solicitacao.lotaTitular.id" value="${lotaTitular != null ? lotaTitular.idLotacao:''}" />
											<input type="hidden" name="solicitacao.titular.id" value="${titular!= null ? titular.idPessoa:''}" />
										</div>
									</div>									
								</div>
								
								<!--  Solicitante -->
								<div class="row">
									<div class="col-sm-12">
										<div class="form-group">
											<label for="formulario_solicitacaosolicitante_sigla">Solicitante</label> 	
											<sigasr:selecao3 tipo="pessoa"
												propriedade="solicitacao.solicitante"
												tema="simple"
												modulo="siga"
												reler="ajax"
												onchange="toggleInterlocutorMeioComunicacaoEDataOrigem();" />
											<siga:error name="solicitacao.solicitante"/>
											
										</div>
									</div>
								</div>
								
								<!--  Interlocutor -->
								<div class="row">
									<div class="col-sm-12">
										<div id="interlocutor" style="display: none;">
											<div class="form-group">
												<label>Interlocutor (Opcional)</label> 
												<sigasr:selecao3 tipo="pessoa"
													propriedade="solicitacao.interlocutor" 
													tema="simple"
													modulo="siga"/>
											</div>
										</div>
									</div>
								</div>
								
								<div id="divLocalRamalEMeioContato" depende="solicitacao.solicitante">
									<script>
										//Edson: talvez fosse possivel fazer de um modo melhor, mas assim � mais pr�tico
										$("#solicitacaosolicitanteSpan").html("${solicitanteDescricaoCompleta}");
										$("#horarioComunicacao").mask("99:99");
									</script>
									<c:if test="${locaisDisponiveis.size() > 1}">
										<div class="row">
											<div class="col-sm-12">
												<div class="form-group">
													<label>Local</label>
													<input type="hidden" name="solicitante" value="${solicitacao.solicitante.id}" id="solicitante"></input>
				 									<select name="solicitacao.local.id" id="local" onchange="sbmt('solicitacao.local')" class="form-control">
					        						<c:forEach items="${locaisDisponiveis.keySet()}" var="orgao">
					                						<optgroup label="${orgao.acronimoOrgaoUsu}">
					                						<c:forEach items="${locaisDisponiveis.get(orgao)}" var="local">
					                        						<option value="${local.idComplexo}" ${solicitacao.local.idComplexo.equals(local.idComplexo) ? 'selected' : ''}>${local.nomeComplexo}</option>
					                						</c:forEach>
					                						</optgroup>
					        						</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</c:if>
										
									<!--  Telefone e Endereco de Atendimento -->
									<div class="row">									
										<div class="col-sm-4">
											<div class="form-group">
												<label>Telefone</label>
												<div>
													<input class="form-control" type="text" name="solicitacao.telPrincipal" id="telPrincipal" value="${solicitacao.telPrincipal}" maxlength="255" />
													<siga:error name="solicitacao.telPrincipal"/>
												</div>										
											</div>
										</div>
									
										<div class="col-sm-8">
									
											<div class="form-group">
												<label>Endere&ccedil;o de atendimento</label>
													<input class="form-control" type="text" name="solicitacao.endereco" id="endereco" value="${solicitacao.endereco}"  maxlength="255" />
													<siga:error name="solicitacao.endereco"/>
												
											</div>
										</div>
									</div>
													
									<!--  Meio comunicacao e contato inicial -->						
									<div id="meioComunicacaoEDataOrigem">
										<div class="row">
											<div class="col-sm-12">
												<div class="form-group">
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
											</div>
										</div>
										
										<div id="dataOrigem">
											<div class="row">
												<div class="col-sm-3">
													<label>Contato inicial</label>
												</div>
												<div class="col-sm-4">
													<div class="form-group">
														<label>Data</label>
														<!-- <input class="form-control" type="date" value="${solicitacao.dtOrigemDDMMYYYY}" id="calendarioComunicacao"  nome="calendario">  --> 	
														<siga:dataCalendar nome="calendario" id="calendarioComunicacao" value="${solicitacao.dtOrigemDDMMYYYY}"/>

														<siga:error name="calendario"/>
													</div>
												</div>
												<div class="col-sm-4">
													<div class="form-group">
														<label>Hora</label> 
														<input class="form-control" type="text" name="horario" id="horarioComunicacao" value="${solicitacao.dtOrigemHHMM}" />
					    								<siga:error name="horario"/>
													</div>
												</div>
												<input type="hidden" name="solicitacao.dtOrigemString" id="stringDtMeioContato" value="${solicitacao.dtOrigemDDMMYYYYHHMM}" />
											</div>																													
										</div>
									</div>
										
									<script>
										toggleInterlocutorMeioComunicacaoEDataOrigem();
									</script>
								</div>
										
										
						
										
								<div class="row">
									<div class="col-sm-12">
										<div class="form-group">
											<label>Quando deseja receber notifica&ccedil;&atilde;o dessa solicita&ccedil;&atilde;o por e-mail?</label>
											<siga:select name="solicitacao.formaAcompanhamento" list="formaAcompanhamentoList"
													listValue="descrFormaAcompanhamento" listKey="idFormaAcompanhamento" isEnum="true"
													value="${solicitacao.formaAcompanhamento != null ? solicitacao.formaAcompanhamento : ''}" />
										</div>
									</div>
								</div>	
											
							</div> <!-- Fim Dados Basicos -->
							






							

							
							<div>
								<div style="margin-top: 15px">
									<h5>Detalhamento</h5>
								</div>
							
								
								<div class="row">
									<div class="col-sm-12">
										<div id="divItem" depende="solicitacao.solicitante;solicitacao.local" class="form-control">
											<c:if test="${not empty solicitacao.solicitante}">						
												<sigasr:classificacao metodo="editar" exibeLotacaoNaAcao="true" exibeConhecimento="true" lotacaoDoTitular="${lotacaoDoTitularLegivel}" />
											</c:if>
										</div>
									</div>
								</div>
		
								<div class="form-group">
									<label>Descri&ccedil;&atilde;o</label>
									<textarea class="form-control" rows="10" name="solicitacao.descrSolicitacao"
										id="descrSolicitacao" maxlength="8192">${solicitacao.descrSolicitacao}</textarea>
										<siga:error name="solicitacao.descrSolicitacao"/>
								</div>
								<div class="form-group">
									<label>Anexar arquivo</label>
									<div class="form-control custom-file">
										<input class="custom-file-input" id="idSelecaoArquivo" type="file" name="solicitacao.arquivo" accept="*.*"/>
										<label class="custom-file-label text-truncate" for="idSelecaoArquivo" data-browse="Escolha o Arquivo">Clique para selecionar o arquivo a anexar</label>
									</div>
									<script js>
										$('.custom-file-input').on('change', function() { 
											   let fileName = $(this).val().split('\\').pop(); 
											   $(this).next('.custom-file-label').addClass("selected").html(fileName); 
											});
									</script>
									<!-- 
									<input type="file" name="solicitacao.arquivo" class="form-control"/>
									 -->
								</div>
		
								<c:if test="${exibirCampoPrioridade}">
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
								</c:if>
					
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
								<div class="gt-form-row mt-2">
									<input type="button" value="Gravar" class="btn btn-primary" id="gravar" /> 
									<c:if test="${not empty solicitacao.id}">
									<a href="${linkTo[SolicitacaoController].excluir}?sigla=${solicitacao.siglaCompacta}" class="gt-btn-alternate-medium gt-btn-left">Descartar</a>
									</c:if>
								</div>
							</div>		
						</form>
					</div>									
				</div>
			</div>
			
			
			<!-- Sidebar -->
			<div class="col-sm-4">				
				<jsp:include page="exibirCronometro.jsp"/>
				<jsp:include page="exibirPendencias.jsp"/>
				
				<div id="divConhecimentosRelacionados" depende="solicitacao.local;solicitacao.solicitante;solicitacao.acao;solicitacao.itemConfiguracao">
						<jsp:include page="exibirConhecimentosRelacionados.jsp"/>
				</div>
					
				<div id="divSolicitacoesRelacionadas" depende="solicitacao.local;solicitacao.solicitante;solicitacao.acao;solicitacao.itemConfiguracao">
					<div id="divInternaSolicitacoesRelacionadas"></div>
						<script type="text/javascript">
							function carregar(){
								//Edson: verifica se h� algum campo no formul�rio para o qual n�o existe checkbox no quadro de 
								//solicita��es relacionadas (!.length), ou ent�o existe *e* est� marcado, e envia na url como true. Veja no 
								//coment�rio de listarSolicitacoesRelacionadas.jsp o motivo pelo qual esta verifica��o tem de ser feita aqui:
								var camposFiltraveis = ['solicitante', 'itemConfiguracao', 'acao'];
								var url = '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas}?'+$('#formSolicitacao').serialize();
								for (var i = 0; i < camposFiltraveis.length; i++){
									var campo = $(" #formSolicitacao [name='solicitacao."+camposFiltraveis[i]+".id']");
									var filtro = $(" #formRelacionadas [name='filtro."+camposFiltraveis[i]+".id']");
									if (campo.val() && (!filtro.length || filtro.is(":checked")))
										url += '&' + 'filtro.'+camposFiltraveis[i]+'.id=' + campo.val();
								}
								
								SetInnerHTMLFromAjaxResponse(filtraURL(url),"divInternaSolicitacoesRelacionadas");
							}
							carregar();
						</script>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>