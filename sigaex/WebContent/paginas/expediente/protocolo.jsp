<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Protocolo de Transferência</h2>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">

				<table class="gt-table">
					<tr>
					<tr>
						<td>De:</td>
						<td>${cadastrante.lotacao.descricao} -
							${cadastrante.descricao}</td>
					</tr>
					<tr>
						<td>Para:</td>
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
				<table class="gt-table" style="word-wrap: break-word; width: 100%;">
					<col width="22%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="44%" />
					<tr class="header">
						<td rowspan="2" align="right">Número</td>
						<td colspan="3" align="center">Documento</td>
						<td colspan="3" align="center">Última Movimentação</td>
						<td colspan="2" align="center">Atendente</td>
						<td rowspan="2">Descrição</td>
					</tr>
					<tr class="header">
						<td align="center">Data</td>
						<td align="center">Lotação</td>
						<td align="center">Pessoa</td>
						<td align="center">Data</td>
						<td align="center">Lotação</td>
						<td align="center">Pessoa</td>
						<td align="center">Lotação</td>
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
							<td align="right"><ww:url id="url" action="exibir"
									namespace="/expediente/doc">
									<ww:hidden name="sigla" value="%{documento[1].sigla}" />
								</ww:url> <ww:a href="%{url}">${documento[1].exMobil.sigla}</ww:a> <c:if
									test="${not documento[1].exMobil.geral}">
									<td align="center">${documento[0].dtDocDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].lotaSubscritor.sigla}"
											descricao="${documento[0].lotaSubscritor.descricao}" />
									</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}" />
									</td>
									<td align="center">${documento[1].dtMovDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].lotaSubscritor.sigla}"
											descricao="${documento[1].lotaSubscritor.descricao}" />
									</td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].subscritor.iniciais}"
											descricao="${documento[1].subscritor.descricao}" />
									</td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].lotaResp.sigla}"
											descricao="${documento[1].lotaResp.descricao}" />
									</td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].resp.iniciais}"
											descricao="${documento[1].resp.descricao}" />
									</td>
								</c:if> <c:if test="${documento[1].exMobil.geral}">
									<td align="center">${documento[0].dtDocDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}" />
									</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].lotaSubscritor.sigla}"
											descricao="${documento[0].lotaSubscritor.descricao}" />
									</td>
									<td align="center"></td>
									<td align="center"></td>
									<td align="center"></td>
									<td align="center"></td>
								</c:if>
							<td>${f:descricaoConfidencial(documento[0], lotaTitular)}</td>
						</tr>
					</c:forEach>


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
				<p align="center">Recebido em: _____/_____/_____ às _____:_____</p>
				<br />
				<br />
				<br />
				<p align="center">________________________________________________</p>
				<p align="center">Assinatura do Servidor</p>
			</ww:form>
		</div>
	</div>
</siga:pagina>