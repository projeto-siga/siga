<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Texto a ser inserido no corpo da ordem de serviço">
			<mod:grupo>
				<mod:editor titulo="" var="texto_ordem_servico" />
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
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2.5cm;
			}
		</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td><c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" /></td>
		</tr>
		<tr>
			<td align="center"><p style="font-family: Arial; font-size: 10pt; font-weight: bold;">CORREGEDORIA-REGIONAL</p></td>
		</tr>		
		
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">ORDEM DE SERVI&Ccedil;O N &ordm; ${doc.codigo} de ${doc.dtExtensoSemLocalidade} </p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		
		<!-- INICIO NUMERO <span style="font-weight: bold;">ORDEM DE SERVIÇO DA CORREGEDORIA N&ordm; ${doc.codigo}</span> FIM NUMERO -->
		
		<mod:letra tamanho="${tl}">
		
		<p>&nbsp;</p>
		<!-- INICIO MIOLO -->
		<p><span style="font-size:${tl};">
		<!-- INICIO CORPO -->${texto_ordem_servico}<!-- FIM CORPO -->
		</span>
		</p>
		<p align="center"><!-- INICIO FECHO -->CUMPRA-SE.<!-- FIM FECHO --><br />
		<br />
		</p>
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /><!-- FIM ASSINATURA -->
		<!-- FIM MIOLO -->
		</mod:letra>
		
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		
		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->
		
		</body>
		</html>
	</mod:documento>
</mod:modelo>

