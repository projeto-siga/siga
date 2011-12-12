<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@ taglib prefix="ww" uri="/webwork"%>
<siga:pagina titulo="Lista Configurações">
	<h1>Configurações cadastradas (listagem por tipo):</h1>
	<br />
	<ww:url id="url" action="editar" namespace="/expediente/configuracao">
	</ww:url>
	<input type="button" value="Incluir Configuração"
		onclick="javascript:window.location.href='${url}'">
	<br>
	<br>
	<c:forEach var="tipoConfiguracao" items="${itens}">
		<h1>${tipoConfiguracao[0].dscTpConfiguracao}</h1>
		<b>Default: </b>
		<ww:select name="idSituacao" list="listaSituacao"
			listKey="idSitConfiguracao" listValue="dscSitConfiguracao"
			theme="simple" headerValue="[Indefinido]" headerKey="0" />
		<br />
		<br />
		<table class="list" width="100%">
			<tr class="header">
				<td align="center">ID</td>
				<td align="center">Nível de acesso</td>
				<td align="center">Pessoa</td>
				<td align="center">Lotação</td>
				<td align="center">Função</td>
				<td align="center">Órgão</td>
				<td align="center">Cargo</td>
				<td align="center">Tipo de Movimentação</td>
				<td align="center">Via</td>
				<td align="center">Modelo</td>
				<td align="center">Classificação</td>
				<td align="center">Tipo da forma de documento</td>
				<td align="center">Forma de documento</td>
				<td align="center">Tipo de Documento</td>
				<td align="center">Vínculo</td>
				<td align="center">Serviço</td>
				<td align="center"><b>Situação</b></td>
				<td></td>
			</tr>
			<c:set var="evenorodd" value="" />
			<c:set var="tamanho" value="0" />

			<c:forEach var="configuracao" items="${tipoConfiguracao[1]}">
				<tr class="${evenorodd}">
					<td align="right"><c:if test="${not empty configuracao.idConfiguracao}">${configuracao.idConfiguracao}</c:if></td>
					<td><c:if test="${not empty configuracao.exNivelAcesso}">${configuracao.exNivelAcesso.nmNivelAcesso}(${configuracao.exNivelAcesso.grauNivelAcesso})</c:if></td>
					<td><c:if test="${not empty configuracao.dpPessoa}">
						<siga:selecionado sigla="${configuracao.dpPessoa.iniciais}"
							descricao="${configuracao.dpPessoa.descricao}" />
					</c:if></td>
					<td><c:if test="${not empty configuracao.lotacao}">
						<siga:selecionado sigla="${configuracao.lotacao.sigla}"
							descricao="${configuracao.lotacao.descricao}" />
					</c:if></td>
					<td><c:if test="${not empty configuracao.funcaoConfianca}">${configuracao.funcaoConfianca.nomeFuncao}</c:if></td>
					<td><c:if test="${not empty configuracao.orgaoUsuario}">${configuracao.orgaoUsuario.nmOrgaoUsu}</c:if></td>
					<td><c:if test="${not empty configuracao.cargo}">${configuracao.cargo.nomeCargo}</c:if></td>
					<td><c:if test="${not empty configuracao.exTipoMovimentacao}">${configuracao.exTipoMovimentacao.descrTipoMovimentacao}</c:if></td>
					<td><c:if test="${not empty configuracao.exVia}">${configuracao.exVia.destinacao}(${configuracao.exVia.codVia})</c:if></td>
					<td><c:if test="${not empty configuracao.exModelo}">${configuracao.exModelo.nmMod}</c:if></td>
					<td><c:if test="${not empty configuracao.exClassificacao}">${configuracao.exClassificacao.descrClassificacao}</c:if></td>
					<td><c:if test="${not empty configuracao.exFormaDocumento.exTipoFormaDoc}">${configuracao.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc}</c:if></td>
					<td><c:if test="${not empty configuracao.exFormaDocumento}">${configuracao.exFormaDocumento.descrFormaDoc}</c:if></td>
					<td><c:if test="${not empty configuracao.exTipoDocumento}">${configuracao.exTipoDocumento.descrTipoDocumento}</c:if></td>
					<td><c:if test="${not empty configuracao.exPapel}">${configuracao.exPapel.descPapel}</c:if></td>
					<td><c:if test="${not empty configuracao.cpServico}">${configuracao.cpServico.dscServico}</c:if></td>
					<td><c:if
						test="${not empty configuracao.exSituacaoConfiguracao}">${configuracao.exSituacaoConfiguracao.dscSitConfiguracao}</c:if></td>
					<td><ww:url id="url" action="editar"
						namespace="/expediente/configuracao">
						<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
					</ww:url> <ww:a href="%{url}">Alterar<br>
					</ww:a> <ww:url id="url" action="excluir"
						namespace="/expediente/configuracao">
						<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
					</ww:url> <ww:a href="%{url}">Excluir</ww:a></td>
				</tr>
				<c:choose>
					<c:when test='${evenorodd == "even"}'>
						<c:set var="evenorodd" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="evenorodd" value="even" />
					</c:otherwise>
				</c:choose>
				<c:set var="tamanho" value="${tamanho + 1 }" />
			</c:forEach>
			<tr class="footer">
				<td colspan="8">Total Listado: ${tamanho}</td>
			</tr>
		</table>
		<br />
	</c:forEach>
</siga:pagina>
