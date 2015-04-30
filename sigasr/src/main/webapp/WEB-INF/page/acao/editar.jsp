<%-- #{extends 'main.html' /} #{set title:'Edição de A&ccedil;&atilde;o' /} --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div class="gt-form gt-content-box">
	<form id="acaoForm" enctype="multipart/form-data">
		<input type="hidden" name="idAcao">
		
<%-- 		#{ifErrors} --%>
<!-- 		<p class="gt-error">Alguns campos obrigatórios não foram -->
<%-- 			preenchidos ${error}</p> --%>
<%-- 		#{/ifErrors} --%>
		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo <span>*</span></label> 
			<input type="text" name="sigla" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>T&iacute;tulo <span>*</span></label> <input type="text" name="tituloAcao" size="100" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Descri&ccedil;&atilde;o</label> <input type="text" name="descrAcao" size="100" maxlength="255"/>
		</div>
		
		<div class="gt-form-row gt-width-66">
			<label>Tipo de a&ccedil;&atilde;o</label>
			<siga:selecao inputName="tipoAcao" urlAcao="buscar"></siga:selecao>
		</div>
		
		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="acaoService.gravar()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="acaoService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="acaoService.aplicar()"/>
		</div>
	</form>
</div>