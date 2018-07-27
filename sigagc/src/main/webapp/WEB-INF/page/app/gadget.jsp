<%@ include file="/WEB-INF/page/include.jsp"%>

<div id="sigagc"></div>
<table class="table-hover" style="width: 100%">
	<thead>
		<tr>
			<th width="50%">Situação</th>
			<th width="25%" style="text-align: right">Atendente</th>
			<th width="25%" style="text-align: right">Lotação</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="contagem" items="${contagens}">
			<tr>
				<td><a
					href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.lotaResponsavel.id=${lotaTitular.idLotacao}&filtro.pesquisa=true">${contagem[1]}</a></td>
				<td align="right"><a
					href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.responsavel.id=${cadastrante.idPessoa}&filtro.pesquisa=true">${contagem[2]}</a></td>
				<td align="right"><a
					href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.lotaResponsavel.id=${lotaTitular.idLotacao}&filtro.pesquisa=true">${contagem[3]}</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="mt-2">
	<a class="btn btn-primary float-right btn-sm ml-2"
		href="${linkTo[AppController].editar}"
		title="Criar novo expediente ou processo administrativo">Novo
		Conhecimento</a> <a class="btn btn-primary float-right btn-sm ml-2"
		href="${linkTo[AppController].listar}"
		title="Pesquisar expedientes e processos administrativos">Pesquisar
		Conhecimentos</a>
</div>

