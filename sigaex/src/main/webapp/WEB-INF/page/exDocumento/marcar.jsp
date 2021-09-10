<!-- Marcar Modal -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="podeRetroativa" scope="session" value="${f:resource('/siga.marcadores.permite.data.retroativa')}" />
<div class="modal fade" id="definirMarcaModal" tabindex="-1"
	role="dialog" aria-labelledby="anotarModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="anotarModalLabel">
					Acrescentar
					<fmt:message key="documento.marca2" />
				</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form id="marcarForm" name="marcar_gravar" method="POST"
					action="${linkTo[ExMovimentacaoController].aMarcarGravar()}">
					<input type="hidden" name="sigla" value="${m.sigla}" />
					<div class="form-group">
						<div class="form-group">
							<div class="text-center" v-if="carregando">
								<div class="spinner-grow text-info text-center" role="status"></div>
							</div>
							<div class="form-group" v-if="!carregando">
								<label for="marcador">Marcador</label> <select name="marcador"
									v-model="idMarcador" id="marcador" class="form-control">
									<optgroup v-for="grupo in listaAgrupada"
										v-bind:label="grupo.grupo">
										<option :disabled="!option.ativo" 
											:style="!option.ativo ? 'color: LightGray;' : ''" 
											v-for="option in grupo.lista" 
											v-bind:value="option.idMarcador"
											v-if="option.ativo || !(option.explicacao).includes('não está marcado ')"
											>{{ option.nome }}{{option.ativo || (option.explicacao).includes('não está marcado')? '' : ' (Já marcado no documento)'}}</option>
									</optgroup>
								</select>
							</div>
							<div class="form-group" v-if="exibirInteressado">
								<label for="marcador">Interessado</label> <select
									name="interessado" v-model="interessado" id="interessado"
									class="form-control">
									<option v-if="marcador.interessado.includes('PESSOA')"
										value="pessoa">Pessoa</option>
									<option v-if="marcador.interessado.includes('LOTACAO')"
										value="lotacao">Lotacao</option>
								</select>
							</div>
							<div v-if="exibirLotacao" class="form-group">
								<label for="marcador">Lotacao</label>
								<siga:selecao tema='simple' titulo="Lotação:"
									propriedade="lotaSubscritor" modulo="siga" />
							</div>
							<div v-if="exibirPessoa" class="form-group">
								<label for="marcador">Pessoa</label>
								<siga:selecao tema='simple' titulo="Matrícula:"
									propriedade="subscritor" modulo="siga" />
							</div>
							<div
								v-if="marcador && (marcador.planejada != 'DESATIVADA' || marcador.limite !== 'DESATIVADA')"
								class="form-group row">
								<div class="col col-12 col-md-6"
									v-if="marcador && marcador.planejada != 'DESATIVADA'">
									<label for="planejada">Data de Exibição</label> <input
										name="planejada" id="planejada" class="form-control campoData"
										autocomplete="off" />
								</div>
								<div class="col col-12 col-md-6"
									v-if="marcador && marcador.limite != 'DESATIVADA'">
									<label for="limite">Prazo Final</label> <input name="limite"
										id="limite" class="form-control campoData"
										autocomplete="off" />
								</div>
							</div>
							<div class="form-group"
								v-if="marcador && marcador.texto && marcador.texto != 'DESATIVADA'">
								<label for="texto">Texto</label> <input name="texto" id="texto"
									class="form-control" />
							</div>

						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				<button type="button" class="btn btn-primary"
					onclick="javascript: sbmt();">Gravar</button>
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
						interessado : undefined,
						idMarcador : undefined,
						lista : [],
						carregando : false,
						primeiraCarga : true,
						errormsg : undefined
					};
				},

				computed : {
					listaAgrupada : function() {
						if (!this.lista)
							return;
						var l = [];
						for (var i = 0; i < this.lista.length; i++) {
							var m = this.lista[i];
							if (l.length == 0
									|| l[l.length - 1].grupo != m.grupo)
								l.push({
									grupo : m.grupo,
									lista : []
								})
							l[l.length - 1].lista.push(m)
						}
						return l;
					},
					marcador : function() {
						if (!this.idMarcador)
							return;
						for (var i = 0; i < this.lista.length; i++) {
							if (this.lista[i].idMarcador == this.idMarcador)
								return this.lista[i];
						}
						return;
					},
					exibirInteressado : function() {
						if (!this.marcador)
							return false;
						if (this.marcador.interessado
								&& this.marcador.interessado != 'ATENDENTE')
							return true;
						return false;
					},
					exibirPessoa : function() {
						if (!this.marcador || !this.marcador.interessado)
							return false;
						if (this.marcador.interessado == 'PESSOA')
							return true;
						return this.interessado == 'pessoa';
					},
					exibirLotacao : function() {
						if (!this.marcador || !this.marcador.interessado)
							return false;
						if (this.marcador.interessado == 'LOTACAO')
							return true;
						return this.interessado == 'lotacao';
					}
				},
				
				watch : {
					marcador : function() {
						this.$nextTick(
							function() {
								$('.campoData').datepicker({
						           	onSelect: function(){
						                   ${onSelect}
									}
								})
							}
						)
					}
				},

				methods : {
					carregar : function() {
						this.carregando = true;
						var self = this
						httpGet(
								'/sigaex/api/v1/documentos/${m.mob.codigoCompacto}/marcadores-disponiveis',
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
								&& error.statusText && error.statusText !== "") {
							component.errormsg = error.statusText;
						}
						if (component.errormsg === undefined) {
							component.errormsg = "Erro desconhecido!";
						}
					}
				}
			});

	window.initdefinirMarcaModal = function() {
	}

	function sbmt() {
		var dtPlanejada = document.getElementById('planejada');
		var dtLimite = document.getElementById('limite');
		
		if (!${podeRetroativa} && dtPlanejada != null)
			if (!verifica_data(dtPlanejada,0,false,false))
				return;

		if (!${podeRetroativa} && dtLimite != null)
			if (!verifica_data(dtLimite,0,false,false))
				return;
		
		document.getElementById('marcarForm').submit();
	}
	
</script>
