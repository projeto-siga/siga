<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Protocolo de Transferência" popup="true">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Protocolo de Transferência</h2>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">

				<table class="gt-table">
					<tr>
						<tr>
							<td>De:</td>
							<td>${lotaTitular.descricao} -
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
							<td align="right"><a
								href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].exMobil.sigla}">${documento[1].exMobil.codigo}</a>
								<c:if test="${not documento[1].exMobil.geral}">
									<td align="center">${documento[0].dtDocDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].lotaSubscritor.sigla}"
											descricao="${documento[0].lotaSubscritor.descricao}" /></td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}" /></td>
									<td align="center">${documento[1].dtMovDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].lotaSubscritor.sigla}"
											descricao="${documento[1].lotaSubscritor.descricao}" /></td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].subscritor.iniciais}"
											descricao="${documento[1].subscritor.descricao}" /></td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].lotaResp.sigla}"
											descricao="${documento[1].lotaResp.descricao}" /></td>
									<td align="center"><siga:selecionado
											sigla="${documento[1].resp.iniciais}"
											descricao="${documento[1].resp.descricao}" /></td>
								</c:if> <c:if test="${documento[1].exMobil.geral}">
									<td align="center">${documento[0].dtDocDDMMYY}</td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}" /></td>
									<td align="center"><siga:selecionado
											sigla="${documento[0].lotaSubscritor.sigla}"
											descricao="${documento[0].lotaSubscritor.descricao}" /></td>
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
			<form name="frm" action="principal" namespace="/" method="get"
				theme="simple">
				<input type="button" value="Imprimir"
					class="gt-btn-medium gt-btn-left"
					onclick="javascript: document.body.offsetHeight; window.print();" />
				<c:if test="${popup != true}">
					<input type="button" value="Voltar"
						class="gt-btn-medium gt-btn-left"
						onclick="javascript:history.back();history.back();" />
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