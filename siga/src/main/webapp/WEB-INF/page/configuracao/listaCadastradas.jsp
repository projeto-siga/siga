<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<h5>${tipoDeConfiguracao.descr}
	<span style="float: right; font-size: 70%">(Situação default: </b>
		${tpConfiguracao.situacaoDefault.dscSitConfiguracao})
	</span>
</h5>


<table class="table table-sm table-striped" width="100%">
	<tr class="${thead_color}">
		<th align="center" style="${tipoDeConfiguracao.style('PESSOA')}">Pessoa</th>
		<th align="center" style="${tipoDeConfiguracao.style('LOTACAO')}">Lotação</th>
		<th align="center"
			style="${tipoDeConfiguracao.style('TIPO_DE_LOTACAO')}">Tipo de
			Lotação</th>
		<th align="center" style="${tipoDeConfiguracao.style('CARGO')}">Cargo</th>
		<th align="center" style="${tipoDeConfiguracao.style('FUNCAO')}">Função</th>
		<th align="center" style="${tipoDeConfiguracao.style('ORGAO')}">Órgão</th>
		<th align="center" style="${tipoDeConfiguracao.style('SERVICO')}">Serviço</th>

		<th align="center"
			style="${tipoDeConfiguracao.style('PESSOA_OBJETO')}">Pessoa
			Objeto</th>
		<th align="center"
			style="${tipoDeConfiguracao.style('LOTACAO_OBJETO')}">Lotação
			Objeto</th>
		<th align="center" style="${tipoDeConfiguracao.style('CARGO_OBJETO')}">Cargo
			Objeto</th>
		<th align="center"
			style="${tipoDeConfiguracao.style('FUNCAO_OBJETO')}">Função
			Objeto</th>
		<th align="center" style="${tipoDeConfiguracao.style('ORGAO_OBJETO')}">Órgão
			Objeto</th>

		<th align="center"
			style="${tipoDeConfiguracao.style('DEFINICAO_DE_PROCEDIMENTO')}">Diagrama</th>

		<th align="center" style="${tipoDeConfiguracao.style('SITUACAO')}">Situação</th>
		<th align="center" style="min-width: 70px;"></th>
	</tr>
	<c:set var="tamanho" value="0" />

	<c:forEach var="configuracao" items="${listConfig}">
		<tr>
			<td style="${tipoDeConfiguracao.style('PESSOA')}"><c:if
					test="${not empty configuracao.dpPessoa}">
					<siga:selecionado sigla="${configuracao.dpPessoa.iniciais}"
						descricao="${configuracao.dpPessoa.descricao}" />
				</c:if></td>
			<td style="${tipoDeConfiguracao.style('LOTACAO')}"><c:if
					test="${not empty configuracao.lotacao}">
					<siga:selecionado sigla="${configuracao.lotacao.sigla}"
						descricao="${configuracao.lotacao.descricao}" />
				</c:if></td>
			<td style="${tipoDeConfiguracao.style('TIPO_DE_LOTACAO')}"><c:if
					test="${not empty configuracao.cpTipoLotacao}">${configuracao.cpTipoLotacao.dscTpLotacao}</c:if></td>
			<td style="${tipoDeConfiguracao.style('CARGO')}"><c:if
					test="${not empty configuracao.cargo}">${configuracao.cargo.nomeCargo}</c:if></td>
			<td style="${tipoDeConfiguracao.style('FUNCAO')}"><c:if
					test="${not empty configuracao.funcaoConfianca}">${configuracao.funcaoConfianca.nomeFuncao}</c:if></td>
			<td style="${tipoDeConfiguracao.style('ORGAO')}"><c:if
					test="${not empty configuracao.orgaoUsuario}">${configuracao.orgaoUsuario.acronimoOrgaoUsu}</c:if></td>
			<td style="${tipoDeConfiguracao.style('SERVICO')}"><c:if
					test="${not empty configuracao.cpServico}">${configuracao.cpServico.dscServico}</c:if></td>

			<td style="${tipoDeConfiguracao.style('PESSOA_OBJETO')}"><c:if
					test="${not empty configuracao.pessoaObjeto}">
					<siga:selecionado sigla="${configuracao.pessoaObjeto.iniciais}"
						descricao="${configuracao.pessoaObjeto.descricao}" />
				</c:if></td>
			<td style="${tipoDeConfiguracao.style('LOTACAO_OBJETO')}"><c:if
					test="${not empty configuracao.lotacaoObjeto}">
					<siga:selecionado sigla="${configuracao.lotacaoObjeto.sigla}"
						descricao="${configuracao.lotacaoObjeto.descricao}" />
				</c:if></td>
			<td style="${tipoDeConfiguracao.style('CARGO_OBJETO')}"><c:if
					test="${not empty configuracao.cargoObjeto}">${configuracao.cargoObjeto.nomeCargo}</c:if></td>
			<td style="${tipoDeConfiguracao.style('FUNCAO_OBJETO')}"><c:if
					test="${not empty configuracao.funcaoConfiancaObjeto}">${configuracao.funcaoConfiancaObjeto.nomeFuncao}</c:if></td>
			<td style="${tipoDeConfiguracao.style('ORGAO_OBJETO')}"><c:if
					test="${not empty configuracao.orgaoObjeto}">${configuracao.orgaoObjeto.acronimoOrgaoUsu}</c:if></td>
			<td style="${tipoDeConfiguracao.style('DEFINICAO_DE_PROCEDIMENTO')}"><c:if
					test="${not empty configuracao.definicaoDeProcedimento}">${configuracao.definicaoDeProcedimento.nome}</c:if></td>
			<td style="${tipoDeConfiguracao.style('SITUACAO')}"><c:if
					test="${not empty configuracao.cpSituacaoConfiguracao}">${configuracao.cpSituacaoConfiguracao.dscSitConfiguracao}</c:if></td>
			<td><c:if test="${not empty nmTipoRetorno}">
					<c:url var="url" value="/app/configuracao/editar">
						<c:param name="id" value="${configuracao.idConfiguracao}" />
						<c:param name="idMod" value="${idMod}" />
						<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
						<c:param name="campoFixo" value="${campoFixo}" />
						<c:param name="idFormaDoc" value="${idFormaDoc}" />
					</c:url>
				</c:if> <c:if test="${empty nmTipoRetorno}">
					<c:url var="url" value="/app/configuracao/editar">
						<c:param name="id" value="${configuracao.idConfiguracao}" />
					</c:url>
				</c:if> <siga:links estilo="margin-bottom: 0; text-align: center;"
					buttons="${false}">
					<siga:link icon="pencil" titleImg="Alterar" url="${url}"
						estilo="margin-bottom: 0; text-align: center; padding: 2px;" />

					<c:if test="${not empty nmTipoRetorno}">
						<c:url var="urlExcluir" value="/app/configuracao/excluir">
							<c:param name="id" value="${configuracao.idConfiguracao}" />
							<c:param name="idMod" value="${idMod}" />
							<c:param name="nmTipoRetorno" value="${nmTipoRetorno}" />
							<c:param name="idFormaDoc" value="${idFormaDoc}" />
						</c:url>
					</c:if>
					<c:if test="${empty nmTipoRetorno}">
						<c:url var="urlExcluir" value="/app/configuracao/excluir">
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
