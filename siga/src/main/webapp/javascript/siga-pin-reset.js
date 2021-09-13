var PinReset = PinReset || {}

PinReset.Etapas = (function() {
	
	class Etapas {
        constructor() {
            this.etapaAtual = 0;
            this.etapas = $('.js-etapa');
            this.btnCancelar = $('.js-btn-cancelar');
            this.btnProximo = $('.js-btn-proximo');

            this.btnEnviarCodigo = $('#btnEnviarCodigo');
            this.btnReenviarCodigo = $('#btnReenviarCodigo');

            this.tituloPrincipalEtapa = $('#tituloPrincipalEtapa');
            this.spinner = $('.js-spinner--salvando');
            this.btnGoToMesa = $('#btnGoToMesa');

            this.pinUser = $('#pinUser');
            this.pinUserConfirm = $('#pinUserConfirm');
			this.tokenPin = $('#tokenPin');


            this.apresentacaoPinOK = false;
            this.cadastroPinEtapaOK = false;
            this.emitter = $({});
            this.on = this.emitter.on.bind(this.emitter);
        }
        iniciar() {
            this.btnCancelar.on('click', onBtnCancelarClicado.bind(this));
            this.btnProximo.on('click', onBtnProximoClicado.bind(this));

            this.btnEnviarCodigo.on('click', onBtnEnviarCodigoClicado.bind(this));
            this.btnReenviarCodigo.on('click', onBtnReenviarCodigoClicado.bind(this));


            this.on('validarResetPin', onValidar.bind(this));


            exibirEtapa.call(this, this.etapaAtual);
        }
    }
	
	
	
	function onValidar(evento, validacao) {		
		if(this.tokenPin.val() === "") {
			sigaModal.alerta('Código de segurança não informado. Favor inserí-lo.').select(this.tokenPin);		
			validacao.resultado = false;
			return false;
		} 
		
		if( this.tokenPin.val().length !== 8) {
			sigaModal.alerta('Código de segurança inválido. Favor corrigir.').select(this.tokenPin);		
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
	
	function onBtnEnviarCodigoClicado() {			
		enviarCodigo.call(this);
	}
	
	function onBtnReenviarCodigoClicado() {			
		enviarCodigo.call(this);
	}
	

	
	function onBtnProximoClicado(podeValidar) {
		if (podeValidar !== false && !validarCampos.call(this, this.etapaAtual)) {
			return false;
		} 			
				
		if (typeof this.etapas[this.etapaAtual + 1] !== 'undefined') {
			switch (this.etapas[this.etapaAtual + 1].id) {
			case 'enviarTokenPin':											
				break;
			case 'cadastroPinEtapa':
				break;
			}
			
			atualizarEtapa.call(this, 1);
		} else if (this.etapaAtual + 1 >= this.etapas.length) {
			salvar.call(this);
		}
	}
		
	function exibirEtapa(numeroEtapa) {		
		this.etapas[numeroEtapa].style.display = "block";
	  
		if (numeroEtapa == 0) {
			this.btnProximo.css('display', 'none');    
		} else {
			this.btnProximo.css('display', 'inline');
		}
		
		if (numeroEtapa == (this.etapas.length - 1)) {
			this.btnProximo.removeClass('btn-primary').addClass('btn-success');
			this.btnProximo.html('Redefinir PIN  <i class="fas fa-check"></i>');  
		}
		
		atualizarTituloEtapaTopo.call(this);

	}
	
	function atualizarEtapa(form,numeroEtapa) {  		
		if (form.etapaAtual == 0) {
			form.etapas[form.etapaAtual].style.display = "none";
			form.etapaAtual += numeroEtapa;
			exibirEtapa.call(form, form.etapaAtual);
		}
	}		
	
	function salvar() {																					
		if (validarCampos.call(this, this.etapaAtual)){	
			var form = this;		
			$.ajax({
				url: '/siga/api/v1/pin/reset',
			    contentType: 'application/x-www-form-urlencoded',
			    type: 'POST',
			    data: {'tokenPin':this.tokenPin.val(),'pin':this.pinUser.val()}, 											
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
	
	
	function enviarCodigo() {																			
		var form = this;						
		$.ajax({
			url: '/siga/api/v1/pin/gerar-token-reset',
		    contentType: 'application/x-www-form-urlencoded',
		    type: 'POST',		
			beforeSend: onSpinnerMostrar.bind(this),							
	        success: function(result){
				atualizarEtapa(form, 1);
				sigaModal.alerta("Código de segurança gerado e enviado para o e-mail cadastrado.").select(form.tokenPin);
	        },
	        error: function(result){	
	        	erroRequisicao(form,result);
	        },
			complete: onSpinnerOcultar.bind(this)	
		});									
	}
	
	function iniciarRequisicao() {
		this.btnGoToMesa.hide();
		$(this.etapas[this.etapaAtual]).hide();

		this.btnProximo.parent().hide();					
		this.spinner.parent().parent().parent().show();
		
		this.spinner.css({'border': '15px solid rgba(0, 0, 0, .1)', 'border-left-color': '#28a745'});		
		this.spinner.parent().parent().parent().find('h1').html('Salvando PIN...');
		this.spinner.parent().find('.icone-salvo-sucesso').css('opacity', '0');		
	}
	
	function erroRequisicao(form,result) {
		$(form.etapas[form.etapaAtual]).show();

		form.btnProximo.parent().show();					
		form.spinner.parent().parent().parent().hide();
	
    	sigaModal.alerta(result.responseJSON.errormsg,false,"Erro");	
    	console.log(result.responseJSON.errormsg);
	}
	
	function finalizarRequisicao(form) {
		
		form.spinner.css('border-color', '#28a745');		
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Seu PIN foi redefinido!<br/><p class="lead">Nunca divulgue seu PIN. Ele é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'enviarTokenPin':				
				break;
			case 'cadastroPinEtapa':
				this.emitter.trigger('validarResetPin', retorno);	
				break;
		}
		
		return retorno.resultado;					
	}		
	
	function atualizarTituloEtapaTopo() {
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo').remove();
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo--duvida').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
			case 'enviarTokenPin':					
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Enviar código para redefinir PIN</span>');				
				break;
			case 'cadastroPinEtapa':				
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Redefina seu PIN</span>');				
				break;

			}					
		}	
		

	}
	
	return Etapas;	
}());



$(function() {
	var etapas = new PinReset.Etapas();
	etapas.iniciar();
});
