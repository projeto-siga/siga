
<%@ include file="/WEB-INF/page/include.jsp"%>

<li class="nav-item dropdown"><a href="javascript:void(0);"
	class="nav-link dropdown-toggle" data-toggle="dropdown">Procedimentos</a>
	<ul class="dropdown-menu">
		<li><a class="dropdown-item"
			href="${linkTo[WfAppController].iniciar}">Iniciar</a></li>
		<li><a class="dropdown-item"
			href="${linkTo[AppController].resumo}">Ativos</a></li>
	</ul> <c:if
		test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;FE:Ferramentas')}">
		<li class="nav-item dropdown"><a href="javascript:void(0);"
			class="nav-link dropdown-toggle" data-toggle="dropdown">Ferramentas</a>
			<ul class="dropdown-menu navmenu-large">
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;DIAG;Cadastrar Diagramas')}">
					<li><a class="dropdown-item"
						href="${linkTo[WfDiagramaController].lista()}">Cadastro de
							Diagramas</a></li>
				</c:if>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;RESP;Cadastrar Responsáveis')}">
					<li><a class="dropdown-item"
						href="${linkTo[WfResponsavelController].lista()}">Cadastro de
							Responsáveis</a></li>
				</c:if>
			</ul></li>
	</c:if></li>
<c:if
	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;MEDIR: Analisar métricas')}">
	<li class="nav-item dropdown"><a href="javascript:void(0);"
		class="nav-link dropdown-toggle" data-toggle="dropdown">Relatórios
	</a>
		<ul class="dropdown-menu navmenu-large">
			<li class="dropdown-submenu"><a href="#"
				class="dropdown-item dropdown-toggle">Apresentar Métricas</a>
				<ul class="dropdown-menu">
					<c:forEach var="pd" items="${processDefinitions}">
						<li><a class="dropdown-item"
							href="${linkTo[MetricaController].medir}?orgao=${lotaTitular.orgaoUsuario.id}&procedimento=${pd.name}&pdId=${pd.id}">${pd.name}</a>
						</li>
					</c:forEach>
				</ul></li>
		</ul></li>
</c:if>