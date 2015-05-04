<script src="/sigasr/public/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/public/javascripts/language/messages_pt_BR.min.js"></script>
<style>
#sortable ul {
        height: 1.5em;
        line-height: 1.2em;
}

.ui-state-highlight {
        height: 1.5em;
        line-height: 1.2em;
}
</style>
<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<form id="formDesignacao">
		<input type="hidden" id="idConfiguracao" name="idConfiguracao" value="${idConfiguracao}" />
		<input type="hidden" id="hisIdIni" name="hisIdIni" value="${hisIdIni}" />
		<div>
			<div class="gt-form-row">
				<label>Descrição <span>*</span></label>
				<input id="descrConfiguracao"
					   type="text"
					   name="descrConfiguracao"
					   value="${descrConfiguracao}"
					   maxlength="255"
					   style="width: 791px;"
					   required/> 
				<span style="display:none;color: red" id="designacao.descrConfiguracao">Descrição não informada.</span>
			</div>
			<div class="gt-form-row box-wrapper">
				<div id="divSolicitante" class="box box-left gt-width-50">
					<label>Solicitante</label>
					#{pessoaLotaFuncCargoSelecao
						nomeSelLotacao:'lotacao',
						nomeSelPessoa:'dpPessoa',
						nomeSelFuncao:'funcaoConfianca',
						nomeSelCargo:'cargo',
						nomeSelGrupo:'cpGrupo',
						valuePessoa:dpPessoa?.pessoaAtual,
						valueLotacao:lotacao?.lotacaoAtual,
						valueFuncao:funcaoConfianca,
						valueCargo:cargo,
						valueGrupo:cpGrupo,
						disabled:disabled/}
				</div>
				<div class="box gt-width-50">
					<label>Órgão</label> 
					#{select name:'orgaoUsuario', 
						items:_orgaos, 
						valueProperty:'idOrgaoUsu',
						labelProperty:'nmOrgaoUsu',
						value:orgaoUsuario?.idOrgaoUsu,
						class:'select-siga',
						style:'width: 100%;'}
					#{option 0}Nenhum#{/option} 
					#{/select}
				</div>
			</div>
			
			<div class="gt-form-row box-wrapper">
				<div class="box box-left gt-width-50">
					<label>Local</label> 
					#{select name:'complexo', 
						items:_locais, 
						valueProperty:'idComplexo',
						labelProperty:'nomeComplexo', 
						value:complexo?.idComplexo,
						class:'select-siga',
						style:'width: 100%'}
						#{option 0}Nenhum#{/option} 
					#{/select}
				</div>
				<div class="box gt-width-50">
					<label>Atendente <span>*</span></label>#{selecao
						tipo:'lotacao', nome:'atendente', value:atendente?.lotacaoAtual,
						disabled:_modoExibicao == 'equipe' ? 'true' : disabled /}
					<span style="display:none;color: red" id="designacao.atendente">Atendente não informado;</span>
				</div>
			</div>	
			
			#{configuracaoItemAcao itemConfiguracaoSet:itemConfiguracaoSet,
							 acoesSet:acoesSet}#{/configuracaoItemAcao}
			
			<div class="gt-form-row">
				<div class="gt-form-row">
					<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="designacaoService.gravar()"/>
					<a class="gt-btn-medium gt-btn-left" onclick="designacaoService.cancelarGravacao()">Cancelar</a>
					<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="designacaoService.aplicar()"/>
				</div>
			</div>
			
			<div class="gt-form-row gt-width-100">
				<p class="gt-error" style="display:none;" id="erroCamposObrigatorios">Alguns campos obrigatórios não foram
					preenchidos.</p>
			</div>
		</div>
	</form>
</div>
