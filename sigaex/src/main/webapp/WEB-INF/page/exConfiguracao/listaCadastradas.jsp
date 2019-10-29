<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<h5>${tpConfiguracao.dscTpConfiguracao}
	<span style="float: right; font-size: 70%">(Situação default: </b>
		${tpConfiguracao.situacaoDefault.dscSitConfiguracao})
	</span>
</h5>


<table class="table table-sm table-striped" width="100%">
	<tr class="${thead_color}">
		<th align="center">Nível de acesso</th>
		<th align="center">Pessoa</th>
		<th align="center">Lotação</th>
		<th align="center">Tipo de Lotação</th>
		<th align="center">Função</th>
		<th align="center">Órgão</th>
		<th align="center">Órgão Objeto</th>
		<th align="center">Cargo</th>
		<th align="center">Tipo de Movimentação</th>
		<th align="center">Origem</th>
		<th align="center">Tipo de Espécie</th>
		<th align="center">Espécie</th>
		<th align="center">Modelo</th>
		<th align="center">Via</th>
		<th align="center">Classificação</th>
		<th align="center">Vínculo</th>
		<th align="center">Serviço</th>

		<th align="center">Pessoa Objeto</th>
		<th align="center">Lotação Objeto</th>
		<th align="center">Cargo Objeto</th>
		<th align="center">Função Objeto</th>

		<th align="center">Situação</th>
		<th align="center" style="min-width: 70px;"></th>
	</tr>
	<c:set var="tamanho" value="0" />

	<c:forEach var="configuracao" items="${listConfig}">
		<tr>
			<td><c:if test="${not empty configuracao.exNivelAcesso}">${configuracao.exNivelAcesso.nmNivelAcesso}(${configuracao.exNivelAcesso.grauNivelAcesso})</c:if>
			</td>
			<td><c:if test="${not empty configuracao.dpPessoa}">
					<siga:selecionado sigla="${configuracao.dpPessoa.iniciais}"
						descricao="${configuracao.dpPessoa.descricao}" />
				</c:if></td>
			<td><c:if test="${not empty configuracao.lotacao}">
					<siga:selecionado sigla="${configuracao.lotacao.sigla}"
						descricao="${configuracao.lotacao.descricao}" />
				</c:if></td>
			<td><c:if test="${not empty configuracao.cpTipoLotacao}">${configuracao.cpTipoLotacao.dscTpLotacao}</c:if></td>
			<td><c:if test="${not empty configuracao.funcaoConfianca}">${configuracao.funcaoConfianca.nomeFuncao}</c:if></td>
			<td><c:if test="${not empty configuracao.orgaoUsuario}">${configuracao.orgaoUsuario.acronimoOrgaoUsu}</c:if></td>
			<td><c:if test="${not empty configuracao.orgaoObjeto}">${configuracao.orgaoObjeto.acronimoOrgaoUsu}</c:if></td>
			<td><c:if test="${not empty configuracao.cargo}">${configuracao.cargo.nomeCargo}</c:if></td>
			<td><c:if test="${not empty configuracao.exTipoMovimentacao}">${configuracao.exTipoMovimentacao.descrTipoMovimentacao}</c:if></td>
			<td><c:if test="${not empty configuracao.exTipoDocumento}">${configuracao.exTipoDocumento.descrTipoDocumento}</c:if></td>
			<td><c:if test="${not empty configuracao.exTipoFormaDoc}">${configuracao.exTipoFormaDoc.descTipoFormaDoc}</c:if></td>
			<td><c:if test="${not empty configuracao.exFormaDocumento}">${configuracao.exFormaDocumento.descrFormaDoc}</c:if></td>
			<td><c:if test="${not empty configuracao.exModelo}">${configuracao.exModelo.nmMod}</c:if></td>
			<td><c:if test="${not empty configuracao.exVia}">${configuracao.exVia.exTipoDestinacao}(${configuracao.exVia.codVia})</c:if></td>
			<td><c:if test="${not empty configuracao.exClassificacao}">${configuracao.exClassificacao.classificacaoAtual.codificacao}</c:if></td>
			<td><c:if test="${not empty configuracao.exPapel}">${configuracao.exPapel.descPapel}</c:if></td>
			<td><c:if test="${not empty configuracao.cpServico}">${configuracao.cpServico.dscServico}</c:if></td>

			<td><c:if test="${not empty configuracao.pessoaObjeto}">
					<siga:selecionado sigla="${configuracao.pessoaObjeto.iniciais}"
						descricao="${configuracao.pessoaObjeto.descricao}" />
				</c:if></td>
			<td><c:if test="${not empty configuracao.lotacaoObjeto}">
					<siga:selecionado sigla="${configuracao.lotacaoObjeto.sigla}"
						descricao="${configuracao.lotacaoObjeto.descricao}" />
				</c:if></td>
			<td><c:if test="${not empty configuracao.cargoObjeto}">${configuracao.cargoObjeto.nomeCargo}</c:if></td>
			<td><c:if test="${not empty configuracao.funcaoConfiancaObjeto}">${configuracao.funcaoConfiancaObjeto.nomeFuncao}</c:if></td>

			<td><c:if
					test="${not empty configuracao.cpSituacaoConfiguracao}">${configuracao.cpSituacaoConfiguracao.dscSitConfiguracao}</c:if></td>
			<td><c:if test="${not empty nmTipoRetorno}">
					<c:url var="url" value="/app/expediente/configuracao/editar">
						<c:param name="id" value="${configuracao.idConfiguracao}" />
						<c:param name="idMod" value="${idMod}" />
						<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
						<c:param name="campoFixo" value="${campoFixo}" />
						<c:param name="idFormaDoc" value="${idFormaDoc}" />
					</c:url>
				</c:if> <c:if test="${empty nmTipoRetorno}">
					<c:url var="url" value="/app/expediente/configuracao/editar">
						<c:param name="id" value="${configuracao.idConfiguracao}" />
					</c:url>
				</c:if> <siga:links estilo="margin-bottom: 0; text-align: center;" buttons="${false}">
					<siga:link icon="pencil" titleImg="Alterar" url="${url}"
						estilo="margin-bottom: 0; text-align: center; padding: 2px;" />

					<c:if test="${not empty nmTipoRetorno}">
						<c:url var="urlExcluir"
							value="/app/expediente/configuracao/excluir">
							<c:param name="id" value="${configuracao.idConfiguracao}" />
							<c:param name="idMod" value="${idMod}" />
							<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
							<c:param name="idFormaDoc" value="${idFormaDoc}" />
						</c:url>
					</c:if>
					<c:if test="${empty nmTipoRetorno}">
						<c:url var="urlExcluir"
							value="/app/expediente/configuracao/excluir">
							<c:param name="id" value="${configuracao.idConfiguracao}" />
						</c:url>
					</c:if>

					<siga:link icon="delete" titleImg="Excluir" url="${urlExcluir}"
						popup="excluir" confirm="Deseja excluir configuração?" />
				</siga:links></td>
		</tr>
		<c:set var="tamanho" value="${tamanho + 1 }" />
	</c:forEach>
	</tbody>
</table>
