<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Movimentação">

	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			ExMovimentacaoForm.page.value = '';
			ExMovimentacaoForm.acao.value = 'aAnexar';
			ExMovimentacaoForm.submit();
		}

		function testpdf(x) {
			padrao = /\.pdf/;
			a = x.arquivo.value;
			OK = padrao.exec(a);
			if (a != '' && !OK) {
				window.alert("Somente é permitido anexar arquivo PDF!");
				x.arquivo.value = '';
				x.arquivo.focus();
			}
		}
	</script>
	
	<ww:url id="url" action="assinar_mov_gravar_lote"
		namespace="/expediente/mov">
		<ww:param name="id">${mobilVO.mob.id}</ww:param>
	</ww:url>
	<script language="VBScript">
Function assinar()
	prov = MsgBox("Confirma que o Anexo a ser assinado foi devidamente analisado?", vbYesNo, "Confirmação")
	If prov = vbYes then
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
		frm.action="<ww:property value="%{url}"/>"
		frm.Submit()
	End If

	
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

	<table width="100%">
		<tr>
			<td><ww:form action="anexar_gravar" namespace="/expediente/mov"
					method="POST" enctype="multipart/form-data" cssClass="form">

					<input type="hidden" name="postback" value="1" />
					<ww:hidden name="sigla" value="%{sigla}" />

					<h1>
						Anexação de Arquivo - ${doc.codigo}
						<c:if test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if>
					</h1>

					<tr class="header">
						<td colspan="2">Dados do Arquivo</td>
					</tr>
					<ww:textfield name="dtMovString" label="Data"
						onblur="javascript:verifica_data(this, true);" />

					<tr>
						<td>Responsável:</td>
						<td><siga:selecao tema="simple" propriedade="subscritor" />
							&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
								onclick="javascript:displayTitular(this);" />Substituto</td>
					</tr>
					<c:choose>
						<c:when test="${!substituicao}">
							<tr id="tr_titular" style="display: none">
						</c:when>
						<c:otherwise>
							<tr id="tr_titular" style="">
						</c:otherwise>
					</c:choose>

					<td>Titular:</td>
					<input type="hidden" name="campos" value="titularSel.id" />
					<td colspan="3"><siga:selecao propriedade="titular"
							tema="simple" />
					</td>
		</tr>

		<ww:textfield name="descrMov" label="Descrição" maxlength="80" size="80" />

		<ww:file name="arquivo" label="Arquivo" accept="application/pdf"
			onchange="testpdf(this.form)" />

		<!-- ww:datepicker tooltip="Select Your Birthday" label="Birthday"
				name="birthday" / -->
		<ww:submit value="Ok" cssClass="button" align="center" />
		<!--  			<tr class="button"> teste
				<td><ww:submit type="input" value="Cancela" theme="simple"
					onclick="javascript:history.back(-1);" /></td>
			</tr> 
-->
		</ww:form>
		</td>
		</tr>
	</table>
	
	<c:if test="${(not empty mobilVO.movs)}">
	
		<table border="0" cellpadding="0" cellspacing="0" width="80%" >
			<tr>
				<h1 colspan="4" > Anexos Pendentes de Assinatura </h1>
			</tr> 
		</table>
		<table class="mov" width="80%">		
			<tr class="${docVO.classe}">
				<td align="center" rowspan="2">Data</td>			
				<td colspan="2" align="left">Cadastrante</td>			
				<td colspan="2" align="left">Atendente</td>
				<td rowspan="2">Descrição</td>			
			</tr>
			<tr class="${docVO.classe}">
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>			
				<td align="left">Lotação</td>
				<td align="left">Pessoa</td>
			</tr>
		    <c:forEach var="mov" items="${mobilVO.movs}">
				<c:if test="${(not mov.cancelada)}">
					<tr class="${mov.classe} ${mov.disabled}">				   
						<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />							
						<ww:if test="${dt == dtUlt}">
							<c:set var="dt" value="" />
						</ww:if>
						<ww:else>
							<c:set var="dtUlt" value="${dt}" />
						</ww:else>
		
						<td align="center">${dt}</td>				
						<td align="left"><siga:selecionado
							sigla="${mov.parte.lotaCadastrante.sigla}"
							descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.cadastrante.nomeAbreviado}"
							descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" /></td>				
						<td align="left"><siga:selecionado
							sigla="${mov.parte.lotaResp.sigla}"
							descricao="${mov.parte.lotaResp.descricaoAmpliada}" /></td>
						<td align="left"><siga:selecionado
							sigla="${mov.parte.resp.nomeAbreviado}"
							descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" /></td>
						<td>${mov.descricao}<c:if test='${mov.idTpMov != 2}'> ${mov.complemento}</c:if>
						<c:set var="assinadopor" value="${true}" /> <siga:links
							inline="${true}"
							separator="${not empty mov.descricao and mov.descricao != null}">
							<c:forEach var="acao" items="${mov.acoes}">
								<c:choose>
									<c:when test='${mov.idTpMov == 32}'>
										<ww:url id="url" value="${acao.nameSpace}/${acao.acao}">
											<c:forEach var="p" items="${acao.params}">
												<ww:param name="${p.key}">${p.value}</ww:param>
											</c:forEach>
										</ww:url>
									</c:when>
									<c:otherwise>
										<ww:url id="url" action="${acao.acao}"
											namespace="${acao.nameSpace}">
											<c:forEach var="p" items="${acao.params}">
												<ww:param name="${p.key}">${p.value}</ww:param>
											</c:forEach>
										</ww:url>
									</c:otherwise>
								</c:choose>
								<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
									pos="${acao.pos}" url="${url}" test="${true}"
									popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
									ajax="${acao.ajax}" idAjax="${mov.idMov}" />
								<c:if test='${assinadopor and mov.idTpMov == 2}'> ${mov.complemento}
									<c:set var="assinadopor" value="${false}" />
								</c:if>
							</c:forEach>
						</siga:links></td>			
					</tr>			
				</c:if>
			</c:forEach>
		
		</table>
	</c:if>
	<ww:hidden name="conteudo_b64" value="${mov.conteudoBlobPdfB64}" />
	<ww:hidden name="assinaturaB64" />
			<ww:hidden name="assinante" />
	
	<ww:url id="url" action="exibir" namespace="/expediente/doc">
		<ww:param name="sigla" value="%{sigla}" />
	</ww:url>
	<siga:link title="Voltar" url="${url}" test="${true}" />

  <!--   ${f:obterBotoesExtensaoAssinador(lotaTitular.orgaoUsuario)}   -->
  <input type="button" value="Assinar Todos"
							onclick="vbscript:assinar" />
    
<%--    <c:set var="jspServer" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar_lote.action?id=${mobilVO.mob.id}&copia=${copia}"/>
	<c:set var="nextURL" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/fechar_popup.action?sigla=${mob.sigla}" />
    <c:set var="url_0" value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/semmarcas/hashSHA1/${mov.nmPdf}" />
   
    
	${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,request.contextPath,sigla,doc.codigoCompacto,jspServer,nextURL,url_0 )}
 --%>
</siga:pagina>