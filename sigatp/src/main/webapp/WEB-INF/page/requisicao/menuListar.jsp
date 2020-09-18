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
			<a class="filtro_Voltar" id="menuRequisicoesVoltar" href="${linkTo[RequisicaoController].listarFiltrado('PROGRAMADA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('PROGRAMADA')}">Voltar</a>
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarTodas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_T" id="menuRequisicoesMostrarTodas" href="${linkTo[RequisicaoController].listar}"></a>
	   		<a href="${linkTo[RequisicaoController].listar}"><U>T</U>odas</a>
   		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarAutorizadasENaoAtendidas}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_na_A" id="menuRequisicoesMostrarAutorizadasENaoAtendidas" href="${linkTo[RequisicaoController].listarFiltrado('AUTORIZADA','NAOATENDIDA')}">AN</a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('AUTORIZADA','NAOATENDIDA')}">A<U>u</U>torizadas/<U>N</U>&atilde;o Atendidas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarAbertas}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_B" id="menuRequisicoesMostrarAbertas" href="${linkTo[RequisicaoController].listarFiltrado('ABERTA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('ABERTA')}">A<U>b</U>ertas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarAutorizadas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_U" id="menuRequisicoesMostrarAutorizadas" href="${linkTo[RequisicaoController].listarFiltrado('AUTORIZADA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('AUTORIZADA')}">A<U>u</U>torizadas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarRejeitadas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_R" id="menuRequisicoesMostrarRejeitadas" href="${linkTo[RequisicaoController].listarFiltrado('REJEITADA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('REJEITADA')}"><U>R</U>ejeitadas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarProgramadas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_P" id="menuRequisicoesMostrarProgramadas" href="${linkTo[RequisicaoController].listarFiltrado('PROGRAMADA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('PROGRAMADA')}"><U>P</U>rogramadas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarEmAtendimento}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_E" id=menuRequisicoesMostrarEmAtendimento href="${linkTo[RequisicaoController].listarFiltrado('EMATENDIMENTO')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('EMATENDIMENTO')}"><U>E</U>m Atendimento</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarAtendidas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_A" id="menuRequisicoesMostrarAtendidas" href="${linkTo[RequisicaoController].listarFiltrado('ATENDIDA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('ATENDIDA')}"><U>A</U>tendidas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarNaoAtendidas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_N" id=menuRequisicoesMostrarNaoAtendidas href="${linkTo[RequisicaoController].listarFiltrado('NAOATENDIDA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('NAOATENDIDA')}"><U>N</U>&atilde;o Atendidas</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuRequisicoesMostrarCanceladas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_C" id="menuRequisicoesMostrarCanceladas" href="${linkTo[RequisicaoController].listarFiltrado('CANCELADA')}"></a>
			<a href="${linkTo[RequisicaoController].listarFiltrado('CANCELADA')}"><U>C</U>anceladas</a>&nbsp;&nbsp;&nbsp;
		</span>
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
		<form method="post" action="${linkTo[RequisicaoController].listarAvancado}" enctype="multipart/form-data">
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
						<c:forEach items="${estReq.values}" var="item">
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