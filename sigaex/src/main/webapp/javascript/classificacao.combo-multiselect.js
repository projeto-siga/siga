var ExClassificacao = ExClassificacao || {};


ExClassificacao.ComboClassificacao = (function() {
	
	function ComboClassificacao() {		
		this.combo = $('.js-siga-multiploselect--classificacao');				
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
		this.inputHiddenClassificacoesSelecionadas = $('#inputHiddenClassificacoesSelecionadas');
	}
				
	ComboClassificacao.prototype.iniciar = function() {	
		this.combo.on('change', onClassificacaoAlterada.call(this));
		this.combo.on('changed.bs.select', onClassificacaoSelecionada.bind(this));		
		inicializarClassificacoes.call(this);
	}
	
	function inicializarClassificacoes() {
		var idClassificacaoSelecionada = this.inputHiddenClassificacoesSelecionadas.val();
		if (idClassificacaoSelecionada) {
			var idClassificacoes = idClassificacaoSelecionada.split(',');
			this.combo.selectpicker('val', idClassificacoes);
			this.combo.selectpicker('render');			
		} /* else {
			// this.combo.selectpicker('selectAll');
		}*/
	}
	
	function onClassificacaoAlterada() {						
		if (this.combo && this.combo[0].options.length > 0) {
			if (this.combo[0].options[0].text.toLowerCase() == 'selecione') {
				this.combo[0].remove(0);				
				this.combo.removeAttr('disabled');			
				this.combo.parent().removeClass('disabled');
				this.combo.parent().children().removeClass('disabled');	
														
				this.combo.selectpicker('refresh');												
			}
		} else {
			reset.call(this);
		}					
	}	
	
	function onClassificacaoSelecionada(e, clickedIndex, isSelected, previousValue) {
		var idClassificacaoSelecao = this.combo.val() ? this.combo.val().toString() : '';
		this.inputHiddenClassificacoesSelecionadas.val(idClassificacaoSelecao);		
		this.emitter.trigger('selecionado', idClassificacaoSelecao);		
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().addClass('disabled');
		this.combo.parent().children().addClass('disabled');
				
		this.combo.selectpicker('refresh');
		
		this.inputHiddenClassificacoesSelecionadas.val('');
	}
			
	return ComboClassificacao;
	
}()); 

$(function() {	
	var ComboClassificacao = new ExClassificacao.ComboClassificacao();
	ComboClassificacao.iniciar();
 
});