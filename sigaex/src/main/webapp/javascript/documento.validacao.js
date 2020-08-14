var funcoesCallback = [];

function adicionarFuncaoParaValidacao(elemento, funcao) {
	if (!(elemento instanceof jQuery)) {
		elemento = $(elemento);
	}
	
	funcoesCallback.push({ elemento, funcao });
}

function executarFuncoesCallback() {
	funcoesCallback.forEach(function(item) {		
		if (item.elemento.length > 0 && typeof item.funcao === 'function') {
			var obrigatorio = $('input[type=hidden][name=obrigatorios][value="' + item.elemento.attr('name') + '"]');
			
			if (obrigatorio.length == 0 || (obrigatorio.length > 0 && !item.elemento.hasClass('is-invalid'))) {
				executarFuncao(item.elemento, item.funcao);
			}															
		}					
	});
}

function executarFuncao(elemento, funcao) {
	try {
		var resultado = funcao.call();
		
		if (typeof resultado === 'string' && resultado.trim() != '') {
			aplicarErro(elemento, resultado);				
		} else {
			removerErro(elemento);
		}
	} catch (err) {
		console.log('Não foi possível executar a função: \n' + funcao + '\n Erro: ' + err.message);
	}
}

function criarIds() {
	$('#frm').find('[data-criar-id="true"]').each(function(index, value) {				
		if (value.id === '') {
			var input = $(value);
			var label = input.parent().find('[data-nome-ref="' + value.name + '"]');
			var id = value.name.concat(index);
			
			input.attr('id', id);
			label.attr('for', id);			
		}		
	});
}

function validarCamposEntrevista() {
	var obrigatorios = $('#frm').find('[name=obrigatorios]');		
	
	obrigatorios.each(function() {
		var elemento = $('[name="' + this.value + '"]');
		var valor = elemento.val();
		var temCpf = !!elemento.data('formatarCpf');
		var temCnpj = !!elemento.data('formatarCnpj');
		
		if (valor == null || valor === '' || !valor || /^\s*$/.test(valor)) {
			var tituloLabel = obterLabel(elemento);				
			var mensagem = 'Favor preencher o campo';
			
			if (tituloLabel.text().length > 0) {								
				if (temCpf || temCnpj) {
					mensagem = mensagem.concat(' '.concat(tituloLabel.text().trim()));					
				} else {
					mensagem = mensagem.concat(' '.concat(tituloLabel.text().trim().toLowerCase()));					
				}
				mensagem = mensagem.replace(':', ' ');
			}
			
			aplicarErro(elemento, mensagem);						
		} else {
			removerErro(elemento);																						
		}
							
		if (valor.length > 0 || valor || !/^\s*$/.test(valor)) {			
			if (elemento.hasClass('campoData')) {
				validarData(elemento);				
			}
						
			if (elemento.hasClass('campoHoraMinuto')) {
				validarHoraMinuto(elemento);				
			}
											
			if (temCpf) {
				validarCpf(elemento);																			
			} 
											
			if (temCnpj) {
				validarCnpj(elemento);
			}							
		}
						
		validarSelect(elemento);							
		validarInputRadioECheckbox(elemento);
		
	});	
	
	validarDocumentoCapturado();	
	executarFuncoesCallback();
}

function validarData(elemento) {
	var resultado = verifica_data(elemento[0], false, true);
	
	if (typeof resultado === 'string' && resultado.length > 0) {
		aplicarErro(elemento, resultado.concat('. Favor preencher neste formato ex.: ' + $.datepicker.formatDate('dd/mm/yy', new Date())));
	}
	
	if (typeof resultado === 'boolean' && !resultado) {
		aplicarErro(elemento, 'Data inválida');
	}
}

function validarHoraMinuto(elemento) {
	var resultado = verifica_hora(elemento[0], false, true);
	
	if (typeof resultado === 'string' && resultado.length > 0) {
		var horaMinutoAtual = new Date();
		
		aplicarErro(elemento, resultado.concat('. Favor preencher neste formato ex.: ' + horaMinutoAtual.getHours() + ':' + horaMinutoAtual.getMinutes()));
	}
	
	if (typeof resultado === 'boolean' && !resultado) {
		aplicarErro(elemento, 'Data inválida');
	}
}

function validarCpf(elemento) {
	if (!isCpfValido(elemento.val())) {
		aplicarErro(elemento, 'Favor informar um CPF válido');
	}		
}

function validarCnpj(elemento) {
	if (!isCnpjValido(elemento.val())) {
		aplicarErro(elemento, 'Favor informar um CNPJ válido');
	}				
}

function validarSelect(elemento) {
	if (elemento[0].tagName === 'SELECT') {
		var option = elemento.find(':selected');
		if (option.length > 0 && (option.attr('id') === 'opcaoNeutra' || option.val() === '')) {
			aplicarErro(elemento, 'Favor selecione uma opção');
		}
	}
}

function validarInputRadioECheckbox(elemento) {
	if(elemento[0].tagName === 'INPUT' && (elemento[0].type === 'radio' || elemento[0].type === 'checkbox')) {
		var checado = $('input[name="' + elemento.attr('name') + '"]:checked');
		if (checado.length == 0) {				
			aplicarErro(elemento, 'Favor selecionar alguma opção');									
		}
	}
}

function validarDocumentoCapturado() {
	var origem = document.getElementsByName('exDocumentoDTO.idTpDoc')[0].value;
	var id = document.getElementsByName('exDocumentoDTO.id')[0].value;
	if ((origem == 4 || origem == 5) && !id) {
		var arquivo = document.getElementsByName('arquivo')[0];
		if (!arquivo.classList.contains('is-invalid')) {
			if (arquivo.value == "") {
				var mensagem = 'Documento capturado não pode ser gravado sem que seja informado o arquivo PDF';
				aplicarErro($(arquivo), mensagem);					
			} else {
				removerErro($(arquivo));
			}						
		}		
	}
}

function aplicarErro(elemento, mensagem) {		
	aplicarElementoInvalido(elemento);
	aplicarLabelInvalido(elemento);	
	aplicarMensagemErro(elemento, mensagem);									
}

function removerErro(elemento) {			
	if (elemento.hasClass('is-invalid')) {		
		removerElementoInvalido(elemento);						
		removerLabelInvalido(elemento);
	}
}

function aplicarElementoInvalido(elemento) {
	elemento.addClass('is-invalid');
}

function aplicarLabelInvalido(elemento) {
	var tituloLabel = obterLabel(elemento);
	var nome = elemento.attr('name');
	
	if (tituloLabel.length > 0) {
		if (!tituloLabel.hasClass('titulo-'.concat(nome))) {
			tituloLabel.addClass('titulo-'.concat(nome));
		}					
		tituloLabel.css('color', '#dc3545');
	}
}

function removerElementoInvalido(elemento) {
	elemento.removeClass('is-invalid');
}

function removerLabelInvalido(elemento) {
	var nome = elemento.attr('name');	
	var tituloLabel = $('.titulo-'.concat(nome));
	
	if (tituloLabel.length == 0) {
		tituloLabel = obterLabel(elemento);
	}
	
	if (tituloLabel.length > 0) {
		tituloLabel.removeClass('titulo-'.concat(nome));
		tituloLabel.css('color', 'black');
	}	
}

function aplicarMensagemErro(elemento, mensagem) {
	var mensagemDiv = $('.invalid-feedback-'.concat(elemento.attr('name')));
	
	if (mensagemDiv.length > 0) {
		if (elemento[0].type === 'radio' || elemento[0].type === 'checkbox') {
			mensagemDiv.text('');
			mensagemDiv.last().text(mensagem);
		} else {
			mensagemDiv.text(mensagem);
		}				
	}
}

function obterLabel(elemento) {
	var tituloLabel = $('label[for="' + elemento.attr('name') + '"]');
	
	if (tituloLabel.length == 0) {
		var tag = elemento.parent().prev();		
		
		if (tag.length > 0 && (tag[0].tagName === 'LABEL' || tag[0].tagName === 'SPAN' || tag[0].tagName === 'B')) {
			tituloLabel = tag;
		} else {
			tag = elemento.prev();
			
			if (tag.length > 0 && (tag[0].tagName === 'LABEL' || tag[0].tagName === 'SPAN' || tag[0].tagName === 'B')) {
				tituloLabel = tag;
			}				
		}	
		
		if (tag.length > 0 && tag.data('toggle') === 'tooltip') {
			tag = tag.prev();
			
			if (tag.length > 0 && (tag[0].tagName === 'LABEL' || tag[0].tagName === 'SPAN' || tag[0].tagName === 'B')) {
				tituloLabel = tag;
			}				
		}
	}	
	
	return tituloLabel;
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

function isCnpjValido(cnpj) {
    cnpj = cnpj.replace(/[^\d]+/g,'');

    if(cnpj == '') return false;

    if (cnpj.length != 14)
      return false;

    // Elimina CNPJs invalidos conhecidos
    if (cnpj == "00000000000000" || 
        cnpj == "11111111111111" || 
        cnpj == "22222222222222" || 
        cnpj == "33333333333333" || 
        cnpj == "44444444444444" || 
        cnpj == "55555555555555" || 
        cnpj == "66666666666666" || 
        cnpj == "77777777777777" || 
        cnpj == "88888888888888" || 
        cnpj == "99999999999999")
      return false;

    // Valida DVs
    tamanho = cnpj.length - 2
      numeros = cnpj.substring(0,tamanho);
    digitos = cnpj.substring(tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
        pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0))
      return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0,tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
        pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(1))
      return false;

    return true;
}
  
$(function() {
	criarIds();			
});