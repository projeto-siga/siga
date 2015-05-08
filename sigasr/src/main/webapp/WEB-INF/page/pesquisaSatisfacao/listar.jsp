<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Serviços">

	<jsp:include page="../main.jsp"></jsp:include>

	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script
		src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
	<script src="../../../javascripts/jquery.serializejson.min.js"></script>
	<script src="../../../javascripts/jquery.populate.js"></script>
	<script src="../../../javascripts/jquery.maskedinput.min.js"></script>
	<script src="../../../javascripts/base-service.js"></script>

	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2>Pesquisas de Satisfação</h2>
			<!-- content bomex -->
			<div class="gt-content-box dataTables_div">
				<div class="gt-form-row dataTables_length">
					<label> <siga:checkbox name="mostrarDesativados"
							value="${mostrarDesativados}"></siga:checkbox> <b>Incluir
							Inativas</b>
					</label>
				</div>

				<table id="pesquisa_table" border="0" class="gt-table display">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Descrição</th>
							<th></th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${pesquisas}" var="pesq">
							<tr <c:if test="${!pesq.ativo}"> class="configuracao-herdada" </c:if> data-json-id="${pesq.idPesquisa}" data-json="${pesq.toJson()}" onclick="pesquisaService.editar($(this).data('json'), 'Alterar pesquisa')"
							style="cursor: pointer;">
								<td>${pesq.nomePesquisa}</td>
								<td>${pesq.descrPesquisa}</td>
								<td class="acoes">
									<siga:desativarReativar id="${pesq.idPesquisa}" onReativar="pesquisaService.reativar" onDesativar="pesquisaService.desativar" isAtivo="${pesq.isAtivo()}"></siga:desativarReativar>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
</siga:pagina>
