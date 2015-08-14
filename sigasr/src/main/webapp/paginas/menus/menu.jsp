<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<li><a href="#">Servi&ccedil;os</a>
	<ul>
		<li><a href="${linkTo[SolicitacaoController].editar}">Cadastrar</a></li>
		<li><a href="${linkTo[SolicitacaoController].buscar}">Pesquisar</a></li>
	</ul></li>

<li><a href="${linkTo[SolicitacaoController].listarLista[false]}">Prioriza&ccedil;&atilde;o</a>
	<ul>
		<li><a href="${linkTo[SolicitacaoController].listarLista[false]}">Lista
				de Prioridade</a></li>
	</ul></li>

<c:if test="${exibirMenuAdministrar || exibirMenuConhecimentos}">
	<li><a href="#">Administra&ccedil;&atilde;o</a>
		<ul>
			<c:if test="${exibirMenuAdministrar}">
				<li><a href="${linkTo[ItemConfiguracaoController].listar}">Item de configura&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[AcaoController].listar}">A&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[TipoAcaoController].listar}">Tipo de A&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[DesignacaoController].listar}">Designa&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[AcordoController].listar}">Acordo</a></li>
				<li><a href="${linkTo[AtributoController].listar[false]}">Atributo</a></li>
				<c:if test="${exibirMenuConhecimentos}">
					<li><a href="${linkTo[ConhecimentoController].listar}">Conhecimento</a></li>
				</c:if>
				<%--<li><a href="${linkTo[DisponibilidadeController].listar}">Disponibilidade</a></li>
				<li><a href="${linkTo[EquipeController].listar[false]}">Equipe</a></li>
				<li><a href="${linkTo[PesquisaSatisfacaoController].listar}">Pesquisa de Satisfa&ccedil;&atilde;o</a></li> --%>
			</c:if>
		</ul></li>
</c:if>

<c:if test="${exibirMenuRelatorios}">
	<li><a href="#">Relat&oacute;rio</a></li>
	<ul>
		<li><a href="@{Application.estatistica}">Estat&iacute;sticas</a></li>
	</ul>
</c:if>
