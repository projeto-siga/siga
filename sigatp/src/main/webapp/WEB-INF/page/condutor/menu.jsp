<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<p class="gt-table-action-list">

	<c:if test="${menuCondutoresIncluir}">
		<a class="once" href="${linkTo[CondutorController].incluir}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			<fmt:message key="dados.cadastrais" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuCondutoresEditar}">
		<a class="once" href="${linkTo[CondutorController].editar(idCondutor)}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			<fmt:message key="dados.cadastrais" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuPlantoes}">
		<a class="once" href="${linkTo[PlantaoController].listarPorCondutor(idCondutor)}">
			<img src="/sigatp/public/images/plantaoicon.png" style="margin-right: 5px;">
			<fmt:message key="plantao" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAfastamentos}">
		<a class="once" href="${linkTo[AfastamentoController].listarPorCondutor(idCondutor)}">
			<img src="/sigatp/public/images/afastamentoicon.png" style="margin-right: 5px;">
			<fmt:message key="afastamentos" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuEscalasDeTrabalho}">
		<a class="once" href="${linkTo[EscalaDeTrabalhoController].listarPorCondutor(idCondutor)}">
		<img src="/sigatp/public/images/escalaicon.png" style="margin-right: 5px;">
		<fmt:message key="escala.trabalho" />
	</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAgenda}">
		<a class="once" href="${linkTo[AgendaController].listarPorCondutor(idCondutor)}">
			<img src="/sigatp/public/images/agendaicon.png" style="margin-right: 5px;">
			<fmt:message key="agenda" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuInfracoes}">
		<a class="once" href="${linkTo[AutoDeInfracaoController].listarPorCondutor(idCondutor)}">
			<img src="/sigatp/public/images/infracoesicon.png" style="margin-right: 5px;">
			<fmt:message key="infracoes" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
</p>