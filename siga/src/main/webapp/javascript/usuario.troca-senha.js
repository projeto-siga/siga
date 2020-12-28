var Siga = Siga || {};

Siga.TrocaSenhaUsuario = (function() {
	
	function TrocaSenhaUsuario() {
		this.trocaSenhaUsuarioModal = $('#trocaSenhaUsuario');			
		this.trocaSenhaUsuarioBtn = $('.js-troca-senha-usuario-btn');
		this.mensagemErro = $('.js-mensagem-erro');			
		this.loginUsuario = $('#loginUsuario');
		this.cpfInput = $('#cpfUsuario');
		this.senhaAtualInput = $('#senhaAtualUsuario');
		this.novaSenhaInput = $('#novaSenhaUsuario');
		this.confirmacaoNovaSenhaUsuarioInput = $('#confirmacaoNovaSenhaUsuario');		
		this.contInput = $('#cont');				
		this.trocarSenhaRedeUsuarioCheckbox = $('#trocarSenhaRedeUsuario');
		this.containerSenhaAlteradaSucesso = $('.container-senha-alterada-sucesso');		
	}
	
	TrocaSenhaUsuario.prototype.iniciar = function() {
		this.trocaSenhaUsuarioModal.on('shown.bs.modal', onModalInicializado.bind(this));							
		this.cpfInput.on('blur', onCpfInformado.bind(this, checarBotaoSalvar));	
		this.cpfInput.on('input', aplicarMascaraCPF.bind(this, checarBotaoSalvar));
		this.cpfInput.on('change', aplicarMascaraCPF.bind(this, checarBotaoSalvar));		
		this.senhaAtualInput.on('blur', onSenhaAtualInformada.bind(this, checarBotaoSalvar));
		this.senhaAtualInput.on('input', onSenhaAtualInformada.bind(this, checarBotaoSalvar));		
		this.novaSenhaInput.on('blur', onNovaSenhaInformada.bind(this, checarBotaoSalvar));
		this.novaSenhaInput.on('input', onNovaSenhaInformada.bind(this, checarBotaoSalvar));				
		this.confirmacaoNovaSenhaUsuarioInput.on('blur', onConfirmacaoNovaSenhaUsuario.bind(this, checarBotaoSalvar));
		this.confirmacaoNovaSenhaUsuarioInput.on('input', onConfirmacaoNovaSenhaUsuario.bind(this, checarBotaoSalvar));
		this.trocarSenhaRedeUsuarioCheckbox.on('click', onTrocarSenhaRedeUsuarioClicado.bind(this));
		this.trocaSenhaUsuarioBtn.on('click', onTrocaSenhaUsuarioClicado.bind(this));
	}
	
	function onModalInicializado() {
		inicializarCampos.call(this);		
	}
	
	function onCpfInformado(callback) {
		if (!!!this.cpfInput.val() || this.cpfInput.val() == '000.000.000-00') {	
			if(this.cpfInput.val() == '000.000.000-00') {
				this.cpfInput.val('');
			}
			exibirMensagemErroInput(this.cpfInput, 'Favor informar o CPF');
		} else if (isCpfValido(this.cpfInput.val())) {
			removerMensagemErroInput(this.cpfInput);
		}
		
		callback.call(this);
	}
	
	function aplicarMascaraCPF(callback, event) {  
	    cpf = this.cpfInput.val().replace(/([^\d])/g, '');

	    if (event.type == 'change') {
	      while (cpf.length < 11) {
	        cpf = '0' + cpf;
	      }               
	    }
	    cpf = cpf.replace(/^(\d{3})(\d)/, '$1.$2');
	    cpf = cpf.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
	    cpf = cpf.replace(/\.(\d{3})(\d)/, '.$1-$2');            
	    this.cpfInput.val(cpf);     
	                   
	    if (cpf.length == 14) {
	      if(isCpfValido(cpf)) {
	    	  removerMensagemErroInput(this.cpfInput);     
	      } else {    	  
	    	  exibirMensagemErroInput(this.cpfInput, 'CPF informado está inválido');                       
	      }
	    }
	    
	    callback.call(this);
	}
	
	function isCpfValido(cpf) {	
	    cpf = cpf.replace(/[^\d]+/g,'');	
	    if(cpf == '') return false;	
	    // Elimina CPFs invalidos conhecidos	
	    if (cpf.length != 11 || 
	        cpf == "00000000000" || 
	        cpf == "11111111111" || 
	        cpf == "22222222222" || 
	        cpf == "33333333333" || 
	        cpf == "44444444444" || 
	        cpf == "55555555555" || 
	        cpf == "66666666666" || 
	        cpf == "77777777777" || 
	        cpf == "88888888888" || 
	        cpf == "99999999999")
	      return false;		
	    // Valida 1o digito	
	    add = 0;	
	    for (i=0; i < 9; i ++)		
	      add += parseInt(cpf.charAt(i)) * (10 - i);	
	    rev = 11 - (add % 11);	
	    if (rev == 10 || rev == 11)		
	      rev = 0;	
	    if (rev != parseInt(cpf.charAt(9)))		
	      return false;		
	    // Valida 2o digito	
	    add = 0;	
	    for (i = 0; i < 10; i ++)		
	      add += parseInt(cpf.charAt(i)) * (11 - i);	
	    rev = 11 - (add % 11);	
	    if (rev == 10 || rev == 11)	
	      rev = 0;	
	    if (rev != parseInt(cpf.charAt(10)))
	      return false;		
	    return true;   
	}
	
	function onSenhaAtualInformada(callback) {
		if (!!!this.senhaAtualInput.val()) {							
			exibirMensagemErroInput(this.senhaAtualInput, 'Favor informar a senha atual');
		} else {
			removerMensagemErroInput(this.senhaAtualInput);
		}
		
		callback.call(this);
	}
	
	function onNovaSenhaInformada(callback) {
		if (!!!this.novaSenhaInput.val()) {							
			exibirMensagemErroInput(this.novaSenhaInput, 'Favor informar a nova senha');
		} else if (this.novaSenhaInput.val().length <= 5) {
			exibirMensagemErroInput(this.novaSenhaInput, 'Favor utilize uma senha com pelo menos 6 caracteres');		
		} else if (this.confirmacaoNovaSenhaUsuarioInput.val() && this.confirmacaoNovaSenhaUsuarioInput.val() !== this.novaSenhaInput.val()) {
			exibirMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput, 'Senhas não conferem');
			removerMensagemErroInput(this.novaSenhaInput);
		} else {
			removerMensagemErroInput(this.novaSenhaInput);
			removerMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput);
		}
		
		callback.call(this);
	}
	
	function onConfirmacaoNovaSenhaUsuario(callback) {
		if (!!!this.confirmacaoNovaSenhaUsuarioInput.val()) {							
			exibirMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput, 'Favor confirmar a nova senha');
		} else if (this.novaSenhaInput.val() && this.confirmacaoNovaSenhaUsuarioInput.val() !== this.novaSenhaInput.val()) {
			exibirMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput, 'Senhas não conferem');
		} else {
			removerMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput);
		}
		
		callback.call(this);
	}	
	
	function onTrocarSenhaRedeUsuarioClicado() {
		this.trocarSenhaRedeUsuarioCheckbox.parent().find('label').css('color', 'black');				
	}
	
	function checarBotaoSalvar() {
		var temInputVazio = false;
		this.trocaSenhaUsuarioModal.find('form').find('input[type=text], input[type=password]').each(function() {
			if (!!!this.value) {
				temInputVazio = true;
				return false;
			}
		});
		var temInputInvalido = this.trocaSenhaUsuarioModal.find('form').find('.is-invalid').length > 0;
		
		if (temInputVazio || temInputInvalido) {
			bloquearBotaoSalvar.call(this);
		} else {
			liberarBotaoSalvar.call(this);
		}
	}
			
	function onTrocaSenhaUsuarioClicado(event) {
		event.preventDefault();
		
		var username = this.loginUsuario.val();
		var password = this.novaSenhaInput.val();
		var cont = this.contInput.val();		
				
		var dados = {
				'usuario': {
					'nomeUsuario': username,
					'cpf': this.cpfInput.val().replace(/[^\d]+/g,''), 
					'senhaAtual': this.senhaAtualInput.val(), 
					'senhaNova': password, 
					'senhaConfirma': this.confirmacaoNovaSenhaUsuarioInput.val(),					
					'cont': cont,
					'trocarSenhaRede': (this.trocarSenhaRedeUsuarioCheckbox.is(':checked') ? 'on' : 'off')
				}
			};		

		$.ajax({
			url: this.trocaSenhaUsuarioModal.find('form').attr('action'),
			method: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(dados),
			beforeSend: inicializarSpinner.bind(this),
			success: onTrocaSenhaConcluida.bind(this, username, password, cont),
			error: onTrocaSenhaErro.bind(this),
			complete: finalizarSpinner.bind(this)
		});					
	}
	
	function onTrocaSenhaConcluida(username, password, cont, resultado) {
		if (resultado.usuarioAction.erros) {			
			var erros = resultado.usuarioAction.erros;
			var primeiroInputComErro;
			removerMensagemErroPainel.call(this);
						
			erros.forEach(function(erro, index) {
				if (erro.campo == 'CPF') {					
					adicionarMensagemErroPainel.call(this, erro.mensagem);
					exibirMensagemErroInput(this.cpfInput, erro.mensagem);
					primeiroInputComErro = this.cpfInput;
				}
				
				if (erro.campo == 'senhaAtual') {					
					adicionarMensagemErroPainel.call(this, erro.mensagem);
					exibirMensagemErroInput(this.senhaAtualInput, erro.mensagem);
					if (!!!primeiroInputComErro) primeiroInputComErro = this.senhaAtualInput;
				}
				
				if (erro.campo == 'senhaNova') {					
					adicionarMensagemErroPainel.call(this, erro.mensagem);
					this.novaSenhaInput.addClass('is-invalid');
					exibirMensagemErroInput(this.novaSenhaInput, erro.mensagem);
					if (!!!primeiroInputComErro) primeiroInputComErro = this.novaSenhaInput;
				}
				
				if (erro.campo == 'senhaConfirma') {					
					adicionarMensagemErroPainel.call(this, erro.mensagem);
					exibirMensagemErroInput(this.confirmacaoNovaSenhaUsuarioInput, erro.mensagem);
					if (!!!primeiroInputComErro) primeiroInputComErro = this.confirmacaoNovaSenhaUsuarioInput;
				}								
				
				if (erro.campo == 'trocarSenhaRede') {					
					adicionarMensagemErroPainel.call(this, erro.mensagem);
					this.trocarSenhaRedeUsuarioCheckbox.parent().find('label').css('color', '#dc3545');									
				}
				
			}.bind(this));
			
			exibirMensagemErroPainel.call(this);	
			checarBotaoSalvar.call(this);
			if (primeiroInputComErro) primeiroInputComErro.focus();
		} else {
			removerMensagemErroPainel.call(this);			
			exibirMensagemSucesso.call(this);
			
			setTimeout(function() { 
				logar(username, password, cont); 
			}, 3000);			
		}					
	}
	
	function onTrocaSenhaErro() {
		this.mensagemErro.find('.js-mensagem-erro-textos').html('<div><i class="fa  fa-exclamation-circle"></i> Erro ao efetuar o processo de trocar a senha</div>');
		this.mensagemErro.removeClass('hidden');
	}
	
	function liberarBotaoSalvar() {
		this.trocaSenhaUsuarioBtn.removeAttr('disabled');
	}
	
	function bloquearBotaoSalvar() {
		this.trocaSenhaUsuarioBtn.attr('disabled', 'disabled');
	}
	
	function adicionarMensagemErroPainel(mensagem) {		
		this.mensagemErro.find('.js-mensagem-erro-textos').append('<div><i class="fa  fa-exclamation-circle"></i> ' + mensagem + '</div>');		
	}
	
	function exibirMensagemErroPainel() {
		this.mensagemErro.removeClass('hidden');
	}
	
	function removerMensagemErroPainel() {
		this.mensagemErro.find('.js-mensagem-erro-textos').html('');
		this.mensagemErro.addClass('hidden');
	}
	
	function exibirMensagemErroInput(input, mensagem) {
		input.parent().find('.invalid-feedback').text(mensagem);
		input.addClass('is-invalid');		
	}
	
	function removerMensagemErroInput(input) {		
		input.parent().find('.invalid-feedback').text('');
		input.removeClass('is-invalid');						
	}	
	
	function inicializarCampos() {
		removerMensagemErroPainel.call(this);
		this.trocaSenhaUsuarioModal.find('form').find('#cpfUsuario, #senhaAtualUsuario, #novaSenhaUsuario, #confirmacaoNovaSenhaUsuario').val('');
		this.trocaSenhaUsuarioModal.find('form').find('.invalid-feedback').text('');
		this.trocaSenhaUsuarioModal.find('form').find('.is-invalid').removeClass('is-invalid');
		this.cpfInput.focus();
	}
	
	function exibirMensagemSucesso() {
		this.containerSenhaAlteradaSucesso.css({'visibility':'visible', 'opacity': '1'});
	}
	
	function inicializarSpinner() {
		this.trocaSenhaUsuarioBtn.html('<span class="spinner-border spinner-border-sm spinner-botao-salvar" role="status" aria-hidden="true"></span>');
		this.trocaSenhaUsuarioBtn.attr('disabled', 'disabled');
	}
	
	function finalizarSpinner() {
		this.trocaSenhaUsuarioBtn.html('Salvar');
		this.trocaSenhaUsuarioBtn.removeAttr('disabled');
	}		
	
	function logar(username, password, cont) {
		var formularioLogin = document.getElementById('formLogin');
		$(formularioLogin).append('<input type="hidden" id="cont"/>');
		
		formularioLogin.elements["username"].value = username;
		formularioLogin.elements["password"].value = password;
		formularioLogin.elements["cont"].value = cont;
		formularioLogin.submit();
	}
	
	return TrocaSenhaUsuario;
	
}());

$(function() {
	var trocaSenhaUsuario = new Siga.TrocaSenhaUsuario();
	trocaSenhaUsuario.iniciar();	
});