<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de TransferÃªncia" popup="true">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<ww:form name="frm" action="protocolo_transf" namespace="/expediente/mov" method="GET" theme="simple">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Protocolo de TransferÃªncia</h2>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">

				<table class="gt-table">
					<tr>
						<td>De:</td>
						<td>${cadastrante.lotacao.descricao} -
							${cadastrante.descricao}</td>
							<ww:hidden name="pessoa" value="%{cadastrante.sigla}" />
					</tr>
					
					<td>Para:</td>
						<td>${mov.respString}</td>
					</tr>
					<tr>
						<td>Data:</td>
						<td colspan="2">${mov.dtRegMovDDMMYYYYHHMMSS}</td>
						<ww:hidden name="dt" value="%{mov.dtRegMovDDMMYYYYHHMMSS}" />
					</tr>
				</table>

			</div>

			<h3>Documento(s) NÃ£o Transferido(s)</h3>

			<div class="gt-content-box gt-for-table">
				<table class="gt-table" style="word-wrap: break-word; width: 100%;">
					<col width="30%" />
					<col width="16%" />
					<col width="54%" />
					<tr class="header">
						<td align="center">Documento</td>
						<td align="center">LotaÃ§Ã£o</td>
						<td align="center">DescriÃ§Ã£o</td>
					</tr>
					<c:forEach var="documento" items="${itens[0]}">
						<tr >
							<td align="center">
							<ww:url id="url" action="exibir" namespace="/expediente/doc">
							<ww:param name="sigla">${documento[0].sigla}</ww:param>
							</ww:url> <ww:a href="%{url}">${documento[0].codigo}</ww:a> 
							<td align="center">
								<siga:selecionado sigla="${documento[0].doc.lotaSubscritor.sigla}" descricao="${documento[0].doc.lotaSubscritor.descricao}" />
							</td>
							<td align="center">
								<siga:selecionado sigla="${documento[1]}" descricao="${documento[1]}" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<br/>
		</div>
		
		<c:if test="${not empty itens[1]}">
		<ww:hidden name="itens" value="%{itens[1]}" />
		
		<h3>Documento(s) Transferido(s) Com Sucesso </h3>

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
						<td rowspan="2" align="right">NÃºmero</td>
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

					<c:forEach var="documento" items="${itens[1]}">
						<c:choose>
							<c:when test='${evenorodd == "even"}'>
								<c:set var="evenorodd" value="odd" />
							</c:when>
							<c:otherwise>
								<c:set var="evenorodd" value="even" />
							</c:otherwise>
						</c:choose>
						<tr class="${evenorodd}">
							<td align="right">
								<ww:url id="url" action="exibir" namespace="/expediente/doc">
									<ww:param name="sigla">${documento[1].exMobil.sigla}</ww:param>
								</ww:url> 
								<ww:a href="%{url}">${documento[1].exMobil.codigo}</ww:a>
								<c:if test="${not documento[1].exMobil.geral}">
								<ww:hidden name="itens" value="%{documento[1].exMobil.idMobil}" />
								
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
					<input type="submit" value="Gerar Protocolo" class="gt-btn-medium gt-btn-left" />
			</c:if>
			<input type="button" value="Voltar" class="gt-btn-medium gt-btn-left"  onclick="javascript:history.back();" />	
	</ww:form>			
	</div>
</siga:pagina>