<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<br></br>
		<mod:grupo>
			<mod:texto var="ptsub" titulo="<b>Portaria de Subdelegação nº</b>" />
			<mod:data titulo="<b>Data de Vigência</b>" var="datvig" />
			<mod:texto var="proc_port" titulo="<b>Processo Administrativo</b>" />
		</mod:grupo>
		<br>
		<mod:grupo>
			<mod:caixaverif titulo="<b>Consideração Extra</b>" var="outros"
				marcado="Nao" reler="sim" />
			<c:if test="${ outros == 'Sim'}">
				<mod:texto titulo="Especificar" var="outrostext" largura="70" />
			</c:if>
		</mod:grupo>
		<br>
		<mod:grupo titulo="Texto a ser inserido no corpo da portaria:">
			<br>
			<mod:grupo>
				<mod:editor titulo="" var="texto_portaria" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
			opcoes="Normal;Pequeno;Grande" />
	</mod:entrevista>
	<mod:documento>
		<c:set var="tl" value="9pt" />
		<c:if test="${tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 1cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
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
		<br>
		<p align="center" style="font-family: Arial; font-size: 11pt;"><!-- INICIO NUMERO --><span
			style="font-weight: bold;">PORTARIA N&ordm; ${doc.codigo}</span><!-- FIM NUMERO -->
		<b>&nbsp; de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}</b></p>

		<!-- INICIO TITULO 
			<mod:letra tamanho="${tl}">
				<p>${doc.codigo}</p>
			</mod:letra>
		FIM TITULO -->

		<mod:letra tamanho="${tl}">
			<p>&nbsp;</p>
			<!-- INICIO MIOLO -->

			<!-- INICIO CORPO -->
			<p align="justify"><span style="TEXT-INDENT: 2cm; font-size:${tl};">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><c:choose>
				<c:when test="${doc.subscritor.sexo == 'M'}">O DIRETOR</c:when>
				<c:otherwise>A DIRETORA</c:otherwise>
			</c:choose> DA SECRETARIA DE RECURSOS HUMANOS DO TRIBUNAL REGIONAL FEDERAL DA 2ª
			REGIÃO</b>, conforme subdelegação de competência outorgada pela Portaria
			nº ${ptsub}, de ${datvig}, da Secretaria Geral, <c:choose>
				<c:when test="${outros != 'Sim'}">e considerando o que
			consta do Processo Administrativo nº ${proc_port}, </c:when>
				<c:otherwise>${outrostext} o que
			consta do Processo Administrativo nº ${proc_port}, </c:otherwise>
			</c:choose><b>RESOLVE</b>:<br>

			${texto_portaria} </span></p>

			<!-- FIM CORPO -->

			<p align="center"><!-- INICIO FECHO --><b>PUBLIQUE-SE.
			REGISTRE-SE. CUMPRA-SE.</b><!-- FIM FECHO --><br />
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

