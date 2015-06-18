<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
			<mod:grupo titulo="Texto a ser inserido no corpo da portaria">
				<mod:grupo>
					<mod:editor titulo="" var="texto_portaria" />
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
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
		<body>
		
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<p align="center" style="font-family:Arial;font-size:11pt;">
			<!-- INICIO NUMERO --><span style="font-weight: bold;">PORTARIA-DIRFO-GP N&ordm; ${doc.codigo}&nbsp;</span><!-- FIM NUMERO --> de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}
		</p>
		
		<!-- INICIO TITULO 
			<mod:letra tamanho="${tl}">
				<p>${doc.codigo}</p>
			</mod:letra>
		FIM TITULO -->
		
		<mod:letra tamanho="${tl}">

			<p>&nbsp;</p>
			<!-- INICIO MIOLO -->
			<p style="TEXT-INDENT: 2cm" align="justify"><b>O JUIZ FEDERAL - DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS 
			SERVIÇOS AUXILIARES DA JUSTIÇA FEDERAL DE 1º GRAU - SEÇÃO JUDICIÁRIA DO ESPÍRITO SANTO</b>, no uso de suas atribuições legais,</p>
			<p style="TEXT-INDENT: 2cm" align="justify"><b>RESOLVE:</b></p>
			
			<!-- INICIO CORPO --><p><span style="font-size:${tl};">${texto_portaria}</span><p/><!-- FIM CORPO -->
			
			
			<p align="center"><!-- INICIO FECHO -->PUBLIQUE-SE. REGISTRE-SE. CUMPRA-SE.<!-- FIM FECHO --><br />
			<br />
			</p>
			<!-- FIM MIOLO -->
			<!-- INICIO ASSINATURA -->
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />   
			
			<!-- FIM ASSINATURA -->
		</mod:letra>
		
	
		
		<!-- INICIO PRIMEIRO RODAPE
		<c:choose>
			<c:when test="${(public == 'sim') and (empty dataInicio)}">
				<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumentalPublicacao.jsp" />
			</c:when>
			<c:otherwise>
				<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			</c:otherwise>
		</c:choose>
		FIM PRIMEIRO RODAPE -->
		
		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>

