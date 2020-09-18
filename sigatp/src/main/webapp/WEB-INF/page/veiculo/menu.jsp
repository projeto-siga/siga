<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<p class="gt-table-action-list">
	<c:if test="${menuVeiculosIncluir}">
		<a class="once" href="${linkTo[VeiculoController].incluir}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			<fmt:message key="dados.cadastrais" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuVeiculosEditar}"> 
		<a class="once" href="${linkTo[VeiculoController].editar(idVeiculo)}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			<fmt:message key="dados.cadastrais" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAvarias}"> 
		<a class="once" href="${linkTo[AvariaController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/avariasicon.png" style="margin-right: 5px;">
			<fmt:message key="avarias" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuRelatoriosdiarios}">
		<a class="once" href="${linkTo[RelatorioDiarioController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/relatoriosicon.png" style="margin-right: 5px;">
			<fmt:message key="relatorios.diarios" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAgenda}">
		<a class="once" href="${linkTo[AgendaController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/agendaicon.png" style="margin-right: 5px;">
			<fmt:message key="agenda" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAbastecimentos}">
		<a class="once" href="${linkTo[AbastecimentoController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/abastecimentoicon.png" style="margin-right: 5px;">
			<fmt:message key="abastecimentos" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuAutosdeinfracoes}">
		<a class="once" href="${linkTo[AutoDeInfracaoController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/infracoesicon.png" style="margin-right: 5px;">
			<fmt:message key="infracoes" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	
	<c:if test="${menuLotacoes}">
		<a class="once" href="${linkTo[LotacaoVeiculoController].listarPorVeiculo(idVeiculo)}">
			<img src="/sigatp/public/images/lotacoesicon.png" style="margin-right: 5px;">
			<fmt:message key="lotacoes" />
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>	
</p>