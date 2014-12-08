/**
 * Utilitario para converter 
 */
function Formulario(form) {
	this.populateFromJson = function(obj) {
		form.populate(obj, {
			resetForm:true
		});
	}
	
	this.toJson = function() {
		return form.serializeJSON();
	}
}

function DesativarReativar(service) {
	this.callbackReativar = service.reativar;
	this.callbackDesativar = service.desativar;
	this.service = service;
	this.ativo = false;

	this.ativo = function(ativo) {
		this.ativo = ativo;
		return this;
	}
	
	this.innerHTML = function(td, id) {
		if (this.ativo) {
			innerHTMLDesativar(td, id, this.service);
		} else {
			innerHTMLAtivar(td, id, this.service);
		}
	}
	
	function innerHTMLAtivar(td, id) {
		var a = $('<a class="once" title="Reativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;">');
   		
   		var service = this.service;
   		a.bind('click', function(event) {
   			me.service.reativar(event, id);
     	});
   		td.html('');
   		a.append(img);
     	td.append(a);
	}

	function innerHTMLDesativar(td, id, service) {
		var a = $('<a class="once" title="Desativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">');
   		
   		var service = this.service;
   		a.bind('click', function(event) {
   			service.desativar(event, id);
     	});
   		td.html('');
   		a.append(img);
     	td.append(a);
	}
}

/**
 * opts.urlDesativar
 * opts.urlReativar
 * opts.urlGravar
 * opts.dialogCadastro
 * opts.objectName
 * opts.formCadastro
 * opts.mostrarDesativados
 */
function BaseService(opts) {
	this.opts = opts;
	this.formularioHelper = new Formulario(opts.formCadastro);
}
/**
 * Envia uma requisicao para o servidor utilizando o metodo post
 * opts.url = URL para onde a requisicao sera enviada
 * opts.obj = Objeto a ser enviado na requisicao
 */
BaseService.prototype.post = function(opts) {
	this.removerErros();
	
	return $.ajax({
    	type: "POST",
    	url: opts.url,
    	data: jQuery.param(opts.obj),
    	dataType: "text",
    	error: function(error) {
    		console.log(error);
    		
    		if(error.status == 400) {
	    		var errors = JSON.parse(error.responseText);
	    		for(var i = 0; i < errors.length; i++) {
	    			var span = $('<span class="error" style="color: red"></span>');
	    			span.html(errors[i].message);
	    			span.insertAfter($('[name=' + errors[i].key + ']'));
	    		}
    		} else {
    			
    			$("#codigoErro").html( error.status);
    			$("#responseText").html( error.responseText);
    			$("#responseStatus").html(error.statusText);
    			
    			$('#server_error_dialog').dialog('open');
    		}
    	}
   	});
}
BaseService.prototype.removerErros = function() {
	$('.error').remove();
}
BaseService.prototype.desativar = function(event, id) {
	event.stopPropagation();
	window.location = this.opts.urlDesativar + queryDesativarReativar(id, this.opts.mostrarDesativados);
}
BaseService.prototype.reativar = function(event, id) {
	event.stopPropagation();
	window.location = this.opts.urlReativar + queryDesativarReativar(id, this.opts.mostrarDesativados);
}
BaseService.prototype.editar = function(obj, title) {
	this.removerErros();
	this.formularioHelper.populateFromJson(obj);
	this.opts.dialogCadastro.dialog('option', 'title', title);
	this.opts.dialogCadastro.dialog('open');
}
BaseService.prototype.cadastrar = function(title) {
	this.removerErros();
	this.formularioHelper.populateFromJson({});
	this.opts.dialogCadastro.dialog('option', 'title', title);
	this.opts.dialogCadastro.dialog('open');
}
BaseService.prototype.gravar = function() {
	var service = this,
		obj = this.formularioHelper.toJson(),
		url = this.opts.urlGravar,
		wrapper = {},
		success = function(objSalvo) {
			if(service.onGravar) {
				service.onGravar(obj, JSON.parse(objSalvo));
			}
			opts.dialogCadastro.dialog("close");
		}
		
	wrapper[this.opts.objectName] = obj;
	
	this.post({
		'url' : url, 
		'obj' : wrapper
	}).success(success);
}
BaseService.prototype.cancelarGravacao = function() {
	this.opts.dialogCadastro.dialog("close");
}
BaseService.prototype.onGravar = function(obj, objSalvo) {
	var idAntigo = this.getId(obj),
		idNovo = this.getId(objSalvo);
	
	// Se foi uma edicao
	if(idAntigo) {
		var tr = this.opts.tabelaRegistros.find("tr[data-jsonId=" + idAntigo + "]");
		tr.attr('data-jsonId', idNovo);
		tr.data('json', objSalvo);
		
		acoesTable
			.api()
			.row(tr)
			.data(this.getRow(objSalvo));
		
	} else {
		acoesTable
			.api()
			.row
			.add(this.getRow(objSalvo))
			.draw();
	}
	
	new DesativarReativar(this)
		.ativo(objSalvo.ativo)
		.innerHTML(tr.find('td.acoes'), idNovo);
}

$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
	var carregando = $('#carregando'),
		originalPosition = carregando.css('position');
	
	carregando.css('position', 'fixed');
	carregando.css('z-index', '999');
	carregando.css('display', 'block');
	
	jqXHR.complete(function() {
		carregando.css('position', originalPosition);
		carregando.css('display', 'none');
	});
});
