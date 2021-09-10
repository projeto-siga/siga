<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<p class="gt-table-action-list">
	<c:if test="${menuRequisicoesIncluir}">
  		<a class="once" href="${linkTo[RequisicaoController].incluir}">
  			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
  			Incluir Requisi&ccedil;&atilde;o
  		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesEditar}">
		<a class="once" href="${linkTo[RequisicaoController].editar(idRequisicao)}">
			<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
			Editar Requisi&ccedil;&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesAutorizar}">
		<a class="once" href="${linkTo[AndamentoController].autorizar(idRequisicao)}">
			<img src="/sigatp/public/images/approvedicon.png" style="margin-right: 5px;">
			Autorizar
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesRejeitar}">
		<a class="once" href="${linkTo[AndamentoController].rejeitar(idRequisicao)}">
			<img src="/sigatp/public/images/rejectedicon.png" style="margin-right: 5px;">
			Rejeitar
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesCancelar}">
		<a class="once" href="${linkTo[AndamentoController].cancelar(idRequisicao)}">
			<img src="/sigatp/public/images/canceledmicon.png" style="margin-right: 5px;">
				Cancelar
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesListarAndamentos}">
		<a class="once" href="${linkTo[AndamentoController].listarPorRequisicao(idRequisicao,popUp)}">
			<img src="/sigatp/public/images/listandamentosicon.png" style="margin-right: 5px;">
			Andamentos
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>

	<c:if test="${menuRequisicoesMostrarRequisicao}">
		<a class="once" href="${linkTo[RequisicaoController].buscarPelaSequence(true,requisicaoTransporte.buscarSequence())}">
			<img src="/sigatp/public/images/requisicaoicon.png" style="margin-right: 5px;">
			Requisi&ccedil;&atilde;o
		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
</p>