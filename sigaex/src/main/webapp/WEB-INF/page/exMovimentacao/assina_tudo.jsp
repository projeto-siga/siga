<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina
	titulo="Assinatura em Lote de Documentos, Despachos e Anexos"
	onLoad="javascript: TestarAssinaturaDigital();"
	incluirJs="/sigaex/javascript/assinatura.js">

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

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">
		$(document).ready(
				function() {
					$("#checkall-assinar").change(function() {
						var checked = $(this).prop("checked");
						$("input:checkbox.chk-assinar").each(function() {
							$(this).prop('checked', checked);
							$(this).trigger("change");
						});
					});
					$("#checkall-autenticar").change(function() {
						var checked = $(this).prop("checked");
						$("input:checkbox.chk-autenticar").each(function() {
							$(this).prop('checked', checked);
							$(this).trigger("change");
						});
					});
					$("#checkall-senha").change(
							function() {
								$("input:checkbox.chk-senha").prop('checked',
										$(this).prop("checked"));
							});
					$("input:checkbox.chk-assinar").change(
							function() {
								if ($(this).prop("checked"))
									$(
											"#" + $(this).attr("id")
													+ ".chk-autenticar").prop(
											'checked', false);
							});
					$("input:checkbox.chk-autenticar")
							.change(
									function() {
										if ($(this).prop("checked"))
											$(
													"#" + $(this).attr("id")
															+ ".chk-assinar")
													.prop('checked', false);
									});
				});

		function displaySel(chk, el) {
			document.getElementById('div_' + el).style.display = chk.checked ? ''
					: 'none';
			if (chk.checked == -2)
				document.getElementById(el).focus();
		}

		function displayTxt(sel, el) {
			document.getElementById('div_' + el).style.display = sel.value == -1 ? ''
					: 'none';
			document.getElementById(el).focus();
		}
	</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Assinatura em Lote de Documentos, Despachos e Anexos</h2>
			<form name="frm" id="frm" cssClass="form" theme="simple">

				<input type="hidden" name="postback" value="1" />
				<div class="gt-content-box gt-for-table">
					<table class="gt-form-table">
						<tr class="header">
							<td>Assinatura</td>
						</tr>
						<tr class="button">
							<td>
								<div id="dados-assinatura" style="visible: hidden">
									<c:set var="jspServer"
										value="${request.contextPath}/app/expediente/mov/assinar_gravar" />
									<c:set var="jspServerSenha"
										value="${request.contextPath}/app/expediente/mov/assinar_senha_gravar" />
									<c:set var="nextURL" value="/siga/principal.action" />
									<c:set var="urlPath" value="${request.contextPath}" />

									<input type="hidden" id="jspserver" name="jspserver"
										value="${jspServer}" /> <input type="hidden"
										id="jspServerSenha" name="jspServerSenha"
										value="${jspServerSenha}" /> <input type="hidden"
										id="nexturl" name="nextUrl" value="${nextURL}" /> <input
										type="hidden" id="urlpath" name="urlpath" value="${urlPath}" />
									<c:set var="url">${request.requestURL}</c:set>
									<c:set var="uri" value="${request.requestURI}" />
									<c:set var="urlBase"
										value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />
									<input type="hidden" id="urlbase" name="urlbase"
										value="${urlBase}" />

									<c:set var="botao" value="" />
									<c:if test="${autenticando}">
										<c:set var="botao" value="autenticando" />
									</c:if>
									<c:set var="lote" value="true" />
								</div> <c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}">
									<c:import url="/javascript/inc_assina_js.jsp" />
									<div id="capicom-div">
										<c:if test="${not autenticando}">
											<a id="bot-assinar" href="#"
												onclick="javascript: AssinarDocumentos('false', this);"
												class="gt-btn-alternate-large gt-btn-left">Assinar
												Documento</a>
										</c:if>
										<c:if test="${autenticando}">
											<a id="bot-conferir" href="#"
												onclick="javascript: AssinarDocumentos('true', this);"
												class="gt-btn-alternate-large gt-btn-left">Autenticar
												Documento</a>
										</c:if>
									</div>
									<p id="ie-missing" style="display: none;">
										A assinatura digital utilizando padrão do SIGA-DOC só poderá
										ser realizada no Internet Explorer. No navegador atual, apenas
										a assinatura com <i>Applet Java</i> é permitida.
									</p>
									<p id="capicom-missing" style="display: none;">
										Não foi possível localizar o componente <i>CAPICOM.DLL</i>.
										Para realizar assinaturas digitais utilizando o método padrão
										do SIGA-DOC, será necessário instalar este componente. O <i>download</i>
										pode ser realizado clicando <a
											href="https://drive.google.com/file/d/0B_WTuFAmL6ZERGhIczRBS0ZMaVE/view"><u>aqui</u></a>.
										Será necessário expandir o <i>ZIP</i> e depois executar o
										arquivo de instalação.
									</p>
									<script type="text/javascript">
										if (window.navigator.userAgent
												.indexOf("MSIE ") > 0
												|| window.navigator.userAgent
														.indexOf(" rv:11.0") > 0) {
											document
													.getElementById("capicom-div").style.display = "block";
											document
													.getElementById("ie-missing").style.display = "none";
										} else {
											document
													.getElementById("capicom-div").style.display = "none";
											document
													.getElementById("ie-missing").style.display = "block";
										}
									</script>

								</c:if> <c:if
									test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">		    
			   		${f:obterExtensaoAssinador(lotaTitular.orgaoUsuario,request.scheme,request.serverName,request.serverPort,urlPath,jspServer,nextURL,botao,lote)}	
	         	</c:if> <c:if
									test="${(not empty documentosQuePodemSerAssinadosComSenha)}">
									<a id="bot-assinar-senha" href="#"
										onclick="javascript: assinarComSenha();"
										class="gt-btn-large gt-btn-left">Assinar com Senha</a>
								</c:if>
							</td>
						</tr>
					</table>
				</div>
				<br />


				<!-- Assinaveis -->




				<c:if test="${(not empty assinaveis)}">
					<h2>Assinaveis</h2>
					<div class="gt-content-box gt-for-table">
						<table class="gt-table">
							<thead>
								<tr>
									<th width="3%" style="text-align: center">Assi&shy;nar</th>
									<th width="3%" style="text-align: center">Auten&shy;ticar</th>
									<th width="3%" style="text-align: center">Com Senha</th>
									<th width="13%" rowspan="2">Número</th>
									<th rowspan="2" style="text-align: center">Data</th>
									<th width="15%" colspan="2" style="text-align: center">Cadastrante</th>
									<th width="15%" rowspan="2" align="center">Tipo</th>
									<th width="49%" rowspan="2" align="left">Descrição</th>
								</tr>
								<tr>

									<th style="text-align: center"><input type="checkbox"
										id="checkall-assinar" /></th>
									<th style="text-align: center"><input type="checkbox"
										id="checkall-autenticar" /></th>
									<th style="text-align: center"><input type="checkbox"
										id="checkall-senha" /></th>
									<th style="text-align: center">Pessoa</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="assdoc" items="${assinaveis}">
									<c:set var="x" scope="request">chk_${assdoc.doc.idDoc}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<c:set var="podeAssinarComSenha" value="${assdoc.podeSenha}" />
									<c:set var="classAssinarComSenha"
										value="nao-pode-assinar-senha" />
									<c:if test="${podeAssinarComSenha}">
										<c:set var="classAssinarComSenha" value="pode-assinar-senha" />
									</c:if>
									<tr class="even">

										<td style="text-align: center"><c:if
												test="${assdoc.podeAssinar}">
												<input type="checkbox" name="${x}" value="true" ${x_checked}
													class="chk-assinar" />
											</c:if></td>
										<td style="text-align: center"></td>
										<td style="text-align: center"><c:if
												test="${podeAssinarComSenha}">
												<input type="checkbox" name="${x}" value="true" ${x_checked}
													class="chk-senha" />
											</c:if></td>
										<td><a target="_blank"
											href="/sigaex/app/expediente/doc/exibir?sigla=${assdoc.doc.sigla}">${assdoc.doc.codigo}</a>
										</td>
										<td style="text-align: center">${assdoc.doc.dtDocDDMMYY}</td>
										<td style="text-align: center">${assdoc.doc.lotaCadastrante.siglaLotacao}</td>
										<td style="text-align: center">${assdoc.doc.cadastrante.sigla}</td>
										<td>${assdoc.doc.descrFormaDoc}</td>
										<td>${assdoc.doc.descrDocumento}</td>
									</tr>
									<input type="hidden" name="pdf${x}" value="${assdoc.doc.sigla}" />
									<input type="hidden" name="url${x}"
										value="/app/arquivo/exibir?arquivo=${assdoc.doc.codigoCompacto}.pdf" />


									<c:forEach var="assmov" items="${assdoc.movs}">
										<c:set var="x" scope="request">chk_${assmov.mov.idMov}</c:set>
										<c:remove var="x_checked" scope="request" />
										<c:if test="${param[x] == 'true'}">
											<c:set var="x_checked" scope="request">checked</c:set>
										</c:if>
										<c:set var="podeAssinarComSenha" value="${assmov.podeSenha}" />
										<c:set var="classAssinarComSenha"
											value="nao-pode-assinar-senha" />
										<c:if test="${podeAssinarComSenha}">
											<c:set var="classAssinarComSenha" value="pode-assinar-senha" />
										</c:if>
										<tr class="even">
											<td style="text-align: center"><input type="checkbox"
												id="${x}" name="${x}" value="true" ${x_checked}
												class="chk-assinar" /></td>
											<td style="text-align: center"><c:if
													test="${assmov.podeAutenticar}">
													<input type="checkbox" name="${x}" id="${x}" value="true"
														${x_checked} class="chk-autenticar" />
												</c:if></td>
											<td style="text-align: center"><c:if
													test="${podeAssinarComSenha}">
													<input type="checkbox" name="${x}" id="${x}" value="true"
														${x_checked} class="chk-senha" />
												</c:if></td>
											<td><a style="padding-left: 2em;" target="_blank"
												href="/sigaex/app/arquivo/exibir?popup=true&id=688&arquivo=${assmov.mov.nmPdf}">${assmov.mov.referencia}</a>
											</td>
											<td style="text-align: center">${assmov.mov.dtRegMovDDMMYY}</td>
											<td style="text-align: center">${assmov.mov.lotaCadastrante.siglaLotacao}</td>
											<td style="text-align: center">${assmov.mov.cadastrante.sigla}</td>
											<td>${assmov.mov.exTipoMovimentacao.sigla}</td>
											<td>${assmov.mov.obs}</td>
										</tr>
										<input type="hidden" name="pdf${x}"
											value="${assmov.mov.referencia}" />
										<input type="hidden" name="url${x}"
											value="/app/arquivo/exibir?arquivo=${assmov.mov.referencia}.pdf" />
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				</
				<form>
		</div>
	</div>

	<c:if test="${(not empty documentosQuePodemSerAssinadosComSenha)}">
		<div id="dialog-form" title="Assinar com Senha">
			<form id="form-assinarSenha" method="post"
				action="assinar_mov_login_senha_gravar">
				<input type="hidden" id="id" name="id" value="${mov.idMov}" /> <input
					type="hidden" id="tipoAssinaturaMov" name="tipoAssinaturaMov"
					value="A" />
				<fieldset>
					<label>Matrícula</label> <br /> <input id="nomeUsuarioSubscritor"
						type="text" name="nomeUsuarioSubscritor"
						class="text ui-widget-content ui-corner-all"
						onblur="javascript:converteUsuario(this)" /><br /> <br /> <label>Senha</label>
					<br /> <input type="password" id="senhaUsuarioSubscritor"
						name="senhaUsuarioSubscritor"
						class="text ui-widget-content ui-corner-all" autocomplete="off" />
				</fieldset>
			</form>
		</div>

		<div id="dialog-message" title="Basic dialog">
			<p id="mensagemAssinaSenha"></p>
		</div>

		<script>
			dialog = $("#dialog-form").dialog({
				autoOpen : false,
				height : 210,
				width : 350,
				modal : true,
				buttons : {
					"Assinar" : assinarGravar,
					"Cancelar" : function() {
						dialog.dialog("close");
					}
				},
				close : function() {

				}
			});

			function assinarGravar() {
				AssinarDocumentosSenha('false', this);
			}

			dialogM = $("#dialog-message").dialog({
				autoOpen : false,
			});

			function assinarComSenha() {
				var n = $("input.nao-pode-assinar-senha:checked").length;

				if (n > 0) {
					$("#mensagemAssinaSenha")
							.html(
									n
											+ (n === 1 ? " documento selecionado não pode ser assinado somente com senha."
													: " documentos selecionados não podem ser assinados somente com senha.")
											+ " Selecione somente os documentos que estão marcados com ");
					$("#mensagemAssinaSenha")
							.append(
									"<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />");

					dialogM.dialog("open");
				} else {
					var nPode = $("input.pode-assinar-senha:checked").length;

					if (nPode == 0) {
						$("#mensagemAssinaSenha")
								.html(
										"Nenhum documento selecionado. Selecione somente os documentos que estão marcados com ");
						$("#mensagemAssinaSenha")
								.append(
										"<img src=\"/siga/css/famfamfam/icons/keyboard.png\" alt=\"Permite assinatura com senha\" title=\"Permite assinatura com senha\" />");
						dialogM.dialog("open");
					} else {
						dialog.dialog("open");
					}
				}
			}
		</script>
	</c:if>
</siga:pagina>
