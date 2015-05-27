/**
 * 
 */
//<c:set var="url" value="editar" />
function sbmt(id) {
	
	var frm = document.getElementById('frm');
	
	//Dispara a função onSave() do editor, caso exista
    if (typeof(onSave) == "function"){
    	onSave();
    } 
	
	if (id == null || IsRunningAjaxRequest()) {
		frm.action='recarregar';
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('recarregar', frm, id);
	}
	return;
	
	if (typeof(frm.submitsave) == "undefined")
		frm.submitsave = frm.submit;
	frm.action='recarregar';
                    
	if (id == null || typeof(id) == 'undefined' || IsRunningAjaxRequest()) {
		window.customOnsubmit = function() {return true;};
		frm.onsubmit = null;
		frm.submit = frm.submitsave;
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('recarregar', frm, id);
	}
}

//<c:set var="url" value="gravar" />
function gravarDoc() {
	clearTimeout(saveTimer);
	if (!validar(false)){
		triggerAutoSave();
		return false;
	}
	frm.action='gravar';
	window.customOnsubmit = function() {return true;};
	if (typeof(frm.submitsave) != "undefined")
		frm.submit = frm.submitsave;
	
	//Dispara a função onSave() do editor, caso exista
   	if (typeof(onSave) == "function")
   		onSave();
	
	frm.submit();
}

function validar(silencioso){
	
	var descr = document.getElementsByName('exDocumentoDTO.descrDocumento')[0].value;
	var eletroHidden = document.getElementById('eletronicoHidden');
	var eletro1 = document.getElementById('eletronicoCheck1');
	var eletro2 = document.getElementById('eletronicoCheck2');
	var descricaoAutomatica = document.getElementById('descricaoAutomatica');
	if (descricaoAutomatica == null && (descr==null || descr=="")) {
		aviso("Preencha o campo Descrição antes de gravar o documento.", silencioso);
		return false;
	}
	
	if (descr==null || descr=="") {
		aviso("Preencha o campo Descrição antes de gravar o documento.", silencioso);
		return false;
	}
	
	if (eletroHidden == null && !eletro1.checked && !eletro2.checked) {
		aviso("É necessário informar se o documento será digital ou físico, na parte superior da tela.", silencioso);
		return false;
	}

	var limite = document.getElementsByName('exDocumentoDTO.tamanhoMaximoDescricao')[0].value;
	if (document.getElementsByName('exDocumentoDTO.descrDocumento')[0].value.length >= limite) {
		aviso('O tamanho máximo da descrição é de ' + limite + ' caracteres', silencioso);
		return false;
	}
	
	return true;
	
}

function aviso(msg, silencioso){
	if (silencioso)
		avisoVermelho('O documento não pôde ser salvo: ' + msg);
	else alert(msg);
}

//<c:set var="url" value="excluirpreench" />
function removePreench(){
			//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
frm.action='excluirpreench';
frm.submit();
}

//<c:set var="url" value="alterarpreench" />
function alteraPreench(){
			//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
frm.action='alterarpreench';
frm.submit();
}

//<c:set var="url" value="carregarpreench" />
function carregaPreench(){
if (document.getElementById('preenchimento').value==0){
	frm.btnRemover.disabled="true";
	frm.btnAlterar.disabled="true";
}
else {
	//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
	frm.btnAlterar.disabled="true";
	frm.btnRemover.disabled="false";
	frm.action='carregarpreench';
	frm.submit();
	}
}

//<c:set var="url" value="gravarpreench" />
function adicionaPreench(){
var result='';
while((result=='') && (result!=null)){
	result=prompt('Digite o nome do padrão de preenchimento a ser criado para esse modelo:', '');
	if (result=='')
 		alert('O nome do padrão de preenchimento não pode ser vazio');
 	else if (result!=null){
 			//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
 			document.getElementsByName('exDocumentoDTO.nomePreenchimento')[0].value = result;
 			frm.action='gravarpreench';
			frm.submit();
 	}
}

}

//<c:set var="urlPdf" value="preverPdf" />
//<c:set var="url" value="prever" />
var newwindow = '';
function popitup_documento(pdf) {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open('','',winProp);
		newwindow.name='doc';
	}
	
	newwindow.opener = self;
	t = frm.target; 
	a = frm.action;
	frm.target = newwindow.name;
	if (pdf)
		frm.action='preverPdf';
	else
		frm.action='prever';

	//Dispara a função onSave() do editor, caso exista
    if (typeof(onSave) == "function"){
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
   window.alert('Atenção: essa opção só deve ser selecionada quando o subscritor possui certificado digital, pois será exigida a assinatura digital do documento.');   
}

var saveTimer;
function triggerAutoSave(){
	clearTimeout(saveTimer);
	saveTimer=setTimeout('autoSave()',60000 * 2);
}

triggerAutoSave();

var stillSaving = false;
//<c:set var="url" value="gravar" />
function autoSave(){
	if (stillSaving)
		return;
	if (!validar(true))
		return tryAgainAutoSave();
	for (instance in CKEDITOR.instances)
		CKEDITOR.instances[instance].updateElement();
	if (typeof(onSave) == "function")
		onSave();
	stillSaving = true;
	$.ajax({
		type: "POST",
		url: "gravar?ajax=true",
	   	data: $(frm).serialize(),
	   	timeout: 30000,
	   	success: doneAutoSave,
	   	error: failAutoSave
	});
}

function doneAutoSave(response){
	var data = response.split('_');
    if (data[0] == 'OK'){
    	avisoVerde('Documento ' + data[1] + ' salvo');
    	document.getElementById('codigoDoc').innerHTML = data[1];
    	document.getElementById('sigla').value = data[1];
    	stillSaving = false;
    	triggerAutoSave();
    } else failAutoSave();
}

function failAutoSave(response){
	tryAgainAutoSave(); 
	avisoVermelho('Atenção: Ocorreu um erro ao salvar o documento.');
	stillSaving = false;
}

function tryAgainAutoSave(){
	clearTimeout(saveTimer);
	saveTimer=setTimeout('autoSave()',60000 * 2);
}
