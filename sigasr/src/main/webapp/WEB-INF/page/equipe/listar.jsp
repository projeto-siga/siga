<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Equipes">
	
	<jsp:include page="../main.jsp"></jsp:include>
	
	<style>
	.ui-widget-header {
		border: 1px solid #365b6d;
		background: #365b6d;
	}
	</style>
	
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>
	
	
	
	<div class="gt-bd clearfix">
	    <input type="hidden" id="lotacaoUsuario" name="lotacaoUsuario" value='${lotacaoUsuario.toJson()}'/>
		<div class="gt-content">
			<h2>Pesquisa de Equipes</h2>
			<!-- content bomex -->
			<div class="gt-content-box gt-for-table dataTables_div">
				<table id="equipes_table" class="gt-table display">
					<thead>
						<tr>
							<th style="font-weight: bold;">Sigla</th>
							<th>Lotação</th>
						</tr>
					</thead>	
					<tbody>
						<c:forEach items="${listaEquipe}" var="equipe">
							<tr <c:if test="${equipe.podeEditar(lotaTitular, cadastrante)}">
									onclick="equipeService.editar($(this).data('json'), 'Alterar Equipe')" style="cursor: pointer;"</c:if>
								data-json-id="${equipe.idEquipe}" data-json='${equipe.toJson()}'>
								<td class="gt-celula-nowrap" style="font-size: 13px; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
									${equipe.lotacaoEquipe.sigla}
								</td>
								<td class="gt-celula-nowrap" style="font-size: 13px; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
									${equipe.lotacaoEquipe.descricao}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="gt-table-buttons">
				<a onclick="equipeService.cadastrar('Incluir Equipe')" class="gt-btn-medium gt-btn-left">Incluir</a>
			</div>
		</div>
	</div>
	
	<sigasr:modal nome="equipe" titulo="Cadastrar Equipe">
		<div id="divEditarEquipeForm"><jsp:include page="editar.jsp"></jsp:include></div>
	</sigasr:modal>
</siga:pagina>

<script type="text/javascript">
	var opts = {
			 urlGravar : '${linkTo[EquipeController].gravarEquipe}',
			 dialogCadastro : $('#equipe_dialog'),
			 tabelaRegistros : $('#equipes_table'),
			 objectName : 'equipe',
			 formCadastro : $('#form')
	};

	$(document).ready(function() {
		if ( $.fn.dataTable.isDataTable( '#equipes_table' ) ) {
			opts.dataTable = $('#equipes_table').dataTable();
		}
		else {
			/* Table initialization */
			opts.dataTable = $('#equipes_table').dataTable({
				stateSave : true,
				"language": {
					"emptyTable":     "Não existem resultados",
				    "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
				    "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
				    "infoFiltered":   "(filtrando do total de _MAX_ registros)",
				    "infoPostFix":    "",
				    "thousands":      ".",
				    "lengthMenu":     "Mostrar _MENU_ registros",
				    "loadingRecords": "Carregando...",
				    "processing":     "Processando...",
				    "search":         "Filtrar:",
				    "zeroRecords":    "Nenhum registro encontrado",
				    "paginate": {
				        "first":      "Primeiro",
				        "last":       "Último",
				        "next":       "Próximo",
				        "previous":   "Anterior"
				    },
				    "aria": {
				        "sortAscending":  ": clique para ordenação crescente",
				        "sortDescending": ": clique para ordenação decrescente"
				    }
				},
				"iDisplayLength": 25
			});
		}
	});

	function EquipeService(opts) {
		BaseService.call(this, opts);
	}
	EquipeService.prototype = Object.create(BaseService.prototype);
	
	var equipeService = new EquipeService(opts);

	equipeService.getId = function(equipe) {
		return equipe.idEquipe || equipe['equipe.idEquipe'];
	}

	equipeService.getRow = function(equipe) {
		return [equipe.lotacaoEquipe.sigla, equipe.lotacaoEquipe.descricao];
	}

	equipeService.onRowClick = function(equipe) {
		equipeService.editar(equipe, 'Alterar Equipe');
	}

	equipeService.serializar = function(obj) {
		var serializado = BaseService.prototype.serializar.call(this, obj)  + "&" + equipeService.getListasAsString();
		return serializado + "&equipe=" + this.getId(obj);
	}
	
	equipeService.getListasAsString = function() {
		var params = '', dataAux = new Date();

		// Percorre lista de Exceções
		equipeService.excecoesTable.api().rows().indexes().each(
				function(i) {
					var rowValues = equipeService.excecoesTable.api().row(i)
							.data();

					// Atualiza a string serializada
					if (rowValues) {
						if (rowValues[0] == 0)
							params += '&excecaoHorarioSet[' + i
									+ '].strDataEspecifica=' + atualizaData(rowValues[1]).toJSON();
						else
							params += '&excecaoHorarioSet[' + i
									+ '].diaSemana=' + rowValues[0];

						params += '&excecaoHorarioSet[' + i
								+ '].strHoraIni='
								+ atualizaHora(dataAux, rowValues[3]).toJSON();
						params += '&excecaoHorarioSet[' + i
								+ '].strHoraFim='
								+ atualizaHora(dataAux, rowValues[4]).toJSON();
						params += '&excecaoHorarioSet[' + i
								+ '].strInterIni='
								+ atualizaHora(dataAux, rowValues[5]).toJSON();
						params += '&excecaoHorarioSet[' + i
								+ '].strInterFim='
								+ atualizaHora(dataAux, rowValues[6]).toJSON();
					}
				});
		return params;
	}

	/**
	 * Customiza o metodo editar
	 */
	equipeService.editar = function(obj, title) {
		BaseService.prototype.editar.call(this, obj, title); // super.editar();
		equipeService.atualizarModalEquipe(obj);
	}

	/**
	 * Sobescreve o metodo cadastrar para limpar a tela.
	 */
	equipeService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title); // super.editar();
		equipeService.atualizarModalEquipe();
	}

	/**
	 * Constroi a datatable de excessoes de horario
	 */
	equipeService.construirExcessoesDataTable = function() {
		if ($.fn.dataTable.isDataTable('#excecoes_table')) {
			this.excecoesTable = $('#excecoes_table').dataTable();
		} else {
			this.excecoesTable = $('#excecoes_table')
					.dataTable(
							{
								"language" : {
									"emptyTable" : "Não existem resultados",
									"info" : "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
									"infoEmpty" : "Mostrando de 0 a 0 do total de 0 registros",
									"infoFiltered" : "(filtrando do total de _MAX_ registros)",
									"infoPostFix" : "",
									"thousands" : ".",
									"lengthMenu" : "Mostrar _MENU_ registros",
									"loadingRecords" : "Carregando...",
									"processing" : "Processando...",
									"search" : "Filtrar:",
									"zeroRecords" : "Nenhum registro encontrado",
									"paginate" : {
										"first" : "Primeiro",
										"last" : "Último",
										"next" : "Próximo",
										"previous" : "Anterior"
									},
									"aria" : {
										"sortAscending" : ": clique para ordenação crescente",
										"sortDescending" : ": clique para ordenação decrescente"
									}
								},
								"columnDefs" : [ {
									"targets" : [ 0, 1 ],
									"visible" : false,
									"searchable" : false
								} ],
								"iDisplayLength" : 25
							});
		}

		// Remover exceções
		$('#excecoes_table tbody').on(
				'click',
				'a.excecao_remove',
				function() {
					equipeService.excecoesTable.api()
							.row($(this).closest('tr')).remove().draw(false);
				});
	}
	/**
	 * Atualiza a modal de cadastro 
	 */
	equipeService.atualizarModalEquipe = function(equipe) {
		equipeService.finalizarExcessoesTable();

		if (equipe) {
			equipeService.carregarExcecoes(equipe);
			equipeService.carregarDesignacoes(equipe.idEquipe);
		}
		// Caso seja cadastro, atualiza os dados da Lotação
		else {
			var lotacaoUsuarioValue = $("#lotacaoUsuario").val();
		    if (lotacaoUsuarioValue && lotacaoUsuarioValue != "") {
				var lota = JSON.parse(lotacaoUsuarioValue);
				equipeEdicao = {
					formulario_lotacaoEquipeSel_id : lota.id,
					formulario_lotacaoEquipeSel_descricao : lota.descricao,
					formulario_lotacaoEquipeSel_sigla : lota.sigla,
					lotacaoEquipeSelSpan : lota.descricao
				};
		    }else{
		    	equipeEdicao = {};
			}
			// chama o editar para popular o campo da lotação
			equipeService.formularioHelper.populateFromJson(equipeEdicao);
			designacaoService.populateFromJSonList({});
		}
		equipeService.construirExcessoesDataTable();
	}

	/**
	 * Finaliza a tabela de excessoes.
	 */
	equipeService.finalizarExcessoesTable = function() {
		if (equipeService.excecoesTable) {
			equipeService.excecoesTable.fnDestroy();
		}
		TableHelper.limpar($("#excecoes_table"));
	}
	/**
	 * Preenche a lista de excecoes de horario
	 */
	equipeService.carregarExcecoes = function(equipe) {
		var table = $('#excecoes_table');

		if (equipe.excecaoHorarioSet) {
			// cria a lista de Exceções de horário, e adiciona na tela
			for (i = 0; i < equipe.excecaoHorarioSet.length; i++) {
				var item = equipe.excecaoHorarioSet[i];
				row = [
						item.diaSemana ? item.diaSemana : 0,
						item.diaSemana ? ''
								: (item.dataEspecifica ? item.dataEspecifica
										: ''),
						item.descrDiaSemana ? item.descrDiaSemana
								: (item.dataEspecifica ? moment(
										new Date(item.dataEspecifica)
												.toISOString()).format(
										"DD/MM/YYYY") : ''),
						item.horaIni ? moment(
								new Date(item.horaIni).toISOString()).format(
								"HH:mm") : '',
						item.horaFim ? moment(
								new Date(item.horaFim).toISOString()).format(
								"HH:mm") : '',
						item.interIni ? moment(
								new Date(item.interIni).toISOString()).format(
								"HH:mm") : '',
						item.interFim ? moment(
								new Date(item.interFim).toISOString()).format(
								"HH:mm") : '',
						"<a class=\"excecao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>" ];
				table.append(TableHelper.criarTd(row));
			}
		}
	}
	/**
	 * Preenche a lista de designacoes. Executa uma chamada ajax ao servidor e recarrega a lista da tela.
	 */
	equipeService.carregarDesignacoes = function(id) {
		$
				.ajax({
					type : "GET",
					url : "/sigasr/app/equipe/" + id + "/designacoes",
					dataType : "text",
					success : function(lista) {
						var listaJSon = JSON.parse(lista);
						designacaoService.populateFromJSonList(listaJSon);
					},
					error : function(error) {
						alert("Não foi possível carregar as Designações desta Equipe.");
					}
				});
	}
</script>