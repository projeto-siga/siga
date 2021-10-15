<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ variable name-given="cfg" scope="NESTED"%>

<c:set var="tamanho" value="0" />

<c:forEach var="cfg" items="${listConfig}">
	<tr>
		<td style="${tipoDeConfiguracao.style('PESSOA')}"><c:if
				test="${not empty cfg.dpPessoa}">
				<siga:selecionado sigla="${cfg.dpPessoa.iniciais}"
					descricao="${cfg.dpPessoa.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('LOTACAO')}"><c:if
				test="${not empty cfg.lotacao}">
				<siga:selecionado sigla="${cfg.lotacao.sigla}"
					descricao="${cfg.lotacao.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('TIPO_DE_LOTACAO')}"><c:if
				test="${not empty cfg.cpTipoLotacao}">${cfg.cpTipoLotacao.dscTpLotacao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('CARGO')}"><c:if
				test="${not empty cfg.cargo}">${cfg.cargo.nomeCargo}</c:if></td>
		<td style="${tipoDeConfiguracao.style('FUNCAO')}"><c:if
				test="${not empty cfg.funcaoConfianca}">${cfg.funcaoConfianca.nomeFuncao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('ORGAO')}"><c:if
				test="${not empty cfg.orgaoUsuario}">${cfg.orgaoUsuario.acronimoOrgaoUsu}</c:if></td>
		<td style="${tipoDeConfiguracao.style('SERVICO')}"><c:if
				test="${not empty cfg.cpServico}">${cfg.cpServico.dscServico}</c:if></td>

		<td style="${tipoDeConfiguracao.style('PESSOA_OBJETO')}"><c:if
				test="${not empty cfg.pessoaObjeto}">
				<siga:selecionado sigla="${cfg.pessoaObjeto.iniciais}"
					descricao="${cfg.pessoaObjeto.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('LOTACAO_OBJETO')}"><c:if
				test="${not empty cfg.lotacaoObjeto}">
				<siga:selecionado sigla="${cfg.lotacaoObjeto.sigla}"
					descricao="${cfg.lotacaoObjeto.descricao}" />
			</c:if></td>
		<td style="${tipoDeConfiguracao.style('CARGO_OBJETO')}"><c:if
				test="${not empty cfg.cargoObjeto}">${cfg.cargoObjeto.nomeCargo}</c:if></td>
		<td style="${tipoDeConfiguracao.style('FUNCAO_OBJETO')}"><c:if
				test="${not empty cfg.funcaoConfiancaObjeto}">${cfg.funcaoConfiancaObjeto.nomeFuncao}</c:if></td>
		<td style="${tipoDeConfiguracao.style('ORGAO_OBJETO')}"><c:if
				test="${not empty cfg.orgaoObjeto}">${cfg.orgaoObjeto.acronimoOrgaoUsu}</c:if></td>

		<jsp:doBody />

		<td style="${tipoDeConfiguracao.style('SITUACAO')}"><c:if
				test="${not empty cfg.cpSituacaoConfiguracao}">${cfg.cpSituacaoConfiguracao.descr}</c:if></td>
		<td><c:if test="${not empty nmTipoRetorno}">
				<c:url var="url" value="/app/configuracao/editar">
					<c:param name="id" value="${cfg.idConfiguracao}" />
					<c:param name="idMod" value="${idMod}" />
					<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
					<c:param name="campoFixo" value="${campoFixo}" />
					<c:param name="idFormaDoc" value="${idFormaDoc}" />
				</c:url>
			</c:if> <c:if test="${empty nmTipoRetorno}">
				<c:url var="url" value="/app/configuracao/editar">
					<c:param name="id" value="${cfg.idConfiguracao}" />
				</c:url>
			</c:if> <siga:links estilo="margin-bottom: 0; text-align: center;"
				buttons="${false}">
				<siga:link icon="pencil" titleImg="Alterar" url="${url}"
					estilo="margin-bottom: 0; text-align: center; padding: 2px;" />

				<c:if test="${not empty nmTipoRetorno}">
					<c:url var="urlExcluir" value="/app/configuracao/excluir">
						<c:param name="id" value="${cfg.idConfiguracao}" />
						<c:param name="idMod" value="${idMod}" />
						<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
						<c:param name="idFormaDoc" value="${idFormaDoc}" />
					</c:url>
				</c:if>
				<c:if test="${empty nmTipoRetorno}">
					<c:url var="urlExcluir" value="/app/configuracao/excluir">
						<c:param name="id" value="${cfg.idConfiguracao}" />
					</c:url>
				</c:if>

				<siga:link icon="delete" titleImg="Excluir" url="${urlExcluir}"
					popup="excluir" confirm="Deseja excluir configuração?" />

				<c:if test="${not empty erroEmConfiguracao[cfg.id]}">
					<span>${erroEmConfiguracao[cfg.id]}</span>
				</c:if>
			</siga:links></td>
	</tr>
	<c:set var="tamanho" value="${tamanho + 1 }" />
</c:forEach>
</tbody>
</table>
