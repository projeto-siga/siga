<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Segunda Via de Protocolo">

	<script type="text/javascript" language="Javascript1.1">
	<c:url var="url" value="/app/expediente/mov/via_protocolo"/>
	function sbmt(offset) {
		frm.action = '${url}';
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

	<form name="frm" action="via_protocolo_gravar" method="GET" theme="simple">
		<input type="hidden" name="postback" value="1" />
		
		<table class="form">
			<tr class="header">
				<td colspan="2">2&ordf; Via de Protocolo</td>
			</tr>
			<tr>
				<td>Atendente</td>

				<td>
					<select name="tipoResponsavel" onchange="javascript:sbmt();"> 
						<c:forEach items="${listaTipoResp}" var="item">
							<option value="${item.key}" ${item.key == tipoResponsavel ? 'selected' : ''}>
								${item.value}
							</option>
						</c:forEach>
					</select>
					<c:choose>
						<c:when test="${tipoResponsavel == 1}">
							<siga:selecao propriedade="lotaResponsavel" tema="simple" reler="sim" modulo="siga"/>
						</c:when>
						<c:when test="${tipoResponsavel == 2}">
							<siga:selecao propriedade="responsavel" tema="simple" reler="sim" modulo="siga"/>
						</c:when>
						<c:when test="${tipoResponsavel == 3}">
							<siga:selecao propriedade="cpOrgao" tema="simple" reler="sim" modulo="siga"/>
						</c:when>
					</c:choose>					
				</td>
				<c:if test="${tipoResponsavel == 3}">
					<tr>
						<td>Observação</td>
						<td><input type="text" size="30" name="obsOrgao" /></td>
					</tr>
				</c:if>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Emitir" /></td>
			</tr>
		</table>
		<br />


		<c:forEach var="secao" begin="0" end="1">
			<c:remove var="primeiro" />
			<c:forEach var="m" items="${itens}">
				<c:if
					test="${(secao==0 and not m.emTransitoExterno) or (secao==1 and m.emTransitoExterno)}">
					<c:if test="${empty primeiro}">
						<h1><c:choose>
							<c:when test="${secao==0}">Documentos em Trânsito</c:when>
							<c:otherwise>Documentos Transferidos a Órgão Externo</c:otherwise>
						</c:choose></h1>

						<table class="list" width="100%">
							<tr class="header">
								<td rowspan="2" align="center"><input type="checkbox"
									name="checkall" onclick="checkUncheckAll(this)" /></td>
								<td rowspan="2" align="right">Número</td>
								<td colspan="3" align="center">Documento</td>
								<td colspan="2" align="center">Última Movimentação</td>
								<td rowspan="2">Descrição</td>
							</tr>
							<tr class="header">
								<td align="center">Data</td>
								<td align="center">Lotação</td>
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
								<c:set var="x" scope="request">chk_${m.id}</c:set>
								<c:remove var="x_checked" scope="request" />
								<c:if test="${param[x] == 'true'}">
									<c:set var="x_checked" scope="request">checked</c:set>
								</c:if>
								<td width="2%" align="center"><input type="checkbox"
									name="${x}" value="true" ${x_checked} /></td>
								<td width="11.5%" align="right"><c:choose>
									<c:when test='${param.popup!="true"}'>
										<c:url var="url" value="/app/expediente/doc/exibir">
											<c:param name="sigla">${m.sigla}</c:param>
										</c:url>
										<a href="${url}">${m.sigla}</a>
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
								<td width="44%">${f:descricaoConfidencial(m.doc,lotaTitular)}</td>
							</tr>
							</c:if>
							</c:forEach>
							<c:if test="${not empty primeiro}">
						</table>
					</c:if>
			</c:forEach>
	</form>
</siga:pagina>