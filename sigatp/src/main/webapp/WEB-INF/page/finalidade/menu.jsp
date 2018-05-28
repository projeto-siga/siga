<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="../../public/stylesheets/andamentos.css" type="text/css" media="screen"/>

<p class="gt-table-action-list">
	<c:if test="${menuFinalidadesMostrarVoltar}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_Voltar" href="${linkTo[FinalidadeController].listar}"></a>
		<a href="${linkTo[FinalidadeController].listar}">Voltar</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuFinalidadesMostrarTodas}">
		<img src="/sigatp/public/images/filter-icon.png"/>
		<a class="filtro_T" href="${linkTo[FinalidadeController].listarTodas}"></a>
		<a href="${linkTo[FinalidadeController].listarTodas}"><U>T</U>odas</a>&nbsp;&nbsp;&nbsp;
	</c:if>
</p>