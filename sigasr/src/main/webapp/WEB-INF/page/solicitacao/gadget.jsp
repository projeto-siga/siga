<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table-hover" style="width: 100%">
	<thead>
		<tr>
			<th width="25%">Situa&ccedil;&atilde;o</th>
			<th width="25%" style="text-align: right">Atendente</th>
			<th width="25%" style="text-align: right">Lota&ccedil;&atilde;o</th>
			<th width="25%" style="text-align: right">N&atilde;o
				atribu&iacute;das</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${contagens}" var="contagem">
			<tr>
				<td><a
					href="${linkTo[SolicitacaoController].buscar}?filtro.situacao.idMarcador=${contagem[0]}&filtro.lotaAtendente.id=${lotaTitular.idLotacao}">${contagem[1]}</a>
				</td>
				<td align="right"><a
					href="${linkTo[SolicitacaoController].buscar}?filtro.situacao.idMarcador=${contagem[0]}&filtro.atendente.id=${titular.idPessoa}">${contagem[2]}</a>
				</td>
				<td align="right"><a
					href="${linkTo[SolicitacaoController].buscar}?filtro.situacao.idMarcador=${contagem[0]}&filtro.lotaAtendente.id=${lotaTitular.idLotacao}">${contagem[3]}</a>
				</td>
				<td align="right"><a
					href="${linkTo[SolicitacaoController].buscar}?filtro.situacao.idMarcador=${contagem[0]}&filtro.lotaAtendente.id=${lotaTitular.idLotacao}&filtro.naoDesignados=true">${contagem[4]}</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div class="mt-2">
	<a class="btn btn-primary float-right btn-sm ml-2"
		href="${linkTo[SolicitacaoController].editar}"
		title="Nova Solicita&ccedil;&atilde;o">Nova
		Solicita&ccedil;&atilde;o</a> <a
		class="btn btn-primary float-right btn-sm ml-2"
		href="${linkTo[SolicitacaoController].buscar}"
		title="Pesquisar Solicita&ccedil;&otilde;es">Pesquisar
		Solicita&ccedil;&otilde;es</a>
</div>
