<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div class="gt-form gt-content-box">
	<form id="acaoForm" enctype="multipart/form-data">

		<input type="hidden" name="acao.idAcao" id="id">

		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo <span>*</span></label>
			<input type="text" id="sigla" name="acao.sigla" maxlength="255" required/>
		</div>

		<div class="gt-form-row gt-width-66">
			<label>T&iacute;tulo <span>*</span></label> <input type="text" id="tituloAcao" name="acao.tituloAcao" size="100" maxlength="255" required/>
		</div>

		<div class="gt-form-row gt-width-66">
			<label>Descri&ccedil;&atilde;o</label> <input type="text" id="descrAcao" name="acao.descrAcao" size="100" maxlength="255"/>
		</div>

		<div class="gt-form-row gt-width-66">
			<label>Tipo de a&ccedil;&atilde;o</label>
			<input type="hidden" name="tipoAcao" id="tipoAcao" class="selecao">
			<siga:selecao modulo="sigasr" tema="simple" propriedade="tipoAcao" urlAcao="buscar"></siga:selecao>
		</div>

		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="prepararObjeto()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="acaoService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="acaoService.aplicar()"/>
		</div>

	</form>
</div>

<script>
	function prepararObjeto() {
		var tipoAcao = $('#tipoAcao');
		var tipoAcaoSelId = $('#formulario_tipoAcaoSel_id');
		tipoAcao.val(tipoAcaoSelId.val());

		acaoService.gravar();
	}
</script>