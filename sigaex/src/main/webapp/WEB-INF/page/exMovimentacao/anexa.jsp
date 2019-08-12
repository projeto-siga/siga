<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<siga:pagina titulo="Movimentação" compatibilidade="IE=EmulateIE9">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
			$("body").addClass("fisico");
		</script>
	</c:if>
	<%-- 	<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;VBS:VBScript e CAPICOM')}"> --%>
	<c:import url="/javascript/inc_assina_js.jsp" />
	<%-- 	</c:if> --%>

	<script type="text/javascript" language="Javascript1.1">
		var frm = document.getElementById('frm');
		function sbmt() {
			ExMovimentacaoForm.page.value = '';
			ExMovimentacaoForm.acao.value = 'aAnexar';
			ExMovimentacaoForm.submit();
		}

		function checkUncheckAll(theElement) {
			var theForm = theElement.form, z = 0;
			for (z = 0; z < theForm.length; z++) {
				if (theForm[z].type == 'checkbox'
						&& theForm[z].name != 'checkall') {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				}
			}
		}

		function montaTableAssinados(carregaDiv) {
			if (carregaDiv == true) {
				$('#tableAssinados').html('Carregando...');
				$
						.ajax({
							url : '/sigaex/app/expediente/mov/mostrar_anexos_assinados?sigla=${mobilVO.sigla}',
							success : function(data) {
								$('#tableAssinados').html(data);
								window.setTimeout(mostraBotaoAssinatura, 300);
							}
						});
			} else
				($('#tableAssinados').html(''));
		}

		function mostraBotaoAssinatura() {
			if ($("input[name^='ad_chk']").length > 0) {
				$('#dados-assinatura').show();
			}
		}

		/**
		 * Valida se o anexo foi selecionado ao clicar em OK
		 */
		function validaSelecaoAnexo(form) {
			var result = true;
			var arquivo = form.arquivo;
			if (arquivo == null || arquivo.value == '') {
				alert("O arquivo a ser anexado não foi selecionado!");
				result = false;
			}
			return result;
		}

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

	<div class="container-fluid">
		<c:if test="${!assinandoAnexosGeral}">
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>Anexação de Arquivo - ${mob.siglaEDescricaoCompleta}</h5>
				</div>
				<div class="card-body">
					<form action="anexar_gravar" method="POST"
						enctype="multipart/form-data" class="form">
						<input type="hidden" name="postback" value="1" /> <input
							type="hidden" name="sigla" value="${sigla}" />
						<div class="row">
							<div class="col-md-2 col-sm-3">
								<div class="form-group">
									<label for="dtMovString">Data</label> <input
										class="form-control" type="text" name="dtMovString"
										value="${dtMovString}"
										onblur="javascript:verifica_data(this,0);" />
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label>Responsável</label>
									<siga:selecao tema="simple" propriedade="subscritor"
										modulo="siga" />
								</div>
							</div>
							<div class="col-sm-2 mt-4">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="checkbox" theme="simple"
										name="substituicao" value="${substituicao}"
										onclick="javascript:displayTitular(this);" /> <label
										class="form-check-label">Substituto</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<div id="tr_titular"
										style="${!substituicao == '' ? 'display: none' : ''}">
										<label>Titular</label> <input class="form-control"
											type="hidden" name="campos" value="titularSel.id" />
										<siga:selecao propriedade="titular" tema="simple"
											modulo="siga" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label>Descrição</label> <input class="form-control"
										type="text" name="descrMov" maxlength="80" size="80" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label>Arquivo:</label> <input class="form-control" type="file"
										name="arquivo" accept="application/pdf"
										onchange="testpdf(this.form)" />
								</div>
							</div>
						</div>

						<c:set var="pendencias" value="${false}" />
						<c:forEach var="mov" items="${mobilCompletoVO.movs}">
							<c:if test="${(not mov.cancelada) and (mov.idTpMov eq 57)}">
								<c:set var="pendencias" value="${true}" />
							</c:if>
						</c:forEach>
						<c:if test="${pendencias}">
							<tr class="header">
								<td colspan="2">Pendencias de Anexação</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="gt-form">
										<label>A anexação deste arquivo resolve as seguintes
											pendências:</label>
										<c:forEach var="mov" items="${mobilCompletoVO.movs}">
											<c:if test="${(not mov.cancelada) and (mov.idTpMov eq 57)}">
												<label class="gt-form-element-label"><input
													type="checkbox" class="gt-form-checkbox"
													name="pendencia_de_anexacao" value="${mov.idMov}">
														${mov.descricao}</label>
											</c:if>
										</c:forEach>
									</div>
								</td>
							</tr>
						</c:if>


						<div class="row">
							<div class="col-sm">
								<input type="submit" value="Ok" class="btn btn-primary"
									onclick="javascript: return validaSelecaoAnexo( this.form );" />
								<input type="button" value="Cancela"
									onclick="javascript:window.location.href='/sigaex/app/expediente/doc/exibir?sigla=${mobilVO.sigla}'"
									class="btn btn-cancel ml-2" /><input class="ml-2"
									type="checkbox" name="check"
									onclick="javascript:montaTableAssinados(check.checked);" />
								Exibir anexos assinados
							</div>
						</div>
					</form>
				</div>
			</div>
		</c:if>

		<c:choose>
			<c:when test="${(not empty mobilVO.movs)}">
				<c:if test="${assinandoAnexosGeral}">
					<input style="display: inline" type="checkbox" name="check"
						onclick="javascript:montaTableAssinados(check.checked);" />
					<b>Exibir anexos assinados</b>
					<br />
				</c:if>
				<br />
				<h2>
					Anexos Pendentes de Assinatura
					<c:if test="${assinandoAnexosGeral}">
					      - ${mob.siglaEDescricaoCompleta}
					</c:if>
				</h2>

				<form action="anexar_gravar" method="POST"
					enctype="multipart/form-data" class="form">
					<input type="hidden" name="popup" value="true" /> <input
						type="hidden" name="copia" id="copia" value="false" />

					<table class="table table-stripped mov">
						<thead class="thead-dark">
							<tr>
								<th></th>
								<th align="center" rowspan="2">&nbsp;&nbsp;&nbsp;Data</th>
								<th colspan="2" align="center">Cadastrante</th>
								<th colspan="2" align="center">Atendente</th>
								<th rowspan="2">Descrição</th>
							</tr>
							<tr>
								<c:choose>
									<c:when
										test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;EXT:Extensão')}">
										<th style="text-align: center"><input type="checkbox" name="checkall"
											onclick="checkUncheckAll(this)" /></th>
									</c:when>
									<c:otherwise>
										<td></td>
									</c:otherwise>
								</c:choose>
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
								<th align="left">Lotação</th>
								<th align="left">Pessoa</th>
							</tr>
						</thead>
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
									<c:set var="x" scope="request">ad_chk_${mov.mov.idMov}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td style="text-align: center"><input type="checkbox" name="${x}"
										value="true" ${x_checked} /></td>
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
											buttons="${false}"
											inline="${true}"
											separator="${not empty mov.descricao and mov.descricao != null}">
											<c:forEach var="acao" items="${mov.acoes}">
												<c:choose>
													<c:when test='${mov.idTpMov == 32}'>
														<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
															<c:forEach var="p" items="${acao.params}">
																<c:param name="${p.key}" value="${p.value}" />
															</c:forEach>
														</c:url>
													</c:when>
													<c:otherwise>
														<c:url var="url" value="${acao.nameSpace}/${acao.acao}">
															<c:forEach var="p" items="${acao.params}">
																<c:param name="${p.key}" value="${p.value}" />
															</c:forEach>
														</c:url>
													</c:otherwise>
												</c:choose>

												<c:set var="retornaTela" value='${acao.acao == "excluir"}'></c:set>
												<c:choose>
													<c:when test="${retornaTela}">
														<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
															pos="${acao.pos}"
															url="${pageContext.request.contextPath}${acao.url}continuarTela=TRUE"
															test="${true}" popup="${acao.popup}"
															confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
															idAjax="${mov.idMov}" />
													</c:when>
													<c:otherwise>
														<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
															pos="${acao.pos}"
															url="${pageContext.request.contextPath}${acao.url}"
															test="${true}" popup="${acao.popup}"
															confirm="${acao.msgConfirmacao}" ajax="${acao.ajax}"
															idAjax="${mov.idMov}" />
													</c:otherwise>
												</c:choose>

												<c:if test='${assinadopor and mov.idTpMov == 2}'> ${mov.complemento}
															    <c:set var="assinadopor" value="${false}" />
												</c:if>
											</c:forEach>
										</siga:links> <input type="hidden" name="ad_descr_${mov.idMov}"
										value="${mov.mov.referencia}" /> <input type="hidden"
										name="ad_url_pdf_${mov.idMov}"
										value="/sigaex/app/arquivo/exibir?arquivo=${mov.mov.nmPdf}" />
										<input type="hidden" name="ad_url_post_${mov.idMov}"
										value="/sigaex/app/expediente/mov/assinar_mov_gravar" /> <input
										type="hidden" name="ad_url_post_password_${mov.idMov}"
										value="/sigaex/app/expediente/mov/assinar_mov_login_senha_gravar" />
										<input type="hidden" name="ad_id_${mov.idMov}"
										value="${fn:replace(mov.mov.referencia, ':', '_')}" /> <input
										type="hidden" name="ad_description_${mov.idMov}"
										value="${mov.mov.obs}" /> <input type="hidden"
										name="ad_kind_${mov.idMov}"
										value="${mov.mov.exTipoMovimentacao.sigla}" />
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</table>
				</form>
	</div>
	</div>
	</c:when>
	<c:otherwise>
		<c:if test="${assinandoAnexosGeral}">
			<script language="javascript">
				montaTableAssinados(true);
			</script>
		</c:if>
	</c:otherwise>
	</c:choose>
	<div class="gt-content clearfix">
		<div id="tableAssinados">
			<br />
		</div>
	</div>

	<div id="dados-assinatura" style="display: none">

		<input type="hidden" name="ad_url_base" value="" /> <input
			type="hidden" name="ad_url_next"
			value="/sigaex/app/expediente/doc/atualizar_marcas?sigla=${mobilVO.sigla}" />
		<tags:assinatura_botoes autenticar="true" assinar="true"
			assinarComSenha="${f:podeAssinarMovimentacaoDoMobilComSenha(titular,lotaTitular,mob)}"
			autenticarComSenha="${f:podeAutenticarComSenha(titular,lotaTitular,mob)}" />

		<c:set var="botao" value="ambos" />
		<c:set var="lote" value="true" />
	</div>
	<script>
		mostraBotaoAssinatura();
	</script>


	</div>
	<tags:assinatura_rodape />
</siga:pagina>
