<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
<%--  Suprimi a entrevista pois o  número do processo será preenchido automaticamente, não será necessário o campo processo.
	<mod:entrevista>
		<mod:texto titulo="Nº processo" var="numProcesso" largura="20" />
	</mod:entrevista>  --%>
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
		
		<p align="left"><b>PROCESSO ${doc.sigla}</b></p>

		<br />
		<br />

		<p align="center"><b>CERTIDÃO</b></p>

		<br />
		<br />

		<p align="left" style="TEXT-INDENT: 2cm">Certifico que, nesta
		data, encerrei o volume ${mob.numSequencia} do processo em epígrafe.</p>

		<br />
		<br />

		<p align="center" style="TEXT-INDENT: 0cm">${mov.dtExtenso}</p>

		<br />
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




























