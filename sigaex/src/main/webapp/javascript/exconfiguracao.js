var ExConfiguracao = ExConfiguracao || {}

ExConfiguracao.Etapas = (function() {
	
	function Etapas() {
		this.etapaAtual = 0;
		this.btnProximo = $('.js-btn-proximo');
		this.btnAnterior = $('.js-btn-anterior');
		this.etapas = $('.js-etapa');
		this.spanIndicadorEtapa = $('.js-indicador-etapa');
		this.comboModelos = $('.js-siga-multiploselect--modelo.selectpicker');
		this.quantidadeModelos = this.comboModelos.data('quantidadeModelos');		
		this.btnConfirmacaoModal = $('.btn-confirmacao-modal');
		this.isContinuar = false;
	}
	
	Etapas.prototype.iniciar = function() {
		this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
		this.btnProximo.on('click', onBtnProximoClicado.bind(this));
		this.btnConfirmacaoModal.on('click', onBtnConfirmacaoModalClicado.bind(this));
		exibirEtapa.call(this, this.etapaAtual);		
	}	
	
	function onBtnAnteriorClicado() {
		atualizarEtapa.call(this, -1);
	}
	
	function onBtnProximoClicado() {
		atualizarEtapa.call(this, 1)
	}
	
	function onBtnConfirmacaoModalClicado() {
		atualizarEtapa.call(this, 1, false);
		this.isContinuar = true;
	}
	
	function exibirEtapa(numeroEtapa) {    
		this.etapas[numeroEtapa].style.display = "block";
	  
		if (numeroEtapa == 0) {
			this.btnAnterior.css('display', 'none');    
		} else {
			this.btnAnterior.css('display', 'inline');
		}
		if (numeroEtapa == (this.etapas.length - 1)) {
			this.btnPrtximo.text('Salvar');    
		} else {
			this.btnProximo.text('Próximo');    
		}
	  
		atualizarSpanIndicadorEtapa.call(this, numeroEtapa);
	}
	
	function atualizarEtapa(numeroEtapa, validar) {      					    
		if (this.isContinuar == false && validar !== false && numeroEtapa === 1 && this.comboModelos.val() != null && this.comboModelos.val().length >= this.quantidadeModelos) {
			sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Você selecionou todos os modelos, tem certeza que deseja aplicar configuração para todos?');		  		  
			return false;
		}
		if (this.isContinuar == false && validar !== false && numeroEtapa === 1 && this.comboModelos.val() != null && this.comboModelos.val().length > 10) {			
			sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Você selecionou ' + this.comboModelos.val().length + ' modelos, tem certeza que deseja aplicar configuração para todos os modelos selecionados?');		  
			return false;
		}
		sigaModal.fechar('confirmacaoModal');
		
		this.etapas[this.etapaAtual].style.display = "none";
	  
		this.etapaAtual += numeroEtapa;
	  
		if (this.etapaAtual >= this.etapas.length) {
			//submeter form
			//document.getElementById("regForm").submit();
			return false;
	  }
	  
	  exibirEtapa.call(this, this.etapaAtual);
	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
	}

	return Etapas;	
}());

$(function() {
	$('[name=idTpConfiguracao]').addClass('siga-select2');
	
	var etapas = new ExConfiguracao.Etapas();
	etapas.iniciar();	
});