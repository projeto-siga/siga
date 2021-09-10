<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<li class="nav-item dropdown">
	<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Servi&ccedil;os</a>
	<ul class="dropdown-menu">
		<li>
			<a href="${linkTo[SolicitacaoController].editar}" class="dropdown-item">Cadastrar</a>
		</li>
		<li>
			<a href="${linkTo[SolicitacaoController].buscar}" class="dropdown-item" >Pesquisar</a>
		</li>
	</ul>
</li>

<li class="nav-item dropdown">
	<a href="${linkTo[SolicitacaoController].listarLista(false)}" class="nav-link dropdown-toggle" data-toggle="dropdown">Prioriza&ccedil;&atilde;o</a>
	<ul class="dropdown-menu">
		<li>
			<a href="${linkTo[SolicitacaoController].listarLista(false)}" class="dropdown-item">Lista
				de Prioridade</a>
		</li>
	</ul>
</li>

<c:if test="${exibirMenuAdministrar || exibirMenuConhecimentos}">
	<li class="nav-item dropdown">
		<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Administra&ccedil;&atilde;o</a>
		<ul class="dropdown-menu">
			<c:if test="${exibirMenuAdministrar}">
				<li><a href="${linkTo[ItemConfiguracaoController].listar}" class="dropdown-item">Item de configura&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[AcaoController].listar}" class="dropdown-item">A&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[TipoAcaoController].listar}" class="dropdown-item">Tipo de A&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[DesignacaoController].listar}" class="dropdown-item">Designa&ccedil;&atilde;o</a></li>
				<li><a href="${linkTo[AcordoController].listar}" class="dropdown-item">Acordo</a></li>
				<li><a href="${linkTo[DisponibilidadeController].listar}" class="dropdown-item">Disponibilidade</a></li>
				<li><a href="${linkTo[EquipeController].listar(false)}" class="dropdown-item">Equipe</a></li>
				<li><a href="${linkTo[AtributoController].listar(false)}" class="dropdown-item">Atributo</a></li>
				<li><a href="${linkTo[PesquisaSatisfacaoController].listar}" class="dropdown-item">Pesquisa de Satisfa&ccedil;&atilde;o</a></li>
			</c:if>
			<c:if test="${exibirMenuConhecimentos}">
				<li><a href="${linkTo[ConhecimentoController].listar}" class="dropdown-item">Conhecimento</a></li>
			</c:if>
		</ul>
	</li>
</c:if>

<c:if test="${exibirMenuRelatorios}">
	<li class="nav-item dropdown">
		<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Relat&oacute;rio</a>
		<ul class="dropdown-menu">
			<li><a href="${linkTo[RelatorioController].exibirRelAtendimentos}" class="dropdown-item">Atendimentos</a></li>
		</ul>
	</li>
</c:if>
