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


            exibirEtapa.call(this, this.etapaAtual);
        }
    }
	
	
	
	function onValidar(evento, validacao) {			
		return true;		
	}

	function onBtnAnteriorClicado() {		
		
		if (typeof this.etapas[this.etapaAtual - 1] !== 'undefined') {	
			switch (this.etapas[this.etapaAtual - 1].id) {
				case 'cpfResetSenha':		
										
					break;
				case 'emailResetSenha':				
					break;
			}
			atualizarEtapa(this, -1, false);
		} else {
			window.location.href = "/siga/public/app/login";
		}
		
			
	}
	

	function onBtnProximoClicado(podeValidar) {
		if (this.etapaAtual === 0)
			localizarAcesso.call(this);
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
	
	function enviarCodigo() {																			
		var form = this;	
		emailSelected = $('input[name="gridRadioEmail"]:checked').val();					
		$.ajax({
			url: '/siga/public/app/usuario/senha/gerar-token-reset',
		    type: 'POST',		
			data: {'cpf':this.cpfUser.val(),'emailOculto':emailSelected,'jwt':this.jwt.val()},
			beforeSend: onSpinnerMostrar.bind(this),							
	        success: function(result){
				if (form.etapaAtual === 1)
					atualizarEtapa(form, 1);
				sigaModal.alerta("Código de segurança gerado e enviado para o e-mail cadastrado.");
				
	        },
	        error: function(result){	
				let msgError = result.responseText;
	        	erroRequisicao(form,msgError);
	        },
			complete: onSpinnerOcultar.bind(this)	
		});									
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
	
	function salvar() {			
		if (validarCampos.call(this, this.etapaAtual)){																	
			var form = this;
			emailSelected = $('input[name="gridRadioEmail"]:checked').val();				
			$.ajax({
				url: '/siga/public/app/usuario/senha/reset',
			    type: 'POST',
			   	data: {'cpf':this.cpfUser.val(),'token':this.tokenSenha.val(),'senhaNova':this.passNova.val(),'senhaConfirma':this.passConfirmacao.val(),'jwt':this.jwt.val(),'emailOculto':emailSelected},										
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
	
	function localizarAcesso() {																			
		let form = this;	
		
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
					break;
				case 'emailResetSenha':		
					titulo = 'Gerar e Enviar Código';		
					break;
				case 'resetSenha':		
					titulo = 'Defina uma nova Senha';		
					break;

			}	
			this.tituloPrincipalEtapa.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;'+titulo+'</span>');					
		}	
		

	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
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
	
	
	function validateUsuarioForm(form) {
		var s = document.getElementById("passwordStrength").className;
		if (s == "strength0" || s == "strength1" || s == "strength2") {			
			sigaModal.alerta('Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números.');
			return false;
		}
		var p1 = document.getElementById("pass").value;
		var p2 = document.getElementById("pass2").value;
		if (p1 != p2) {			
			sigaModal.alerta('Repetição da nova senha não confere, favor redigitar.');					
			return false;
		}
		return true;
	}

	function passwordStrength(password) {
		var desc = new Array();
		desc[0] = "Inaceitável";
		desc[1] = "Muito Fraca";
		desc[2] = "Fraca";
		desc[3] = "Razoável";
		desc[4] = "Boa";
		desc[5] = "Forte";
		var score = 0;

		//if password bigger than 6 give 1 point
		if (password.length >= 6)
			score++;

		//if password has both lower and uppercase characters give 1 point      
		if ((password.match(/[a-z]/)) && (password.match(/[A-Z]/)))
			score++;

		//if password has at least one number give 1 point
		if ((password.match(/[a-z]/) || password.match(/[A-Z]/))
				&& (password.match(/\d+/)))
			score++;

		//if password has at least one special caracther give 1 point
		if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/))
			score++;

		//if password bigger than 12 give another 1 point
		if (password.length >= 12)
			score++;

		//mininum requirements to be accepted by the AD
		if (score > 2
				&& (password.length < 6 || !password.match(/[a-z]/)
						|| !password.match(/[A-Z]/) || !password.match(/\d+/)))
			score = 2;

		document.getElementById("passwordDescription").innerHTML = desc[score];
		document.getElementById("passwordStrength").className = "strength"
				+ score;
	}

	return Etapas;	
}());



$(function() {
	var etapas = new SenhaReset.Etapas();
	etapas.iniciar();
});