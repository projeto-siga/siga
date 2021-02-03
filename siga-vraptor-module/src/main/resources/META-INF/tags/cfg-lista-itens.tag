<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ variable name-given="config" scope="NESTED" %>

<c:set var="tamanho" value="0" />

<c:forEach var="config" items="${listConfig}">
	<tr>
		<td style="${tipoDeConfiguracao.style('PESSOA')}"><c:if
				test="${not empty config.dpPessoa}">
				<siga:selecionado sigla="${config.dpPessoa.iniciais}"
					descricao="${config.dpPessoa.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('LOTACAO')}"><c:if
				test="${not empty config.lotacao}">
				<siga:selecionado sigla="${config.lotacao.sigla}"
					descricao="${config.lotacao.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('TIPO_DE_LOTACAO')}"><c:if
				test="${not empty config.cpTipoLotacao}">${config.cpTipoLotacao.dscTpLotacao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('CARGO')}"><c:if
				test="${not empty config.cargo}">${config.cargo.nomeCargo}</c:if></td>
		<td style="${tipoDeConfiguracao.style('FUNCAO')}"><c:if
				test="${not empty config.funcaoConfianca}">${config.funcaoConfianca.nomeFuncao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('ORGAO')}"><c:if
				test="${not empty config.orgaoUsuario}">${config.orgaoUsuario.acronimoOrgaoUsu}</c:if></td>
		<td style="${tipoDeConfiguracao.style('SERVICO')}"><c:if
				test="${not empty config.cpServico}">${config.cpServico.dscServico}</c:if></td>

		<td style="${tipoDeConfiguracao.style('PESSOA_OBJETO')}"><c:if
				test="${not empty config.pessoaObjeto}">
				<siga:selecionado sigla="${config.pessoaObjeto.iniciais}"
					descricao="${config.pessoaObjeto.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('LOTACAO_OBJETO')}"><c:if
				test="${not empty config.lotacaoObjeto}">
				<siga:selecionado sigla="${config.lotacaoObjeto.sigla}"
					descricao="${config.lotacaoObjeto.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('CARGO_OBJETO')}"><c:if
				test="${not empty config.cargoObjeto}">${config.cargoObjeto.nomeCargo}</c:if></td>
		<td style="${tipoDeConfiguracao.style('FUNCAO_OBJETO')}"><c:if
				test="${not empty config.funcaoConfiancaObjeto}">${config.funcaoConfiancaObjeto.nomeFuncao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('ORGAO_OBJETO')}"><c:if
				test="${not empty config.orgaoObjeto}">${config.orgaoObjeto.acronimoOrgaoUsu}</c:if></td>

		<jsp:doBody />

		<td style="${tipoDeConfiguracao.style('SITUACAO')}"><c:if
				test="${not empty config.cpSituacaoConfiguracao}">${config.cpSituacaoConfiguracao.dscSitConfiguracao}</c:if></td>
		<td><c:if test="${not empty nmTipoRetorno}">
				<c:url var="url" value="/app/configuracao/editar">
					<c:param name="id" value="${config.idConfiguracao}" />
					<c:param name="idMod" value="${idMod}" />
					<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
					<c:param name="campoFixo" value="${campoFixo}" />
					<c:param name="idFormaDoc" value="${idFormaDoc}" />
				</c:url>
			</c:if> <c:if test="${empty nmTipoRetorno}">
				<c:url var="url" value="/app/configuracao/editar">
					<c:param name="id" value="${config.idConfiguracao}" />
				</c:url>
			</c:if> <siga:links estilo="margin-bottom: 0; text-align: center;"
				buttons="${false}">
				<siga:link icon="pencil" titleImg="Alterar" url="${url}"
					estilo="margin-bottom: 0; text-align: center; padding: 2px;" />

				<c:if test="${not empty nmTipoRetorno}">
					<c:url var="urlExcluir" value="/app/configuracao/excluir">
						<c:param name="id" value="${config.idConfiguracao}" />
						<c:param name="idMod" value="${idMod}" />
						<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
						<c:param name="idFormaDoc" value="${idFormaDoc}" />
					</c:url>
				</c:if>
				<c:if test="${empty nmTipoRetorno}">
					<c:url var="urlExcluir" value="/app/configuracao/excluir">
						<c:param name="id" value="${config.idConfiguracao}" />
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
