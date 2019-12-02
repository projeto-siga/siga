<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Anotação em Lote">

	<script type="text/javascript" language="Javascript1.1">	
	function sbmt(offset) {
		frm.action = '${pageContext.request.contextPath}/app/expediente/mov/anotar_lote';
		frm.submit();
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
		
	function checkUncheckAll(theElement) {
		var theForm = theElement.form, z = 0;
		for(z=0; z<theForm.length;z++) {
	    	if(theForm[z].type == 'checkbox' && theForm[z].name != 'checkall') {
				theForm[z].checked = !(theElement.checked);
				theForm[z].click();
			}
		}
	}
	
	function displaySel(chk, el) {
		document.getElementById('div_' + el).style.display=chk.checked ? '' : 'none';
		if (chk.checked == -2) 
			document.getElementById(el).focus();
	}
	
	function displayTxt(sel, el) {					
		document.getElementById('div_' + el).style.display=sel.value == -1 ? '' : 'none';
		document.getElementById(el).focus();
	}
	
	
</script>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Anotação em Lote</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="anotar_lote_gravar" method="post">
					<input type="hidden" name="postback" value="1" />
					<div class="row">
						<div class="col-md-2 col-sm-3">
							<div class="form-group">
								<label>Data</label>
								<input type="text" name="dtMovString"
									id="dtMovString" onblur="javascript:verifica_data(this,0);"
									class="form-control" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Responsável</label>
								<siga:selecao tema="simple" propriedade="subscritor" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-2 mt-4">
							<div class="form-group">
								<div class="form-check form-check-inline">
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
										<label>Titular:</label>
										<input type="hidden" name="campos" value="${titularSel.id}" />
										<siga:selecao propriedade="titular"	tema="simple" modulo="siga"/>
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
						<div class="col-sm">
							<div class="form-group">
								<label for="descrMov">Nota</label>
								<textarea class="form-control" name="descrMov" value="${descrMov}" cols="60" rows="5"
									onkeyup="corrige();tamanho();" onblur="tamanho();"
									onclick="tamanho();"></textarea>
								<small class="form-text text-muted" id="Qtd">Restam&nbsp;255&nbsp;Caracteres</small>
							</div>
						</div>
					</div>
				<c:if test="${tipoResponsavel == 3}">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Observação</label>
								<input type="text" size="30" name="obsOrgao" id="obsOrgao" class="form-control" />
							</div>
						</div>
					</div>
				</c:if>

					<div class="row">
						<div class="col-sm">
							<input type="submit" value="Ok" class="btn btn-primary" />
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
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
						<h5>Atendente: <c:choose>
							<c:when test="${secao==0}">${titular.descricao}</c:when>
							<c:otherwise>${lotaTitular.descricao}</c:otherwise>
						</c:choose></h5>
					<div>
						<table class="table table-hover table-striped">
							<thead class="${thead_color} align-middle text-center">
								<tr>
									<th rowspan="2" class="text-right"><input type="checkbox"
										name="checkall" onclick="checkUncheckAll(this)" /></th>
									<th rowspan="2" class="text-right">Número</th>
									<th rowspan="2" class="text-center">Destinação da via</th>
									<th colspan="3" class="text-center">Documento</th>
									<th colspan="2" class="text-center">Última Movimentação</th>
									<th rowspan="2" class="text-left">Descrição</th>
								</tr>
								<tr class="header">
									<th class="text-center">Data</th>
									<th class="text-center"><fmt:message key="usuario.lotacao"/></th>
									<th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
									<th class="text-center">Data</th>
									<th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:set var="primeiro" value="${true}" />
							</c:if>
	
								<tr>
									<c:set var="x" scope="request">chk_${m.id}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td width="2%" align="center"><input type="checkbox"
										name="${x}" value="true" ${x_checked} /></td>
									<td width="11.5%" align="right"><c:choose>
										<c:when test='${param.popup!="true"}'>										
											<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${m.sigla}">${m.sigla}</a>
										</c:when>
										<c:otherwise>
											<a
												href="javascript:opener.retorna_${param.propriedade}('${m.id}','${m.sigla},'');">${m.sigla}</a>
										</c:otherwise>
									</c:choose></td>
									<c:if test="${not m.geral}">
										<td width="2%" align="center">${f:destinacaoPorNumeroVia(m.doc,
										m.numSequencia)}</td>
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
										<td width="2%" align="center"></td>
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
									<td width="44%">${f:descricaoSePuderAcessar(m.doc, titular,
									lotaTitular)}</td>
								</tr>
							</c:if>
							</c:forEach>
							<c:if test="${not empty primeiro}">
							</tbody>
						</table>
					</div>
					</c:if>
			</c:forEach>
	</form>
</siga:pagina>