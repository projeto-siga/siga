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
		AtributoEditavel.DIV_EDITAVEL = this.getElementoComAtributoEditavel();
		AtributoEditavel.FORM = this.construirForm();
	},
	
	construirForm: function() {
		var form = new FormEditavel(this.propriedades, AtributoEditavel.DIV_EDITAVEL);
		return form.construir();
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
var FormEditavel = function (propriedades, divEditavel) {
	this.propriedades = propriedades;
	this.divEditavel = divEditavel;
	
	this.form = "<form class='form-editavel' action='#' method='post' enctype='multipart/form-data'>" +	
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
	
	construir: function() {
		this.iniciar();
		this.inserirBotao();
		this.inserirCampoHidden();
		this.inserirCampoEditavel(this.propriedades);

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
		var div = this.divEditavel;
		var url = this.propriedades.urlDestino;
		var form = this.form;
		var param = this.getParamToBlock();
		form.find('.botao-editavel > .ok').click(function(event) {
			event.preventDefault();
			form.block(param);
			Siga.ajax(url, form.serialize(), "POST", function(response) {
				div.find('span').text(response);
				div.show();
				form.unblock();
				form.remove();
			});
		});
	},
	
	getParamToBlock: function() {
		var param = { css: {
		    border: 'none',
		    paddingTop: '3px',
		    backgroundColor: '#000',
		    opacity: .6,
		}, message: '<h4 style="color : #fff;"> Carregando... </h4>' };
		
		return param;	
	},
	
	acaoCancelar: function() {
		var div = this.divEditavel;
		this.form.find('.botao-editavel > .cancelar').click(function() {
			div.show();
			this.form.remove();
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
	}
}

var CampoEditavel = function (propriedades) {
	this.campoFactory =  new CampoEditavelFactory(propriedades).getInstancia();
}

CampoEditavel.prototype = {
	constructor: CampoEditavel,
	
	construir: function() {
		this.campoFactory.iniciar();
		this.campoFactory.inserirParametros();
		this.campoFactory.inserirNomeEValor();
		
		return this.campoFactory.campo;
	}
}

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
		if (this.propriedades.tipo === 'NUM_INTEIRO' || this.propriedades.tipo === 'NUM_DECIMAL')
			return new Numero(this.propriedades, this.input);
		if (this.propriedades.tipo === 'VL_PRE_DEFINIDO')
			return new ValorPreDefinido(this.propriedades, this.select);
		if (this.propriedades.tipo === 'HIDDEN')
			return new Hidden(this.propriedades, this.input);
	}
}


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
		this.campo.attr("name", this.propriedades.nome)
		 		  .attr("value", this.propriedades.valor);
	},

	inserirAtributos: function(arrayComParamentros) {
		var campo = this.campo;
		arrayComParamentros.forEach(function(elemento) {
			campo.attr(elemento.key, elemento.value);
		});
		this.campo = campo;
	},
	
	inserirOpcoes: function() {
		var campo = this.campo;
		var arrayComValores = this.propriedades.valoresPreDefinidos.slice(1, -1).split(",");
		arrayComValores.forEach(function(elemento) {
			campo.append($('<option>', {value: elemento.trim()})
				 .text(elemento.trim())); 
		});
		this.campo = campo;
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