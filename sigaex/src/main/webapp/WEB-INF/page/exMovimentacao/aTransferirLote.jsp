<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Transferência em Lote">

	<script type="text/javascript" language="Javascript1.1">
		function sbmt(offset) {
			frm.action = '${pageContext.request.contextPath}/app/expediente/mov/transferir_lote';
			frm.submit();
		}

		function enableDisableItem(coreName, enable) {
			var estiloCombo, estiloLabel;
			if (!document.getElementById('chk_' + coreName).checked) {
				estiloLabel = 'none';
				estiloCombo = 'none';
			} else if (enable == true) {
				estiloLabel = 'none';
				estiloCombo = '';
			} else {
				estiloLabel = '';
				estiloCombo = 'none';
			}
			document.getElementById('div_tpd_' + coreName).style.display = estiloCombo;
			document.getElementById('div_lbl_' + coreName).style.display = estiloLabel;
		}

		function checkUncheckAll(theElement) {
			var theForm = theElement.form, z = 0;
			for (z = 0; z < theForm.length; z++) {
				if (theForm[z].type == 'checkbox'
						&& (theForm[z].name != 'checkall' && theForm[z].name != 'substituicao') ) {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				} else if (((theForm[z].type == 'select-one' && theForm[z].name
						.substr(0, 4) == 'tpd_') || (theForm[z].type == 'text' && theForm[z].name
						.substr(0, 4) == 'txt_'))
						&& theForm[z].name != 'tpdall'
						&& theForm[z].name != 'txtall') {
					if (!theElement.checked)
						enableDisableItem(theForm[z].name.substring(4), false);
				}
			}
		}

		function displaySel(chk, el) {
			document.getElementById('div_' + el).style.display = chk.checked ? ''
					: 'none';
			if (chk.checked == -2)
				document.getElementById(el).focus();
			if (document.getElementById('tpdall').value == 0)
				enableDisableItem(chk.name.substring(4), true);
			else
				enableDisableItem(chk.name.substring(4), false);
		}

		function displayTxt(sel, el) {
			document.getElementById('div_' + el).style.display = sel.value == -1 ? ''
					: 'none';
			document.getElementById(el).focus();
		}

		function enableDisableAll(theElement) {
			if (theElement.value == -1) {
				document.getElementById('div_txtall').style.display = '';
				document.getElementById('txtall').focus();
			} else {
				document.getElementById('div_txtall').style.display = 'none';
			}
		}
		function updateTipoResponsavel() {

			var objSelecionado = document.getElementById("tipoResponsavel");

			switch (parseInt(objSelecionado.value)) {
			case 1:
				document.getElementById("selecaoLotaResponsavel").style.display = '';
				document.getElementById("selecaoResponsavel").style.display = 'none';
				document.getElementById("selecaoCpOrgao").style.display = 'none';
				document.getElementById("selecaoCpOrgaoObservacao").style.display = 'none';
				break;
			case 2:
				document.getElementById("selecaoLotaResponsavel").style.display = 'none';
				document.getElementById("selecaoResponsavel").style.display = '';
				document.getElementById("selecaoCpOrgao").style.display = 'none';
				document.getElementById("selecaoCpOrgaoObservacao").style.display = 'none';
				break;
			case 3:
				document.getElementById("selecaoLotaResponsavel").style.display = 'none';
				document.getElementById("selecaoResponsavel").style.display = 'none';
				document.getElementById("selecaoCpOrgao").style.display = '';
				document.getElementById("selecaoCpOrgaoObservacao").style.display = '';
				break;

			}
		}
	</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Transferência em Lote</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="transferir_lote_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data</label> <input type="text" name="dtMovString"
									id="dtMovString" onblur="javascript:verifica_data(this,0);"
									class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
							</div>
						</div>
						<div class="col-sm">
							<div class="form-group">
								<div class="form-check form-check-inline mt-4">
									<input type="checkbox" name="substituicao" onclick="javascript:displayTitular(this);" class="form-check-input" /> 
									<label class="form-check-label">Substituto</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<c:choose>
									<c:when test="${!substituicao}">
										<div id="tr_titular" style="display: none;">
									</c:when>
									<c:otherwise>
										<div id="tr_titular" style="">
									</c:otherwise>
								</c:choose>
								<label>Titular</label> <input type="hidden" name="campos"
									value="${titularSel.id}" />
								<siga:selecao propriedade="titular" tema="simple" modulo="siga" />
							</div>
						</div>
					</div>
			</div>

			<div class="row">
				<div class="col-sm">
					<div class="form-group">
						<label>Função do Responsável</label> <input type="hidden"
							name="campos" value="${nmFuncaoSubscritor}" /> <input
							type="text" name="nmFuncaoSubscritor" id="nmFuncaoSubscritor"
							value="${nmFuncaoSubscritor}" size="50" maxLength="128"
							class="form-control" /> <small class="form-text text-muted">(opcional)</small>


					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<div class="form-group">
						<label>Atendente</label> <select class="custom-select"
							id="tipoResponsavel" name="tipoResponsavel"
							value="${tipoResponsavel}"
							onchange="javascript:updateTipoResponsavel();">
							<c:forEach var="item" items="${listaTipoResp}">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-sm-8">
					<div class="form-group">
						<label>&nbsp;</label> <span id="selecaoLotaResponsavel"> <siga:selecao
								propriedade="lotaResponsavel" tema="simple" modulo="siga" />
						</span> <span id="selecaoResponsavel" style="display: none;"> <siga:selecao
								propriedade="responsavel" tema="simple" modulo="siga" />
						</span> <span id="selecaoCpOrgao" style="display: none;"> <siga:selecao
								propriedade="cpOrgao" tema="simple" modulo="siga" />
						</span>
					</div>
				</div>
			</div>
			<div class="row" id="selecaoCpOrgaoObservacao" style="display: none;">
				<div class="col-sm">
					<div class="form-group">
						<label>Observação</label> <input type="text" size="30"
							name="obsOrgao" id="obsOrgao" class="form-control" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<div class="form-group" id="div_tpdall">
						<label>Despacho Único</label> <select name="tpdall" id="tpdall"
							class="custom-select"
							onchange="javascript:enableDisableAll(this);">
							<c:forEach var="item" items="${tiposDespacho}">
								<option value="${item.idTpDespacho}">
									${item.descTpDespacho}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="col-sm-8">
					<div class="form-group" id="div_txtall" style="display: none;">
						<label>&nbsp;</label> <input type="text" name="txtall" id="txtall"
							maxlength="255" class="form-control" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-1">
					<button type="submit" class="btn btn-primary">Transferir</button>
				</div>
			</div>
		</div>
	</div>

	<c:forEach var="secao" begin="0" end="1">
		<c:remove var="primeiro" />
		<c:forEach var="m" items="${itens}">
			<c:if
				test="${(secao==0 and titular.idPessoaIni==m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni) or (secao==1 and titular.idPessoaIni!=m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni)}">
				<c:if test="${empty primeiro}">
					<br />
					<h5>
						Destinatário:
						<c:choose>
							<c:when test="${secao==0}">
										${titular.descricao}
									</c:when>
							<c:otherwise>
										${lotaTitular.descricao}
									</c:otherwise>
						</c:choose>
					</h5>
					<div>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th rowspan="2" class="text-right">Número</th>
									<th colspan="3">Documento</th>
									<th colspan="2">Última Movimentação</th>
									<th rowspan="2">Descrição</th>
									<th rowspan="2" align="center"><input type="checkbox"
										name="checkall" value="true" onclick="checkUncheckAll(this)" />
									</th>
									<th rowspan="2" class="col-5">Despacho <c:if
											test="${secao==0}" />
									</th>
								</tr>
								<tr >
									<th class="text-center">Data</td>
									<th class="text-center"><fmt:message key="usuario.lotacao"/></td>
									<th class="text-center"><fmt:message key="usuario.pessoa2"/></td>
									<th class="text-center">Data</td>
									<th class="text-center"><fmt:message key="usuario.pessoa2"/></td>
								</tr>
							</thead>
							<tbody class="table-bordered">
								<c:set var="primeiro" value="${true}" />
								</c:if>

								<tr>
									<td class="text-right"><c:choose>
											<c:when test='${param.popup!="true"}'>
												<a
													href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${m.sigla}">
													${m.sigla} </a>
											</c:when>
											<c:otherwise>
												<a
													href="javascript:opener.retorna_${param.propriedade}('${m.id}','${m.sigla},'');">
													${m.sigla} </a>
											</c:otherwise>
										</c:choose></td>
									<c:if test="${not m.geral}">
										<td class="text-center">${m.doc.dtDocDDMMYY}</td>
										<td class="text-center"><siga:selecionado
												isVraptor="true" sigla="${m.doc.lotaSubscritor.sigla}"
												descricao="${m.doc.lotaSubscritor.descricao}" /></td>
										<td class="text-center"><siga:selecionado
												isVraptor="true" sigla="${m.doc.subscritor.iniciais}"
												descricao="${m.doc.subscritor.descricao}" /></td>
										<td class="text-center">${m.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
										<td class="text-center"><siga:selecionado
												isVraptor="true"
												sigla="${m.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
												descricao="${m.ultimaMovimentacaoNaoCancelada.resp.descricao}" />
										</td>
									</c:if>
									<c:if test="${m.geral}">
										<td class="text-center">${m.doc.dtDocDDMMYY}</td>
										<td class="text-center"><siga:selecionado
												isVraptor="true" sigla="${m.doc.subscritor.iniciais}"
												descricao="${m.doc.subscritor.descricao}" /></td>
										<td class="text-center"><siga:selecionado
												isVraptor="true" sigla="${m.doc.lotaSubscritor.sigla}"
												descricao="${m.doc.lotaSubscritor.descricao}" /></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
									</c:if>
									<td>${f:descricaoConfidencial(m.doc, lotaTitular)}</td>
									<c:set var="x" scope="request">
												chk_${m.id}
											</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<c:set var="tpd_x" scope="request">
												tpd_${m.id}
											</c:set>
									<c:set var="txt_x" scope="request">
												txt_${m.id}
											</c:set>
									<c:set var="lbl_x" scope="request">
												lbl_${m.id}
											</c:set>
									<td align="center" class="align-middle text-center"><input type="checkbox" name="${x}"
										value="true" ${x_checked} id="${x}"
										onclick="javascript:displaySel(this, '${tpd_x}');" /></td>
									<td align="center" class="align-middle text-center">
											<c:remove var="style" /> 
											<c:if test="${empty param[x]}">
												<c:set var="style" value=" style=display:none" />
											</c:if>
										<div id="div_${tpd_x}" ${style}>
											<select class="custom-select" name="${tpd_x}" id="${tpd_x}"
												onchange="javascript:displayTxt(this, '${txt_x}');">
												<c:forEach var="tpd" items="${tiposDespacho}">
													<c:remove var="selected" />
													<c:if test="${tpd.idTpDespacho == param[tpd_x]}">
														<c:set var="selected" value="selected" />
													</c:if>
													<option value="${tpd.idTpDespacho}" ${selected}>
														${tpd.descTpDespacho}</option>
												</c:forEach>
											</select>
											<c:remove var="style" />
											<c:if test="${param[tpd_x] != -1}">
												<c:set var="style" value=" style=display:none" />
											</c:if>
											<div id="div_${txt_x}" ${style}>
												<input type="text" name="${txt_x}" id="${txt_x}"
													value="${param[txt_x]}" maxlength="255" class="form-control"/>
											</div>
										</div>
										<div id="div_${lbl_x}" style="display: none">Despacho
											Único</div></td>
								</tr>
								</c:if>
								</c:forEach>
								<c:if test="${not empty primeiro}">
							</tbody>
						</table>
					</div>
				</c:if>
		</c:forEach>



		</div>
		</div>


		</form>
		</div>
		</div>
		</div>
</siga:pagina>