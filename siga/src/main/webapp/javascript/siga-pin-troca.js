var PinTrocar = PinTrocar || {}

PinTrocar.Etapas = (function() {
	
	function Etapas() {
		this.etapaAtual = 0;
		this.etapas = $('.js-etapa');

		this.btnProximo = $('.js-btn-proximo');	
		
		this.btnEnviarCodigo = $('#btnEnviarCodigo');
		this.btnReenviarCodigo = $('#btnReenviarCodigo');
				
		this.spanIndicadorEtapa = $('.js-indicador-etapa');
		this.btnErroModal = $('.btn-erro-modal');		
		this.tituloPrincipalEtapa = $('#tituloPrincipalEtapa');
		this.spinner = $('.js-spinner--salvando');
		this.btnGoToMesa = $('#btnGoToMesa');
		
		this.pinUser = $('#pinUser');
		this.pinUserConfirm = $('#pinUserConfirm');
		
		this.apresentacaoPinOK = false;
		this.cadastroPinEtapaOK = false;						
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);		
	}
	
	Etapas.prototype.iniciar = function() {

		this.btnProximo.on('click', onBtnProximoClicado.bind(this));
		this.btnErroModal.on('click', onBtnErroClicado.bind(this));
		
		this.btnEnviarCodigo.on('click', onBtnEnviarCodigoClicado.bind(this));
		this.btnReenviarCodigo.on('click', onBtnReenviarCodigoClicado.bind(this));

		
		this.pinUser.on('change', onChangePassword.bind(this));
		this.pinUserConfirm.on('keyup', onChangePassword.bind(this));


		exibirEtapa.call(this, this.etapaAtual);			
	}	
	
	function onChangePassword(){
		  if($('#pinUser').val() != $('#pinUserConfirm').val()) {
			//  sigaModal.alerta('Repetição da nova senha não confere, favor redigitar.');
		  } 
	}

	function onBtnAnteriorClicado() {			
		switch (this.etapas[this.etapaAtual - 1].id) {
			case 'apresentacaoPin':							
				break;
			case 'cadastroPinEtapa':				
				break;
		}
		
		atualizarEtapa.call(this, -1, false);	
	}
	
	function onBtnEnviarCodigoClicado() {			
		enviarCodigo();
		atualizarEtapa.call(this, 1);	
	}
	
	function onBtnReenviarCodigoClicado() {			
		enviarCodigo();
	}
	
	
	function onBtnErroClicado() {			
		sigaModal.fechar('erroModal');	
	}
	
	function onBtnProximoClicado(podeValidar) {
		if (podeValidar !== false && !validarCampos.call(this, this.etapaAtual)) {
			return false;
		} 			
		
		if (sigaModal.isAberto('erroModal')) {
			sigaModal.fechar('erroModal');
		}
				
		if (typeof this.etapas[this.etapaAtual + 1] !== 'undefined') {
			switch (this.etapas[this.etapaAtual + 1].id) {
			case 'apresentacaoPin':											
				break;
			case 'cadastroPinEtapa':
				break;
			}
			
			atualizarEtapa.call(this, 1);
		} else if (this.etapaAtual + 1 >= this.etapas.length) {
			salvar.call(this);
		}
	}
	

	function atualizarBtnProximo() {						
		if (false) {
			habilitarBtnProximo.call(this);			
		} else {
			desabilitarBtnProximo.call(this);
		}				
	}
	
	function desabilitarBtnProximo(obj) {
		obj.btnProximo.attr('disabled', 'disabled');
	}
	
	function habilitarBtnProximo(obj) {
		obj.btnProximo.removeAttr('disabled');
	}
	
	function exibirEtapa(numeroEtapa) {		
		this.etapas[numeroEtapa].style.display = "block";
	  

		this.btnProximo.removeClass('btn-primary').addClass('btn-success');
		this.btnProximo.html('Redefinir PIN  <i class="fas fa-check"></i>');    
		//desabilitarBtnProximo(this);

		atualizarTituloEtapaTopo.call(this);
	}
	
	function atualizarEtapa(numeroEtapa) {  		
		this.etapas[this.etapaAtual].style.display = "none";
	  
		this.etapaAtual += numeroEtapa;
	  
		exibirEtapa.call(this, this.etapaAtual);
	}		
	
	function salvar() {																			
		var form = this;						
		$.ajax({
			url: '/siga/api/v1/pin/trocar',
		    contentType: 'application/x-www-form-urlencoded',
		    type: 'POST',
		    data: {'pinAtual':$('#pinUserCurrent').val(),'pin':$('#pinUser').val()}, 											
			beforeSend: iniciarRequisicao.bind(this),
	        success: function(result){
	        	console.log(result.mensagem);
	        	finalizarRequisicao(form);
	        },
	        error: function(result){	
	        	erroRequisicao(form,result);
	        },
		});									
	}
	
	
	function enviarCodigo() {																			
		var form = this;						
		$.ajax({
			url: '/siga/api/v1/pin/gerar-token-reset',
		    contentType: 'application/x-www-form-urlencoded',
		    type: 'POST',									
	        success: function(result){
				sigaModal.enviarTextoEAbrir('erroModal', result.mensagem);	
	        },
	        error: function(result){	
	        	erroRequisicao(form,result);
	        },
		});									
	}
	
	function iniciarRequisicao() {
		this.btnGoToMesa.hide();
		$(this.etapas[this.etapaAtual]).hide();
		this.spanIndicadorEtapa.parent().hide();
		this.btnProximo.parent().hide();					
		this.spinner.parent().parent().parent().show();
		
		this.spinner.css({'border': '15px solid rgba(0, 0, 0, .1)', 'border-left-color': '#28a745'});		
		this.spinner.parent().parent().parent().find('h1').html('Salvando PIN...');
		this.spinner.parent().find('.icone-salvo-sucesso').css('opacity', '0');		
	}
	
	function erroRequisicao(form,result) {
		$(form.etapas[form.etapaAtual]).show();
		form.spanIndicadorEtapa.parent().show();
		form.btnProximo.parent().show();					
		form.spinner.parent().parent().parent().hide();
	
    	sigaModal.enviarTextoEAbrir('erroModal', result.responseJSON.errormsg);	
    	console.log("Falhou");
	}
	
	function finalizarRequisicao(form) {
		
		form.spinner.css('border-color', '#28a745');		
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Sua chave PIN foi redefinida!<br/><p class="lead">Nunca divulgue sua chave PIN. Ela é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'apresentacaoPin':
				retorno.alertaConfirmado = this.apresentacaoPinOK; 				
				this.emitter.trigger('validarModelos', retorno);				
				break;
			case 'cadastroPinEtapa':
				break;
			case 'selecaoDestinatario':
				break;
			case 'selecaoDestinatarioDefinicao':
				break;
		}
		
		return retorno.resultado;					
	}		
	
	function atualizarTituloEtapaTopo() {
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo').remove();
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo--duvida').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
			case 'apresentacaoPin':					
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Apresentação</span>');				
				break;
			case 'cadastroPinEtapa':				
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Cadastre um Novo PIN</span>');				
				break;

			}					
		}	
		

	}
	

	return Etapas;	
}());



$(function() {
	$('[name=idTpConfiguracao]').addClass('siga-select2');
	$('[data-toggle="popover"]').popover();
	
	var etapas = new PinTrocar.Etapas();
	etapas.iniciar();


});
