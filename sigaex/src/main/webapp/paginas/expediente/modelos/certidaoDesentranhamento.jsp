<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<mod:modelo>
	<mod:entrevista>

		<mod:grupo>
			<mod:texto titulo="Desentranhar folhas: início" var="folhaInicial"
				largura="8" />
			<mod:texto titulo="fim" var="folhaFinal" largura="8" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Vocativo" var="vocativo" largura="20" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Responsável" var="responsavel" />
		</mod:grupo>
		<%--  <mod:grupo titulo="Finalidade:"></mod:grupo>
			<mod:grupo>
				<mod:editor titulo="" var="textoFinalidade" />
			</mod:grupo> --%>
		<mod:grupo>
			<mod:memo titulo="Motivo" var="textoMotivo" colunas="60" linhas="10" />
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
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<br><br/><c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
						</td>	
					</tr>
					<tr bgcolor="#FFFFFF">
						<td cellpadding="5">
							&nbsp;
						</td>
					</tr>
				</table>
			FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
				<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
				<br/><br/>
			FIM CABECALHO -->

		<br />
		<br />
		<br />

		<p align="center"><b>CERTIDÃO DE DESENTRANHAMENTO</b></p>
		<br />
		<br />
		<c:choose>
			<c:when test="${not empty mov.exMovimentacaoRef.exMobilRef}">
				<c:set var="mobil"
					value="${mov.exMovimentacaoRef.exMobilRef.mobilPrincipal}" />
			</c:when>
			<c:otherwise>
				<c:set var="mobil" value="${mov.exMobil.mobilPrincipal}" />
			</c:otherwise>
		</c:choose>

		<p align="left">${mobil.nomeCompleto}</p>

		<p align="left">RESPONSÁVEL: ${vocativo}
		${mov.subscritor.descricao}</p>
		<c:if test="${not empty textoMotivo}">
			<p align="left">MOTIVO: ${textoMotivo}</p>
		</c:if>
		<br />
		<br />
		<br />
		<p align="left" style="TEXT-INDENT: 2cm">Certifico que, nesta
		data, desentranhei <c:if test="${folhaInicial != folhaFinal}">
		 as folhas ${folhaInicial} a ${folhaFinal}
		</c:if> <c:if test="${folhaInicial == folhaFinal}">
		 a folha ${folhaInicial}
		</c:if> do ${mobil.descricaoCompleta} do
		${mobil.doc.exFormaDocumento.exTipoFormaDoc.descricao} em epígrafe.</p>

		<br />
		<br />

		<p align="center" style="TEXT-INDENT: 0cm">${mov.dtExtenso}</p>

		<br />

		<c:import
			url="/paginas/expediente/modelos/inc_assinatura_mov_despacho.jsp?formatarOrgao=sim" />

		<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			FIM PRIMEIRO RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>
