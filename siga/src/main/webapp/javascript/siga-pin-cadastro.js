var PinCadastro = PinCadastro || {}

PinCadastro.Etapas = (function() {
	
	class Etapas {
        constructor() {
            this.etapaAtual = 0;
            this.etapas = $('.js-etapa');
            this.btnAnterior = $('.js-btn-anterior');
            this.btnProximo = $('.js-btn-proximo');
            this.spanIndicadorEtapa = $('.js-indicador-etapa');
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
        iniciar() {
            this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
            this.btnProximo.on('click', onBtnProximoClicado.bind(this));

            this.on('validarNovoPin', onValidar.bind(this));

            exibirEtapa.call(this, this.etapaAtual);
        }
    }
	
	
	
	function onValidar(evento, validacao) {			
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

	function onBtnAnteriorClicado() {			
		switch (this.etapas[this.etapaAtual - 1].id) {
			case 'apresentacaoPin':							
				break;
			case 'cadastroPinEtapa':				
				break;
		}
		
		atualizarEtapa.call(this, -1, false);	
	}
	

	function onBtnProximoClicado(podeValidar) {
		if (podeValidar !== false && !validarCampos.call(this, this.etapaAtual)) {
			return false;
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
	
	function habilitarBtnProximo(obj) {
		obj.btnProximo.removeAttr('disabled');
	}
	
	function exibirEtapa(numeroEtapa) {		
		this.etapas[numeroEtapa].style.display = "block";
	  
		if (numeroEtapa == 0) {
			this.btnAnterior.css('display', 'none');    
		} else {
			this.btnAnterior.css('display', 'inline');
		}
		if (numeroEtapa == (this.etapas.length - 1)) {
			this.btnProximo.removeClass('btn-primary').addClass('btn-success');
			this.btnProximo.html('Criar PIN  <i class="fas fa-check"></i>');    
			this.pinUser.focus();
			//desabilitarBtnProximo(this);
		} else {
			this.btnProximo.removeClass('btn-success').addClass('btn-primary');
			this.btnProximo.html('Próximo  <i class="fas fa-long-arrow-alt-right"></i>');    
			habilitarBtnProximo(this);
		}
		
		atualizarTituloEtapaTopo.call(this);
		atualizarSpanIndicadorEtapa.call(this, numeroEtapa);
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
				url: '/siga/api/v1/pin',
			    contentType: 'application/x-www-form-urlencoded',
			    type: 'POST',
			    data: {'pin':$('#pinUser').val()}, 											
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
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Seu PIN foi definido!<br/><p class="lead">Agora você já pode utilizá-lo para Assinar e Autenticar Documentos com Senha.</p><p class="lead">Nunca divulgue seu PIN. Ele é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'apresentacaoPin':			
				break;
			case 'cadastroPinEtapa':					
				this.emitter.trigger('validarNovoPin', retorno);	
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
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Cadastre um novo PIN</span>');				
				break;

			}					
		}	
		

	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
	}

	return Etapas;	
}());



$(function() {

	var etapas = new PinCadastro.Etapas();
	etapas.iniciar();


});
