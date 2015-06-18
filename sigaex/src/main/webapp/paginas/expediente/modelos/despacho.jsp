<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados do Documento de Origem">
			<c:choose>
				<c:when
					test="${postback != 1 && (empty doc.idDoc) && (not empty doc.pai)}">
					<c:set var="tipoDeDocumentoValue">${doc.pai.descrFormaDoc}</c:set>
					<c:set var="numeroValue">${doc.pai.sigla}</c:set>
					<c:set var="dataValue">${doc.pai.dtDocDDMMYY}</c:set>
					<c:set var="orgaoValue">${doc.pai.orgaoUsuario.acronimoOrgaoUsu}</c:set>
				</c:when>
				<c:otherwise>
					<c:set var="tipoDeDocumentoValue">${param['tipoDeDocumento']}</c:set>
					<c:set var="numeroValue">${param['numero']}</c:set>
					<c:set var="dataValue">${param['data']}</c:set>
					<c:set var="orgaoValue">${param['orgao']}</c:set>
				</c:otherwise>
			</c:choose>
			<mod:grupo>
				<mod:texto titulo="Tipo de documento" var="tipoDeDocumento"
					largura="20" valor="${tipoDeDocumentoValue}" />
				<mod:texto titulo="Número" var="numero" largura="20" valor="${numeroValue}" />
			</mod:grupo>
			<mod:grupo>
				<mod:data titulo="Data" var="data" valor="${dataValue}" />
				<mod:texto titulo="Nome do Órgão" var="orgao" largura="30" valor="${orgaoValue}"/>
			</mod:grupo>
		</mod:grupo>

		<c:set var="orgao_dest_prov" value="${orgao_dest}" />
		<c:if test="${param['alterouSel'] eq 'lotacaoDestinatario'}">
			<c:set var="orgao_dest_prov">${doc.lotaDestinatario.nomeLotacao}</c:set>
		</c:if>
		<c:if test="${param['alterouSel'] eq 'destinatario'}">
			<c:set var="orgao_dest_prov">${doc.destinatario.lotacao.nomeLotacao}</c:set>
		</c:if>
		<mod:grupo titulo="Órgão de destino">
			<mod:grupo>
				<mod:texto titulo="Nome" var="orgao_dest" largura="30"
					valor="${orgao_dest_prov}" /> &nbsp&nbsp&nbsp <mod:selecao
					titulo="Gênero do Destinatário" var="genero"
					opcoes="Feminino;Masculino" />

			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo do despacho">
			<mod:grupo>
				<mod:editor titulo="" var="texto" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Nº da Portaria de delegação" var="portDelegacao"
				largura="60" />
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br />
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;">DESPACHO N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight: bold;"><br />REF. ${tipoDeDocumento} N&ordm; ${numero}, ${data} - ${orgao}.</p></td>
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
			<p><br>
			<br>
			<br>
			</p>
			<c:if test="${not empty orgao_dest}">
				<c:if test="${genero=='Feminino'}">
					<p>À ${orgao_dest},</p>
				</c:if>
				<c:if test="${genero=='Masculino'}">
					<p>Ao ${orgao_dest},</p>
				</c:if>
			</c:if>

			<span style="font-size:${tl};"> ${texto} </span>
			<center>${doc.dtExtenso}</center>
			<p>&nbsp;</p>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?textoFinal=${portDelegacao}" />

			<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

			<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</mod:letra>
		</body>
		</html>
	</mod:documento>
</mod:modelo>

