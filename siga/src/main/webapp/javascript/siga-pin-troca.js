var PinTrocar = PinTrocar || {}

PinTrocar.Etapas = (function() {
	
	class Etapas {
        constructor() {
            this.etapaAtual = 0;
            this.etapas = $('.js-etapa');

			this.btnCancelar = $('.js-btn-cancelar');
            this.btnProximo = $('.js-btn-proximo');

            this.btnEnviarCodigo = $('#btnEnviarCodigo');
            this.btnReenviarCodigo = $('#btnReenviarCodigo');

            this.spanIndicadorEtapa = $('.js-indicador-etapa');

            this.tituloPrincipalEtapa = $('#tituloPrincipalEtapa');
            this.spinner = $('.js-spinner--salvando');
            this.btnGoToMesa = $('#btnGoToMesa');

            this.pinUser = $('#pinUser');
            this.pinUserConfirm = $('#pinUserConfirm');
			this.pinUserCurrent = $('#pinUserCurrent');
            
			this.apresentacaoPinOK = false;
            this.cadastroPinEtapaOK = false;
            this.emitter = $({});
            this.on = this.emitter.on.bind(this.emitter);
        }
        iniciar() {
			this.btnCancelar.on('click', onBtnCancelarClicado.bind(this));
            this.btnProximo.on('click', onBtnProximoClicado.bind(this));

            this.on('validarTrocaPin', onValidar.bind(this));

            exibirEtapa.call(this, this.etapaAtual);
        }
    }

	function onValidar(evento, validacao) {		
		if(this.pinUserCurrent.val() === "") {
			sigaModal.alerta('PIN atual não informado. Favor inserí-lo.').select(this.pinUserCurrent);		
			validacao.resultado = false;
			return false;
		} 
		
		if( !isNumeric(this.pinUserCurrent.val())) {
			sigaModal.alerta('PIN atual deve conter apenas dígitos númericos (0-9). Favor corrigir.').select(this.pinUserCurrent);		
			validacao.resultado = false;
			return false;
		} 
		
		if( this.pinUserCurrent.val().length !== 8) {
			sigaModal.alerta('PIN deve ter 8 dígitos numéricos.').select(this.pinUserCurrent);		
			validacao.resultado = false;
			return false;
		} 
			
		if(this.pinUser.val() === "") {
			sigaModal.alerta('Novo PIN não informado. Favor inserí-lo.').select(this.pinUser);		
			validacao.resultado = false;
			return false;
		} 
		
		if( !isNumeric(this.pinUser.val())) {
			sigaModal.alerta('PIN deve conter apenas dígitos númericos (0-9). Favor corrigir.').select(this.pinUser);		
			validacao.resultado = false;
			return false;
		} 
		
		if( this.pinUser.val().length !== 8) {
			sigaModal.alerta('PIN deve ter 8 dígitos numéricos.').select(this.pinUser);		
			validacao.resultado = false;
			return false;
		} 
		
		if(this.pinUserConfirm.val() === "") {
			sigaModal.alerta('Confirmação do PIN não informado. Favor inserí-lo.').select(this.pinUserConfirm);		
			validacao.resultado = false;
			return false;
		} 
			
		if(this.pinUser.val() !== this.pinUserConfirm.val()) {
			sigaModal.alerta('Confirmação do PIN não confere com o novo. Favor corrigir.').select(this.pinUserConfirm);
			validacao.resultado = false;
			return false;
		} 		
		validacao.resultado = true;
		return true;		
	}
	
	
	function onBtnCancelarClicado() {			
		window.location.href = "/siga/app/principal";
	}
	
	function onBtnProximoClicado(podeValidar) {
		if (podeValidar !== false && !validarCampos.call(this, this.etapaAtual)) {
			return false;
		} 			
				
		if (typeof this.etapas[this.etapaAtual + 1] !== 'undefined') {
			switch (this.etapas[this.etapaAtual + 1].id) {
				case 'trocaPinEtapa':
					break;
			}
			
			atualizarEtapa.call(this, 1);
		} else if (this.etapaAtual + 1 >= this.etapas.length) {
			salvar.call(this);
		}
	}
	

	function exibirEtapa(numeroEtapa) {		
		this.etapas[numeroEtapa].style.display = "block";
	  
		this.btnProximo.removeClass('btn-primary').addClass('btn-success');
		this.btnProximo.html('Alterar PIN  <i class="fas fa-check"></i>');    
		
		
		atualizarTituloEtapaTopo.call(this);
		this.pinAtual.focus();
	}
	
	function atualizarEtapa(numeroEtapa) {  		
		this.etapas[this.etapaAtual].style.display = "none";
	  
		this.etapaAtual += numeroEtapa;
	  
		exibirEtapa.call(this, this.etapaAtual);
	}		
	
	function salvar() {	
		if (validarCampos.call(this, this.etapaAtual)){																			
			var form = this;				
			$.ajax({
				url: '/siga/api/v1/pin/trocar',
			    contentType: 'application/x-www-form-urlencoded',
			    type: 'POST',
			    data: {'pinAtual':this.pinUserCurrent.val(),'pin':this.pinUser.val()}, 											
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
	
    	sigaModal.alerta(result.responseJSON.errormsg,false,"Erro");
    	console.log(result.responseJSON.errormsg);
	}
	
	function finalizarRequisicao(form) {
		
		form.spinner.css('border-color', '#28a745');		
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Seu PIN foi alterado!<br/><p class="lead">Nunca divulgue seu PIN. Ele é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'trocaPinEtapa':					
				this.emitter.trigger('validarTrocaPin', retorno);	
				break;
		}
		
		return retorno.resultado;					
	}		
	
	function atualizarTituloEtapaTopo() {
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo').remove();
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo--duvida').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
			case 'trocaPinEtapa':				
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Alterar PIN</span>');				
				break;

			}					
		}	
	}
	

	return Etapas;	
}());



$(function() {
	var etapas = new PinTrocar.Etapas();
	etapas.iniciar();
});
