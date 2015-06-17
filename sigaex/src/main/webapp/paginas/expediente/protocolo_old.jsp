<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<c:set var="baseURL" value="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}" />

<c:set var="titulo_pagina" scope="request">Protocolo de TransferÃªncia</c:set>

<script type="text/javascript" language="Javascript1.1"
	src="<c:url value="/staticJavascript.action"/>"></script>

<c:import url="${baseURL}/siga/paginas/cabecalho_popup.jsp" />

<ww:form name="frm" action="principal" namespace="/" method="GET"
	theme="simple">
	<h1>Protocolo de TransferÃªncia</h1>
	<table>
		<tr>
		<tr>
			<td>De:</td>
			<td>${cadastrante.lotacao.descricao} - ${cadastrante.descricao}</td>
		</tr>
		<tr>
			<td>Para:</td>
			<td>${mov.respString}</td>
		</tr>
		<tr>
			<td>Data:</td>
			<td colspan="2">${dtAtualDDMMYYHHMMSS}</td>
		</tr>
		<table class="list" width="100%">
			<tr class="header">
				<td rowspan="2" align="right">NÃºmero</td>
				<td rowspan="2" align="center">Via</td>
				<td colspan="3" align="center">Documento</td>
				<td colspan="3" align="center">Ãšltima MovimentaÃ§Ã£o</td>
				<td colspan="2" align="center">Atendente</td>
				<td rowspan="2">DescriÃ§Ã£o</td>
			</tr>
			<tr class="header">
				<td align="center">Data</td>
				<td align="center">LotaÃ§Ã£o</td>
				<td align="center">Pessoa</td>
				<td align="center">Data</td>
				<td align="center">LotaÃ§Ã£o</td>
				<td align="center">Pessoa</td>
				<td align="center">LotaÃ§Ã£o</td>
				<td align="center">Pessoa</td>
			</tr>

			<c:forEach var="documento" items="${itens}">
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
							<ww:url id="url" action="exibir" namespace="/expediente/doc">
								<ww:param name="sigla">${documento[1].sigla}</ww:param>
							</ww:url>
							<ww:a href="%{url}">${documento[0].codigo}</ww:a>
						</c:when>
						<c:otherwise>
							<a
								href="javascript:opener.retorna_${param.propriedade}('${documento[1].idMov}','${documento[0].codigo}-'+String.fromCharCode(${documento[1].numVia}+64),''); opener.ExMovimentacaoForm.numViaDocBusca.value=${documento[1].numVia};">${documento[0].codigo}</a>
						</c:otherwise>
					</c:choose></td>
					<c:if test="${documento[1].numVia != 0}">
						<td width="2%" align="center">${documento[1].numVia}</td>
						<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[0].lotaSubscritor.siglaLotacao}"
							descricao="${documento[0].lotaSubscritor.descricao}" /></td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[0].subscritor.iniciais}"
							descricao="${documento[0].subscritor.descricao}" /></td>
						<td width="5%" align="center">${documento[1].dtMovDDMMYY}</td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[1].lotaSubscritor.siglaLotacao}"
							descricao="${documento[1].lotaSubscritor.descricao}" /></td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[1].subscritor.iniciais}"
							descricao="${documento[1].subscritor.descricao}" /></td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[1].lotaResp.siglaLotacao}"
							descricao="${documento[1].lotaResp.descricao}" /></td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[1].resp.iniciais}"
							descricao="${documento[1].resp.descricao}" /></td>
					</c:if>
					<c:if test="${documento[1].numVia == 0}">
						<td width="2%" align="center"></td>
						<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[0].subscritor.iniciais}"
							descricao="${documento[0].subscritor.descricao}" /></td>
						<td width="4%" align="center"><siga:selecionado
							sigla="${documento[0].lotaSubscritor.siglaLotacao}"
							descricao="${documento[0].lotaSubscritor.descricao}" /></td>
						<td width="5%" align="center"></td>
						<td width="4%" align="center"></td>
						<td width="4%" align="center"></td>
						<td width="10.5%" align="center"></td>
					</c:if>
					<td width="44%">${f:descricaoConfidencial(documento[0], lotaTitular)}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
		<input type="button" value="Imprimir"
			onclick="javascript: document.body.offsetHeight; window.print();" />
		<c:if test="${param.popup != true}">
			<input type="button" value="Voltar" onclick="javascript:history.back();" />
		</c:if>
		<br />
		<br />
		<p align="center">Recebido em: _____/_____/_____ Ã s _____:_____</p>
		<br />
		<br />
		<br />
		<p align="center">________________________________________________</p>
		<p align="center">Assinatura do Servidor</p>
		</ww:form>

		<c:import url="${baseURL}/siga/paginas/rodape_popup.jsp" />