<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
			<mod:grupo>
				<mod:texto titulo="Título" var="tituloAviso" largura="45"/> <b>(Por extenso, em letras maiúsculas, mencionando-se a natureza/motivo.)<b/>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Assunto" var="assunto" largura="45"/><b>(Por extenso, indicando a matéria, o tema ou o objeto.)<b/>
			</mod:grupo>
			<mod:grupo titulo="Texto a ser inserido no corpo do AVISO: exposição do fato (conteúdo simples e objetivo; com informações precisas.)">
				<mod:grupo>
					<mod:editor titulo="" var="texto_aviso" />
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
		<br/>
		<p align="center" style="font-family:Arial;font-size:11pt;">
				<span style="font-weight: bold;">AVISO ${tituloAviso}</span>
		</p>
		<p align="left" style="font-family:Arial;font-size:11pt;">
				<span style="font-weight: bold;">Assunto: ${assunto}</span>
		</p>
		
		<mod:letra tamanho="${tl}">

			<p>&nbsp;</p>
			
			
			<p><span style="font-size:${tl};">${texto_aviso}</span><p/>
			
			<br />
			<p align="center">${doc.dtExtenso}<br />
			<br />
			</p>
			<br />
			
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />   
			
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

