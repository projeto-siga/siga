<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>

	<c:if test="${empty esconderTexto}">		
		<mod:grupo titulo="Texto a ser inserido no corpo da solicitação">
			<mod:grupo>
				<mod:editor titulo="" var="texto_solicitacao" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
				opcoes="Normal;Pequeno;Grande" />
	</c:if>
	</mod:entrevista>

	<mod:documento>
		<c:if test="${empty tamanhoLetra or tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<!-- INICIO CORPO -->
		<p align="right"><b><!-- INICIO NUMERO -->SOLICITAÇÃO Nº ${doc.codigo}<!-- FIM NUMERO --></b><br/>${doc.dtExtenso}
		</p>
					<h1 algin="center">Exmo(a). Sr(a). Juiz(a) Federal - Diretor do Foro</h1>
					
		<mod:letra tamanho="${tl}">

		<p style="TEXT-INDENT: 2cm" align="right">
					
		<p>&nbsp;</p>
		<p><span style="font-size:${tl};">${texto_solicitacao}</span></p>
		<!-- FIM CORPO -->
		
		
		<p style="MARGIN-LEFT: 2cm" align="justify">Atenciosamente,</p>
		
		<br><br><br>
		
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		</mod:letra>		
		</body>
		</html>
	</mod:documento>
</mod:modelo>
