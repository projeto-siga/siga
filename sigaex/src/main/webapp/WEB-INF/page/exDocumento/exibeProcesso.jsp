<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExDocumento"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivoNumerado"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivo"%>
<%@page import="java.util.List"%>
<siga:pagina titulo="Documento" popup="${param.popup}" >

<c:if test="${not docVO.digital}">
	<script type="text/javascript">
		$("html").addClass("fisico");
		$("body").addClass("fisico");
	</script>
</c:if>

<c:if test="${mob.doc.podeReordenar()}"> 
	<style>
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
			
		.menu-ordenacao {
			text-align: center;
			height: auto;
			max-height: 0;		
			opacity: 0;		
			position: relative;		
			left: -999px;
			transition: left .3s, opacity .3s, max-height .5s;		
		}		
		
		.form {
			text-align: center;
		}		
		
		.checkbox-oculto {
			display: none;
		}
		
		#btnOrdenarDocumentos {
			background-color: transparent;
		}		
		
		#btnOrdenarDocumentos:disabled,
		#btnSalvarOrdenacao:disabled {
			cursor: not-allowed;
		}
		
		.container-tabela-lista-documentos {
			overflow: hidden;							
		}
		
		@media screen and (max-width: 575px) {
  			.container-tabela-lista-documentos {
    			overflow: auto;
    			max-height: 112px;
  			}
		}
	</style>
</c:if>

<script type="text/javascript">
	var iframeids = [ "maincntnt" ]
	var iframehide = "no"
	var getFFVersion = navigator.userAgent.substring(
			navigator.userAgent.indexOf("Firefox")).split("/")[1]
	var FFextraHeight = parseFloat(getFFVersion) >= 0.1 ? 3 : 0
	var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
	if (is_chrome) {
		FFextraHeight = 30;

	}
</script>
<script type="text/javascript" language="Javascript1.1">
	window.addEventListener('resize', function () {
	    redimensionar();
	});
	
	function removerBotoes() {
		if($('#radioPDFSemMarcas').hasClass('active') && '${siga_cliente}' == 'GOVSP' && window.parent.painel.document.getElementById("print") != null) {
			window.parent.painel.document.getElementById("print").remove();
			window.parent.painel.document.getElementById("download").remove();
		}
		if(window.parent.painel.document.getElementById("openFile") != null) {
			window.parent.painel.document.getElementById("openFile").remove();
		}
	}
	
	function redimensionar() {
		if("${f:resource('/sigaex.conversor.html.ext')}" == "br.gov.jfrj.itextpdf.MyPD4ML") {
			document.getElementById('painel').scrolling = "auto";
		} else {
			 if($('#radioHTML').hasClass('active') || document.getElementById('radioHTML').checked) {
			     if(window.parent.painel.document.getElementsByClassName("divDoc").length > 0) {
			        var divs = window.parent.painel.document.getElementsByClassName("divDoc");
			        
			        for(var i = 0; i < divs.length; i++) {
			        		window.parent.painel.document.getElementsByClassName("divDoc")[i].style.width=document.getElementById('painel').clientWidth - 10;
			        		window.parent.painel.document.getElementsByClassName("divDoc")[i].style.padding = "20px";
			        }   
		    		
			        return;
			    }
			    else {
			        setTimeout(function() {
			        	redimensionar();
			        }, 5000);
			    }
			}
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
		var ifrp = document.getElementById('paipainel');

		ifr.height = pageHeight() - 300;

		if (ifr && !window.opera) {
			ifr.style.display = "block";
			if (ifr.contentDocument && ifr.contentDocument.body.offsetHeight) //ns6 syntax
				ifr.height = ifr.contentDocument.body.offsetHeight
						+ FFextraHeight;
			else if (ifr.Document && ifr.Document.body.scrollHeight) //ie5+ syntax
				ifr.height = ifr.Document.body.scrollHeight;
		}
	}

	function telaCheia(b) {
        $('#sidebar').toggleClass('active');
        $('.wrapper').toggleClass('col-sm-3');
        $('.wrapper').toggleClass('d-none');
        
        if ($('#TelaCheia').hasClass('btn-secondary')) {
	 		b.classList.remove("btn-secondary");
	 		b.setAttribute("class", "ml-2 btn btn-primary btn-sm align-center");
	 		b.textContent = "Abrir Índice";
	 		var divDocRight = document.getElementById('right-col');
	 		divDocRight.setAttribute("class", "col-sm-12");
		} else {
	 		b.classList.remove("btn-primary");
	 		b.setAttribute("class", "ml-2 btn btn-secondary btn-sm align-center");
	 		b.textContent = "Tela cheia";
	 		var divDocRight = document.getElementById('right-col');
	 		divDocRight.setAttribute("class", "col-sm-9");
		}

		redimensionar();
		resize();
	}				
</script>

<!-- main content bootstrap -->
<div class="container-fluid content" id="page">
	<div class="row mt-3" id="inicio">
		<div class="col">
			<h2>
				<c:if test="${empty ocultarCodigo}">
					${docVO.sigla}
				</c:if>
				<a class="btn btn-secondary float-right ${hide_only_TRF2}" name="voltar" href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" accesskey="r">Volta<u>r</u></a>
			</h2>
		</div>
	</div>
	<div class="row mt-3 ${hide_only_GOVSP}">
		<div class="col">
			<c:forEach var="m" items="${docVO.mobs}" varStatus="loop">
				<c:if test="${((((mob.id == m.mob.id))))}">
					<h3 style="margin-bottom: 0px;">
						${m.getDescricaoCompletaEMarcadoresEmHtml(cadastrante,lotaTitular)}
						<c:if test="${docVO.digital and not empty m.tamanhoDeArquivo}">
							 - ${m.tamanhoDeArquivo}
						</c:if>
					</h3>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<div class="row">
		<div class="col">
			<c:choose>
				<c:when test="${doc.eletronico}">
					<c:set var="exibedoc" value="header_eletronico" />
				</c:when>
				<c:otherwise>
					<c:set var="exibedoc" value="header" />
				</c:otherwise>
			</c:choose>
			<siga:links inline="${true}">
				<div class="d-inline position-fixed fixed-bottom" style="left:auto">
					<div class="float-right mr-3 opacity-80">
						<p>
							<a class="btn btn-light btn-circle" href="#inicio"> 
								<i class="fas fa-chevron-up h6"></i>
							</a>
						</p>
						<p>
							<a class="btn btn-light btn-circle" href="#final"> 
								<i class="fas fa-chevron-down h6"></i>
							</a>
						</p>
					</div>
				</div>				
				<c:if test="${siga_cliente != 'GOVSP'}">
					<div class="mt-2">
						<siga:link icon="application_view_list" classe="once" title="Visualizar&nbsp;_Movimentações"
							url="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" atalho="${true}"
							test="${true}" /> <span class="pl-2"></span>
						<button type="button" class="link-btn btn btn-secondary btn-sm align-center" id="TelaCheia" data-toggle="button" aria-pressed="false" autocomplete="off"
							accesskey="t" onclick="javascript: telaCheia(this);">
							<u>T</u>ela Cheia
						</button>			
						<span class="pl-2"></span>			
						<div class="d-inline-block align-center mb-2 mt-2">
							<siga:link icon="wrench" title="Preferência:" test="${true}" url="" />
							<span class="pl-2"></span>			
							<span style="white-space: nowrap;">
							<input type="radio" id="radioHTML" name="formato" value="html" accesskey="h" checked="checked" onclick="exibir(htmlAtual,pdfAtual,'');">
								<u>H</u>TML&nbsp;
							</input>
							</span>
							<span class="pl-2"></span>			
							<span style="white-space: nowrap;">
							<input type="radio" id="radioPDF" name="formato" value="pdf" accesskey="p" onclick="exibir(htmlAtual,pdfAtual,'');">
								<u>P</u>DF -  <a id="pdflink" accesskey="a"> <u>a</u>brir</a>
							</input>
							</span>
							<span class="pl-2"></span>			
							<span style="white-space: nowrap;">
							<input type="radio" id="radioPDFSemMarcas" name="formato" accesskey="s" value="pdfsemmarcas" onclick="exibir(htmlAtual,pdfAtual,'semmarcas/');">
								PDF <u>s</u>em marcas - <a id="pdfsemmarcaslink" accesskey="b"> a<u>b</u>rir</a>
							</input>
							</span>
						</div>
					</div>
				</c:if>
	
			</siga:links>
		</div>
	</div>
	<div class="row mt-3">
		<c:set var="arqsNum" value="${mob.arquivosNumerados}" />
		<c:set var="paginacao" value="${not empty arqsNum[0].paginaInicial}" />
		<div class="wrapper col-sm-4 col-lg-3">
			<div id="sidebar" class="w-100">
				<div class="card-sidebar card bg-light mb-3" id="documentosDossie">
					<div class="text-size-6 card-header">
						<span class="titulo-docs">
							<fmt:message key='documento.dossie'/>  /  <fmt:message key='usuario.lotacao'/>
							<c:if test="${mob.doc.podeReordenar()}">													
								<button type="button" class="btn" id="btnOrdenarDocumentos" data-toggle="tooltip" data-placement="top" title="Reordenar itens" ${arqsNum.size() == 1 ? 'disabled' : ''}>
									<i class="fas fa-sort"></i>
								</button>									
								<c:if test="${mob.doc.podeReordenar() && podeExibirReordenacao && mob.doc.temOrdenacao()}">								
									<br />*reordenados temporariamente
								</c:if>
							</c:if>
						</span>		
						<c:if test="${mob.doc.podeReordenar()}">				
							<div class="menu-ordenacao"">
								Clique e arraste os itens tracejados para reordená-los<br />							
								<form action="${pageContext.request.contextPath}/app/expediente/doc/reordenar" id="formReordenarDocs" class="form" method="POST">									
									<input type="hidden" name="idDocumentos" id="inputHiddenIdDocs" />													
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
					</div>
					<div class="card-body pl-1 pr-1 pt-0 pb-0  container-tabela-lista-documentos">
						<table class="text-size-6 table table-hover table-sm table-striped m-0 mov tabela-documentos">
							<tbody id="${mob.doc.podeReordenar() ? 'sortable' : ''}">
								<c:forEach var="arqNumerado" items="${arqsNum}">
									<tr>										
										<td style="display: none;">
											${arqNumerado.arquivo.idDoc}	
										</td>
										<td>
											<a target="_blank" title="${fn:substring(tooltipResumo,0,fn:length(tooltipResumo)-4)}" href="/sigaex/app/arquivo/exibir?arquivo=${arqNumerado.referenciaPDF}">
												<img src="/siga/css/famfamfam/icons/page_white_acrobat.png">
											</a>
										</td>
										<td style="padding-left: ${arqNumerado.nivel * 5 + 5}pt">
											<c:if test="${siga_cliente != 'GOVSP' && !empty arqNumerado.arquivo.resumo}">
												<c:forEach var="itemResumo" items="${arqNumerado.arquivo.resumo}">
													<c:set var="tooltipResumo" value="${tooltipResumo}${itemResumo.key}:${itemResumo.value}&#13" />
												</c:forEach>
											</c:if> 
											<a title="${fn:substring(tooltipResumo,0,fn:length(tooltipResumo)-4)}" 
												href="javascript:exibir('${arqNumerado.referenciaHtml}','${arqNumerado.referenciaPDF}','')">
												<c:choose>
													<c:when test="${siga_cliente == 'GOVSP'}">
														${arqNumerado.nomeOuDescricaoComMovimentacao}
													</c:when>
													<c:otherwise>
														${arqNumerado.nomeOuDescricao}
													</c:otherwise>
												</c:choose>
											</a>
											<c:set var="tooltipResumo" value="" />
										</td>
										<td align="center">${arqNumerado.arquivo.lotacao.sigla}</td>
										<c:if test="${paginacao}">
											<td align="center">${arqNumerado.paginaInicial}</td>
										</c:if>
									</tr>
									<c:if test="${siga_cliente != 'GOVSP' && !empty arqNumerado.arquivo.resumo}">
										<c:set var="possuiResumo" value="sim" />
									</c:if>
									<c:set var="arquivo" value="${arqNumerado}" scope="request" />
									<c:if test="${arqNumerado.arquivo.class.simpleName == 'ExMovimentacao'}">
										<c:if test="${mov.exTipoMovimentacao == 'ANEXACAO' && !empty mov.descrMov}">
											<c:set var="possuiResumo" value="sim" />
										</c:if>
									</c:if>
				
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<td>
										<a target="_blank" href="/sigaex/app/arquivo/exibir?arquivo=${arqsNum[0].referenciaPDFCompletoDocPrincipal}">
											<img src="/siga/css/famfamfam/icons/page_white_acrobat.png">
										</a>
									</td>
									<td style="padding-left: 5pt;">
										<a class="js-siga-info-doc-completo" href="javascript:exibir('${arqsNum[0].referenciaHtmlCompletoDocPrincipal}','${arqsNum[0].referenciaPDFCompletoDocPrincipal}','')">COMPLETO</a>
									</td>
									<c:if test="${siga_cliente != 'GOVSP' && paginacao}">
										<td align="center" style="padding-left: 5pt;"></td>										
										<td align="center" style="padding-left: 5pt;">
											${arqsNum[fn:length(arqsNum)-1].paginaFinal}
										</td>										
									</c:if>	
								</tr>							
								<c:if test="${!empty possuiResumo}">
									<tr>
										<td></td>
										<td colspan="2" style="padding-left: 5pt;">
											<a href="javascript:exibirNoIFrame('${pageContext.request.contextPath}/app/expediente/doc/exibirResumoProcesso?sigla=${mob.sigla}')">RESUMO</a>
										</td>
										<c:if test="${paginacao}">
											<td></td>
										</c:if>
									</tr>
								</c:if>
								
								<c:if test="${podeExibirTodosOsVolumes}">
								<tr>
									<td>
										<a target="_blank" href="/sigaex/app/arquivo/exibir?arquivo=${arqsNum[0].referenciaPDFCompletoDocPrincipalVolumes}">
											<img src="/siga/css/famfamfam/icons/page_white_acrobat.png">
										</a>
									</td>
									<td style="padding-left: 5pt;">
										<a class="js-siga-info-doc-completo" href="javascript:exibir('${arqsNum[0].referenciaHtmlCompletoDocPrincipalVolumes}','${arqsNum[0].referenciaPDFCompletoDocPrincipalVolumes}','')">TODOS OS VOLUMES</a>
									</td>
									<c:if test="${siga_cliente != 'GOVSP' && paginacao}">
										<td align="center" style="padding-left: 5pt;"></td>										
										<td align="center" style="padding-left: 5pt;">
										</td>										
									</c:if>	
								</tr>							
								</c:if>
							</tfoot>
						</table>
						<c:if test="${mob.doc.podeReordenar() && podeExibirReordenacao && mob.doc.temOrdenacao()}">						
							<div class="menu-retornar-para-original">
								<hr>
								<form action="${pageContext.request.contextPath}/app/expediente/doc/reordenar" id="formVoltarDocsParaOrdemOriginal" class="form" method="POST">
									<input type="hidden" name="sigla" value="${sigla}" />																
									<input type="checkbox" name="isVoltarParaOrdemOriginal" id="isVoltarParaOrdemOriginal" class="checkbox-oculto" checked disbled />																										
									<button type="submit" class="btn btn-warning" id="btnResetarOrdemOriginal">
										<i class="fas fa-undo-alt"></i> Retornar para ordem original									
									</button>																						
								</form>
							</div>	
						</c:if>					
					</div>
				</div>
			</div>
		</div>
		<div id="right-col" class="col-sm-8 col-lg-9">
			<c:if test="${siga_cliente == 'GOVSP'}">
				<div id="linhaBtn" class="mb-2">						
					<div class="input-group d-inline mb-2">						
						<div id="radioBtn" class="btn-group">
							<a class="btn btn-primary btn-sm active" data-toggle="formato" data-title="html" id="radioHTML" name="formato" value="html" accesskey="h" onclick="toggleBotaoHtmlPdf($(this)); exibir(htmlAtual,pdfAtual,'');">
								<u>H</u>TML
							</a>
							<a class="btn btn-primary btn-sm notActive" data-toggle="formato" data-title="pdf" id="radioPDF" name="formato" value="pdf" accesskey="p" onclick="toggleBotaoHtmlPdf($(this)); exibir(htmlAtual,pdfAtual,'');">
	<!--  									<a id="pdflink" accesskey="a"> -->
										<u>P</u>DF
	<!-- 									</a> -->
							</a>
							<a class="btn btn-primary btn-sm notActive" data-toggle="formato" data-title="pdfsemmarcas" id="radioPDFSemMarcas" name="pdfsemmarcas" value="pdfsemmarcas" accesskey="p" onclick="toggleBotaoHtmlPdf($(this)); exibir(htmlAtual,pdfAtual,'semmarcas/');">
										PDF Sem Marcas
							</a>
						</div>
						<a class="btn-btn-primary btn-sm d-none" id="pdflink" accesskey="a"><u>a</u>brir PDF</a>
						<a class="btn-btn-primary btn-sm d-none" id="pdfsemmarcaslink" accesskey="b">a<u>b</u>rir PDF</a>
						<input type="hidden" name="formato" id="radio" value="html">
					</div>
					<button type="button" class="btn btn-secondary btn-sm" id="TelaCheia" data-toggle="button" aria-pressed="false" autocomplete="off"
						onclick="javascript: telaCheia(this);">
						<u>T</u>ela Cheia
					</button>
				</div>
			</c:if>
			<div id="paipainel" style="margin: 0px; padding: 0px; border: 0px; clear: both;overflow:hidden;">
				<iframe style="visibility: visible; margin: 0px; padding: 0px; min-height: 20em;" name="painel" id="painel" src="" align="right" width="100%" onload="$(document).ready(function () {resize();});redimensionar();removerBotoes();verificarMensagem(this.src)" frameborder="0" scrolling="no"></iframe>
			</div>
		</div>
	</div>
	<div id="final"></div>
</div>
</siga:pagina>
<c:if test="${mob.doc.podeReordenar()}">
	<script src="/siga/javascript/jquery-ui-1.12.1/custom/sortable/jquery-ui-1.12.1.min.js"></script>
	<script src="/siga/javascript/jqueryui-touch-punch-0.2.3/jquery.ui.touch-punch-0.2.3.min.js"></script>
</c:if>
<script src="/siga/bootstrap/js/bootstrap.min.js"></script>
<c:if test="${mob.doc.podeReordenar() && podeExibirReordenacao && mob.doc.temOrdenacao()}">
	<script>	
		$(document).ready(function() {
			//se exibindo documentos reordenados, não permite visualização PDF						
			$('#radioPDF').attr('data-toggle', 'tooltip').attr('data-placement', 'top').attr('title', 'Indisponível enquanto documento estiver reordenado').removeAttr('onclick').css({'cursor':'not-allowed', 'color':'rgba(0, 0, 0, 0.3)', 'border':'1px solid rgba(0, 0, 0, 0.3)'});		
			$('#radioPDFSemMarcas').attr('data-toggle', 'tooltip').attr('data-placement', 'top').attr('title', 'Indisponível enquanto documento estiver reordenado').removeAttr('onclick').css({'cursor':'not-allowed', 'color':'rgba(0, 0, 0, 0.3)', 'border':'1px solid rgba(0, 0, 0, 0.3)'});
		});
	</script>
</c:if>
<script>
	$(function () {	
		analisarAlturaListaDocumentos();
		
		$('[data-toggle="tooltip"]').tooltip();
	});
	
	function analisarAlturaListaDocumentos() {
		var containerListaDeDocumentos = $('#documentosDossie');
		var alturaContainerListaDeDocumentos = containerListaDeDocumentos.height();
		var distanciaTopo = containerListaDeDocumentos.offset().top;
		var alturaJanela = window.screen.availHeight;
		var diferencaNecessaria = 102;		
		var alturaLimite = alturaJanela - distanciaTopo - diferencaNecessaria;
				
		if (alturaContainerListaDeDocumentos > alturaLimite) {					
			aplicarMaxHeight(containerListaDeDocumentos, alturaLimite);
			aplicarScroll(containerListaDeDocumentos.find('.container-tabela-lista-documentos'));
		}		
	}
	
	function aplicarMaxHeight(elemento, altura) {
		elemento.css('max-height', altura + 'px');
	}
	
	function aplicarScroll (elemento) {
		elemento.css('overflow', 'auto');
	}	
</script>
<c:if test="${siga_cliente == 'GOVSP' && paginacao}">
	<script>
		$(function() {			
			var quantidadePaginas = '${arqsNum[fn:length(arqsNum)-1].paginaFinal}';
			
			if (quantidadePaginas && quantidadePaginas > 0) {
				var linkDocCompleto = $('.js-siga-info-doc-completo');
				var title = quantidadePaginas + ' página' + (quantidadePaginas > 1 ? 's' : '');
				
				linkDocCompleto.attr('data-toggle', 'tooltip').attr('data-placement', 'right').attr('title', title);
				linkDocCompleto.tooltip();
				linkDocCompleto.css({'padding': '5px 5px 5px 0'});														
			}
		});
	</script>
</c:if>	
<script>
	var htmlAtual = '${arqsNum[0].referenciaHtmlCompletoDocPrincipal}';
	var pdfAtual = '${arqsNum[0].referenciaPDFCompletoDocPrincipal}';	
	var path = '/sigaex/app/arquivo/exibir?idVisualizacao=${idVisualizacao}';
	
	if ('${mob.doc.podeReordenar()}' === 'true' && '${podeExibirReordenacao}' === 'true') path += '&exibirReordenacao=true';			
	path += '&arquivo=';			

	function fixlinks(refHTML, refPDF) {
		
		if ('${siga_cliente}' == 'GOVSP') {
			document.getElementById('pdflink').href = path + refPDF + '&sigla=${sigla}';
			
			if ($('#radioPDFSemMarcas').hasClass('active')) {
				document.getElementById('pdfsemmarcaslink').href = path + refPDF
					+ "&semmarcas=1";
			}
		} else {
			document.getElementById('pdflink').href = path + refPDF;
		}
		
		if (document.getElementById('radioPDFSemMarcas') != null) {
			document.getElementById('pdfsemmarcaslink').href = path + refPDF
					+ "&semmarcas=1";
		}
	}

	//Nato: convem remover as outras maneiras de chamar o resize() e deixar apenas o jquery.
	function exibir(refHTML, refPDF, semMarcas) {
		var ifr = document.getElementById('painel');
		var ifrp = document.getElementById('paipainel');
		if('${excedeuTamanhoMax}' === 'true' && !($('#radioHTML').hasClass('active') || document.getElementById('radioHTML').checked)) {
			sigaModal.alerta("Agregação de documentos excedeu o tamanho máximo permitido.");
			return;
		}
		if (ifr.addEventListener)
			ifr.removeEventListener("load", resize, false);
		else if (ifr.attachEvent)
			ifr.detachEvent("onload", resize); // Bug fix line

			if ('${siga_cliente}' == 'GOVSP') {
			// Para GOVSP com link buttons

			var refSiglaDocPrincipal = '&sigla=${sigla}';
			
			if ($('#radioHTML').hasClass('active') && refHTML != '') {
				$('#pdflink').addClass('d-none');
				$('#pdfsemmarcaslink').addClass('d-none');
				ifr.src = path + refHTML + refSiglaDocPrincipal;
				ifrp.style.border = "0px solid black";
				ifrp.style.borderBottom = "0px solid black";
				if (ifr.addEventListener)
					ifr.addEventListener("load", resize, false);
				else if (ifr.attachEvent)
					ifr.attachEvent("onload", resize);
			} else {
				if ($('#radioPDFSemMarcas').hasClass('active')) {
					$('#pdfsemmarcaslink').removeClass('d-none');
					$('#pdflink').addClass('d-none');
					ifr.src = path + refPDF + "&semmarcas=1";
				} else {
					$('#pdflink').removeClass('d-none');
					$('#pdfsemmarcaslink').addClass('d-none');
					ifr.src = path + refPDF + refSiglaDocPrincipal;
				}
				
				if(!refPDF.includes("completo=1")) {
					var url = ifr.src;
					ifr.src = montarUrlDocPDF(ifr.src, "${f:resource('/sigaex.pdf.visualizador')}");
				}
				
				ifrp.style.border = "1px solid black";
				ifr.height = pageHeight() - 300;
			}
			
		} else {
			// Para TRF2 com radio buttons
			if (document.getElementById('radioHTML').checked && refHTML != '') {
				ifr.src = path + refHTML;
				ifrp.style.border = "0px solid black";
				ifrp.style.borderBottom = "0px solid black";
				if (ifr.addEventListener)
					ifr.addEventListener("load", resize, false);
				else if (ifr.attachEvent)
					ifr.attachEvent("onload", resize);
			} else {
				if (document.getElementById('radioPDFSemMarcas').checked)
					ifr.src = path + refPDF + "&semmarcas=1"
				else
					ifr.src = path + refPDF;
				
				if(!refPDF.includes("completo=1")) {
					var url = ifr.src;
					ifr.src = montarUrlDocPDF(ifr.src, "${f:resource('/sigaex.pdf.visualizador')}");
				}
				ifrp.style.border = "0px solid black";
				ifr.height = pageHeight() - 300;
			}
		}
			
		htmlAtual = refHTML;
		pdfAtual = refPDF;

		fixlinks(refHTML, refPDF);
		$('#painel').load(function() {
			setTimeout(function() {
				resize();
			}, 100);
		});
	}
	
	function verificarMensagem(url) {
		if(url.includes("file=")) {
			if((window.parent.painel.document.getElementById('errorMessage') != null &&
					window.parent.painel.document.getElementById('errorMessage').textContent != "" )) {
				document.getElementById('painel').src = decodeURIComponent(url.substring(url.indexOf("file=")+5));
			} else {
				if(window.parent.painel.document.getElementsByClassName("textLayer").length == 0) {
					setTimeout(function() {
						verificarMensagem(url);
					}, 100);
				}
			}
		}	
	}

	exibir(window.htmlAtual, window.pdfAtual);
	fixlinks(window.htmlAtual, window.pdfAtual);
	resize();

	function exibirNoIFrame(url) {
		var ifr = document.getElementById('painel');
		var ifrp = document.getElementById('paipainel');

		if (ifr.addEventListener)
			ifr.removeEventListener("load", resize, false);
		else if (ifr.attachEvent)
			ifr.detachEvent("onload", resize);
		if (document.getElementById('radioHTML').checked) {
			ifrp.style.border = "0px solid black";
			ifrp.style.borderBottom = "0px solid black";
			if (ifr.addEventListener)
				ifr.addEventListener("load", resize, false);
			else if (ifr.attachEvent)
				ifr.attachEvent("onload", resize);
		} else {
			ifr.height = "0";
			ifrp.style.border = "0px solid black";
			ifr.height = pageHeight() - 150;
		}
		ifr.src = url;
	}
	
	function toggleBotaoHtmlPdf(btn) {		
	    var sel = btn.data('title');
	    var tog = btn.data('toggle');
	    $('#'+tog).prop('value', sel);
	    
	    $('a[data-toggle="'+tog+'"]').not('[data-title="'+sel+'"]').removeClass('active').addClass('notActive');
	    $('a[data-toggle="'+tog+'"][data-title="'+sel+'"]').removeClass('notActive').addClass('active');
	}
	
</script>
<script>
	$('#radioBtnXXXX a').on('click', function(){
	    var sel = $(this).data('title');
	    var tog = $(this).data('toggle');
	    $('#'+tog).prop('value', sel);
	    
	    $('a[data-toggle="'+tog+'"]').not('[data-title="'+sel+'"]').removeClass('active').addClass('notActive');
	    $('a[data-toggle="'+tog+'"][data-title="'+sel+'"]').removeClass('notActive').addClass('active');
	})
</script>
<c:if test="${mob.doc.podeReordenar()}"> 
	<script src="/siga/javascript/documento.reordenar-doc.js"></script>
</c:if>
