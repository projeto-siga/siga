<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<mod:modelo>
	<mod:entrevista>

		<mod:grupo>
			<mod:texto titulo="Desentranhar folhas: inÃ­cio" var="folhaInicial"
				largura="8" />
			<mod:texto titulo="fim" var="folhaFinal" largura="8" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Vocativo" var="vocativo" largura="20" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="ResponsÃ¡vel" var="responsavel" />
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
		 <!-- INICIO CABECALHO FIM CABECALHO -->
		<table width="100%" align="left" border="0" cellpadding="0"
			cellspacing="0" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<table width="100%" border="0" cellpadding="2">
					<tr>
						<td width="100%" align="center" valign="bottom"><img src="contextpath/imagens/brasao_sp.png" width="65" height="65" /></td>
					</tr>
					<tr>
						<td width="100%" align="center">
							<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt; font-weight: bold;">GOVERNO DO ESTADO DE SÃO PAULO</p>
						</td>
					</tr>
					<tr>
						<td width="100%" align="center">
							<p style="font-family: Arial; font-size: 10pt;"><c:choose>
								<c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
								<c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
							</c:choose></p>
						</td>
					</tr>
					<tr>
						<td width="100%" align="center">
							<p style="font-family: Arial; font-size: 10pt;"><c:choose>
								<c:when test="${empty mov}">${doc.lotaTitular.nomeLotacao}</c:when>
								<c:otherwise>${mov.lotaTitular.nomeLotacao}</c:otherwise>
							</c:choose></p>
						</td>
					</tr>
					</tr>
				</table>
				</td>
			</tr>
		</table>

		<br />
		<br />
		<br />

		<p align="center"><b>Termo de Desentranhamento</b></p>
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

		<p align="left">Documento: ${doc.sigla} <c:if test="${not empty mob.numSequencia}">${mob.numSequencia}º Volume</c:if></p>

		<p align="left">Responsável: ${vocativo} ${mov.subscritor.descricao}</p>
		<c:if test="false">
			<p align="left">MOTIVO: ${textoMotivo}</p>
		</c:if>
		<br />
		<br />
		<br />
		<p align="left" style="TEXT-INDENT: 2cm">Certifico que, nesta
		data, desentranhei deste documento ${mob.exMobilPai.sigla}<c:if test="${folhaInicial != folhaFinal}">
		 as folhas ${folhaInicial} a ${folhaFinal}
		</c:if> <c:if test="${folhaInicial == folhaFinal}">
		 a folha ${folhaInicial}
		</c:if> correspondente ao documento ${mob.sigla}.</p>
		
		<c:if test="${textoMotivo.isEmpty() == false}">
			<p align="left" style="TEXT-INDENT: 2cm">
				Motivo: ${textoMotivo}
			</p>
		</c:if>

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
