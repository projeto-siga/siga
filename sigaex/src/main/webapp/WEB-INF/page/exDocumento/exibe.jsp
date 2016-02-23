<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="libs"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>
<siga:cabecalho titulo="Documento" popup="${param.popup}" />

<script>
	if (window.Worker) {
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
</script>

<div class="gt-bd" style="padding-bottom: 0px;" id="page">
	<div class="gt-content">
		<c:if test="${not empty param.msg}">
			<p align="center">
				<b>${param.msg}</b>
			</p>
		</c:if>
		<form name="frm" action="exibir" theme="simple" method="POST">
		</form>
		<h2>
			<c:if test="${empty ocultarCodigo}">
				${docVO.sigla}
			</c:if>
		</h2>
		<c:set var="primeiroMobil" value="${true}" />
		<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
			<c:if
				test="${m.mob.geral or true or (((mob.geral or (mob.id == m.mob.id)) and ((m.mob.getUltimaMovimentacaoNaoCancelada() != null))))}">
				<h3 style="margin-bottom: 0px;">
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
							<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}" pre="${acao.pre}" pos="${acao.pos}"
								url="${pageContext.request.contextPath}${acao.url}" test="${true}" popup="${acao.popup}"
								confirm="${acao.msgConfirmacao}" classe="${acao.classe}" 
								estilo="line-height: 160% !important" />
						</c:forEach>
					</siga:links>
				</c:if>
				<div id="formMov" style="display: none"></div>
				<script>
					function meuTeste(result) {
						jFormMov = $('#formMov');
						jFormMov.html("");
						jFormMov.append($(result).find("script"));
						jFormMov.append($(result).find(".gt-content-box"));
						jFormMov.show('blind', {}, 500);
						$("input[value='Cancela']").removeAttr('onclick');
						$("input[value='Cancela']").click(function() {
							jFormMov.hide('blind', {}, 500);
						});
						jFormMov
								.find("form")
								.submit(
										function(e) {
											var postData = $(this)
													.serializeArray();
											var formURL = $(this)
													.attr("action");
											$
													.ajax({
														url : formURL,
														type : "POST",
														data : postData,
														success : function(
																data,
																textStatus,
																jqXHR) {
															if (data
																	.indexOf("ompletar") > 0) {
																$(
																		"<div style='margin-top: 15px; color: #365b6d; font-weight: bold; font-size: 108%'>"
																				+ data
																						.substring(
																								data
																										.indexOf('<h3>') + 4,
																								data
																										.indexOf('</h3>'))
																				+ "</div>")
																		.dialog(
																				{
																					modal : true,
																					resizable : false
																				});
															}
														},
														error : function(jqXHR,
																textStatus,
																errorThrown) {
															alert(errorThrown);
														}
													});
											e.preventDefault();
										});
					}
				</script>

				<c:set var="dtUlt" value="" />
				<c:set var="temmov" value="${false}" />
				<c:forEach var="mov" items="${m.movs}">
					<c:if test="${ (mov.idTpMov != 14 and not mov.cancelada)}">
						<c:set var="temmov" value="${true}" />
					</c:if>
				</c:forEach>
				<div class="gt-bd gt-cols clearfix" style="padding-top: 0px; margin-top: 25px; padding-left: 0px;">
					<div class="gt-content">
						<c:if test="${f:resource('isWorkflowEnabled')}">
							<c:if test="${ (primeiroMobil) and (docVO.tipoFormaDocumento == 'processo_administrativo')}">
								<div id="${docVO.sigla}" depende=";wf;" class="wf_div" />
								<!--ajax:${doc.codigo}-${i}-->
								<!--/ajax:${doc.codigo}-${i}-->
					</div>
								<c:set var="primeiroMobil" value="${false}" />
							</c:if>
						<c:if test="${(not m.mob.geral) or (docVO.tipoFormaDocumento != 'processo_administrativo')}">
							<div id="${m.sigla}" depende=";wf;" class="wf_div" />
							<!--ajax:${doc.codigo}-${i}-->
							<!--/ajax:${doc.codigo}-${i}-->
				</div>
					</c:if>

				</c:if>

				<c:choose>
					<c:when test="${docVO.conteudoBlobHtmlString != null}">
						<div class="gt-content-box" style="padding: 10px;">
							<table style="width: 100%">
								<tr>
									<td>
										<tags:fixdocumenthtml>
											${docVO.conteudoBlobHtmlString}
										</tags:fixdocumenthtml>
									</td>
								</tr>
							</table>
						</div>
					</c:when>
					<c:when test="${docVO.doc.pdf}">
						<iframe style="visibility: visible; margin: 0px; padding: 0px;" 
							name="painel" id="painel" src="/sigaex/app/arquivo/exibir?arquivo=${docVO.doc.referenciaPDF}" align="right" width="100%"
							frameborder="0" 
							scrolling="auto"></iframe>
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
						<table class="gt-table mov">
							<thead>
								<tr>
									<th rowspan="2" align="center" style="padding: 5px 5px;">Data</th>
									<th rowspan="2" style="padding: 5px 5px;">Lotação</th>
									<th rowspan="2" style="padding: 5px 5px;">Evento</th>
									<th rowspan="2" style="padding: 5px 5px;">Descrição</th>
								</tr>
							</thead>
							<c:set var="evenorodd" value="odd" />
							<c:forEach var="mov" items="${m.movs}">
								<c:if
									test="${ (mov.idTpMov != 14 and
							          not mov.cancelada)}">
									<tr class="${mov.classe} ${mov.disabled}">
										<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
										<c:choose>
											<c:when test="${dt == dtUlt}">
												<c:set var="dt" value="" />
											</c:when>
											<c:otherwise>
												<c:set var="dtUlt" value="${dt}" />
											</c:otherwise>
										</c:choose>
										<c:set var="lota" value="${mov.mov.lotaCadastrante.sigla}" />
										<c:choose>
											<c:when test="${lota == lotaUlt}">
												<c:set var="lota" value="" />
											</c:when>
											<c:otherwise>
												<c:set var="lotaUlt" value="${lota}" />
											</c:otherwise>
										</c:choose>
										<td align="center" style="padding: 5px 5px;">${dt}</td>
										<td style="padding: 5px 5px;">${lota}</td>
										<td style="padding: 5px 5px;">${mov.mov.exTipoMovimentacao.sigla}</td>
										<td style="padding: 5px 5px; word-break: break-all;">${mov.descricao}
											<c:if test='${mov.idTpMov != 2}'> ${mov.complemento} </c:if>
											<c:set var="assinadopor" value="${true}" /> <siga:links
												inline="${true}"
												separator="${not empty mov.descricao and mov.descricao != null}">
												<c:forEach var="acao" items="${mov.acoes}">
													<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
														pos="${acao.pos}"
														url="${pageContext.request.contextPath}${acao.url}"
														test="${true}" popup="${acao.popup}"
														confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
														idAjax="${mov.idMov}" classe="${acao.classe}" />
													<c:if test='${assinadopor and mov.idTpMov == 2}'> ${mov.complemento}
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
	
	<div class="gt-sidebar">
		<c:if test="${m.pendencias}">
			<div class="gt-sidebar-content" id="pendencias">
				<h3>Pendências</h3>
				<c:if test="${not empty m.pendenciaProximoModelo}">
					<p style="margin-bottom: 3px;">
						<b style="color: rgb(195, 0, 0)">Próximo Documento:</b>
					</p>
					<ul>
						<c:if test="${m.pendenciaProximoModelo == 110}">
							<li>
								<a	href="${pageContext.request.contextPath}/app/expediente/doc/editar?mobilPaiSel.sigla=${m.sigla}&criandoAnexo=true"
									title="Despacho de Concessão de Diárias" style="text-decoration: none">
										Despacho de Concessão de Diárias
								</a>
							</li>
						</c:if>
						<c:if test="${m.pendenciaProximoModelo == 111}">
							<li>
								<a	href="${pageContext.request.contextPath}/app/expediente/doc/editar?mobilPaiSel.sigla=${m.sigla}&criandoAnexo=true"
									title="Registro de Pagamento de Diárias" style="text-decoration: none">
										Registro de Pagamento de Diárias
								</a>
							</li>
						</c:if>
						<c:if test="${m.pendenciaProximoModelo == 112}">
							<li>
								<a	href="${pageContext.request.contextPath}/app/expediente/doc/editar?mobilPaiSel.sigla=${m.sigla}&criandoAnexo=true"
									title="Certidão de Publicação de Diárias" style="text-decoration: none">
										Certidão de Publicação de Diárias
								</a>
							</li>
						</c:if>
					</ul>
				</c:if>
				
				<c:if test="${not empty m.pendenciasDeAnexacao}">
					<p style="margin-bottom: 3px;">
						<b style="color: rgb(195, 0, 0)">Anexos Pendentes:</b>
					</p>
					<ul>
						<c:forEach var="anexoPendente" items="${m.pendenciasDeAnexacao}">
							<li>
								<a
								href="${pageContext.request.contextPath}/app/expediente/mov/anexar?sigla=${m.sigla}"
								title="${anexoPendente.descricao}" style="text-decoration: none">
									${anexoPendente.descricao}
								</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
				<c:if test="${not empty m.anexosNaoAssinados}">
					<p style="margin-bottom: 3px;">
						<b style="color: rgb(195, 0, 0)">Anexos não assinados:</b>
					</p>
					<ul>
						<c:forEach var="naoAssinado" items="${m.anexosNaoAssinados}">
							<li>
								<a
								href="javascript:popitup('${pageContext.request.contextPath}/app/expediente/mov/exibir?id=
								${m.sigla}&popup=true')"
								title="${naoAssinado.descricao}" style="text-decoration: none">
									${naoAssinado.mov.nmArqMov} </a>
							</li>
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
								/app/expediente/mov/naoAssinado?id=${naoAssinado.idMov}&popup=true')"
								title="${naoAssinado.descricao}" style="text-decoration: none">
									${naoAssinado.descricao} </a></li>
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
							<li>
							<a
								href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${naoJuntado.sigla}"
								title="${naoJuntado.descrDocumento}"
								style="text-decoration: none"> ${naoJuntado.sigla} </a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
				<c:if test="${not empty m.pendenciasDeColaboracao}">
					<p style="margin-bottom: 3px;">
						<b style="color: rgb(195, 0, 0)">Pendências de Colaboração:</b>
					</p>
					<ul>
						<c:forEach var="colaboracaoPendente" items="${m.pendenciasDeColaboracao}">
							<li>
								<a
								href="${pageContext.request.contextPath}/app/expediente/mov/anexar?sigla=${m.sigla}"
								title="${colaboracaoPendente.descricao}" style="text-decoration: none">
									${colaboracaoPendente.descricao}
								</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</c:if>

		<c:if
			test="${not empty docVO.documentosPublicados or not empty docVO.boletim}">
			<div class="gt-sidebar-content" style="padding-top: 10px">
				<h3>Boletim Interno</h3>
				<c:if test="${not empty docVO.documentosPublicados}">
					<p class="apensados" style="margin-top: 0pt;">
						<b>Documentos Publicados: </b>
						<c:forEach var="documentoPublicado"
							items="${docVO.documentosPublicados}">
							<a
								href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documentoPublicado.sigla}"
								title="${documentoPublicado.sigla}"
								style="text-decoration: none"> ${documentoPublicado.sigla}
							</a>
							&nbsp;
					</c:forEach>
					</p>
				</c:if>
				<c:if test="${not empty docVO.boletim}">
					<p class="apensados" style="margin-top: 0pt;">
						<b>Publicado no Boletim: </b>
						<a
							href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${docVO.boletim.sigla}"
							title="${docVO.boletim.sigla}" style="text-decoration: none">
							${docVO.boletim.sigla}
						</a>
					</p>
				</c:if>
			</div>
		</c:if>
		<c:if test="${not empty docVO.outrosMobsLabel}">
			<jsp:useBean id="now" class="java.util.Date" />
			<div class="gt-sidebar-content">
				<h3>${docVO.outrosMobsLabel}</h3>
				<ul style="list-style-type: none; margin: 0; padding: 0;">
					<c:forEach var="entry" items="${docVO.marcasPorMobil}">
						<c:set var="outroMob" value="${entry.key}" />
						<li>
							<c:choose>
								<c:when test="${outroMob.numSequencia == m.mob.numSequencia}">
									<i><b>${outroMob.terminacaoSigla}</b></i>
								</c:when>
								<c:otherwise>
									<a
										href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${outroMob.sigla}"
										title="${outroMob.doc.descrDocumento}" 
										style="text-decoration: none"> ${outroMob.terminacaoSigla}
									</a>
								</c:otherwise>
							</c:choose>
							&nbsp;-&nbsp; 
							<c:forEach var="marca" items="${entry.value}" varStatus="loop">
								${marca.cpMarcador.descrMarcador} 
								<c:if test="${marca.dtIniMarca gt now}">
									a partir de ${marca.dtIniMarcaDDMMYYYY}
								</c:if>
								<c:if test="${not empty marca.dtFimMarca}"> 
									até ${marca.dtFimMarcaDDMMYYYY}
								</c:if>
								<c:if test="${not empty marca.dpLotacaoIni}">
									[${marca.dpLotacaoIni.lotacaoAtual.sigla}
									<c:if test="${not empty marca.dpPessoaIni}">
										&nbsp;${marca.dpPessoaIni.pessoaAtual.sigla}
									</c:if>
									]
								</c:if>
							</c:forEach>
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:if>
		
		<!-- Início mapa colaboração -->
		<c:if test="${docVO.dotColaboracao.numNodos > 1}">
		<!-- Início mapa tramitação -->
	
		<!-- Sidebar List -->
		<div class="gt-sidebar-content" id="Colaboracao">
			<h3 style="margin-bottom: 10px">Colaboração</h3>
			<div style="display: none" id="inputColaboracao">
			</div>
			<a href="javascript:void(0)" href="javascript:void(0)" style="text-decoration: none">
			<div id="outputColaboracao" style="border: 0px; background-color: #e2eaee; padding: 0px;">
			</div>
			</a>
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
				var input = 'digraph ""{ graph[tooltip="Colaboração"] ${docVO.dotColaboracao} }';
				input = escapeAcentos(input);

				if (window.VizWorker) {
					document.getElementById("output2Colaboracao").innerHTML = "Aguarde...";
					window.VizWorker.postMessage({
						id : "output2Colaboracao",
						graph : input
					});
					return;
				}
				
				var result = Viz(input, "svg", "dot");
		  		document.getElementById("output2Colaboracao").innerHTML = result;
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
			$("#outputColaboracao").css("background-color", $("html").css("background-color"));
			var bgcolor = rgb2hex($("#outputColaboracao").css("background-color"));
			var input = 'digraph "" { graph[tooltip="Colaboração" ratio="' + ratioColaboracao() + '"  color="'+ bgcolor +'" bgcolor="'+bgcolor+'" URL="javascript: bigmapColaboracao();"]; node[fillcolor=white fontsize=50 style=filled ]; edge[fontsize=30]; ${docVO.dotColaboracao} }';
			input = escapeAcentos(input);

			if (window.VizWorker) {
				document.getElementById("outputColaboracao").innerHTML = "Aguarde...";
				window.VizWorker.postMessage({
					id : "outputColaboracao",
					graph : input
				});
				return;
			}
			
			var result = Viz(input, "svg", "dot");
		  	document.getElementById("outputColaboracao").innerHTML = result;
			updateContainerColaboracao();
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
			<div class="gt-sidebar-content">
				<h3 style="margin-bottom: 10px">Documentos Relacionados</h3>
				<div id="outputRelacaoDocs" style="border: 0px; background-color: #e2eaee; padding: 0px">
					<c:forEach items="${docVO.dotRelacaoDocs.asMap}" var="mapa">
						<p style="margin-bottom: 3px;">
							<b>${mapa.key}:</b>
						</p>
						<ul>
							<c:forEach var="mobRelacionado" items="${mapa.value}">
								<li><a
									href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobRelacionado.sigla}"
									title="${mobRelacionado.doc.descrDocumento}"style="text-decoration: none">
									${mobRelacionado.sigla} </a>
								</li>
							</c:forEach>
						</ul>
					</c:forEach>
				</div>
			</div>

			<script>
				$(document).ready(function() {
					$(window).resize(function() {
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
						var input = 'digraph "" { graph[tooltip="Documentos Relacionados"]; edge[penwidth=2]; ${docVO.dotRelacaoDocs} }';
						input = escapeAcentos(input);

						if (window.VizWorker) {
							document.getElementById("output2RelacaoDocs").innerHTML = "Aguarde...";
							window.VizWorker.postMessage({
								id : "output2RelacaoDocs",
								graph : input
							});
							return;
						}
						
						var result = Viz(input, "svg", "dot");
						document.getElementById("output2RelacaoDocs").innerHTML = result;
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
					$("#outputRelacaoDocs").css("background-color",
							$("html").css("background-color"));
					var bgcolor = rgb2hex($("#outputRelacaoDocs").css(
							"background-color"));
					var input = 'digraph "" { graph[ratio="0.4" tooltip="Documentos Relacionados" color="'
							+ bgcolor
							+ '" bgcolor="'
							+ bgcolor
							+ '" URL="javascript: bigmapRelacaoDocs();"]; node[fillcolor=white fontsize=20 style=filled]; edge[penwidth=2]; ${docVO.dotRelacaoDocs} }';
					input = escapeAcentos(input);

					if (window.VizWorker) {
						document.getElementById("outputRelacaoDocs").innerHTML = "Aguarde...";
						window.VizWorker.postMessage({
							id : "outputRelacaoDocs",
							graph : input
						});
						return;
					}
					
					var result = Viz(input, "svg", "dot");
					document.getElementById("outputRelacaoDocs").innerHTML = result;
					updateContainerRelacaoDocs();
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
		<div class="gt-sidebar-content" id="tramitacao">
			<h3 style="margin-bottom: 10px">Tramitação</h3>
			<div style="display: none" id="inputTramitacao">
			</div>
			<a href="javascript:void(0)" href="javascript:void(0)" style="text-decoration: none">
			<div id="outputTramitacao" style="border: 0px; background-color: #e2eaee; padding: 0px;">
			</div>
			</a>
		</div>
		<script>
		$(document).ready(function () {
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
				var input = 'digraph ""{ graph[tooltip="Tramitação"] ${docVO.dotTramitacao} }';
				input = escapeAcentos(input);

				if (window.VizWorker) {
					document.getElementById("output2Tramitacao").innerHTML = "Aguarde...";
					window.VizWorker.postMessage({
						id : "output2Tramitacao",
						graph : input
					});
					return;
				}

				var result = Viz(input, "svg", "dot");
		  		document.getElementById("output2Tramitacao").innerHTML = result;
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
			$("#outputTramitacao").css("background-color", $("html").css("background-color"));
			var bgcolor = rgb2hex($("#outputTramitacao").css("background-color"));
			var input = 'digraph "" { graph[tooltip="Tramitação" ratio="' + ratioTramitacao() + '"  color="'+ bgcolor +'" bgcolor="'+bgcolor+'" URL="javascript: bigmapTramitacao();"]; node[fillcolor=white fontsize=50 style=filled ]; edge[fontsize=30]; ${docVO.dotTramitacao} }';
			input = escapeAcentos(input);

			if (window.VizWorker) {
				document.getElementById("outputTramitacao").innerHTML = "Aguarde...";
				window.VizWorker.postMessage({
					id : "outputTramitacao",
					graph : input
				});
				return;
			}
			
			var result = Viz(input, "svg", "dot");
		  	document.getElementById("outputTramitacao").innerHTML = result;
			updateContainerTramitacao();
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
    	
    	<!-- Fim mapa tramitação -->
    	</c:if>

		<div class="gt-sidebar-content">
			<h3>Documento ${docVO.doc.exTipoDocumento.descricao}</h3>
			<p>
				<b>Suporte:</b> ${docVO.fisicoOuEletronico}
			</p>
			<p>
				<b>Data:</b> ${docVO.dtDocDDMMYY}
			</p>
			<p>
				<b>De:</b> ${docVO.subscritorString}
			</p>
			<p>
				<b>Para:</b> ${docVO.destinatarioString}
			</p>
			<p>
				<b>Cadastrante:</b> ${docVO.cadastranteString} ${docVO.lotaCadastranteString}
			</p>
			<p>
				<b>Espécie:</b> ${docVO.forma}
			</p>
			<p>
				<b>Modelo:</b> ${docVO.modelo}
			</p>
			<p>
				<b>Descrição:</b> ${docVO.descrDocumento}
			</p>
			<p>
				<b>Classificação:</b> ${docVO.classificacaoDescricaoCompleta}
			</p>
			<c:if test="${not empty docVO.dadosComplementares}">
		    	${docVO.dadosComplementares}
			</c:if>

			<c:if test="${not empty docVO.cossignatarios}">
				<div class="gt-sidebar-content" style="padding-top: 10px">
					<h3>Cossignatários</h3>
					<ul>
						<c:forEach var="cossig" items="${docVO.cossignatarios}">
							<li>${cossig.key.subscritor.nomePessoa}<c:if
									test="${cossig.value}">&nbsp;
					<a
										href="/sigaex/app/expediente/mov/excluir?id=${cossig.key.idMov}">Excluir</a>
								</c:if>
							</li>
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<c:if test="${not empty docVO.doc.perfis}">
				<div class="gt-sidebar-content" style="padding-top: 10px">
					<h3>Perfis</h3>
					<c:forEach var="perfil" items="${docVO.doc.perfis}">
						<p style="margin-bottom: 3px;">
							<b>${perfil.key.descPapel}:</b>
						</p>
						<ul>
							<c:forEach var="pessoaOuLota" items="${perfil.value}">
								<li><c:catch var="exception">${pessoaOuLota.nomePessoa}</c:catch>
									<c:if test="${not empty exception}">${pessoaOuLota.nomeLotacao}</c:if>
								</li>
							</c:forEach>
						</ul>
					</c:forEach>
				</div>
			</c:if>

			<div class="gt-sidebar-content" style="padding-top: 10px">
				<h3>Nível de Acesso</h3>
				<p>
					<b>${docVO.nmNivelAcesso}</b>
					<c:if test="${not empty docVO.listaDeAcessos}">
						<c:choose>
							<c:when test="${docVO.listaDeAcessos.size() eq 1}">
								<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
									<c:choose>
										<c:when test="${acesso eq 'PUBLICO'}">
										(Público)
									</c:when>
										<c:otherwise>
										(${acesso.sigla})
									</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<ul>
									<c:forEach var="acesso" items="${docVO.listaDeAcessos}" varStatus="loop">
										<li>${acesso.sigla}</li>
									</c:forEach>
								</ul>
							</c:otherwise>
						</c:choose>
					</c:if>
				</p>
			</div>
			<div class="gt-sidebar-content" id="gc"></div>
		</div>

	</div>
	</c:if>
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



<div class="gt-bd clearfix" id="svgRelacaoDocs" style="display: none; overflow-y: scroll;">
	<div class="gt-content clearfix">
		<div id="desc_editar_relacaoDocs">
			<div style="padding-bottom: 15px;" id="output2RelacaoDocs" oncontextmenu="return false;">
				<h4 id="naoCarregouBigRelacaoDocs">Carregando...</h4>
			</div>
		</div>
	</div>
	<p style="font-weight: bold">
		Clique sobre a imagem com o botão esquerdo para ampliar ou com o direito para reduzir.</p>
	<a href="javascript:void(0)" onclick="javascript: $('#svgRelacaoDocs').dialog('close');"
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

<c:if test="${f:resource('isWorkflowEnabled')}">
	<script type="text/javascript">
		var url = "/sigawf/app/doc?sigla=${docVO.mob.sigla}&ts=1${currentTimeMillis}";
		Siga.ajax(url, null, "GET", function(response){		
			var div = $(".wf_div:last"); 
			$(div).html(response);
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
		Siga.ajax(urlGc.substring(7), null, "GET", function(response){	
			$("#gc").html(response);
		});		
	</script>
</c:if>

<siga:rodape />