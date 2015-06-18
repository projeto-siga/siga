<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
			<mod:grupo titulo="Texto a ser inserido no corpo do anexo">
				<mod:grupo>
					<mod:editor titulo="" var="texto_anexo" />
				</mod:grupo>
			</mod:grupo>
			<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
				opcoes="Normal;Pequeno;Grande" />
		</c:if>
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
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
		
		<c:if test="${empty tl}">
				<c:set var="tl" value="11pt"></c:set>
		</c:if>
		
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">ANEXO N&ordm; ${doc.codigo}</p></td>
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

			<!-- INICIO NUMERO --><span style="font-weight: bold;">Anexo ao documento ${doc.exMobilPai.exDocumento.codigo}</span><!-- FIM NUMERO -->
			<!-- INICIO TITULO <br /><p style="font-family: Arial; font-size: 11pt; font-weight: bold;"align="left">ANEXO N&ordm; ${doc.codigo}</p> FIM TITULO -->
			<!-- INICIO MIOLO --><!-- INICIO CORPO -->
			<span style="font-size: ${tl}"> ${texto_anexo} </span>
			<p>&nbsp;</p>
			<!-- FIM CORPO -->
			<!-- FIM MIOLO -->
			<!-- INICIO FECHO --><!-- FIM FECHO -->
			<!-- INICIO ASSINATURA --><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /><!-- FIM ASSINATURA -->
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
	<%-- 	
	<mod:finalizacao>
		{Memorando Finalizado!}
		<c:set var="f" value="${f:criarWorkflow('Exoneracao', doc)}" />
	</mod:finalizacao>
--%>
</mod:modelo>