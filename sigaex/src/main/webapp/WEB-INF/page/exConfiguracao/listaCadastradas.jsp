<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:cfg-lista-cabecalho>
	<th align="center"
		style="${tipoDeConfiguracao.style('NIVEL_DE_ACESSO')}">Nível de
		acesso</th>
	<th align="center"
		style="${tipoDeConfiguracao.style('TIPO_MOVIMENTACAO')}">Tipo de
		Movimentação</th>
	<th align="center"
		style="${tipoDeConfiguracao.style('TIPO_DOCUMENTO')}">Origem</th>
	<th align="center"
		style="${tipoDeConfiguracao.style('TIPO_FORMA_DOCUMENTO')}">Tipo
		de Espécie</th>
	<th align="center"
		style="${tipoDeConfiguracao.style('FORMA_DOCUMENTO')}">Espécie</th>
	<th align="center" style="${tipoDeConfiguracao.style('MODELO')}">Modelo</th>
	<th align="center" style="${tipoDeConfiguracao.style('VIA')}">Via</th>
	<th align="center" style="${tipoDeConfiguracao.style('CLASSIFICACAO')}">Classificação</th>
	<th align="center" style="${tipoDeConfiguracao.style('PAPEL')}">Vínculo</th>
</siga:cfg-lista-cabecalho>

<siga:cfg-lista-itens>
	<td style="${tipoDeConfiguracao.style('NIVEL_DE_ACESSO')}"><c:if
			test="${not empty cfg.exNivelAcesso}">${cfg.exNivelAcesso.nmNivelAcesso}(${cfg.exNivelAcesso.grauNivelAcesso})</c:if></td>
	<td style="${tipoDeConfiguracao.style('TIPO_MOVIMENTACAO')}"><c:if
			test="${not empty cfg.exTipoMovimentacao}">${cfg.exTipoMovimentacao.descr}</c:if></td>
	<td style="${tipoDeConfiguracao.style('TIPO_DOCUMENTO')}"><c:if
			test="${not empty cfg.exTipoDocumento}">${cfg.exTipoDocumento.descrTipoDocumento}</c:if></td>
	<td style="${tipoDeConfiguracao.style('TIPO_FORMA_DOCUMENTO')}"><c:if
			test="${not empty cfg.exTipoFormaDoc}">${cfg.exTipoFormaDoc.descTipoFormaDoc}</c:if></td>
	<td style="${tipoDeConfiguracao.style('FORMA_DOCUMENTO')}"><c:if
			test="${not empty cfg.exFormaDocumento}">${cfg.exFormaDocumento.descrFormaDoc}</c:if></td>
	<td style="${tipoDeConfiguracao.style('MODELO')}"><c:if
			test="${not empty cfg.exModelo}">${cfg.exModelo.nmMod}</c:if></td>
	<td style="${tipoDeConfiguracao.style('VIA')}"><c:if
			test="${not empty cfg.exVia}">${cfg.exVia.exTipoDestinacao}(${cfg.exVia.codVia})</c:if></td>
	<td style="${tipoDeConfiguracao.style('CLASSIFICACAO')}"><c:if
			test="${not empty cfg.exClassificacao}">${cfg.exClassificacao.classificacaoAtual.codificacao}</c:if></td>
	<td style="${tipoDeConfiguracao.style('PAPEL')}"><c:if
			test="${not empty cfg.exPapel}">${cfg.exPapel.descPapel}</c:if></td>
</siga:cfg-lista-itens>

