<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<h5>${tipoDeConfiguracao.descr}
	<span style="float: right; font-size: 70%">(Situação default: </b>
		${tpConfiguracao.situacaoDefault.descr})
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
			
		<jsp:doBody />

		<th align="center" style="${tipoDeConfiguracao.style('SITUACAO')}">Situação</th>
		<th align="center" style="min-width: 70px;"></th>
	</tr>

