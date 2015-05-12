<div class="gt-form gt-content-box">
	<form id="tipoAcaoForm">
		<input type="hidden" name="tipoAcao">

		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo<span>*</span></label>
			<input type="text" name="tipoAcao.siglaTipoAcao" id="siglaTipoAcao" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>T&iacute;tulo <span>*</span></label> <input type="text" id="tituloTipoAcao" name="tipoAcao.tituloTipoAcao" size="100" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Descri&ccedil;&atilde;o</label> <input type="text" id="descrTipoAcao" name="tipoAcao.descrTipoAcao" size="100" maxlength="255"/>
		</div>

		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.gravar()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.aplicar()"/>
		</div>
	</form>
</div>