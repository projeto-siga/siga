<!-- Anotar Modal -->
<div class="modal fade" id="definirMarcaModal" tabindex="-1" role="dialog"
	aria-labelledby="anotarModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="anotarModalLabel">Acrescentar/Editar Marcador</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="anotarForm" name="anotar" method="POST"
					action="${linkTo[WfAppController].anotar}?id=${task.id}">
					<div class="form-group">
						<div class="form-group">
							<div class="form-group row">
							<div class="col">
							<label for="planejada">Nome</label>
							<input name="planejada" id="planejada" class="form-control" />
							</div>
							<div class="col">
							<label for="limite">Descrição</label>
							<input name="limite" id="limite" class="form-control" />
							</div>
							</div>
							<div class="form-group row">
							<div class="col">
							<label for="marcador">Tipo de Marcador</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Global</option>
								<option value="2">Unidade</option>
								<option value="3">Taxonomia</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Aplicação de Marcador</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Geral</option>
								<option value="2">Em todas as vias</option>
								<option value="3">Via específica ou último volume</option>
							</select>
							</div>
							</div>
							<div class="form-group row">
							<div class="col">
							<label for="marcador">Cor</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Amarelo</option>
								<option value="2">Azul</option>
								<option value="3">Vermelhor</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Ícone</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Pessoa</option>
								<option value="2">Etiqueta</option>
								<option value="3">Bomba</option>
							</select>
							</div>
							</div>
							<div class="form-group row">
							<div class="col">
							<label for="marcador">Data Planejada</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Data Limite</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							</div>
							<div class="form-group row">
							<div class="col">
							<label for="marcador">Opções de Exibição</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="2">Imediata</option>
								<option value="3">Depois de expirado o prazo</option>
								<option value="3">Selecionar</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Justificativa</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							</div>

							<label for="observacoes">Opções de Definição do Interessado</label>
							<div class="form-group row">
							<div class="col">
							<label for="marcador">Atentende</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="2">Default</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Lotação</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="2">Default</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							<div class="col">
							<label for="marcador">Pessoa</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Desativada</option>
								<option value="2">Opcional</option>
								<option value="2">Default</option>
								<option value="3">Obrigatória</option>
							</select>
							</div>
							</div>
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
