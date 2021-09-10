<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" href="/sigatp/public/stylesheets/andamentos.css" type="text/css" media="screen" />

<script type="text/javascript">
	function trocaExibicaoCaixaBuscaAvancada() {
		$(".caixa_busca_avancado").toggle();
	}
</script>

<div class="gt-table-action-list">
	<c:if test="${menuAbastecimentosMostrarVoltar}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png" />
			<a class="filtro_Voltar" href="${linkTo[AbastecimentoController].listar}"></a>
			<a href="${linkTo[AbastecimentoController].listar}">Voltar</a>&nbsp;&nbsp;&nbsp;
		</span>
	</c:if>
	<c:if test="${menuAbastecimentosMostrarAvancado && (fn:length(condutoresEscalados) > 0 && fn:length(veiculosEscalados) > 0 && fn:length(fornecedores) > 0 && fn:length(combustivel.values) > 0)}">
		<span class="filtro">
			<img src="/sigatp/public/images/filter-icon.png" />
			<a class="filtro_Avancado" href="#"></a>
			<a href="#" onclick="javascript:trocaExibicaoCaixaBuscaAvancada();">Avan&ccedil;ado</a>&nbsp;&nbsp;&nbsp;
		</span>
		
		
	</c:if>
</div>

<br/>

<div class="caixa_busca_avancado">
	<form method="post" action="${linkTo[AbastecimentoController].listarAvancado}" enctype="multipart/form-data">
		<div class="gt-form">
			<div class="coluna margemDireitaG">
				<label for="condutorEscalado.id">Condutor </label>
				<siga:select name="condutorEscalado.id" id="lstCondutores" headerKey="0" headerValue=""
					list="condutoresEscalados" listKey="id" listValue="dadosParaExibicao" value="${condutorEscalado.id}" />
			</div>
			<div class="coluna margemDireitaG">
				<label for="fornecedor">Fornecedor</label>
				<siga:select name="fornecedor.id" id="lstFornecedores" headerKey="0" headerValue="" 
					list="fornecedores" listKey="id" listValue="razaoSocial" value="${fornecedor.id}" />
			</div>
			<div class="coluna margemDireitaG">
				<label for="veiculoEscalado.id">Ve&iacute;culo </label>
				<siga:select name="veiculoEscalado.id" id="lstVeiculos" headerKey="0" headerValue="" 
					list="veiculosEscalados" listKey="id" listValue="dadosParaExibicao" value="${veiculoEscalado.id}" />
			</div>
			<div class="coluna margemDireitaG">
				<label for="tipoDeCombustivel">Tipo de Combust&iacute;vel</label>
				<select name="tipoDeCombustivel">
					<option />
					<c:forEach items="${combustivel.tiposParaAbastecimento()}" var="item">
						<option value="<c:out value='${item.descricao}'/>" 
							<c:if test="${tipoDeCombustivel.equals(item)  && !menuAbastecimentosMostrarAvancado}">selected</c:if> > 
							<c:out value="${item.descricao}" />
						</option>
					</c:forEach>
				</select>	
			</div>
			<br/><br/>
			<div id="btnAcoes" class="gt-table-buttons" style="display:block;">
				<input id="btnFiltrar" type="submit" value="<fmt:message key='views.botoes.filtrar'/>" class="gt-btn-medium gt-btn-left" />
			</div>
		</div>
	</form>
</div>

<c:if test="${menuAbastecimentosMostrarVoltar && (fn:length(condutoresEscalados) > 0 || fn:length(veiculosEscalados) > 0 || fn:length(fornecedores) > 0 || fn:length(combustivel.values) > 0)}">
	<script type="text/javascript">
		trocaExibicaoCaixaBuscaAvancada();
	</script>
</c:if>