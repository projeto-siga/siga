/**
 * 
 */

function displayPersonalizacao(thisElement) {
	var thatElement = document.getElementById('tr_personalizacao');
	if (thisElement.checked)
		thatElement.style.display = '';
	else {
		thatElement.style.display = 'none';
		document.getElementById('personalizarFuncao').value = '';
		document.getElementById('personalizarUnidade').value = '';
	}
}

function personalizacaoSeparar() {
	var a = document.getElementById('frm_nmFuncaoSubscritor').value.split(';');
	document.getElementById('personalizarFuncao').value = a.length > 0 ? a[0] : '';
	document.getElementById('personalizarUnidade').value = a.length > 1 ? a[1] : '';
	document.getElementById('personalizarLocalidade').value = a.length > 2 ? a[2] : '';
	document.getElementById('personalizarNome').value = a.length > 3 ? a[3] : '';
}

function personalizacaoJuntar() {
	var f = document.getElementById('personalizarFuncao').value.trim();
	var u = document.getElementById('personalizarUnidade').value.trim();
	var l = document.getElementById('personalizarLocalidade').value.trim();
	var n = document.getElementById('personalizarNome').value.trim();
	var j = f + ';' + u + ';' + l + ';' + n;
	while (j.slice(-1) == ';')
		j = j.substring(0, j.length - 1);
	document.getElementById('frm_nmFuncaoSubscritor').value = j;
}

// <c:set var="url" value="editar" />
function sbmt(id) {
	var frm = document.getElementById('frm');
	
	var mod = document.getElementsByName('exDocumentoDTO.idMod')[0];
	if (mod.value == '[Selecione]')
		mod.value = '0';
	
	// Dispara a função onSave() do editor, caso exista
	if (typeof (onSave) == "function") {
		onSave();
	}
	if (id && !IsRunningAjaxRequest()) {
		ReplaceInnerHTMLFromAjaxResponse('recarregar', frm, id);
	} else {
		frm.action = 'recarregar';
		frm.submit();
	}
	return;

	if (typeof (frm.submitsave) == "undefined")
		frm.submitsave = frm.submit;
	frm.action = 'recarregar';

	if (id == null || typeof (id) == 'undefined' || IsRunningAjaxRequest()) {
		window.customOnsubmit = function() {
			return true;
		};
		frm.onsubmit = null;
		frm.submit = frm.submitsave;
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('recarregar', frm, id);
	}
}

// <c:set var="url" value="gravar" />
function gravarDoc() {
	clearTimeout(saveTimer);
	if (!validar(false)) {
		triggerAutoSave();
		return false;
	}
	frm.action = 'gravar';
	window.customOnsubmit = function() {
		return true;
	};
	if (typeof (frm.submitsave) != "undefined")
		frm.submit = frm.submitsave;

	// Dispara a função onSave() do editor, caso exista
	if (typeof (onSave) == "function")
		onSave();

	document.getElementById("btnGravar").disabled = true;
	frm.submit();
}

function validar(silencioso) {
	personalizacaoJuntar();
	
	var descr = document.getElementsByName('exDocumentoDTO.descrDocumento')[0].value;
	var eletroHidden = document.getElementById('eletronicoHidden');
	var eletro1 = document.getElementById('eletronicoCheck1');
	var eletro2 = document.getElementById('eletronicoCheck2');
	var subscritor = document.getElementById('formulario_exDocumentoDTO.subscritorSel_id');
	var temCossignatarios = document.getElementById('temCossignatarios');
	var descricaoAutomatica = document.getElementById('descricaoAutomatica');
	if (descricaoAutomatica == null && (descr == null || descr == "")) {
		aviso("Preencha o campo Descrição antes de gravar o documento.",
				silencioso);
		return false;
	}
	if ((temCossignatarios && temCossignatarios.value === 'true') && (!subscritor || !subscritor.value)) {
		aviso("É necessário informar um subscritor, pois o documento possui cossignatários",silencioso);
		return false;
	}
	if (eletroHidden == null && !eletro1.checked && !eletro2.checked) {
		aviso(
				"É necessário informar se o documento será digital ou físico, na parte superior da tela.",
				silencioso);
		return false;
	}

	var limite = document
			.getElementsByName('exDocumentoDTO.tamanhoMaximoDescricao')[0].value;
	if (document.getElementsByName('exDocumentoDTO.descrDocumento')[0].value.length >= limite) {
		aviso('O tamanho máximo da descrição é de ' + limite + ' caracteres',
				silencioso);
		return false;
	}

	if (document.getElementById('frm_nmFuncaoSubscritor').value.length > 128) {
		aviso('O tamanho máximo da soma dos caracteres de personalização é de 128 caracteres',
				silencioso);
		return false;
	}

	// Impede a gravação de um documento que possui campos obrigatorios quando
	// esses não forem informados
	var frm = document.getElementById('frm');
	var obrigatorios = frm.elements["obrigatorios"];
	if (typeof (obrigatorios) != "undefined") {
		if (typeof (obrigatorios.length) != "number") {
			obrigatorios = [ obrigatorios ];
		}
		var s = "";
		for (var i = 0; i < obrigatorios.length; i++) {
			var elm = frm.elements[obrigatorios[i].value];
			var obr = elm.value;
			if (obr == null || obr == "" || !obr || /^\s*$/.test(obr)) {
				aviso("Parâmetro obrigatório não foi informado: "
						+ obrigatorios[i].value, silencioso);
				elm.focus();
				return false;
			}
		}
	}

	// Impede que um documento capturado seja gravado sem que seja informado o
	// arquivo PDF
	var origem = document.getElementsByName('exDocumentoDTO.idTpDoc')[0].value;
	var id = document.getElementsByName('exDocumentoDTO.id')[0].value;
	if (origem == 4 && !id) {
		var arquivo = document.getElementsByName('arquivo')[0];
		if (arquivo.value == "") {
			aviso('('+document.getElementById('codigoDoc').innerHTML + ') ' +
					'Documento capturado não pode ser gravado sem que seja informado o nome do arquivo PDF',
					silencioso);
			arquivo.focus();
			return false;
		}
	}
	return true;
}

function aviso(msg, silencioso) {
	document.getElementById("btnGravar").disabled = false;
	
	if (silencioso)
		avisoVermelho('O documento não pôde ser salvo: ' + msg);
	else
		alert(msg);
}

// <c:set var="url" value="excluirpreench" />
function removePreench() {
	// Dispara a função onSave() do editor, caso exista
	if (typeof (onSave) == "function") {
		onSave();
	}
	frm.action = 'excluirpreench';
	frm.submit();
}

// <c:set var="url" value="alterarpreench" />
function alteraPreench() {
	// Dispara a função onSave() do editor, caso exista
	if (typeof (onSave) == "function") {
		onSave();
	}
	frm.action = 'alterarpreench';
	frm.submit();
}

// <c:set var="url" value="carregarpreench" />
function carregaPreench() {
	if (document.getElementById('preenchimento').value == 0) {
		frm.btnRemover.disabled = "true";
		frm.btnAlterar.disabled = "true";
	} else {
		// Dispara a função onSave() do editor, caso exista
		if (typeof (onSave) == "function") {
			onSave();
		}
		frm.btnAlterar.disabled = "true";
		frm.btnRemover.disabled = "false";
		frm.action = 'carregarpreench';
		frm.submit();
	}
}

// <c:set var="url" value="gravarpreench" />
function adicionaPreench() {
	var result = '';
	while ((result == '') && (result != null)) {
		result = prompt(
				'Digite o nome do padrão de preenchimento a ser criado para esse modelo:',
				'');
		if (result == '')
			alert('O nome do padrão de preenchimento não pode ser vazio');
		else if (result != null) {
			// Dispara a função onSave() do editor, caso exista
			if (typeof (onSave) == "function") {
				onSave();
			}
			document.getElementsByName('exDocumentoDTO.nomePreenchimento')[0].value = result;
			frm.action = 'gravarpreench';
			frm.submit();
		}
	}

}

// <c:set var="urlPdf" value="preverPdf" />
// <c:set var="url" value="prever" />
var newwindow = '';
function popitup_documento(pdf) {
	personalizacaoJuntar();
	
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width=' + popW + ',height=' + popH + ',left=' + winleft
				+ ',top=' + winUp + ',scrollbars=yes,resizable'
		newwindow = window.open('', '', winProp);
		newwindow.name = 'doc';
	}

	newwindow.opener = self;
	t = frm.target;
	a = frm.action;
	frm.target = newwindow.name;
	if (pdf)
		frm.action = 'preverPdf';
	else
		frm.action = 'prever';

	// Dispara a função onSave() do editor, caso exista
	if (typeof (onSave) == "function") {
		onSave();
	}
	frm.submit();
	frm.target = t;
	frm.action = a;

	if (window.focus) {
		newwindow.focus()
	}
	return false;
}

function checkBoxMsg() {
	window
			.alert('Atenção: essa opção só deve ser selecionada quando o subscritor possui certificado digital, pois será exigida a assinatura digital do documento.');
}

var saveTimer;
function triggerAutoSave() {
	clearTimeout(saveTimer);
	saveTimer = setTimeout('autoSave()', 60000 * 2);
}

triggerAutoSave();

var stillSaving = false;
// <c:set var="url" value="gravar" />
function autoSave() {
	if (stillSaving)
		return;
	if (!validar(true))
		return tryAgainAutoSave();
	for (instance in CKEDITOR.instances)
		CKEDITOR.instances[instance].updateElement();
	if (typeof (onSave) == "function")
		onSave();
	stillSaving = true;
	$.ajax({
		type : "POST",
		url : "gravar?ajax=true",
		data : $(frm).serialize(),
		timeout : 30000,
		success : doneAutoSave,
		error : failAutoSave
	});
}

function doneAutoSave(response) {
	var data = response.split('_');
	if (data[0] == 'OK') {
		avisoVerde('Documento ' + data[1] + ' salvo');
		document.getElementById('codigoDoc').innerHTML = data[1];
		document.getElementById('sigla').value = data[1];
		stillSaving = false;
		triggerAutoSave();
	} else
		failAutoSave();
}

function failAutoSave(response) {
	tryAgainAutoSave();
	avisoVermelho('Atenção: Ocorreu um erro ao salvar o documento.');
	stillSaving = false;
}

function tryAgainAutoSave() {
	clearTimeout(saveTimer);
	saveTimer = setTimeout('autoSave()', 60000 * 2);
}

function parte_bloquear(partes, p, blocked) {
	partes[p].blocked = blocked;
	for (var i = 0; i < partes[p].adeps.length; i++) {
		var d = partes[p].adeps[i];
		if (!partes[d].blocked) {
			console.log(partes[d].id + " bloqueada por :" + partes[p].id);
			parte_bloquear(partes, d, true);
		}
	}
}

function parte_atualizar(titular, lotaTitular) {
	// prepara um vetor com a informação sobre as partes
	var partes = {};
	$(".parte_dependentes").each(function(index) {
		var info = $(this).val().split(":");
		var id = info[0];
		var adeps = info[1].split(";");
		for (var i = 0; i < adeps.length; i++) {
			adeps[i] = adeps[i].trim();
			if (adeps[i] == "") {
				adeps.splice(i, 1);
				break;
			}
		}
		partes[id] = {
			id : id,
			adeps : adeps,
			block : info[2] == "true",
			resp : ';' + info[3] + ';',
			mensagem : $('#parte_mensagem_' + id).val(),
			checked : $('#parte_chk_' + id).is(":checked"),
			locked : false, // modificações proibidas pois está marcado como já
			// preenchido
			disabled : false, // disabilitado porque depende de algum que
			// ainda não foi preenchido ou não é da alçada
			// do responsável
			blocked : false, // bloqueado por haver um dependente que força o
			// bloquei dos predecessores
			active : false
		};
		// console.log('checked: ' + id + ' - ' + partes[id].checked);
	});

	// para cada parte
	for (p in partes) {
		var id = partes[p].id;

		// verifica o responsavel
		if (partes[p].resp.indexOf(';' + titular + ';') == -1
				&& partes[p].resp.indexOf(';' + lotaTitular + ';') == -1) {
			console.log(id + " desabilitado por ser outro responsável ("
					+ partes[p].resp + ")" + " titular: " + titular
					+ " lotaTitular: " + lotaTitular);
			partes[p].disabled = true;
		}
		if (!partes[p].disabled && partes[p].checked) {
			console.log(id + " trancado por estar preenchido");
			partes[p].locked = true;
		}

		// para cada dependencia
		for (var i = 0; i < partes[p].adeps.length; i++) {
			var d = partes[p].adeps[i];
			if (!partes[d].checked) {
				if (partes[p].checked) {
					console.log(id + " limpo por ser dependente de :" + d);
					partes[p].checked = false;
					document.getElementById(id).value = 'Nao';
				}
				if (!partes[p].disabled) {
					console.log(id + " desabilitado por ser dependente de :"
							+ d);
					partes[p].disabled = true;
				}
			}
		}

		// bloquear
		//
		if (partes[p].block && partes[p].checked)
			parte_bloquear(partes, p, false);

		// remover mensagem
		if (partes[p].checked)
			partes[p].mensagem = "";
	}

	for (id in partes) {
		$('#parte_chk_' + id).prop('checked', partes[id].checked);
		$('#' + id).val(partes[id].checked ? 'Sim' : 'Nao');
		if (partes[id].locked || partes[id].disabled || partes[id].blocked)
			$('#parte_fieldset_' + id).attr('disabled', true);
		else
			$('#parte_fieldset_' + id).removeAttr('disabled');
		console.log("*** " + id + ": locked(" + partes[id].locked
				+ ") disabled(" + partes[id].disabled + ") blocked("
				+ partes[id].blocked + ") fieldset(" + partes[id].locked
				|| partes[id].disabled || partes[id].blocked + ")");
		$('#parte_chk_' + id).prop('disabled',
				partes[id].disabled || partes[id].blocked);
		$('#parte_mensagem_' + id).val(partes[id].mensagem);
		$('#parte_div_mensagem_' + id).html(partes[id].mensagem);
	}
}

function parte_solicitar_alteracao(id, titular, lotaTitular) {

	// <form id="form-solicitar-alteracao" method="post"
	// action="/sigaex/app/expediente/mov/assinar_senha_gravar" >
	// <input type="hidden" id="sigla" name="sigla" value="${sigla}" />
	// <fieldset>
	// <label>Matrícula</label> <br/>
	// <input id="nomeUsuarioSubscritor" type="text"
	// name="nomeUsuarioSubscritor" class="text ui-widget-content ui-corner-all"
	// onblur="javascript:converteUsuario(this)"/><br/><br/>
	// <label>Senha</label> <br/>
	// <input type="password" name="senhaUsuarioSubscritor" class="text
	// ui-widget-content ui-corner-all" autocomplete="off"/>
	// </fieldset>
	// </form>
	this.dialogo = $(
			'<div id="dialog-sa" title="Solicitar Alteração"><form id=\"form-solicitar-alteracao\"><fieldset><label>Motivo<br/><input type=\"text\" id=\"motivo\" class=\"text ui-widget-content ui-corner-all\" style="width:100%"/></label></form></div>')
			.dialog(
					{
						title : "Solicitar Alteração",
						width : '30%',
						height : 'auto',
						resizable : false,
						autoOpen : true,
						position : {
							my : "center top+50%",
							at : "center top",
							of : window
						},
						modal : true,
						closeText : "hide",
						buttons : {
							"Prosseguir" : function() {
								$(this).dialog("close");
								$('#parte_chk_' + id).prop('checked', false);
								$('#parte_mensagem_' + id).val(
										$('#motivo').val());
								$('#parte_div_mensagem_' + id).html(
										$('#motivo').val());
								parte_atualizar(titular, lotaTitular);
								$(this).dialog('destroy');
							},
							"Cancelar" : function() {
								$(this).dialog("close");
								$(this).dialog('destroy');
							}
						}
					});
}
