<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>

	<h1>Documento: ${m.sigla}</h1>
	
<c:if test="{not empty m.apensosDiretosExcetoVolumeApensadoAoProximo}">
	<h1>Documento(s) Apensado(s)</h1>
	<table class="list" width="100%">
		<tr class="header">
			<td rowspan="3" align="right">NÃºmero</td>
			<td colspan="3" align="center">Documento</td>
			<td rowspan="3">Tipo</td>
			<td rowspan="3">DescriÃ§Ã£o</td>
		</tr>
		<tr class="header">
			<td rowspan="2" align="center">Data</td>
			<td colspan="2" align="center">ResponsÃ¡vel</td>
		</tr>
		<tr class="header">
			<td align="center">LotaÃ§Ã£o</td>
			<td align="center">Pessoa</td>
		</tr>
		<c:forEach var="mobItem" items="${m.apensosDiretosExcetoVolumeApensadoAoProximo}">
			<c:if test="${not mobItem.cancelada}">
				<c:choose>
					<c:when test="${mobItem.doc.eletronico}">
						<c:set var="exibedoc" value="eletronicoeven" />
					</c:when>
					<c:otherwise>
						<c:set var="exibedoc" value="even" />
					</c:otherwise>
				</c:choose>

				<tr class="${exibedoc}">
					<c:set var="podeAcessar"
						value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,mobItem)}" />

					<td width="11.5%" align="right"><c:choose>
						<c:when test='${param.popup!="true"}'>
							<c:choose>
								<c:when test="${podeAcessar eq true}">
									<ww:url id="url" action="exibir" namespace="/expediente/doc">
										<ww:param name="sigla">${mobItem.sigla}</ww:param>
									</ww:url>
									<ww:a href="%{url}">${mobItem.sigla}</ww:a>
								</c:when>
								<c:otherwise> 
												${mobItem.sigla}
											</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<a
								href="javascript:opener.retorna_${param.propriedade}('${mobItem.codigo}','montaDescricao(${mobItem.doc.idDoc}, ${mobItem.numSequencia},'${f:selDescricaoConfidencial(mobItem), lotaTitular, titular)}'));">${mobItem.codigo}</a>
						</c:otherwise>
					</c:choose></td>
					<td width="5%" align="center">${mobItem.doc.dtDocDDMMYY}</td>
					<td width="4%" align="center"><siga:selecionado
						sigla="${mobItem.doc.lotaSubscritor.siglaLotacao}"
						descricao="${mobItem.doc.lotaSubscritor.descricao}" /></td>
					<td width="4%" align="center"><siga:selecionado
						sigla="${mobItem.doc.subscritor.iniciais}"
						descricao="${mobItem.doc.subscritor.descricao}" /></td>
					<td width="6%">${mobItem.doc.descrFormaDoc}</td>

					<c:set var="acessivel" value="" />
					<c:set var="acessivel"
						value="${f:testaCompetencia('acessarAberto',titular,lotaTitular,mobItem)}" />

					<c:choose>
						<c:when test="${acessivel eq true}">
							<c:set var="estilo" value="" />
							<c:if
								test="${f:mostraDescricaoConfidencial(mobItem.doc, titular, lotaTitular) eq true}">
								<c:set var="estilo" value="confidencial" />
							</c:if>
							<td class="${estilo}" width="44%">${f:descricaoSePuderAcessar(mobItem.doc, titular,
							lotaTitular)}</td>
						</c:when>
						<c:otherwise>
							<td>[DescriÃ§Ã£o InacessÃ­vel]</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:if>
		</c:forEach>
	</table>
</c:if>