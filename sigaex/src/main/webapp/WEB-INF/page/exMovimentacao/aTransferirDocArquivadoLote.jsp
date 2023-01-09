<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<fmt:message key="documento.transferencia.arquivados.lote" var="titulo" />
<siga:pagina titulo="${titulo}">

	<script type="text/javascript" language="Javascript1.1">
	/*function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}

		let form = document.forms['frm'];
		form ["paramoffset"].value = offset;
		form.action = "transferir_lote";
		form.method = "GET";
		form ["p.offset"].value = offset;

		form.submit();
	}*/

		function checkUncheckAll(theElement) {
			let isChecked = theElement.checked;
			Array.from(document.getElementsByClassName("chkDocumento")).forEach(chk => chk.checked = isChecked);
		}

		function displaySel(chk, el) {
			document.getElementById("checkall").checked = 
				Array.from(document.getElementsByClassName("chkDocumento")).every(chk => chk.checked);
		}

		function updateResponsavelSelecionado(id) {
            document.getElementById('responsavelSelecionado').innerHTML = document.getElementById(id).value;
        }
		
		function updateTipoResponsavel() {
			document.getElementById("lotaResponsavel").style.display = 'none';
			document.getElementById("responsavel").style.display = 'none';
			//document.getElementById("selecaoCpOrgao").style.display = 'none';
			//Array.from(document.getElementsByClassName("campo-orgao-externo")).forEach(el => el.style.display = 'none'); 

			var objSelecionado = document.getElementById("tipoResponsavel");

			switch (parseInt(objSelecionado.value)) {
			case 1:
				document.getElementById("lotaResponsavel").style.display = '';
				break;
			case 2:
				document.getElementById("responsavel").style.display = '';
				break;
			/*case 3:
				document.getElementById("selecaoCpOrgao").style.display = '';
				Array.from(document.getElementsByClassName("campo-orgao-externo")).forEach(el => el.style.display = ''); 
				break;*/
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
				<form name="frm" action="pesquisa_documentos_arquivados_transferencia" method="get">
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="paramoffset" value="0" /> 
					<input type="hidden" name="p.offset" value="0" />
					
					
					<h4>ORIGEM:</h4>											
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label for="tipoResponsavel">usuário/unidade</label> 
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
								<label>&nbsp;</label> 
								 <span id="lotaResponsavel">
									<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga" 
										/>
								</span> 
								<span id="responsavel" style="display: none;"> 
									<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
								</span>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${not empty itens}">
							<div class="row ">
								<div class="col-sm-3">
									<h4>DESTINO:</h4>
								</div>
							</div>
							<!-- <div class="row ">
								<div class="col-sm-3">
									<div class="form-group">
										<label for="tipoDestinatario">unidade</label> 
										<select class="custom-select"
											id="tipoDestinatario" name="tipoDestinatario"
											value="${tipoDestinatario}"
											onchange="javascript:updateTipoResponsavel();">
											<c:forEach var="item" items="${listaTipoResp}">
												<option value="${item.key}">${item.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label>&nbsp;</label> 
										 <span id="lotacaoDestinatario">
											<siga:selecao propriedade="lotacaoDestinatario" tema="simple" modulo="siga" 
												/>
										</span> 
										<span id="destinatario" style="display: none;"> 
											<siga:selecao propriedade="destinatario" tema="simple" modulo="siga"/>
										</span>
									</div>
								</div>
							</div>
							 -->
							<div class="row">
								<div class="col-sm-9">
									<h4>Motivo da Transferência:</h4>
		                            <div class="form-group campo-orgao-externo">
		                                <input type="text" size="30" name="obsOrgao" id="obsOrgao" class="form-control"/>
		                            </div>
		                        </div>
		                    </div>
		                    
		                    <div class="row">
								<div class="col-sm-1">
		                    		<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-primary" />				
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="row">
								<div class="col-sm-1">
									<button type="submit" class="btn btn-primary">Pesquisar</button>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="gt-content-box gt-for-table">
						<br />
						<h5>Destinatário: <span id="responsavelSelecionado"></span></h5>
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