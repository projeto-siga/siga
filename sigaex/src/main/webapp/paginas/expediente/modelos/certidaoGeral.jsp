<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados do Documento de Origem">
			<mod:grupo>
				<mod:texto titulo="Tipo de documento" var="tipoDeDocumento" largura="20" />
				<mod:texto titulo="Número" var="numero" largura="20" />
			</mod:grupo>
			<mod:grupo>
			<mod:texto titulo="Assunto" var="assunto" largura="100" />
			</mod:grupo>
			<mod:grupo>
			<mod:texto titulo="Relator" var="relator" largura="100" />
			</mod:grupo>
			<mod:grupo>
			<mod:texto titulo="Data do julgamento" var="dataJulgamento" largura="30" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo da certidão">
			<mod:grupo>
				<mod:editor titulo="" var="texto" />
			</mod:grupo>
		<mod:selecao
			titulo="Tamanho da letra"
			var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>	
		</mod:grupo>

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
	margin-bottom: 2cm;
    }
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0" ><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
				<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
				<br/><br/>
			FIM CABECALHO -->
		<br/>
		<p align="center" style="font-family:Arial;font-size:11pt;font-weight: bold;">CERTIDÃO N&ordm; ${doc.codigo}</p>
		<mod:letra tamanho="${tl}">		
		<br />
		<br />
		<c:if test="${not empty tipoDeDocumento}">
		<p align="left">${tipoDeDocumento}&nbsp nº&nbsp ${numero}</p>
		</c:if>
		<c:if test="${not empty assunto}">
		<p align="left">ASSUNTO:&nbsp ${assunto}</p>
		</c:if>
		<c:if test="${not empty relator}">
				<p>RELATOR:&nbsp ${relator} </p>
		</c:if>
		<c:if test="${not empty dataJulgamento}">
				<p>Data do julgamento:&nbsp ${dataJulgamento} </p>
		</c:if>
		<br />
		
		${texto}
	
			<br />
		<p align="center" style="TEXT-INDENT: 0 cm">${doc.dtExtenso}</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

		</mod:letra>	
		<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			FIM PRIMEIRO RODAPE -->
		</body>
		</html>
	</mod:documento>
</mod:modelo>
