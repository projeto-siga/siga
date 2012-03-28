<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Novo Documento">
	<%--onLoad="javascript:autoSave();" --%>

	<script type="text/javascript">

<ww:url id="url" action="testar_conexao" namespace="/expediente/doc">
</ww:url>
var conexaoTimer;
function testaConexao(){
	PassAjaxResponseToFunction('<ww:property value="%{url}"/>','naoCaiu', false, true, null);
	conexaoTimer = setTimeout('avisaCaiu()',360000);
}

function limitaDescricao(silencioso){
	var limite = ${tamanhoMaximoDescricao};
	if (document.getElementsByName('descrDocumento')[0].value.length >= limite) {
		if (!silencioso)
			alert('O tamanho máximo da descrição é de ' + limite + ' caracteres');
		return false;
	}
	return true;
}


function naoCaiu(response, param){
	clearTimeout(conexaoTimer);
	setTimeout('testaConexao()',360000);
}

function avisaCaiu(){
	alert('Atenção: O sistema parece não estar respondendo. \nAs alterações feitas a partir de agora nesta tela não serão salvas. \nFavor, abrir o Siga em uma outra janela.');
}

var saveTimer;
function autoSave(){
	var salvarViaAjax = document.getElementById('salvarViaAjax');
	if(salvarViaAjax == null || salvarViaAjax.value != 'N') {
		clearTimeout(saveTimer);
		saveTimer=setTimeout('salvaAjaxFCK(true)',70000);
	}
}

function tryAgain(){
	clearTimeout(saveTimer);
	saveTimer=setTimeout('salvaAjaxFCK(true)',15000);
}

function FCKeditor_OnComplete( editorInstance ){
    	editorInstance.Commands.GetCommand('Save').Execute = function(){
    		window.parent.salvaAjaxFCK();
    	}
 		//autoSave();
}

var isSaving = false;
<ww:url id="url" action="gravar_ajax" namespace="/expediente/doc">
</ww:url>
function salvaAjaxFCK(automatico){
	if (!isSaving){
		if (!validar(automatico))
			return autoSave();
		for ( i = 0; i < parent.frames.length; ++i ) {
			if ( parent.frames[i].FCK )
				parent.frames[i].FCK.UpdateLinkedField();
        	if (parent.frames[i].updateFCKeditor) { 
            	  // Calls all functions in the functions array 
             	for (var i = 0 ; i < parent.frames[i].updateFCKeditor.length ; i++) 
				parent.frames[i].updateFCKeditor[i]() ; 
            } 
    	}
		var i;
		var s = "";
		for (i = 0; i < frm.length; i++){
			s = s + '&' + frm[i].name + '=' + escape(frm[i].value);
		}
		if (automatico)
			s = s + '&auto_save=1';
		isSaving = true;
		//PassAjaxResponseToFunction('<ww:property value="%{url}"/>'+'?'+s, 'salvo', false, automatico, 'POST');
		PassAjaxResponseToFunction('<ww:property value="%{url}"/>','salvo', false, automatico, s);
	}
}

function salvo(response){
	var data = response.split('_');
    if (data[0] == 'OK'){
    	avisoVerde('Salvo ' + data[2], 3000);
    	document.getElementById('codigoDoc').innerHTML = data[1];
    	document.getElementById('sigla').value = data[1];
    	document.getElementById('dataDoc').innerHTML = data[2];
    	autoSave();
    }
    else {
    	tryAgain(); 
    	//avisoVermelho('Ocorreu um erro ao salvar o documento.', 4000);
    }
    isSaving = false;
}

<ww:url id="url" action="editar" namespace="/expediente/doc">
</ww:url>
function sbmt(id) {
	
	var frm = document.getElementById('frm');
	//frm.submit();
	//return;
	
	//Dispara a função onSave() do editor, caso exista
    if (typeof(onSave) == "function"){
    	onSave();
    } 
	
	if (id == null || IsRunningAjaxRequest()) {
		frm.action='<ww:property value="%{url}"/>';
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>', frm, id);
	}
	return;
	
	if (typeof(frm.submitsave) == "undefined")
		frm.submitsave = frm.submit;
	frm.action='<ww:property value="%{url}"/>';
    for ( i = 0; i < parent.frames.length; ++i ) {
        if ( parent.frames[i].FCK )
                 parent.frames[i].FCK.UpdateLinkedField();
        if (parent.frames[i].updateFCKeditor) { 
              // Calls all functions in the functions array 
              for (var i = 0 ; i < parent.frames[i].updateFCKeditor.length ; i++) 
                      parent.frames[i].updateFCKeditor[i]() ; 
        } 
    }
                    
	if (id == null || typeof(id) == 'undefined' || IsRunningAjaxRequest()) {
		//window.alert("submitting");
		window.customOnsubmit = function() {return true;};
		frm.onsubmit = null;
		frm.submit = frm.submitsave;
		frm.submit();
		//window.alert("submitting done!");
	} else {
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>', frm, id);
	}
}

<ww:url id="url" action="gravar" namespace="/expediente/doc">
</ww:url>
counter = 0;
function gravarDoc() {
	clearTimeout(saveTimer);
	if (counter > 0){
		return;
	} else {
		if (!validar(false))
			return false;
		frm.action='<ww:property value="%{url}"/>';
		window.customOnsubmit = function() {return true;};
		if (typeof(frm.submitsave) != "undefined")
			frm.submit = frm.submitsave;
		counter++;
		
		//Dispara a função onSave() do editor, caso exista
    	if (typeof(onSave) == "function")
    		onSave();
		
		frm.submit();
	}
}

function validar(silencioso){
var descr = document.getElementsByName('descrDocumento')[0].value;
var eletroHidden = document.getElementById('eletronicoHidden');
var eletro1 = document.getElementById('eletronicoCheck1');
var eletro2 = document.getElementById('eletronicoCheck2');

if (descr==null || descr=="") {
	if (!silencioso){
		alert("Preencha o campo Descrição antes de gravar o documento.");
		document.getElementById('descrDocumento').focus();
	}
	return false;
}

if (eletroHidden == null && !eletro1.checked && !eletro2.checked) {
	if (!silencioso)
		alert("É necessário informar se o documento será digital ou físico, na parte superior da tela.");
	return false;
}

return limitaDescricao(silencioso);

}

<ww:url id="url" action="excluirpreench" namespace="/expediente/doc"></ww:url>
function removePreench(){
			//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
frm.action='<ww:property value="%{url}"/>';
frm.submit();
}

<ww:url id="url" action="alterarpreench" namespace="/expediente/doc"></ww:url>
function alteraPreench(){
			//Dispara a função onSave() do editor, caso exista
    		if (typeof(onSave) == "function"){
    			onSave();
    		} 
frm.action='<ww:property value="%{url}"/>';
frm.submit();
}

<ww:url id="url" action="carregarpreench" namespace="/expediente/doc"></ww:url>
function carregaPreench(){
if (frm.preenchimento.value==0){
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
	frm.action='<ww:property value="%{url}"/>';
	frm.submit();
	}
}

<ww:url id="url" action="gravarpreench" namespace="/expediente/doc"></ww:url>
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
 			frm.nomePreenchimento.value=result;
 			frm.action='<ww:property value="%{url}"/>';
			frm.submit();
 	}
}

}

<ww:url id="urlPdf" action="preverPdf" namespace="/expediente/doc">
</ww:url>
<ww:url id="url" action="prever" namespace="/expediente/doc">
</ww:url>
var newwindow = '';
function popitup_documento(pdf) {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open('','${propriedade}',winProp);
		newwindow.name='doc';
	}
	
	newwindow.opener = self;
	t = frm.target; 
	a = frm.action;
	frm.target = newwindow.name;
	if (pdf)
		frm.action='<ww:property value="%{urlPdf}"/>';
	else
		frm.action='<ww:property value="%{url}"/>';
//	alert(frm.action);
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

</script>

	<center>
	<table border="0" width="100%">
		<tr>
			<td><ww:form id="frm" name="frm" action="editar"
				namespace="/expediente/doc" theme="simple" method="POST">
				<ww:token />
				<input type="hidden" id="alterouModelo" name="alterouModelo" />
				<ww:hidden name="postback" value="1" />
				<ww:hidden id="sigla" name="sigla" value="%{sigla}" />
				<ww:hidden name="nomePreenchimento" value="" />
				<input type="hidden" name="campos" value="despachando" />
				<ww:hidden name="despachando" value="${despachando}" />
				<input type="hidden" name="campos" value="criandoAnexo" />
				<ww:hidden name="criandoAnexo" value="${criandoAnexo}" />

				<table class="form" width="100%">
					<tr class="header">
						<c:choose>
							<c:when test='${empty doc}'>
								<td colspan="4">Novo Documento</td>
							</c:when>
							<c:otherwise>
								<td>Documento:</td>
								<td colspan="3"><span id="codigoDoc">${doc.codigo}</span>
								de: <span id="dataDoc">${doc.dtRegDocDDMMYY}</span></td>
							</c:otherwise>
						</c:choose>
					</tr>

					<c:choose>
						<c:when test="${(doc.eletronico) && (doc.numExpediente != null)}">
							<c:set var="estiloTipo" value="display: none" />
							<c:set var="estiloTipoSpan" value="" />
						</c:when>
						<c:otherwise>
							<c:set var="estiloTipo" value="" />
							<c:set var="estiloTipoSpan" value="display: none" />
						</c:otherwise>
					</c:choose>

					<input type="hidden" name="campos" value="idTpDoc" />
					<tr>


						<td width="10%">Origem:</td>
						<td width="10%"><ww:select name="idTpDoc"
							list="tiposDocumento" listKey="idTpDoc"
							listValue="descrTipoDocumento"
							onchange="javascript:document.getElementById('alterouModelo').value='true';sbmt();"
							cssStyle="${estiloTipo}" /> <span style="${estiloTipoSpan}">${doc.exTipoDocumento.descrTipoDocumento}</span>
						</td>
						<td width="5%" align="right">Data:</td>
						<input type="hidden" name="campos" value="dtDocString" />
						<td><ww:textfield name="dtDocString" size="10"
							onblur="javascript:verifica_data(this, true);" /> &nbsp;&nbsp; <input
							type="hidden" name="campos" value="nivelAcesso" />Acesso <ww:select
							name="nivelAcesso" list="listaNivelAcesso" theme="simple"
							listKey="idNivelAcesso" listValue="nmNivelAcesso" /> <input
							type="hidden" name="campos" value="eletronico" /> <c:choose>
							<c:when test="${eletronicoFixo}">
								<input type="hidden" name="eletronico" id="eletronicoHidden"
									value="${eletronico}" />
								${eletronicoString}
							</c:when>
							<c:otherwise>
								<ww:radio list="%{#{1:'Digital',2:'Físico'}}" name="eletronico"
									id="eletronicoCheck" label="" value="${eletronico}"
									disabled="${eletronicoFixo}" />
							</c:otherwise>

						</c:choose></td>
					</tr>
					<c:if test='${tipoDocumento == "antigo"}'>
						<tr>
							<td>Nº original:</td>
							<input type="hidden" name="campos" value="numExtDoc" />
							<td colspan="3"><ww:textfield name="numExtDoc" size="16"
								maxLength="32" /></td>
						</tr>
						<tr style="font-weight: bold">
							<td>Nº antigo:</td>
							<input type="hidden" name="campos" value="numAntigoDoc" />
							<td colspan="3"><ww:textfield name="numAntigoDoc" size="16"
								maxLength="32" /> (informar o número do documento no antigo
							sistema de controle de expedientes ou de processos
							administrativos [SISAPA] ou [PROT])</td>
						</tr>
					</c:if>
					<c:if test='${tipoDocumento == "externo"}'>
						<tr>
							<td>Nº original:</td>
							<input type="hidden" name="campos" value="numExtDoc" />
							<td><ww:textfield name="numExtDoc" size="32" maxLength="32" /></td>
							<td align="right">Órgão:</td>
							<input type="hidden" name="campos" value="cpOrgaoSel.id" />
							<td><siga:selecao propriedade="cpOrgao" tema="simple" /></td>
						</tr>
						<tr>
							<td>Obs. sobre o Órgão Externo:</td>
							<input type="hidden" name="campos" value="obsOrgao" />
							<td colspan="3"><ww:textfield size="120" name="obsOrgao"
								maxLength="256" /></td>
						</tr>
						<tr>
							<td>Nº antigo:</td>
							<input type="hidden" name="campos" value="numAntigoDoc" />
							<td colspan="3"><ww:textfield name="numAntigoDoc" size="32"
								maxLength="34" /> (informar o número do documento no antigo
							sistema de controle de expedientes, caso tenha sido cadastrado)</td>
						</tr>
					</c:if>
					<input type="hidden" name="desativarDocPai"
						value="${desativarDocPai}" />
					<tr style="display: none;">
						<td>Documento Pai:</td>
						<td colspan="3"><siga:selecao titulo="Documento Pai:"
							propriedade="mobilPai" tema="simple"
							desativar="${desativarDocPai}" reler="sim" /></td>
					</tr>
					<tr>
						<c:choose>
							<c:when test='${tipoDocumento == "externo"}'>
								<td>Subscritor:</td>
								<input type="hidden" name="campos" value="nmSubscritorExt" />
								<td colspan="3"><ww:textfield name="nmSubscritorExt"
									size="80" maxLength="256" /></td>
							</c:when>
							<c:otherwise>
								<td>Subscritor:</td>
								<input type="hidden" name="campos" value="subscritorSel.id" />
								<input type="hidden" name="campos" value="substituicao" />
								<td colspan="3"><siga:selecao propriedade="subscritor"
									tema="simple" />&nbsp;&nbsp;<ww:checkbox name="substituicao"
									onclick="javascript:displayTitular(this);" />Substituto</td>
							</c:otherwise>
						</c:choose>
					</tr>
					<c:choose>
						<c:when test="${!substituicao}">
							<tr id="tr_titular" style="display: none">
						</c:when>
						<c:otherwise>
							<tr id="tr_titular" style="">
						</c:otherwise>
					</c:choose>

					<td>Titular:</td>
					<input type="hidden" name="campos" value="titularSel.id" />
					<td colspan="3"><siga:selecao propriedade="titular"
						tema="simple" /></td>
					</tr>
					<tr>
						<td>Função;Lotação;Localidade:</td>
						<td colspan="3"><input type="hidden" name="campos"
							value="nmFuncaoSubscritor" /> <ww:textfield
							name="nmFuncaoSubscritor" size="50" maxLength="128" />
						(Opcionalmente informe a função e a lotação na forma:
						Função;Lotação;Localidade)</td>
					</tr>
					<%--
					<tr>
					<td>Função:</td>
					<input type="hidden" name="campos" value="nmSubscritorFuncao" />
					<td colspan="3"><ww:if test="${empty doc.nmSubscritorFuncao}">
						<c:set var="style_subs_func_editar" value="display:none" />
						<c:set var="subscritorFuncao" value="false" />
					</ww:if><ww:else>
						<c:set var="style_subs_func" value="display:none" />
						<c:set var="subscritorFuncao" value="true" />
					</ww:else> <span id="span_subs_func_editar" style="${style_subs_func_editar}">
					<ww:hidden name="subscritorFuncaoEditar" value="subscritorFuncao" />
					<ww:textfield name="nmSubscritorFuncao" size="80" />
					</span> <span
						id="span_subs_func" style="${style_subs_func}"><input
						type="button" name="but_subs_func" value="Editar"
						onclick="javascript: document.getElementById('span_subs_func').style.display='none'; document.getElementById('span_subs_func_editar').style.display=''; document.getElementById('subscritorFuncaoEditar').value='true'; " /></span>
					</td>
				</tr>
--%>
					<%--<c:if test='${tipoDocumento != "externo"}'>--%>
					<tr>
						<td>Destinatário:</td>
						<input type="hidden" name="campos" value="tipoDestinatario" />
						<td colspan="3"><ww:select name="tipoDestinatario"
							onchange="javascript:sbmt();" list="listaTipoDest" /> <!-- sbmt('tipoDestinatario') -->
						<siga:span id="destinatario" depende="tipoDestinatario">

							<c:choose>
								<c:when test='${tipoDestinatario == 1}'>
									<input type="hidden" name="campos" value="destinatarioSel.id" />
									<siga:selecao propriedade="destinatario" tema="simple"
										reler="sim" />
									<!--  idAjax="destinatario"  -->
								</c:when>
								<c:when test='${tipoDestinatario == 2}'>
									<input type="hidden" name="campos"
										value="lotacaoDestinatarioSel.id" />
									<siga:selecao propriedade="lotacaoDestinatario" tema="simple"
										reler="sim" /></td>
						<!--  idAjax="destinatario" -->
						</c:when>
						<c:when test='${tipoDestinatario == 3}'>
							<input type="hidden" name="campos"
								value="orgaoExternoDestinatarioSel.id" />
							<siga:selecao propriedade="orgaoExternoDestinatario"
								tema="simple" reler="sim" />
							<!-- idAjax="destinatario" -->
							<br>
							<ww:textfield name="nmOrgaoExterno" size="120" maxLength="256" />
							</td>

						</c:when>
						<c:otherwise>
							<ww:textfield name="nmDestinatario" size="80" maxLength="256" />
							</td>
						</c:otherwise>
						</c:choose>
						</siga:span>
					</tr>

					<%--</c:if>--%>


					<c:if test='${ tipoDocumento != "externo"}'>
						<tr>
							<td>Tipo:</td>
							<td colspan="3"><ww:select name="idFormaDoc"
								onchange="javascript:document.getElementById('alterouModelo').value='true';sbmt();"
								list="formasDocPorTipo" listKey="idFormaDoc"
								listValue="descrFormaDoc" cssStyle="${estiloTipo}" /><!-- sbmt('forma') -->
							<c:if test="${not empty doc.exFormaDocumento}">
								<span style="${estiloTipoSpan}">${doc.exFormaDocumento.descrFormaDoc}</span>
							</c:if></td>
						</tr>

						<tr>
							<td>Modelo:</td>
							<td colspan="3"><siga:div id="modelo" depende="forma">
								<ww:if test="%{modelos.size > 1}">
									<ww:select name="idMod"
										onchange="document.getElementById('alterouModelo').value='true';sbmt();"
										list="modelos" listKey="idMod" listValue="nmMod" 
										cssStyle="${estiloTipo}" />
									<c:if test="${not empty doc.exModelo}">
										<span style="${estiloTipoSpan}">${doc.exModelo.nmMod}</span>
									</c:if>
									<!-- sbmt('modelo') -->
									<c:if test='${tipoDocumento!="interno"}'>(opcional)</c:if>
								</ww:if>
								<ww:else>
									<ww:hidden name="idMod" value="%{modelo.idMod}" />
								</ww:else>
							</siga:div></td>
						</tr>
						<tr>
							<td>Preenchimento Automático:</td>
							<input type="hidden" name="campos" value="preenchimento" />
							<td colspan="3"><ww:select name="preenchimento"
								list="preenchimentos" listKey="idPreenchimento"
								listValue="nomePreenchimento"
								onchange="javascript:carregaPreench()" /> &nbsp; <c:if
								test="${preenchimento==0}">
								<c:set var="desabilitaBtn"> disabled="disabled" </c:set>
							</c:if> <input type="button" name="btnAlterar" value="Alterar"
								onclick="javascript:alteraPreench()"${desabilitaBtn}>&nbsp;<input
								type="button" name="btnRemover" value="Remover"
								onclick="javascript:removePreench()"${desabilitaBtn} >&nbsp;<input
								type="button" value="Adicionar" name="btnAdicionar"
								onclick="javascript:adicionaPreench()"></td>
						</tr>

						<%--
					<tr>
						<td>Protótipo:</td>
						<td colspan="3"><ww:select name="idPrototipo"
							onchange="javascript:sbmt();" list="prototipos" listKey="idPro"
							listValue="nmPro" /><input type="button" name="" value="Novo" />
						<input type="button" name="" value="Excluir" /></td>
					</tr>
					--%>
					</c:if>
					<c:if test='${ tipoDocumento == "externo" }'>
						<ww:hidden name="idFormaDoc" value="%{formaDocPorTipo.idFormaDoc}" />
						<ww:hidden name="idMod" />
					</c:if>
					<%--<c:if test='${ tipoDocumento == "antigo" }'>
					<tr>
						<td>Forma:</td>
						<td colspan="3"><ww:select name="idFormaDoc"
							onchange="javascript:sbmt();" list="formasDocPorTipo"
							listKey="idFormaDoc" listValue="descrFormaDoc" /></td>
					</tr>
					<ww:hidden name="idMod" />
					<ww:if test="%{modelos.size > 1}">
						<tr>
							<td>Modelo:</td>
							<td colspan="3"><ww:select name="idMod"
								onchange="javascript:sbmt();" list="modelos" listKey="idMod"
								listValue="nmMod" /></td>
						</tr>
					</ww:if>
				</c:if>--%>

					<tr>
						<td>Classificação:</td>
						<c:if test="${modelo.exClassificacao!=null}">
							<c:set var="desativarClassif" value="sim" />
						</c:if>
						<input type="hidden" name="campos" value="classificacaoSel.id" />
						<td colspan="3"><siga:span id="classificacao"
							depende="forma;modelo">
							<siga:selecao desativar="${desativarClassif}"
								propriedade="classificacao" tema="simple" reler="sim" />
							<!--  idAjax="classificacao" -->
						</siga:span></td>
					</tr>
					<c:if
						test="${classificacaoSel.id!=null && classificacaoIntermediaria}">
						<tr>
							<td>Descrição da Classificação:</td>
							<td colspan="3"><siga:span id="descrClassifNovo"
								depende="forma;modelo;classificacao">
								<ww:textfield name="descrClassifNovo" size="80"
									value="${descrClassifNovo}" maxLength="4000" />
							</siga:span></td>
						</tr>
					</c:if>
					<tr>
						<input type="hidden" name="campos" value="descrDocumento" />
						<td>Descrição:</td>
						<td colspan="3"><ww:textarea name="descrDocumento" cols="80"
							rows="3" id="descrDocumento" /> <br>
						<span><b>(preencher o campo acima com palavras-chave,
						sempre usando substantivos, gênero masculino e singular)</b></span></td>
					</tr>
					<%--
				<c:if
					test='${tipoDocumento == "externo" or tipoDocumento == "antigo"}'>
					<tr>
						<td>Anexo:</td>
						<td colspan="3"><ww:if test="${empty doc.nmArqDoc}">
							<c:set var="style_anexo" value="display:none" />
							<c:set var="anexar" value="true" />
						</ww:if><ww:else>
							<c:set var="style_anexar" value="display:none" />
							<c:set var="anexar" value="false" />
						</ww:else> <span id="span_anexar" style="${style_anexar}"> <ww:file
							name="arquivo" theme="simple" /><ww:hidden id="anexar"
							name="anexarString" value="${anexar}" /></span><span id="span_anexo"
							style="${style_anexo}"> <ww:url id="url" action="anexo"
							namespace="/expediente/doc">
							<ww:param name="id">${doc.idDoc}</ww:param>
						</ww:url> <tags:anexo url="${url}" nome="${doc.nmArqDoc}"
							tipo="${doc.conteudoTpDoc}" /> <input type="button"
							name="but_anexar" value="Substituir"
							onclick="javascript: document.getElementById('span_anexo').style.display='none'; document.getElementById('span_anexar').style.display=''; document.getElementById('anexar').value='true'; " /></span>
						</td>
					</tr>
				</c:if>
--%>
					<c:if test='${tipoDocumento == "interno"}'>
						<c:if
							test="${modelo.conteudoTpBlob == 'template/freemarker' or not empty modelo.nmArqMod}">
							<tr>
								<td>Entrevista:</td>
								<td colspan="3"><siga:span id="spanEntrevista"
									depende="tipoDestinatario;destinatario;forma;modelo">
									<c:if test="${modelo.conteudoTpBlob == 'template/freemarker'}">
										${f:processarModelo(doc, 'entrevista', par, preenchRedirect)}
									</c:if>
									<c:if test="${modelo.conteudoTpBlob != 'template/freemarker'}">
										<c:import
											url="/paginas/expediente/modelos/${modelo.nmArqMod}?entrevista=1" />
									</c:if>
								</siga:span></td>
							</tr>
						</c:if>
					</c:if>
					<tr>
						<td></td>
						<td colspan="3"><input type="button"
							onclick="javascript: gravarDoc();" name="gravar" value="Ok" /> <c:if
							test='${tipoDocumento == "interno"}'>
							<c:if test="${not empty modelo.nmArqMod or modelo.conteudoTpBlob == 'template/freemarker'}">
								<input type="button" name="ver_doc"
									value="Visualizar o modelo preenchido"
									onclick="javascript: popitup_documento(false);" />
								<img valign="center" src="/sigaex/imagens/pdf.gif"
									name="ver_doc_pdf"
									onmouseover="javascript:this.style.cursor='hand'"
									onclick="javascript: popitup_documento(true);" />
							</c:if>
						</c:if></td>
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>
	</center>

	<!--  tabela do rodapé -->
</siga:pagina>

<script type="text/javascript">
window.customOnsubmit = function() {return true;};
{
//	var frm = document.getElementById('frm');
//	if (typeof(frm.submitsave) == "undefined")
//		frm.submitsave = frm.submit;
}
</script>
