<!-- Anotar Modal -->
<div class="modal fade" id="definirMarcaModal" tabindex="-1" role="dialog"
	aria-labelledby="anotarModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="anotarModalLabel">Acrescentar uma
					Marca</h5>
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
							<div class="form-group">
							<label for="marcador">Marcador</label>
							<select name="marcador" id="marcador" class="form-control">
								<option value="1">Monitorar</option>
								<option value="2">Urgente</option>
								<option value="3">Idoso</option>
							</select>
							</div>
							<div class="form-group">
							<label for="marcador">Interessado</label>
							<siga:pessoaLotaSelecao2 propriedadePessoa="filtro.autor"
										propriedadeLotacao="filtro.lotacao" />
							</div>
							<div class="form-group">
							<label for="exibicao">Tipo de Exibição</label>
							<select name="exibicao" id="exibicao" class="form-control">
								<option value="1">Imediata</option>
								<option value="2">Depois de expirado o prazo</option>
							</select>
							</div>
							<div class="form-group row">
							<div class="col">
							<label for="planejada">Data Planejada</label>
							<input name="planejada" id="planejada" class="form-control" />
							</div>
							<div class="col">
							<label for="limite">Data Limite</label>
							<input name="limite" id="limite" class="form-control" />
							</div>
							</div>
							<div class="form-group">
							<label for="observacoes">Justificativa</label>
							<input name="observacoes" id="observacoes" class="form-control" />
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
