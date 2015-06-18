<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados referenciais">
			<mod:grupo>
				<mod:texto titulo="Referência" var="referencia" largura="30" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Vocativo" var="vocativo" largura="30" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo da exposição de motivos">
			<mod:grupo>
				<mod:editor titulo="" var="texto_exposicao_motivos" />
			</mod:grupo>
		</mod:grupo>
		
			<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
		
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if>
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
							<td align="right"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">EXPOSI&Ccedil;&Atilde;O DE MOTIVOS N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:normal;">${doc.dtExtenso}</p></mod:letra></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">ASSUNTO: ${doc.descrDocumento}</p></mod:letra></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold;">REF: ${referencia}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">
		<p><br>
		<br>
		<br>
		</p>
		<p>${vocativo},</p>
		<span style="font-size:${tl};">
		<p>${texto_exposicao_motivos}</p>
		</span>
		<p>
		<br />
		</p>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>
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

