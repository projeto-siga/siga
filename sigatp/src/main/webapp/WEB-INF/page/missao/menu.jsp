<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<link rel="stylesheet" href="/sigatp/public/stylesheets/andamentos.css" type="text/css" media="screen"/>

<script src="/sigatp/public/javascripts/jquery/jquery-1.6.4.min.js"></script>

<script type="text/javascript">
	function trocaExibicaoCaixaBuscaAvancada() {
		$(".caixa_busca_avancado").toggle();
	}
</script>

<sigatp:calendario />
<p class="gt-table-action-list">
	<c:if test="${menuMissoesMostrarVoltar}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_Voltar" href="${linkTo[MissaoController].listarFiltrado('PROGRAMADA')}"></a>
			<a href="${linkTo[MissaoController].listarFiltrado('PROGRAMADA')}">Voltar</a>&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuMissoesMostrarTodas}">
		<c:choose>
			<c:when test="${exibirMenuAdministrar  || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo}">
				<span class="filtro">				
		 	   		<img src="/sigatp/public/images/filter-icon.png"/>
		 	   		<a class="filtro_T" href="${linkTo[MissaoController].listar}"></a>
		 	   		<a href="${linkTo[MissaoController].listar}"><U>T</U>odas</a>&nbsp;&nbsp;
	 	   		</span>
			</c:when>
			<c:otherwise>
				<span class="filtro">
	 	   			<img src="/sigatp/public/images/filter-icon.png"/>
	 	   			<a class="filtro_T" href="${linkTo[MissaoController].listarPorCondutorLogado}"></a>
	 	   			<a href="${linkTo[MissaoController].listarPorCondutorLogado}"><U>T</U>odas</a>&nbsp;&nbsp;
	 	   		</span>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${menuMissoesMostrarProgramadas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_P" href="${linkTo[MissaoController].listarFiltrado('PROGRAMADA')}"></a>
			<a href="${linkTo[MissaoController].listarFiltrado('PROGRAMADA')}"><U>P</U>rogramadas</a>&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuMissoesMostrarIniciadas}">
		<span class="filtro">
	   		<img src="/sigatp/public/images/filter-icon.png"/>
	   		<a class="filtro_I" href="${linkTo[MissaoController].listarFiltrado('INICIADA')}"></a>
			<a href="${linkTo[MissaoController].listarFiltrado('INICIADA')}"><U>I</U>niciadas</a>&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuMissoesMostrarFinalizadas}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_F" href="${linkTo[MissaoController].listarFiltrado('FINALIZADA')}"></a>
			<a href="${linkTo[MissaoController].listarFiltrado('FINALIZADA')}"><U>F</U>inalizadas</a>&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuMissoesMostrarCanceladas}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_C" href="${linkTo[MissaoController].listarFiltrado('CANCELADA')}"></a>
			<a href="${linkTo[MissaoController].listarFiltrado('CANCELADA')}"><U>C</U>anceladas</a>&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuMissoesMostrarAvancado && (fn:length(condutoresEscalados) > 0 && fn:length(estMis.valores) > 0 && dataInicio == null && dataFim == null)}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png"/>
			<a class="filtro_Avancado" href="#"></a>
			<a href="#" onclick="javascript:trocaExibicaoCaixaBuscaAvancada();">Avan&ccedil;ado</a>&nbsp;&nbsp;
		</span>
	</c:if>
</p>

<p>
	<div class="caixa_busca_avancado">
		<form method="post" action="${linkTo[MissaoController].listarAvancado}" enctype="multipart/form-data">
			<div class="gt-form">
				<div class="coluna margemDireitaG">
					<label for="condutorEscalado.id">Condutor</label>
					<siga:select name="condutorEscalado.id" id="lstCondutores" headerKey="0" headerValue=""
						list="condutoresEscalados" listKey="id" listValue="dadosParaExibicao" value="${condutorEscalado.id}" />
				</div>
				<div class="coluna margemDireitaG">
					<label for="estadoMissao">Estado</label>
					<select id="lstEstadoMissao" name="estadoMissao">
						<option />
						<c:forEach items="${estMis.values()}" var="item">
							<option value="<c:out value='${item.descricao}'/>" 
								<c:if test="${estadoMissao.equals(item) && !menuMissoesMostrarAvancado}">selected</c:if> > 
								<c:out value="${item.descricao}" />
							</option>
						</c:forEach>
					</select>	
				</div>
				<div class="coluna margemDireitaG">
					<label for="dataInicio">Data In&iacute;cio</label>
					<input type="text" name="dataInicio" class="datePicker" value="<fmt:formatDate value="${dataInicio.time}" pattern="dd/MM/yyyy" />" />
				</div>
				<div class="coluna margemDireitaG">
					<label for="dataFim">Data Fim</label>
					<input type="text" name="dataFim" class="datePicker" value="<fmt:formatDate value="${dataFim.time}" pattern="dd/MM/yyyy" />" /> 
				</div>
			</div>
			<div id="btnAcoes" class="gt-table-buttons" style="display:block;">
				<input id="btnFiltrar" type="submit" value="<fmt:message key='views.botoes.filtrar' />" class="gt-btn-medium gt-btn-left" />
			</div>
		</form>
	</div>
</p>

<c:if test="${menuMissoesMostrarVoltar && (fn:length(condutoresEscalados) > 0 || fn:length(estMis.valores) > 0 || dataInicio != null || dataFim != null)}">
	<script type="text/javascript">
		trocaExibicaoCaixaBuscaAvancada();
	</script>
</c:if>