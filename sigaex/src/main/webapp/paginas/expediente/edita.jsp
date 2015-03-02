<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Novo Documento">
<script type="text/javascript" src="/ckeditor/ckeditor/ckeditor.js"></script>
	
<script type="text/javascript">
<ww:url id="url" action="editar" namespace="/expediente/doc">
</ww:url>
function sbmt(id) {
	
	var frm = document.getElementById('frm');
	
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
                    
	if (id == null || typeof(id) == 'undefined' || IsRunningAjaxRequest()) {
		window.customOnsubmit = function() {return true;};
		frm.onsubmit = null;
		frm.submit = frm.submitsave;
		frm.submit();
	} else {
		ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>', frm, id);
	}
}
<ww:url id="url" action="gravar" namespace="/expediente/doc">
</ww:url>
function gravarDoc() {
	clearTimeout(saveTimer);
	if (!validar(false)){
		triggerAutoSave();
		return false;
	}
	frm.action='<ww:property value="%{url}"/>';
	window.customOnsubmit = function() {return true;};
	if (typeof(frm.submitsave) != "undefined")
		frm.submit = frm.submitsave;
	
	//Dispara a função onSave() do editor, caso exista
   	if (typeof(onSave) == "function")
   		onSave();
	
	frm.submit();
}
function validar(silencioso){
	
	var descr = document.getElementsByName('descrDocumento')[0].value;
	var eletroHidden = document.getElementById('eletronicoHidden');
	var eletro1 = document.getElementById('eletronicoCheck1');
	var eletro2 = document.getElementById('eletronicoCheck2');
	var descricaoAutomatica = document.getElementById('descricaoAutomatica');
	if (descricaoAutomatica == null && (descr==null || descr=="")) {
		aviso("Preencha o campo Descrição antes de gravar o documento.", silencioso);
		return false;
	}
	
	if (eletroHidden == null && !eletro1.checked && !eletro2.checked) {
		aviso("É necessário informar se o documento será digital ou físico, na parte superior da tela.", silencioso);
		return false;
	}
	var limite = ${tamanhoMaximoDescricao};
	if (document.getElementsByName('descrDocumento')[0].value.length >= limite) {
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
var saveTimer;
function triggerAutoSave(){
	clearTimeout(saveTimer);
	saveTimer=setTimeout('autoSave()',60000 * 2);
}
triggerAutoSave();
var stillSaving = false;
<ww:url id="url" action="gravar" namespace="/expediente/doc">
</ww:url>
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
		url: "<ww:property value="%{url}"/>?ajax=true",
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
</script>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
	
		<h2>
			<c:choose>
				<c:when test='${empty doc}'>
					Novo Documento
				</c:when>
				<c:otherwise>
					<span id="codigoDoc">${doc.codigo}</span>
					<!-- de: <span id="dataDoc">${doc.dtRegDocDDMMYY}</span>-->
				</c:otherwise>
			</c:choose>
		</h2>
				
		<div class="gt-content-box gt-for-table">

			<ww:form id="frm" name="frm" action="editar"
				namespace="/expediente/doc" theme="simple" method="POST">
				<ww:token />
				<input type="hidden" id="alterouModelo" name="alterouModelo" />
				<ww:hidden name="postback" value="1" />
				<ww:hidden id="sigla" name="sigla" value="%{sigla}" />
				<ww:hidden name="nomePreenchimento" value="" />
				<input type="hidden" name="campos" value="despachando" />
				<ww:hidden name="despachando" value="${despachando}" />
				<input type="hidden" name="campos" value="criandoAnexo" />
				<input type="hidden" name="campos" value="autuando" />
				<ww:hidden name="autuando" value="${autuando}" />
				<ww:hidden name="criandoAnexo" value="${criandoAnexo}" />
				<input type="hidden" name="campos" value="idMobilAutuado" />
				<ww:hidden name="idMobilAutuado" value="${idMobilAutuado}" />

				<table class="gt-form-table">
					<tr class="header">
						<c:choose>
							<c:when test='${empty doc}'>
								<td colspan="4">Novo Documento</td>
							</c:when>
							<c:otherwise>
								<td colspan="4">Dados básicos:</td>
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
								<c:if test="${eletronico == 2}">
									<script type="text/javascript">$("html").addClass("fisico");</script>
								</c:if>
							</c:when>
							<c:otherwise>
								<ww:radio list="%{#{1:'Digital',2:'Físico'}}" name="eletronico"
									id="eletronicoCheck" label="" value="${eletronico}"
									disabled="${eletronicoFixo}" onchange="setFisico();"/>
								<script type="text/javascript">function setFisico() {if ($('input[name=eletronico]:checked').val() == 2) $('html').addClass('fisico'); else $('html').removeClass('fisico');}; setFisico();</script>									
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
							<td>Data original do documento:</td>
							<input type="hidden" name="campos" value="dtDocOriginalString" />
							<td colspan="3"><ww:textfield name="dtDocOriginalString" size="10"
							onblur="javascript:verifica_data(this, true);" /></td>
						</tr>
						<tr>
							<td>Nº original:</td>
							<input type="hidden" name="campos" value="numExtDoc" />
							<td><ww:textfield name="numExtDoc" size="32" maxLength="32" /></td>
							<td align="right">Órgão:</td>
							<input type="hidden" name="campos" value="cpOrgaoSel.id" />
							<td><siga:selecao propriedade="cpOrgao" tema="simple" modulo="siga"/></td>
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
							propriedade="mobilPai" tema="simple" modulo="sigaex"
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
								<td colspan="3"><siga:selecao propriedade="subscritor" modulo="siga"
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
						tema="simple" modulo="siga"/></td>
					</tr>
					<tr>
						<td>Função;<wbr/>Lotação;<wbr/>Localidade:</td>
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
                                        reler="sim" modulo="siga" />
                                    <!--  idAjax="destinatario"  -->
									    
								</c:when>
								<c:when test='${tipoDestinatario == 2}'>
									<input type="hidden" name="campos"
										value="lotacaoDestinatarioSel.id" />
									<siga:selecao propriedade="lotacaoDestinatario" tema="simple"
                                        reler="sim" modulo="siga" /></td>
                        			<!--  idAjax="destinatario" -->
						   
						</c:when>
						<c:when test='${tipoDestinatario == 3}'>
							<input type="hidden" name="campos"
								value="orgaoExternoDestinatarioSel.id" />
							<siga:selecao propriedade="orgaoExternoDestinatario"
                                tema="simple" reler="sim" modulo="siga" />
                            <!-- idAjax="destinatario" -->
							<br>
							<ww:textfield name="nmOrgaoExterno" size="120" maxLength="256" />
							<input type="hidden" name="campos" value="nmOrgaoExterno" />
							</td>

						</c:when>
						<c:otherwise>
							<ww:textfield name="nmDestinatario" size="80" maxLength="256" />
							<input type="hidden" name="campos" value="nmDestinatario" />
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

						<ww:if test="%{modelos.size > 1}">
							<tr>
								<td>Modelo:</td>
								<td colspan="3"><siga:div id="modelo" depende="forma">
										<ww:select name="idMod"
											onchange="document.getElementById('alterouModelo').value='true';sbmt();"
											list="modelos" listKey="idMod" listValue="nmMod" 
											cssStyle="${estiloTipo}" />
										<c:if test="${not empty doc.exModelo}">
											<span style="${estiloTipoSpan}">${doc.exModelo.nmMod}</span>
										</c:if>
										<!-- sbmt('modelo') -->
										<c:if test='${tipoDocumento!="interno"}'>(opcional)</c:if>
								</siga:div></td>
							</tr>
						</ww:if>
							<ww:else>
							<ww:hidden name="idMod" value="%{modelo.idMod}" />
						</ww:else>
						
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

						
					<tr style="display:<ww:if test="%{modelo.exClassificacao!=null}">none</ww:if><ww:else>visible</ww:else>">
						<td>Classificação:</td>
						<c:if test="${modelo.exClassificacao!=null}">
							<c:set var="desativarClassif" value="sim" />
						</c:if>
						<input type="hidden" name="campos" value="classificacaoSel.id" />
						<td colspan="3"><siga:span id="classificacao"
							depende="forma;modelo">
							<siga:selecao desativar="${desativarClassif}" modulo="sigaex"
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
					<tr style="display:<ww:if test="%{modelo.descricaoAutomatica}">none</ww:if><ww:else>visible</ww:else>">
					    <c:if test="${modelo.descricaoAutomatica}">
							<input type="hidden" id="descricaoAutomatica" value="sim" />
						</c:if>
						<input type="hidden" name="campos" value="descrDocumento" />
						<td>Descrição:</td>
						<td colspan="3"><ww:textarea name="descrDocumento" cols="80"
							rows="2" id="descrDocumento" cssClass="gt-form-textarea"/> <br>
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
							<tr class="header">
								<td colspan="4">Dados complementares</td>
							</tr>
							<tr>
								<td colspan="4"><siga:span id="spanEntrevista"
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
						<td colspan="4"><input type="button"
							onclick="javascript: gravarDoc();" name="gravar" value="Ok" class="gt-btn-small gt-btn-left"/>
						 	<c:if test='${tipoDocumento == "interno"}'>
							<c:if test="${not empty modelo.nmArqMod or modelo.conteudoTpBlob == 'template/freemarker'}">
								<input type="button" name="ver_doc"
									value="Visualizar o Documento"
									onclick="javascript: popitup_documento(false);" class="gt-btn-large gt-btn-left"/>
								<input type="button" 
									name="ver_doc_pdf"
									onclick="javascript: popitup_documento(true);" value="Visualizar a Impressão" class="gt-btn-large gt-btn-left"/>
							</c:if>
						</c:if></td>
					</tr>
				</table>
			</ww:form>

		</div>	
	</div>
</div>

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