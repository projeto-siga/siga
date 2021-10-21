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

			this.emailListContainer = $('#emailListContainer');
			this.cpfUser = $('#cpfUser');
			this.jwt = $('#jwt');
			
			this.btnEnviarCodigo = $('#btnEnviarCodigo');
            this.btnReenviarCodigo = $('#btnReenviarCodigo');

			this.passNova = $('#passNova');
			this.passConfirmacao = $('#passConfirmacao');
			this.tokenSenha = $('#tokenSenha');
			
			this.ldap = $('#ldapContainer');

            this.cpfResetSenhaOK = false;
            this.emailResetSenhaOK = false;
            this.emitter = $({});
            this.on = this.emitter.on.bind(this.emitter);
        }
        iniciar() {
            this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
            this.btnProximo.on('click', onBtnProximoClicado.bind(this));

            this.btnEnviarCodigo.on('click', onBtnEnviarCodigoClicado.bind(this));
            this.btnReenviarCodigo.on('click', onBtnReenviarCodigoClicado.bind(this));

			this.passNova.on('keyup', onPassNovaKeyup.bind(this));
			
			this.on('validarResetSenha', validarResetSenha.bind(this));
			this.on('validarCpfRecaptcha', validarCpfRecaptcha.bind(this));
			this.on('validarEmail', validarEmail.bind(this));
			
			//Permite apenas números no CPF
			setInputFilter(this.cpfUser[0], function(value) {
			  return /^-?\d*$/.test(value); 
			});

            exibirEtapa.call(this, this.etapaAtual);
        }
    }

	/* -----  eventos ------ */
	function onBtnAnteriorClicado() {		
		const LOGIN_URL = "/siga/public/app/login";
		
		if (typeof this.etapas[this.etapaAtual - 1] !== 'undefined') {	
			switch (this.etapas[this.etapaAtual - 1].id) {
				case 'cpfResetSenha':		
										
					break;
				case 'emailResetSenha':				
					break;
			}
			atualizarEtapa(this, -1, false);
		} else {
			window.location.href = LOGIN_URL;
		}	
	}
	

	function onBtnProximoClicado() {
		if (this.etapaAtual === 0)
			localizarAcesso.call(this);
					
		if (typeof this.etapas[this.etapaAtual + 1] !== 'undefined') {
			switch (this.etapas[this.etapaAtual + 1].id) {
			case 'cpfResetSenha':											
				break;
			case 'emailResetSenha':
				break;
			}
		} else if (this.etapaAtual + 1 >= this.etapas.length) {
			salvar.call(this);
		}
	}

	function onPassNovaKeyup() {			
		passwordStrength(this.passNova.val());
	}
	
	function onBtnEnviarCodigoClicado() {			
		enviarCodigo.call(this);
	}
	
	function onBtnReenviarCodigoClicado() {			
		enviarCodigo.call(this);
	}
	
	/* ---- ----- */
	

	/*--- comtroles das etapas -----*/
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
			
			this.cpfUser.focus(); 
			habilitarBtnProximo(this);
		} else if (numeroEtapa == 1) {
			this.btnProximo.css('display', 'none');  
			this.btnAnterior.html('<i class="fas fa-long-arrow-alt-left"></i> Anterior');  
			this.emailListContainer.innerHTML = ''; //clear
 
		} else {
			this.btnProximo.css('display', 'inline');  

			this.btnProximo.removeClass('btn-primary').addClass('btn-success');
			this.btnProximo.html('Redefinir Senha  <i class="fas fa-check"></i>');    
			this.btnAnterior.html('<i class="fas fa-long-arrow-alt-left"></i> Anterior');  
			
			this.tokenSenha.focus();

		}
		
		atualizarTituloEtapaTopo.call(this);
		atualizarSpanIndicadorEtapa.call(this, numeroEtapa);
	}	
	
	function atualizarEtapa(form,numeroEtapa) {  		

		form.etapas[form.etapaAtual].style.display = "none";
		form.etapaAtual += numeroEtapa;
		exibirEtapa.call(form, form.etapaAtual);

	}	
	
	function atualizarTituloEtapaTopo() {
		this.tituloPrincipalEtapa.find('.js-titulo-etapa-topo').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
				case 'cpfResetSenha':				
					titulo = 'Localize seu Acesso';	
					break;
				case 'emailResetSenha':		
					titulo = 'Enviar Código';		
					break;
				case 'resetSenha':		
					titulo = 'Defina uma Nova Senha';		
					break;

			}	
			this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;'+titulo+'</span>');					
		}	
	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
	}
	/* ---- ----- */
	
	/*--------- AJAX ------------*/
	//1 - Etapa
	function localizarAcesso() {																			
		if (validar.call(this, this.etapaAtual)){		
			let form = this;				
			$.ajax({
				url: '/siga/public/app/pessoa/usuarios/buscarEmailParcialmenteOculto/'+ this.cpfUser.val(),
			    contentType: 'application/json',
			    type: 'GET',	
				data: {'g-recaptcha-response':grecaptcha.getResponse()},
				beforeSend: onSpinnerMostrar.bind(this),							
		        success: function(result){
					grecaptcha.reset();
					form.jwt.val(result.jwt);
					createRadioEmail(result.emails);
					atualizarEtapa(form, 1);
		        },
		        error: function(result){	
					grecaptcha.reset();
		        	erroRequisicao(form,result.responseText);
		        },
				complete: onSpinnerOcultar.bind(this)	
			});	
		}								
	}
	
	//2 - Etapa
	function enviarCodigo() {	
		if (validar.call(this, this.etapaAtual)){																		
			let form = this;	
			emailSelected = $('input[name="gridRadioEmail"]:checked').val();					
			$.ajax({
				url: '/siga/public/app/usuario/senha/gerar-token-reset',
			    type: 'POST',		
				data: {'cpf':this.cpfUser.val(),'emailOculto':emailSelected,'jwt':this.jwt.val()},
				beforeSend: onSpinnerMostrar.bind(this),							
		        success: function(result){
					//redireciona para etapa seguinte se estiver na lista de email
					if (form.etapaAtual === 1) {
						atualizarEtapa(form, 1);
					}	
					
					if (result.ldapEnable) {
						form.ldap.css('display', 'inline');
						$('#trocarSenhaRede').attr("checked", true);
					} else {
						form.ldap.css('display', 'none');
						$('#trocarSenhaRede').attr("checked", false);
					}
						
						
					sigaModal.alerta("Código de segurança gerado e enviado para o e-mail cadastrado.");
		        },
		        error: function(result){	
					let msgError = result.responseText;
		        	erroRequisicao(form,msgError);
		        },
				complete: onSpinnerOcultar.bind(this)	
			});	
		}								
	}
	
	//3 - Etapa final
	function salvar() {		
		if (validar.call(this, this.etapaAtual)){																	
			let form = this;
			emailSelected = $('input[name="gridRadioEmail"]:checked').val();	
			trocarSenhaRede = $('#trocarSenhaRede').is(':checked');
			$.ajax({
				url: '/siga/public/app/usuario/senha/reset',
			    type: 'POST',
			   	data: {'cpf':this.cpfUser.val(),'token':this.tokenSenha.val(),'senhaNova':this.passNova.val(),'senhaConfirma':this.passConfirmacao.val(),'jwt':this.jwt.val(),'emailOculto':emailSelected,'trocarSenhaRede':trocarSenhaRede},										
				beforeSend: iniciarRequisicao.bind(this),
		        success: function(result){
		        	finalizarRequisicao(form);
		        },
		        error: function(result){	
		        	erroRequisicao(form,result.responseText);
		        },
			});	
		}								
	}
	
	/* controlar andamento requisição */
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
	
	function erroRequisicao(form,errormsg) {
		$(form.etapas[form.etapaAtual]).show();
		form.spanIndicadorEtapa.parent().show();
		form.btnProximo.parent().show();					
		form.spinner.parent().parent().parent().hide();
	
		errormsg = errormsg.replace('<html><head><title>Error</title></head><body>','').replace('</body></html>','');
		
    	sigaModal.alerta(errormsg,false,"Erro");
    	console.log(errormsg);
	}
	
	function finalizarRequisicao(form) {
		
		form.spinner.css('border-color', '#28a745');		
		form.spinner.parent().parent().parent().find('h1').html('Pronto! Sua Senha foi redefinida!<br/><p class="lead">Nunca divulgue sua Senha. Ela é de uso pessoal e intransferível.</p>');
		form.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');	
		form.btnGoToMesa.show();
	
	}	
	/*---- -----*/
	
	/*--- validadores ------*/
	function validar(numeroEtapa) {				
		let retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'cpfResetSenha':	
				this.emitter.trigger('validarCpfRecaptcha', retorno);			
				break;
			case 'emailResetSenha':		
				this.emitter.trigger('validarEmail', retorno);			
				break;
			case 'resetSenha':		
				this.emitter.trigger('validarResetSenha', retorno);	
				break;				
		}
		
		return retorno.resultado;					
	}		
	
	function validarResetSenha(evento, validacao) {	
		let strength = $("#passwordStrength").className;
		if (strength== "strength0" || strength == "strength1" || strength == "strength2") {			
			sigaModal.alerta('Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números.');
			validacao.resultado = false;
			return false;
		}
		let passNova = $("#passNova").val();
		let passConfirmacao = $("#passConfirmacao").val();
		if (passNova != passConfirmacao) {			
			sigaModal.alerta('Repetição da nova senha não confere, favor redigitar.');					
			validacao.resultado = false;
			return false;
		}
		validacao.resultado = true;
		return true;
	}
	
	function validarEmail(evento, validacao) {	
		let emailSelected = $('input[name="gridRadioEmail"]:checked').val();
		if (emailSelected === undefined || emailSelected === "") {			
			sigaModal.alerta('Email para envio do código não foi selecionado.');
			validacao.resultado = false;
			return false;
		}
		validacao.resultado = true;
		return true;
	}
	
	function validarCpfRecaptcha(evento, validacao) {	
		if(this.cpfUser.val() === "") {
			sigaModal.alerta('CPF não informado. Favor inserí-lo.').select(this.cpfUser);		
			validacao.resultado = false;
			return false;
		} 
		
		if (!isCpfValido(this.cpfUser.val())) {
			sigaModal.alerta('CPF informado inválido.').select(this.cpfUser);		
			validacao.resultado = false;
			return false;
		}
		
		if (grecaptcha.getResponse().length == 0) {
			sigaModal.alerta('Verificação de Segurança não efetuada.').select(grecaptcha);		
			validacao.resultado = false;
			return false;
			
		}
		validacao.resultado = true;
		return true;
	}
	
	
	
	function createRadioEmail(list) {
		this.emailListContainer.innerHTML = ''; //clear
		for (var idx in list) {
			$('<div>', {
			    class: 'form-check',
			}).appendTo(this.emailListContainer).append(
				$('<input>', {
				    id: 'gridRadioEmail_'+idx,
					type: 'radio',
					name: 'gridRadioEmail',
				    class: 'form-check-input',
					value: list[idx],
					checked: idx === "0"
				}),
				$('<label>', {
					id: 'gridRadioEmail_'+idx,
				    for: 'gridRadioEmail_'+idx,
					name: 'gridRadioEmail',
				    class: 'form-check-label'
				}).append(list[idx])
			)
	
		}

	}

	return Etapas;	
}());


//inicializa módulo
$(function() {
	var etapas = new SenhaReset.Etapas();
	etapas.iniciar();
});