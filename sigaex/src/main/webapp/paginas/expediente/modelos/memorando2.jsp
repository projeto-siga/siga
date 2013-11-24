<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
			<mod:grupo titulo="Texto a ser inserido no corpo do memorando">
				<mod:grupo>
					<mod:editor2 titulo="" var="texto_memorando" />
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
		<html>
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
@body {
	margin-top: 2cm;
	margin-bottom: 0.5cm; 
}
@first-page-body {
	margin-top: 4cm;
	margin-bottom: 0.5cm; 
}
</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina2.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">MEMORANDO N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->
		
		<mod:letra tamanho="${tl}">
			<p align="left">DE: <c:choose>
				<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
				<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
			</c:choose> <br>
			PARA: ${doc.destinatarioString}</p>
			<span style="font-size: ${tl}"> ${texto_memorando} </span>
			<p align="justify" style="text-indent:2cm">Atenciosamente,</p>
			<p>&nbsp;</p>
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

		</mod:letra>
		</body>
		<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->
		
		</html>
	</mod:documento>
<%-- 	
	<mod:finalizacao>
		{Memorando Finalizado!}
		<c:set var="f" value="${f:criarWorkflow('Exoneracao', doc)}" />
	</mod:finalizacao>
--%>	
</mod:modelo>
