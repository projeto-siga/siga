var MultiploSelect = MultiploSelect || {};

MultiploSelect.ComboLotacao = (function() {
	
	function ComboLotacao() {		
		this.combo = $('.js-siga-multiploselect--lotacao');				
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
		this.inputHiddenLotacoesSelecionadas = $('#inputHiddenLotacoesSelecionadas');
	}
				
	ComboLotacao.prototype.iniciar = function() {	
		this.combo.on('change', onLotacaoAlterada.call(this));
		this.combo.on('changed.bs.select', onLotacaoSelecionada.bind(this));		
		inicializarLotacoes.call(this);
	}
	
	function inicializarLotacoes() {
		var idLotacaoSelecionada = this.inputHiddenLotacoesSelecionadas.val();
		if (idLotacaoSelecionada) {
			var idLotacoes = idLotacaoSelecionada.split(',');
			this.combo.selectpicker('val', idLotacoes);
			this.combo.selectpicker('render');			
		} /* else {
			// this.combo.selectpicker('selectAll');
		}*/
	}
	
	function onLotacaoAlterada() {						
		if (this.combo && this.combo[0].options.length > 1) {
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
	
	function onLotacaoSelecionada(e, clickedIndex, isSelected, previousValue) {
		var idLotacaoSelecao = this.combo.val() ? this.combo.val().toString() : '';
		this.inputHiddenLotacoesSelecionadas.val(idLotacaoSelecao);		
		this.emitter.trigger('selecionado', idLotacaoSelecao);		
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().addClass('disabled');
		this.combo.parent().children().addClass('disabled');
				
		this.combo.selectpicker('refresh');
		
		this.inputHiddenLotacoesSelecionadas.val('');
	}
			
	return ComboLotacao;
	
}());

MultiploSelect.ComboClassificacao = (function() {
	
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
		if (this.combo && this.combo[0].options.length > 1) {
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
	var ComboClassificacao = new MultiploSelect.ComboClassificacao();
	ComboClassificacao.iniciar();
	
	var ComboLotacao = new MultiploSelect.ComboLotacao();
	ComboLotacao.iniciar();
 
});