
<%@ include file="/WEB-INF/page/include.jsp"%>

	<li class="nav-item dropdown">
		<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Procedimentos</a>
		<ul class="dropdown-menu">
			<li>
				<a class="dropdown-item" href="${linkTo[AppController].resumo}" >Ativos</a>
			</li>

			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;OPERAR:Executar comandos da tela inicial')}">
				<li class="dropdown-submenu">
					<a href="javascript:void(0);" class="dropdown-item dropdown-toggle">Iniciar</a>
					<ul class="dropdown-menu">
						<c:forEach var="pd" items="${processDefinitions}">
							<li>
								<a class="dropdown-item" href="${linkTo[AppController].initializeProcess[pd.id]}" >${pd.name}</a>
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:if>
		</ul> 
		<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;FE:Ferramentas')}">
			<li class="nav-item dropdown">
				<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Ferramentas</a>
				<ul class="dropdown-menu navmenu-large">
					<c:if	test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;CONFIGURAR:Configurar iniciadores')}">
						<li class="dropdown-submenu">
							<a href="#" class="dropdown-item dropdown-toggle">Configurar</a>
							<ul class="dropdown-menu">
								<c:forEach var="pd" items="${processDefinitions}">
									<li>
										<a class="dropdown-item" href="${linkTo[ConfiguracaoController].pesquisar[lotaTitular.orgaoUsuario.acronimoOrgaoUsu][pd.name]}">${pd.name}</a>
									</li>
								</c:forEach>
							</ul>
						</li>
					</c:if>

					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;DESIGNAR:Designar tarefas')}">
						<li class="dropdown-submenu">
							<a href="#" class="dropdown-item dropdown-toggle">Designar Tarefas</a>
							<ul class="dropdown-menu">
								<c:forEach var="pd" items="${processDefinitions}">
									<li>
										<a class="dropdown-item" href="${linkTo[DesignacaoController].pesquisar[lotaTitular.orgaoUsuario.acronimoOrgaoUsu][pd.name]}" >${pd.name}</a>
									</li>
								</c:forEach>
							</ul></li>
					</c:if>
				
					<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;EDITAR:Editar procedimento')}">
						<li class="dropdown-submenu">
							<a href="#" class="dropdown-item dropdown-toggle">Editar Procedimento</a>
							<ul class="dropdown-menu navmenu-large">
								<c:forEach var="pd" items="${processDefinitions}">
									<li>
										<a class="dropdown-item" href="${linkTo[EdicaoController].form[pd.name]}">${pd.name}</a>
									</li>
								</c:forEach>
							</ul>
						</li>
					</c:if>
				</ul>
			</li>
		</c:if>
	</li>
	<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;MEDIR: Analisar métricas')}">
		<li class="nav-item dropdown">
			<a href="javascript:void(0);" class="nav-link dropdown-toggle" data-toggle="dropdown">Relatórios </a>
			<ul class="dropdown-menu navmenu-large">
				<li class="dropdown-submenu">
					<a href="#" class="nav-link dropdown-toggle">Apresentar Métricas</a>
					<ul class="dropdown-menu">
						<c:forEach var="pd" items="${processDefinitions}">
							<li>
								<a class="dropdown-item" href="${linkTo[MetricaController].medir}?orgao=${lotaTitular.orgaoUsuario.id}&procedimento=${pd.name}&pdId=${pd.id}">${pd.name}</a>
							</li>
						</c:forEach>
					</ul>
				</li>
			</ul>
		</li>
	</c:if>