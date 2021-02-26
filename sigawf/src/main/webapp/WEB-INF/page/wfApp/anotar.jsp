<!-- Anotar Modal -->
<div class="modal fade" id="anotarModal" tabindex="-1" role="dialog"
	aria-labelledby="anotarModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="anotarModalLabel">Acrescentar uma
					Anotação</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="anotarForm" name="anotar" method="POST"
					action="${linkTo[WfAppController].anotar}?id=${piId}">
					<div class="form-group">
						<div class="form-group">
							<label for="descrMov">Nota</label>
							<textarea class="form-control" name="descrMov" value="" cols="60"
								rows="5" onkeydown="corrige();tamanho();" maxlength="255"
								onblur="tamanho();" onclick="tamanho();"></textarea>
							<small class="form-text text-muted float-right" id="Qtd">Restam&nbsp;255&nbsp;Caracteres</small>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				<button type="button" class="btn btn-primary"
					onclick="javascript: document.getElementById('anotarForm').submit();">Gravar</button>
			</div>
		</div>
	</div>
</div>
<script>
	function initanotarModal() {

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
		nota = this.anotar.descrMov.value;
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