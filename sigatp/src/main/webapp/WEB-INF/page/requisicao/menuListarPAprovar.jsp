<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<link rel="stylesheet" href="/sigatp/public/stylesheets/andamentos.css" type="text/css" media="screen"/>

<script src="/sigatp/public/javascripts/jquery/jquery-1.6.4.min.js"></script>

<script type="text/javascript">
	function trocaExibicaoCaixaBuscaAvancada() {
		$(".caixa_busca_avancado").toggle();
	}
</script>

<p class="gt-table-action-list">
	<c:if test="${menuRequisicoesMostrarVoltar}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_Voltar" id="menuRequisicoesVoltar" href="${linkTo[RequisicaoController].listarPAprovar}"></a>
			<a href="${linkTo[RequisicaoController].listarPAprovar}">Voltar</a>
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarTodas}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_T" id="menuRequisicoesAprovarMostrarTodas" href="${linkTo[RequisicaoController].listarPAprovar}">+</a>
		<a href="${linkTo[RequisicaoController].listarPAprovar}">Todas</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuRequisicoesMostrarAbertas}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_B" id="menuRequisicoesAprovarMostrarAbertas" href="${linkTo[RequisicaoController].listarPAprovarFiltrado('ABERTA')}">B</a>
		<a href="${linkTo[RequisicaoController].listarPAprovarFiltrado('ABERTA')}">A<U>b</U>ertas</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuRequisicoesMostrarAutorizadas}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_U" id="menuRequisicoesAprovarMostrarAutorizadas" href="${linkTo[RequisicaoController].listarPAprovarFiltrado('AUTORIZADA')}">U</a>
		<a href="${linkTo[RequisicaoController].listarPAprovarFiltrado('AUTORIZADA')}">A<U>u</U>torizadas</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuRequisicoesMostrarRejeitadas}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_R" id="menuRequisicoesAprovarMostrarRejeitadas" href="${linkTo[RequisicaoController].listarPAprovarFiltrado('REJEITADA')}">R</a>
		<a href="${linkTo[RequisicaoController].listarPAprovarFiltrado('REJEITADA')}"><U>R</U>ejeitadas</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuRequisicoesMostrarAvancado && (fn:length(estReq.values) > 0 && dataInicio == null && dataFim == null)}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png" />
			<a class="filtro_Avancado" href="#"></a>
			<a href="#" onclick="javascript:trocaExibicaoCaixaBuscaAvancada();">Avan&ccedil;ado</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>	
</p>

<p>
	<div class="caixa_busca_avancado">
		<form method="post" action="${linkTo[RequisicaoController].listarAvancadoPAprovar}" enctype="multipart/form-data">
			<sigatp:calendario />
			<div class="gt-form">
				<div class="coluna margemDireitaG">
					<label for="dataInicio">Data In&iacute;cio</label>
					<input type="text" name="dataInicio" class="datePicker" value="<fmt:formatDate value="${dataInicio.time}" pattern="dd/MM/yyyy" />" />
				</div>
				<div class="coluna margemDireitaG">
					<label for="dataFim">Data Fim</label>
					<input type="text" name="dataFim" class="datePicker" value="<fmt:formatDate value="${dataFim.time}" pattern="dd/MM/yyyy" />" /> 
				</div>
				<div class="coluna margemDireitaG">
					<label for="estadoRequisicao">Estado</label>
					<select name="estadoRequisicao">
						<option />
						<c:forEach items="${estadosRequisicao}" var="item">
							<option value="<c:out value='${item.descricao}'/>" 
								<c:if test="${estadoRequisicao.equals(item) && !menuRequisicoesMostrarAvancado}">selected</c:if> > 
								<c:out value="${item.descricao}" />
							</option>
						</c:forEach>
					</select>	
				</div>
				<div id="btnAcoes" class="gt-table-buttons" style="display:block;">
					<input id="btnFiltrar" type="submit" value="<fmt:message key='views.botoes.filtrar'/>" class="gt-btn-medium gt-btn-left" />
				</div>
			</div>	
		</form>
	</div>
</p>

<c:if test="${menuRequisicoesMostrarVoltar && (fn:length(estReq.values) > 0 || dataInicio != null || dataFim != null)}">
	<script type="text/javascript">
		trocaExibicaoCaixaBuscaAvancada();
	</script>
</c:if>