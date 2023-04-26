<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>

<c:set var="exibirExplicacao" scope="request" value="${libs:podeExibirRegraDeNegocioEmBotoes(titular, lotaTitular)}" />

<siga:pagina titulo="${docVO.sigla}" popup="${param.popup}" >

<style>									
	.container-files {
		opacity: 1;
		transition: opacity 1s ease-in;										
	}
																			
	.files {
		margin-top: 15px;
	}															

	.files .btn.btn-sm.btn-light {
		width: 84%;
		padding: 0;			
		background-color: transparent;
		border: none;
		text-align: justify;
		white-space: normal;  									
		transition: padding .5s;																									
	}
	
	.files .btn.btn-sm.btn-light:hover {
		padding: 5px;
		background-color: rgba(0, 123, 255, 0.18);
		border: none;			
		border-radius: 10px;
	}	
	
	.files .btn.btn-sm.btn-light img {
		float: left;
	}																																
	
	.files .btn.btn-sm.btn-outline-danger.btn-cancel {
		padding: 1px 3px;
		margin-left: 10px;
		right: 20px; 
		position: absolute;
		float: right;					
		font-size: .9em; 																										
	}
	
	.container-confirmacao-cancelar-arquivo {
		width: 100%;
	    height: 77%;
	    margin: 0;    		
	    position: absolute;
	    left: 0;
		bottom: 0;
		background-color: white;
		text-align: center;
		visibility: hidden;																	
		opacity: 0;
		transition: opacity 1s ease-out;	    								
	}	
	
	.confirmacao-cancelar-arquivo {
		width: 100%;			
		position: absolute;
		top: 50%;			
		transform: translate(0, -50%);			
	}		
	
	.confirmacao-cancelar-arquivo h1 {
		font-size: 1.3em;
	}
	
	.confirmacao-cancelar-arquivo p {
		max-width: 80%;
		margin: 0 auto;
	}															
	
	.confirmacao-cancelar-arquivo button {
		margin: 10px 2px 0 2px;
	}			
	
	@media (min-width: 768px) and (max-width: 1019px) {
		.files .btn.btn-sm.btn-light {
			width: 65%;											
		}					
	}	
	
	@media (min-width: 1020px) and (max-width: 1416px) {
		.files .btn.btn-sm.btn-light {
			width: 75%;											
		}
	}			
		
	.tabela-ordenavel tbody {
		cursor: move;
	}
		
	.tabela-ordenavel tbody tr {
		border: 2px dashed #A9A9A9;				
	}
		
	.tabela-ordenavel tbody tr:hover,
	.tabela-ordenavel tbody tr:focus {
		border-left: 3px dashed #007BFF;	
		border-right: 3px dashed #007BFF;		
		background-color: #CED4DA;	
	}
		
	.tabela-ordenavel tbody a {
		pointer-events: none;
	}								
</style>						

<script>
	if (${not empty f:resource('/vizservice.url')}) {
	} else if (window.Worker) {
		window.VizWorker = new Worker("/siga/javascript/viz.js");
		window.VizWorker.onmessage = function(oEvent) {
			document.getElementById(oEvent.data.id).innerHTML = oEvent.data.svg;
			$(document).ready(function() {
				try {
					updateContainerTramitacao();
				} catch(ex) {};
				try {
					updateContainerRelacaoDocs();
				} catch(ex) {};
				try {
					updateContainerColaboracao();
				} catch(ex) {};
			});
		};
	} else {
		document
				.write("<script src='/siga/javascript/viz.js' language='JavaScript1.1' type='text/javascript'>"
						+ "<"+"/script>");
	}

	function buildSvg(id, input, cont) {
		if (${not empty f:resource('/vizservice.url')}) {
		    input = input.replace(/fontsize=\d+/gm, "");
			$.ajax({
			    url: "/siga/public/app/graphviz/svg",
			    data: input,
			    type: 'POST',
			    processData: false,
			    contentType: 'text/vnd.graphviz',
			    contents: window.String,
			    success: function(data) {
				    data = data.replace(/width="\d+pt" height="\d+pt"/gm, "");
				    $(data).width("100%");
			        $("#" + id).html(data);
			    }
			});
		} else if (window.VizWorker) {
			document.getElementById(id).innerHTML = "Aguarde...";
			window.VizWorker.postMessage({
				id : id,
				graph : input
			});
		} else {
			var result = Viz(input, "svg", "dot");
			document.getElementById(id).innerHTML = result;
			cont();
		}
	}

	function pageHeight() {
		return window.innerHeight != null ? window.innerHeight
				: document.documentElement
						&& document.documentElement.clientHeight ? document.documentElement.clientHeight
						: document.body != null ? document.body.clientHeight
								: null;
	}

	function resize() {
		var ifr = document.getElementById('painel');
		ifr.height = pageHeight() - 300;
		if (ifr.height < 300)
			ifr.height = 300;
		console.log("resize foi chamado!");
	}
</script>

<c:if test="${not docVO.digital}">
	<script type="text/javascript">
		$("body").addClass("fisico");
		$("html").addClass("fisico");
		$("#outputTramitacao").addClass("fisico");
	</script>
</c:if>

<script type="text/javascript">
	var css = "<style>a:link {text-decoration: none}</style>";
	$(css).appendTo("head");
	function escapeAcentos(s) {
		//Edson: o replace abaixo é necessário porque o viz.js não monta o gráfico corretamente se
		//houver caracteres com acento...
		return s.replace(/[ãâáàÃÁÀÂêéÊÉôõóÔÕÓúÚçÇ]/gim, function(i) {
			return '&#' + i.charCodeAt(0) + ';';
		});
	}
	
	function excluirArqAuxiliar(id, sigla) {
		frm.elements["id"].value = id;
		frm.elements["sigla"].value = sigla;
		frm.action = '/sigaex/app/expediente/mov/cancelar_movimentacao_gravar';
		frm.submit();
		//alert(id);
		//alert(frm.elements["sigla"].value);
	}
</script>

<div class="container-fluid content" id="page">	
	<c:if test="${not empty param.msg}">
		<div class="row mt-3">
			<p align="center">
				<b>${param.msg}</b>
			</p>
		</div>
	</c:if>
	<div class="row mt-3">
		<div class="col">
			<form name="frm" action="exibir" theme="simple" method="POST">
				<input type="hidden" id="id" name="id"/> <input type="hidden" id="sigla" name="sigla"/>	
				<input type="hidden" id="visualizador" value="${f:resource('/sigaex.pdf.visualizador') }"/>
			</form>
			<h2>
				<c:if test="${empty ocultarCodigo}">${docVO.sigla}
				</c:if>
				<button type="button" name="voltar" onclick="${(empty param.linkVolta) ? 'javascript:window.location.href=\'/siga\';' : 'javascript:'.concat(param.linkVolta) }" class="btn btn-secondary float-right ${hide_only_TRF2}" accesskey="r">Volta<u>r</u></button>				
			</h2>
		</div>
	</div>
	<c:set var="primeiroMobil" value="${true}" />
	<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
		<div class="row  siga-menu-acoes">
			<div class="col">
				<h3 class="${hide_only_GOVSP} style="margin-bottom: 0px;">
					${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
					<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
				 		- ${m.tamanhoDeArquivo}
					</c:if>
				</h3>
				<c:set var="ocultarCodigo" value="${true}" />
				<c:if test='${param.popup!="true"}'>
					<c:set var="acoes" value="${m.acoesOrdenadasPorNome}" />
					<siga:links>
						<c:forEach var="acao" items="${acoes}">
							<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
								pre="${acao.pre}" pos="${acao.pos}"
								url="${pageContext.request.contextPath}${acao.url}"
								popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
								classe="${acao.classe}" estilo="line-height: 160% !important"
								atalho="${true}" modal="${acao.modal}"
								descr="${acao.descr}" 
								post="${acao.post}"
								test="${acao.pode}" />
						</c:forEach>
					</siga:links>
				</c:if>
			</div>
		</div>
		<c:set var="temmov" value="${false}" />
		<c:forEach var="mov" items="${m.movs}">
			<c:if test="${ (mov.exTipoMovimentacao != 'CANCELAMENTO_DE_MOVIMENTACAO' and not mov.cancelada)}">
				<c:set var="temmov" value="${true}" />
			</c:if>
			<c:if test="${ (mov.exTipoMovimentacao == 'CIENCIA' and not mov.cancelada and 
				mov.mov.cadastrante == cadastrante and mov.mov.lotaCadastrante == lotaTitular)}">
				<c:set var="descrCiencia" value="${mov.descricao}" />
			</c:if>
		</c:forEach>
		<div class="row mt-2">
			<div class="col col-sm-12 col-md-8">
				<div>
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')}">
						<c:if
							test="${ (primeiroMobil == true) and (docVO.tipoFormaDocumento == 'processo_administrativo')}">
							<div id="${docVO.sigla}" depende=";wf;" class="wf_div"></div>
							<!--ajax:${doc.codigo}-${i}-->
							<!--/ajax:${doc.codigo}-${i}-->
							<c:set var="primeiroMobil" value="${false}" />
						</c:if>
						<c:if
							test="${(not m.mob.geral) or (docVO.tipoFormaDocumento != 'processo_administrativo')}">
							<div id="${m.sigla}" depende=";wf;" class="wf_div"></div>
							<!--ajax:${doc.codigo}-${i}-->
							<!--/ajax:${doc.codigo}-${i}-->
						</c:if>
					</c:if>

					<c:choose>
						<c:when test="${docVO.conteudoBlobHtmlString != null}">
							<div class="card-sidebar card border-alert bg-white mb-3">
								<div class="card-body">
									<tags:fixdocumenthtml>
											${docVO.conteudoBlobHtmlString}
										</tags:fixdocumenthtml>
								</div>
							</div>
						</c:when>
						<c:when test="${docVO.doc.pdf && not docVO.doc.exModelo.extensoesArquivo}">
							<c:set var="urlCapturado" value="/sigaex/app/arquivo/exibir?arquivo=${docVO.doc.referenciaPDF}"/>
							<iframe style="display: block;" name="painel" id="painel"
								width="100%" frameborder="0" scrolling="auto"></iframe>			
							<script>
							$(document).ready(function(){resize();$(window).resize(function(){resize();});});
						</script>
						</c:when>
					</c:choose>

					<c:if test="${temmov}">
						<div class="gt-content-box gt-for-table"
							style="margin-bottom: 25px; margin-top: 10px;">
							<script type="text/javascript">
							var css = "<style>TABLE.mov TR.despacho { background-color: rgb(240, 255, 240);}";
							css += "TABLE.mov TR.juntada,TR.desentranhamento { background-color: rgb(229, 240, 255);}";
							css += "TABLE.mov TR.anotacao { background-color: rgb(255, 255, 255);}";
							css += "TABLE.mov TR.anexacao { background-color: rgb(255, 255, 215);}";
							css += "TABLE.mov TR.encerramento_volume { background-color: rgb(255, 218, 218);}</style>";
							$(css).appendTo("head");
						</script>
							<table class="table table-sm table-responsive-sm table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th class="text-left">Data</th>
										<th class="text-left"><fmt:message key="usuario.lotacao"/></th>
										<th class="text-left">Evento</th>
										<th class="text-left">Descrição</th>
									</tr>
								</thead>
								<c:set var="evenorodd" value="odd" />
								<c:forEach var="mov" items="${m.movs}">
									<c:if
										test="${ (mov.exTipoMovimentacao != 'CANCELAMENTO_DE_MOVIMENTACAO' and mov.exTipoMovimentacao != 'ANEXACAO_DE_ARQUIVO_AUXILIAR' and
							          mov.exTipoMovimentacao != 'CANCELAMENTO_JUNTADA' and not mov.cancelada)}">
										<tr class="${mov.classe} ${mov.disabled}">
											<td class="text-left" title="${mov.tempoRelativo}">${mov.dtRegMovDDMMYY}</td>
											<td class="text-left" title="${mov.mov.cadastrante.descricao} - ${mov.mov.lotaCadastrante.descricao}">${mov.mov.lotaCadastrante.sigla}</td>
											<td class="text-left" >${mov.mov.exTipoMovimentacao.descr}</td>
											<td class="text-left" 
													<c:if test="${mov.exTipoMovimentacao == 'ENCERRAMENTO_DE_VOLUME'}">data-toggle="tooltip"  data-placement="top" title="O sistema encerra automaticamente um volume após a inclusão de ${f:resource('volume.max.paginas')} páginas para evitar lentidão no processamento e geração de PDF."
													</c:if>>
												${mov.descricao}
												<c:if test="${mov.exTipoMovimentacao != 'ANEXACAO'}"> ${mov.complemento} </c:if>
												<c:set var="assinadopor" value="${true}" />
												<siga:links
														buttons="${false}"
														inline="${true}"
														separator="${not empty mov.descricao and mov.descricao != null}">
														<c:forEach var="acao" items="${mov.acoes}">
															<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
																pos="${acao.pos}"
																url="${pageContext.request.contextPath}${acao.url}"
																test="${acao.pode}" popup="${acao.popup}"
																confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
																idAjax="${mov.idMov}" classe="${acao.classe}" post="${acao.post}" />
															<c:if test="${assinadopor and mov.exTipoMovimentacao == 'ANEXACAO'}"> ${mov.complemento}
																<c:set var="assinadopor" value="${false}" />
															</c:if>
														</c:forEach>
												</siga:links>
											</td>
										</tr>
										<c:choose>
											<c:when test='${evenorodd == "even"}'>
												<c:set var="evenorodd" value="odd" />
											</c:when>
											<c:otherwise>
												<c:set var="evenorodd" value="even" />
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</table>
						</div>
					</c:if>
				</div>				
			</div>
			<div class="col col-sm-12 col-md-4">
				<div class="gt-sidebar">
					<c:if test="${m.pendencias}">
						<div class="card-sidebar card bg-light mb-3" id="pendencias">
							<tags:collapse title="Pendências" id="Pendencias" collapseMode="${collapse_Expanded}">
								<c:if test="${not empty m.pendenciasDeAnexacao}">
									<p style="margin-bottom: 3px;">
										<b style="color: rgb(195, 0, 0)">Anexos Pendentes:</b>
									</p>
									<ul>
										<c:forEach var="anexoPendente"
											items="${m.pendenciasDeAnexacao}">
											<li><a
												href="${pageContext.request.contextPath}/app/expediente/mov/anexar?sigla=${m.sigla}"
												title="${anexoPendente.descricao}"
												style="text-decoration: none"> ${anexoPendente.descricao}
											</a></li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${not empty m.anexosNaoAssinados}">
									<p style="margin-bottom: 3px;">
										<b style="color: rgb(195, 0, 0)">Anexos não assinados:</b>
									</p>
									<ul>
										<c:forEach var="naoAssinado" items="${m.anexosNaoAssinados}">
											<li><a
												href="javascript:popitup('${pageContext.request.contextPath}/app/expediente/mov/exibir?id=
									${naoAssinado.idMov}&popup=true')"
												title="${naoAssinado.descricao}"
												style="text-decoration: none">
													${naoAssinado.mov.nmArqMov} </a></li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${not empty m.despachosNaoAssinados}">
									<p style="margin-bottom: 3px; margin-top: 8px;">
										<b style="color: rgb(195, 0, 0)">Despachos não assinados:</b>
									</p>
									<ul>
										<c:forEach var="naoAssinado" items="${m.despachosNaoAssinados}">
											<li><a
												href="javascript:popitup('${pageContext.request.contextPath}
									/app/expediente/mov/exibir?id=${naoAssinado.idMov}&popup=true')"
												title="${naoAssinado.descricao}"
												style="text-decoration: none"> ${naoAssinado.descricao} </a></li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${not empty m.expedientesJuntadosNaoAssinados}">
									<p style="margin-bottom: 3px; margin-top: 8px;">
										<b style="color: rgb(195, 0, 0)">Expedientes juntados não
											assinados:</b>
									</p>
									<ul>
										<c:forEach var="naoAssinado"
											items="${m.expedientesJuntadosNaoAssinados}">
											<li><a
												href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${naoAssinado.sigla}"
												style="text-decoration: none"> ${naoAssinado.sigla} </a></li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${not empty m.expedientesFilhosNaoJuntados}">
									<p style="margin-bottom: 3px; margin-top: 8px;">
										<b style="color: rgb(195, 0, 0)">Expedientes não juntados:</b>
									</p>
									<ul>
										<c:forEach var="naoJuntado"
											items="${m.expedientesFilhosNaoJuntados}">
											<li><a
												href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${naoJuntado.sigla}"
												title="${naoJuntado.descrDocumento}"
												style="text-decoration: none"> ${naoJuntado.sigla} </a></li>
										</c:forEach>
									</ul>
								</c:if>
								<c:if test="${not empty m.pendenciasDeColaboracao}">
									<p style="margin-bottom: 3px;">
										<b style="color: rgb(195, 0, 0)">Pendências de Colaboração:</b>
									</p>
									<ul>
										<c:forEach var="colaboracaoPendente"
											items="${m.pendenciasDeColaboracao}">
											<li><a
												href="${pageContext.request.contextPath}/app/expediente/mov/anexar?sigla=${m.sigla}"
												title="${colaboracaoPendente.descricao}"
												style="text-decoration: none">
													${colaboracaoPendente.descricao} </a></li>
										</c:forEach>
									</ul>
								</c:if>
							</tags:collapse>
						</div>
					</c:if>



					<c:if test="${not empty docVO.documentosPublicados or not empty docVO.boletim}">
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Boletim Interno" id="BoletimInterno" collapseMode="${collapse_Expanded}">
								<c:if test="${not empty docVO.documentosPublicados}">
									<p class="apensados" style="margin-top: 0pt;">
										<b>Documentos Publicados: </b>
										<c:forEach var="documentoPublicado"
											items="${docVO.documentosPublicados}">
											<a
												href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documentoPublicado.sigla}"
												title="${documentoPublicado.sigla}"
												style="text-decoration: none">
												${documentoPublicado.sigla} </a>
												&nbsp;
										</c:forEach>
									</p>
								</c:if>
								<c:if test="${not empty docVO.boletim}">
									<p class="apensados" style="margin-top: 0pt;">
										<b>Publicado no Boletim: </b> <a
											href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docVO.boletim.sigla}"
											title="${docVO.boletim.sigla}" style="text-decoration: none">
											${docVO.boletim.sigla} </a>
									</p>
								</c:if>
							</tags:collapse>
						</div>
					</c:if>

					<!-- tabela de móbiles e marcas -->
					<c:if test="${not empty docVO.outrosMobsLabel and not empty docVO.marcasDeSistemaPorMobil}">
						<div class="card-sidebar card bg-light mb-3">
							<c:set var="butRefresh"><a title="Atualizar marcas"
								style="float: right; margin-top: 0px; padding-left: 1em; padding-right: 1em;"
								href="${linkTo[ExDocumentoController].aAtualizarMarcasDoc(sigla)}?sigla=${sigla}"
								${popup?'target="_blank" ':''}> <img
								src="/siga/css/famfamfam/icons/arrow_refresh.png">
								
							</a></c:set>
							<tags:collapse title="${docVO.outrosMobsLabel}" id="OutrosMob" collapseMode="${collapse_Expanded}" addToTitle="${butRefresh}" classInfo="m-0 p-0">
								<div class="table-responsive">
								<table class="table table-sm mb-0 w-100">
								<!-- <thead class="align-middle text-center">
									<tr>
										<th class="text-left"></th>
										<th class="text-left">Marca</th>
										<th class="text-left"><fmt:message key="usuario.pessoa"/></th>
										<th class="text-left"><fmt:message key="usuario.lotacao"/></th>
										<th class="text-left">Texto</th>
									</tr>
								</thead> -->
								<tbody>
									<c:forEach var="entry" items="${docVO.marcasDeSistemaPorMobil}">
										<c:set var="outroMob" value="${entry.key}" />
										<c:set var="mobNome" value="${outroMob.isGeral() ? 'Geral' : outroMob.terminacaoSigla}" />
										<c:forEach var="marca" items="${entry.value}" varStatus="loop">
											<c:set var="lotacaoAtual" value="${marca.dpLotacaoIni.lotacaoAtual}"/>
											<c:set var="pessoaAtual" value="${marca.dpPessoaIni.pessoaAtual}"/>
											<tr class="${mov.classe} ${mov.disabled}">
											<c:if test="${loop.first}">
											<td rowspan="${entry.value.size()}" style="padding-left: 1.25rem"><c:choose>
													<c:when test="${(not outroMob.geral) and outroMob.numSequencia == m.mob.numSequencia}">
														<i><b>${mobNome}</b></i>
													</c:when>
													<c:otherwise>
														<a
															href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${outroMob.sigla}"
															title="${outroMob.doc.descrDocumento}"
															style="text-decoration: none">
															${mobNome} </a>
													</c:otherwise>
												</c:choose></td>
											</c:if>
											<td>${marca.descricaoComDatas}</td>
											<td><siga:selecionado isVraptor="true" sigla="${pessoaAtual.nomeAbreviado}"
												descricao="${pessoaAtual.descricao} - ${pessoaAtual.sigla}"
												pessoaParam="${pessoaAtual.siglaCompleta}" /></td>
											<td><siga:selecionado isVraptor="true" sigla="${marca.dpLotacaoIni.lotacaoAtual.sigla}"
												descricao="${marca.dpLotacaoIni.lotacaoAtual.descricaoAmpliada}"
												lotacaoParam="${marca.dpLotacaoIni.lotacaoAtual.siglaCompleta}" /></td>
											<c:choose>
												<c:when test="${not empty marca.exMovimentacao.descrMov}">
													<td>${marca.exMovimentacao.descrMov}</td>
												</c:when>
												<c:otherwise>
													<td style="padding-left:0; padding-right: 0"></td>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${marca.exMovimentacao.podeCancelar(titular, lotaTitular)}">
													<td style="padding-left:.25em; padding-right: 0"><a href="javascript:postToUrl('/sigaex/app/expediente/mov/cancelar_movimentacao_gravar?id=${marca.exMovimentacao.idMov}&sigla=${sigla}')" title="${exibirExplicacao ? marca.exMovimentacao.expliquePodeCancelar(titular, lotaTitular) : ''}"><i class="far fa-trash-alt"></i></a></td>
												</c:when>
												<c:otherwise>
													<td style="padding-left:0; padding-right: 0"></td>
												</c:otherwise>
											</c:choose>
											<td style="padding-left:0; padding-right: 1.25rem"></td>
											</tr>
										</c:forEach>
									</c:forEach>
								</tbody>
							</table>
							</div>
							</tags:collapse>
						</div>
					</c:if>

					<!-- tabela marcas -->
					<c:if test="${not empty docVO.marcasDoMobil}">
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Marcadores" id="Marcadores" collapseMode="${collapse_Expanded}" classInfo="m-0 p-0">
								<div class="table-responsive">
								<table class="table table-sm mb-0 w-100">
								<tbody>
									<c:forEach var="marca" items="${docVO.marcasDoMobil}" varStatus="loop">
										<c:set var="lotacaoAtual" value="${marca.dpLotacaoIni.lotacaoAtual}"/>
										<c:set var="pessoaAtual" value="${marca.dpPessoaIni.pessoaAtual}"/>
										<c:if test="${loop.first or (marca.cpMarcador.idFinalidade.grupo != g)}">
											<tr><td colspan="6" style="padding-left: 1.25rem; color: gray">${marca.cpMarcador.idFinalidade.grupo.nome}</td></tr>
											<c:set var="g" value="${marca.cpMarcador.idFinalidade.grupo}"/>
										</c:if>
										<tr>
										<td style="padding-left: 2.5rem">${marca.descricaoComDatas}
										<c:if test="${marca.cpMarcador.idFinalidade.idTpInteressado != 'ATENDENTE'}">
											- <c:if test="${not empty pessoaAtual}"><siga:selecionado isVraptor="true" sigla="${pessoaAtual.nomeAbreviado}"
											descricao="${pessoaAtual.descricao} - ${pessoaAtual.sigla}"
											pessoaParam="${pessoaAtual.siglaCompleta}" /></c:if>
											<c:choose>
												<c:when test="${not empty lotacaoAtual}">
													<c:if test="${not empty pessoaAtual}">/</c:if>
													<siga:selecionado isVraptor="true" sigla="${marca.dpLotacaoIni.lotacaoAtual.sigla}"
														descricao="${marca.dpLotacaoIni.lotacaoAtual.descricaoAmpliada}"
														lotacaoParam="${marca.dpLotacaoIni.lotacaoAtual.siglaCompleta}" />
												</c:when>
												<c:otherwise>
													<c:if test="${empty pessoaAtual}">
														<siga:selecionado 
															isVraptor="true" 
															sigla="${marca.exMovimentacao.lotaSubscritor.sigla} (Sem acesso ao documento - A marca não será mostrada)"
															descricao="${marca.exMovimentacao.lotaSubscritor.descricaoAmpliada}"
															lotacaoParam="${marca.exMovimentacao.lotaSubscritor.siglaCompleta}" />
													</c:if>												
												</c:otherwise>
											</c:choose>
										</c:if>
										</td>
										<c:choose>
											<c:when test="${not empty marca.exMovimentacao.descrMov}">
												<td>${marca.exMovimentacao.descrMov}</td>
											</c:when>
											<c:otherwise>
												<td style="padding-left:0; padding-right: 0"></td>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${marca.exMovimentacao.podeCancelar(titular, lotaTitular)}">
												<td style="padding-left:.25em; padding-right: 0"><a href="javascript:postToUrl('/sigaex/app/expediente/mov/cancelar_movimentacao_gravar?id=${marca.exMovimentacao.idMov}&sigla=${marca.exMovimentacao.exMobil.sigla}&descrMov=' + encodeURIComponent('Exclusão de marcador: ${marca.cpMarcador.descrMarcador}'))" 
													title="${exibirExplicacao ? marca.exMovimentacao.expliquePodeCancelar(titular, lotaTitular) : ''}"><i class="far fa-trash-alt"></i></a></td>
											</c:when>
											<c:otherwise>
												<td style="padding-left:.25em; padding-right: 0"><a title="${exibirExplicacao ? marca.exMovimentacao.expliquePodeCancelar(titular, lotaTitular) : ''}"><i class="far fa-trash-alt text-secondary"></i></a></td>
											</c:otherwise>
										</c:choose>
										<td style="padding-left:0; padding-right: 1.25rem"></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							</div>
							</tags:collapse>
						</div>
					</c:if>

					<!-- Início mapa colaboração -->
					<c:if test="${docVO.dotColaboracao.numNodos > 1}">
						<!-- Sidebar List -->
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Colaboração" id="Colaboracao" collapseMode="${collapse_Expanded}">
								<div style="display: none" id="inputColaboracao"></div>
								<a href="javascript:void(0)" href="javascript:void(0)"
									style="text-decoration: none">
									<div id="outputColaboracao" class="bg-light"
										style="border: 0px; padding: 0px;">
									</div>
								</a>
							</tags:collapse>
						</div>
						<script>
							$(document).ready(function () {
							    $(window).resize(function() {
								    updateContainerColaboracao();
							    });
							    	 
								$("#svgColaboracao").dialog({
							    	autoOpen: false,
							    	height: $(window).height()*0.9,
							    	width: $(window).width()*0.9,
							    	modal: true,
								    resizable: false
							  	});
								$("#output2Colaboracao").mousedown(function(e){
									if (e.button == 0 && zoomColaboracao < 3)
										zoomColaboracao += 0.2;
									else if (e.button == 2 && zoomColaboracao > 0.5)
										zoomColaboracao -= 0.2;
									updateContainerColaboracao();
								});
								if (!document.getElementById("outputColaboracao").hasChildNodes()){
									$("#Colaboracao").hide();
								}
							});
							
							function bigmapColaboracao() {
								$('#svgColaboracao').dialog('open');
								if ($('#naoCarregouBigColaboracao')[0] != undefined){
									var input = 'digraph G { graph[tooltip="Colaboração"] ${docVO.dotColaboracao} }';
									input = escapeAcentos(input);
									buildSvg('output2Colaboracao', input, updateContainerColaboracao);
								}
							  	updateContainerColaboracao();
							} 
					
							var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
							function rgb2hex(rgb) {
								 rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
								 return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
							}			
							function hex(x) {
					  			return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
					 		}
						 	function ratioColaboracao(){
						 		var x = ${docVO.dotColaboracao.numNodos};
						 		if (x <= 2)
							 		return '0.4';
						 		else if (x ==3)
							 		return '0.5';
						 		else if (x ==4)
							 		return '0.6';
						 		else return '0.7';
						 	}
							
							function smallmapColaboracao() {
								//$("#outputColaboracao").css("background-color", $("html").css("background-color"));
								var bgcolor = rgb2hex($("#outputColaboracao").css("background-color"));
								var input = 'digraph G { graph[tooltip="Colaboração" ratio="' + ratioColaboracao() + '"  color="'+ bgcolor +'" bgcolor="'+bgcolor+'" URL="javascript: bigmapColaboracao();"]; node[fillcolor=white fontsize=50 style=filled ]; edge[fontsize=30]; ${docVO.dotColaboracao} }';
								input = escapeAcentos(input);
								buildSvg('outputColaboracao', input, updateContainerColaboracao);
							}
					
							var zoomColaboracao = 1;
							function updateContainerColaboracao() {
							    var smallwidth = $('#outputColaboracao').width(); 
						    	var smallsvg = $('#outputColaboracao :first-child').first(); 
						    	var smallviewbox = document.getElementById('outputColaboracao').firstElementChild.getAttribute('viewBox');
							      
						    	if(typeof smallviewbox != 'undefined') {
								    var a = smallviewbox.split(' ');  
						
							    	// set attrs and 'resume' force 
							    	smallsvg.attr('width', smallwidth);
							    	smallsvg.attr('height', smallwidth * a[3] / a[2]);
						    	}
							    
						    	var bigsvg = $('#output2Colaboracao :first-child').first(); 
						    	var bigviewbox = bigsvg.attr('viewBox');
							      
						    	if(typeof bigviewbox != 'undefined') {
								    var a = bigviewbox.split(' ');  
						
							    	// set attrs and 'resume' force 
							    	bigsvg.attr('width', a[2] * zoomColaboracao);
							    	bigsvg.attr('height', zoomColaboracao * a[3]);
						    	};
							}
							smallmapColaboracao();
					    </script>
						<!-- Fim mapa colaboração -->
					</c:if>


					<!-- Início mapa relação entre documentos -->
					<c:if test="${docVO.dotRelacaoDocs.numNodos > 1}">
						<!-- Sidebar List -->
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Documentos Relacionados" id="DocsRelacionados" collapseMode="${collapse_Expanded}">
								<div id="outputRelacaoDocs" class="bg-light" style="border: 0px; padding: 0px">
									<c:forEach items="${docVO.dotRelacaoDocs.secundariosAsMap}" var="mapa">
										<p style="margin-top: .5em;">
											${mapa.key}:
										</p>
										<ul>
											<c:forEach var="mobRelacionado" items="${mapa.value}">
												<li><a
													href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobRelacionado.sigla}"
													title="${mobRelacionado.doc.descrDocumento}"
													style="text-decoration: none"> ${mobRelacionado.sigla} </a></li>
											</c:forEach>
										</ul>
									</c:forEach>
								</div>
								<c:forEach items="${docVO.dotRelacaoDocs.principaisAsMap}" var="mapa">
									<p style="margin-top: .5em;">
										${mapa.key}:
									</p>
									<ul>
										<c:forEach var="mobRelacionado" items="${mapa.value}">
											<li><a
												href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobRelacionado.sigla}"
												title="${mobRelacionado.doc.descrDocumento}"
												style="text-decoration: none"> ${mobRelacionado.sigla} </a></li>
										</c:forEach>
									</ul>
								</c:forEach>
							</tags:collapse>
						</div>

						<script>
							$(document).ready(function() {
								$(window).resize(function() {
									updateContainerRelacaoDocs();
								});

								$('#collapseDocsRelacionados').on('shown.bs.collapse', function() {
									updateContainerRelacaoDocs();
							    });
								
								$("#svgRelacaoDocs").dialog({
									autoOpen : false,
									height : $(window).height() * 0.9,
									width : $(window).width() * 0.9,
									modal : true,
									resizable : false
								});
								$("#output2RelacaoDocs").mousedown(function(e) {
									if (e.button == 0 && zoomRelacaoDocs < 3)
										zoomRelacaoDocs += 0.2;
									else if (e.button == 2 && zoomRelacaoDocs > 0.5)
										zoomRelacaoDocs -= 0.2;
									updateContainerRelacaoDocs();
								});
							});
			
							function bigmapRelacaoDocs() {
								$('#svgRelacaoDocs').dialog('open');
								if ($('#naoCarregouBigRelacaoDocs')[0] != undefined) {
									var input = 'digraph G { graph[tooltip="Documentos Relacionados"]; edge[penwidth=2]; ${docVO.dotRelacaoDocs} }';
									input = escapeAcentos(input);
									buildSvg('output2RelacaoDocs', input, updateContainerRelacaoDocs);
								}
								updateContainerRelacaoDocs();
							}
			
							var hexDigits = new Array("0", "1", "2", "3", "4", "5", "6",
									"7", "8", "9", "a", "b", "c", "d", "e", "f");
							function rgb2hex(rgb) {
								rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
								return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
							}
							function hex(x) {
								return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16]
										+ hexDigits[x % 16];
							}

							function smallmapRelacaoDocs() {
								$("#outputRelacaoDocs").css("background-color",	$("html").css("background-color"));
								var bgcolor = rgb2hex($("#outputRelacaoDocs").css("background-color"));
								var input = 'digraph G { graph[ratio="0.4" tooltip="Documentos Relacionados" color="'
										+ bgcolor
										+ '" bgcolor="'
										+ bgcolor
										+ '" URL="javascript: bigmapRelacaoDocs();"]; node[fillcolor=white fontsize=20 style=filled]; edge[penwidth=2]; ${docVO.dotRelacaoDocs} }';
								input = escapeAcentos(input);
								buildSvg('outputRelacaoDocs', input, updateContainerRelacaoDocs);
							}
							
							var zoomRelacaoDocs = 1;
							function updateContainerRelacaoDocs() {
								var smallwidth = $('#outputRelacaoDocs').width();
								var smallsvg = $('#outputRelacaoDocs :first-child').first();
								var smallviewbox = document.getElementById('outputRelacaoDocs').firstElementChild.getAttribute('viewBox');
			
								if (typeof smallviewbox != 'undefined') {
									var a = smallviewbox.split(' ');
			
									// set attrs and 'resume' force 
									smallsvg.attr('width', smallwidth);
									smallsvg.attr('height', smallwidth * a[3] / a[2]);
								}
			
								var bigsvg = $('#output2RelacaoDocs :first-child').first();
								var bigviewbox = bigsvg.attr('viewBox');
			
								if (typeof bigviewbox != 'undefined') {
									var a = bigviewbox.split(' ');
			
									// set attrs and 'resume' force 
									bigsvg.attr('width', a[2] * zoomRelacaoDocs);
									bigsvg.attr('height', a[3] * zoomRelacaoDocs);
								}
							}
							smallmapRelacaoDocs();
						</script>
					</c:if>
					<!-- Fim mapa relação entre documentos -->


					<c:if test="${docVO.dotTramitacao.numNodos > 1}">
						<!-- Início mapa tramitação -->

						<!-- Sidebar List -->
						
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Tramitação" id="Tramitacao" collapseMode="${collapse_Tramitacao}">
								<div style="display: none" id="inputTramitacao"></div>
								<a href="javascript:void(0)" href="javascript:void(0)"
									style="text-decoration: none">
									<div id="outputTramitacao" class="bg-light"
										style="border: 0px; padding: 0px;">
									</div>
								</a>
								
								
								<script>
									$(document).ready(function () {
										$('#collapseTramitacao').on('shown.bs.collapse', function() {
										    updateContainerTramitacao();
									    });
										    	 
									    $(window).resize(function() {
										    updateContainerTramitacao();
									    });
										    	 
										$("#svgTramitacao").dialog({
									    	autoOpen: false,
									    	height: $(window).height()*0.9,
									    	width: $(window).width()*0.9,
									    	modal: true,
										    resizable: false
									  	});
										$("#output2Tramitacao").mousedown(function(e){
											if (e.button == 0 && zoomTramitacao < 3)
												zoomTramitacao += 0.2;
											else if (e.button == 2 && zoomTramitacao > 0.5)
												zoomTramitacao -= 0.2;
											updateContainerTramitacao();
										});
										if (!document.getElementById("outputTramitacao").hasChildNodes()){
											$("#tramitacao").hide();
										}
									});
										
									function bigmapTramitacao() {
										$('#svgTramitacao').dialog('open');
										if ($('#naoCarregouBigTramitacao')[0] != undefined){
											var input = 'digraph G { graph[tooltip="Tramitação"] ${docVO.dotTramitacao} }';
											input = escapeAcentos(input);
											buildSvg('output2Tramitacao', input, updateContainerTramitacao);
										}
									  	updateContainerTramitacao();
									} 
							
									var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
									function rgb2hex(rgb) {
										 rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
										 return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
									}			
									function hex(x) {
							  			return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
							 		}
								 	function ratioTramitacao(){
								 		var x = ${docVO.dotTramitacao.numNodos};
								 		if (x <= 2)
									 		return '0.4';
								 		else if (x ==3)
									 		return '0.5';
								 		else if (x ==4)
									 		return '0.6';
								 		else return '0.7';
								 	}
									
									function smallmapTramitacao() {
										//$("#outputTramitacao").css("background-color", $("html").css("background-color"));
										var bgcolor = rgb2hex($("#outputTramitacao").css("background-color"));
										var input = 'digraph G { graph[tooltip="Tramitação" ratio="' + ratioTramitacao() + '"  color="'+ bgcolor +'" bgcolor="'+bgcolor+'" URL="javascript: bigmapTramitacao();"]; node[fillcolor=white fontsize=50 style=filled ]; edge[fontsize=30]; ${docVO.dotTramitacao} }';
										input = escapeAcentos(input);
										buildSvg('outputTramitacao', input, updateContainerTramitacao);
									}
							
									var zoomTramitacao = 1;
									function updateContainerTramitacao() {
									    var smallwidth = $('#outputTramitacao').width(); 
								    	var smallsvg = $('#outputTramitacao :first-child').first(); 
								    	var smallviewbox = document.getElementById('outputTramitacao').firstElementChild.getAttribute('viewBox');
									      
								    	if(typeof smallviewbox != 'undefined') {
										    var a = smallviewbox.split(' ');  
								
									    	// set attrs and 'resume' force 
									    	smallsvg.attr('width', smallwidth);
									    	smallsvg.attr('height', smallwidth * a[3] / a[2]);
								    	}
									    
								    	var bigsvg = $('#output2Tramitacao :first-child').first(); 
								    	var bigviewbox = bigsvg.attr('viewBox');
									      
								    	if(typeof bigviewbox != 'undefined') {
										    var a = bigviewbox.split(' ');  
								
									    	// set attrs and 'resume' force 
									    	bigsvg.attr('width', a[2] * zoomTramitacao);
									    	bigsvg.attr('height', zoomTramitacao * a[3]);
								    	};
									}
									smallmapTramitacao();
							    </script>
							</tags:collapse>
						</div>
						<!-- Fim mapa tramitação -->
					</c:if>

					<div class="card-sidebar card bg-light mb-3">
						<c:set var="docDetalhesTitle" scope="request" value="${pagina_de_erro}" />
						<tags:collapse title="${siga_cliente=='GOVSP'?'Propriedades do Documento (':'Documento '}${docVO.doc.exTipoDocumento.descricao}${siga_cliente=='GOVSP'?')':''}" id="DocDetalhes" collapseMode="${collapse_Expanded}">
							<p class="${hide_only_GOVSP}">
								<b>Suporte:</b> ${docVO.fisicoOuEletronico}
							</p>
							<p>
								<b><fmt:message key="documento.data.assinatura"/>:</b> 
								<c:choose>
									<c:when test="${not empty docVO.dataPrimeiraAssinatura}">
										${docVO.dataPrimeiraAssinatura}
									</c:when>
									<c:otherwise>
										${docVO.dtFinalizacao}
										<c:if test="${not empty docVO.originalData}">- <b>original:</b> ${docVO.originalData}</c:if>
									</c:otherwise>
								</c:choose>
								
							</p>
							<c:if test="${not empty docVO.originalNumero}">
								<p>
									<b>Número original:</b> ${docVO.originalNumero}
								</p>
							</c:if>
							<p class="${hide_only_GOVSP}">
								<b>De:</b> ${docVO.subscritorString}
							</p>
							<p class="${hide_only_GOVSP}">
								<b>Para:</b> ${docVO.destinatarioString}
							</p>
							<p>
								<b>Cadastrante:</b> ${docVO.cadastranteString}
								${docVO.lotaCadastranteString}
							</p>
							<p class="${hide_only_GOVSP}">
								<b>Espécie:</b> ${docVO.forma}
							</p>
							<p>
								<b>Modelo:</b> ${docVO.modelo}
							</p>
							<p id="descricao">
								<b>Descrição:</b> ${docVO.descrDocumento}
							</p>
							<script language="javascript">
                    function parseDescricao(id){
                        var descricao = document.getElementById(id);
                        
                        if (!descricao)
                            return;
                        
                        var wordsArray = descricao.innerHTML.split(/(\s+)/);
                        descricao.style.wordWrap = "break-word";
                        descricao.style.whiteSpace = "simple";
                        
                        for(var i=0; i < wordsArray.length; i++){
                            if (wordsArray[i].length > 50) {
                            	descricao.style.wordWrap = "break-all";
                                break;
                            }
                        }

                    }
                    
                    parseDescricao('descricao');
           		 </script>
							<p>
								<b>Classificação:</b> ${docVO.classificacaoDescricaoCompleta}
							</p>
							
							<c:if test="${not empty docVO.tipoDePrincipal and not empty docVO.principal}">
							<p>
								<b>${docVO.tipoDePrincipal}:</b> <a href="/sigawf/app/procedimento/${docVO.principalCompacto}">${docVO.principal}</a>
							</p>
							</c:if>
							
							<c:if test="${not empty docVO.dadosComplementares}">${docVO.dadosComplementares}</c:if>

						</tags:collapse>
					</div>

					<c:if test="${not empty descrCiencia}">
						<div class="card-sidebar card bg-light mb-3 ${hide_only_TRF2}">
							<tags:collapse title="Ciência" id="Ciencia" collapseMode="${collapse_Expanded}">
								<p>${descrCiencia}</p>
							</tags:collapse>
						</div>
					</c:if>

					<c:if test="${not empty m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}">
						<div class="card-sidebar card bg-light mb-3 ${hide_only_TRF2}">
							<tags:collapse title="Situação do Documento" id="SituacaoDoc" collapseMode="${collapse_Expanded}">
								<p class="font-weight-bold">
									${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
									<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
								 		- ${m.tamanhoDeArquivo}
									</c:if>
								</p>
								<c:if test="${not empty docVO.dtPrazoDeAssinatura}">
									<div>
										<span>Prazo de Assinatura: ${docVO.dtPrazoDeAssinatura}</span>
										<a class="float-right ${marca.exMovimentacao.podeCancelarOuAlterarPrazoDeAssinatura(titular, lotaTitular, m.mob, docVO.doc.movPrazoDeAssinatura)? '' : 'disabled' }" 
										href="javascript:postToUrl('/sigaex/app/expediente/mov/cancelar_movimentacao_gravar?id=${docVO.doc.movPrazoDeAssinatura.idMov}&sigla=${docVO.doc.movPrazoDeAssinatura.exMobil.sigla}')" 
											><i class="far fa-trash-alt"></i></a>
									</div>
								</c:if>
							</tags:collapse>
						</div>
					</c:if>

					<c:if test="${not empty docVO.cossignatarios}">
						<div class="card-sidebar card bg-light mb-3">
							<c:if test="${podeReordenar}">
								<c:set var="butOrdemAssinatura">
										<a class="text-dark" title="Reordenar itens" id="ordemAssinatura" style="float: right; margin-top: 0px; padding-left: 1em; padding-right: 1em;">
										Ordem de Assinatura <i class="fas fa-sort"></i>
									</button>
								</c:set>
							</c:if>
							<tags:collapse title="Cossignatários" id="Cossignatários" collapseMode="${collapse_Expanded}" addToTitle="${butOrdemAssinatura}">


								<c:if test="${podeReordenar}">
								<div class="menu-ordenacao  pb-2" style="text-align: center;height: auto;max-height: 0;opacity: 0;position: relative;left: -999px;transition: left .3s, opacity .3s, max-height .5s;">
									Clique e arraste os itens tracejados para reordená-los<br />							
									<form action="${pageContext.request.contextPath}/app/expediente/doc/reordenarAss" id="formReordenarAss" class="form" method="POST">									
										<input type="hidden" name="ids" id="inputHiddenIds" />													
										<input type="hidden" name="sigla" value="${sigla}" />
										<button type="submit" class="mt-3 ml-2 btn btn-success btn-sm align-center" id="btnSalvarOrdenacao" disabled>
											<i class="fas fa-check"></i> Salvar
										</button>
										<button type="button" class="mt-3 ml-2 btn btn-danger btn-sm align-center" id="btnCancelarOrdenacao">
												<i class="fas fa-times"></i> Cancelar
										</button>																					
									</form>
								</div>
								</c:if>
								<div class="card-body pl-1 pr-1 pt-0 pb-0  container-tabela-lista-assinaturas">
									<table class="mov tabela-assinaturas">
										<tbody id="${podeReordenar ? 'sortable' : ''}">
											<ul>
											<c:forEach var="ord" items="${docVO.getListaOrdenadaCossigSub()}">
												<tr>										
													<td style="display: none;">
														${ord.key.sigla}											
													</td>
													<td>
														<li>${ord.key.nomePessoa} <c:if test="${ord.key.sigla == docVO.doc.subscritor.sigla}"><a class="btn btn-sm btn-light mb-2">Subscritor</a></c:if>
														<c:if test="${ord.value != 0}">&nbsp;
															<a class="btn btn-sm btn-light mb-2" href="/sigaex/app/expediente/mov/excluir?id=${ord.value}">Excluir</a>
														</c:if>
														</li>
													</td>
												</tr>
											</c:forEach>
											</ul>
										</tbody>
									</table>
								</div>
							</tags:collapse>
						</div>
					</c:if>

					<c:if test="${not empty docVO.doc.perfis}">
						<div class="card-sidebar card bg-light mb-3">
							<tags:collapse title="Perfis" id="Perfis" collapseMode="${collapse_Expanded}">
								<c:forEach var="perfil" items="${docVO.doc.perfis}">
									<p style="margin-bottom: 3px;">
										<b>${perfil.key.descPapel}:</b>
									</p>
									<ul>
										<c:forEach var="pessoaOuLota" items="${perfil.value}">
												<c:catch var="exception">
													${pessoaOuLota.nomePessoa}
													<c:if test="${siga_cliente == 'GOVSP'}">
														 - ${pessoaOuLota.sigla}
														&nbsp;&nbsp;&nbsp;
														<a class="btn btn-sm btn-secondary mb-2 " href="javascript:if(confirm('Tem certeza que deseja exluir acompanhamento?')) location.href='/sigaex/app/expediente/mov/cancelarPerfil?sigla=${docVO.sigla}&idPessoa=${pessoaOuLota.id }';" >
															Excluir Acompanhamento
														</a><br/>
													</c:if>
												</c:catch>
												<c:if test="${not empty exception}">
													${pessoaOuLota.nomeLotacao}
													<c:if test="${siga_cliente == 'GOVSP'}">
														 - ${pessoaOuLota.sigla}
														&nbsp;&nbsp;&nbsp;
														<a class="btn btn-sm btn-secondary mb-2 " href="javascript:if(confirm('Tem certeza que deseja exluir acompanhamento?')) location.href='/sigaex/app/expediente/mov/cancelarPerfil?sigla=${docVO.sigla}&idLotacao=${pessoaOuLota.id }';" >
															Excluir Acompanhamento
														</a><br/>
													</c:if>
												</c:if>
											</li>
										</c:forEach>
									</ul>
								</c:forEach>
							</tags:collapse>
						</div>
					</c:if>


					<div class="card-sidebar card bg-light mb-3" >
						<tags:collapse title="Nível de Acesso" id="NivelAcesso" collapseMode="${collapse_NivelAcesso}">
							<p>
								<b>${docVO.nmNivelAcesso}</b>
								<c:if test="${not empty docVO.listaDeAcessos}">
									<c:choose>
										<c:when test="${docVO.listaDeAcessos.size() eq 1}">
											<c:forEach var="acesso" items="${docVO.listaDeAcessos}"
												varStatus="loop">
												<c:choose>
													<c:when test="${acesso eq 'PUBLICO'}">
										(Público)
									</c:when>
													<c:otherwise>
										(${acesso.sigla} - ${acesso.descricao})
									</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<ul>
												<c:forEach var="acesso" items="${docVO.listaDeAcessos}"
													varStatus="loop">
													<li>
														<c:choose>
															<c:when test="${siga_cliente == 'GOVSP'}">
																${acesso.sigla} - ${acesso.descricao}
															</c:when>
															<c:otherwise>
																${acesso.sigla}
															</c:otherwise>
														</c:choose>
													</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
								</c:if>
							</p>
						</tags:collapse>
					</div>

					<div class="card-sidebar card bg-light mb-3">										
						<tags:collapse title="Arquivos Auxiliares" id="ArqAuxiliares" collapseMode="${collapse_ArqAuxiliares}">
							<div class="container-confirmacao-cancelar-arquivo">
								<div class="confirmacao-cancelar-arquivo">
									<h1>Confirma cancelamento do arquivo?</h1>
									<p class="descricao-arquivo-confirmacao">descrição do arquivo</p>															
									<button type="button" class="btn btn-sm btn-success btn-cancelar-arquivo-nao">																
										Não
									</button>
									<button type="button" class="btn btn-sm btn-danger btn-cancelar-arquivo-sim">																
										Sim
									</button>
								</div>
							</div>		
							<div class="container-files">
								<c:if test="${docVO.podeAnexarArquivoAuxiliar}">
									<p>
										<a title="Anexar um novo arquivo auxiliar" class="btn btn-sm btn-secondary"
											href="${linkTo[ExMovimentacaoController].anexarArquivoAuxiliar}?sigla=${sigla}"
											${popup?'target="_blank" ':''}> 
											<i class="fas fa-plus-circle"></i>
											Incluir Arquivo
										</a>
									</p>
								</c:if>								
								<c:forEach var="mov" items="${m.movs}">
									<c:if test="${mov.exTipoMovimentacao == 'ANEXACAO_DE_ARQUIVO_AUXILIAR' and not mov.cancelada}">										
										<div class="files">
											<siga:links buttons="${false}" inline="${true}" separator="${false}">
												<c:forEach var="acao" items="${mov.acoes}">
													<c:set var="acaourl" value="${acao.url}" />
													<c:set var="acaourl"
														value="${fn:replace(acaourl, '__scheme__', pageContext.request.scheme)}" />
													<c:set var="acaourl"
														value="${fn:replace(acaourl, '__serverName__', pageContext.request.serverName)}" />
													<c:set var="acaourl"
														value="${fn:replace(acaourl, '__serverPort__', pageContext.request.serverPort)}" />
													<c:set var="acaourl"
														value="${fn:replace(acaourl, '__contextPath__', pageContext.request.contextPath)}" />
													<c:set var="acaourl"
														value="${fn:replace(acaourl, '__pathInfo__', pageContext.request.pathInfo)}" />
													<c:if test="${acao.url == acaourl}">
														<c:set var="acaourl"
															value="${pageContext.request.contextPath}${acao.url}" />
													</c:if>	
													<c:choose> 
															<c:when test="${siga_cliente ne 'GOVSP'}">
																<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
																		pre="${acao.pre}" pos="${acao.pos}" url="${acaourl}"
																		test="${true}" popup="${acao.popup}"
																		confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
																		idAjax="${mov.idMov}" classe="${acao.classe}" />
															</c:when>
															<c:when test="${not empty acao.icone and acao.nomeNbsp ne 'Cancelar' and siga_cliente eq 'GOVSP'}">
																<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
																	pre="${acao.pre}" pos="${acao.pos}" url="${acaourl}"
																	test="${true}" popup="${acao.popup}"
																	confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
																	idAjax="${mov.idMov}" classe="${acao.classe}" />																	
															</c:when>
															<c:when test="${acao.nomeNbsp eq 'Cancelar' and siga_cliente eq 'GOVSP'}">
																<button type="button" class="btn btn-sm btn-outline-danger btn-cancel"																	
																	onclick="confirmarExclusaoArquivoAuxiliar(${mov.idMov}, '${mov.mov.exMobil.sigla}', this)">																
																	Cancelar
																</button>																													
															</c:when>															
														</c:choose>																							
												</c:forEach>
												<div class="row ml-4 mb-3">
													<small class="form-text text-muted mt-0">
														<siga:link title="${mov.mov.cadastrante.sigla}/${mov.mov.lotaCadastrante.sigla} - ${mov.tempoRelativo}" test="${true}" classe="${acao.classe}" />																										
													</small>
												</div>
											</siga:links>
										</div>
									</c:if>
								</c:forEach>
							</div>
						</tags:collapse>
					</div>
	
						<div class="gt-sidebar-content">
							<div id="gc"></div>
						</div>
					</div>
		<%@ include file="marcar.jsp"%>
	</c:forEach>

</div>
</div>

<script type="text/javascript">
	var css = "<style>.ui-widget-header {border: 1px solid #365b6d;background: #365b6d;}</style>";
	$(css).appendTo("head");
</script>

<div class="gt-bd clearfix" id="svgColaboracao"
	style="display: none; overflow-y: scroll;">
	<div class="gt-content clearfix">
		<div id="desc_editar_colaboracao">
			<div style="padding-bottom: 15px;" id="output2Colaboracao"
				oncontextmenu="return false;">
				<h4 id="naoCarregouBigColaboracao">Carregando...</h4>
			</div>
		</div>
	</div>
	<p style="font-weight: bold">Clique sobre a imagem com o botão
		esquerdo para ampliar ou com o direito para reduzir.</p>
	<a href="javascript:void(0)"
		onclick="javascript: $('#svgColaboracao').dialog('close');"
		class="gt-btn-large gt-btn-left">Voltar</a>
</div>



<div class="gt-bd clearfix" id="svgRelacaoDocs"
	style="display: none; overflow-y: scroll;">
	<div class="gt-content clearfix">
		<div id="desc_editar_relacaoDocs">
			<div style="padding-bottom: 15px;" id="output2RelacaoDocs"
				oncontextmenu="return false;">
				<h4 id="naoCarregouBigRelacaoDocs">Carregando...</h4>
			</div>
		</div>
	</div>
	<p style="font-weight: bold">Clique sobre a imagem com o botão
		esquerdo para ampliar ou com o direito para reduzir.</p>
	<a href="javascript:void(0)"
		onclick="javascript: $('#svgRelacaoDocs').dialog('close');"
		class="gt-btn-large gt-btn-left">Voltar</a>
</div>

<div class="gt-bd clearfix" id="svgTramitacao"
	style="display: none; overflow-y: scroll;">
	<div class="gt-content clearfix">
		<div id="desc_editar_tramitacao">
			<div style="padding-bottom: 15px;" id="output2Tramitacao"
				oncontextmenu="return false;">
				<h4 id="naoCarregouBigTramitacao">Carregando...</h4>
			</div>
		</div>
	</div>
	<p style="font-weight: bold">Clique sobre a imagem com o botão
		esquerdo para ampliar ou com o direito para reduzir.</p>
	<a href="javascript:void(0)"
		onclick="javascript: $('#svgTramitacao').dialog('close');"
		class="gt-btn-large gt-btn-left">Voltar</a>
</div>

<c:if test="${f:resource('/sigawf.ativo') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;WF:Módulo de Workflow')}">
	<script type="text/javascript">
		<c:if test="${ (docVO.tipoFormaDocumento == 'processo_administrativo')}">
			var url = "/sigawf/app/doc?sigla=${docVO.mob.sigla}&ts=1${currentTimeMillis}";
		</c:if>
		<c:if test="${(not m.mob.geral) or (docVO.tipoFormaDocumento != 'processo_administrativo')}">
			var url = "/sigawf/app/doc?sigla=${docVO.doc.codigo}&ts=1${currentTimeMillis}";
		</c:if>
		
        $.ajax({
            url: url,
            type: "GET"
        }).fail(function(jqXHR, textStatus, errorThrown){
			var div = $(".wf_div:last");
			$(div).html('<p class="erro">Houve um problema ao verificar se há fluxos do SIGA-WF associados a este documento. Favor atualizar a página para tentar novamente.</p>');
        }).done(function(data, textStatus, jqXHR ){
			var div = $(".wf_div:last");
			$(div).html(data);
        });
	</script>
</c:if>
<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
	<c:url var="url" value="/../sigagc/app/knowledgeSidebar">
		<c:param name="tags">@documento</c:param>
		<c:forEach var="tag" items="${docVO.tags}">
			<c:param name="tags">${tag}</c:param>
		</c:forEach>
		<c:param name="estilo">sidebar</c:param>
		<c:param name="ts">${currentTimeMillis}</c:param>
	</c:url>
	<script type="text/javascript">
		var urlGc = "${url}";

        $.ajax({
            url: urlGc,
            type: "GET"
        }).fail(function(jqXHR, textStatus, errorThrown){
        	if (errorThrown !== "Not Found")
        		$("#gc").html(errorThrown);
        }).done(function(data, textStatus, jqXHR ){
        	$("#gc").html(data); 
        });
	</script>
</c:if>
</div>
</div>

<c:if test="${recebimentoPendente  and !origemRedirectTransferirGravar}">				
	<style>
		.gt-sidebar, .siga-menu-acoes {
			filter: blur(2px);
   			-moz-filter: blur(2px);
   			-webkit-filter: blur(2px);
   			-o-filter: blur(2px);    			
		}	
		
		.gt-sidebar a, .siga-menu-acoes a {
			cursor: not-allowed;
   			pointer-events: none;
		}		
		
		#modalReceberDocumento {
			left: auto !important;
			overflow-y: hidden !important;
			max-width: 260px;
			max-height: 180px;
			padding-right: 0;
		}		
				
		.siga-btn-receber-doc {					
			opacity: 0;
			visibility: hidden;					
			background: #007bff;
			border-radius: 50%;
			width: 60px;
			height: 60px;		
			position: fixed;
			bottom: 25px;
			right: 25px;			
			box-shadow: 0 0 0 0 #28a745, 0 0 0 0 #007bff;	
			transition: box-shadow 1.1s cubic-bezier(.19,1,.22,1), opacity 1.3s, visibility 1.3s;
			animation: piscar 1s linear infinite;
		}

		.siga-btn-receber-doc:hover {
			box-shadow: 0 0 0 8px #007bff, 0 0 0 8px #007bff;		
		}
		
		.icone-receber-doc {		
			font-size: 24px;			
			transition: font-size .5s;
		}
		
		.siga-btn-receber-doc:hover .icone-receber-doc {
			font-size: 28px;
		}	
		
		@keyframes piscar {
		  50% {
		    box-shadow: 0 0 0 8px #007bff, 0 0 0 8px #007bff;
		  }
		}	
	</style>
	
	<div class="modal fade" id="modalReceberDocumento" tabindex="-1" role="dialog" aria-labelledby="confirmacao" aria-hidden="true">
	  <div class="modal-dialog text-center" role="document">
	    <div class="modal-content">	      
	      <div class="modal-body text-center">Deseja receber o documento?</div>
	      <div class="modal-footer text-center">
	      	<div class="row" style="margin: 0 auto;">
		        <button id="button_receber_cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Não</button>		        	       
	        	<a href="${linkTo[ExMovimentacaoController].aReceber()}?sigla=${docVO.mob.sigla}" onclick="sigaSpinner.mostrar();" 
	        		class="btn btn-primary btn-acao" role="button" aria-pressed="true" style="margin-left: .5rem;">Sim</a>		        
		    </div>    
	      </div>
	    </div>
	  </div>
	</div>	
	<button id="button_receber_ok" type="button" class="btn btn-primary siga-btn-receber-doc" data-placement="left" title="Receber" data-siga-modal-abrir="modalReceberDocumento">
		<i class="fas fa-envelope-open-text icone-receber-doc"></i>
	</button>
	<c:if test="${!docVO.mob.isJuntado() and !origemRedirectTransferirGravar}">
	<script>
		$(function() {
			var modalReceberDocumento = $('#modalReceberDocumento');				
			var btnReceberDocumento = $('.siga-btn-receber-doc');
			
			sigaModal.abrir('modalReceberDocumento');								
			btnReceberDocumento.tooltip();					
			$('body').css('overflow', 'auto');
										
			modalReceberDocumento.on('shown.bs.modal', function (e) {
				btnReceberDocumento.css({'opacity':'0', 'visibility':'hidden'});								
			});	
			
			modalReceberDocumento.on('hidden.bs.modal', function (e) {
				btnReceberDocumento.css({'opacity':'1', 'visibility':'visible'});								
			});												
		});	
	</script>	
	</c:if>
</c:if>
<c:if test="${docVO.doc.isComposto()}">
	<c:choose>
		<c:when test="${podeExibirTodosOsVolumes }">
			<siga:siga-modal id="modalDeConfirmacaoArqCorrente" exibirRodape="true" 
					tituloADireita="<i class='fas fa-exclamation-circle' style='font-size: 1.5em; color: #ffc107;'></i> <label style='font-size: 1.1em;vertical-align: middle;'><b>Atenção</b></label>"
					descricaoBotaoFechaModalDoRodape="Não" descricaoBotaoDeAcao="Sim" 
					linkBotaoDeAcao="${linkTo[ExMovimentacaoController].aArquivarCorrenteGravar()}?sigla=${docVO.sigla}">
				<div class="modal-body">
		       		 Verifique se há necessidade de incluir o Termo de Encerramento para este documento. Deseja continuar com o arquivamento?
		     	</div>	     	
			</siga:siga-modal>	
		</c:when>
		<c:otherwise>
			<siga:siga-modal id="modalDeConfirmacaoArqCorrente" exibirRodape="true" 
					tituloADireita="<i class='fas fa-exclamation-circle' style='font-size: 1.5em; color: #ffc107;'></i> <label style='font-size: 1.1em;vertical-align: middle;'><b>Atenção</b></label>"
					descricaoBotaoFechaModalDoRodape="Não" descricaoBotaoDeAcao="Sim" 
					linkBotaoDeAcao="${linkTo[ExMovimentacaoController].aArquivarCorrenteGravar()}?sigla=${mob.sigla}">
				<div class="modal-body">
		       		 Verifique se há necessidade de incluir o Termo de Encerramento para este documento. Deseja continuar com o arquivamento?
		     	</div>	     	
			</siga:siga-modal>			
		</c:otherwise>
	</c:choose>
	<script>
		$(function() {
			var btnArqCorrente = $('.siga-btn-arq-corrente');				
			if (btnArqCorrente) {				
				btnArqCorrente.attr('href', '#').attr('data-siga-modal-abrir', 'modalDeConfirmacaoArqCorrente');					
			}							
		});	
	</script>
</c:if>
<c:if test="${mob.isJuntado()}">			
	<siga:siga-modal id="modalDeAvisoTornarDocumentoSemEfeito" exibirRodape="true" 
		tituloADireita="<i class='fas fa-exclamation-circle' style='font-size: 1.5em; color: #ffc107;'></i> <label style='font-size: 1.1em;vertical-align: middle;'><b>Atenção</b></label>"
		descricaoBotaoFechaModalDoRodape="Ok">
		<div class="modal-body">
       		É necessário desentranhar o documento para realizar o seu cancelamento.
     	</div>	     	
	</siga:siga-modal>
	
	<c:if test="${mob.mobilPrincipal.isSobrestado() && mob.mobilPrincipal.doc.isComposto()}">
		<siga:siga-modal id="modalDeAvisoDesentranhar" exibirRodape="true" 
			tituloADireita="<i class='fas fa-exclamation-circle' style='font-size: 1.5em; color: #ffc107;'></i> <label style='font-size: 1.1em;vertical-align: middle;'><b>Atenção</b></label>"
			descricaoBotaoFechaModalDoRodape="Ok">
			<div class="modal-body">
	       		Não é possível fazer o desentranhamento porque o documento ao qual este está juntado encontra-se sobrestado.
	     	</div>	     	
		</siga:siga-modal>
		<script>
			$(function() {
				var btnDesentranhar = $('.siga-btn-desentranhar');
				if (btnDesentranhar) {
					btnDesentranhar.attr('href', '#').attr('data-siga-modal-abrir', 'modalDeAvisoDesentranhar');					
				}							
			});
		</script>
	</c:if>	
			
	<script>
		$(function() {
			var btnCancelar = $('.siga-btn-tornar-documento-sem-efeito');				
			if (btnCancelar) {										
				btnCancelar.attr('href', '#').attr('data-siga-modal-abrir', 'modalDeAvisoTornarDocumentoSemEfeito');					
			}							
		});	
		
		$(function() {
			var btnRefazer = $('.siga-btn-refazer');				
			if (btnRefazer) {										
				btnRefazer.attr('href', '#').attr('data-siga-modal-abrir', 'modalDeAvisoTornarDocumentoSemEfeito');					
			}							
		});
	</script>	
</c:if>
	
<script>
	var containerArquivosAuxiliares = $('.container-files');
	var containerConfimarcaoArquivoAuxiliarACancelar = $('.container-confirmacao-cancelar-arquivo');
	var descricaoArquivoAuxiliarACancelar = $('.descricao-arquivo-confirmacao');
	
	$(function() {																				
		atualizarDescricaoArquivosAuxiliares();																												
	});
	
	function atualizarDescricaoArquivosAuxiliares() {
		$('.files').find('.btn.btn-sm.btn-light').each(function(index) {
			var arquivo = $(this);
			var btnCancelar = arquivo.parent().find('.btn.btn-sm.btn-outline-danger.btn-cancel');																					
														
			arquivo.attr('title', 'Clique para baixar o arquivo: ' + this.text);
													
			if (arquivo.text().length > 150) {
				arquivo.text(this.text.substring(0, 150) + '...');
			}																					
			
			if (btnCancelar) {
				btnCancelar.attr('title', 'Cancelar arquivo: ' + this.text).attr('data-description', this.text);
			}																							
		});		
	}
	
	function confirmarExclusaoArquivoAuxiliar(idMov, sigla, botao) {										
		var btnCancela = $('.btn-cancelar-arquivo-nao');
		var btnConfirma = $('.btn-cancelar-arquivo-sim');			
		
		containerConfimarcaoArquivoAuxiliarACancelar.css({'visibility':'visible', 'opacity':'1'});																				
		containerArquivosAuxiliares.css({'visibility':'hidden', 'opacity':'0'});												
		
		btnCancela.attr('onclick', 'cancelarExclusaoArquivoAuxiliar()');										
		btnConfirma.attr('onclick', 'excluirArqAuxiliar(' + idMov + ', \'' + sigla + '\')');
					
		descricaoArquivoAuxiliarACancelar.text($(botao).attr('data-description'));										
	}
	
	function cancelarExclusaoArquivoAuxiliar() {
		containerConfimarcaoArquivoAuxiliarACancelar.css({'visibility':'hidden', 'opacity':'0'});
		containerArquivosAuxiliares.css({'visibility':'visible', 'opacity':'1'});				
	}
	window.onload = function () { 
		if (document.getElementById('painel'))
			document.getElementById('painel').src = montarUrlDocPDF('${urlCapturado}',document.getElementById('visualizador').value); 
	} 
</script>
<c:if test="${podeReordenar}"> 
	<script src="/siga/javascript/assinatura.reordenar-ass.js"></script>
</c:if>
</siga:pagina>
