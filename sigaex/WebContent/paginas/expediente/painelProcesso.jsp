<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--estilo do documento HTML -->
	<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>
	<link rel="StyleSheet" href="${path}/sigalibs/siga.css" type="text/css"
		title="SIGA Estilos" media="screen">
	<script src="${path}/sigalibs/ajax.js" language="JavaScript1.1" type="text/javascript"></script>
	<script src="${path}/sigalibs/static_javascript.js" language="JavaScript1.1" type="text/javascript"></script>

<link href="${pageContext.request.contextPath}/sigalibs/menu.css"
	rel="stylesheet" type="text/css" />
<!--  -->

</head>
<body>
<table class="message" width="100%">
	<tr class="${exibedoc}">
		<td width="50%"><b>Documento
		${doc.exTipoDocumento.descricao}:</b> ${doc.codigoString}</td>
		<td><b>Data:</b> ${doc.dtDocDDMMYY}</td>
	</tr>
	<tr class="${exibedoc}">
		<td><b>De:</b> ${doc.subscritorString}</td>
		<td><b>Classificação:</b>
		${doc.exClassificacao.descricaoCompleta}</td>
	</tr>
	<tr class="${exibedoc}">
		<td><b>Para:</b> ${doc.destinatarioString}</td>
		<td><b>Descrição:</b> ${doc.descrDocumento}</td>
	</tr>
	<tr class="${exibedoc}">
		<td><b>Nível de Acesso:</b> ${doc.exNivelAcesso.nmNivelAcesso}</td>
		<td><c:if test="${not empty doc.exDocumentoPai}">
			<b>Documento Pai:</b>
			<ww:url id="url" action="exibirProcesso" namespace="/expediente/doc">
				<ww:param name="sigla">${doc.exMobilPai.sigla}</ww:param>
			</ww:url>
			<!-- <ww:a href="%{url}">${doc.exDocumentoPai.codigo}-${doc.numViaDocPaiToChar}</ww:a> -->
			<ww:a href="">${doc.exDocumentoPai.codigo}-${doc.numViaDocPaiToChar}</ww:a>
		</c:if></td>
	</tr>
	<c:if test='${tipoDocumento != "interno" and not empty doc.nmArqDoc}'>
		<tr>
			<td colspan="2"><ww:url id="url" action="anexo"
				namespace="/expediente/doc">
				<ww:param name="sigla">${doc.sigla}</ww:param>
			</ww:url><tags:anexo url="${url}" nome="${doc.nmArqDoc}"
				tipo="${doc.conteudoTpDoc}" /></td>
		</tr>
	</c:if>
	<c:if test="${tipoDocumento == 'interno' && doc.conteudo != ''}">
		<tr>
			<td colspan="2"><c:choose>
				<c:when test="${empty doc.dtFechamento}">
					<c:import
						url="/paginas/expediente/modelos/${doc.exModelo.nmArqMod}?entrevista=2" />
				</c:when>
				<c:otherwise>
					<tags:fixdocumenthtml>
								${doc.conteudoBlobHtmlString}
								</tags:fixdocumenthtml>
				</c:otherwise>
			</c:choose></td>
		</tr>
	</c:if>
</table>
</body>
</html>