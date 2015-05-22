<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<li><a href="#">Servi&ccedil;os</a>
	<ul>
		<li><a href="${linkTo[ServicoController].teste}">Cadastrar</a></li>
		<li><a href="@{Application.buscarSolicitacao}">Pesquisar</a></li>
	</ul>
</li>

<li><a href="#">Prioriza&ccedil;&atilde;o</a>
	<ul>
		<li><a href="@{Application.listarLista}">Lista de Prioridade</a></li>
	</ul>
</li>
	
<%-- 	<c:if test="${exibirMenuAdministrar || exibirMenuConhecimentos}"> --%>
	<c:if test="${true}">
			<li><a href="#">Administra&ccedil;&atilde;o</a>
				<ul>
<%-- 				<c:if test="${exibirMenuAdministrar}"> --%>
					<c:if test="${true}">
						<li><a href="${linkTo[ItemConfiguracaoController].listar[false]}">Item de configura&ccedil;&atilde;o</a></li>
						<li><a href="${linkTo[AcaoController].listar[false]}">A&ccedil;&atilde;o</a></li>
						<li><a href="${linkTo[TipoAcaoController].listar[false]}">Tipo de A&ccedil;&atilde;o</a></li>
						<li><a href="${linkTo[DesignacaoController].listar[false]}">Designa&ccedil;&atilde;o</a></li>
						<li><a href="${linkTo[AcordoController].listar[false]}">Acordo</a></li>
						<li><a href="${linkTo[DisponibilidadeController].listar}">Disponibilidade</a></li>
						<li><a href="${linkTo[EquipeController].listar[false]}">Equipe</a></li>
						<li><a href="${linkTo[AtributoController].listar[false]}">Atributo</a></li>
						<li><a href="${linkTo[PesquisaSatisfacaoController].listar[false]}">Pesquisa de Satisfa&ccedil;&atilde;o</a></li> 
					</c:if>
<%-- 					<c:if test="${exibirMenuConhecimentos}"> --%>
					<c:if test="${true}">
						<li><a href="${linkTo[ConhecimentoController].listar}">Conhecimento</a></li>
					</c:if>
				</ul>
			</li>
	 </c:if>
	 
	<c:if test="${exibirMenuRelatorios}">
		<li><a href="#">Relat&oacute;rio</a></li>
		<ul>
<%-- 		<c:if test="${exibirMenuAdministrar}"> --%>
			<c:if test="${true}">
				<li><a href="${linkTo[ItemConfiguracaoController].listar[false]}">Item de configura&ccedil;&atilde;o</a></li>
					<li><a href="${linkTo[AcaoController].listar[false]}">A&ccedil;&atilde;o</a></li>
					<li><a href="${linkTo[TipoAcaoController].listar[false]}">Tipo de A&ccedil;&atilde;o</a></li>
					<li><a href="@{Application.listarDesignacao}">Designa&ccedil;&atilde;o</a></li>
					<li><a href="${linkTo[AcordoController].listar[false]}">Acordo</a></li>
					<li><a href="@{Application.listarDisponibilidadeItens}">Disponibilidade</a></li>
					<li><a href="${linkTo[EquipeController].listar[false]}">Equipe</a></li>
					<li><a href="${linkTo[AtributoController].listar[false]}">Atributo</a></li>
					<li><a href="${linkTo[PesquisaSatisfacaoController].listar[false]}">Pesquisa de Satisfa&ccedil;&atilde;o</a></li> 
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
