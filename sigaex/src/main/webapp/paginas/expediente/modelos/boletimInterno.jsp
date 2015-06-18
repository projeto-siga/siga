<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@page import="br.gov.jfrj.siga.ex.bl.ExBL"%>
<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo salvarViaAjax="N" acaoGravar="gravarBI" acaoExcluir="excluirBI" acaoCancelar="refazerBI" acaoFinalizar="finalizarBI">
	<mod:entrevista>
		<mod:grupo>
			<mod:texto titulo="Nome do Diretor do Foro" var="nmDiretorForo" valor="Dr. Carlos Guilherme Francovich Lugones" largura="40"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Nome do Diretor da SG" var="nmDiretorRH" valor="Patrícia Reis Longhi" largura="40" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Setores Responsáveis" var="setoresResponsaveis" valor="DIRFO: NSDF / SGP: SEPRF, SEBEN, SECAD, SELEG, SELOT, SESAU / SOF / SG" largura="90"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Geração" var="geraImpress" valor="SID/CIPE" largura="90"/>
		</mod:grupo>
		
		<mod:grupo titulo="Documentos a Publicar">
			<table>
				<c:forEach var="ex" items="${listaDocsAPublicarBoletim}">
					<tr>
						<td><mod:caixaverif var="doc_boletim${ex.idDoc}"
							marcado="Sim" /></td>
						<td><a href="javascript:void(0)"
							onclick="javascript: window.open('/sigaex/expediente/doc/exibir.action?popup=true&id=${ex.idDoc}', '_new', 'width=700,height=500,scrollbars=yes,resizable')">${ex.codigo}</a></td>
						<td style="padding-left: 30px">${ex.dtFinalizacaoDDMMYY}</td>
						<td style="padding-left: 30px">${ex.lotaCadastrante.sigla}</td>
						<td style="padding-left: 30px">${ex.descrDocumento}</td>
						<td style="padding-left: 30px">
							<ww:url id="url" action="cancelar_pedido_publicacao_boletim" namespace="/expediente/mov">
								<ww:param name="sigla">${ex.sigla}</ww:param>
							</ww:url>
							<ww:a href="%{url}">Cancelar Pedido</ww:a>
						</td>						
					</tr>
				</c:forEach>
			</table>
		</mod:grupo>
		
		<mod:grupo titulo="Documentos Selecionados">
			<table>
				<c:forEach var="ex" items="${listaDocsAPublicarBoletimPorDocumento}">
					<tr>
						<td><mod:caixaverif var="doc_boletim${ex.idDoc}"
							marcado="Sim" /></td>
						<td><a href="javascript:void(0)"
							onclick="javascript: window.open('/sigaex/expediente/doc/exibir.action?popup=true&id=${ex.idDoc}', '_new', 'width=700,height=500,scrollbars=yes,resizable')">${ex.codigo}</a></td>
						<td style="padding-left: 30px">${ex.dtFinalizacaoDDMMYY}</td>
						<td style="padding-left: 30px">${ex.lotaCadastrante.sigla}</td>
						<td style="padding-left: 30px">${ex.descrDocumento}</td>
					</tr>
				</c:forEach>
			</table>
		</mod:grupo>		
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>

		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
			@body {
				margin-top: 2cm;
				margin-bottom: 0.5cm; 
			}
			@first-page-body {
				margin-top: 4cm;
				margin-bottom: 8cm; 
			}
		</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
			<table width="100%" align="left" border="0" bgcolor="#FFFFFF">
				<tr bgcolor="#FFFFFF">
					<td align="left" valign="bottom" width="100%"><img
						src="contextpath/imagens/cabecalhoBoletimInterno.gif" width="450" height="65" /></td>
				</tr>
			</table>
		</td></tr>
		<tr>
			<td style="border-width: 2px 0px 2px 0px; border-style: solid; padding-bottom: 0px">
			<table width="100%">
				<tr>
					<td align="left" width="50%">
						${doc.codigo}
					</td>
					<td align="right" width="50%">
						Publicação Diária - Data: ${doc.dtDocDDMMYYYY}
					</td>	
				</tr>
			</table>
			</td>
		</tr>
		</table>
		
		FIM PRIMEIRO CABECALHO -->
		<br />
		<br />
		<br />
		<!-- INICIO CABECALHO
			<table width="100%" bgcolor="#FFFFFF">
				<tr>
					<td valign="bottom" align="center" style="border-width: 0px 0px 1px 0px; border-style: solid; padding-top: 3px">
						<span style="font-size:11px">${doc.codigo} de ${doc.dtDocDDMMYY}</span>
					</td>
				</tr>
			</table>
		FIM CABECALHO -->

		<c:set var="hBIE"
			value="${f:obterHierarquizadorBIE(lotaTitular.orgaoUsuario, doc)}"></c:set>

		<c:forEach var="topico" items="${hBIE.nodosPrincipais}"
			varStatus="loop">
			<c:if test="${not topico.vazio}">
				<table align="left" border="0" width="100%" bgcolor="#FFFFFF">
					<tr>
						<td valign="bottom" style="border-width: ${loop.index}px 0px 0px 0px; border-style: solid; padding-top: 3px">
						<h2>${topico.descr}</h2>
						</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${loop.index == 1}"><c:set var="sublinhado">style="text-decoration: underline"</c:set></c:if>
			<c:forEach var="subTopico" items="${topico.nodos}">
			<span style="page-break-inside: avoid">
				<c:if test="${not subTopico.vazio}">
					<h3 align="right" ${sublinhado}>${subTopico.descr}</h3>
				</c:if>
				<%--<mod:letra tamanho="10pt">--%>
					<c:forEach var="d" items="${subTopico.exDocumentoSet}"
						varStatus="num">
						<c:if test="${d.exFormaDocumento.idFormaDoc != 55}">
							<c:set var="aberturaAtual" value="${f:enxugaHtml(d.aberturaHtmlString)}" />
							<c:set var="fechoAtual" value="${f:enxugaHtml(d.fechoHtmlString)}${f:enxugaHtml(d.assinaturaHtmlString)}" />
						</c:if>

						<c:if
							test="${not empty aberturaAtual && ultAbertura != aberturaAtual}">
							<div align="center">${aberturaAtual}</div>
						</c:if>
						<c:set var="ultAbertura" value="${aberturaAtual}" />
						
						<c:if
							test="${(not empty ultFecho) && ultFecho != fechoAtual}">
							<div align="center">${ultFecho}</div>
							<br/>
						</c:if>
						<c:choose>
							<c:when test="${d.exModelo.hisIdIni == 234}">
								<span style="font-weight: bold">${f:obterTituloMateriaLivre(subTopico, d, 1)}</span>
								${f:obterBlobMateriaLivre(subTopico, d, 1)}
							</c:when>
							<c:otherwise>
								<c:if test="${not empty d.numeroHtmlString}">
									<span style="font-weight: bold">${d.numeroHtmlString} de ${d.dtD} de ${d.dtMMMM} de
									${d.dtYYYY}</span>
								</c:if>
								${f:enxugaHtml(d.corpoHtmlString)}
							</c:otherwise>
						</c:choose>
						<c:set var="ultFecho" value="${fechoAtual}" />
						<br/>
					</c:forEach>
				<%--</mod:letra>--%>
				<c:if test="${not subTopico.vazio}">
					<div align="center">${ultFecho}</div>
					<br/>
				</c:if>
				<c:remove var="ultFecho" />
				</span>
			</c:forEach>
		</c:forEach>
		<br/>
		<span>&nbsp;</span>
		<br/>
		<center><span style="font-size: 15px;">********************************* FIM *********************************</span></center>

		<!-- INICIO PRIMEIRO RODAPE
		<%--<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />--%>
		<c:import url="/paginas/expediente/modelos/inc_rodapeBoxBoletimInternol.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->
	</mod:documento>

	<%--<mod:finalizacao>
		{Boletim Interno gerado e finalizado}
		<c:forEach var="d" items="${listaDocsAPublicarBoletim}">
			<c:if
				test="${requestScope[f:concat('doc_boletim',d.idDoc)] == 'Sim'}">
			</c:if>
		</c:forEach>

	</mod:finalizacao>--%>
</mod:modelo>