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
	compatibilidade="IE=EmulateIE9">

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
									<input type="hidden" name="ad_url_base" value="" /> <input
										type="hidden" name="ad_url_next" value="/siga/app/principal" />
									<c:set var="botao" value="" />
									<c:if test="${autenticando}">
										<c:set var="botao" value="autenticando" />
									</c:if>
									<c:set var="lote" value="false" />
								</div> <tags:assinatura_botoes assinar="true" />
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
									<c:set var="x" scope="request">ad_chk_${assdoc.doc.idDoc}</c:set>
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

										<td style="text-align: center"><c:choose>
												<c:when test="${assdoc.podeAssinar}">
													<input type="checkbox" name="${x}" value="true"
														${x_checked} class="chk-assinar" />
												</c:when>
												<c:otherwise>
													<input type="hidden" name="${x}" value="false" />
												</c:otherwise>
											</c:choose></td>
										<td style="text-align: center"></td>
										<td style="text-align: center"><c:choose>
												<c:when test="${assdoc.podeSenha}">
													<input type="checkbox"
														name="ad_password_${assdoc.doc.idDoc}" value="true"
														class="chk-senha" />
												</c:when>
												<c:otherwise>
													<input type="hidden" name="ad_password_${assdoc.doc.idDoc}"
														value="false" />
												</c:otherwise>
											</c:choose></td>
										<td><a target="_blank"
											href="/sigaex/app/expediente/doc/exibir?sigla=${assdoc.doc.sigla}">${assdoc.doc.codigo}</a>
										</td>
										<td style="text-align: center">${assdoc.doc.dtDocDDMMYY}</td>
										<td style="text-align: center">${assdoc.doc.lotaCadastrante.siglaLotacao}</td>
										<td style="text-align: center">${assdoc.doc.cadastrante.sigla}</td>
										<td>${assdoc.doc.descrFormaDoc}</td>
										<td>${assdoc.doc.descrDocumento}</td>
									</tr>
									<!-- Nato: desabilitando o trâmite automático na assinatura em lote -->
									<input type="hidden" name="ad_tramitar_${assdoc.doc.idDoc}"
										value="false" />
									<input type="hidden" name="ad_descr_${assdoc.doc.idDoc}"
										value="${assdoc.doc.sigla}" />
									<input type="hidden" name="ad_url_pdf_${assdoc.doc.idDoc}"
										value="/sigaex/app/arquivo/exibir?arquivo=${assdoc.doc.codigoCompacto}.pdf" />
									<input type="hidden" name="ad_url_post_${assdoc.doc.idDoc}"
										value="/sigaex/app/expediente/mov/assinar_gravar" />
									<input type="hidden"
										name="ad_url_post_password_${assdoc.doc.idDoc}"
										value="/sigaex/app/expediente/mov/assinar_senha_gravar" />

									<input type="hidden" name="ad_id_${assdoc.doc.idDoc}"
										value="${assdoc.doc.codigoCompacto}" />
									<input type="hidden" name="ad_description_${assdoc.doc.idDoc}"
										value="${assdoc.doc.descrDocumento}" />
									<input type="hidden" name="ad_kind_${assdoc.doc.idDoc}"
										value="${assdoc.doc.descrFormaDoc}" />

									<c:forEach var="assmov" items="${assdoc.movs}">
										<c:set var="x" scope="request">ad_chk_mov${assmov.mov.idMov}</c:set>
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
													<input type="checkbox" name="ad_aut_mov${assmov.mov.idMov}"
														id="${x}" value="true" ${x_checked} class="chk-autenticar" />
												</c:if></td>
											<td style="text-align: center"><c:if
													test="${podeAssinarComSenha}">
													<input type="checkbox"
														name="ad_password_mov${assmov.mov.idMov}" id="${x}"
														value="true" class="chk-senha" />
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
										<!-- Nato: desabilitando o trâmite automático na assinatura em lote -->
										<input type="hidden" name="ad_tramitar_${assmov.mov.idMov}"
											value="false" />
										<input type="hidden" name="ad_descr_mov${assmov.mov.idMov}"
											value="${assdoc.doc.sigla}:${assmov.mov.idMov}" />
										<input type="hidden" name="ad_url_pdf_mov${assmov.mov.idMov}"
											value="/sigaex/app/arquivo/exibir?arquivo=${assmov.mov.referencia}.pdf" />
										<input type="hidden" name="ad_url_post_mov${assmov.mov.idMov}"
											value="/sigaex/app/expediente/mov/assinar_mov_gravar" />
										<input type="hidden"
											name="ad_url_post_password_mov${assmov.mov.idMov}"
											value="/sigaex/app/expediente/mov/assinar_mov_login_senha_gravar" />

										<input type="hidden" name="ad_id_mov${assmov.mov.idMov}"
											value="${fn:replace(assmov.mov.referencia, ':', '_')}" />
										<input type="hidden"
											name="ad_description_mov${assmov.mov.idMov}"
											value="${assmov.mov.obs}" />
										<input type="hidden" name="ad_kind_mov${assmov.mov.idMov}"
											value="${assmov.mov.exTipoMovimentacao.sigla}" />
									</c:forEach>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<form>
		</div>
	</div>

	<tags:assinatura_rodape nomeUsuarioSubscritor="${assmov.mov.exDocumento.subscritor.sigla}" />
</siga:pagina>
