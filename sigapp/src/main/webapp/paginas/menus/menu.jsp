 <!--  <li><a href="#">Forum</a> 
 	<ul> 
 		</li>
 		<li><a href="@{foruns_listar()}">Listar foruns</a>
 		</li>
 	</ul> 
 </li>
 -->

 <!--  
 <li><a href="#">Sala</a>
	<ul>
		<li><a href="${linkTo[SalaController].incluir}">Incluir sala</a>
		</li>
		<li><a href="${linkTo[SalaController].listar}">Listar salas</a>
		</li>
	</ul>
 </li>
 -->
 <li><a href="${linkTo[AgendamentoController].incluirAjax}">Agendar</a>
	<ul>
	</ul>
 </li>
 <li><a href="${linkTo[AgendamentoController].excluir}">Cancelar<br>Editar</a>
	<ul>
		
	</ul>
 </li>
 <li><a href="#" >Relat&oacute;rios</a>
	<ul>
		<li><a href="${linkTo[AgendamentoController].hoje}">Marcadas Hoje</a>
		</li>
		<li><a href="${linkTo[AgendamentoController].amanha}">Marcadas Amanh&atilde;</a>
		</li>
		<li><a href="${linkTo[AgendamentoController].agendadas}">Agendadas</a>
		</li>
		<li><a href="${linkTo[AgendamentoController].imprime}">Imprime agendamentos</a>
		</li>
		<li><a href="${linkTo[AgendamentoController].salaLista}">Imprime os agendamentos de uma sala</a>
		</li>
	</ul>
 </li>
 <li><a href="${linkTo[PeritoController].incluir}">Inclui um perito</a>
 </li>
 <li><a href="${linkTo[UsuarioFormController].atualiza}">Configura&ccedil;&atilde;o</a>
 </li>
 <li><a href="${linkTo[PrincipalController].creditos}">Cr&eacute;ditos</a>
	<ul></ul>
 </li>
