<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<fmt:message key="documento.transferencia.lote" var="titulo" />
<siga:pagina titulo="${titulo}">

	<script type="text/javascript" language="Javascript1.1">
	function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}

		let form = document.forms['frm'];
		form ["paramoffset"].value = offset;
		form.action = "transferir_lote";
		form.method = "GET";
		form ["p.offset"].value = offset;

		form.submit();
	}

	function checkUncheckAll(theElement) {
			let isChecked = theElement.checked;
			Array.from(document.getElementsByClassName("chkDocumento")).forEach(chk => chk.checked = isChecked);
		}

		function displaySel(chk, el) {
			document.getElementById("checkall").checked = 
				Array.from(document.getElementsByClassName("chkDocumento")).every(chk => chk.checked);
		}

		function updateTipoResponsavel() {
			document.getElementById("selecaoLotaResponsavel").style.display = 'none';
			document.getElementById("selecaoResponsavel").style.display = 'none';
			document.getElementById("selecaoCpOrgao").style.display = 'none';
			Array.from(document.getElementsByClassName("campo-orgao-externo")).forEach(el => el.style.display = 'none'); 

			var objSelecionado = document.getElementById("tipoResponsavel");

			switch (parseInt(objSelecionado.value)) {
			case 1:
				document.getElementById("selecaoLotaResponsavel").style.display = '';
				break;
			case 2:
				document.getElementById("selecaoResponsavel").style.display = '';
				break;
			case 3:
				document.getElementById("selecaoCpOrgao").style.display = '';
				Array.from(document.getElementsByClassName("campo-orgao-externo")).forEach(el => el.style.display = ''); 
				break;
			}
		}
	</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>${titulo}</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="transferir_lote_gravar" method="post">
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="paramoffset" value="0" /> 
					<input type="hidden" name="p.offset" value="0" />
					
					<div class="row campo-orgao-externo" style="display: none;">
						<div class="col-sm">
							<div class="form-group">
								<span style="color: red"><fmt:message
										key="tela.tramitar.atencao" /></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label for="tipoResponsavel"><fmt:message key="tela.tramitarLote.tipoResponsavel"/></label> 
								<select class="custom-select"
									id="tipoResponsavel" name="tipoResponsavel"
									value="${tipoResponsavel}"
									onchange="javascript:updateTipoResponsavel();">
									<c:forEach var="item" items="${listaTipoResp}">
										<option value="${item.key}">${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>&nbsp;</label> <span id="selecaoLotaResponsavel">
									<siga:selecao propriedade="lotaResponsavel" tema="simple"
										modulo="siga" />
								</span> <span id="selecaoResponsavel" style="display: none;"> <siga:selecao
										propriedade="responsavel" tema="simple" modulo="siga" />
								</span> <span id="selecaoCpOrgao" style="display: none;"> <siga:selecao
										propriedade="cpOrgao" tema="simple" modulo="siga" />
								</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label>Data da devolução</label> <input type="text"
									name="dtDevolucaoMovString"
									onblur="javascript:verifica_data(this,0);"
									value="${dtDevolucaoMovString}" class="form-control" /> <small
									class="form-text text-muted">Atenção: somente preencher
									a data de devolução se a intenção for, realmente, que o
									documento seja devolvido até esta data.</small>
							</div>
						</div>
						<div class="col-sm-3">

							<div class="form-group campo-orgao-externo"
								style="display: none;">
								<div class="form-check form-check-inline mt-4 ">
									<input class="form-check-input" type="checkbox"
										name="protocolo" id="protocolo" value="mostrar"
										<c:if test="${protocolo}">checked</c:if> /> <label
										class="form-check-label" for="protocolo"><fmt:message
											key="tela.tramitar.checkbox" /></label>
								</div>
							</div>

						</div>
						<div class="col-sm-3">
							<div class="form-group campo-orgao-externo"
								style="display: none;">
								<label>Observação</label> <input type="text" size="30"
									name="obsOrgao" id="obsOrgao" class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1">
							<button type="submit" class="btn btn-primary">
								<fmt:message key="documento.transferir" />
							</button>
						</div>
					</div>

					<div class="gt-content-box gt-for-table">
						<br />
						<h5>Destinatário: ${titular.descricao}</h5>
						<div>
							<table class="table table-hover table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th rowspan="2" align="center"><input type="checkbox"
											id="checkall" name="checkall" value="true"
											onclick="checkUncheckAll(this)" /></th>
										<th rowspan="2" class="text-right">Número</th>
										<th colspan="3">Documento</th>
										<th colspan="2">Última Movimentação</th>
										<th rowspan="2">Descrição</th>
										<th rowspan="2" class="col-5 d-none">Despacho <c:if
												test="${secao==0}" />
										</th>
									</tr>
									<tr>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message
												key="usuario.lotacao" /></th>
										<th class="text-center"><fmt:message
												key="usuario.pessoa2" /></th>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message
												key="usuario.pessoa2" /></th>
									</tr>
								</thead>
								<tbody class="table-bordered">
									<siga:paginador maxItens="${maxItems}" maxIndices="10"
										totalItens="${tamanho}" itens="${itens}" var="documento">
										<c:set var="x" scope="request">
												chk_${documento.id}
											</c:set>
										<c:set var="tpd_x" scope="request">tpd_${documento.id}</c:set>
										<tr>
											<td align="center" class="align-middle text-center"><input
												type="checkbox" name="documentosSelecionados"
												value="${documento.id}" id="${x}" class="chkDocumento"
												onclick="javascript:displaySel(this, '${tpd_x}');" /></td>
											<td class="text-right"><c:choose>
													<c:when test='${param.popup!="true"}'>
														<a
															href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
															${documento.sigla} </a>
													</c:when>
													<c:otherwise>
														<a
															href="javascript:opener.retorna_${param.propriedade}('${documento.id}','${documento.sigla},'');">
															${documento.sigla} </a>
													</c:otherwise>
												</c:choose></td>
											<c:if test="${not documento.geral}">
												<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" /></td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" /></td>
												<td class="text-center">${documento.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
														descricao="${documento.ultimaMovimentacaoNaoCancelada.resp.descricao}" />
												</td>
											</c:if>
											<c:if test="${documento.geral}">
												<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" /></td>
												<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" /></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
												<td class="text-center"></td>
											</c:if>
											<td>
												<c:choose>
													<c:when test="${siga_cliente == 'GOVSP'}">
														${documento.doc.descrDocumento}
													</c:when>
													<c:otherwise>
														${f:descricaoConfidencial(documento.doc, lotaTitular)}
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</siga:paginador>
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>