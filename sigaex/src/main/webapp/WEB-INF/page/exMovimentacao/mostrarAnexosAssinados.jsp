<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${(not empty mobilVO.movs)}">
	    <h2>Anexos Assinados</h2>
		<div class="gt-content-box gt-for-table">
		    <form name="frm_anexo" id="frm_anexo" class="form">
				<input type="hidden" name="popup" value="true" />
				<input type="hidden" name="copia" id="copia" value="false" />
				<table class="gt-table mov">
				    <thead>
					    <tr>
								<td></td>
								<th align="center" rowspan="2">&nbsp;&nbsp;&nbsp;Data</th>
								<th colspan="2" align="center">Cadastrante</th>
								<th colspan="2" align="center">Atendente</th>
								<th rowspan="2">Descrição</th>
							</tr>
							<tr>
								<c:choose>
								    <c:when test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
										<td align="center"><input type="checkbox" name="checkall"
											onclick="checkUncheckAll(this)" /></td>
									</c:when>
									<c:otherwise><td></td></c:otherwise>
								</c:choose>
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
							</tr>
						<thead>
	     				<c:set var="i" value="${0}" />
						<c:forEach var="mov" items="${mobilVO.movs}">
							<c:if test="${(not mov.cancelada)}">
								<tr class="${mov.classe} ${mov.disabled}">
									<c:set var="dt" value="${mov.dtRegMovDDMMYY}" />
									<c:choose>
										<c:when test="${dt == dtUlt}">
											<c:set var="dt" value="" />
										</c:when>
										<c:otherwise>
											<c:set var="dtUlt" value="${dt}" />
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
											<c:set var="x" scope="request">chk_${mov.mov.idMov}</c:set>
											<c:remove var="x_checked" scope="request" />
											<c:if test="${param[x] == 'true'}">
												<c:set var="x_checked" scope="request">checked</c:set>
											</c:if>
											<td align="center"><input type="checkbox" name="${x}"
												value="true" ${x_checked} /></td>
										</c:when>
										<c:otherwise><td></td></c:otherwise>
									</c:choose>
									<td align="center">${dt}</td>
									<td align="left"><siga:selecionado
											sigla="${mov.parte.lotaCadastrante.sigla}"
											descricao="${mov.parte.lotaCadastrante.descricaoAmpliada}" />
									</td>
									<td align="left"><siga:selecionado
											sigla="${mov.parte.cadastrante.nomeAbreviado}"
											descricao="${mov.parte.cadastrante.descricao} - ${mov.parte.cadastrante.sigla}" />
									</td>
									<td align="left"><siga:selecionado
											sigla="${mov.parte.lotaResp.sigla}"
											descricao="${mov.parte.lotaResp.descricaoAmpliada}" /></td>
									<td align="left"><siga:selecionado
											sigla="${mov.parte.resp.nomeAbreviado}"
											descricao="${mov.parte.resp.descricao} - ${mov.parte.resp.sigla}" />
									</td>
									<td>${mov.descricao}<c:if test='${mov.idTpMov != 2}'> ${mov.complemento}</c:if>
										<c:set var="assinadopor" value="${true}" /> <siga:links
											inline="${true}"
											separator="${not empty mov.descricao and mov.descricao != null}">
										<c:forEach var="acao" items="${mov.acoes}">
											<c:choose>
												<c:when test='${mov.idTpMov == 32}'>
													<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
														<c:forEach var="p" items="${acao.params}">
															<c:param name="${p.key}" value="${p.value}"/>
														</c:forEach>
													</c:url>
												</c:when>
												<c:otherwise>
													<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
														<c:forEach var="p" items="${acao.params}">
															<c:param name="${p.key}" value="${p.value}"/>
														</c:forEach>
													</c:url>
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
										<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
											<input type="hidden" name="pdf${x}" value="${mov.mov.referencia}" />
											<input type="hidden" name="url${x}" value="${mov.mov.nmPdf}" />
										</c:if>
									</siga:links></td>
								</tr>
							</c:if>
	     				</c:forEach>
				</table>
			</form>
	    </div>
		<div id="dados-assinatura" style="visible: hidden">
	    <c:set var="jspServer" value="${request.contextPath}/app/expediente/mov/assinar_mov_gravar" />
			<c:set var="nextURL" value="${request.contextPath}/app/expediente/doc/atualizar_marcas?sigla=${mobilVO.sigla}" />
		  <c:set var="urlPath" value="${request.contextPath}" />

	   	<input type="hidden" id="jspserver" name="jspserver" value="${jspServer}" />
			<input type="hidden" id="nexturl" name="nextUrl" value="${nextURL}" />
			<input type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
			<c:set var="url">${request.requestURL}</c:set>
			<c:set var="uri" value="${request.requestURI}" />
			<c:set var="urlBase" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
			<input type="hidden" id="urlbase" name="urlbase" value="${urlBase}" />

		    <c:set var="botao" value="ambos" />
		    <c:set var="lote" value="true" />
		</div>
		<c:if
			test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
				<div id="capicom-div">
					<a id="bot-conferir" href="#" onclick="javascript: AssinarDocumentos('true', this);" class="gt-btn-alternate-large gt-btn-left">Autenticar em Lote</a>
					<a id="bot-assinar" href="#" onclick="javascript: AssinarDocumentos('false', this);" class="gt-btn-alternate-large gt-btn-left">Assinar em Lote</a>
				</div>
			<p id="ie-missing" style="display: none;">A assinatura digital utilizando padrão do SIGA-DOC só poderá ser realizada no Internet Explorer. No navegador atual, apenas a assinatura com <i>Applet Java</i> é permitida.</p>
			<p id="capicom-missing" style="display: none;">Não foi possível localizar o componente <i>CAPICOM.DLL</i>. Para realizar assinaturas digitais utilizando o método padrão do SIGA-DOC, será necessário instalar este componente. O <i>download</i> pode ser realizado clicando <a href="https://drive.google.com/file/d/0B_WTuFAmL6ZERGhIczRBS0ZMaVE/view">aqui</a>. Será necessário expandir o <i>ZIP</i> e depois executar o arquivo de instalação.</p>
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

		<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
		    ${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.serverPort,urlPath,jspServer,nextURL,botao,lote)}
		</c:if>
	</c:when>
	<c:otherwise>
			<b>Não há anexos assinados</b>
	</c:otherwise>
</c:choose>
