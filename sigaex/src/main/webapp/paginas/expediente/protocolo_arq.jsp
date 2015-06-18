<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Arquivamento" popup="true">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
		<h2>Protocolo de Arquivamento</h2>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
			
		<table class="gt-table">
			<tr>
				<td>Cadastrante:</td>
				<td>${mov.respString}</td>
			</tr>
			<tr>
				<td>Data:</td>
				<td colspan="2">${mov.dtRegMovDDMMYYHHMMSS}</td>
			</tr>
		</table>
		
		</div>
		
		<h3>Documento(s)</h3>
		
		<div class="gt-content-box gt-for-table">
		<table class="gt-table" style="table-layout:fixed; word-wrap:break-word; width:100%;">
				<thead>
				<tr>
					<th rowspan="2" align="right">NÃºmero</th>
					<th colspan="3" align="center">Documento</th>
					<th colspan="3" align="center">Ãšltima MovimentaÃ§Ã£o</th>
					<th colspan="2" align="center">Atendente</th>
					<th rowspan="2">DescriÃ§Ã£o</th>
				</tr>
				<tr>
					<th align="center">Data</th>
					<th align="center">LotaÃ§Ã£o</th>
					<th align="center">Pessoa</th>
					<th align="center">Data</th>
					<th align="center">LotaÃ§Ã£o</th>
					<th align="center">Pessoa</th>
					<th align="center">LotaÃ§Ã£o</th>
					<th align="center">Pessoa</th>
				</tr>
				</thead>
				<tbody>
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
						<td width="11.5%" align="right"><ww:url id="url"
							action="exibir" namespace="/expediente/doc">
							<ww:param name="sigla">${documento[1].exMobil.sigla}</ww:param>
						</ww:url> <ww:a href="%{url}">${documento[1].exMobil.sigla}</ww:a> <c:if
							test="${not documento[1].exMobil.geral}">
							<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[0].lotaSubscritor.sigla}"
								descricao="${documento[0].lotaSubscritor.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[0].subscritor.iniciais}"
								descricao="${documento[0].subscritor.descricao}" /></td>
							<td width="5%" align="center">${documento[1].dtMovDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[1].lotaSubscritor.sigla}"
								descricao="${documento[1].lotaSubscritor.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[1].subscritor.iniciais}"
								descricao="${documento[1].subscritor.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[1].lotaResp.sigla}"
								descricao="${documento[1].lotaResp.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[1].resp.iniciais}"
								descricao="${documento[1].resp.descricao}" /></td>
						</c:if> <c:if test="${documento[1].exMobil.geral}">
							<td width="2%" align="center"></td>
							<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[0].subscritor.iniciais}"
								descricao="${documento[0].subscritor.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${documento[0].lotaSubscritor.sigla}"
								descricao="${documento[0].lotaSubscritor.descricao}" /></td>
							<td width="5%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="10.5%" align="center"></td>
						</c:if>
						<td width="44%">${f:descricaoConfidencial(documento[0],
						lotaTitular)}</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</div>
			
			<br />
			<ww:form name="frm" action="principal" namespace="/" method="GET"
				theme="simple">
			<input type="button" value="Imprimir" class="gt-btn-medium"
				onclick="javascript: document.body.offsetHeight; window.print();" />
			<c:if test="${param.popup != true}">
				<input type="button" value="Voltar"
					onclick="javascript:history.back();" />
			</c:if>
			<br />
			<br />
			<p align="center">Arquivado em: _____/_____/_____ Ã s _____:_____</p>
			<br />
			<br />
			<br />
			<p align="center">________________________________________________</p>
			<p align="center">Assinatura do Servidor</p>
			</ww:form>

	</div></div>
</siga:pagina>