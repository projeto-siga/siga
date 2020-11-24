/**
 * Utilitario para converter um form para um object em JSON
 */
function Formulario(form) {
	this.populateFromJson = function(obj) {
		var clone = Object.create(obj);
		this.reset();
		form.find('input[type=hidden]').val(''); // WA
		this.prepareObjectToForm(clone);

		form.populate(clone, {
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

	/**
	 * Faz a preparação do objeto para conter os campos necessários para popular os componentes do TRF.
	 * ToDo: nao buscar por className pois elas foram alteradas e sao apenas para apresentacao!
	 */
	this.prepareObjectToForm = function(obj) {
		for (var x in obj) {
		    if (typeof obj[x] == 'object') {
		    	var component = form.find('[name=' + x + ']');

		    	if (component.length == 0) {
		    		component = form.find('[id=' + x + ']');
		    	}

	    		var className = component.size() > 0 ? component[0].className : null,
	        	objeto = obj[x];

		    	// Caso o atributo seja um objeto, verifica qual seu tipo e preenche os valores necessários
		        if (className && objeto) {
		        	if (className == 'selecao') {
		        		this.prepareForSelecaoComponent(x, obj, objeto);
		        	}
			        else if (className == 'pessoaLotaSelecao') {
			        	var combo = component.closestPreviousElement("select");
			        	this.prepareForSelecaoComponent(x, obj, objeto);
			        	obj[combo.id] = obj[x].tipo;
			        }
			        else if (className == 'lotaSelecao')
			        	this.prepareForSelecaoComponent(x, obj, objeto);
			        else if (className == 'pessoaLotaFuncCargoSelecao') {
			        	var select = jQuery(component).parent().parent().parent().find('select')[0];
			        	obj[select.id] = objeto.tipo;
			        	select.changeValue(objeto.tipo);
			        	this.prepareForSelecaoComponent(x, obj, objeto);
			        }
			        else { // if (className == 'select-siga') {
			        	obj[x] = objeto ? objeto.id : '';
			        }
		        }
		    }
		}
	}

	/**
	 * Cria os atributos esperados pelo componente selecao.tag
	 */
	this.prepareForSelecaoComponent = function(atributo, obj, objeto) {
		obj["formulario_"+atributo+"Sel"] = objeto ? objeto.id : '';
		obj["formulario_"+atributo+"Sel_id"] = objeto ? objeto.id : '';
		obj["formulario_"+atributo+"Sel_sigla"] = objeto ? objeto.sigla : '';
		obj["formulario_"+atributo+"Sel_descricao"] = objeto ? objeto.descricao : '';
		obj[atributo+"SelSpan"] = objeto ? objeto.descricao : '';
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
		var a = $('<a class="once gt-btn-ativar item-desativado" title="Reativar"/>'),
   		img = $('<img src="/siga/css/famfamfam/icons/tick.png" style="margin-right: 5px;">');

   		a.bind('click', function(event) {
   			service.reativar(event, id);
     	});
   		td.html('');
   		a.append(img);
     	td.append(a.wrap('<div>'));
     	td.parent().find('td').addClass('item-desativado');
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
     	td.parent().find('td').removeClass('item-desativado');
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

BaseService.prototype.serializar = function(obj) {
	var queryString = jQuery.param(obj);
	if(!obj.serializar || obj.serializar != 'false') {
		while(queryString.indexOf('%5B') > 1) {
			queryString = queryString.replace('%5B', '.').replace('%5D', '');
		}
	}
	return  queryString;
}

BaseService.prototype.errorHandler = function(error) {
	if (/<[a-z][\s\S]*>/i.test(error.responseText)){
		var msg = $(error.responseText).find('#message').html();
		var details = $(error.responseText).find('#details').html();
		alert(msg + (details ? '\n' + details : ''));
	} else alert(error.responseText);
}
/**
 * Remove as mensagens de erro na tela
 */
BaseService.prototype.removerErros = function() {
	this.opts.formCadastro.find('span.error').remove();
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
	     "beforeSend": function () {
	    		jQuery.blockUI(objBlock);
	    	},
			"complete": function () {
				jQuery.unblockUI();
			},
	     success: function(response) {
			if(service.opts.mostrarDesativados == "true" || service.opts.mostrarDesativados == true) {
				var obj = JSON.parse(response),
					dataTableRow = service.opts.dataTable.api().row(tr),
					tableTr = $(dataTableRow.node());

				row[service.opts.colunas] = service.gerarColunaDesativar(service.getId(obj));
				tableTr.attr('data-json-id', service.getId(obj));
				tableTr.attr('data-json', response);
				tableTr.data('json', obj);
				service.opts.dataTable.api().row(tr).data(row).draw();

				 $(tr).find('td').addClass('item-desativado');
			}
			else {
				service.opts.dataTable.api().row(tr).remove().draw();
			}
	     },
	     error: BaseService.prototype.errorHandler
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
	     "beforeSend": function () {
	    		jQuery.blockUI(objBlock);
	    	},
			"complete": function () {
				jQuery.unblockUI();
			},
	     success: function(response) {
	    	 service.opts.dataTable.api().row(tr).data(row);

	         var obj = JSON.parse(response),
				dataTableRow = service.opts.dataTable.api().row(tr),
				tableTr = $(dataTableRow.node());

	         row[service.opts.colunas] = service.gerarColunaAtivar(service.getId(obj));
	         tableTr.attr('data-json-id', service.getId(obj));
	         tableTr.attr('data-json', response);
	         tableTr.data('json', obj);

	         $(tr).find('td').removeClass('item-desativado');
	         service.opts.dataTable.api().row(tr).data(row).draw();
	     },
	     error: BaseService.prototype.errorHandler
	});

}
/**
 * Gerar a Coluna Ativar
 */
BaseService.prototype.gerarColunaAtivar = function(id) {
	var column = '<a class="once gt-btn-ativar item-desativado" onclick="' + this.opts.objectName + 'Service.desativar(event, ' + id + ')" title="Desativar"><img src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;"></a>';
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
	this.opts.formCadastro.find('span.selecao').html('');
	this.opts.formCadastro.find('span.pessoaLotaSelecao').html('');
	this.opts.formCadastro.find('span.lotaSelecao').html('');
	this.opts.formCadastro.find('span.pessoaLotaFuncCargoSelecao').html('');
}

/**
 * Executa a acao de gravar o registro
 */
BaseService.prototype.gravar = function() {
	this.gravarAplicar(false);
}

BaseService.prototype.gravarAplicar = function(isAplicar) {
	if (!this.isValidForm())
		return false;

	var service = this,
		obj = this.getObjetoParaGravar(),
		url = this.opts.urlGravar,
		success = function(objSalvo) {
			if(service.onGravar) {
				service.onGravar(obj, JSON.parse(objSalvo));
			}

			if (isAplicar) {
				service.formularioHelper.populateFromJson(JSON.parse(objSalvo));
                alert("Cadastro salvo com sucesso.");
			}

			else
				service.opts.dialogCadastro.dialog("close");
		}

//
//	this.post({
//		'url' : url,
//		'obj' : wrapper
//	}).success(success);
//
	this.removerErros();

	return $.ajax({
    	type: "POST",
    	url: url,
    	data: this.serializar(obj),
    	dataType: "text",
    	"beforeSend": function () {
    		jQuery.blockUI(objBlock);
    	},
		"complete": function () {
			jQuery.unblockUI();
		},
    	error: BaseService.prototype.errorHandler

   	}).success(success);
}

/**
 * Executa a acao de aplicar o registro
 */
BaseService.prototype.aplicar = function() {
	return this.gravarAplicar(true);
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
		if(this.opts.dataTable) {
			tr = this.opts.dataTable.$('tr[data-json-id=' + idAntigo + ']');
			tr.attr('data-json-id', idNovo);
			tr.attr('data-json', JSON.stringify(objSalvo));
			tr.data('json', objSalvo);

			this.opts.dataTable
				.api()
				.row(tr)
				.data(this.getRow(objSalvo)).draw();
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

			tr = $(row.node());
			tr.attr('data-json-id', idNovo);
			tr.attr('data-json', JSON.stringify(objSalvo));
			tr.data('json', objSalvo);
			row.draw();

			var indice = this.indiceAcoes(tr);
			if(indice > -1) {
				tr.find('td:nth(' + indice + ')').addClass('acoes');
			}
		}
	}
	this.bindRowClick(tr, objSalvo);

	var tdAcoes = tr.find('td.acoes');
	if(tdAcoes.size() > 0) {
		new DesativarReativar(this)
			.ativo(objSalvo.ativo)
			.innerHTML(tdAcoes, idNovo);
	}
	return tr;
}

BaseService.prototype.bindRowClick = function(tr, objSalvo) {
	var onRowClick = this.onRowClick;
	if(onRowClick) {
		tr.attr('onclick','').unbind('click');
		tr.off('click');
		tr.css('cursor', 'pointer');

		tr.on('click', function() {
			onRowClick(objSalvo);
		});
	}
}

BaseService.prototype.row = function(obj) {
	var id = this.getId(obj);
	return this.opts.tabelaRegistros.find("tr[data-json-id=" + id + "]");
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

BaseService.criar = function(opts, service) {
	service.prototype = Object.create(BaseService.prototype);
	return new service(opts);
}