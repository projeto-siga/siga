<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/public/stylesheets/main.css" type="text/css" media="screen"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/public/javascripts/simplepagination/css/simplePagination.css" type="text/css"/>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery-1.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/simplepagination/js/jquery.simplePagination.js"></script>

<li id="limenuRequisicoes"><a id="menuRequisicoes" class="" href="#">Requisi&ccedil;&otilde;es</a>
	<ul>
		<li id="limenuRequisicoesIncluir"><a id="menuRequisicoesIncluir" class="" href="${linkTo[RequisicaoController].incluir}">Incluir</a></li>

		<c:choose>
     			<c:when test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo || exibirMenuAdministrarFrota || exibirMenuAprovador }">
				<c:if test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAprovador}">
					<li id="limenuRequisicoesAprovar"><a id="menuRequisicoesAprovar" class="" href="${linkTo[RequisicaoController].listarPAprovar}">Autorizar/Rejeitar</a></li>
				</c:if>
				<li id="limenuRequisicoesListar"><a id="menuRequisicoesListar" class="" href="${linkTo[RequisicaoController].listarFiltrado('AUTORIZADA','NAOATENDIDA')}">Listar</a></li>
			</c:when>
			<c:otherwise>
				<li><a id="menuRequisicoesListar" class="" href="${linkTo[RequisicaoController].listar}">Listar</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</li>
<c:if test="${exibirMenuGabinete || exibirMenuAdminGabinete}">
	<li>
		<a id="menuGabinetes" class="" href="#">Gabinetes</a>
	  	<ul>
			<li><a id="menuGabAbastecimentos" class="" href="${linkTo[AbastecimentoController].listar}">Abastecimentos</a></li>  	
	 		<li><a id="menuGabinetesListar" class="" href="${linkTo[ControleGabineteController].listar}">Controles de entrada / sa&iacute;da</a></li>
	 		<li><a id="menuAdmFornecedores" class="" href="${linkTo[FornecedorController].listar}">Fornecedores</a></li>	
		</ul>
	</li>
</c:if>
<c:if test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo  || exibirMenuAgente}">
  <li><a id="menuMissoes" class="" href="#">Miss&otilde;es</a>
  	<ul>
  	<c:choose>
	 	 <c:when test="${exibirMenuAdministrar || exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo}">
			<li><a id="menuMissoesIncluir" class="" href="${linkTo[MissaoController].incluir}">Incluir</a></li>
			<li><a id="menuMissoesListar" class="" href="${linkTo[MissaoController].listarFiltrado('PROGRAMADA')}">Listar</a></li>
	 	 </c:when>
		 <c:otherwise>
		 	<li><a id="menuMissoesListar" class="" href="${linkTo[MissaoController].listarPorCondutorLogado}">Listar</a></li>
		 </c:otherwise>
  	</c:choose>
	</ul>
</li>
</c:if>

<c:choose>
	<c:when test="${exibirMenuAdministrar}">
		<li><a id="menuAdm" href="#">Administrar</a>
			<ul>
				<li><a id="menuAdmAbastecimentos" class="" href="${linkTo[AbastecimentoController].listar}">Abastecimentos</a></li>
				<li><a id="menuAdmInfracoes" class="" href="${linkTo[AutoDeInfracaoController].listar}">Autos de Infra&ccedil;&atilde;o</a></li>
				<!-- <li><a id="menuAdmCompras" class="" href="@{Application.emdesenvolvimento}">Compras de Materiais</a></li> -->
				<li><a id="menuAdmCondutores" class="" href="${linkTo[CondutorController].listar}">Condutores</a></li>
				<li><a id="menuAdmFinalidades" class="" href="${linkTo[FinalidadeController].listar}"><fmt:message key="finalidades" /></a></li>

				<li><a id="menuAdmFornecedores" class="" href="${linkTo[FornecedorController].listar}">Fornecedores</a></li>
				<li><a id="menuAdmPenalidades" class="" href="${linkTo[PenalidadeController].listar}">Penalidades</a></li>		
				<li><a id="menuAdmPlantoes" class="" href="${linkTo[PlantoesMensaisController].listar}">Plant&otilde;es mensais</a></li>
				<!-- <li><a id="menuAdmMateriais" class="" href="@{Application.emdesenvolvimento}">Materiais em Estoque</a></li> -->
				<li><a id="menuAdmRelat" class="" href="#">Relat&oacute;rios</a>
					<ul>
				  		<li><a id="menuAdmRelatAgendaCondutores" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioController].listarAgendaTodosCondutores}');">Agenda dos Condutores</a></li>
				  		<li><a id="menuAdmRelatAgendaVeiculos" class="" href="#" onclick=  "javascript:window.open('${linkTo[RelatorioController].listarAgendaTodosVeiculos}');">Agenda dos Ve&iacute;culos</a></li>
						<li><a id="menuAdmRelatMissoesEmAndamento" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioController].listarMissoesEmAndamento}');">Miss&otilde;es em Andamento</a></li>
						<li><a id="menuAdmRelatRanking" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioRankingController].consultar}');">Ranking por Requisi&ccedil;&otilde;es</a></li>
						<li><a id="menuAdmRelatConsumoMedio" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioConsumoMedioController].consultar}');">Consumo M&eacute;dio de Combust&iacute;vel</a></li>
						<li><a id="menuAdmRelatMissoesPendentes" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioMissoesPendentesController].gerarRelatorio}');">Miss&otilde;es Pendentes por Condutor</a></li>
						<li><a id="menuAdmRelatUsoVeiculos" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioUsoVeiculosController].consultar}');">Uso de Ve&iacute;culos por Per&iacute;odo</a></li>					
					</ul>
				</li>
				<li><a id="menuAdmServicos" class="" href="${linkTo[ServicoVeiculoController].listar}">Servi&ccedil;os</a></li>
				<li><a id="menuAdmVeiculos" class="" href="${linkTo[VeiculoController].listar}">Ve&iacute;culos</a></li>
			</ul>
		</li>
	</c:when>
	<c:when test="${exibirMenuAdministrarFrota}">
		<li><a id="menuAdm" href="#">Administrar</a>
			<ul>
				<li><a id="menuAdmAbastecimentos" class="" href="${linkTo[AbastecimentoController].listar}">Abastecimentos</a></li>
				<!-- <li><a id="menuAdmCompras" class="" href="@{Application.emdesenvolvimento}">Compras de Materiais</a></li> -->
				<li><a id="menuAdmFornecedores" class="" href="${linkTo[FornecedorController].listar}">Fornecedores</a></li>
				<li><a id="menuAdmPenalidades" class="" href="${linkTo[PenalidadeController].listar}">Penalidades</a></li>			
				<!-- <li><a id="menuAdmMateriais" class="" href="@{Application.emdesenvolvimento}">Materiais em Estoque</a></li> -->
				<li><a id="menuAdmRelat" class="" href="#">Relat&oacute;rios</a>
					<ul>
				  		<li><a id="menuAdmRelatAgendaVeiculos" class="" href="#" onclick=  "javascript:window.open('${linkTo[RelatorioController].listarAgendaTodosVeiculos}');">Agenda dos Ve&iacute;culos</a></li>
					</ul>
				</li>
				<li><a id="menuAdmServicos" class="" href="${linkTo[ServicoVeiculoController].listar}">Servi&ccedil;os</a></li>
				<li><a id="menuAdmVeiculos" class="" href="${linkTo[VeiculoController].listar}">Ve&iacute;culos</a></li>
			</ul>
		</li>
	</c:when>
	<c:when test="${exibirMenuAdministrarMissao || exibirMenuAdministrarMissaoComplexo}">
		<li><a id="menuAdm" href="#">Administrar</a>
			<ul>
				<li><a id="menuAdmAbastecimentos" class="" href="${linkTo[AbastecimentoController].listar}">Abastecimentos</a></li>
				<li><a id="menuAdmInfracoes" class="" href="${linkTo[AutoDeInfracaoController].listar}">Autos de Infra&ccedil;&atilde;o</a></li>
				<li><a id="menuAdmCondutores" class="" href="${linkTo[CondutorController].listar}">Condutores</a></li>
				<li><a id="menuAdmFinalidades" class="" href="${linkTo[FinalidadeController].listar}">Finalidades</a></li>
				<li><a id="menuAdmFornecedores" class="" href="${linkTo[FornecedorController].listar}">Fornecedores</a></li>
				<li><a id="menuAdmPlantoes" class="" href="${linkTo[PlantoesMensaisController].listar}"">Plant&otilde;es mensais</a></li>
				<!-- <li><a id="menuAdmMateriais" class="" href="@{Application.emdesenvolvimento}">Materiais em Estoque</a></li> -->
				<li><a id="menuAdmRelat" class="" href="#">Relat&oacute;rios</a>
					<ul>
				  		<li><a id="menuAdmRelatAgendaCondutores" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioController].listarAgendaTodosCondutores}');">Agenda dos Condutores</a></li>
				  		<li><a id="menuAdmRelatAgendaVeiculos" class="" href="#" onclick=  "javascript:window.open('${linkTo[RelatorioController].listarAgendaTodosVeiculos}');">Agenda dos Ve&iacute;culos</a></li>
						<li><a id="menuAdmRelatMissoesEmAndamento" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioController].listarMissoesEmAndamento}');">Miss&otilde;es em Andamento</a></li>
						<li><a id="menuAdmRelatRanking" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioRankingController].consultar}');">Ranking por Requisi&ccedil;&otilde;es</a></li>
						<li><a id="menuAdmRelatConsumoMedio" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioConsumoMedioController].consultar}');">Consumo M&eacute;dio de Combust&iacute;vel</a></li>
						<li><a id="menuAdmRelatMissoesPendentes" class="" href="#" onclick="javascript:window.open('${linkTo[RelatorioMissoesPendentesController].gerarRelatorio}');">Miss&otilde;es Pendentes por Condutor</a></li>
					</ul>
				</li>
			</ul>
		</li>
	</c:when>
</c:choose>

<li><a class="" href="#">Ajuda</a>
	<ul>
		<li><a class="" href="#" onclick="javascript:window.open('${linkTo[ApplicationController].exibirManualUsuario}');">Manual do Usu&aacute;rio</a></li>
    	<c:if test="${exibirMenuGabinete || exibirMenuAdminGabinete}">
			<li><a class="" href="#" onclick="javascript:window.open('${linkTo[ApplicationController].exibirManualUsuarioDeGabinete}');">Manual do Usu&aacute;rio de Gabinete</a></li>
    	</c:if>
	</ul>
</li>

<script type="text/javascript">
	function isBlank(str) {
	    return (!str || /^\s*$/.test(str));
	}

	$(function() {

		$("[class^='gt-btn-']").not(".btnSelecao").not("a")
		.each(function() {
			var executar = $(this).attr("onClick");
			if(!isBlank(executar)) {
				$(this).attr("executar", executar);
				$(this).removeAttr("onClick");
			}
		});

		$("[class^='gt-btn-']").not(".btnSelecao").not("a")
		.click(function(event) {
			event.preventDefault();
			$("[class^='gt-btn-']").prop('disabled', true);
			$(this).val('Processando...');

			var executar = $(this).attr("executar");
			if(!isBlank(executar)) {
				eval(executar);
			} else {
				$(this.form).submit();
			}
		});

 		$(".lnkMotivoLog").click(function(event) {
		    event.preventDefault();
			$("#dialog-form").data("link", this).dialog("open");
		}); 

		// paginacao - inicio
		if (!($("#pagination").length === 0)){

			var items = $("#htmlgrid tbody tr");

	        var numItems = items.length;
	        var perPage = 10;

	        // only show the first 2 (or "first per_page") items initially
	        items.slice(perPage).hide();

	        // now setup pagination
	        $("#pagination").pagination({
	            items: numItems,
	            itemsOnPage: perPage,
	            cssStyle: "light-theme",
	            onPageClick: function(pageNumber) { // this is where the magic happens
	                // someone changed page, lets hide/show trs appropriately
	                var showFrom = perPage * (pageNumber - 1);
	                var showTo = showFrom + perPage;

	                items.hide() // first hide everything, then show for the new page
	                     .slice(showFrom, showTo).show();
	            }
	        });
		}
	        // paginacao - fim
	});
</script>
