<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<style type="text/css">

  .filtro {	
		background: url(atuais/filter-icon.png)	no-repeat;
	}
	.status_N, .status_P, .status_A, .status_E,
	.filtro_N, .filtro_P, .filtro_A, .filtro_E,
	.filtro_U, .filtro_B, .filtro_R, .filtro_C,
    .filtro_I, .filtro_F,	.filtro_na_A, .filtro_T,
    .filtro_Z {
		/* No html, usar a letra correspondente ao nome do filtro/status, salvo indicação em contrário
		   N = não atendida, P = programada, A = atendida, E = em atendimento,
	     U = autorizada, B = aberta, R = rejeitada, C = cancelada,
 		   I = iniciada, F = finalizada,	na_A = autorizada (quando no filtro Não Autorizada/Autorizada, usar caracter 'A'),
		   T = todas (usar caracter '+') */
		border: 2px solid;
		border-radius: 1em;
		text-transform: uppercase;
		padding: 0 .25em;
		font:normal 70% verdana, arial, sans-serif;
		font-weight: 900;
		position:relative;
	}
	.status_N, .status_P, .status_A, .status_E {
		margin-left: .2em;
		top: -.2em;
	}	
	.filtro_N, .filtro_P, .filtro_A, .filtro_E, .filtro_U, .filtro_B, 
	.filtro_R, .filtro_C, .filtro_I, .filtro_F, .filtro_T, .filtro_Z	{
		top: .4em;
		left: -.7em;
	}	
	.filtro_B, {
		border-color: #00F;
		color: #00F;
	}
	.status_N, .filtro_N, .filtro_R, .filtro_C {
		border-color: #FF2000;
		color: #FF2000;
	}
	.status_P, .filtro_P {
		border-color: #33EE00;
		color: #33EE00;
	}
	.status_A, .filtro_A, .filtro_U, .filtro_na_A, .filtro_F, .filtro_T, .filtro_Z {
		border-color: #33EE00;
		color: #33EE00;
	}
	.filtro_na_A {
		top: .4em;
		margin-left: -1.5em;
	}	
	.status_E, .filtro_E, .filtro_I {
		border-color: #FC0;
		color: #FC0;
	}
 .filtro_I {
	 padding: 0 .38em;
 }
</style>
	   	<p class="gt-table-action-list">
	   		<c:if test="${menuServicosVeiculoMostrarTodos}">
	   			<img src="/sigatp/public/images/filter-icon.png"/>
	   			<a class="filtro_T" href="${linkTo[ServicoVeiculoController].listar}">+</a>
				<a href="${linkTo[ServicoVeiculoController].listar}"><U>T</U>odos</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${menuServicosVeiculoMostrarAgendados}">
     			<img src="/sigatp/public/images/filter-icon.png"/>
     			<a class="filtro_A" href="${linkTo[ServicoVeiculoController].listarFiltrado('AGENDADO')}">A</a>
				<a href="${linkTo[ServicoVeiculoController].listarFiltrado('AGENDADO')}"><U>A</U>gendados</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${menuServicosVeiculoMostrarIniciados}">
	   		  	<img src="/sigatp/public/images/filter-icon.png"/>
	   		  	<a class="filtro_I" href="${linkTo[ServicoVeiculoController].listarFiltrado('INICIADO')}">I</a>
				<a href="${linkTo[ServicoVeiculoController].listarFiltrado('INICIADO')}"><U>I</U>niciados</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${menuServicosVeiculoMostrarRealizados}">
	   		  	<img src="/sigatp/public/images/filter-icon.png"/>
	   		  	<a class="filtro_Z" href="${linkTo[ServicoVeiculoController].listarFiltrado('REALIZADO')}">Z</a>
				<a href="${linkTo[ServicoVeiculoController].listarFiltrado('REALIZADO')}">Reali<U>z</U>ados</a>&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${menuServicosVeiculoMostrarCancelados}">
	   		  	<img src="/sigatp/public/images/filter-icon.png"/>
	   		  	<a class="filtro_C" href="${linkTo[ServicoVeiculoController].listarFiltrado('CANCELADO')}">C</a>
				<a href="${linkTo[ServicoVeiculoController].listarFiltrado('CANCELADO')}"><U>C</U>ancelados</a>&nbsp;&nbsp;&nbsp;
			</c:if>
		</p>