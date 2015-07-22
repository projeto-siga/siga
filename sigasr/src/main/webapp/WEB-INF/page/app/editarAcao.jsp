#{extends 'main.html' /} #{set title:'EdiÃ§Ã£o de A&ccedil;&atilde;o' /}
<div class="gt-form gt-content-box">
	<form id="acaoForm" enctype="multipart/form-data">
		<input type="hidden" name="idAcao">
		
		#{ifErrors}
		<p class="gt-error">Alguns campos obrigatÃ³rios nÃ£o foram
			preenchidos ${error}</p>
		#{/ifErrors}
		<div class="gt-form-row gt-width-66">
			<label>CÃ³digo <span>*</span></label> 
			<input type="text" name="sigla" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>TÃ­tulo <span>*</span></label> <input type="text"
				name="tituloAcao" size="100" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>DescriÃ§Ã£o</label> <input type="text"
				name="descrAcao" size="100" maxlength="255"/>
		</div>
		
		<div class="gt-form-row gt-width-66">
			<label>Tipo de aÃ§Ã£o</label>
				#{selecao tipo:'tipoAcao', nome:'tipoAcao', value:tipoAcao?.atual /}
		</div>
		
		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="acaoService.gravar()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="acaoService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="acaoService.aplicar()"/>
		</div>
	</form>
</div>