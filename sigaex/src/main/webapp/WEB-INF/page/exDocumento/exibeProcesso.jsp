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
	 		b.setAttribute("class", "mt-3 ml-2 btn btn-primary btn-sm align-center");
	 		b.textContent = "Abrir Índice";
	 		var divDocRight = document.getElementById('right-col');
	 		divDocRight.setAttribute("class", "col-sm-12");
		} else {
	 		b.classList.remove("btn-primary");
	 		b.setAttribute("class", "mt-3 ml-2 btn btn-secondary btn-sm align-center");
	 		b.textContent = "Tela cheia";
	 		var divDocRight = document.getElementById('right-col');
	 		divDocRight.setAttribute("class", "col-sm-9");
		}
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
				<div class="d-inline position-fixed fixed-bottom">
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
					<div>
						<siga:link icon="application_view_list" classe="mt-3 once" title="Visualizar&nbsp;_Movimentações"
							url="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" atalho="${true}"
							test="${true}" />
						<button type="button" class="mt-3 ml-2 btn btn-secondary btn-sm align-center" id="TelaCheia" data-toggle="button" aria-pressed="false" autocomplete="off"
							accesskey="t" onclick="javascript: telaCheia(this);">
							<u>T</u>ela Cheia
						</button>
						<div class="d-inline-block col-sm-6 align-bottom pb-1 mt-3 mr-2">
							<siga:link icon="wrench" title="Preferência:" test="${true}" url="" />
							<input type="radio" id="radioHTML" name="formato" value="html" accesskey="h" checked="checked" onclick="exibir(htmlAtual,pdfAtual,'');">
								&nbsp;<u>H</u>TML&nbsp;
							</input>
							<input type="radio" id="radioPDF" name="formato" value="pdf" accesskey="p" onclick="exibir(htmlAtual,pdfAtual,'');">
								&nbsp;<u>P</u>DF -  <a id="pdflink" accesskey="a"> <u>a</u>brir</a>
							</input>
							<input class="ml-2" type="radio" id="radioPDFSemMarcas" name="formato" accesskey="s" value="pdfsemmarcas" onclick="exibir(htmlAtual,pdfAtual,'semmarcas/');">
								&nbsp;PDF <u>s</u>em marcas - <a id="pdfsemmarcaslink" accesskey="b"> a<u>b</u>rir</a>
							</input>
						</div>
					</div>
				</c:if>
	
			</siga:links>
		</div>
	</div>
	<div class="row mt-3">
		<c:set var="arqsNum" value="${mob.arquivosNumerados}" />
		<c:set var="paginacao" value="${not empty arqsNum[0].paginaInicial}" />
		<div class="wrapper col-sm-3 float-left" >
			<nav id="sidebar">
				<div class="card-sidebar card bg-light mb-3" id="documentosDossie">
					<div class="card-header">
						<fmt:message key='documento.dossie'/>  /  <fmt:message key='usuario.lotacao'/>
					</div>
					<div class="card-body p-1">
						<table class="table table-hover table-striped mov">
							<tbody>
								<c:forEach var="arqNumerado" items="${arqsNum}">
									<tr>
										<td>
											<a target="_blank" title="${fn:substring(tooltipResumo,0,fn:length(tooltipResumo)-4)}" href="/sigaex/app/arquivo/exibir?arquivo=${arqNumerado.referenciaPDF}">
												<img src="/siga/css/famfamfam/icons/page_white_acrobat.png">
											</a>
										</td>
										<td style="padding-left: ${arqNumerado.nivel * 5 + 5}pt">
											<c:if test="${!empty arqNumerado.arquivo.resumo}">
												<c:forEach var="itemResumo" items="${arqNumerado.arquivo.resumo}">
													<c:set var="tooltipResumo" value="${tooltipResumo}${itemResumo.key}:${itemResumo.value}&#13" />
												</c:forEach>
											</c:if> 
											<a title="${fn:substring(tooltipResumo,0,fn:length(tooltipResumo)-4)}" href="javascript:exibir('${arqNumerado.referenciaHtml}','${arqNumerado.referenciaPDF}','')">${arqNumerado.nomeOuDescricao}</a>
											<c:set var="tooltipResumo" value="" />
										</td>
										<td align="center">${arqNumerado.arquivo.lotacao.sigla}</td>
										<c:if test="${paginacao}">
											<td align="center">${arqNumerado.paginaInicial}</td>
										</c:if>
									</tr>
									<c:if test="${!empty arqNumerado.arquivo.resumo}">
										<c:set var="possuiResumo" value="sim" />
									</c:if>
									<c:set var="arquivo" value="${arqNumerado}" scope="request" />
									<c:if test="${arqNumerado.arquivo.class.simpleName == 'ExMovimentacao'}">
										<c:if test="${mov.exTipoMovimentacao.id == 2 && !empty mov.descrMov}">
											<c:set var="possuiResumo" value="sim" />
										</c:if>
									</c:if>
				
								</c:forEach>
								<tr>
									<td>
										<a target="_blank" href="/sigaex/app/arquivo/exibir?arquivo=${arqsNum[0].referenciaPDFCompleto}">
											<img src="/siga/css/famfamfam/icons/page_white_acrobat.png">
										</a>
									</td>
									<td style="padding-left: 5pt;">
										<a href="javascript:exibir('${arqsNum[0].referenciaHtmlCompleto}','${arqsNum[0].referenciaPDFCompleto}','')">COMPLETO</a>
									</td>
									<td align="center" style="padding-left: 5pt;"></td>
									<c:if test="${paginacao}">
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
							</tbody>
						</table>
					</div>
				</div>
			</nav>
		</div>
		<div id="right-col" class="col-sm-9 float-right">
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
						</div>
						<a class="btn-btn-primary btn-sm d-none" id="pdflink" accesskey="a"><u>a</u>brir PDF</a>
						<input type="hidden" name="formato" id="radio" value="html">
					</div>
					<button type="button" class="btn btn-secondary btn-sm" id="TelaCheia" data-toggle="button" aria-pressed="false" autocomplete="off"
						onclick="javascript: telaCheia(this);">
						<u>T</u>ela Cheia
					</button>
				</div>
			</c:if>
			<div id="paipainel" style="margin: 0px; padding: 0px; border: 0px; clear: both;">
				<iframe style="visibility: visible; margin: 0px; padding: 0px;" name="painel" id="painel" src="" align="right" width="100%" onload="$(document).ready(function () {resize();});" frameborder="0" scrolling="auto"></iframe>
			</div>
		</div>
	</div>
	<div id="final"></div>
</div>
</siga:pagina>
<script src="/siga/bootstrap/js/bootstrap.min.js"></script>
<script>
	var path = '/sigaex/app/arquivo/exibir?idVisualizacao=${idVisualizacao}&arquivo=';
	var htmlAtual = '${arqsNum[0].referenciaHtmlCompleto}';
	var pdfAtual = '${arqsNum[0].referenciaPDFCompleto}';

	function fixlinks(refHTML, refPDF) {
		document.getElementById('pdflink').href = path + refPDF;
		if (document.getElementById('radioPDFSemMarcas') != null) {
			document.getElementById('pdfsemmarcaslink').href = path + refPDF
					+ "&semmarcas=1";
		}
	}

	//Nato: convem remover as outras maneiras de chamar o resize() e deixar apenas o jquery.
	function exibir(refHTML, refPDF, semMarcas) {
		var ifr = document.getElementById('painel');
		var ifrp = document.getElementById('paipainel');

		if (ifr.addEventListener)
			ifr.removeEventListener("load", resize, false);
		else if (ifr.attachEvent)
			ifr.detachEvent("onload", resize); // Bug fix line

		if (document.getElementById('radioPDFSemMarcas') == null) {
			// Para GOVSP com link buttons

			if ($('#radioHTML').hasClass('active') && refHTML != '') {
				$('#pdflink').addClass('d-none');
				ifr.src = path + refHTML;
				ifrp.style.border = "0px solid black";
				ifrp.style.borderBottom = "0px solid black";
				if (ifr.addEventListener)
					ifr.addEventListener("load", resize, false);
				else if (ifr.attachEvent)
					ifr.attachEvent("onload", resize);
			} else {
				$('#pdflink').removeClass('d-none');
				ifr.src = path + refPDF;
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
				ifrp.style.border = "1px solid black";
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
