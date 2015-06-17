<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="AnotaÃ§Ã£o em Lote">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">
	<ww:url id="url" action="anotar_lote" namespace="/expediente/mov"/>
	function sbmt(offset) {
		frm.action = '${url}';
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
			alert('DescriÃ§Ã£o com mais de 255 caracteres');
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

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>AnotaÃ§Ã£o em Lote</h2>

			<div class="gt-content-box gt-for-table">


	<ww:form name="frm" action="anotar_lote_gravar"
		namespace="/expediente/mov" method="GET" theme="simple">
		
		<ww:token/>
		<ww:hidden name="postback" value="1" />
		<table class="gt-form-table">
			<tr>
				<td>
					<table class="form" width="100%">
						<tr class="header">
							<td colspan="2">AnotaÃ§Ã£o</td>
						</tr>
						<tr>
							<td>Data:</td>
							<td><ww:textfield name="dtMovString"
								onblur="javascript:verifica_data(this,0);" /></td>
						</tr>
						<tr>
							<td>ResponsÃ¡vel:</td>
							<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
							&nbsp;&nbsp;<ww:checkbox theme="simple" name="substituicao"
								onclick="javascript:displayTitular(this);" />Substituto</td>
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
						<td colspan="3"><siga:selecao propriedade="titular"
									tema="simple" modulo="siga"/></td>
						</tr>
						<tr>
							<td>FunÃ§Ã£o do ResponsÃ¡vel:</td>
							<td><input type="hidden" name="campos"
								value="nmFuncaoSubscritor" /> <ww:textfield
								name="nmFuncaoSubscritor" size="50" maxLength="128" theme="simple"/> (opcional)</td>
						</tr>
						<tr>
							<td>Nota</td>
							<td><ww:textarea name="descrMov" cols="60" rows="5"
								onkeyup="corrige();tamanho();" onblur="tamanho();"
								onclick="tamanho();" />
							<div id="Qtd">Restam&nbsp;255&nbsp;Caracteres</div>
							</td>
						</tr>
		
						<c:if test="${tipoResponsavel == 3}">
							<tr>
								<td>ObservaÃ§Ã£o</td>
								<td><ww:textfield size="30" name="obsOrgao" /></td>
							</tr>
						</c:if>
		
						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok" class="gt-btn-small gt-btn-left" /> <input type="button"
								value="Cancela" onclick="javascript:history.back();" class="gt-btn-small gt-btn-left" /></td>
						</tr>
					</table>
		
				</td>
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
								<td rowspan="2" align="center">DestinaÃ§Ã£o da via</td>
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
						</table>
						</div>
					</c:if>
			</c:forEach>
	</ww:form>
	
		</div></div>
</siga:pagina>