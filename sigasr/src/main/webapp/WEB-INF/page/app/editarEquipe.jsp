#<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Cadastro de Equipe">

<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="/sigasr/public/javascripts/detalhe-tabela.js"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/sigasr/public/javascripts/jquery.maskedinput.min.js"></script>
<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>

<style>
.barra-subtitulo-top {
	border-radius: 5px 5px 0 0;
	margin: -16px 0 0 -16px;
}

.barra-subtitulo {
	color: #365b6d !important;
	border-bottom: 1px solid #ccc;
	border-radius: 0 !important;
	margin: 0 -15px 10px -15px;
}

.gt-content-box {
	padding-top: 0px !important;
}
</style>
<script type="text/javascript">

	$(document).ready(function() {
		/* Table initialization */
		excecoesTable = $('#excecoes_table').dataTable({
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
			"columnDefs": [{"targets": [0, 1], "visible": false, "searchable": false}],
			"iDisplayLength": 25
		});
		
		// Remover exceções
		$('#excecoes_table tbody').on('click" 'a.excecao_remove" function () {
			excecoesTable.api().row($(this).closest('tr')).remove().draw(false);
		});
		
		$("#dataEspecifica").mask("99/99/9999");
		$("#horaIni").mask("99:99");
		$("#horaFim").mask("99:99");
		$("#interIni").mask("99:99");
		$("#interFim").mask("99:99");
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
		if (!camposObrigatoriosExcecaoPreenchidos()) {
			// recupera as comboboxes
			var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0],
				jDiaSemana = jDiaSemanaCbb.options[jDiaSemanaCbb.selectedIndex],
				dataSelecionada = $("#dataEspecifica").datepicker("getDate"),
				diaSemanaValue = '"
				dataToISOString = '"
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
			
			excecoesTable.api().row.add(row).draw();
			modalExcecaoFechar();
		}
	}
	
	function modalExcecaoFechar() {
		$("#excecaoHorario_dialog").dialog('close');
	}
	
	function camposObrigatoriosExcecaoPreenchidos() {
		var jDiaSemanaCbb = document.getElementsByName("diaSemana")[0],
			hasErros = false;
		
		// limpa as mensagens de erros
		resetMensagensErro();
		
		// Mostra a mensagem de campos obrigatórios
		if (document.getElementById("dataEspecifica").value == "" && jDiaSemanaCbb.selectedIndex == 0) {
			document.getElementById("diaSemanaError").style.display = "inline";
			document.getElementById("dataEspecificaError").style.display = "inline";
			hasErros = true;
		}
		
		if (document.getElementById("horaIni").value == "") {
			document.getElementById("horaIniError").style.display = "inline";
			hasErros = true;
		}
		
		if (document.getElementById("horaFim").value == "") {
			document.getElementById("horaFimError").style.display = "inline";
			hasErros = true;
		}
		
		if (document.getElementById("interIni").value == "") {
			document.getElementById("interIniError").style.display = "inline";
			hasErros = true;
		}
		
		if (document.getElementById("interFim").value == "") {
			document.getElementById("interFimError").style.display = "inline";
			hasErros = true;
		}
		
		return hasErros;
	}
	
	function resetMensagensErro() {
		document.getElementById("diaSemanaError").style.display = "none";
		document.getElementById("dataEspecificaError").style.display = "none";
		document.getElementById("horaIniError").style.display = "none";
		document.getElementById("horaFimError").style.display = "none";
		document.getElementById("interIniError").style.display = "none";
		document.getElementById("interFimError").style.display = "none";
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
	
	function inserirEquipe() {
		if (block())
			$.ajax({
	        	type: "POST",
        		url: "@{Application.gravarEquipe()}",
        		data: serializeEquipe(),
        		dataType: "text",
        		success: function() {
	        		location.href = "@{Application.listarEquipe()}";
        		}
       		});
	}
	
	function serializeEquipe() {
		var jForm = $("form"),
			params = jForm.serialize(),
			idEquipe = $("#idEquipe").val();
		
		params += getExcecoesTableAsString(idEquipe);
		
		return params;
	}
	
	function getExcecoesTableAsString(idEquipe) {
		var params = '"
			dataAux = new Date();
		
		// Percorre lista de Exceções
		excecoesTable.api().rows().indexes().each(function (i) {
			var rowValues = excecoesTable.api().row(i).data();
			
			// Atualiza a string serializada
			if (rowValues) {
				if (rowValues[0] == 0)
					params += '&equipe.excecaoHorarioSet[' + i + '].dataEspecifica=' + rowValues[1];
				else
					params += '&equipe.excecaoHorarioSet[' + i + '].diaSemana=' + rowValues[0];
				
				params += '&equipe.excecaoHorarioSet[' + i + '].strHoraIni=' + atualizaHora(dataAux, rowValues[3]).toJSON();
				params += '&equipe.excecaoHorarioSet[' + i + '].strHoraFim=' + atualizaHora(dataAux, rowValues[4]).toJSON();
				params += '&equipe.excecaoHorarioSet[' + i + '].strInterIni=' + atualizaHora(dataAux, rowValues[5]).toJSON();
				params += '&equipe.excecaoHorarioSet[' + i + '].strInterFim=' + atualizaHora(dataAux, rowValues[6]).toJSON();
				
				//params += '&equipe.excecaoHorarioSet[' + i + '].equipe.idEquipe=' + idEquipe;
			}
		});
		
		return params;
	}
	
	function atualizaHora(data, horaString) {
		var horaMinuto = horaString.split(":"),
			hora = horaMinuto[0],
			minuto = horaMinuto[1];
		
		data.setHours(hora);
		data.setMinutes(minuto);
		
		return data;
	}
</script>

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Cadastro de Equipe</h2>

		<div class="gt-form gt-content-box">
			<form id="form" class="formEditarEquipe" enctype="multipart/form-data">
				<input type="hidden" id="idEquipe" name="equipe.idEquipe" value="${equipe.idEquipe}">
				<input type="hidden" id="idEquipeIni" name="equipe.hisIdIni" value="${equipe.hisIdIni}">
				<c:if test="${not empty errors}">
				<p class="gt-error">Alguns campos obrigatórios não foram
					preenchidos ${error}</p>
				</c:if>
				<div class="gt-form-table">
					<div class="barra-subtitulo barra-subtitulo-top header"
						align="center" valign="top">Dados básicos</div>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Lotação</label><sigasr:selecao
						tipo="lotacao" nome="equipe.lotacao"
						value:${equipe.lotacao?.lotacaoAtual}" disabled="true" />
					<span style="display:none;color: red" id="equipe.lotacao">Lotação não informada.</span>
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
							
							<tbody>
								<c:forEach items="${equipe.excecaoHorarioSet}" var="excecao">
								<tr>
									<c:choose>
									<c:when test="${excecao?.diaSemana}">
										<td>${excecao?.diaSemana }</td>
										<td></td>
										<td>${excecao?.diaSemana.descrDiaSemana }</td>
									</c:when>
									<c:otherwise>
										<td>0</td>
										<td>${excecao?.dataEspecifica.format('yyyy-MM-dd HH:mm:ss.SSSZ') }</td>
										<td>${excecao?.dataEspecifica.format('dd/MM/yyyy') }</td>
									</c:otherwise>
									</c:if>
									
									<td>${excecao?.horaIni.format('HH:mm') }</td>
									<td>${excecao?.horaFim.format('HH:mm') }</td>
									<td>${excecao?.interIni.format('HH:mm') }</td>
									<td>${excecao?.interFim.format('HH:mm') }</td>
									<td><a class="excecao_remove"><img src="/siga/css/famfamfam/icons/delete.png" style="visibility: inline; cursor: pointer" /></a></td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="gt-table-buttons">
						<a href="javascript: modalExcecaoAbrir()" class="gt-btn-medium gt-btn-left">Incluir</a>
					</div>
				</div>
				
				<div class="gt-form-table">
					<div class="barra-subtitulo barra-subtitulo-top header"
						align="center" valign="top">Designações</div>
				</div>
				<div class="gt-form-row">
					<sigasr:designacoes nome="designacoes:designacoesEquipe" 
						modoExibicao="equipe" 
						orgaos="${orgaos}" 
						locais="${locais}" 
						unidadesMedida="${unidadesMedida}" 
						pesquisaSatisfacao="${pesquisaSatisfacao}" 
						listasPrioridade="${listasPrioridade}" />
				</div>
				<div class="gt-form-row">
					<a href="javascript: inserirEquipe()" class="gt-btn-medium gt-btn-left">Gravar</a>
					<a href="@{Application.listarEquipe}" class="gt-btn-medium gt-btn-left">Cancelar</a>
				</div>
			</form>
		</div>
	</div>
</div>

<sigasr:modal nome="excecaoHorario" titulo="Adicionar Exceção de Horário">
	<div id="dialogExcecaoHorario">
		<div class="gt-content">
			<div class="gt-form gt-content-box">
				<div class="gt-form-row gt-width-100">
					<label>Dia da Semana</label> 
					<sigasr:select name="diaSemana" 
						items="${SrSemana.values()" 
						valueProperty="idSemana"
						labelProperty="descrDiaSemana" 
						value="${diaSemana?.idSemana}"
						class="select-siga"
						style="width: 100%;">
						<sigasr:opcao valor="0">Nenhuma</sigasr:opcao> 
					</sigasr:select>
					<span style="display:none;color: red" id="diaSemanaError">Dia da Semana não informado</span>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Data Específica</label>
					<input type="text" name="calendario" id="dataEspecifica" value="${dataEspecifica}">
					<span style="display:none;color: red" id="dataEspecificaError">Data Específica não informada</span>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Início Expediente</label>
					<input type="text" name="horaIni" id="horaIni" value="${horaIni}">
					<span style="display:none;color: red" id="horaIniError">Início Expediente não informado</span>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Fim Expediente</label>
					<input type="text" name="horaFim" id="horaFim" value="${horaFim}">
					<span style="display:none;color: red" id="horaFimError">Fim Expediente não informado</span>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Início Intervalo</label>
					<input type="text" name="interIni" id="interIni" value="${interIni}">
					<span style="display:none;color: red" id="interIniError">Início Intervalo não informado</span>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Fim Intervalo</label>
					<input type="text" name="interFim" id="interFim" value="${interFim}">
					<span style="display:none;color: red" id="interFimError">Fim Intervalo não informado</span>
				</div>
				
				<div class="gt-form-row">
					<a href="javascript: inserirExcecaoHorario()" class="gt-btn-medium gt-btn-left">Ok</a>
					<a href="javascript: modalExcecaoFechar()" class="gt-btn-medium gt-btn-left">Cancelar</a>
				</div>
				
			</div>
		</div>
	</div>
</sigasr:modal>