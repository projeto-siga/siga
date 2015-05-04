<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<li><a href="#">Servi&ccedil;o</a>
	<ul>
		<li><a href="@{Application.editar}">Cadastrar</a></li>
		<li><a href="@{Application.buscarSolicitacao}">Pesquisar</a></li>
		<li><a href="/sigasr/app/equipe/listar">Equipe</a></li>
	</ul>
	
	<li><a href="@{Application.listarLista}">Prioriza&ccedil;&atilde;o</a>
		<ul>
			<li><a href="@{Application.listarLista}">Lista de Prioridade</a></li>
		</ul>
	
	<c:if test="${exibirMenuAdministrar || exibirMenuConhecimentos}">
			<li><a href="#">Administra&ccedil;&atilde;o</a>
				<ul>
					<c:if test="${exibirMenuAdministrar}">
						<li><a href="@{Application.listarItem}">Item de configura&ccedil;&atilde;o</a></li>
						<li><a href="${linkTo[AcaoController].listar[false]}">A&ccedil;&atilde;o</a></li>
					<!--<li><a href="@{Application.listarAcao}">A&ccedil;&atilde;o</a></li> -->
						<li><a href="@{Application.listarTipoAcao}">Tipo de A&ccedil;&atilde;o</a></li>
						<li><a href="@{Application.listarDesignacao}">Designa&ccedil;&atilde;o</a></li>
						<li><a href="@{Application.buscarAcordo}">Acordo</a></li>
						<li><a href="@{Application.listarDisponibilidadeItens}">Disponibilidade</a></li>
						<li><a href="@{Application.listarEquipe}">Equipe</a></li>
						<li><a href="@{Application.listarAtributo}">Atributo</a></li>
						<li><a href="@{Application.listarPesquisa}">Pesquisa de Satisfa&ccedil;&atilde;o</a></li> 
					</c:if>
					<c:if test="${exibirMenuConhecimentos}">
						<li><a href="@{Application.listarConhecimento}">Conhecimento</a></li>
					</c:if>
				</ul>
			</li>
	 </c:if>
	 
	<c:if test="${exibirMenuRelatorios}">
		<li><a href="#">Relat&oacute;rio</a></li>
		<ul>
			<li><a href="@{Application.estatistica}">Estat&iacute;sticas</a></li>
		</ul>
	</c:if>
</li>