<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Recebimento em Lote">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">
	<ww:url id="url" action="receber_lote" namespace="/expediente/mov"/>
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

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Recebimento em Lote</h2>
			<div class="gt-content-box gt-for-table">

	<ww:form name="frm" action="receber_lote_gravar"
		namespace="/expediente/mov" method="GET" theme="simple">
		<ww:token />
		<ww:hidden name="postback" value="1" />
		<table class="gt-form-table">
			<tr class="header">
				<td colspan="2">Recebimento</td>
			</tr>
			<tr>
				<td colspan="2"><ww:submit value="Receber"  cssClass="gt-btn-small gt-btn-left" /></td>
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
						<h2>Atendente: <c:choose>
							<c:when test="${secao==0}">${titular.descricao}</c:when>
							<c:otherwise>${lotaTitular.descricao}</c:otherwise>
						</c:choose></h2>
						<div class="gt-content-box gt-for-table">
						<table class="gt-table">
							<tr class="header">
								<td rowspan="2" align="center"><input type="checkbox"
									name="checkall" onclick="checkUncheckAll(this)" /></td>
								<td rowspan="2" align="right">NÃºmero</td>
								<td colspan="3" align="center">Documento</td>
								<td colspan="2" align="center">Ãšltima MovimentaÃ§Ã£o</td>
								<td rowspan="2">DescriÃ§Ã£o</td>
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
								<c:set var="x" scope="request">chk_${m.id}</c:set>
								<c:remove var="x_checked" scope="request" />
								<c:if test="${param[x] == 'true'}">
									<c:set var="x_checked" scope="request">checked</c:set>
								</c:if>
								<td width="2%" align="center"><input type="checkbox"
									name="${x}" value="true" ${x_checked} /></td>
								<td width="11.5%" align="right"><c:choose>
									<c:when test='${param.popup!="true"}'>
										<ww:url id="url" action="exibir" namespace="/expediente/doc">
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
								<td width="44%">${f:descricaoSePuderAcessar(m.doc, titular,
								lotaTitular)}</td>
							</tr>
							</c:if>
							</c:forEach>
							<c:if test="${not empty primeiro}">
						</table>
						</div>
					</c:if>
			</c:forEach>
	</ww:form>

	</div></div>
</siga:pagina>
