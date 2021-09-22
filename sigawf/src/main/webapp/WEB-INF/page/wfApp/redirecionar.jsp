<!-- Redirecionar Modal -->
<div class="modal fade" id="redirecionarModal" tabindex="-1"
	role="dialog" aria-labelledby="redirecionarModalLabel"
	aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="redirecionarModalLabel">Redirecionar</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="redirecionarForm" name="redirecionar" method="POST"
					action="${linkTo[WfAppController].redirecionar(pi.siglaCompacta, null)}">
					<div class="form-group">
						<div class="form-group">
							<label for="descrMov">Tarefa Destino</label> <select name="tdId"
								class="form-control" required>
								<c:forEach var="td"
									items="${pi.definicaoDeProcedimento.definicaoDeTarefa}">
									<option value="${td.id}">${td.nome}</option>
								</c:forEach>
							</select> <small class="form-text text-muted float-right" id="Qtd">Selecione
								a tarefa para a qual vc deseja redirecionar o procedimento</small>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				<button type="button" class="btn btn-primary"
					onclick="javascript: document.getElementById('redirecionarForm').submit();">Redirecionar</button>
			</div>
		</div>
	</div>
</div>
<script>
	function initredirecionarModal() {

	}

	function tamanho() {
		var i = tamanho2();
		if (i < 0) {
			i = 0
		}
		;
		document.getElementById("Qtd").innerText = 'Restam ' + i
				+ ' Caracteres';
	}

	function tamanho2() {
		nota = new String();
		nota = this.redirecionar.descrMov.value;
		var i = 255 - nota.length;
		return i;
	}
	function corrige() {
		if (tamanho2() < 0) {
			alert('Descrição com mais de 255 caracteres');
			nota = new String();
			nota = document.getElementById("descrMov").value;
			document.getElementById("descrMov").value = nota.substring(0, 255);
		}
	}
</script>