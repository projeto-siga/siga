<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
				<mod:selecao titulo="Quantidade de PCDs a informar:" opcoes="1;2;3;4;5;6;7;8;9;10" var="pcds" reler="sim" />
				<mod:selecao titulo="Mês de Referência:" var="mesReferencia" opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
				<mod:texto titulo="Ano:" var="anoReferencia" largura="4" />
		</mod:grupo>
			<mod:grupo>
				<c:forEach var="i" begin="1" end="${pcds}">
					<mod:texto titulo="Número da PCD" var="numpcd${i}" />	
					<mod:grupo>
							<mod:editor titulo="" var="texto_diaria${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
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
				margin-right: 2cm;
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
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<mod:letra tamanho="${tl}">
		<p>DE: <c:choose>
			<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
			<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
		</c:choose> <br>
		PARA: ${doc.destinatarioString}</p>
	
		<p align="center"> SOLICITAÇÃO DE PUBLICAÇÃO BOLETIM INTERNO<br/>
		PCD - Pedido de Concessão de Diária
		</p>
		
		<span style="font-size:${tl};">
		
		<!-- INICIO CORPO -->
		<p><span style="font-weight: bold">CONCESSÃO DE DIÁRIAS - ${mesReferencia} de ${anoReferencia}</span></p>
		<!-- FIM CORPO -->
		</span>
		
		<c:forEach var="i" begin="1" end="${pcds}">
			<!-- INICIO CORPO -->
			<p>
			<span style="font-weight: bold">PCD: ${anoReferencia}/${requestScope[f:concat('numpcd',i)]}</span>
			<span style="font-size:${tl};">
			${requestScope[f:concat('texto_diaria',i)]}
			</span>
			</p>
			<!-- FIM CORPO -->
			
		</c:forEach>
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

