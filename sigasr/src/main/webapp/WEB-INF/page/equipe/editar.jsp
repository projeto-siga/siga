<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
<script src="/sigasr/javascripts/detalhe-tabela.js"></script>

<div class="container-fluid mb-2">
	<form id="form" class="formEditarEquipe" enctype="multipart/form-data">
		<input type="hidden" id="idEquipe" name="equipe.idEquipe"> <input
			type="hidden" id="idEquipeIni" name="equipe.hisIdIni">

		<p class="gt-error" style="display: none;"
			id="erroEquipeCamposObrigatorios">Alguns campos obrigatórios
			não foram preenchidos</p>

		<div class="card mb-2">
			<div class="card-header">
				<h3>Dados básicos</h3>
			</div>
			<div class="card card-body">
				<label>Lotação</label> <input type="hidden" name="lotacaoEquipe"
					id="lotacaoEquipe" class="selecao">
				<siga:selecao propriedade="lotacaoEquipe" tema="simple"
					modulo="siga" urlAcao="buscar" desativar="sim" />
			</div>
		</div>

		<div class="card mb-2">
			<div class="card-header">
				<h3>Horário de Trabalho</h3>
			</div>
			<div class="card card-body"></div>
		</div>

		<div class="card mb-2">
			<div class="card-header">
				<h3>Exceções ao calendário padrão</h3>
			</div>
			<div class="card card-body">
				<div class="table-responsive">
					<table id="excecoes_table" border="0" class="table">
						<thead>
							<tr>
								<th>Dia Semana</th>
								<th>Data (String)</th>
								<th>Dia</th>
								<th>Início Expediente</th>
								<th>Final Expediente</th>
								<th>Início Intervalo</th>
								<th>Final Intervalo</th>
								<th></th>
							</tr>
						</thead>
					</table>
					<div class="gt-table-buttons mt-2">
						<a href="javascript: modalExcecaoAbrir()" class="btn btn-primary"
							style="color: #fff">Incluir</a>
					</div>
				</div>
			</div>
		</div>

		<div class="card mb-2">
			<div class="card-header">
				<h3>Designações</h3>
			</div>
			<div class="card card-body">
				<div class="table-responsive">
					<sigasr:designacao modoExibicao="equipe"
						designacoes="${designacoes}"
						mostrarDesativados="mostrarDesativado"
						unidadesMedida="${unidadesMedida}" orgaos="${orgaos}"
						locais="${locais}" pesquisaSatisfacao="${pesquisaSatisfacao}"
						listasPrioridade="${listasPrioridade}" />
				</div>
			</div>
		</div>
	</form>

	<div class="gt-form-row">
		<input type="button" value="Gravar" class="btn btn-primary"
			onclick="equipeService.gravar()" /> <a class="btn btn-secondary"
			style="color: #fff" onclick="equipeService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary"
			onclick="equipeService.aplicar()" />
	</div>
</div>

<sigasr:modal nome="excecaoHorario"
	titulo="Adicionar Exceção de Horário" largura="80%">
	<div class="container-fluid mb-2">
		<div id="dialogExcecaoHorario">
			<form id="excecaoHorarioForm" method="get" action=""
				enctype="multipart/form-data">

				<div class="card card-body mb-2">

					<div class="form-group">
						<label>Dia da Semana</label> <select name="diaSemana"
							class="form-control">
							<option value=0>Nenhuma
								</optgroup>
								<c:forEach items="${diasSemana}" var="dia">
									<option value="${dia}">${dia.descrDiaSemana}</option>
								</c:forEach>
						</select> <span style="display: none; color: red" id="diaSemanaError">Dia
							da Semana não informado</span>
					</div>

					<div class="form-group">
						<label>Data Específica</label>
						<siga:dataCalendar nome="dataEspecifica" value="${dataEspecifica}"></siga:dataCalendar>
						<span style="display: none; color: red" id="dataEspecificaError">Data
							Específica não informada</span>
					</div>

					<div id="erroHorarioInvalido" style="display: none; width: 300px"
						class="form-group">

						<span style="color: red">O período informado está
							inválido. A data de início deve ser menor que a data de
							término e o período de intervalo deve estar contido no período
							de expediente</span>

					</div>

					<table>
						<tr>
							<td>
								<div class="form-group">
									<label>Início Expediente <span>*</span></label><br /> <input
										type="time" name="horaIni" id="horaIni" value="${horaIni}"
										class="form-control" required>
								</div>
							</td>
							<td>
								<div class="form-group">
									<label>Fim Expediente <span>*</span></label><br /> <input
										type="time" name="horaFim" id="horaFim" value="${horaFim}"
										class="form-control" required>
								</div>
							</td>
						<tr>
							<td>
								<div class="form-group">
									<label>Início Intervalo <span>*</span></label><br /> <input
										type="time" name="interIni" id="interIni" value="${interIni}"
										class="form-control" required>
								</div>
							</td>
							<td>
								<div class="form-group">
									<label>Fim Intervalo <span>*</span></label><br /> <input
										type="time" name="interFim" id="interFim" value="${interFim}"
										class="form-control" required>
								</div>
							</td>
						</tr>
					</table>
					<div class="gt-form-row">
						<a href="javascript: inserirExcecaoHorario()"
							class="btn btn-primary style="color:#fff">Ok</a> <a
							href="javascript: modalExcecaoFechar()" class="btn btn-secondary"
							style="color: #fff">Cancelar</a>
					</div>

				</div>
			</form>
		</div>
	</div>
</sigasr:modal>

<script type="text/javascript">
	var validatorForm, validatorFormExcessao = null;

	jQuery(document).ready(function($) {
		$.validator.addMethod("hora", function(value, element, regexp) {
			var re = new RegExp('^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$');
			return this.optional(element) || re.test(value);
		}, "Hora inválida.");

		validatorForm = jQuery("#form").validate();

		validatorFormExcessao = $("#excecaoHorarioForm").validate({
			onfocusout : false
		});

		$("#dataEspecifica").mask("99/99/9999");
		$(".hora").mask("99:99");
		$("#horaIni").rules("add", {
			hora : ""
		});
		$("#horaFim").rules("add", {
			hora : ""
		});
		$("#interIni").rules("add", {
			hora : ""
		});
		$("#interFim").rules("add", {
			hora : ""
		});
	});

	function modalExcecaoAbrir() {
		// limpa as mensagens de erros
		resetMensagensErro();
		resetCamposTela();

		$("#dataEspecifica").datepicker({
			showOn : "button",
			buttonImage : "/siga/css/famfamfam/icons/calendar.png",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy'
		});

		$("#excecaoHorario_dialog").dialog('open');
	}

	function inserirExcecaoHorario() {
		if (isValidFormExc() && isvalidFormExcecao()) {
			// recupera as comboboxes
			var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0], jDiaSemana = jDiaSemanaCbb.options[jDiaSemanaCbb.selectedIndex], dataSelecionada = $(
					"#dataEspecifica").datepicker("getDate"), diaSemanaValue = '', dataToISOString = '', diaText = '';

			if (document.getElementById("dataEspecifica").value == "") {
				diaSemanaValue = jDiaSemana.value;
				diaText = jDiaSemana.text;
			} else {
				dataToISOString = dataSelecionada.toISOString();
				diaText = document.getElementById("dataEspecifica").value;
			}

			var row = [
					diaSemanaValue,
					dataToISOString,
					diaText,
					document.getElementById("horaIni").value,
					document.getElementById("horaFim").value,
					document.getElementById("interIni").value,
					document.getElementById("interFim").value,
					"<a class=\"excecao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>" ];

			equipeService.excecoesTable.api().row.add(row).draw();
			modalExcecaoFechar();
		}
	}

	function modalExcecaoFechar() {
		$("#excecaoHorario_dialog").dialog('close');
	}

	function isvalidFormExcecao() {
		var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0], isValid = true;

		// limpa as mensagens de erros
		resetMensagensErro();
		esconderValidacaoHorario();

		// Mostra a mensagem de campos obrigatórios
		if (document.getElementById("dataEspecifica").value == ""
				&& jDiaSemanaCbb.selectedIndex == 0) {
			document.getElementById("diaSemanaError").style.display = "inline";
			document.getElementById("dataEspecificaError").style.display = "inline";
			isValid = false;
			alert("Por favor, preencha o Dia da Semana ou a Data Específica.");
		}

		return isValid;
	}

	function resetMensagensErro() {
		validatorFormExcessao.resetForm();
		document.getElementById("diaSemanaError").style.display = "none";
		document.getElementById("dataEspecificaError").style.display = "none";

		$("#horaIni").removeClass("error");
		$("#horaFim").removeClass("error");
		$("#interIni").removeClass("error");
		$("#interFim").removeClass("error");
	}

	function resetCamposTela() {
		var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0];

		jDiaSemanaCbb.selectedIndex = 0;
		document.getElementById("horaIni").value = '';
		document.getElementById("horaFim").value = '';
		document.getElementById("interIni").value = '';
		document.getElementById("interFim").value = '';
		document.getElementById("dataEspecifica").value = '';
	}

	function atualizaData(dataString) {
		return new Date(dataString);
	}

	function atualizaHora(data, horaString) {
		var horaMinuto = horaString.split(":"), hora = horaMinuto[0], minuto = horaMinuto[1];

		data.setHours(hora);
		data.setMinutes(minuto);

		return data;
	}

	function horarioEstaValido() {
		var horaInicio = moment($("#horaIni").val(), 'hh:mm'), horaTermino = moment(
				$("#horaFim").val(), 'hh:mm'), inicioIntervalo = moment($(
				"#interIni").val(), 'hh:mm'), terminoIntervalo = moment($(
				"#interFim").val(), 'hh:mm'), valido = true, mensagem = null;

		if (!estaAntes(horaInicio, horaTermino)) {
			mensagem = "A hora de início de expediente deve ser menor que a de término."
			valido = false;
		} else if (!estaAntes(inicioIntervalo, terminoIntervalo)) {
			mensagem = "A hora de início do intervalo deve ser menor que a de término."
			valido = false;
		} else if (!estaNoIntervalo(inicioIntervalo, horaInicio, horaTermino)) {
			mensagem = "A hora de início do intervalo está fora do periodo do expediente."
			valido = false;
		} else if (!estaNoIntervalo(terminoIntervalo, horaInicio, horaTermino)) {
			mensagem = "A hora de término do intervalo está fora do periodo do expediente."
			valido = false;
		}
		mostrarValidacaoHorario(mensagem);
		return valido;
	}

	function estaAntes(inicio, fim) {
		return inicio.isBefore(fim)
	}

	function estaNoIntervalo(data, inicio, fim) {
		return data.isSame(inicio) || data.isSame(fim)
				|| (data.isAfter(inicio) && data.isBefore(fim));
	}

	function mostrarValidacaoHorario(mensagem) {
		if (mensagem) {
			$('#erroHorarioInvalido').css('display', 'block');
			$('#erroHorarioInvalido span').html(mensagem);
		}
	}

	function esconderValidacaoHorario() {
		$('#erroHorarioInvalido').css('display', 'none');
	}

	function isValidFormExc() {
		if (jQuery("#excecaoHorarioForm").valid()) {
			return horarioEstaValido();
		} else
			return false;
	}
</script>