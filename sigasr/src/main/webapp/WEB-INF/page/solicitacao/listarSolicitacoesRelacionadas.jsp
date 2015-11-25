<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div class="gt-sidebar-content">

<h3>Solicita&ccedil;&otilde;es Relacionadas</h3>
<div class="gt-content-box gt-form" style="margin-bottom: 5px !important; padding: 10px 10px 10px 10px;">
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
	<c:if test="${not empty solicitacao.solicitante}">
		<input type="checkbox" name="filtro.solicitante.id"
			onchange="sbmt('formRelacionadas')"
			value="${solicitacao.solicitante.idPessoa}" ${not empty filtro.solicitante ? 'checked' : ''}/> <b>Solicitante:</b> ${solicitacao.solicitante.nomeAbreviado}<br/>
	</c:if>
	<c:if test="${not empty solicitacao.itemConfiguracao}">
		<input type="checkbox" name="filtro.itemConfiguracao.id"
			onchange="sbmt('formRelacionadas')"
			value="${solicitacao.itemConfiguracao.id}" ${not empty filtro.itemConfiguracao ? 'checked' : ''} /> <b>Item:</b> ${solicitacao.itemConfiguracao.tituloItemConfiguracao}<br/>
	</c:if>
	<c:if test="${not empty solicitacao.acao}">
		<input type="checkbox" name="filtro.acao.id"
			onchange="sbmt('formRelacionadas')"
			value="${solicitacao.acao.id}" ${not empty filtro.acao ? 'checked' : '' } /> <b>Ação:</b> ${solicitacao.acao.tituloAcao}<br/>
	</c:if>
	</form>
</div>
<div class="gt-content-box" id="resultados" depende="formRelacionadas">
	<table width="100%" class="gt-table">
		<colgroup>
			<col width="25%" />
			<col width="75%" />
		</colgroup>
		<tbody>
			<c:choose>
				<c:when test="${not empty solicitacoesRelacionadas}">
					<thead>
						<tr>
							<th>Aberta em</th>
							<th>Teor</th>
						</tr>
					</thead>
					<c:forEach items="${solicitacoesRelacionadas}" var="sol">
						<tr style="cursor: pointer" onclick="window.open('${linkTo[SolicitacaoController].exibir}TMPSR${sol[0]}');" target="new">
							<td>
								${sol[2]}
							</td> 
							<td>
								<siga:selecionado sigla="${sol[1]}" descricao="${sol[1]}"/>
							</td> 
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="2" style="text-align: center;">N&atilde;o h&aacute; solicita&ccedil;&otilde;es relacionadas</td></tr>
				</c:otherwise>	
			</c:choose>
			<tr><td colspan="2"><a href="" onclick="window.open('${linkTo[SolicitacaoController].buscar}?'+$('#formRelacionadas').serialize());return false;">Ver Mais</a></td></tr>		
		</tbody>
	</table>
	<script>
		$("#resultados span").each(function (){
			if (this.innerHTML.length > 150){
				this.innerHTML = this.innerHTML.substring(0, 150) + '...';
			}
		});
	</script>
</div>
</div>


