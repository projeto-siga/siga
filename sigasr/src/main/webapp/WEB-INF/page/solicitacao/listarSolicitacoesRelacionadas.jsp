<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div class="card mb-1">
	<h6 class="card-header">Solicita&ccedil;&otilde;es Relacionadas</h6>

	<div class="card-body">

		<div class="gt-content-box gt-form">
			<form id="formRelacionadas">
			
			<%-- Edson: o hidden abaixo impede que o filtro chegue nulo ao servidor no postback se nenhuma checkbox estiver marcada.
			O servidor verifica se o filtro está chegando nulo (por ser o primeiro request) e força o preenchimento
			completo do filtro (marca todas as checks) conforme os valores da solicitação. Quando o filtro *não* chega lá nulo, é
			porque é um postback. Nesse caso, se há algum campo não preenchido no filtro, o servidor entende que é porque o 
			usuário desmarcou a check. Então, caso este JSP esteja sendo included por uma página que alimente novos campos na solicitação,
			a página terá a responsabilidade de verificar se já havia uma checkbox para filtrar por esse campo e, se não, forçá-la
			como default true. O servidor não poderá fazer isso (verificar se uma check não existia e marcá-la default como true) porque, 
			como dito, se um campo do filtro chega lá vazio, é entendido que o usuário desmarcou de propósito, não que a check não existia --%>
			<input type="hidden" name="filtro.pesquisar" value="false" />
			<label>Buscar por</label>
			<c:if test="${not empty solicitacao.solicitante.idPessoa}">
				<div>				
					<input type="checkbox" name="filtro.solicitante.id" 
						onchange="sbmt('formRelacionadas', '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas}?'+$('#formRelacionadas').serialize(), true)"
						value="${solicitacao.solicitante.idPessoa}" ${not empty filtro.solicitante ? 'checked' : ''}/> <b>Solicitante:</b> ${solicitacao.solicitante.nomeAbreviado}
				</div>
			</c:if>
			<c:if test="${not empty solicitacao.itemConfiguracao.id}">
				<div>				
					<input type="checkbox" name="filtro.itemConfiguracao.id"
						onchange="sbmt('formRelacionadas', '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas}?'+$('#formRelacionadas').serialize(), true)"
						value="${solicitacao.itemConfiguracao.id}" ${not empty filtro.itemConfiguracao ? 'checked' : ''} /> <b>Item:</b> ${solicitacao.itemConfiguracao.tituloItemConfiguracao}
				</div>
			</c:if>
			<c:if test="${not empty solicitacao.acao.id}">
				<div>
					<input type="checkbox" name="filtro.acao.id"
						onchange="sbmt('formRelacionadas', '${linkTo[SolicitacaoController].listarSolicitacoesRelacionadas}?'+$('#formRelacionadas').serialize(), true)"
						value="${solicitacao.acao.id}" ${not empty filtro.acao ? 'checked' : '' } /> <b>Ação:</b> ${solicitacao.acao.tituloAcao}
				</div>
			</c:if>
			</form>
		</div>
		<div id="resultados" depende="formRelacionadas" class="table-responsive" >
			<table  class="table table-sm">				
				<c:choose>
					<c:when test="${not empty solicitacoesRelacionadas}">
						<thead>
							<tr>
								<th scope="col">Aberta em</th>
								<th scope="col">Teor</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${solicitacoesRelacionadas}" var="sol">
								<tr style="cursor: pointer" onclick="window.open('${linkTo[SolicitacaoController].exibir}TMPSR${sol.id}');" target="new">
									<td>
										${sol.dtRegDDMMYYYY}
									</td> 
									<td>
										<siga:selecionado sigla="${sol.descricao}" descricao="${sol.descricao}"/>
									</td> 
								</tr>
							</c:forEach>
						</tbody>
					</c:when>
					<c:otherwise>
						<tbody>
							<tr><td colspan="2" style="text-align: center;">N&atilde;o h&aacute; solicita&ccedil;&otilde;es relacionadas</td></tr>
						</tbody>							
					</c:otherwise>	
				</c:choose>
				<tr><td colspan="2"><a href="" onclick="window.open('${linkTo[SolicitacaoController].buscar}?'+$('#formRelacionadas').serialize());return false;">Ver Mais</a></td></tr>				
			</table>
			<script type="text/javascript">
				$("#resultados span").each(function (){
					if (this.innerHTML.length > 150){
						this.innerHTML = this.innerHTML.substring(0, 150) + '...';
					}
				});
			</script>
		</div>
	</div>
</div>


