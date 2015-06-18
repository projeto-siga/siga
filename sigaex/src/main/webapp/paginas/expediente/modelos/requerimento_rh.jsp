<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA ADOTANTE -->

<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
		<mod:texto titulo="Endereçamento" var="texto_destinatario"/> 
			<mod:grupo titulo="Texto a ser inserido no corpo do requerimento">
				<mod:grupo>
					<mod:editor titulo="" var="texto_requerimento" />
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
		<c:if test="${not empty texto_requerimento}">
		<c:choose>
			<c:when test="${not empty texto_destinatario}">
				<h1 align="center">${texto_destinatario}</h1>
			</c:when>
			<c:when test="${not empty sepag}">
				<h1>SENHOR(A) SUPERVISOR(A) DA SEÇÃO DE FOLHA DE PAGAMENTO</h1>
			</c:when>
			<c:when
				test="${para eq 'diretorForo' || (not empty doc.lotaDestinatario and f:lotacaoPorNivelMaximo(doc.lotaDestinatario,4).sigla == 'DIRFO')}">
				<h1 align="center">Exmo. Sr. Juiz Federal - Diretor do Foro</h1>
			</c:when>
			<c:when test="${para eq 'diretoraRH'}">
				<h1 align="center">Ilma. Sra. Diretora da Subsecretaria de
				Gestão de Pessoas</h1>
			</c:when>
			<c:when test="${para eq 'sinap'}">
				<h1 align="center">Ilma. Sra. Supervisora da Seção de Inativos e Pensionistas</h1>
			</c:when>
			<c:when test="${para eq 'presidenteTRF'}">
				<h3 align="center">EXCELENTÍSSIMO SENHOR PRESIDENTE DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</H3>
			</c:when>
			<c:when test="${not empty doc.lotaDestinatario}">
				<h1>SENHOR(A) SUPERVISOR(A) DA ${doc.lotaDestinatario.nomeMaiusculas}</h1>
			</c:when>
			<%--<c:otherwise>
				<h1 align="center">Ilmo(a). Sr(a). Diretor(a) da
				${doc.lotaDestinatario.descricao}</h1>
			</c:otherwise>--%>
		</c:choose>


		<p style="TEXT-INDENT: 2cm" align="justify">
		<p><span style="font-size: ${tl}">${texto_requerimento}</span></p>
		<c:choose>
		<c:when test="${not empty nestesTermos}">
			<p style="TEXT-INDENT: 2cm">${nestesTermos}</p>
		</c:when>
		<c:when test="${empty sepag}">
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
		</c:when>
		</c:choose>
		<br/>
		<br/>
		<p align="center">${doc.dtExtenso}</p>
		<c:if test="${empty semCargo}">
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?apenasCargo=sim" />
		</c:if>
		<c:if test="${not empty acordoSuperior}"> 
			<br/>
			<c:set var="comData" value="sim" scope="request" />
			
			<c:import url="/paginas/expediente/modelos/inc_deAcordoAssSupHierarquico.jsp" />
			
		</c:if>
		</c:if>
		
		<c:if test="${not empty texto_requerimento4}">

			<p><span style="font-size: ${tl}">${texto_requerimento4}</span></p>
			<br />
			<br />

		</c:if>

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		<c:choose>
			<c:when test="${not empty texto_requerimento2 and not empty texto_requerimento}">
				<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<%-- Segunda via --%>
		<c:if test="${not empty texto_requerimento2}">
		<p style="TEXT-INDENT: 2cm" align="justify">
		<p><span style="font-size: ${tl}">${texto_requerimento2}</span></p>
			
			<c:choose>
				<c:when test="${not empty texto_requerimento3}">
					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>

		</c:if>
		<c:if test="${not empty texto_requerimento3}">
		<p style="TEXT-INDENT: 2cm" align="justify">
		<p><span style="font-size: ${tl}">${texto_requerimento3}</span></p>
		</c:if>
		
		</body>
		</html>
	</mod:documento>
</mod:modelo>
