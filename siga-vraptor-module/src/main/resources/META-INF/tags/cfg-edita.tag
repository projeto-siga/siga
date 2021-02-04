<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css"
		type="text/css" media="screen, projection" />
	<link rel="stylesheet"
		href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
		media="screen, projection" />

	<script type="text/javascript" language="Javascript1.1">
		function sbmt() {
			editar_gravar = 'editar';
			editar_gravar.submit();
		}
	</script>

	<div class="container-fluid">
		<div class="card bg-light mb-3">

			<form action="editar_gravar">
				<input type="hidden" name="postback" value="1" /> <input
					type="hidden" name="nmTipoRetorno" value="${nmTipoRetorno}" /> <input
					type="hidden" name="id" value="${id}" />

				<c:set var="dataFim" value="" />

				<div class="card-header">
					<h5>Cadastro de configuração</h5>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-sm-6 col-lg-4">
							<div class="form-group">
								<label>Tipo de Configuração</label>
								<c:choose>
									<c:when test="${not empty config.cpTipoConfiguracao}">
										<input type="hidden" name="idTpConfiguracao"
											value="${config.cpTipoConfiguracao.idTpConfiguracao}" />
										<label class="form-control">${config.cpTipoConfiguracao.dscTpConfiguracao}</label>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="idTpConfiguracao"
											value="${tipoDeConfiguracao.id}" />
										<label class="form-control">${tipoDeConfiguracao.descr}</label>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('SITUACAO')}">
							<div class="form-group">
								<label>Situação</label>
								<siga:select name="idSituacao" list="situacoes" listKey="id"
									listValue="descr" theme="simple" headerValue="[Indefinido]"
									headerKey="0" value="${idSituacao}"
									required="${tipoDeConfiguracao.obrigatorio('SITUACAO')}" />
							</div>
						</div>
					</div>
					<div class="row"
						style="${tipoDeConfiguracao.style('PESSOA,TIPO_DE_LOTACAO,LOTACAO,CARGO,FUNCAO,ORGAO,PESSOA_OBJETO,LOTACAO_OBJETO,CARGO_OBJETO,FUNCAO_OBJETO,ORGAO_OBJETO')}">
						<div class="col col-12">
							<p class="alert alert-dark">Restrições Gerais à Aplicação da
								Regra</p>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('PESSOA')}">
							<div class="form-group">
								<label>Pessoa</label>
								<siga:selecao propriedade="pessoa" tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('TIPO_DE_LOTACAO')}">
							<div class="form-group">
								<label>Tipo lotação</label>
								<siga:select name="idTpLotacao" list="listaTiposLotacao"
									theme="simple" listKey="idTpLotacao" listValue="dscTpLotacao"
									headerValue="[Indefinido]" headerKey="0" value="${idTpLotacao}" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('LOTACAO')}">
							<div class="form-group">
								<label>Lotação</label>
								<siga:selecao propriedade="lotacao" tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('CARGO')}">
							<div class="form-group">
								<label>Cargo</label>
								<siga:selecao propriedade="cargo" tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('FUNCAO')}">
							<div class="form-group">
								<label>Função de Confiança</label>
								<siga:selecao propriedade="funcao" tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('ORGAO')}">
							<div class="form-group">
								<label>Órgão</label>
								<siga:select name="idOrgaoUsu" list="orgaosUsu"
									listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
									headerValue="[Indefinido]" headerKey="0" value="${idOrgaoUsu}" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('PESSOA_OBJETO')}">
							<div class="form-group">
								<label>Pessoa Objeto</label>
								<siga:selecao tipo="pessoa" propriedade="pessoaObjeto"
									tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('LOTACAO_OBJETO')}">
							<div class="form-group">
								<label>Lotação Objeto</label>
								<siga:selecao tipo="lotacao" propriedade="lotacaoObjeto"
									tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('CARGO_OBJETO')}">
							<div class="form-group">
								<label>Cargo Objeto</label>
								<siga:selecao tipo="cargo" propriedade="cargoObjeto"
									tema="simple" modulo="siga" />
							</div>
						</div>
						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('FUNCAO_OBJETO')}">
							<div class="form-group">
								<label>Função de Confiança Objeto</label>
								<siga:selecao tipo="funcao" propriedade="funcaoObjeto"
									tema="simple" modulo="siga" />
							</div>
						</div>

						<div class="col-sm-6 col-lg-4"
							style="${tipoDeConfiguracao.style('ORGAO_OBJETO')}">
							<div class="form-group">
								<label>Órgão Objeto</label>
								<siga:select name="idOrgaoObjeto" list="orgaosUsu"
									listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
									headerValue="[Indefinido]" headerKey="0"
									value="${idOrgaoObjeto}" />
							</div>
						</div>
					</div>
					<jsp:doBody />
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group mb-0">
								<input type="submit" value="Ok" class="btn btn-primary" /> <input
									type="button" value="Cancelar"
									onclick="javascript:history.back();" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			$('[name=idDefinicaoDeProcedimento]').addClass('siga-select2');
			$('[name=idOrgaoUsu]').addClass('siga-select2');
			$('[name=idOrgaoObjeto]').addClass('siga-select2');
		});
	</script>
	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>
