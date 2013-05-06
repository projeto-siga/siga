<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Despacho" popup="true">

<c:if test="${not doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
	<script language="VBScript">
Function assinar()
	Dim Assinatura
	Dim Configuracao
	On Error Resume Next
	Set Configuracao = CreateObject("CAPICOM.Settings")
	Configuracao.EnablePromptForCertificateUI = True
	Set Assinatura = CreateObject("CAPICOM.SignedData")
	Set Util = CreateObject("CAPICOM.Utilities")
	If Erro Then Exit Function
	Assinatura.Content = Util.Base64Decode(frm.conteudo_b64.value)
	frm.conteudo_b64.value = Null
	frm.assinaturaB64.value = Assinatura.Sign(Nothing, True, 0)
	If Erro Then Exit Function
	Dim Assinante
	Assinante = Assinatura.Signers(1).Certificate.SubjectName
	Assinante = Split(Assinante, "CN=")(1)
	Assinante = Split(Assinante, ",")(0)
	frm.assinante.value = Assinante
	If Erro Then Exit Function
	frm.Submit()
End Function

Function Erro() 
	If Err.Number <> 0 then
		MsgBox "Ocorreu um erro durante o processo de assinatura: " & Err.Description
		Err.Clear
		Erro = True
	Else
		Erro = False
	End If
End Function
</script>
</c:if>
	<c:if test="">
		<c:set var="" value="" />
	</c:if>
	<ww:form name="frm" action="${acao}" namespace="/expediente/mov"
		theme="simple" validate="false" method="POST">
		<ww:hidden name="sigla" value="%{sigla}" />
			
		<h1><b>Confirme os dados do despacho abaixo:</b></h1>

		<table border="0" width="100%">
			<tr>
				<td>
				<table class="message" width="100%">
					<tr class="header">
						<td width="50%"><b>Documento
						${doc.exTipoDocumento.descricao}:</b> ${doc.codigo}</td>
						<td><b>Data:</b> ${doc.dtDocDDMMYY}</td>
					</tr>
					<tr class="header">
						<td><b>De:</b> ${doc.subscritorString}</td>
						<td><b>Classificação:</b>
						${doc.exClassificacao.descricaoCompleta}</td>
					</tr>
					<tr class="header">
						<td><b>Para:</b> ${doc.destinatarioString}</td>
						<td><b>Descrição:</b> ${doc.descrDocumento}</td>
					</tr>
					<c:if
						test="${mov.conteudoBlobMov != null && mov.conteudoBlobMov != ''}">
						<tr>
							<td colspan="2">
							<div id="conteudo">${mov.conteudoBlobHtmlString}</div>
							</td>
						</tr>
					</c:if>
				</table>

				<br />
				<ww:hidden name="copia" value="${copia}" />
<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">				
				<ww:hidden name="conteudo_b64" value="${mov.conteudoBlobPdfB64}" />
				<ww:hidden name="assinaturaB64" /> <ww:hidden name="assinante" />
				<c:choose>
					<c:when test="${copia == true}">
						<input type="button" value="Conferir" onclick="vbscript: assinar" />
					</c:when>
					<c:otherwise>
						<input type="button" value="Assinar" onclick="vbscript: assinar" />
					</c:otherwise>
				</c:choose>
</c:if>				
				</td>
			</tr>
		</table>
	</ww:form>
</siga:pagina>