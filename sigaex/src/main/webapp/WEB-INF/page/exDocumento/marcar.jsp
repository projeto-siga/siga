<!-- Marcar Modal -->
<div class="modal fade" id="definirMarcaModal" tabindex="-1"
	role="dialog" aria-labelledby="anotarModalLabel" aria-hidden="true">
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
				<form id="anotarForm" name="marcar_gravar" method="POST"
					action="${linkTo[ExMovimentacaoController].aMarcarGravar()}">
					<input type="hidden" name="sigla" value="${sigla}" />
					<div class="form-group">
						<div class="form-group">
							<div class="form-group">
								<label for="marcador">Marcador</label> <select
									name="marcador" v-model="marcador" id="marcador"
									class="form-control">
									<option v-for="option in lista"
										v-bind:value="option.idMarcador">{{ option.nome }}</option>
								</select>
							</div>
							<div class="form-group">
								<label for="marcador">Interessado</label>
								<siga:pessoaLotaSelecao2 propriedadePessoa="subscritorSel"
									propriedadeLotacao="lotaSubscritorSel" />
							</div>
							<div class="form-group row">
								<div class="col">
									<label for="planejada">Data Planejada</label> <input
										name="planejada" id="planejada" class="form-control" />
								</div>
								<div class="col">
									<label for="limite">Data Limite</label> <input name="limite"
										id="limite" class="form-control" />
								</div>
							</div>
							<div class="form-group">
								<label for="observacoes">Justificativa</label> <input
									name="observacoes" id="observacoes" class="form-control" />
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

<script type="text/javascript" src="/sigaex/javascript/vue.min.js"></script>


<script type="text/javascript" language="Javascript1.1">
	var httpGet = function(url, success, error) {
		var xhr = XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject(
				"Microsoft.XMLHTTP");
		xhr.open("GET", url, true);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					success(xhr.responseText);
				} else {
					error(xhr, xhr.status);
				}
			}
		};
		xhr.onerror = function() {
			error(xhr, xhr.status);
		};
		xhr.send();
	}

	window.initdefinirMarcaModal = function() {
		var demo = new Vue(
				{
					el : "#definirMarcaModal",

					mounted : function() {
						this.errormsg = undefined;
						var self = this
						setTimeout(function() {
							self.carregar();
						});
					},

					data : function() {
						return {
							marcador : undefined,
							lista : [],
							carregando : false,
							primeiraCarga : true,
							errormsg : undefined
						};
					},

					methods : {
						carregar : function() {
							this.carregando = true;
							var self = this
							httpGet(
									'/sigaex/api/v1/doc/${sigla}/marcadores-disponiveis',
									function(text) {
										self.carregando = false;
										self.lista.length = 0;
										var resp = JSON.parse(text);
										for (var i = 0; i < resp.list.length; i++) {
											self.lista.push(self
													.fixItem(resp.list[i]));
										}
										self.primeiraCarga = false;
									}, function(xhr, status) {
										self.carregando = false;
										self.showError(xhr, self);
									})
						},

						fixItem : function(item) {
							this.applyDefauts(item, {
								idMarcador : undefined,
								nome : undefined,
								odd : undefined
							});
							return item;
						},

						// De UtilsBL
						applyDefauts : function(obj, defaults) {
							for ( var k in defaults) {
								if (!defaults.hasOwnProperty(k))
									continue;
								if (obj.hasOwnProperty(k))
									continue;
								obj[k] = defaults[k];
							}
						},

						showError : function(error, component) {
							component.errormsg = undefined;
							try {
								component.errormsg = error.data.errormsg;
							} catch (e) {
							}
							if (component.errormsg === undefined
									&& error.statusText
									&& error.statusText !== "") {
								component.errormsg = error.statusText;
							}
							if (component.errormsg === undefined) {
								component.errormsg = "Erro desconhecido!";
							}
						},
					}
				});
	}
</script>
