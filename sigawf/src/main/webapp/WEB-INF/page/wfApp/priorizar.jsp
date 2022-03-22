<!-- Anotar Modal -->
<div class="modal fade" id="priorizarModal" tabindex="-1" role="dialog"
	aria-labelledby="priorizarModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="priorizarModalLabel">Alterar a
					Prioridade</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="priorizarForm" name="priorizar" method="POST"
					action="${linkTo[WfAppController].priorizar(pi.siglaCompacta)}">
					<div class="form-group">
						<div class="form-group">
							<label for="descrMov">Prioridade</label> <select
								class="form-control" name="prioridade">
								<option value="MUITO_ALTA" ${pi.prioridade == 'MUITO_ALTA' ? 'selected' : ''}>Muito Alta</option>
								<option value="ALTA" ${pi.prioridade == 'ALTA' ? 'selected' : ''}>Alta</option>
								<option value="MEDIA" ${pi.prioridade == 'MEDIA' ? 'selected' : ''}>Média</option>
								<option value="BAIXA" ${pi.prioridade == 'BAIXA' ? 'selected' : ''}>Baixa</option>
								<option value="MUITO_BAIXA" ${pi.prioridade == 'MUITO_BAIXA' ? 'selected' : ''}>Muito Baixa</option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				<button type="button" class="btn btn-primary"
					onclick="javascript: document.getElementById('priorizarForm').submit();">Gravar</button>
			</div>
		</div>
	</div>
</div>
<script>
	function initpriorizarModal() {

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
		nota = this.priorizar.descrMov.value;
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