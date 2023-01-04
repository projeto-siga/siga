<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Movimentação">

<%-- <c:if test="${not mob.doc.eletronico}"> --%>
<!-- 	<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script> -->
<%-- </c:if> --%>

<script type="text/javascript" language="Javascript1.1">
// function sbmt() {
// 	frm.action='${pageContext.request.contextPath}/app/expediente/mov/vincularPapelLote';
// 	frm.submit();
// }

function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}

		let form = document.forms['frm'];
		form ["paramoffset"].value = offset;
		form.action = "vincularPapelLote";
		form.method = "GET";
		form ["p.offset"].value = offset;

		form.submit();
	}

function tamanho() {
	var i = tamanho2();
	if (i<0) {i=0};
	document.getElementById("Qtd").innerText = 'Restam ' + i + ' Caracteres';
}

function tamanho2() {
	nota= new String();
	nota = this.frm.descrMov.value;
	var i = 255 - nota.length;
	return i;
}
function corrige() {
	if (tamanho2()<0) {
		alert('Descrição com mais de 255 caracteres');
		nota = new String();
		nota = document.getElementById("descrMov").value;
		document.getElementById("descrMov").value = nota.substring(0,255);
	}
}

var newwindow = '';
function popitup_movimentacao() {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open('','${propriedade}',winProp);
		newwindow.name='mov';
	}
	
	newwindow.opener = self;
	t = frm.target; 
	a = frm.action;
	frm.target = newwindow.name;
	frm.action='${pageContext.request.contextPath}/app/expediente/mov/prever?id=${mov.idMov}';
	frm.submit();
	frm.target = t; 
	frm.action = a;
	
	if (window.focus) {
		newwindow.focus()
	}
	return false;
}	

function alteraResponsavel()
{
	var objSelecionado = document.getElementById('tipoResponsavel');
	
	switch (parseInt(objSelecionado.value))
	{
		case 1:
			document.getElementById('selecaoResponsavel').style.display = '';
			document.getElementById('selecaoLotaResponsavel').style.display = 'none';
			document.getElementById('formulario_lotaResponsavelSel_sigla').value = '';
			document.getElementById('lotaResponsavelSelSpan').textContent = '';
			document.getElementById('formulario_lotaResponsavelSel_id').value = '';	
			break;
		case 2:
			document.getElementById('selecaoResponsavel').style.display = 'none';
			document.getElementById('selecaoLotaResponsavel').style.display = '';
			document.getElementById('formulario_responsavelSel_sigla').value = '';
			document.getElementById('responsavelSelSpan').textContent = '';
			document.getElementById('formulario_responsavelSel_id').value = '';
			break;
	}
}

</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5><fmt:message key="documento.vinculacao"/></h5>
			</div>
			<div class="card-body">
				<form name="frm" action="vincularPapel_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
<%-- 					<input type="hidden" name="sigla" value="${sigla}"/> --%>
					<input type="hidden" name="paramoffset" value="0" /> 
					<input type="hidden" name="p.offset" value="0" />
					<div class="row">
						<c:if test="${siga_cliente != 'GOVSP'}">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label for="dtMovString">Data</label>
								<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this,0);" class="form-control"/>
							</div>
						</div>
						</c:if>
						<div class="col-sm-3">
							<div class="form-group">
								    <label>Responsável</label>
									<select class="form-control" id="tipoResponsavel"  name="tipoResponsavel" onchange="javascript:alteraResponsavel();">
										<c:forEach items="${listaTipoRespPerfil}" var="item">
											<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
												${item.value}
											</option>  
										</c:forEach>
									</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
									<span id="selecaoResponsavel">
										<label>&nbsp;</label>
										<siga:selecao propriedade="responsavel" tema="simple" modulo="siga"/>
									</span>
									<span id="selecaoLotaResponsavel">
										<label>&nbsp;</label>
										<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"/>
									</span>		
									<script>alteraResponsavel();</script>			  
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<label for="idPapel">Perfil</label>
								<select class="form-control" name="idPapel">
									<c:forEach items="${listaExPapel}" var="item">
										<option value="${item.idPapel}" ${item.idPapel == idPapel ? 'selected' : ''}>
											${item.descPapel}
										</option>  
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
<!-- 					<div class="row"> -->
<!-- 						<div class="col-sm"> -->
<!-- 							<input type="submit" value="Ok" class="btn btn-primary"/> -->
<!-- 							<input type="button" value=<fmt:message key="botao.cancela"/> onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> -->
<!-- 						</div> -->
<!-- 					</div> -->

					<div class="gt-content-box gt-for-table">
						<br />
						<h5>Usuário(s)/Unidade(s) adicionado(s)</h5>
						<div>
							<table class="table table-hover table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th class="text-center" style="width: 10%;">Matrícula</th>
										<th class="text-center">Nome</th>
										<th class="text-center">Unidade</th>
										<th class="text-center" style="width: 15%;">Função</th>
										<th class="text-center" style="width: 8%;">Excluir</th>
									</tr>
								</thead>
								<tbody class="table-bordered">
									<tr>
										<td>Matricula</td>
										<td>Nome</td>
										<td>Unidade</td>
										<td>Função</td>
										<td>Botão</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					
					<div class="gt-content-box gt-for-table">
						<br />
						<div>
							<table class="table table-hover table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th rowspan="2" class="text-center" style="width: 5%;">
											<input type="checkbox" 	id="checkall" name="checkall" value="true"
											onclick="checkUncheckAll(this)" />
										</th>
										<th class="text-center" style="width: 15%;" colspan="1">Número</th>
										<th class="text-center" style="width: 25%;" colspan="4">Cadastrante</th>
										<th class="text-center" style="width: 25%;" rowspan="2">Descrição</th>
									</tr>
									<tr>
										<th class="text-center" style="width: 16%;"></th>
										<th class="text-center">Data</th>
										<th class="text-center"><fmt:message key="usuario.lotacao" /></th>
										<th class="text-center"><fmt:message key="usuario.pessoa2" /></th>
										<th class="text-center">Tipo</th>
									</tr>
								</thead>
								<tbody class="table-bordered">
									<siga:paginador maxItens="${maxItems}" maxIndices="10"
										totalItens="${tamanho}" itens="${itens}" var="documento">
										<c:set var="x" scope="request">chk_${documento.id}</c:set>
										<c:set var="tpd_x" scope="request">tpd_${documento.id}</c:set>
										<tr>
											<td align="center" class="align-middle text-center"><input
												type="checkbox" name="documentosSelecionados"
												value="${documento.id}" id="${x}" class="chkDocumento"
												onclick="javascript:displaySel(this, '${tpd_x}');" /></td>
											<td class="text-center align-middle"><c:choose>
													<c:when test='${param.popup!="true"}'>
														<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
															${documento.sigla} </a>
													</c:when>
													<c:otherwise>
														<a
															href="javascript:opener.retorna_${param.propriedade}('${documento.id}','${documento.sigla},'');">
															${documento.sigla} </a>
													</c:otherwise>
												</c:choose></td>
											<td class="text-center">${documento.doc.dtDocDDMMYY}</td>
											<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.lotaSubscritor.sigla}"
														descricao="${documento.doc.lotaSubscritor.descricao}" />
											</td>
											<td class="text-center"><siga:selecionado
														isVraptor="true"
														sigla="${documento.doc.subscritor.iniciais}"
														descricao="${documento.doc.subscritor.descricao}" />
											</td>
											<td>
												${documento.doc.exFormaDocumento.descrFormaDoc}
											</td>
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
