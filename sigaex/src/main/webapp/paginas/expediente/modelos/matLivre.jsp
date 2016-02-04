<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:selecao titulo="Quantidade de Materias a informar"
			opcoes="1;2;3;4;5;6;7;8;9;10" var="materias" reler="sim" />
		<mod:grupo>
			<c:forEach var="i" begin="1" end="${materias}">

				<mod:selecao reler="sim" titulo="Tipo de Matéria"
					opcoes="Portaria SGP;Errata;Anexo;Portaria;Campo Livre"
					var="tipoMateria${i}" />

				<c:set var="vars" value="nmTipoMateria${i}" />
				<c:choose>
					<c:when test="${param.entrevista == 1}">
						<c:if
							test="${requestScope[f:concat('tipoMateria',i)] eq 'Campo Livre'}">
							<mod:texto titulo="Descrição" var="nmTipoMateria${i}" />
						</c:if>
					</c:when>
					<c:otherwise>
						<span class="valor">
						${requestScope[f:concat('nmTipoMateria',i)]}
						</span>
					</c:otherwise>
				</c:choose>

				<mod:grupo>
					<mod:texto titulo="Título da Matéria" var="tituloMateria${i}"
						largura="40" />
				</mod:grupo>
				<mod:grupo>
					<mod:lotacao titulo="Órgão de Origem" var="orgOrigem${i}" />
				</mod:grupo>
				<mod:grupo>
					<mod:editor titulo="" var="texto_publicacao${i}" />
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
			opcoes="Normal;Pequeno;Grande" />
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

			<p align="center">SOLICITAÇÃO DE PUBLICAÇÃO BOLETIM INTERNO<br />
			Matéria Livre</p>

			<c:forEach var="i" begin="1" end="${materias}">
				<p><b>${requestScope[f:concat('tituloMateria',i)]}</b><br />
				<b>Origem:</b>
				${requestScope[f:concat(f:concat('orgOrigem',i),'_lotacaoSel.sigla')]}<br />
				<span> ${requestScope[f:concat('texto_publicacao',i)]} </span></p>
			</c:forEach>
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>
		<br>
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