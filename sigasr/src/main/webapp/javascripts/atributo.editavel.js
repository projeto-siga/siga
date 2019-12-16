/**
 * @class atributoEditavel
 * 
 * exemplo @param propriedades
 * 		var propriedades = {
			id: idAtributo,
			valor: valorAtributo,
			tipo: tipoAtributo,
			valoresPreDefinidos: preDefinidoSet,
			elemento: event.target,
			nome: 'atributoSolicitacao',
			urlDestino: '${linkTo[SolicitacaoController].editarAtributo}'
		};
 **/
var AtributoEditavel = function (propriedades) {
	this.propriedades = propriedades;
}

/**
 * metodos
 **/
AtributoEditavel.prototype = {
	constructor: AtributoEditavel,
	
	iniciar: function() {
		AtributoEditavel.ELEMENTO_EDITAVEL = this.getElementoComAtributoEditavel();
		AtributoEditavel.FORM_EDITAVEL = this.getForm();
	},
	
	getForm: function() {
		var form = new FormEditavel(this.propriedades, AtributoEditavel.ELEMENTO_EDITAVEL);
		return form;
	},
	
	getElementoComAtributoEditavel: function() {
		return $(this.propriedades.elemento).closest(".atributo-editavel");
	},
	
	editar: function() {
		this.iniciar();
		AtributoEditavel.ELEMENTO_EDITAVEL.hide().after(AtributoEditavel.FORM_EDITAVEL.construir());
		AtributoEditavel.FORM_EDITAVEL.definirAlturaDivBotao();
	},
	
	excluir: function() {
		var elementoEditavel = this.getElementoComAtributoEditavel();
		elementoEditavel.block(paramToBlock);

		$.ajax({
            url: this.propriedades.urlDestino,
            type: "GET"
        }).fail(function(jqXHR, textStatus, errorThrown){
			elementoEditavel.unblock();
			elementoEditavel.parent().remove();
        }).done(function(data, textStatus, jqXHR ){
			elementoEditavel.unblock();
			elementoEditavel.parent().remove();
        });
	},
	
	toString: function() {
		console.log(this.propriedades.id + ", " + this.propriedades.valor + ", " + this.propriedades.tipo + ", " + this.propriedades.valoresPreDefinidos +
				", " + $(this.propriedades.elemento).text());
	}
}

AtributoEditavel.FORM_EDITAVEL = "";
AtributoEditavel.ELEMENTO_EDITAVEL = "";


/**
 * @class formEditavel
 **/
var FormEditavel = function (propriedades, elementoEditavel) {
	this.propriedades = propriedades;
	this.elementoEditavel = elementoEditavel;
	
	this.form = "<form class='form-editavel' action='#' method='post' enctype='multipart/form-data'>" +	
						"<div class='div-campo-editavel' style='float: left; margin-right: 11px;'></div>" +
						"<div class='botao-editavel'></div>" +
				"</form>";
	
	this.botao = "<input type='submit' class='ok ui-state-default ui-corner-all' style='padding:.2em 1em; margin-right: 5px;' value='ok'/>" +
				 "<input type='button' class='cancelar ui-state-default ui-corner-all' style='padding:.2em 1em;' value='cancelar' />";
}

/**
 * metodos
 **/
FormEditavel.prototype = {
	constructor: FormEditavel,
	
	construir: function() {
		this.iniciar();
		this.inserirCampoEditavel(this.propriedades);
		this.inserirCampoHidden();
		this.inserirBotao();
		
		return this.form;
	},
	
	iniciar: function() {
		var formJquery = $(this.form);
		this.form = formJquery;
	},
	
	inserirBotao: function() {
		var divBotao = this.form.find('.botao-editavel');
		divBotao.append(this.botao);
		this.inserirAcaoNoBotao();
	}, 
	
	inserirAcaoNoBotao: function() {
		this.acaoOk();
		this.acaoCancelar();
	},
	
	acaoOk: function() {
		var self = this;
		self.form.find('.botao-editavel > .ok').click(function(event) {
			event.preventDefault();
			var validador = new Validador(self.getCampoEditavel(), self.propriedades.tipo);
			validador.mensagemHandler();
			if (!validador.isCampoPreenchido()) {
				validador.getMensagemCampoNaoPreenchido();
				return;
			}
			self.form.block(paramToBlock);
			
            $.ajax({
                url: self.propriedades.urlDestino, self.form.serialize(),
                type: "POST"
            }).done(function(data, textStatus, jqXHR ){
				self.elementoEditavel.find('span.valor-atributo').text(response);
				self.elementoEditavel.show();
				self.form.unblock();
				self.form.remove();
            });
		});
	},
	
	acaoCancelar: function() {
		var self = this;
		this.form.find('.botao-editavel > .cancelar').click(function() {
			self.elementoEditavel.show();
			self.form.remove();
		});
	},
		
	inserirCampoHidden: function() {
		var nameDoInput = this.propriedades.nome.split(".")[0] + '.id';
		this.inserirCampoEditavel({tipo: 'HIDDEN', nome: nameDoInput, valor: this.propriedades.id});
	},
	
	inserirCampoEditavel: function(propriedades) {
		var divComCampoEditavel = this.form.find('.div-campo-editavel');
		var campoEditavel = this.construirCampoEditavel(propriedades);
		divComCampoEditavel.append(campoEditavel);
	},
	
	construirCampoEditavel: function(propriedades) {
		var campo = new CampoEditavel(propriedades); 
		return campo.construir();
	},
	
	getCampoEditavel: function() {
		var divComCampoEditavel = this.form.find('.div-campo-editavel');
		var campoEditavel = divComCampoEditavel.find('.campo-editavel').first();
		return campoEditavel;
	},
	
	definirAlturaDivBotao: function() {
		var divBotao = this.form.find('.botao-editavel');
		var campoEditavel = this.getCampoEditavel();
		divBotao.css({'height': campoEditavel.height() + 15 + 'px'});
	},
}

/**
 * @class campoEditavel
 * constroi o campo de acordo com os tipos existentes [DATA, TEXTO, NUM_INTEIRO...]
 **/
var CampoEditavel = function (propriedades) {
	this.campoFactory =  new CampoEditavelFactory(propriedades).getInstancia();
}

/**
 * metodos
 **/
CampoEditavel.prototype = {
	constructor: CampoEditavel,
	
	construir: function() {
		this.campoFactory.iniciar();
		this.campoFactory.inserirParametros();
		this.campoFactory.inserirNomeEValor();
		this.campoFactory.inserirMascara();
	
		return this.campoFactory.campo;
	}
}

/**
 * @class campoEditavelFactory
 * define o tipo de campo que sera colocado dentro da @class formEditavel atraves da @class campoEditavel
 **/
var CampoEditavelFactory = function(propriedades) {
	this.propriedades = propriedades;
	this.input 		= "<input class='campo-editavel' />";
	this.textarea 	= "<textarea class='campo-editavel'></texarea>";
	this.select 	= "<select class='campo-editavel'></select>";
}

CampoEditavelFactory.prototype = {
	constructor: CampoEditavelFactory,
	
	getInstancia: function() {
		if (this.propriedades.tipo === 'TEXTO')
			return new Texto(this.propriedades, this.input);
		if (this.propriedades.tipo === 'NUM_INTEIRO' || this.propriedades.tipo === 'NUM_DECIMAL' || this.propriedades.tipo === 'HORA')
			return new Numero(this.propriedades, this.input);
		if (this.propriedades.tipo === 'VL_PRE_DEFINIDO')
			return new ValorPreDefinido(this.propriedades, this.select);
		if (this.propriedades.tipo === 'HIDDEN')
			return new Hidden(this.propriedades, this.input);
		if (this.propriedades.tipo === 'TEXT_AREA')
			return new Textarea(this.propriedades, this.textarea);
		if (this.propriedades.tipo === 'DATA')
			return new Data(this.propriedades, this.input);
	}
}

/**
 * @class campoEditavelAbstract
 * contem os comportamentos comuns entre as diversas @class que existem para cada tipo de campo
 **/
var CampoEditavelAbstract = function(propriedades, campo) {
	this.propriedades = propriedades;
	this.campo = campo;
}

CampoEditavelAbstract.prototype = {
	constructor: CampoEditavelAbstract,
		
	iniciar: function() {
		var campoJquery = $(this.campo);
		this.campo = campoJquery;
	},
	
	inserirNomeEValor: function() {
		this.campo.prop("name", this.propriedades.nome)
		 		  .prop("value", this.propriedades.valor);
	},

	inserirAtributos: function(arrayComParamentros) {
		var self = this;
		arrayComParamentros.forEach(function(elemento) {
			self.campo.prop(elemento.key, elemento.value);
		});
	},
	
	inserirOpcoes: function() {
		var self = this;
		var arrayComValores = this.propriedades.valoresPreDefinidos.slice(1, -1).split(",");
		arrayComValores.forEach(function(elemento) {
			self.campo.append($('<option>', {value: elemento.trim()})
				 .text(elemento.trim())); 
		});
	},
	
	inserirMascara: function() {
		var validador = new Validador(this.campo, this.propriedades.tipo);
		var nomeFuncao = validador.getNomeFuncaoParaInserirMascara();
		if (typeof validador[nomeFuncao] == 'function')
			validador[nomeFuncao]();
	}
}


var Texto = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
	this.tipo = {key: 'type', value: 'text'};
	this.tamanho = {key: 'size', valaue: '70'};
	this.maximo = {key: 'maxlength', value: '255'};
}

Texto.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

Texto.prototype.constructor = Texto;
	
Texto.prototype.getParametros = function() {
	return new Array(this.tipo, this.tamanho, this.maximo);
};
	
Texto.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};


var Numero = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
	this.tipo = {key: 'type', value: 'text'};
	this.maximo = {key: 'maxlength', value: '9'};
}

Numero.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

Numero.prototype.constructor = Numero;
	
Numero.prototype.getParametros = function() {
	return new Array(this.tipo, this.maximo);
};
	
Numero.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};


var ValorPreDefinido = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
}

ValorPreDefinido.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

ValorPreDefinido.prototype.constructor = ValorPreDefinido;
	
ValorPreDefinido.prototype.inserirParametros = function() {
	this.inserirOpcoes();
};


var Textarea = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
	this.coluna = {key: 'cols', value: '85'};
	this.linha = {key: 'rows', value: '5'};
	this.maximo = {key: 'maxlength', value: '255'};
}

Textarea.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

Textarea.prototype.constructor = Textarea;
	
Textarea.prototype.getParametros = function() {
	return new Array(this.coluna, this.linha, this.maximo);
};
	
Textarea.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};


var Data = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
	this.tipo = {key: 'type', value: 'text'};
	this.id = {key: 'id', value: propriedades.nome.replace('.','')};
}

Data.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

Data.prototype.constructor = Data;
	
Data.prototype.getParametros = function() {
	return new Array(this.tipo, this.id);
};
	
Data.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
	this.inserirDatepicker();
};

Data.prototype.inserirDatepicker = function() {
	this.campo.datepicker({
	    dateFormat: 'dd/mm/yy',
	    monthNames: ["Janeiro", "Fevereiro", "Mar\u00E7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
	    dayNamesMin: ["Dom","Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
		firstDay: 0
	});
};


var Hidden = function(propriedades, campo) {
	CampoEditavelAbstract.call(this, propriedades, campo);
	this.tipo = {key: 'type', value: 'hidden'};
}

Hidden.prototype = new CampoEditavelAbstract(this.propriedades, this.campo); 

Hidden.prototype.constructor = Hidden;
	
Hidden.prototype.getParametros = function() {
	return new Array(this.tipo);
};
	
Hidden.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};


/**
 * @class validador
 */
var Validador = function(campo, tipo) {
	this.campo = campo;
	this.tipo = tipo;
}
/**
 * metodos
 **/
Validador.prototype = {
	constructor: Validador,
	
	getNomeFuncaoParaInserirMascara: function() {
		return 	'inserirMascara_' + this.tipo;
	},
	
	inserirMascara_HORA: function() {
		var self = this;
		this.campo.mask("99:99", {completed: function() {
			self.validar_HORA(this.val());
		}});
	},
	
	validar_HORA: function(valor) {
		var tempo = valor.split(':');
		var hora = tempo[0];
		var minuto = tempo[1];
		this.removerMensagem();
		if (hora > 23 || minuto > 59)
			this.mostrarMensagem('Horário inválido para o padrão 24h'); 
	},
	
	inserirMascara_NUM_INTEIRO: function() {
		this.campo.mask('9?99999999', {placeholder: ' '});
	},
	
	inserirMascara_NUM_DECIMAL: function() {
		$.mask.definitions['d'] = '[0-9,]';
		this.campo.mask('9?dddddddd', {placeholder: ' '});
	},
	
	inserirMascara_DATA: function() {
		this.campo.mask('99/99/9999');
	},
	
	isCampoPreenchido: function() {
		var valor = this.campo.val();
		return valor.length > 0;
	},
	
	getMensagemCampoNaoPreenchido: function() {
		this.mostrarMensagem('Atributo não informado');
	},
	
	mostrarMensagem: function(msg) {
		var span = $('<span>'); 
		span.addClass('error')
			.css({'margin-left':'10px', 'color':'red'})
			.text(msg)
			.insertAfter(this.campo);
	},
	
	mensagemHandler: function() {
		var self = this;
		self.campo.focus(function() {
			event.stopPropagation();
			self.removerMensagem();
		});
	},
	
	removerMensagem: function() {
		this.campo.siblings('span.error').remove();
	}
}


var paramToBlock = { css: {
	    border: 'none',
	    paddingTop: '3px',
	    backgroundColor: '#000',
	    opacity: .6,
	}, message: '<h4 style="color : #fff;"> Carregando... </h4>'
};
