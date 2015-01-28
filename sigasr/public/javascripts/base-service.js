/**
 * Utilitario para converter um form para um object em JSON
 */
function Formulario(form) {
	this.populateFromJson = function(obj) {
		this.reset();
		form.find('input[type=hidden]').val(''); // WA
		
		prepareObjectToForm(obj);
		
		form.populate(obj, {
			resetForm:true
		});
	}
	
	this.toJson = function() {
		return form.serializeJSON();
	}
	
	this.reset = function() {
		if(form.resetForm) {
			form.resetForm();
		}
		form.find('.error').removeClass('error'); // Ao entrar no cadastro, remove classe de erro
	}
}

/**
 * Faz a preparação do objeto para conter os campos necessários para popular os componentes do TRF. 
 */
function prepareObjectToForm(obj) {
	for (var x in obj) {
	    if (typeof obj[x] == 'object') {
	    	var component = document.getElementsByName(x)[0],	    		
	    		className = component != null ? component.className : null,
	        	objeto = obj[x];
	        
	    	// Caso o atributo seja um objeto, verifica qual seu tipo e preenche os valores necessários
	        if (className && objeto) {
	        	if (className == 'selecao') {
	        		prepareForSelecaoComponent(x, obj, objeto);
	        	}
		        else if (className == 'pessoaLotaSelecao') {
		        	var combo = component.closestPreviousElement("select");
		        	prepareForSelecaoComponent(x, obj, objeto);
		        	obj[combo.id] = obj[x].tipo;
		        }
		        else if (className == 'lotaSelecao')
		        	prepareForSelecaoComponent(x, obj, objeto);
		        else if (className == 'pessoaLotaFuncCargoSelecao') {
		        	var select = jQuery(component).parent().parent().parent().find('select')[0];
		        	obj[select.id] = objeto.tipo;
		        	select.onchange();
		        	prepareForSelecaoComponent(x, obj, objeto);
		        }
		        	
		        else if (className == 'select-siga') {
		        	obj[x] = objeto ? objeto.id : '';
		        }
	        }
	    }
	}
}

/**
 * Cria os atributos esperados pelo componente selecao.html
 */
function prepareForSelecaoComponent(atributo, obj, objeto) {
	obj[atributo] = objeto ? objeto.id : '';
	obj[atributo+"_sigla"] = objeto ? objeto.sigla : '';
	obj[atributo+"_descricao"] = objeto ? objeto.descricao : '';
	obj[atributo+"Span"] = objeto ? objeto.descricao : '';
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
		var a = $('<a class="once gt-btn-ativar" title="Reativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;">');
   		
   		a.bind('click', function(event) {
   			service.reativar(event, id);
     	});
   		td.html('');
   		a.append(img);
     	td.append(a.wrap('<div>'));
	}

	function innerHTMLDesativar(td, id, service) {
		var a = $('<a class="once gt-btn-ativar" title="Desativar"/>'),
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
	
	this.opts.validatorForm = opts.formCadastro.validate({
		onfocusout: false
	});
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
			var span = $('<span class="error" style="color: red; font-weight:bold"></span>');
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
	$('span.error').remove();
	this.resetErrosForm();
}
/**
 * Desativa o registro
 */
BaseService.prototype.desativar = function(event, id) {
	event.stopPropagation();
	var tr = $(event.currentTarget).parent().parent()[0],
		row = this.opts.dataTable.api().row(tr).data(),
		service = this;
	
	return $.ajax({
	     type: "POST",
	     url: this.opts.urlDesativar,
	     data: {id : id, mostrarDesativados : this.opts.mostrarDesativados},
	     dataType: "text",
	     success: function(response) {
			if(service.opts.mostrarDesativados == "true") {
				row[service.opts.colunas] = service.gerarColunaDesativar(id);
				service.opts.dataTable.api().row(tr).data(row);
			}
			else {
				service.opts.dataTable.api().row(tr).remove().draw();
			}
	     },
	     error: function(response) {
	    	var modalErro = $('#modal-error');
	    	modalErro.find("h3").html(response.responseText);
	    	modalErro.show(); 
	     }
	});
}
/**
 * Reativa o registro
 */
BaseService.prototype.reativar = function(event, id) {
	event.stopPropagation();
	var tr = $(event.currentTarget).parent().parent()[0],
		row = this.opts.dataTable.api().row(tr).data(),
		service = this;

	return $.ajax({
	     type: "POST",
	     url: this.opts.urlReativar,
	     data: {id : id, mostrarDesativados : this.opts.mostrarDesativados},
	     dataType: "text",
	     success: function(id) {
	         row[service.opts.colunas] = service.gerarColunaAtivar(id);
	         service.opts.dataTable.api().row(tr).data(row);
	     },
	     error: function(response) {
	    	var modalErro = $('#modal-error');
	    	modalErro.find("h3").html(response.responseText);
	    	modalErro.show(); 
	     }
	});
	
}
/**
 * Gerar a Coluna Ativar
 */
BaseService.prototype.gerarColunaAtivar = function(id) {
	var column = '<a class="once gt-btn-ativar" onclick="' + this.opts.objectName + 'Service.desativar(event, ' + id + ')" title="Desativar"><img src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"></a>';
		return column;
}
/**
 * Gerar a Coluna Desativar
 */
BaseService.prototype.gerarColunaDesativar = function(id) {
	var column = '<a class="once gt-btn-desativar" onclick="' + this.opts.objectName + 'Service.reativar(event, ' + id + ')" title="Reativar"><img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;"></a>';
		return column;
 }
/**
 * Inicia o modal de edicao do registro
 */
BaseService.prototype.editar = function(obj, title) {
	this.removerErros();
	this.limparSpanComponentes();
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
	this.limparSpanComponentes();
	this.formularioHelper.populateFromJson({});
	this.opts.dialogCadastro.dialog('option', 'title', title);
	this.opts.dialogCadastro.dialog('open');
}

/**
 * Limpa o valor dos Spans dos seguintes componentes:
 * 
 * selecao.html
 * pessoaLotaSelecao.html
 * lotaSelecao.html
 * pessoaLotaFuncCargoSelecao.html
 */
BaseService.prototype.limparSpanComponentes = function() {
	$('span.selecao').html('');
	$('span.pessoaLotaSelecao').html('');
	$('span.lotaSelecao').html('');
	$('span.pessoaLotaFuncCargoSelecao').html('');
}

/**
 * Executa a acao de gravar o registro
 */
BaseService.prototype.gravar = function() {
	gravarAplicar(this, false);
}

function gravarAplicar(baseService, isAplicar) {
	if (!baseService.isValidForm())
		return;
	
	var obj = baseService.getObjetoParaGravar(),
		url = baseService.opts.urlGravar,
		wrapper = {},
		success = function(objSalvo) {
			if(baseService.onGravar) {
				baseService.onGravar(obj, JSON.parse(objSalvo));
			}
			
			if (isAplicar) {
				baseService.formularioHelper.populateFromJson(JSON.parse(objSalvo));
				
				alert("Cadastro salvo com sucesso.");
			}
				
			else
				baseService.opts.dialogCadastro.dialog("close");
		}
		
	wrapper[baseService.opts.objectName] = obj;
	
	baseService.post({
		'url' : url, 
		'obj' : wrapper
	}).success(success);
}

/**
 * Executa a acao de aplicar o registro
 */
BaseService.prototype.aplicar = function() {
	gravarAplicar(this, true);
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
				.add(data);
			
			row.draw();
			tr = $(row.node());
			
			var indice = this.indiceAcoes(tr);
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
	
	return tr;
}

BaseService.prototype.indiceAcoes = function(tr) {
	var tds = tr.find('td');
	for(var i = 0; i < tds.length; i++) {
		var td = $(tds[i]);
		if(td.html().trim() == 'COLUNA_ACOES') 
			return i;
	}
	return -1;
}

BaseService.prototype.isValidForm = function() {
    return jQuery(this.opts.formCadastro).valid();
}

BaseService.prototype.resetErrosForm = function() {
	if (this.opts.validatorForm)
		this.opts.validatorForm.resetForm();
}