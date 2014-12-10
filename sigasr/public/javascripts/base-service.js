/**
 * Utilitario para converter um form para um object em JSON
 */
function Formulario(form) {
	this.populateFromJson = function(obj) {
		form.find('input[type=hidden]').val(''); // WA
		form.populate(obj, {
			resetForm:true
		});
	}
	
	this.toJson = function() {
		return form.serializeJSON();
	}
}
/**
 * Utilitatio para desenhar o HTML correto do botao de desativar/reativar
 */
function DesativarReativar(service) {
	this.callbackReativar = service.reativar;
	this.callbackDesativar = service.desativar;
	this.service = service;
	this.isAtivo = false;
	
	this.ativo = function(isAtivo) {
		this.isAtivo = isAtivo;
		return this;
	}
	
	this.innerHTML = function(td, id) {
		if (this.isAtivo) {
			innerHTMLDesativar(td, id, this.service);
		} else {
			innerHTMLAtivar(td, id, this.service);
		}
	}
	
	function innerHTMLAtivar(td, id, service) {
		var a = $('<a class="once" title="Reativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;">');
   		
   		a.bind('click', function(event) {
   			service.reativar(event, id);
     	});
   		td.html('');
   		a.append(img);
     	td.append(a.wrap('<div>'));
	}

	function innerHTMLDesativar(td, id, service) {
		var a = $('<a class="once" title="Desativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">');
   		
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
    	error: BaseService.prototype.errorHandler
   	});
}
BaseService.prototype.errorHandler = function(error) {
	console.error(error);
	
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
/**
 * Remove as mensagens de erro na tela
 */
BaseService.prototype.removerErros = function() {
	$('.error').remove();
}
/**
 * Desativa o registro
 */
BaseService.prototype.desativar = function(event, id) {
	event.stopPropagation();
	window.location = this.opts.urlDesativar + queryDesativarReativar(id, this.opts.mostrarDesativados);
}
/**
 * Reativa o registro
 */
BaseService.prototype.reativar = function(event, id) {
	event.stopPropagation();
	window.location = this.opts.urlReativar + queryDesativarReativar(id, this.opts.mostrarDesativados);
}
/**
 * Inicia o modal de edicao do registro
 */
BaseService.prototype.editar = function(obj, title) {
	this.removerErros();
	this.formularioHelper.populateFromJson(obj);
	
	if(this.opts.dialogCadastro.size() == 0) {
		console.error('O elementro do dialog de cadastro nao foi encontrado!');
	}
	this.opts.dialogCadastro.dialog('option', 'title', title);
	this.opts.dialogCadastro.dialog('open');
	
}
/**
 * Inicia o modal para cadastro de um novo registro
 */
BaseService.prototype.cadastrar = function(title) {
	this.removerErros();
	this.formularioHelper.populateFromJson({});
	this.opts.dialogCadastro.dialog('option', 'title', title);
	this.opts.dialogCadastro.dialog('open');
}
/**
 * Executa a acao de gravar o registro
 */
BaseService.prototype.gravar = function() {
	var service = this,
		obj = this.getObjetoParaGravar(),
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
/**
 * Metodo que implementa a forma padrao de pegar o objeto para gravar no servidor
 */
BaseService.prototype.getObjetoParaGravar = function() {
	return this.formularioHelper.toJson();
}

/**
 * Cancela a gravacao do registro
 */
BaseService.prototype.cancelarGravacao = function() {
	this.opts.dialogCadastro.dialog("close");
}
/**
 * Metodo executado quando o registro foi gravado com sucesso no servidor
 */
BaseService.prototype.onGravar = function(obj, objSalvo) {
	var idAntigo = this.getId(obj),
		idNovo = this.getId(objSalvo),
		tr = null;

	// Se foi uma edicao
	if(idAntigo) {
		tr = this.opts.tabelaRegistros.find("tr[data-json-id=" + idAntigo + "]");
		
		if(this.opts.dataTable) {
			this.opts.dataTable
				.api()
				.row(tr)
				.data(this.getRow(objSalvo));
		}
	} 
	// Senao, eh um novo registro a ser inserido na GRID
	else {
		if(this.opts.dataTable) {
			
			var data = this.getRow(objSalvo),
				row = this.opts.dataTable
				.api()
				.row
				.add(data),
				indice = this.indiceAcoes(data);
			
			row.draw();
			tr = $(row.node());
			
			if(indice > -1) {
				tr.find('td:nth(' + indice + ')').addClass('acoes');
			}
			
			var onRowClick = this.onRowClick;
			if(onRowClick) {
				tr.css('cursor', 'pointer');
				tr.on('click', function() {
					onRowClick(objSalvo);
				});
			}
		}
	}
	
	tr.attr('data-json-id', idNovo);
	tr.data('json', objSalvo);
	
	var tdAcoes = tr.find('td.acoes');
	if(tdAcoes) {
		new DesativarReativar(this)
			.ativo(objSalvo.ativo)
			.innerHTML(tdAcoes, idNovo);
	}
}

BaseService.prototype.indiceAcoes = function(data) {
	for(var i = 0; i < data.length; i++) {
		if(data[i] == 'COLUNA_ACOES') 
			return i;
	}
	return -1;
}