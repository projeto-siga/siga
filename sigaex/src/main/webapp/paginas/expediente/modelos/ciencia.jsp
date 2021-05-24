<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<mod:modelo>
	<mod:entrevista>

		<mod:grupo>
			<mod:texto titulo="Ci�ncia" var="folhaInicial"
				largura="8" />
			<mod:texto titulo="fim" var="folhaFinal" largura="8" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Vocativo" var="vocativo" largura="20" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="ResponsÃ¡vel" var="responsavel" />
		</mod:grupo>
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
		<p align="left">N&uacute;mero de refer&ecirc;ncia: ${doc.sigla}</p>
		
		<br />
		<br />

		<p align="center"><b>CI&Ecirc;NCIA</b></p>

		<br />
		<br />

		
		<p align="left"><siga:fixcrlf>${textoMotivo}</siga:fixcrlf></p>
		
		<br />
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
