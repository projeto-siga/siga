<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Cadastro de configuração">


<ww:url id="url" action="editar" namespace="/configuracao">
</ww:url>

<script type="text/javascript" language="Javascript1.1">

<ww:url id="urlForma" action="carregar_lista_formas" namespace="/expediente/doc">
</ww:url>
function alteraTipoDaForma(){
	ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{urlForma}"/>'+'?tipoForma='+document.getElementById('tipoForma').value+'&idFormaDoc='+'${idFormaDoc}', null, document.getElementById('comboFormaDiv'))
}

<ww:url id="urlModelo" action="carregar_lista_modelos" namespace="/expediente/doc">
</ww:url>
function alteraForma(){
	ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{urlModelo}"/>'+'?forma='+document.getElementById('forma').value+'&idMod='+'${hisIdIni}', null, document.getElementById('comboModeloDiv'))
}

function sbmt() {
	editar_gravar.action='<ww:property value="%{url}"/>';
	editar_gravar.submit();
}

</script>



<body onload="aviso()">

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		


		<form action="editar_gravar.action">
		<input type="hidden" name="postback" value="1" /> <ww:hidden
			name="id" /> <c:set var="dataFim" value="" />
		<h1>Cadastro de configuração <c:if
			test="${not empty configuracao}">
			para ${configuracao.cpTipoConfiguracao.dscTpConfiguracao}
		</c:if></h1>
		<div class="gt-content-box gt-for-table">
		<table class="gt-form-table" width="100%">
			<tr class="header">
				<td colspan="2">Dados da configuração</td>
			</tr>
			<tr>
				<td><b>Tipo de Configuração</b></td>
				<td><ww:select name="idTpConfiguracao"
					list="listaTiposConfiguracao" listKey="idTpConfiguracao"
					listValue="dscTpConfiguracao" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td><b>Situação</b></td>
				<td><ww:select name="idSituacao" list="listaSituacao"
					listKey="idSitConfiguracao" listValue="dscSitConfiguracao"
					theme="simple" headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td>Nível de acesso</td>
				<td><ww:select name="idNivelAcesso" list="listaNivelAcesso"
					theme="simple" listKey="idNivelAcesso" listValue="nmNivelAcesso"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<tr>
				<td>Pessoa</td>
				<td><siga:selecao propriedade="pessoa" tema="simple" /></td>
			</tr>
			<tr>
				<td>Lotação</td>
				<td><siga:selecao propriedade="lotacao" tema="simple" /></td>
			</tr>
			<tr>
				<td>Função de Confiança</td>
				<td><siga:selecao propriedade="funcao" tema="simple" /></td>
			</tr>
			<tr>
				<td>Órgão</td>
				<td><ww:select name="idOrgaoUsu" list="orgaosUsu"
					listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<%--<tr>
				<td>Cargo</td>
				<td><siga:selecao propriedade="cargo" tema="simple" /></td>
			</tr>--%>
			<tr>
				<td>Tipo de Movimentação</td>
				<td><ww:select name="idTpMov" list="listaTiposMovimentacao"
					listKey="idTpMov" listValue="descrTipoMovimentacao" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>
			<%--<tr>
				<td>Via</td>
				<td><ww:select name="idVia" list="listaVias" listKey="idVia"
					listValue="destinacao" theme="simple" headerValue="[Indefinido]"
					headerKey="0" /></td>
			</tr> --%>
			<tr>
				<td>Tipo:</td>
				<td><ww:select name="idTpFormaDoc" list="tiposFormaDoc"
		                       listKey="idTipoFormaDoc" listValue="descTipoFormaDoc"
				      		   theme="simple" headerKey="0" headerValue="[Indefinido]"
							   onchange="javascript:alteraTipoDaForma();" id="tipoForma" />&nbsp;&nbsp;&nbsp;
						       <div style="display: inline" id="comboFormaDiv">
							   		<script type="text/javascript">alteraTipoDaForma();</script>
							   </div>
				</td>
			</tr>
			<!-- Esse timeout no modelo está estranho. Está sendo necessário porque primeiro
      		 precisa ser executado o request ajax referente à FormaDocumento, da qual a lista 
		     de modelos depende. Talvez seria bom tornar síncronos esses dois requests ajax -->
			<tr>
				<td>Modelo:</td>
				<td>
					<div style="display: inline" id="comboModeloDiv">
						<script type="text/javascript">setTimeout("alteraForma()",500);</script>
					</div>
				</td>
			</tr>		
			<tr>
				<td>Classificação</td>
				<td><siga:selecao propriedade="classificacao" tema="simple" /></td>
			</tr>			
			<tr>
				<td>Origem</td>
				<td><ww:select name="idTpDoc" list="listaTiposDocumento"
					listKey="idTpDoc" listValue="descrTipoDocumento" theme="simple"
					headerValue="[Indefinido]" headerKey="0" /></td>
			</tr>			
			<tr>
			</tr>
			<tr class="button">
				<td><input type="submit" value="Ok" class="gt-btn-large gt-btn-left" /> <input type="button"
					value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" /></td>
				<td></td>
			</tr>
		</table>
		</div>
<br />
</div></div>
</body>

</siga:pagina>