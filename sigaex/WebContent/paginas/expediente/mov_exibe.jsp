<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Documento" popup="true">

<c:if test="${not mob.doc.eletronico}">
	<script type="text/javascript">$("html").addClass("fisico");</script>
</c:if>

	<script language="javascript">
function fechaJanela(){
	if (frm.fechaJanela=='sim'){
		opener.refresh();
		self.close();
	}
}
</script>

	<ww:url id="url" value="assinar_mov_gravar.action?id=${mov.idMov}&copia=false"
		namespace="/expediente/mov" escapeAmp="false" >
	</ww:url>
	<ww:url id="url2" value="assinar_mov_gravar.action?id=${mov.idMov}&copia=true"
		namespace="/expediente/mov" escapeAmp="false" >
	</ww:url>
	<c:choose>
		<c:when test="${mov.exTipoMovimentacao.idTpMov==2}">
			<c:set var="msgScript" value="anexo" />
		</c:when>
		<c:when test="${mov.exTipoMovimentacao.idTpMov==13}">
			<c:set var="msgScript" value="desentranhamento" />
		</c:when>
		<c:when test="${mov.exTipoMovimentacao.idTpMov==14}">
			<c:set var="msgScript" value="cancelamento" />
		</c:when>
		<c:otherwise>
			<c:set var="msgScript" value="despacho" />
		</c:otherwise>
	</c:choose>

	<c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
		<script type="text/vbscript">
Function assinar(copia)
	prov = MsgBox("Confirma que o ${msgScript} a ser assinado foi devidamente analisado?", vbYesNo, "Confirmação")
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
        If copia  = "true" Then 
            frm.action="${url2}"
		Else 
            frm.action="${url}"
        End If
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
	</c:if>

	<script type="text/javascript" language="Javascript1.1">
function visualizarImpressao(via) {
	var v;
	var a = frm.action;
	var t = frm.target;
	if (via != null)
		v = '-' + String.fromCharCode('A'.charCodeAt(0) + via - 1);
	else
		v = ''; 
	frm.target='_blank'; 
	frm.action='<c:url value="/"/>' + ('${mob.codigoCompacto}' + v + ':' + ${mov.idMov} + '.pdf').replace(/\//gi, '').replace(/-/gi, '');
	frm.submit();
	frm.target=t;
	frm.action=a;
}

</script>

	<div class="gt-bd" style="padding-bottom: 0px;">
		<div class="gt-content">

			<h2>Documento ${doc.exTipoDocumento.descricao}: ${doc.codigo}</h2>

			<ww:form name="frm" action="exibir" namespace="/expediente/mov"
					theme="simple" method="POST">	
				<div class="gt-content-box" style="padding: 10px;">					
					<table width="100%" border="0">
						<tr>
					    	<td>
								<table border="0" style="width: 100%;">
									<tr>
										<td><c:set var="exibemov" scope="request" value="" /> 
										    <c:set var="exibemovvariante" scope="request" value="" /> 
										    <c:if  test='${empty mov.exMovimentacaoCanceladora}'>
												<%-- Esse documento e essa via --%>
												<c:if test="${(doc.idDoc == mov.exDocumento.idDoc)}">
													<%-- Anexacao --%>
													<c:if test='${mov.exTipoMovimentacao.idTpMov == 2}'>
														<c:set var="exibemov" scope="request" value="anexacao" />
													</c:if>
												</c:if>

												<%-- Juntada --%>
												<c:if test='${mov.exTipoMovimentacao.idTpMov == 12}'>
													<c:set var="exibemov" scope="request" value="juntada" />
												</c:if>

												<%-- Vinculo --%>
												<c:if test='${mov.exTipoMovimentacao.idTpMov == 16}'>
													<c:set var="exibemov" scope="request" value="vinculo" />
													<c:if test="${mov.exDocumento.idDoc == doc.idDoc}">
														<c:set var="exibemovvariante" scope="request" value="vinculoOriginadoAqui" />
													</c:if>
												</c:if>

												<c:if test="${(doc.idDoc == mov.exDocumento.idDoc)}">
													<%-- Despacho --%>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 5) || (mov.exTipoMovimentacao.idTpMov == 6) || (mov.exTipoMovimentacao.idTpMov == 18)}'>
														<c:set var="exibemov" scope="request" value="despacho" />
													</c:if>

													<%-- Desentranhamento --%>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 13)}'>
														<c:set var="exibemov" scope="request"
															value="desentranhamento" />
													</c:if>

													<%-- Encerramento --%>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 43)}'>
														<c:set var="exibemov" scope="request" value="encerramento" />
													</c:if>
													<%-- Encerramento --%>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 14)}'>
														<c:set var="exibemov" scope="request" value="cancelamento" />
													</c:if>
												</c:if>
											</c:if> <%-- fim do if empty mov.exMovimentacaoCanceladora --%>
											<c:if test='${not empty exibemov}'>
												<table class="message" style="width: 100%;">
													<tr class="header_${exibemov}">
														<td width="50%"><b>${mov.descrTipoMovimentacao}</b></td>
														<td><b>Data:</b> ${mov.dtRegMovDDMMYY}</td>
													</tr>
													<tr class="header_${exibemov}">
														<td><b>Responsável:</b> ${mov.subscritor.descricao}</td>

														<%-- TIPO_MOVIMENTACAO_ANEXACAO --%>
														<c:if test="${exibemov == 'anexacao'}">
															<c:url var='anexo' value='/anexo/${mov.idMov}/${mov.nmArqMov}' />
															<td><b>Arquivo:</b> <a class="attached"
																href="${anexo}" target="_blank">${mov.nmArqMov}</a></td>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_JUNTADA --%>
														<c:if test="${exibemov == 'juntada'}">
															<td><b>Documento filho:</b> <ww:url id="url"
																	action="exibir" namespace="/expediente/doc">
																	<ww:param name="id">${mov.exDocumento.idDoc}</ww:param>
																	<ww:param name="via">${mov.numVia}</ww:param>
																</ww:url> <ww:a href="%{url}">${mov.exDocumento.codigo}-${mov.numViaToChar}</ww:a>
															</td>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_REFERENCIA --%>
														<c:if test="${exibemov == 'vinculo'}">
															<td><b>Ver também:</b> <ww:url id="url"
																	action="exibir" namespace="/expediente/doc">
																	<c:choose>
																		<c:when
																			test="${exibemovvariante == 'vinculoOriginadoAqui'}">
																			<ww:param name="id">${mov.exDocumentoRef.idDoc}</ww:param>
																			<ww:param name="via">${mov.numViaDocRef}</ww:param>
																			<c:set var="link">${mov.exDocumentoRef.codigo}-${mov.numViaDocRefToChar}</c:set>
																		</c:when>
																		<c:otherwise>
																			<ww:param name="id">${mov.exDocumento.idDoc}</ww:param>
																			<ww:param name="via">${mov.numVia}</ww:param>
																			<c:set var="link">${mov.exDocumento.codigo}-${mov.numViaToChar}</c:set>
																		</c:otherwise>
																	</c:choose>
																</ww:url> <ww:a href="%{url}">${link}</ww:a>
															</td>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_ANEXACAO --%>
														<c:if test="${exibemov == 'anexacao'}">
															<tr>
																<td colspan=2><c:url var='anexo'
																		value='/anexo/${mov.idMov}/${mov.nmArqMov}' /> <c:url
																		var='anexo' value='/${mov.nmPdf}' /> <iframe
																		src="${anexo}" width="100%" height="600"
																		align="center" style="margin-top: 10px;"> </iframe>
																</td>
															</tr>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_DESPACHO --%>
														<c:if test="${exibemov == 'despacho' or exibemov == 'desentranhamento' or exibemov == 'encerramento' or exibemov == 'cancelamento'}">
																<td></td>
															<tr>
																<c:choose>
																	<c:when test="${mov.conteudoTpMov == 'text/xhtml'}">
																		<td colspan="2" style="margin-top: 10px;">${mov.conteudoBlobString}</td>
																	</c:when>
																	<c:when
																		test="${mov.conteudoTpMov == 'application/zip'}">
																		<td colspan="2" style="margin-top: 10px;"><tags:fixdocumenthtml>
																			${mov.conteudoBlobHtmlString}
																		</tags:fixdocumenthtml></td>
																	</c:when>
																	<c:otherwise>
																		<td colspan="2" style="margin-top: 10px;">${mov.obs}</td>
																	</c:otherwise>
																</c:choose>
															</tr>	
														</c:if>
													</tr>
												</table>
											</c:if>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>

					<c:if test="${not empty mov.exMovimentacaoReferenciadoraSet}">
						<h1>Assinaturas para essa movimentação:</h1>
						<div class="gt-content-box" style="padding:0">
							<table border="0" class="gt-table">
								<thead>
								<tr>
									<th rowspan="2">Data</th>
									<th colspan="2">Cadastrante</th>
									<th rowspan="2">Descrição</th>
								</tr>
								<tr>
									<th>Lotação</th>
									<th>Pessoa</th>
								</tr>
								</thead>
								<c:set var="evenorodd" value="odd" />

								<c:forEach var="movReferenciadora" items="${mov.exMovimentacaoReferenciadoraSet}">
									<c:choose>
										<c:when test='${evenorodd == "even"}'>
											<c:set var="evenorodd" value="odd" />
										</c:when>
										<c:otherwise>
											<c:set var="evenorodd" value="even" />
										</c:otherwise>
									</c:choose>
									<tr class="${evenorodd}">
										<td width="16%" align="left">${movReferenciadora.dtRegMovDDMMYYHHMMSS}</td>
										<td width="4%" align="left"><siga:selecionado
											sigla="${movReferenciadora.lotaCadastrante.sigla}"
											descricao="${movReferenciadora.lotaCadastrante.descricao}" /></td>
										<td width="4%" align="left"><siga:selecionado
											sigla="${movReferenciadora.cadastrante.iniciais}"
											descricao="${movReferenciadora.cadastrante.descricao}" /></td>
										<td width="44%"><tags:assinatura_mov
											assinante="${movReferenciadora.obs}"
											idmov="${movReferenciadora.idMov}" /></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:if>


					<div style="padding-left: 10; padding-top: 10px;">
						<c:if test="${mov.exTipoMovimentacao.idTpMov!=2}">
							<input type="button" value="Visualizar Impressão" class="gt-btn-large gt-btn-left" 
							       onclick="javascript:visualizarImpressao();" />						      
						</c:if>
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;VBS')}">
							<ww:hidden name="conteudo_b64" value="${mov.conteudoBlobPdfB64}" />
							<ww:hidden name="assinaturaB64" />
							<ww:hidden name="assinante" />
							<!-- Orlando: Alterei o código abaixo para mudar o nome do botão para "Assinar Transferir", quando o idTpMov==6, Despacho com Transferência. -->
							<c:choose>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==5  || mov.exTipoMovimentacao.idTpMov==18}">
									<input type="button" value="Assinar Despacho" class="gt-btn-alternate-large gt-btn-center" 
									       onclick="vbscript: assinar('false')" />								
						    	</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==6 }">
									<input type="button" value="Assinar Transferir" class="gt-btn-alternate-large gt-btn-center" 
									       onclick="vbscript: assinar('false')" />
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==13}">
									<input type="button" value="Assinar Desentranhamento" class="gt-btn-alternate-large gt-btn-center" 
									       onclick="vbscript: assinar('false')" />
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==43}">
									<input type="button" value="Assinar Encerramento" class="gt-btn-alternate-large gt-btn-center" 
									       onclick="vbscript: assinar('false')" />
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==2}">							
									<input type="button" value="Conferir Cópia" class="gt-btn-alternate-large gt-btn-left"
											onclick="vbscript: assinar('true')" />								
									<input type="button" value="Assinar Anexo"  class="gt-btn-alternate-large gt-btn-left"
							        	    onclick="vbscript: assinar('false')" />										
								</c:when>						
							</c:choose>
						</c:if>			
					</div>		
			</ww:form>			
			
			<div style="padding-left: 10;>		
    			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;EXT:Extensão')}">	
	  				<ww:hidden name="pdfchk_${mov.idMov}" value="${mov.referencia}" />
	    			<ww:hidden name="urlchk_${mov.idMov}" value="${mov.nmPdf}" />
	   				<c:set var="jspServer" 
		    	           value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
	    			<c:set var="nextURL"
		    				value="${request.scheme}://${request.serverName}:${request.localPort}/${request.contextPath}/expediente/mov/fechar_popup.action?sigla=${mob.sigla}" />
	    			<c:set var="urlPath" value="/${request.contextPath}" />		
	    			<ww:if test="${mov.exTipoMovimentacao.idTpMov==2}">
	        			<c:set var="botao" value="ambos"/>
	   				</ww:if>    
	    			<ww:else>
	        			<c:set var="botao" value=""/>
	   				</ww:else>
	    			<c:set var="lote" value="false"/>		
	    			${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.localPort,urlPath,jspServer,nextURL,botao,lote)}
			    </c:if>			
    		</div>
		</div>
	</div>
</siga:pagina>