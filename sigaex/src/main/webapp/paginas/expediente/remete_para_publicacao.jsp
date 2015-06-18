<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Remessa para Publicação">

<script type="text/javascript" language="Javascript1.1"
	src="<c:url value="/staticJavascript.action"/>"></script>

<script type="text/javascript" language="Javascript1.1">
	<ww:url id="url" action="remeter_para_publicacao" namespace="/expediente/mov"/>
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

<ww:form name="frm" action="remeter_para_publicacao_gravar"
	namespace="/expediente/mov" method="GET" theme="simple">
	<ww:hidden name="postback" value="1" />
	<br />
	<c:forEach begin="1" end="3" var="i">
		<c:choose>
			<c:when test="${i == 1}">
				<h1>Documentos agendados</h1>
				<c:set var="elementos" value="${itensAgendados}" />
				<table class="form">
					<tr>
						<td><ww:submit value="Remeter" /></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${i == 2}">
				<h1>Documentos remetidos para publicação</h1>
				<c:set var="elementos" value="${itensRemetidos}" />
			</c:when>
			<c:when test="${i == 3}">
				<h1>Documentos publicados</h1>
				<c:set var="elementos" value="${itensPublicados}" />
			</c:when>
		</c:choose>
		<table class="list" width="100%">
			<tr class="header">
				<c:if test="${i == 1}">
					<td rowspan="2" align="center"><input type="checkbox"
						name="checkall" onclick="checkUncheckAll(this)" /></td>
				</c:if>
				<td rowspan="2" align="right">Número</td>
				<c:choose>
					<c:when test="${i == 1}">
						<td rowspan="2">Data de agendamento</td>
					</c:when>
					<c:when test="${i == 2}">
						<td rowspan="2">Data de remessa</td>
					</c:when>
				</c:choose>
				<td colspan="3" align="center">Documento</td>
				<td rowspan="2">Descrição</td>

				</td>
			</tr>
			<tr class="header">
				<td align="center">Data</td>
				<td align="center">Lotação</td>
				<td align="center">Pessoa</td>
			</tr>

			<c:forEach var="documento" items="${elementos}">
				<c:choose>
					<c:when test='${evenorodd == "even"}'>
						<c:set var="evenorodd" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="evenorodd" value="even" />
					</c:otherwise>
				</c:choose>
				<tr class="${evenorodd}">
					<c:if test="${i == 1}">
						<c:set var="x" scope="request">chk_${documento.idDoc}_}</c:set>
						<c:remove var="x_checked" scope="request" />
						<c:if test="${param[x] == 'true'}">
							<c:set var="x_checked" scope="request">checked</c:set>
						</c:if>
						<td width="2%" align="center"><input type="checkbox"
							name="${x}" value="true" ${x_checked} /></td>
					</c:if>
					<td width="11.5%" align="right"><ww:url id="url"
						action="exibir" namespace="/expediente/doc">
						<ww:param name="id">${documento.idDoc}</ww:param>
					</ww:url> <ww:a href="%{url}">${documento.codigo}</ww:a></td>
					<c:choose>
						<c:when test="${i == 1}">
							<td rowspan="2" width="11.5%">${documento.dtUltimoAgendamento}</td>
						</c:when>
						<c:when test="${i == 2}">
							<td rowspan="2" width="11.5%">${documento.dtUltimaRemessaParaPublicacao}</td>
						</c:when>
					</c:choose>
					<td width="5%" align="center">${documento.dtDocDDMMYY}</td>
					<td width="4%" align="center"><siga:selecionado
						sigla="${documento.subscritor.iniciais}"
						descricao="${documento.subscritor.descricao}" /></td>
					<td width="4%" align="center"><siga:selecionado
						sigla="${documento.lotaSubscritor.sigla}"
						descricao="${documento.lotaSubscritor.descricao}" /></td>

					<td width="44%">${f:descricaoSePuderAcessar(documento, titular,
					lotaTitular)}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
	</c:forEach>
</ww:form>

</siga:pagina>