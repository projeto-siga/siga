<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExDocumento"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivoNumerado"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivo"%>
<%@page import="java.util.List"%>
<siga:cabecalho titulo="Documento" popup="${param.popup}" onLoad="load();"/>

<script type="text/javascript">
	//Input the IDs of the IFRAMES you wish to dynamically resize to match its content height:
	//Separate each ID with a comma. Examples: ["myframe1", "myframe2"] or ["myframe"] or [] for none:
	var iframeids = [ "maincntnt" ]

	//Should script hide iframe from browsers that don't support this script (non IE5+/NS6+ browsers. Recommended):
	var iframehide = "no"

	var getFFVersion = navigator.userAgent.substring(
			navigator.userAgent.indexOf("Firefox")).split("/")[1]
	var FFextraHeight = parseFloat(getFFVersion) >= 0.1 ? 3 : 0
			
	var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;

	if (is_chrome) {
		FFextraHeight = 30;
		
	}
			
	//extra height in px to add to iframe in FireFox 1.0+ browsers
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
		if (ifr && !window.opera) {
			ifr.style.display = "block";
			if (ifr.contentDocument && ifr.contentDocument.body.offsetHeight) //ns6 syntax
				ifr.height = ifr.contentDocument.body.offsetHeight
						+ FFextraHeight;
			else if (ifr.Document && ifr.Document.body.scrollHeight) //ie5+ syntax
				ifr.height = ifr.Document.body.scrollHeight;
		}

		//	parent.document.getElementById('painel').height = "0";
		//	document.getElementById('painel').height = document.getElementById('painel').document.height;
		//	if(navigator.appName!='Microsoft Internet Explorer'){
		//		parent.document.getElementById('painel').height =  painel.document.height + 5;
		//	}else{
		//		parent.document.getElementById('painel').style.height = painel.document.body.scrollHeight + 5;
		//	}
	}
</script>

<script type="text/javascript" language="Javascript1.1">
	function load() {
		document.location.href = "#final";
	}
</script>

<c:choose>
	<c:when test="${doc.eletronico}">
		<c:set var="exibedoc" value="header_eletronico" />
	</c:when>
	<c:otherwise>
		<c:set var="exibedoc" value="header" />
	</c:otherwise>
</c:choose>

<siga:links>
	<ww:url id="url" action="exibir" namespace="/expediente/doc">
		<ww:param name="sigla" value="%{sigla}" />
	</ww:url>
	<siga:link title="Visualizar&nbsp;Movimentações" url="${url}"
		test="${true}" />

	<siga:link title="Preferência:" test="${true}" />
	<input type="radio" id="radioHTML" name="formato" value="html"
		checked="checked" onclick="exibir(htmlAtual,pdfAtual,'');">HTML</input>

	<input type="radio" id="radioPDF" name="formato" value="pdf"
		" onclick="exibir(htmlAtual,pdfAtual,'');">PDF</input>

	<input type="radio" id="radioPDFSemMarcas" name="formato"
		value="pdfsemmarcas"
		" onclick="exibir(htmlAtual,pdfAtual,'semmarcas/');">PDF sem marcas</input>
</siga:links>

<table width="100%" border="0">
	<tr>
		<td valign="top" width="25%">
			<table class="list" width="100%">
				<COL width="70%" />
				<COL width="15%" />
				<COL width="15%" />

				<c:set var="arqsNum" value="${mob.arquivosNumerados}" />

				<tr class="${exibedoc}">
					<td align="center">Documentos&nbsp;do&nbsp;Dossiê</td>
					<td align="center">Lotação</td>
					<td align="center">Página</td>
				</tr>

				<c:forEach var="arqNumerado" items="${arqsNum}">
					<tr>
						<td style="padding-left: ${arqNumerado.nivel * 6}pt"><c:if
								test="${!empty arqNumerado.arquivo.resumo}">
								<!-- <ul style="font-size: 9;font-style: italic;color: gray;list-style: disc;margin: 0px;margin-left: 25px">
						   	-->
								<c:forEach var="itemResumo"
									items="${arqNumerado.arquivo.resumo}">
									<!-- <li>${itemResumo.key}:${itemResumo.value}  -->
									<c:set var="tooltipResumo"
										value="${tooltipResumo}${itemResumo.key}:${itemResumo.value}&#13" />
								</c:forEach>
								<!-- </ul>  -->
							</c:if> <a
							title="${fn:substring(tooltipResumo,0,fn:length(tooltipResumo)-4)}"
							href="javascript:exibir('${arqNumerado.referenciaHtml}','${arqNumerado.referenciaPDF}','')">${arqNumerado.nome}</a>
							<c:set var="tooltipResumo" value="" /></td>
						<td align="center">${arqNumerado.arquivo.lotacao.sigla}</td>
						<td align="center">${arqNumerado.paginaInicial}</td>
					</tr>
					<c:if test="${!empty arqNumerado.arquivo.resumo}">
						<c:set var="possuiResumo" value="sim" />
					</c:if>


					<c:set var="arquivo" value="${arqNumerado}" scope="request" />
					<%
				ExArquivo arq = ((ExArquivoNumerado) request.getAttribute("arquivo")).getArquivo();
			
				if (arq instanceof ExMovimentacao) {
					request.setAttribute("mov", (ExMovimentacao) arq);
					%>
					<c:if
						test="${mov.exTipoMovimentacao.id == 2 && !empty mov.descrMov}">
						<c:set var="possuiResumo" value="sim" />
					</c:if>
					<%
				}
				%>

				</c:forEach>
				<tr>
					<td style="background-color: silver">
						<!-- 
					<ww:url
						id="urlProcessoHTML" action="exibirProcessoHTML"
						namespace="/expediente/doc">
						<ww:param name="id">${doc.idDoc}</ww:param>
					</ww:url>
					<input id="enderecoHTMLCompleto${doc.idDoc}" type="hidden" 
						value="completo/${doc.codigo}.html" />
					<input id="enderecoPDFCompleto${doc.idDoc}" type="hidden"
						value="completo/${doc.codigo}.pdf" />
					<a 	href="javascript:exibirNoFrame(${doc.idDoc},'S')">COMPLETO</a>  
				 --> <a
						href="javascript:exibir('completo/${arqsNum[0].referenciaHtml}','completo/${arqsNum[0].referenciaPDF}','')">COMPLETO</a>
					</td>
					<td align="center" style="background-color: silver"></td>
					<td align="center" style="background-color: silver">
						${arqsNum[fn:length(arqsNum)-1].paginaFinal}</td>
				</tr>

				<c:if test="${!empty possuiResumo}">
					<tr>
						<ww:url id="url" action="exibirResumoProcesso"
							namespace="/expediente/doc">
							<ww:param name="sigla">${mob.sigla}</ww:param>
						</ww:url>
						<td colspan="3" style="background-color: silver"><a
							href="javascript:exibirNoIFrame('${url}')">RESUMO</a></td>
					</tr>
				</c:if>

			</table></td>

		<td valign="top" id="paipainel"
			style="padding: 0px; border: 0px solid black; border-top: 0px;"
			width="75%">
			<table width="100%" border="0">
				<tr>
					<td valign="top" width="100%">
						<a name="inicio" href="#final">Ir para o Final&nbsp;&darr;</a>
					</td>
				</tr>
				<tr>
					<td valign="top" width="100%">
						<iframe
							style="visibility: visible; margin: 0px; padding: 0px;"
							name="painel" id="painel" src="" align="right" width="100%"
							onload="resize();" frameborder="0" scrolling="auto"></iframe>
					</td>
				</tr>
				<tr>
					<td valign="top" width="100%">
						<a name="final" href="#inicio">Ir para o Topo&nbsp;&uarr;</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<%--
<c:forEach var="item" items="${doc.form}">
	${item}<br/>
</c:forEach>
--%>


<%--
<!--  tabela do rodapé -->
<c:choose>
	<c:when test='${param.popup!="true"}'>
		<c:import context="/siga" url="/paginas/rodape.jsp" />
	</c:when>
	<c:otherwise>
		<c:import context="/siga" url="/paginas/rodape_popup.jsp" />
	</c:otherwise>
</c:choose>
--%>

<siga:rodape />


<!-- Página Inicial -->
<script>
	var htmlAtual = 'completo/${arqsNum[0].referenciaHtml}';
	var pdfAtual = 'completo/${arqsNum[0].referenciaPDF}';

	function exibir(refHTML, refPDF, semMarcas) {
		var ifr = document.getElementById('painel');
		var ifrp = document.getElementById('paipainel');

		if (ifr.addEventListener)
			ifr.removeEventListener("load", resize, false);
		else if (ifr.attachEvent)
			ifr.detachEvent("onload", resize); // Bug fix line
		if (document.getElementById('radioHTML').checked && refHTML != '') {
			ifr.src = "./" + refHTML;
			ifrp.style.border = "0px solid black";
			ifrp.style.borderBottom = "0px solid black";
			if (ifr.addEventListener)
				ifr.addEventListener("load", resize, false);
			else if (ifr.attachEvent)
				ifr.attachEvent("onload", resize);
		} else {
			ifr.src = "./" + semMarcas + refPDF;
			ifrp.style.border = "1px solid black";
			ifr.height = pageHeight() - 150;
		}

		htmlAtual = refHTML;
		pdfAtual = refPDF;
	}

	exibir(window.htmlAtual, window.pdfAtual);
	resize();

	function exibirNoIFrame(url) {
		var ifr = document.getElementById('painel');
		var ifrp = document.getElementById('paipainel');

		if (ifr.addEventListener)
			ifr.removeEventListener("load", resize, false);
		else if (ifr.attachEvent)
			ifr.detachEvent("onload", resize); // Bug fix line
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
</script>
