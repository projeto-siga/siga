<div>
	<form id="tipoAcaoForm">
		<input type="hidden" name="tipoAcao.idTipoAcao" id="idTipoAcao">

		<div class="form-group">
			<label>C&oacute;digo<span>*</span></label>
			<input type="text" name="tipoAcao.siglaTipoAcao" id="siglaTipoAcao" maxlength="255" required class="form-control"/>
		</div>
		<div class="form-group">
			<label>T&iacute;tulo <span>*</span></label>			
			<input type="text" id="tituloTipoAcao" name="tipoAcao.tituloTipoAcao" maxlength="255" required class="form-control"/>
		</div>
		<div class="form-group">
			<label>Descri&ccedil;&atilde;o</label>
			<input type="text" id="descrTipoAcao" name="tipoAcao.descrTipoAcao" maxlength="255" class="form-control"/>
		</div>

		<input type="button" value="Gravar" class="btn btn-primary" onclick="tipoAcaoService.gravar()"/>
		<a class="btn btn-secondary" style="color: #fff" onclick="tipoAcaoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="btn btn-primary" onclick="tipoAcaoService.aplicar()"/>
	</form>
</div>