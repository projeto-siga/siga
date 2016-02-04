<%@ include file="/WEB-INF/page/include.jsp"%>

<div id="sigagc"></div>
<div class="gt-content-box gt-for-table">
	<table border="0" class="gt-table">
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
				<td ><a href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.lotaResponsavel.id=${lotaTitular.idLotacao}&filtro.pesquisa=true">${contagem[1]}</a></td>
				<td align="right" ><a href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.responsavel.id=${cadastrante.idPessoa}&filtro.pesquisa=true">${contagem[2]}</a></td>
				<td align="right" ><a href="${linkTo[AppController].listar}?filtro.situacao.id=${contagem[0]}&filtro.lotaResponsavel.id=${lotaTitular.idLotacao}&filtro.pesquisa=true">${contagem[3]}</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
<br />
<input type="button" value="Novo Conhecimento"
	onclick="javascript: window.location='${linkTo[AppController].editar}';"
	class="gt-btn-large gt-btn-right" style="cursor: pointer;" title="Criar Novo Conhecimento"/>
