<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="br.gov.jfrj.siga.sr.model.SrPrioridade"%>
<%@page import="br.gov.jfrj.siga.sr.model.SrGravidade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

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
		jQuery(document).ready(function($) {
			
			$('#gravar').click(function() {
				if ($('#siglaCadastrante').val() != $('#formulario_solicitacaosolicitante_sigla')[0].value) {
					var stringDt= $('#calendarioComunicacao').val() + ' ' + $('#horarioComunicacao').val();
					$('#stringDtMeioContato').val(stringDt);
				}
				gravar(); 
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

		function postbackURL(){
			return '${linkTo[SolicitacaoController].editar}?'+$('#formSolicitacao').serialize();
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
	
				<form action="#" method="post" enctype="multipart/form-data" id="formSolicitacao"> 
					<c:if test="${solicitacao.solicitacaoPai != null}">
						<input type="hidden" name="solicitacao.solicitacaoPai.id" 
						value="${solicitacao.solicitacaoPai.idSolicitacao}" /> 
					</c:if>
					
					<input type="hidden" name="solicitacao.id" id="idSol" value="${solicitacao.idSolicitacao}" /> 
					<input type="hidden" name="solicitacao.dtIniEdicaoDDMMYYYYHHMMSS" value="${solicitacao.dtIniEdicaoDDMMYYYYHHMMSS}" /> 
					<input type="hidden" name="solicitacao.numSolicitacao" value="${solicitacao.numSolicitacao}" />
					<input type="hidden" name="solicitacao.numSequencia" value="${solicitacao.numSequencia}" />
					<input type="hidden" id="isDirty" value="false" />
					
					
					<div class="gt-form-table">
						<div class="barra-subtitulo barra-subtitulo-top header" align="center" valign="top">
							Dados B&aacute;sicos
						</div>
					</div>

					<div class="error-message gt-form-table"><p></p></div>
					
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
							$("#horarioComunicacao").mask("99:99");
						</script>
						<c:if test="${locaisDisponiveis.size() > 1}">
							<div class="gt-form-row gt-width-99">
								<label>Local</label>
								<input type="hidden" name="solicitante" value="${solicitacao.solicitante.id}" id="solicitante"></input>
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
						
						<div class="gt-form-row-inline-block gt-width-33">
						
							<label>Telefone</label> 
							<input type="text" name="solicitacao.telPrincipal" id="telPrincipal" value="${solicitacao.telPrincipal}" maxlength="255" />
											
						</div>
						
						<div class="gt-form-row-inline-block gt-width-33">
							<label>Endereço de atendimento</label> 
								<input type="text" name="solicitacao.endereco" id="endereco" value="${solicitacao.endereco}" size="70" maxlength="255" />
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
									<siga:dataCalendar nome="calendario" id="calendarioComunicacao" value="${solicitacao.dtOrigemDDMMYYYY}"/>
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
							<sigasr:classificacao metodo="editar" exibeLotacaoNaAcao="true" exibeConhecimento="true"/>
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
						<input type="button" value="Gravar"
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