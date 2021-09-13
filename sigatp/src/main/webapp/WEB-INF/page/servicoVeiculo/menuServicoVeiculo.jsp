<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	   	
<p class="gt-table-action-list">
	<c:if test="${menuServicoVeiculoEditar}">
		<a class="once" href="${linkTo[ServicoVeiculoController].editar(servico.id)}">
	   		<img src="/sigatp/public/images/editaricon.png" style="margin-right: 5px;">
	   			Editar Servi&ccedil;o de Ve&iacute;culo
	   	</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${menuServicoVeiculoExcluir}">
   		<a class="once" href="${linkTo[ServicoVeiculoController].excluir(servico.id)}">
	   		<img src="/sigatp/public/images/canceledicon.png" style="margin-right: 5px;">
	   		Excluir
   		</a>&nbsp;&nbsp;&nbsp;
	</c:if>
	<a class="once" href="${linkTo[ServicoVeiculoController].listarFiltrado(servico.situacaoServico)}">
   		<img src="/sigatp/public/images/linkvoltaricon.png" style="margin-right: 5px;">Voltar para a lista
   	</a>
</p>