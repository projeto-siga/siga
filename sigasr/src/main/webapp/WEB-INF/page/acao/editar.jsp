<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div class="">
	<form id="acaoForm" enctype="multipart/form-data">

		<input type="hidden" name="acao.idAcao" id="id">

		<div class="form-group">
			<label>C&oacute;digo <span>*</span></label>
			<input type="text" id="sigla" name="acao.sigla" maxlength="255" required class="form-control"/>
		</div>

		<div class="form-group">
			<label>T&iacute;tulo <span>*</span></label>
			<input type="text" id="tituloAcao" name="acao.tituloAcao" size="100" maxlength="255" required class="form-control"/>
		</div>

		<div class="form-group">
			<label>Descri&ccedil;&atilde;o</label>
			<input type="text" id="descrAcao" name="acao.descrAcao" size="100" maxlength="255" class="form-control"/>
		</div>

		<div class="form-group">
			<label>Tipo de a&ccedil;&atilde;o</label>
			<input type="hidden" name="tipoAcaoSel" id="tipoAcao" class="selecao" value="">
			<sigasr:selecao3 tamanho="grande" propriedade="tipoAcaoSel" tipo="tipoAcao" tema="simple" modulo="sigasr"/>
		</div>

		<input type="button" value="Gravar" class="btn btn-primary" onclick="prepararObjeto()"/>
		<a class="btn btn-secondary" style="color: #fff" onclick="acaoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary" onclick="acaoService.aplicar()"/>

	</form>
</div>

<script>
	function prepararObjeto() {
		var tipoAcao = $('#tipoAcaoSel');
		var tipoAcaoSelId = $('#formulario_tipoAcaoSel_id');
		tipoAcao.val(tipoAcaoSelId.val());

		acaoService.gravar();
	}
</script>