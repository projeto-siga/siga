<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Documento" popup="true"
			onLoad="javascript: TestarAssinaturaDigital();" compatibilidade="IE=EmulateIE9">
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
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<script language="javascript">
		function fechaJanela(){
			if (frm.fechaJanela=='sim'){
				opener.refresh();
				self.close();
			}
		}
	</script>

	<c:url var="url" value="${pageContext.request.contextPath}/app/expediente/mov/assinar_mov_gravar?id=${mov.idMov}&copia=false" />
	<c:url var="url2" value="${pageContext.request.contextPath}/app/expediente/mov/assinar_mov_gravar?id=${mov.idMov}&copia=true">
	</c:url>
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
	frm.action='<c:url value="/app/arquivo/exibir?arquivo="/>' + ('${mob.codigoCompacto}' + v + ':' + ${mov.idMov} + '.pdf').replace(/\//gi, '').replace(/-/gi, '');
	frm.submit();
	frm.target=t;
	frm.action=a;
}

</script>

	<div class="gt-bd" style="padding-bottom: 0px;">
		<div class="gt-content">

			<h2>
				Documento ${doc.exTipoDocumento.descricao}: ${doc.codigo}
			</h2>

			<form name="frm" action="exibir" theme="simple" method="post">
				<div class="gt-content-box" style="padding: 10px;">
					<table width="100%" border="0">
						<tr>
							<td>
								<table border="0" style="width: 100%;">
									<tr>
										<td>
											<c:set var="exibemov" scope="request" value="" />
											<c:set var="exibemovvariante" scope="request" value="" />
											<c:if test='${empty mov.exMovimentacaoCanceladora}'>
												<c:if test="${(doc.idDoc == mov.exDocumento.idDoc)}">
													<c:if test='${mov.exTipoMovimentacao.idTpMov == 2}'>
														<c:set var="exibemov" scope="request" value="anexacao" />
													</c:if>
												</c:if>
												<c:if test='${mov.exTipoMovimentacao.idTpMov == 12}'>
													<c:set var="exibemov" scope="request" value="juntada" />
												</c:if>
												<c:if test='${mov.exTipoMovimentacao.idTpMov == 16}'>
													<c:set var="exibemov" scope="request" value="vinculo" />
													<c:if test="${mov.exDocumento.idDoc == doc.idDoc}">
														<c:set var="exibemovvariante" scope="request" value="vinculoOriginadoAqui" />
													</c:if>
												</c:if>
												<c:if test="${(doc.idDoc == mov.exDocumento.idDoc)}">
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 5) || (mov.exTipoMovimentacao.idTpMov == 6) || (mov.exTipoMovimentacao.idTpMov == 18)}'>
														<c:set var="exibemov" scope="request" value="despacho" />
													</c:if>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 13)}'>
														<c:set var="exibemov" scope="request" value="desentranhamento" />
													</c:if>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 43)}'>
														<c:set var="exibemov" scope="request" value="encerramento" />
													</c:if>
													<c:if test='${(mov.exTipoMovimentacao.idTpMov == 14)}'>
														<c:set var="exibemov" scope="request" value="cancelamento" />
													</c:if>
												</c:if>
											</c:if> 
											<c:if test='${not empty exibemov}'>
												<table class="message" style="width: 100%;">
													<tr class="header_${exibemov}">
														<td width="50%">
															<b>${mov.descrTipoMovimentacao}</b>
														</td>
														<td>
															<b>Data:</b> ${mov.dtRegMovDDMMYY}
														</td>
													</tr>
													<tr class="header_${exibemov}">
														<td>
															<b>Responsável:</b> ${mov.subscritor.descricao}
														</td>
														<c:if test="${exibemov == 'anexacao'}">
															<c:url var='anexo' value='/app/arquivo/exibir?arquivo=${mov.nmPdf}' />
															<td>
																<b>Arquivo:</b> 
																<a class="attached" href="${anexo}" target="_blank">
																	${mov.nmArqMov}
																</a>
															</td>
														</c:if>

														<c:if test="${exibemov == 'juntada'}">
															<td>
															<b>Documento filho:</b>
															<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?id=${mov.exDocumento.idDoc}&via=${mov.numVia}">
																${mov.exDocumento.codigo}-${mov.numViaToChar}
															</a>
															</td>
														</c:if>

														<c:if test="${exibemov == 'vinculo'}">
															<td>
																<b>Ver também:</b> 
																<c:url var="url" value="${pageContext.request.contextPath}/app/expediente/doc/exibir">
																	<c:choose>
																		<c:when test="${exibemovvariante == 'vinculoOriginadoAqui'}">
																			<c:param name="id">
																				${mov.exDocumentoRef.idDoc}
																			</c:param>
																			<c:param name="via">
																				${mov.numViaDocRef}
																			</c:param>
																			<c:set var="link">
																				${mov.exDocumentoRef.codigo}-${mov.numViaDocRefToChar}
																			</c:set>
																		</c:when>
																		<c:otherwise>
																			<c:param name="id">
																				${mov.exDocumento.idDoc}
																			</c:param>
																			<c:param name="via">
																				${mov.numVia}
																			</c:param>
																			<c:set var="link">
																				${mov.exDocumento.codigo}-${mov.numViaToChar}
																			</c:set>
																		</c:otherwise>
																	</c:choose>
																</c:url>
																<a href="${url}">
																	${link}
																</a>
															</td>
														</c:if>

														<c:if test="${exibemov == 'anexacao'}">
															<tr>
																<td colspan=2>
																	<c:url var="anexo" value="/app/arquivo/exibir?arquivo=${mov.nmPdf}" /> 
																	<iframe src="${anexo}" width="100%" height="600" align="center" style="margin-top: 10px;">
																	</iframe>
																</td>
															</tr>
														</c:if>

														<c:if test="${exibemov == 'despacho' or exibemov == 'desentranhamento' or exibemov == 'encerramento' or exibemov == 'cancelamento'}">
															<td>
															
															</td>
															<tr>
																<c:choose>
																	<c:when test="${mov.conteudoTpMov == 'text/xhtml'}">
																		<td colspan="2" style="margin-top: 10px;">${mov.conteudoBlobString}</td>
																	</c:when>
																	<c:when test="${mov.conteudoTpMov == 'application/zip'}">
																		<td colspan="2" style="margin-top: 10px;">
																			<tags:fixdocumenthtml>
																				${mov.conteudoBlobHtmlString}
																			</tags:fixdocumenthtml>
																		</td>
																	</c:when>
																	<c:otherwise>
																		<td colspan="2" style="margin-top: 10px;">
																			${mov.obs}
																		</td>
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
					<h1>
						Assinaturas para essa movimentação:
					</h1>
					<div class="gt-content-box" style="padding: 0">
						<table border="0" class="gt-table">
							<thead>
								<tr>
									<th rowspan="2">
										Data
									</th>
									<th colspan="2">
										Cadastrante</th>
									<th rowspan="2">
										Descrição
									</th>
								</tr>
								<tr>
									<th>
										Lotação
									</th>
									<th>
										Pessoa
									</th>
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
									<td width="16%" align="left">
										${movReferenciadora.dtRegMovDDMMYYHHMMSS}
									</td>
									<td width="4%" align="left">
										<siga:selecionado sigla="${movReferenciadora.lotaCadastrante.sigla}" descricao="${movReferenciadora.lotaCadastrante.descricao}" />
									</td>
									<td width="4%" align="left">
										<siga:selecionado sigla="${movReferenciadora.cadastrante.iniciais}" descricao="${movReferenciadora.cadastrante.descricao}" />
									</td>
									<td width="44%">
										<tags:assinatura_mov assinante="${movReferenciadora.obs}" idmov="${movReferenciadora.idMov}" />
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
				<div style="padding-left: 10; padding-top: 10px;">
					<c:if test="${mov.exTipoMovimentacao.idTpMov!=2}">
						<input type="button" value="Visualizar Impressão" class="gt-btn-large gt-btn-left" onclick="javascript:visualizarImpressao();" />
					</c:if>
				</div>
			</form>
			<div style="padding-left: 10;">
				<div id="dados-assinatura" style="visible: hidden">
					<input type="hidden" name="pdfchk_${mov.idMov}" id="pdfchk_${mov.idMov}" value="${mov.referencia}" />
					<input type="hidden" name="urlchk_${mov.idMov}" id="urlchk_${mov.idMov}" value="/app/arquivo/exibir?arquivo=${mov.nmPdf}" />

					<c:set var="jspServer" value="${request.contextPath}/app/expediente/mov/assinar_mov_gravar" />
					<c:set var="jspServerSenha" value="${request.contextPath}/app/expediente/mov/assinar_mov_login_senha_gravar" />
					<c:set var="nextURL" value="${request.contextPath}/app/expediente/mov/fechar_popup?sigla=${mob.sigla}" />
					<c:set var="urlPath" value="${request.contextPath}" />
					
					<input type="hidden" id="jspserver" name="jspserver" value="${jspServer}" />
					<input type="hidden" id="jspServerSenha" name="jspServerSenha" value="${jspServerSenha}" />
					<input type="hidden" id="nexturl" name="nextUrl" value="${nextURL}" />
					<input type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
					<c:set var="url">${request.requestURL}</c:set>
					<c:set var="uri" value="${request.requestURI}" />
					<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
					<input type="hidden" id="urlbase" name="urlbase" value="${urlBase}" />
					
					<c:if test="${not autenticando}">
						<c:choose>
							<c:when test="${mov.exTipoMovimentacao.idTpMov == 2}">
								<c:set var="botao" value="ambos" />
							</c:when>
							<c:otherwise>
								<c:set var="botao" value="" />
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${autenticando}">
						<c:set var="botao" value="autenticando" />
					</c:if>
						
					<c:set var="lote" value="false" />
				</div>		
				<p>
					<b>Link para assinatura externa: </b>
					${enderecoAutenticacao} (informar o código ${mov.siglaAssinaturaExterna})
				</p>
				<tags:assinatura_botoes
					autenticar="${mov.exTipoMovimentacao.idTpMov==2}"
					assinarComSenha="${f:podeAssinarMovimentacaoComSenha(titular,lotaTitular,mov)}"
					autenticarComSenha="${f:podeConferirCopiaMovimentacaoComSenha(titular,lotaTitular,mov)}" 
					idMovimentacao="${mov.idMov}" />
			</div>
		</div>
	</div>

	<tags:assinatura_rodape/>
</siga:pagina>