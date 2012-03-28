<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Documento">

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
	frm.assinaturaB64.value = Assinatura.Sign(Nothing, True, 0)
	If Erro Then Exit Function
	Dim Assinante
	Assinante = Assinatura.Signers(1).Certificate.SubjectName
	Assinante = Split(Assinante, "CN=")(1)
	Assinante = Split(Assinante, ",")(0)
	frm.assinante.value = Assinante
	frm.conteudo_b64.value = ""
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

	<!--<c:choose>
	<c:when test="${fechar eq true}"> 
		<c:set var="acao" value="fechar_assinar_gravar" />
	</c:when>
	<c:otherwise>-->
	<c:set var="acao" value="assinar_gravar" />
	<!--</c:otherwise>
</c:choose>-->

	<ww:form name="frm" id="frm" action="${acao}"
		namespace="/expediente/mov" theme="simple" validate="false"
		method="POST">
		<ww:hidden name="sigla" value="${sigla}" />
		<h1>
			<b>Confirme os dados do documento abaixo:</b>
		</h1>

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
						<c:if test="${doc.conteudo != ''}">
							<tr>
								<td colspan="2">
									<div id="conteudo">${doc.conteudoBlobHtmlString}</div>
								</td>
							</tr>
						</c:if>
					</table> <br /> <ww:hidden name="conteudo_b64"
						value="${doc.conteudoBlobPdfB64}" /> <ww:hidden
						name="assinaturaB64" /> <ww:hidden name="assinante" />
					<center>
						<input type="button" value="Assinar" onclick="vbscript: assinar" />
						${f:obterBotoesExtensaoAssinador(lotaTitular.orgaoUsuario)}
					</center>
				</td>
			</tr>
		</table>
	</ww:form>

	<c:set var="jspServer" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_gravar.action?sigla=${sigla}" />
    <c:set var="nextURL" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/doc/exibir.action?sigla=${sigla}"  />
    <c:set var="url_0" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/semmarcas/hashSHA1/doc/${doc.codigoCompacto}.pdf"  />
    <%-- <c:set var="url_0" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/semmarcas/doc/${doc.codigoCompacto}.pdf"  /> --%>
	${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,request.contextPath,sigla,doc.codigoCompacto,jspServer,nextURL,url_0 )}
</siga:pagina>