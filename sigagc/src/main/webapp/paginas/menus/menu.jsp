<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" data-toggle="dropdown" href="${linkTo[AppController].estatisticaGeral}">Conhecimentos</a>
	<ul class="dropdown-menu">
		<li><a class="dropdown-item" href="${linkTo[AppController].novo}">Novo</a>
		</li>
		<li><a class="dropdown-item" href="${linkTo[AppController].buscar}">Buscar por texto</a>
		</li>
		<li><a class="dropdown-item" href="${linkTo[AppController].navegar}">Navegar</a>
		</li>
		<li><a class="dropdown-item" href="${linkTo[AppController].listar}">Pesquisar</a>
		</li>
		<li class="dropdown-submenu"><a  class="dropdown-item dropdown-toggle" href="#">Estatísticas</a>
			<ul class="dropdown-menu">
				<li><a class="dropdown-item" href="${linkTo[AppController].estatisticaGeral}">Gerais</a></li>
				<li><a class="dropdown-item" href="${linkTo[AppController].estatisticaLotacao}">Por Lotação</a></li>
			</ul>
		</li>
	</ul></li>