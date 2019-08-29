<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">

	<table style="float: none; clear: both;" width="100%" align="left"
		border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
		<tr bgcolor="#FFFFFF">
			<td width="100%">
				<table width="100%" border="0" cellpadding="2">
					<tr>
						<td width="100%" align="center" valign="bottom"><img
							src="/sigaex/imagens/brasao_sp_128.png" width="65" height="65" />
						</td>
					</tr>
					<tr>
						<td width="100%" align="center">
							<p style="font-size: 11pt;">
								<b>GOVERNO DO ESTADO DE S&Atilde;O PAULO</b><br />
							</p>
						</td>
					</tr>
					<tr>
						<td width="100%" align="center"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Relação de Remessa</h2>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">

				<table class="gt-table">
					<tr>
						<tr>
							<td>De:</td>
							<td>${lotaTitular.orgaoUsuario}- ${lotaTitular.descricao} -
								${cadastrante.descricao}</td>
						</tr>
						<tr>
							<td>Para:</td>
							<td>${mov.respSemDescrString}</td>
						</tr>
					<tr>
						<td>Data:</td>
						<td colspan="2">${mov.dtRegMovDDMMYYHHMMSS}</td>
					</tr>
					<c:if test="${mov.respDescrString != ''}">
						<tr>
							<td>Observação:</td>
							<td>${mov.respDescrString}</td>
						</tr>
					</c:if>
				</table>

			</div>

			<h3>Documento(s)</h3>

			<div class="gt-content-box gt-for-table">
				<table class="gt-table" style="word-wrap: break-word;">
					<col width="30%" />
					<col width="70%" />
					<tr class="header">
						<td>Número</td>
						<td>Descrição</td>
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
							<td>${documento[1].exMobil.codigo}</td>
							<td>${f:descricaoConfidencial(documento[0], lotaTitular)}</td>
						</tr>
					</c:forEach>
				</table>
			</div>

			<br />
			<form name="frm" action="principal" namespace="/" method="get"
				theme="simple">
				<input type="button" value="Imprimir"
					class="gt-btn-medium gt-btn-left"
					onclick="javascript: document.body.offsetHeight; window.print();" />
				<c:if test="${popup != true}">
					<input type="button" value="Voltar"
						class="gt-btn-medium gt-btn-left"
						onclick="javascript:history.back();history.back();history.back();" />
				</c:if>
			</form>
			<br /> <br />
			<div>
				<br /> <br />
				<p align="center">Recebido em: _____/_____/_____ às _____:_____</p>
				<br /> <br /> <br />
				<p align="center">________________________________________________</p>
				<p align="center">Assinatura do Servidor</p>
			</div>
		</div>
	</div>
</siga:pagina>