<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Documento" popup="true"
			onLoad="javascript: TestarAssinaturaDigital();"
			incluirJs="sigaex/javascript/assinatura.js">

	<script type="text/javascript" language="Javascript1.1">
		/*  converte para maiúscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
	</script>

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

	<ww:url id="url"
		value="assinar_mov_gravar.action?id=${mov.idMov}&copia=false"
		namespace="/expediente/mov" escapeAmp="false">
	</ww:url>
	<ww:url id="url2"
		value="assinar_mov_gravar.action?id=${mov.idMov}&copia=true"
		namespace="/expediente/mov" escapeAmp="false">
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
	frm.action='<c:url value="/arquivo/exibir.action?arquivo="/>' + ('${mob.codigoCompacto}' + v + ':' + ${mov.idMov} + '.pdf').replace(/\//gi, '').replace(/-/gi, '');
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
										<td><c:set var="exibemov" scope="request" value="" /> <c:set
												var="exibemovvariante" scope="request" value="" /> <c:if
												test='${empty mov.exMovimentacaoCanceladora}'>
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
														<c:set var="exibemovvariante" scope="request"
															value="vinculoOriginadoAqui" />
													</c:if>
												</c:if>

												<c:if test="${(doc.idDoc == mov.exDocumento.idDoc)}">
													<%-- Despacho --%>
													<c:if
														test='${(mov.exTipoMovimentacao.idTpMov == 5) || (mov.exTipoMovimentacao.idTpMov == 6) || (mov.exTipoMovimentacao.idTpMov == 18)}'>
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
											</c:if> <%-- fim do if empty mov.exMovimentacaoCanceladora --%> <c:if
												test='${not empty exibemov}'>
												<table class="message" style="width: 100%;">
													<tr class="header_${exibemov}">
														<td width="50%"><b>${mov.descrTipoMovimentacao}</b>
														</td>
														<td><b>Data:</b> ${mov.dtRegMovDDMMYY}</td>
													</tr>
													<tr class="header_${exibemov}">
														<td><b>Responsável:</b> ${mov.subscritor.descricao}</td>

														<%-- TIPO_MOVIMENTACAO_ANEXACAO --%>
														<c:if test="${exibemov == 'anexacao'}">
															<c:url var='anexo'
																value='/arquivo/exibir.action?arquivo=${mov.nmPdf}' />
															<td><b>Arquivo:</b> <a class="attached"
																href="${anexo}" target="_blank">${mov.nmArqMov}</a>
															</td>
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
																</ww:url> <ww:a href="%{url}">${link}</ww:a></td>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_ANEXACAO --%>
														<c:if test="${exibemov == 'anexacao'}">
															<tr>
																<td colspan=2><c:url var="anexo"
																		value="/arquivo/exibir.action?arquivo=${mov.nmPdf}" /> <iframe
																		src="${anexo}" width="100%" height="600"
																		align="center" style="margin-top: 10px;"> </iframe></td>
															</tr>
														</c:if>

														<%-- TIPO_MOVIMENTACAO_DESPACHO --%>
														<c:if
															test="${exibemov == 'despacho' or exibemov == 'desentranhamento' or exibemov == 'encerramento' or exibemov == 'cancelamento'}">
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
																		</tags:fixdocumenthtml>
																		</td>
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
								</table></td>
						</tr>
					</table>
				</div>

				<c:if test="${not empty mov.exMovimentacaoReferenciadoraSet}">
					<h1>Assinaturas para essa movimentação:</h1>
					<div class="gt-content-box" style="padding: 0">
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

							<c:forEach var="movReferenciadora"
								items="${mov.exMovimentacaoReferenciadoraSet}">
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
											descricao="${movReferenciadora.lotaCadastrante.descricao}" />
									</td>
									<td width="4%" align="left"><siga:selecionado
											sigla="${movReferenciadora.cadastrante.iniciais}"
											descricao="${movReferenciadora.cadastrante.descricao}" />
									</td>
									<td width="44%"><tags:assinatura_mov
											assinante="${movReferenciadora.obs}"
											idmov="${movReferenciadora.idMov}" />
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>


				<div style="padding-left: 10; padding-top: 10px;">
					<c:if test="${mov.exTipoMovimentacao.idTpMov!=2}">
						<input type="button" value="Visualizar Impressão"
							class="gt-btn-large gt-btn-left"
							onclick="javascript:visualizarImpressao();" />
					</c:if>
				</div>
			</ww:form>
			<div style="padding-left: 10;">
				<div id="dados-assinatura" style="visible: hidden">
					<ww:hidden name="pdfchk_${mov.idMov}" value="${mov.referencia}" />
					<ww:hidden name="urlchk_${mov.idMov}" value="/arquivo/exibir.action?arquivo=${mov.nmPdf}" />

					<c:set var="jspServer"
						value="${request.contextPath}/expediente/mov/assinar_mov_gravar.action" />
					<c:set var="jspServerSenha"
					    value="${request.contextPath}/expediente/mov/assinar_mov_login_senha_gravar.action" />
					<c:set var="nextURL"
						value="${request.contextPath}/expediente/mov/fechar_popup.action?sigla=${mob.sigla}" />
					<c:set var="urlPath" value="${request.contextPath}" />

					<ww:hidden id="jspserver" name="jspserver" value="${jspServer}" />
					<ww:hidden id="jspServerSenha" name="jspServerSenha" value="${jspServerSenha}" />
					<ww:hidden id="nexturl" name="nextUrl" value="${nextURL}" />
					<ww:hidden id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="req" value="${pageContext.request}" />
					<c:set var="url">${req.requestURL}</c:set>
					<c:set var="uri" value="${req.requestURI}" />
					<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
					<ww:hidden id="urlbase" name="urlbase" value="${urlBase}" />

					<c:if test="${not autenticando}">
						<ww:if test="${mov.exTipoMovimentacao.idTpMov==2}">
							<c:set var="botao" value="ambos" />
						</ww:if>
						<ww:else>
							<c:set var="botao" value="" />
						</ww:else>
					</c:if>
					<c:if test="${autenticando}">
						<c:set var="botao" value="autenticando" />
					</c:if>

					<c:set var="lote" value="false" />
				</div>
				<p><b>Link para assinatura externa: </b>${enderecoAutenticacao} (informar o código ${mov.siglaAssinaturaExterna})</p>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
					<c:import url="/paginas/expediente/inc_assina_js.jsp" />
					<div id="capicom-div">
						<c:if
							test="${not autenticando}">
							<c:choose>
								<c:when
									test="${mov.exTipoMovimentacao.idTpMov==5  || mov.exTipoMovimentacao.idTpMov==18}">
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar Despacho</a>
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==6 }">
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar Transferir</a>
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==13}">
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar Desentranhamento</a>
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==43}">
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar Encerramento</a>
								</c:when>
								<c:when test="${mov.exTipoMovimentacao.idTpMov==2}">
									<a id="bot-conferir" href="#" onclick="javascript: AssinarDocumentos('true', this);" class="gt-btn-alternate-large gt-btn-left">Autenticar</a>
									<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar Anexo</a>
								</c:when>
							</c:choose>
						</c:if>
						<c:if
							test="${autenticando}">
							<a id="bot-conferir" href="#" onclick="javascript: AssinarDocumentos('true', this);" class="gt-btn-alternate-large gt-btn-left">Autenticar Documento</a>
						</c:if>
					</div>
					<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
					<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://code.google.com/p/projeto-siga/downloads/detail?name=Capicom.zip&can=2&q=#makechanges">aqui</a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
				<script type="text/javascript">
					 if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
						 document.getElementById("capicom-div").style.display = "block";
						 document.getElementById("ie-missing").style.display = "none";
					} else {
						 document.getElementById("capicom-div").style.display = "none";
						 document.getElementById("ie-missing").style.display = "block";
					}
				 </script>
				</c:if>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
	    			${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.serverPort,urlPath,jspServer,nextURL,botao,lote)}
			    </c:if>
			</div>
		</div>
	</div>

	<c:if test="${not autenticando}">
		<c:set var="podeAssinarMovimentacaoComSenha" value="${f:podeAssinarMovimentacaoComSenha(titular,lotaTitular,mov)}" />
		<c:set var="podeConferirCopiaMovimentacaoComSenha" value="${f:podeConferirCopiaMovimentacaoComSenha(titular,lotaTitular,mov)}" />

		<c:if test="${podeAssinarMovimentacaoComSenha || podeConferirCopiaMovimentacaoComSenha}">
			<a id="bot-assinar-senha" href="#" onclick="javascript: assinarComSenha();" class="gt-btn-large gt-btn-left">Assinar/Conferir com Senha</a>

			<div id="dialog-form" title="Assinar com Senha">
	 			<form id="form-assinarSenha" method="post" action="/sigaex/expediente/mov/assinar_mov_login_senha_gravar.action" >
	 				<ww:hidden id="id" name="id" value="${mov.idMov}" />
	 				<ww:hidden id="tipoAssinaturaMov" name="tipoAssinaturaMov" value="A" />
	    			<fieldset>
	    			  <label>Matrícula</label> <br/>
	    			  <input id="nomeUsuarioSubscritor" type="text" name="nomeUsuarioSubscritor" class="text ui-widget-content ui-corner-all" onblur="javascript:converteUsuario(this)"/><br/><br/>
	    			  <label>Senha</label> <br/>
	    			  <input type="password" id="senhaUsuarioSubscritor" name="senhaUsuarioSubscritor"  class="text ui-widget-content ui-corner-all" autocomplete="off"/>
	    			</fieldset>
	  			</form>
			</div>

			 <script>
			    dialog = $("#dialog-form").dialog({
			      autoOpen: false,
			      height: 210,
			      width: 350,
			      modal: true,
			      buttons: {
			    	  <c:if test="${podeAssinarMovimentacaoComSenha}">
			          	"Assinar": assinarGravar,
			          </c:if>
			    	  <c:if test="${podeConferirCopiaMovimentacaoComSenha}">
				          "Autenticar": conferirCopiaGravar,
			          </c:if>
			          "Cancelar": function() {
			            dialog.dialog( "close" );
			          }
			      },
			      close: function() {

			      }
			    });

			    function assinarComSenha() {
			       dialog.dialog( "open" );
			    }

			    function assinarGravar() {
			    	AssinarDocumentosSenha('false', this);
				}

			    function conferirCopiaGravar() {
			    	AssinarDocumentosSenha('true', this);
				}
			  </script>
		</c:if>
	</c:if>
</siga:pagina>
