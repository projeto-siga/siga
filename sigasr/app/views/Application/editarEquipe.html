#{extends 'main.html' /} #{set title:'Edição de Equipes' /}

<style>
.inline {
	display: inline-flex !important;
}

.tabela {
	margin-top: -10px;
	min-width: 200px;
}

.tabela tr {
 	border: solid;
 	border-color: rgb(169, 169, 169);
 	border-width: 1px;
 	font-weight: bold;
	line-height: 20px;
}

.tabela th {
 	color: #365b6d;
 	font-size: 100%;
 	padding: 5px 10px;
 	border: solid 1px rgb(169, 169, 169);
}

.tabela td {
	color: #000;
 	padding-right: 10px !important;
}

.gt-form-table td {
	padding-right: 0px;
}

.barra-subtitulo-top {
	border-radius: 5px 5px 0 0;
}

.barra-subtitulo {
	background-color: #eee;
	padding: 10px 15px;
	border: 1px solid #ccc;
	color: #365b6d !important;
	border-bottom: 1px solid #ccc;
	border-radius: 5px !important;
	margin: 0 -16px 10px -16px;
}

.div-editar-equipe {
	width: 800px !important;
	max-width: 800px !important;
}

.gt-content-box {
	padding-top: 0px !important;
}
</style>

		<div class="gt-form gt-content-box div-editar-equipe">
			<form id="form" class="formEditarEquipe" enctype="multipart/form-data">
				<input type="hidden" id="idEquipe" name="idEquipe">
				<input type="hidden" id="idEquipeIni" name="hisIdIni">
				<input type="text" style="visibility: hidden;" id="lotacaoUsuario" name="lotacaoUsuario" value="${lotacaoUsuario.toJson()}"/>
				<p class="gt-error" style="display:none;" id="erroEquipeCamposObrigatorios">Alguns campos obrigatórios não foram
					preenchidos</p>
				<div class="gt-form-table">
					<div class="barra-subtitulo barra-subtitulo-top header"
						align="center" valign="top">Dados básicos</div>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Lotação</label>#{selecao
						tipo:'lotacaoEquipe', nome:'lotacaoEquipe',
						value:lotacaoUsuario,
						disabled:'true' /}
				</div>
				
				<div class="gt-form-table">
					<div class="barra-subtitulo barra-subtitulo-top header"
						align="center" valign="top">Horário de Trabalho</div>
				</div>
				
				<div class="gt-form-row">
					<h2>Exceções ao calendário padrão</h2>
					<!-- content bomex -->
					<div class="gt-content-box dataTables_div">
						<table id="excecoes_table" border="0" class="gt-table display">
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
					</div>
					<div class="gt-form-row">
						<a href="javascript: modalExcecaoAbrir()" class="gt-btn-medium gt-btn-left">Incluir</a>
					</div>
				</div>
				
				<div class="gt-form-table">
					<div class="barra-subtitulo barra-subtitulo-top header"
						align="center" valign="top">Designações</div>
				</div>
			</form>
			
			<div class="gt-form-row">
			
				#{designacoes modoExibicao:'equipe', 
					orgaos:orgaos, 
					locais:locais, 
					unidadesMedida:unidadesMedida, 
					pesquisaSatisfacao:pesquisaSatisfacao, 
					listasPrioridade:listasPrioridade /}
			
			</div>
			<div class="gt-form-row">
				<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="equipeService.gravar()"/>
				<a class="gt-btn-medium gt-btn-left" onclick="equipeService.cancelarGravacao()">Cancelar</a>
				<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="equipeService.aplicar()"/>
			</div>
		</div>

#{modal nome:'excecaoHorario', titulo:'Adicionar Exceção de Horário'}
	<div id="dialogExcecaoHorario">
		<div class="gt-content">
			<form id="excecaoHorarioForm" method="get" action="" enctype="multipart/form-data">
				<div class="gt-form gt-content-box">
					<div class="gt-form-row gt-width-100">
						<label>Dia da Semana</label> 
						#{select name:'diaSemana', 
							items:models.SrSemana.values(), 
							valueProperty:'idSemana',
							labelProperty:'descrDiaSemana', 
							value:diaSemana?.idSemana,
							class:'select-siga',
							style:'width: 100%;'}
							#{option 0}Nenhuma#{/option} 
						#{/select}
						<span style="display:none;color: red" id="diaSemanaError">Dia da Semana não informado</span>
					</div>
					<div class="gt-form-row gt-width-100">
						<label>Data Específica</label>
						#{dataCalendar nome:'dataEspecifica', value:dataEspecifica /}
						<span style="display:none;color: red" id="dataEspecificaError">Data Específica não informada</span>
					</div>
					
					<div id="erroHorarioInvalido" style="display:none;width:300px" class="gt-form-row gt-width-100">
						<span style="color: red">O período informado está inválido. A data de início deve ser menor que a data de término e o período de intervalo deve estar contido no período de expediente</span>
					</div>
					
					<div class="gt-form-row gt-width-100">
						<label>Início Expediente <span>*</span></label>
						<input type="text" name="horaIni" id="horaIni" value="${horaIni}" class="hora" required>
					</div>
					<div class="gt-form-row gt-width-100">
						<label>Fim Expediente <span>*</span></label>
						<input type="text" name="horaFim" id="horaFim" value="${horaFim}" class="hora" required>
					</div>
					<div class="gt-form-row gt-width-100">
						<label>Início Intervalo <span>*</span></label>
						<input type="text" name="interIni" id="interIni" value="${interIni}" class="hora" required>
					</div>
					<div class="gt-form-row gt-width-100">
						<label>Fim Intervalo <span>*</span></label>
						<input type="text" name="interFim" id="interFim" value="${interFim}" class="hora" required>
					</div>
					
					<div class="gt-form-row">
						<a href="javascript: inserirExcecaoHorario()" class="gt-btn-medium gt-btn-left">Ok</a>
						<a href="javascript: modalExcecaoFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
					</div>
				</div>
			</form>
		</div>
	</div>
#{/modal}

<script type="text/javascript">
	var validatorForm,
		validatorFormExcessao = null;
	
	jQuery(document).ready(function($) {
		$.validator.addMethod(
		        "hora",
		        function(value, element, regexp) {
		            var re = new RegExp('^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$');
		            return this.optional(element) || re.test(value);
		        },
		        "Hora inválida."
		);

		validatorForm = jQuery("#form").validate();
		
		validatorFormExcessao = $("#excecaoHorarioForm").validate({
			onfocusout: false
		});
		
		$("#dataEspecifica").mask("99/99/9999");
		$(".hora").mask("99:99");
		$("#horaIni").rules("add", {hora: ""});
		$("#horaFim").rules("add", {hora: ""});
		$("#interIni").rules("add", {hora: ""});
		$("#interFim").rules("add", {hora: ""});
	});
	
	function modalExcecaoAbrir() {
		// limpa as mensagens de erros
		resetMensagensErro();
		resetCamposTela();
		
		$("#dataEspecifica").datepicker({
		    showOn: "button",
		    buttonImage: "/siga/css/famfamfam/icons/calendar.png",
		    buttonImageOnly: true,
		    dateFormat: 'dd/mm/yy'
		});
		
		$("#excecaoHorario_dialog").dialog('open');
	}
	
	function inserirExcecaoHorario() {
		if (isValidFormExc() && isvalidFormExcecao()) {
			// recupera as comboboxes
			var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0],
				jDiaSemana = jDiaSemanaCbb.options[jDiaSemanaCbb.selectedIndex],
				dataSelecionada = $("#dataEspecifica").datepicker("getDate"),
				diaSemanaValue = '',
				dataToISOString = '',
				diaText = '';
			
			if (document.getElementById("dataEspecifica").value == "") {
				diaSemanaValue = jDiaSemana.value;
				diaText = jDiaSemana.text;
			}
			else {
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
			           "<a class=\"excecao_remove\"><img src=\"/siga/css/famfamfam/icons/delete.png\" style=\"visibility: inline; cursor: pointer\" /></a>"];
			
			equipeService.excecoesTable.api().row.add(row).draw();
			modalExcecaoFechar();
		}
	}
	
	function modalExcecaoFechar() {
		$("#excecaoHorario_dialog").dialog('close');
	}
	
	function isvalidFormExcecao() {
		var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0],
			isValid = true;
		
		// limpa as mensagens de erros
		resetMensagensErro();
		esconderValidacaoHorario();
		
		// Mostra a mensagem de campos obrigatórios
		if (document.getElementById("dataEspecifica").value == "" && jDiaSemanaCbb.selectedIndex == 0) {
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
	
	function atualizaHora(data, horaString) {
		var horaMinuto = horaString.split(":"),
			hora = horaMinuto[0],
			minuto = horaMinuto[1];
		
		data.setHours(hora);
		data.setMinutes(minuto);
		
		return data;
	}

	function horarioEstaValido() {
		var horaInicio = moment($("#horaIni").val(), 'hh:mm'),
		  horaTermino = moment($("#horaFim").val(), 'hh:mm'),
		  inicioIntervalo = moment($("#interIni").val(), 'hh:mm'),
		  terminoIntervalo = moment($("#interFim").val(), 'hh:mm'),
		  valido = true,
		  mensagem = null;
		  
		if(!estaAntes(horaInicio, horaTermino)) {
			mensagem = "A hora de início de expediente deve ser menor que a de témino"
			valido = false;
		}
		else if(!estaAntes(inicioIntervalo, terminoIntervalo)) {
			mensagem = "A hora de início do intervalo deve ser menor que a de término"
			valido = false;
		}
		else if(!estaNoIntervalo(inicioIntervalo, horaInicio, horaTermino)) {
			mensagem = "A hora de início do intervalo está fora do periodo do expediente"
			valido = false;
		} 
		else if(!estaNoIntervalo(terminoIntervalo, horaInicio, horaTermino)) {
			mensagem = "A hora de término do intervalo está fora do periodo do expediente"
			valido = false;
		}
		mostrarValidacaoHorario(mensagem);
		return valido;
	}
		
	function estaAntes(inicio, fim) {
		return inicio.isBefore(fim)
	}

	function estaNoIntervalo(data, inicio, fim) {
		return data.isSame(inicio)
		    || data.isSame(fim)
		    || (data.isAfter(inicio) && data.isBefore(fim));
	}

	function mostrarValidacaoHorario(mensagem) {
		if(mensagem) {
			$('#erroHorarioInvalido').css('display', 'block');
			$('#erroHorarioInvalido span').html(mensagem);
		}
	}

	function esconderValidacaoHorario() {
		$('#erroHorarioInvalido').css('display', 'none');
	}
	
	function isValidFormExc() {
		if(jQuery("#excecaoHorarioForm").valid()) {
			return horarioEstaValido();
		}
		else
			return false;
	}
</script>