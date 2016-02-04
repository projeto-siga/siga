<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Disponibilidade de itens de configuração">

	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/template.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>

	<style>
td .orgao:first-child {
	margin-left: 10px;
}

.orgao {
	width: 15px;
	height: 15px;
	display: inline-block;
	padding-right: 5px;
	line-height: 0px;
	vertical-align: middle !important;
}

.item-pai {
	padding-left: 0em !important;
	font-weight: bold !important;
}

.item-filho {
	padding-left: 2em !important;
	font-style: italic !important;
}

.item-neto {
	padding-left: 4em !important;
}
</style>

	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2>Disponibilidades de Itens de Configura&ccedil;&atilde;o</h2>

			<div class="gt-content-box dataTables_div">
				<table id="itens_configuracao_table" class="gt-table display"></table>
			</div>
		</div>
	</div>

	<!-- template utilizado para adicionar os orgaos -->
	<div id="templateDisponibilidadeItem" style="display: none">
		{for orgao in orgaos}
			
			<div class='orgao' data-sigla-orgao="!{orgao.sigla}"
				data-item-configuracao-id="!{itemConfiguracao.id}"
				onclick="disponibilidadeService.alterarDisponibilidade('!{orgao.sigla}', !{itemConfiguracao.hisIdIni})">
				<img src=""
					src-original="!{disponibilidadeService.construirIcone(orgao.sigla, itemConfiguracao)}"/>
			</div>
		{/for}
	</div>

	<!-- template utilizado para mostrar as siglas dos orgaos no titulo da tabela -->
	<div id="templateSiglasOrgaos" style="display: none">
		{for orgao in orgaos}
			<div class="orgao">!{orgao.sigla}</div>
		{/for}
	</div>

	<!-- modal de cadastro -->
	<sigasr:modal nome="disponibilidadeItem" titulo="Disponibilidade do item">
		<div class="gt-form gt-content-box">
			<form id="disponibilidadeForm">
				<input type="hidden" name="idDisponibilidade">

				<div class="gt-form-row gt-width-66">
					<label>Tipo <span>*</span></label> <select
						id="tipo" name="disponibilidade.tipo" class="select-siga"
						onchange="disponibilidadeService.onChangeTipo()">
						<c:forEach items="${tipoDisponibilidades}"
							var="tipoDisponibilidade">
							<option value="${tipoDisponibilidade}">${tipoDisponibilidade.descricao}</option>
						</c:forEach>
					</select>

				</div>
				<div id="camposDisponibilidade">
					<div id="div_mensagem" class="gt-form-row gt-width-66">
						<label>Mensagem<span>*</span></label>
						<textarea id="mensagem" name="disponibilidade.mensagem" cols="60" maxlength="255" required></textarea>
					</div>
					<div class="gt-form-row gt-width-66">
						<label>Detalhamento t&eacute;cnico<span>*</span></label>
						<textarea id="detalhamentoTecnico" name="disponibilidade.detalhamentoTecnico" cols="60" maxlength="255"
							required></textarea>
					</div>

					<div id="erroHorarioInvalido"></div>

					<div class="gt-form-row gt-width-66">
						<label>Data/hora in&iacute;cio</label>
						<input id="dataHoraInicio" class="dataHora" name="disponibilidade.dataHoraInicio" />
					</div>

					<div class="gt-form-row gt-width-66">
						<label>Data/hora t&eacute;rmino</label>
						<input id="dataHoraTermino" class="dataHora" name="disponibilidade.dataHoraTermino" />
					</div>
				</div>

				<div class="gt-form-row">
					<input type="button" value="Gravar"
						class="gt-btn-medium gt-btn-left"
						onclick="disponibilidadeService.gravar()" /> <a
						class="gt-btn-medium gt-btn-left"
						onclick="disponibilidadeService.cancelarGravacao()">Cancelar</a> <input
						type="button" value="Aplicar" class="gt-btn-medium gt-btn-left"
						onclick="disponibilidadeService.aplicar()" />
				</div>
			</form>
		</div>
	</sigasr:modal>
</siga:pagina>
<script>
	var opts = {
			 urlGravar : '${linkTo[DisponibilidadeController].gravar}',
			 formCadastro : $('#disponibilidadeForm'),
			 dialogCadastro : $('#disponibilidadeItem_dialog'),
			 objectName : 'disponibilidade'
	};

	// Define a "classe" DisponibilidadeService
	function DisponibilidadeService(opts) {
		// super(opts)
		BaseService.call(this, opts);

		this.niveis = {};
		this.niveis[1] = 'item-pai';
		this.niveis[2] = 'item-filho';
		this.niveis[3] = 'item-neto';

		this.orderBy = {};
		this.orderBy[0] = 'siglaItemConfiguracao';
		this.orderBy[1] = 'tituloItemConfiguracao';
		this.tipos = {};

		this.jsonToRowData = function(pagina) {
			var dados = [],
				construirOrgaos = this.construirOrgaos;

// 			pagina.registros.elements.forEach(function(itemConfiguracao) {
			pagina.registros.forEach(function(itemConfiguracao) {
				var context = {
// 						orgaos : pagina.orgaos.elements,
						orgaos : pagina.orgaos,
						itemConfiguracao : itemConfiguracao
				},
				row = [itemConfiguracao.siglaItemConfiguracao || " ",
							itemConfiguracao.tituloItemConfiguracao || " ",
							construirOrgaos(context)];

				dados.push(row);
			});
			this.colunaOrgaos()
				.html(this.conteudoColunaOrgaos(pagina));

			return dados;
		}

		this.colunaOrgaos = function() {
			return $('tr:nth(0) th:last');
		}

		this.conteudoColunaOrgaos = function(pagina) {
			return $('#templateSiglasOrgaos').html().process({
// 				orgaos : pagina.orgaos.elements,
				orgaos : pagina.orgaos,
			});
		}

		this.construirOrgaos = function(context) {
			return $('#templateDisponibilidadeItem').html().process(context);
		}

		this.construirIcone = function(siglaOrgao, itemConfiguracao) {
			for(var i = 0; i < itemConfiguracao.disponibilidades.length; i++) {
				var disponibilidade = itemConfiguracao.disponibilidades[i];
				if(siglaOrgao == disponibilidade.siglaOrgao) {
					return disponibilidade.caminhoIcone;
				}
			}
			return "/sigasr/images/icons/disponibilidade/sem_disponibilidade.png";
		}

		this.alterarDisponibilidade = function(sigla, hisIdIni) {
			if(this.validator) {
				this.validator.resetForm();
			}
			this.esconderValidacaoHorario();

			$('#disponibilidadeItem_dialog').dialog('open');

			this.orgaoSelecionado = this.buscarOgao(sigla);
			this.itemConfiguracaoSelecionado = this.buscarItem(hisIdIni);
			this.disponibilidadeSelecionada = this.buscarDisponibilidade(this.itemConfiguracaoSelecionado, sigla);

			new Formulario($('#disponibilidadeForm')).populateFromJson(this.disponibilidadeSelecionada);
			$('#tipo').trigger('change');
		}

		this.buscarOgao = function(sigla) {
// 			for(var i = 0; i < this.pagina.orgaos.elements.length; i++) {
			for(var i = 0; i < this.pagina.orgaos.length; i++) {
// 				var orgao = this.pagina.orgaos.elements[i];
				var orgao = this.pagina.orgaos[i];
				if(orgao.sigla == sigla)
					return orgao;
			}
			return null;
		}

		this.buscarItem = function(hisIdIni) {
// 			for(var i = 0; i < this.pagina.registros.elements.length; i++) {
			for(var i = 0; i < this.pagina.registros.length; i++) {
// 				var itemConfiguracao = this.pagina.registros.elements[i];
				var itemConfiguracao = this.pagina.registros[i];
				if(itemConfiguracao.hisIdIni == hisIdIni)
					return itemConfiguracao;
			}
			return null;
		}

		this.buscarDisponibilidade = function(itemConfiguracaoSelecionado, sigla) {
			if(itemConfiguracaoSelecionado) {
				var indice = this.buscarIndiceDisponibilidade(itemConfiguracaoSelecionado, sigla);
				return itemConfiguracaoSelecionado.disponibilidades[indice];
			}
			return {
				siglaOrgao : sigla,
				tipo : 'NENHUM'
			};
		}

		this.buscarIndiceDisponibilidade = function(itemConfiguracaoSelecionado, sigla) {
			for(var i = 0; i < itemConfiguracaoSelecionado.disponibilidades.length; i++) {
				var disponibilidade = itemConfiguracaoSelecionado.disponibilidades[i];
				if(disponibilidade.siglaOrgao == sigla)
					return i;
			}
			return -1;
		}

		this.cancelarGravacao = function() {
			$('#disponibilidadeItem_dialog').dialog('close');
		}

		this.onChangeTipo = function() {

			disponibilidadeService.resetErrosForm();

			var tipo = $('#tipo').val();

			if(this.deveEsconderTudo(tipo)) {
				this.esconderCamposDisponibilidade();

			} else if(this.deveEsconderMensagem(tipo)) {
				this
					.mostrarCamposDisponibilidade()
					.esconderCampoMensagem();

			} else {
				this.mostrarCamposDisponibilidade();
			}
		}

		this.esconderCampoMensagem = function() {
			$('#div_mensagem').hide();
			$('#div_mensagem textarea').val('');
			return this;
		}

		this.esconderCamposDisponibilidade = function() {
			$('#camposDisponibilidade').hide();
			$('#camposDisponibilidade input, #camposDisponibilidade textarea').val('');
			return this;
		}

		this.mostrarCamposDisponibilidade = function() {
			$('#div_mensagem').show();
			$('#camposDisponibilidade').show();
			return this;
		}

		this.deveEsconderTudo = function(tipo) {
			return ['DISPONIVEL', 'NENHUM'].indexOf(tipo) > -1;
		}

		this.deveEsconderMensagem = function(tipo) {
			return ['INDISPONIVEL_BLOQUEIO_SOLICITACOES', 'NAO_UTILIZADO'].indexOf(tipo) > -1;
		}

		this.getId = function(disponibilidade) {
			return disponibilidade.idDisponibilidade;
		}

		this.onGravar = function(obj, objSalvo) {
			var me = this;

// 			objSalvo.disponibilidadesAtualizadas.elements.forEach(function(atualizacao) {
			objSalvo.disponibilidadesAtualizadas.forEach(function(atualizacao) {
				var div = $('div[data-sigla-orgao=' + atualizacao.siglaOrgao + '][data-item-configuracao-id=' + atualizacao.idItemConfiguracao + ']'),
					img = div.find('img'),
					novaImg = $('<img>');

				img.remove();
				novaImg.attr('src', atualizacao.caminhoIcone);
				div.append(novaImg);

				var itemConfiguracaoSelecionado = me.buscarItem(atualizacao.hisIdIniItemConfiguracao);
				if(itemConfiguracaoSelecionado) {
					var indice = me.buscarIndiceDisponibilidade(itemConfiguracaoSelecionado, atualizacao.siglaOrgao);
					itemConfiguracaoSelecionado.disponibilidades[indice] = atualizacao;
				}
			});
		}

		this.getObjetoParaGravar = function() {
			var disponibilidade = BaseService.prototype.getObjetoParaGravar.call(this, opts);
			// Precisa estar assim para recarregar o item de configuracao utilizando o SrItemConfiguracaoBinder.java
			
			disponibilidade.itemConfiguracao = this.itemConfiguracaoSelecionado.id;
			disponibilidade.orgao = this.orgaoSelecionado.idOrgaoUsu;
			disponibilidade["disponibilidade.idDisponibilidade"] = disponibilidade.idDisponibilidade;
			
			var pagina = Object.create(this.pagina);
			disponibilidade.pagina = new Object();
			disponibilidade.pagina.tamanho = pagina.tamanho;
			disponibilidade.pagina.numero = pagina.numero;    
			disponibilidade.pagina.count = pagina.count;         
			disponibilidade.pagina.orderBy = pagina.orderBy;         
			disponibilidade.pagina.direcaoOrdenacao = pagina.direcaoOrdenacao;
			disponibilidade.pagina.tituloOuCodigo = pagina.tituloOuCodigo;

			// WA para a serializacao da data com horario que nao estava funcionando.
			if(disponibilidade["disponibilidade.dataHoraInicio"]) {
				disponibilidade["disponibilidade.dataHoraInicioString"] = disponibilidade["disponibilidade.dataHoraInicio"];
				delete disponibilidade["disponibilidade.dataHoraInicio"];
			}

			if(disponibilidade["disponibilidade.dataHoraTermino"]) {
				disponibilidade["disponibilidade.dataHoraTerminoString"] = disponibilidade["disponibilidade.dataHoraTermino"];
				delete disponibilidade["disponibilidade.dataHoraTermino"];
			}
			 
			return disponibilidade;
		}

		this.post = function(opts) {
			this.removerErros();
			opts.obj.pagina = Object.create(this.pagina);
			opts.obj.pagina.registros = {};
			opts.obj.pagina.orgaos = {};

			return $.ajax({
		    	type: "POST",
		    	url: opts.url,
		    	data: jQuery.param(opts.obj),
		    	dataType: "text",
		    	error: BaseService.prototype.errorHandler
		   	});
		}

		this.iniciarDataTable = function() {
			var me = this;

			$('#itens_configuracao_table').dataTable({
				stateSave : true,
				"bProcessing": true,
		        "bServerSide": true,
		        "sAjaxSource" : "${linkTo[DisponibilidadeController].listarPagina}",
				"language": {
					"emptyTable":     "N&atilde;o existem resultados",
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
				        "last":       "&Uacute;ltimo",
				        "next":       "Pr&oacute;ximo",
				        "previous":   "Anterior"
				    },
				    "aria": {
				        "sortAscending":  ": clique para ordena&ccedil;&atilde;o crescente",
				        "sortDescending": ": clique para ordena&ccedil;&atilde;o decrescente"
				    }
				},
				"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
					var order = me.orderBy[oSettings.aaSorting[0][0]],
						orderDirection = oSettings.aaSorting[0][1];

		    		sSource += '?pagina.tamanho=' + oSettings._iDisplayLength;
		    		sSource += '&pagina.numero=' + ((oSettings._iDisplayStart / oSettings._iDisplayLength) + 1);
		    		sSource += '&pagina.count=' + oSettings.iTotalRecords;
		    		sSource += '&pagina.orderBy=' + order;
		    		sSource += '&pagina.direcaoOrdenacao=' + orderDirection;
		    		sSource += '&pagina.tituloOuCodigo=' + oSettings.oPreviousSearch.sSearch;

				    oSettings.jqXHR = $.ajax({
				        dataType: 'json',
				        url: sSource,
				        accepts: "application/json",
				        blockUI : false,
				        success: function (pagina) {
					        var config = {};
					        config.aaData = me.jsonToRowData(pagina);
					        config.iTotalRecords = pagina.count;
					        config.iTotalDisplayRecords = (pagina.count / pagina.tamanho) * oSettings._iDisplayLength;
					        config.sEcho = aoData[0].value;
					        oSettings.iTotalRecords = pagina.count;

					        me.pagina = pagina;
				            fnCallback(config);

				            // WA para evitar requisicao para URL errada da imagem
				            $('img[src-original]').each(function() {
					            var me = $(this),
					            	src = me.attr('src-original');

					            if(src.indexOf('.png') >= 0) {
						            me.attr('src',me.attr('src-original'));
						            me.removeAttr('src-original');
					            }
				            });
				        }
				    });
				},
				"aoColumns":
						[{
					        "sTitle": "C&oacute;digo",
					    }, {
					        "sTitle": "T&iacute;tulo",
					    }, {
					        "sTitle": "&Oacute;rg&atilde;os",
					        "sWidth" : "500px"
					    }],
				// Define que a ordenacao deve comecar na segunda coluna (para evitar coluna com acoes de expandir)
				"aaSorting" : [[0, 'asc']],
				"columnDefs": [
					{
						"targets": [2],
						"searchable": false,
						"sortable": false
					},
				],
				"createdRow" : function(row, data, index) {
// 					var itemConfiguracao = me.pagina.registros.elements[index],
					var itemConfiguracao = me.pagina.registros[index],
						td = $('td:eq(1)', row),
						nivelClass = me.niveis[itemConfiguracao.nivel];

					td.addClass(nivelClass);
				}
			});
			return this;
		}

		this.iniciarValidacoes = function() {
			var me = this;
			$(".dataHora").mask("99/99/9999 99:99");
			$.validator.addMethod(
			        "dataHora", function(value, element, regexp) {
				        var re = new RegExp('^([0-9]{2}\/[0-9]{2}\/[0-9]{4} [0-9]?[0-9]|2[0-3]):[0-5][0-9]$');
				        var horaExtraida = value.match('(\\d\\d):'); // Para nao permitir hora 24:00
				        var hora = horaExtraida ? horaExtraida[0].replace(':', '') : 0;

				        return (this.optional(element) || (hora < 24 && re.test(value) && moment(value, 'DD/MM/YYYY HH:mm').isValid()));
			        },
			        "Data/Hora inv&aacute;lida."
			);
			$.validator.addMethod(
			        "periodo", function(value, element, regexp) {
			        	return me.horarioEstaValido();
			        },
			        "A data/hora de in&iacute;cio deve ser menor que a data/hora t&eacute;rmino"
			);
			this.validator = jQuery("form").validate();
			$("#dataHoraInicio").rules("add", {dataHora: ""});
			$("#dataHoraTermino").rules("add", {dataHora: ""});
			$("#dataHoraInicio").rules("add", {periodo: ""});
			$("#dataHoraTermino").rules("add", {periodo: ""});
			return this;
		}

		this.horarioEstaValido = function() {
			var dataHoraInicioString = $("#dataHoraInicio").val(),
				dataHoraTerminoString = $("#dataHoraTermino").val();

			if(dataHoraInicioString && dataHoraTerminoString) {
				var dataHoraInicio = moment(dataHoraInicioString, 'DD/MM/YYYY HH:mm'),
					dataHoraTermino = moment(dataHoraTerminoString, 'DD/MM/YYYY HH:mm'),
					valido = true,
					mensagem = null;

				return dataHoraInicio.isBefore(dataHoraTermino);
			}
			return true;
		}

		this.mostrarValidacaoHorario = function(mensagem) {
			if(mensagem) {
				$('#erroHorarioInvalido').css('display', 'block');
				$('#erroHorarioInvalido span').html(mensagem);
			}
		}

		this.esconderValidacaoHorario = function() {
			$('#erroHorarioInvalido').css('display', 'none');
		}
	}

	DisponibilidadeService.prototype = Object.create(BaseService.prototype);
	var disponibilidadeService = new DisponibilidadeService(opts);
	disponibilidadeService
		.iniciarDataTable()
		.iniciarValidacoes();

</script>