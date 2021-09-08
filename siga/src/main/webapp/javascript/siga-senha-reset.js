var SenhaReset = SenhaReset || {}

SenhaReset.Etapas = (function() {
	
	class Etapas {
        constructor() {
            this.etapaAtual = 0;
            this.etapas = $('.js-etapa');
            this.btnAnterior = $('.js-btn-cancelar');
            this.btnProximo = $('.js-btn-proximo');
            this.spanIndicadorEtapa = $('.js-indicador-etapa');
            this.tituloPrincipalEtapa = $('#tituloPrincipalEtapa');
            this.spinner = $('.js-spinner--salvando');
            this.btnGoToMesa = $('#btnGoToMesa');

            this.cpfResetSenhaOK = false;
            this.emailResetSenhaOK = false;
            this.emitter = $({});
            this.on = this.emitter.on.bind(this.emitter);
        }
        iniciar() {
            this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
            this.btnProximo.on('click', onBtnProximoClicado.bind(this));

            exibirEtapa.call(this, this.etapaAtual);
        }
    }
	
	
	
	function onValidar(evento, validacao) {			
		return true;		
	}

	function onBtnAnteriorClicado() {			
		switch (this.etapas[this.etapaAtual - 1].id) {
			case 'cpfResetSenha':		
									
				break;
			case 'emailResetSenha':				
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
			case 'cpfResetSenha':											
				break;
			case 'emailResetSenha':
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
			this.btnProximo.css('display', 'inline');  
			
			this.btnProximo.removeClass('btn-success').addClass('btn-primary');
			this.btnProximo.html('Próximo  <i class="fas fa-long-arrow-alt-right"></i>');  
			this.btnAnterior.html('Cancelar');   
			habilitarBtnProximo(this);
 
		} else if (numeroEtapa == 1) {
			this.btnProximo.css('display', 'none');  
 
		} else {
			this.btnProximo.css('display', 'inline');  

			this.btnProximo.removeClass('btn-primary').addClass('btn-success');
			this.btnProximo.html('Gerar  <i class="fas fa-check"></i>');    
			this.btnAnterior.html('<i class="fas fa-long-arrow-alt-left"></i> Anterior');  
			

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
	
	function localizarAcesso() {																			
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
		this.spanIndicadorEtapa.parent().hide();
		this.btnProximo.parent().hide();					
		this.spinner.parent().parent().parent().show();
		
		this.spinner.css({'border': '15px solid rgba(0, 0, 0, .1)', 'border-left-color': '#28a745'});		
		this.spinner.parent().parent().parent().find('h1').html('Salvando nova Senha...');
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
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Sua Senha foi redefinida!<br/><p class="lead">Nunca divulgue sua Senha. Ela é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'cpfResetSenha':			
				break;
			case 'emailResetSenha':					
				this.emitter.trigger('validarNovoPin', retorno);	
				break;
		}
		
		return retorno.resultado;					
	}		
	
	function atualizarTituloEtapaTopo() {
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
			case 'cpfResetSenha':				
				titulo = 'Encontre seu Acesso';
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;'+titulo+'</span>');				
				break;
			case 'emailResetSenha':		
				titulo = 'Gerar e Enviar Código';		
				this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;'+titulo+'</span>');				
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
	var etapas = new SenhaReset.Etapas();
	etapas.iniciar();
});