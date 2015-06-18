<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight: bold;"><br/><br/>REF. N&ordm; ${doc.codigo} de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY} - ${doc.lotaTitular.descricao}.</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<p><br/>
		<br/>
		</p>
		<c:if test="${(not empty mov.lotaResp) and (mov.lotaResp.idLotacaoIni != mov.lotaCadastrante.idLotacaoIni)}">
			<p>À ${mov.lotaResp.descricao},</p>
		</c:if>
		<c:if test="${not empty despachoTexto}">
			<p style="TEXT-INDENT: 2cm">${despachoTexto}</p>
		</c:if>
		${despachoHtml}
		<p align="center"><br />
		${mov.dtExtenso}</p>
		<p><br />
		</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura_mov_despacho.jsp" />

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>

