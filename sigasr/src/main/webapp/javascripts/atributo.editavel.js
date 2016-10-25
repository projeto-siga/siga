/**
 * @class atributoEditavel
 * 
 * 		var propriedades = {
			id: idAtributo,
			valor: valorAtributo,
			tipo: tipoAtributo,
			valoresPreDefinidos: preDefinidoSet,
			elemento: event.target,
			nome: 'atributoSolicitacao'
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
		AtributoEditavel.DIV_EDITAVEL = this.getElementoComAtributoEditavel();
		AtributoEditavel.FORM = this.construirForm();
	},
	
	construirForm: function() {
		var form = new FormEditavel(AtributoEditavel.DIV_EDITAVEL);
		return form.construir(this.propriedades);
	},
	
	getElementoComAtributoEditavel: function() {
		return $(this.propriedades.elemento).closest(".atributo-editavel");
	},
	
	editar: function() {
		this.iniciar();
		AtributoEditavel.DIV_EDITAVEL.hide().after(AtributoEditavel.FORM);
	},
	
	toString: function() {
		console.log(this.propriedades.id + ", " + this.propriedades.valor + ", " + this.propriedades.tipo + ", " + this.propriedades.valoresPreDefinidos +
				", " + $(this.propriedades.elemento).text());
	}
}

AtributoEditavel.FORM = "";
AtributoEditavel.DIV_EDITAVEL = "";


/**
 * @class formEditavel
 **/
var FormEditavel = function (divEditavel) {
	this.divEditavel = divEditavel;
	
	this.form = "<form class='form-editavel'>" +	
						"<div class='div-campo-editavel' style='float: left; margin-right: 11px;'></div>" +
						"<div class='botao-editavel' style='height:30px;'></div>" +
				"</form>";
	
	this.botao = "<input type='submit' class='ok ui-state-default ui-corner-all' style='padding:.2em 1em; margin-right: 5px;' value='ok'/>" +
				 "<input type='button' class='cancelar ui-state-default ui-corner-all' style='padding:.2em 1em;' value='cancelar' />";
}

/**
 * metodos
 **/
FormEditavel.prototype = {
	constructor: FormEditavel,
	
	construir: function(propriedades) {
		this.iniciar();
		this.inserirBotao();
		this.inserirCampoEditavel(propriedades);

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
		
	},
	
	acaoCancelar: function() {
		var div = this.divEditavel;
		this.form.find('.botao-editavel > .cancelar').click(function() {
			div.show();
			this.form.remove();
		});
	},
	
	inserirCampoEditavel: function(propriedades) {
		var divComCampoEditavel = this.form.find('.div-campo-editavel');
		var campoEditavel = this.construirCampoEditavel(propriedades);
		divComCampoEditavel.append(campoEditavel);
	},
	
	construirCampoEditavel: function(propriedades) {
		var campo = new CampoEditavel(propriedades); 
		return campo.construir();
	}
}

var CampoEditavel = function (propriedades) {
	this.propriedades = propriedades;
	this.tipoEditavel =  new TipoCampoEditavel(this.propriedades.tipo).definir();
}

CampoEditavel.prototype = {
	constructor: CampoEditavel,
	
	construir: function() {
		this.tipoEditavel.iniciar();
		this.tipoEditavel.inserirParametros();
		
		return this.tipoEditavel.campo;
	}
}

var TipoCampoEditavel = function(tipo) {
	this.tipo = tipo;
	this.input 		= "<input class='campo-editavel' />";
	this.textarea 	= "<textarea class='campo-editavel'></texarea>";
	this.select 	= "<select class='campo-editavel'></select>";
}

TipoCampoEditavel.prototype = {
	csontructor: TipoCampoEditavel,
	
	definir: function() {
		if (this.tipo === 'TEXTO')
			return new Texto(this.input);
		if (this.tipo === 'NUM_INTEIRO' || this.tipo === 'NUM_DECIMAL')
			return new Numero(this.input);
		if (this.tipo === 'VL_PRE_DEFINIDO')
			return new ValorPreDefinido(this.select);
	}
}


var AcoesEmComum = function(campo) {
	this.campo = campo;
}

AcoesEmComum.prototype = {
	constructor: AcoesEmComum,
		
	iniciar: function() {
		var campoJquery = $(this.campo);
		this.campo = campoJquery;
	},
	
	inserirNomeEvalorAoCampo: function() {
		this.tipoEditavel.campo.attr("name", this.propriedades.nome)
		 					   .attr("value", this.propriedades.valor);
	},

	inserirAtributos: function(arrayComParamentros) {
		var campo = this.campo;
		arrayComParamentros.forEach(function(elemento){
			campo.attr(elemento.key, elemento.value);
		});
		this.campo = campo;
	},
	
	inserirOpcoes: function() {
		
	}	
}


var Texto = function(campo) {
	this.campo = campo; 
	this.tipo = {key: 'type', value: 'text'};
	this.tamanho = {key: 'size', valaue: '70'};
	this.maximo = {key: 'maxlength', value: '255'};
}

Texto.prototype = new AcoesEmComum(this.campo); 

Texto.prototype.constructor = Texto;
	
Texto.prototype.getParametros = function() {
	return new Array(this.tipo, this.tamanho, this.maximo);
};
	
Texto.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};


var Numero = function(campo) {
	this.campo = campo;
	this.tipo = {key: 'type', value: 'text'};
	this.maximo = {key: 'maxlength', value: '9'};
}




var ValorPreDefinido = function(campo) {
	this.campo = campo;
}

ValorPreDefinido.prototype = new AcoesEmComum(this.campo); 

ValorPreDefinido.prototype.constructor = ValorPreDefinido;
	
ValorPreDefinido.prototype.getParametros = function() {
	return new Array(this.tipo, this.maximo);
};
	
ValorPreDefinido.prototype.inserirParametros = function() {
	var arrParam = this.getParametros();
	this.inserirAtributos(arrParam);
};