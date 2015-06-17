<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="TransferÃªncia em Lote">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">
		<ww:url id="url" action="transferir_lote" namespace="/expediente/mov"/>
		function sbmt(offset) {
			frm.action = '${url}';
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
						&& theForm[z].name != 'checkall') {
					theForm[z].checked = !(theElement.checked);
					theForm[z].click();
				} else if (((theForm[z].type == 'select-one' && theForm[z].name
						.substr(0, 4) == 'tpd_') || (theForm[z].type == 'text' && theForm[z].name
						.substr(0, 4) == 'txt_'))
						&& theForm[z].name != 'tpdall'
						&& theForm[z].name != 'txtall') {
					if (!theElement.checked)
						enableDisableItem(theForm[z].name.substring(4), false);
				} /*else if (theForm[z].name == 'checkall'){
				document.getElementById('div_tpdall').style.display=theElement.checked ? '' : 'none';
				}*/
			}
			/*if (theElement.checked){
			 enableDisableAll(document.getElementById('tpdall'));
			 }*/
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
			var theForm = theElement.form, z = 0;
			for (z = 0; z < theForm.length; z++) {
				if (((theForm[z].type == 'select-one' && theForm[z].name
						.substr(0, 4) == 'tpd_') || (theForm[z].type == 'text' && theForm[z].name
						.substr(0, 4) == 'txt_'))
						&& theForm[z].name != 'tpdall'
						&& theForm[z].name != 'txtall') {
					if (theElement.value != 0)
						enableDisableItem(theForm[z].name.substring(4), false);
					else
						enableDisableItem(theForm[z].name.substring(4), true);
				}

			}
			if (theElement.value == -1) {
				document.getElementById('div_txtall').style.display = '';
				document.getElementById('txtall').focus();
			} else {
				document.getElementById('div_txtall').style.display = 'none';
			}
		}
	</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>TransferÃªncia em Lote</h2>

			<div class="gt-content-box gt-for-table">

				<ww:form name="frm" action="transferir_lote_gravar"
					namespace="/expediente/mov" method="POST" theme="simple">
					<ww:token />
					<ww:hidden name="postback" value="1" />
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">TransferÃªncia</td>
						</tr>
						<tr>
							<td>Data:</td>
							<td><ww:textfield name="dtMovString"
									onblur="javascript:verifica_data(this,0);" /></td>
						</tr>
						<tr>
							<td>ResponsÃ¡vel:</td>
							<td><siga:selecao tema="simple" propriedade="subscritor"
									modulo="siga" /> &nbsp;&nbsp;<ww:checkbox theme="simple"
									name="substituicao" onclick="javascript:displayTitular(this);" />Substituto</td>
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
						<td colspan="1"><siga:selecao propriedade="titular"
								tema="simple" modulo="siga" /></td>
						</tr>
						<tr>
							<td>FunÃ§Ã£o do ResponsÃ¡vel:</td>
							<td colspan="1"><input type="hidden" name="campos"
								value="nmFuncaoSubscritor" /> <ww:textfield
									name="nmFuncaoSubscritor" size="50" maxLength="128" />
								(opcional)</td>
						</tr>
						<tr>
							<td>Atendente</td>
							<td><ww:select name="tipoResponsavel" list="listaTipoResp"
									onchange="javascript:sbmt();" /> <c:choose>
									<c:when test="${tipoResponsavel == 1}">
										<siga:selecao propriedade="lotaResponsavel" tema="simple"
											modulo="siga" />
									</c:when>
									<c:when test="${tipoResponsavel == 2}">
										<siga:selecao propriedade="responsavel" tema="simple"
											modulo="siga" />
									</c:when>
									<c:when test="${tipoResponsavel == 3}">
										<siga:selecao propriedade="cpOrgao" tema="simple"
											modulo="siga" />
									</c:when>
								</c:choose></td>

						</tr>
						<c:if test="${tipoResponsavel == 3}">
							<tr>
								<td>ObservaÃ§Ã£o</td>
								<td><ww:textfield size="30" name="obsOrgao" /></td>
							</tr>
						</c:if>
						<tr>
							<td>Despacho Ãšnico:</td>
							<td>
								<div id="div_tpdall">
									<ww:select name="tpdall" id="tpdall"
										onchange="javascript:enableDisableAll(this);"
										list="tiposDespacho" listKey="idTpDespacho"
										listValue="descTpDespacho" />
								</div>

							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div id="div_txtall" style="display: none">
									<input type="text" name="txtall" id="txtall" maxlength="255" />
								</div>
							</td>
						</tr>
						<tr class="button">
							<td colspan="2"><input type="submit" value="Transferir"></td>
						</tr>
					</table>
			</div>

			<c:forEach var="secao" begin="0" end="1">
				<c:remove var="primeiro" />
				<c:forEach var="m" items="${itens}">
					<c:if
						test="${(secao==0 and titular.idPessoaIni==m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni) or (secao==1 and titular.idPessoaIni!=m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni)}">
						<c:if test="${empty primeiro}">
							<br />
							<h2>
								Atendente:
								<c:choose>
									<c:when test="${secao==0}">${titular.descricao}</c:when>
									<c:otherwise>${lotaTitular.descricao}</c:otherwise>
								</c:choose>
							</h2>
							<div class="gt-content-box gt-for-table">
								<table class="gt-table">
									<tr class="header">
										<td rowspan="2" align="right">NÃºmero</td>
										<td colspan="3" align="center">Documento</td>
										<td colspan="2" align="center">Ãšltima MovimentaÃ§Ã£o</td>
										<td rowspan="2">DescriÃ§Ã£o</td>
										<td rowspan="2" align="center"><input type="checkbox"
											name="checkall" value="true" onclick="checkUncheckAll(this)" /></td>
										<td rowspan="2">Despacho<c:if test="${secao==0}">
											</c:if></td>
									</tr>
									<tr class="header">
										<td align="center">Data</td>
										<td align="center">LotaÃ§Ã£o</td>
										<td align="center">Pessoa</td>
										<td align="center">Data</td>
										<td align="center">Pessoa</td>
									</tr>
									<c:set var="primeiro" value="${true}" />
									</c:if>

									<c:choose>
										<c:when test='${evenorodd == "even"}'>
											<c:set var="evenorodd" value="odd" />
										</c:when>
										<c:otherwise>
											<c:set var="evenorodd" value="even" />
										</c:otherwise>
									</c:choose>
									<tr class="${evenorodd}">
										<td width="11.5%" align="right"><c:choose>
												<c:when test='${param.popup!="true"}'>
													<ww:url id="url" action="exibir"
														namespace="/expediente/doc">
														<ww:param name="sigla">${m.sigla}</ww:param>
													</ww:url>
													<ww:a href="%{url}">${m.sigla}</ww:a>
												</c:when>
												<c:otherwise>
													<a
														href="javascript:opener.retorna_${param.propriedade}('${m.id}','${m.sigla},'');">${m.sigla}</a>
												</c:otherwise>
											</c:choose></td>
										<c:if test="${not m.geral}">
											<td width="5%" align="center">${m.doc.dtDocDDMMYY}</td>
											<td width="5%" align="center"><siga:selecionado
													sigla="${m.doc.lotaSubscritor.sigla}"
													descricao="${m.doc.lotaSubscritor.descricao}" /></td>
											<td width="5%" align="center"><siga:selecionado
													sigla="${m.doc.subscritor.iniciais}"
													descricao="${m.doc.subscritor.descricao}" /></td>
											<td width="5%" align="center">${m.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
											<td width="4%" align="center"><siga:selecionado
													sigla="${m.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
													descricao="${m.ultimaMovimentacaoNaoCancelada.resp.descricao}" /></td>
										</c:if>
										<c:if test="${m.geral}">
											<td width="5%" align="center">${m.doc.dtDocDDMMYY}</td>
											<td width="4%" align="center"><siga:selecionado
													sigla="${m.doc.subscritor.iniciais}"
													descricao="${m.doc.subscritor.descricao}" /></td>
											<td width="4%" align="center"><siga:selecionado
													sigla="${m.doc.lotaSubscritor.sigla}"
													descricao="${m.doc.lotaSubscritor.descricao}" /></td>
											<td width="5%" align="center"></td>
											<td width="4%" align="center"></td>
											<td width="4%" align="center"></td>
											<td width="10.5%" align="center"></td>
										</c:if>
										<td width="44%">${f:descricaoConfidencial(m.doc, lotaTitular)}</td>
										<c:set var="x" scope="request">chk_${m.id}</c:set>
										<c:remove var="x_checked" scope="request" />
										<c:if test="${param[x] == 'true'}">
											<c:set var="x_checked" scope="request">checked</c:set>
										</c:if>
										<c:set var="tpd_x" scope="request">tpd_${m.id}</c:set>
										<c:set var="txt_x" scope="request">txt_${m.id}</c:set>
										<c:set var="lbl_x" scope="request">lbl_${m.id}</c:set>
										<td width="2%" align="center"><input type="checkbox"
											name="${x}" value="true" ${x_checked}
											onclick="javascript:displaySel(this, '${tpd_x}');" /></td>
										<td><c:remove var="style" />
											<c:if test="${empty param[x]}">
												<c:set var="style" value=" style=display:none" />
											</c:if>
											<div id="div_${tpd_x}" ${style}>
												<select name="${tpd_x}" id="${tpd_x}"
													onchange="javascript:displayTxt(this, '${txt_x}');">
													<c:forEach var="tpd" items="${tiposDespacho}">
														<c:remove var="selected" />
														<c:if test="${tpd.idTpDespacho == param[tpd_x]}">
															<c:set var="selected" value="selected" />
														</c:if>
														<option value="${tpd.idTpDespacho}" ${selected}>${tpd.descTpDespacho}</option>
													</c:forEach>
												</select>
												<c:remove var="style" />
												<c:if test="${param[tpd_x] != -1}">
													<c:set var="style" value=" style=display:none" />
												</c:if>
												<div id="div_${txt_x}" ${style}>
													<input type="text" name="${txt_x}" id="${txt_x}"
														value="${param[txt_x]}" maxlength="255" />
												</div>
											</div>
											<div id="div_${lbl_x}" style="display: none">Despacho
												Ãšnico</div></td>
									</tr>
									</c:if>
									</c:forEach>
									<c:if test="${not empty primeiro}">
								</table>
							</div>
						</c:if>
				</c:forEach>
				</ww:form>
		</div>
	</div>
</siga:pagina>