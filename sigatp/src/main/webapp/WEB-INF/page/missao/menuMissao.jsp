<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<p class="gt-table-action-list">
	<c:if test="${menuMissaoEditar}">
		<a class="once" href="${linkTo[MissaoController].editar(missao.id)}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			Editar Miss&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuMissaoCancelar}">
		<a class="once" href="${linkTo[MissaoController].cancelar(missao.id)}">
			<img src="/sigatp/public/images/canceledmicon.png" style="margin-right: 5px;">
			Cancelar Miss&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuMissaoIniciar}">
		<a class="once" href="${linkTo[MissaoController].iniciar(missao.id)}">
			<img src="/sigatp/public/images/iniciaricon.png" style="margin-right: 5px;">
			Iniciar Miss&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuMissaoFinalizar}">
		<a class="once" href="${linkTo[MissaoController].finalizar(missao.id)}">
			<img src="/sigatp/public/images/finalizaricon.png" style="margin-right: 5px;">
			Finalizar Miss&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<a class="once" href="${linkTo[MissaoController].listarFiltrado(missao.estadoMissao)}">
		<img src="/sigatp/public/images/linkvoltaricon.png" style="margin-right: 5px;">
   		Voltar para a lista
	</a>
</p>