<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Arquivamento em Lote">
<script type="text/javascript" language="Javascript1.1">
	function sbmt(offset) {
		frm.action = '${pageContext.request.contextPath}/app/expediente/mov/arquivar_corrente_lote';
		frm.submit();
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
	<div class="container-fluid">
	<form name="frm" action="arquivar_corrente_lote_gravar" method="post" theme="simple">		
		<input type="hidden" name="postback" value="1" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Arquivamento</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="submit" class="btn btn-primary">Arquivar</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:forEach var="secao" begin="0" end="1">
			<c:remove var="primeiro" />
			<c:forEach var="m" items="${itens}">
				<c:if test="${(secao==0 and titular.idPessoaIni==m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni) or (secao==1 and titular.idPessoaIni!=m.ultimaMovimentacaoNaoCancelada.resp.idPessoaIni)}">
					<c:if test="${empty primeiro}">
						<h6>Atendente: 
							<c:choose>
								<c:when test="${secao==0}">${titular.descricao}</c:when>
								<c:otherwise>${lotaTitular.descricao}</c:otherwise>
							</c:choose></h2>
						</h6>
						<div>
							<table class="table table-hover table-striped">
								<thead class="${thead_color} align-middle text-center">
									<tr>
										<th rowspan="2" align="center">
											<input type="checkbox" name="checkall" onclick="checkUncheckAll(this)" />
										</th>
										<th rowspan="2" align="right">Número</th>
										<th rowspan="2" align="center">Destinação da via</th>
										<th colspan="3" align="center">Documento</th>
										<th colspan="2" align="center">Última Movimentação</th>
										<th rowspan="2">Descrição</th>
									</tr>
									<tr>
										<th align="center">Data</th>
										<th align="center">Lotação</th>
										<th align="center">Pessoa</th>
										<th align="center">Data</th>
										<th align="center">Pessoa</th>
									</tr>
								</thead>
												<tbody class="table-bordered">
								
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
									<c:set var="x" scope="request">chk_${m.id}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td width="2%" align="center">
										<input type="checkbox" name="${x}" value="true" ${x_checked} />
									</td>
									<td width="11.5%" align="right">
										<c:choose>
											<c:when test='${param.popup!="true"}'>
												<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${m.sigla}">${m.sigla}</a>										
											</c:when>
											<c:otherwise>
												<a href="javascript:opener.retorna_${param.propriedade}('${m.id}','${m.sigla},'');">${m.sigla}</a>
											</c:otherwise>
										</c:choose>
									</td>
									<c:if test="${not m.geral}">
										<td width="2%" align="center">${f:destinacaoPorNumeroVia(m.doc, m.numSequencia)}</td>
										<td width="5%" align="center">${m.doc.dtDocDDMMYY}</td>
										<td width="5%" align="center">
											<siga:selecionado sigla="${m.doc.lotaSubscritor.sigla}" descricao="${m.doc.lotaSubscritor.descricao}" />
										</td>
										<td width="5%" align="center">
											<siga:selecionado sigla="${m.doc.subscritor.iniciais}" descricao="${m.doc.subscritor.descricao}" />
										</td>
										<td width="5%" align="center">${m.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${m.ultimaMovimentacaoNaoCancelada.resp.iniciais}" descricao="${m.ultimaMovimentacaoNaoCancelada.resp.descricao}" />
										</td>
									</c:if>
									<c:if test="${m.geral}">
										<td width="2%" align="center"></td>
										<td width="5%" align="center">${m.doc.dtDocDDMMYY}</td>
										<td width="5%" align="center">
											<siga:selecionado sigla="${m.doc.lotaSubscritor.sigla}" descricao="${m.doc.lotaSubscritor.descricao}" />
										</td>
										<td width="5%" align="center">
											<siga:selecionado sigla="${m.doc.subscritor.iniciais}" descricao="${m.doc.subscritor.descricao}" />
										</td>
										 
										<td width="5%" align="center"></td>
										<td width="4%" align="center"></td>
										<!--<td width="4%" align="center"></td>
										<td width="10.5%" align="center"></td> -->
									</c:if>
									<td width="44%">${f:descricaoSePuderAcessar(m.doc, titular, lotaTitular)}</td>
								</tr>
								</c:if>
								</c:forEach>
								</tbody>
								<c:if test="${not empty primeiro}">
							</table>
						</div>
					</c:if>
			</c:forEach>
	</form>
	</div>
</siga:pagina>