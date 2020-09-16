var ExConfiguracao = ExConfiguracao || {}

ExConfiguracao.Etapas = (function() {
	
	function Etapas() {
		this.etapaAtual = 0;
		this.etapas = $('.js-etapa');
		this.btnPesquisa = $('.js-btn-pesquisa-configuracoes');
		this.btnAnterior = $('.js-btn-anterior');
		this.btnProximo = $('.js-btn-proximo');			
		this.spanIndicadorEtapa = $('.js-indicador-etapa');
		this.btnConfirmacaoModal = $('.btn-confirmacao-modal');						
		this.comboModelos = $('.js-siga-multiploselect--modelo.selectpicker');		
		this.comboTipoConfiguracao = $('.js-tipo-configuracao');		
		this.comboNivelAcesso = $('.js-nivel-acesso');
		this.comboOrgaos = $('.js-siga-multiploselect--orgao.selectpicker');
		this.comboUnidades = $('.js-siga-multiploselect--unidade.selectpicker');
		this.tituloNovaConfiguracao = $('.js-titulo-nova-configuracao');
		this.selecaoModelosOK = false;
		this.selecaoConfiguracaoOK = false;
		this.selecaoNivelAcessoOK = false;
		this.selecaoOrgaosEUnidadesOK = false;								
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);				
	}
	
	Etapas.prototype.iniciar = function() {
		this.btnPesquisa.on('click', onBtnPesquisaClicado.bind(this));
		this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
		this.btnProximo.on('click', onBtnProximoClicado.bind(this));
		this.btnConfirmacaoModal.on('click', onBtnConfirmacaoModalClicado.bind(this));					
		this.comboModelos.on('change', atualizarBtnProximo.bind(this, this.comboModelos));
		this.comboTipoConfiguracao.on('change', atualizarBtnProximo.bind(this, this.comboTipoConfiguracao));
		this.comboNivelAcesso.on('change', atualizarBtnProximo.bind(this, this.comboNivelAcesso));
		this.comboUnidades.on('change', atualizarBtnProximo.bind(this, this.comboUnidades));						
		desabilitarBtnProximo.call(this);
		exibirEtapa.call(this, this.etapaAtual);			
	}	
	
	function onBtnPesquisaClicado() {
		if (this.comboModelos.val() != null) {
			this.btnConfirmacaoModal.addClass('is-pesquisa');
			sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Tem certeza que deseja cancelar o cadastro e ir para a página de Pesquisa de Configurações.');			
		} else {
			location.href = 'pesquisa';
		}
	}
	
	function onBtnAnteriorClicado() {			
		switch (this.etapas[this.etapaAtual - 1].id) {
			case 'selecaoModelos':				
				atualizarBtnProximo.call(this, this.comboModelos);				
				break;
			case 'selecaoConfiguracao':				
				atualizarBtnProximo.call(this, this.comboTipoConfiguracao);
				break;
			case 'selecaoNivelAcesso':				
				atualizarBtnProximo.call(this, this.comboNivelAcesso);
				break;
			case 'selecaoOrgaosEUnidades':				
				atualizarBtnProximo.call(this, this.comboUnidades);				
				break;
		}
		
		atualizarEtapa.call(this, -1, false);	
	}
	
	function onBtnProximoClicado(podeValidar) {
		if (podeValidar !== false && !validarCampos.call(this, this.etapaAtual)) {
			return false;
		} 			
		
		if (sigaModal.isAberto('confirmacaoModal')) {
			sigaModal.fechar('confirmacaoModal');
		}
				
		if (typeof this.etapas[this.etapaAtual + 1] !== 'undefined') {
			switch (this.etapas[this.etapaAtual + 1].id) {
			case 'selecaoModelos':					
				if (this.comboModelos[0].length <= 0) {
					this.emitter.trigger('inicializarModelos');
				}								
				atualizarBtnProximo.call(this, this.comboModelos);
				break;
			case 'selecaoConfiguracao':				
				atualizarBtnProximo.call(this, this.comboTipoConfiguracao);
				break;
			case 'selecaoNivelAcesso':							
				atualizarBtnProximo.call(this, this.comboNivelAcesso);
				break;
			case 'selecaoOrgaosEUnidades':				
				if (this.comboOrgaos[0].length <= 0) {					
					this.emitter.trigger('inicializarOrgaos');
				}							
				atualizarBtnProximo.call(this, this.comboUnidades);	
				break;
			}
			
			atualizarEtapa.call(this, 1);
		}						
	}
	
	function onBtnConfirmacaoModalClicado() {		
		if (this.btnConfirmacaoModal.hasClass('is-pesquisa')) {			
			location.href = 'pesquisa';
			return false;
		}		
		
		switch (this.etapas[this.etapaAtual].id) {
		case 'selecaoModelos':
			this.selecaoModelosOK = true;
			break;
		case 'selecaoConfiguracao':
			this.selecaoConfiguracaoOK = true;
			break;
		case 'selecaoNivelAcesso':
			this.selecaoNivelAcessoOK = true;
			break;
		case 'selecaoOrgaosEUnidades':
			this.selecaoOrgaosEUnidadesOK = true;
			break;
		}
				
		onBtnProximoClicado.call(this, false);			
	}
	
	function atualizarBtnProximo(combo) {
		if (combo.val() > 0 || 		
				(combo.hasClass('js-nivel-acesso') && combo.val() !== '') ||
				(combo.hasClass('selectpicker') && combo.val() != null)) {
			habilitarBtnProximo.call(this);			
		} else {
			desabilitarBtnProximo.call(this);
		}				
	}
	
	function desabilitarBtnProximo() {
		this.btnProximo.attr('disabled', 'disabled');
	}
	
	function habilitarBtnProximo() {
		this.btnProximo.removeAttr('disabled');
	}
	
	function exibirEtapa(numeroEtapa) {		
		this.etapas[numeroEtapa].style.display = "block";
	  
		if (numeroEtapa == 0) {
			this.btnAnterior.css('display', 'none');    
		} else {
			this.btnAnterior.css('display', 'inline');
		}
		if (numeroEtapa == (this.etapas.length - 1)) {
			this.btnProximo.removeClass('btn-primary').addClass('btn-success');
			this.btnProximo.html('Salvar  <i class="fas fa-check"></i>');    			
		} else {
			this.btnProximo.removeClass('btn-success').addClass('btn-primary');
			this.btnProximo.html('Próximo  <i class="fas fa-long-arrow-alt-right"></i>');    
		}
		
		atualizarTituloEtapaTopo.call(this);
		atualizarSpanIndicadorEtapa.call(this, numeroEtapa);
	}
	
	function atualizarEtapa(numeroEtapa) {  		
		this.etapas[this.etapaAtual].style.display = "none";
	  
		this.etapaAtual += numeroEtapa;
	  
		if (this.etapaAtual >= this.etapas.length) {
			//efetuar request para salvar os dados
			
			// colocar um spinner no meio da tela
			
			if (this.comboModelos.val() != null && this.comboModelos.val().length >= 10) {
				// colocar uma mensagem ao salvar, avisando que foram selecionados muitos modelos e o processo pode demorar um pouco			
			} 
			
			return false;
	  }
	  
	  exibirEtapa.call(this, this.etapaAtual);
	}
		
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'selecaoModelos':
				retorno.alertaConfirmado = this.selecaoModelosOK; 				
				this.emitter.trigger('validarModelos', retorno);				
				break;
			case 'selecaoConfiguracao':
				//validarSelecaoConfiguracao.call(this);
				break;
			case 'selecaoNivelAcesso':
				//resposta = validarSelecaoNivelAcesso.call(this);
				break;
			case 'selecaoOrgaosEUnidades':
				//resposta = validarSelecaoOrgaosEUnidades.call(this);
				break;
		}
		
		return retorno.resultado;					
	}
		
	function validarSelecaoConfiguracao() {
		return true;	
	}
	
	function validarSelecaoNivelAcesso() {
		return true;
	}
	
	function validarSelecaoOrgaosEUnidades() {
		return true;
	}
	
	function atualizarTituloEtapaTopo() {
		this.tituloNovaConfiguracao.find('.js-titulo-etapa-topo').remove();
		this.tituloNovaConfiguracao.find('.js-titulo-etapa-topo--duvida').remove();
				
		for (var i = 0; i <= this.etapaAtual; i++) {
			switch (this.etapas[i].id) {				
			case 'selecaoModelos':					
				this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Modelos</span>');				
				break;
			case 'selecaoConfiguracao':				
				this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Configuração</span>');				
				break;
			case 'selecaoNivelAcesso':				
				this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Nível de acesso</span>');							
				break;
			case 'selecaoOrgaosEUnidades':				
				this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Unidades</span>');									
				break;
			}					
		}	
		
		switch (this.etapas[this.etapaAtual].id) {				
		case 'selecaoModelos':					
			this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo--duvida  js-titulo-etapa-topo--duvida"><i class="fas fa-info-circle"></i></span>');		
			this.tituloNovaConfiguracao.find('.js-titulo-etapa-topo--duvida').popover({
				title: 'Seleção de modelos',
				html: true,
				content: 'Nessa etapa você deve selecionar os modelos que deseja configurar'
			  });					
			break;
		case 'selecaoConfiguracao':				
			this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo--duvida  js-titulo-etapa-topo--duvida"><i class="fas fa-info-circle"></i></span>');		
			this.tituloNovaConfiguracao.find('.js-titulo-etapa-topo--duvida').popover({
				title: 'Seleção de tipo de configuração',
				html: true,
				content: 'Nessa etapa você deve selecionar o tipo de configuração a ser aplicada, se quiser entender melhor o que cada configuração faz <a href="tipos/dicionario" target="_blank">clique aqui</a>'
			  });				
			break;
		case 'selecaoNivelAcesso':												
			break;
		case 'selecaoOrgaosEUnidades':																
			break;
		}
	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
	}

	return Etapas;	
}());

ExConfiguracao.ComboModelo = (function() {
	
	function ComboModelo(Etapas) {		
		this.combo = $('.js-siga-multiploselect--modelo');				
		this.spinner = $('.js-spinner--modelo');
		this.Etapas = Etapas;				
	}
				
	ComboModelo.prototype.iniciar = function() {			
		this.Etapas.on('inicializarModelos', onInicializar.bind(this));
		this.Etapas.on('validarModelos', onValidar.bind(this));
		onInicializar.call(this);
	}
	
	function onInicializar() {						
		iniciarConfiguracoes.call(this);
		buscar.call(this);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione um ou mais modelos',
			noneResultsText: 'Nenhum modelo encontrado para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} modelo selecionado':'{0} modelos selecionados'},
			selectedTextFormat: 'count > 2'
		});
	}
	
	function buscar() {
		var resposta = $.ajax({
			url: 'modelos',
			method: 'GET', 											
			beforeSend: iniciarRequisicao.bind(this),
			complete: finalizarRequisicao.bind(this)
		});			
		resposta.done(onBuscarModelosFinalizado.bind(this));		
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.combo.parent().css({'width':'calc(100% - 30px)', 'transition':'width .2s'});
		this.spinner.show();
	}
	
	function finalizarRequisicao() {		
		this.spinner.hide();				
		this.combo.parent().parent().css('width', '100%');
		this.combo.selectpicker('refresh');					
	}	
	
	function onBuscarModelosFinalizado(modelos) {
		var options = [];		
		if(modelos.list.length > 0) {
			modelos.list.forEach(function(modelo){											
				options.push('<option value="' + modelo.id + '">' + modelo.nmMod + '</option>');													
			});															
			this.combo.html(options.join(''));		
			this.combo.removeAttr('disabled');			
			this.combo.parent().removeClass('disabled');
			this.combo.parent().children().removeClass('disabled');		
			this.quantidadeModelos = modelos.list.length;
		} else {			
			reset.call(this);			
		} 			
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().children().addClass('disabled');	
		this.spinner.parent().removeClass('disabled');				
		this.combo.selectpicker('refresh');				
	}
	
	function onValidar(evento, validacao) {				
		if (this.combo.val() != null) {			
			if (!validacao.alertaConfirmado && this.combo.val().length >= this.quantidadeModelos) {
				sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Você selecionou todos os modelos, tem certeza que deseja aplicar configuração para todos?');		  		  
				validacao.resultado = false;
				return false;
			}
			
			if (!validacao.alertaConfirmado && this.combo.val().length > 10) {			
				sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Você selecionou ' + this.combo.val().length + ' modelos, tem certeza que deseja aplicar configuração para todos os modelos selecionados?');		  
				validacao.resultado = false;
				return false;
			}			
		}			
		validacao.resultado = true;
		return true;		
	}
			
	return ComboModelo;
	
}());

ExConfiguracao.ComboOrgao = (function() {
	
	function ComboOrgao(Etapas) {		
		this.combo = $('.js-siga-multiploselect--orgao');				
		this.spinner = $('.js-spinner--orgao');
		this.Etapas = Etapas;		
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);		
	}
				
	ComboOrgao.prototype.iniciar = function() {	
		this.combo.on('change', onOrgaoAlterado.call(this));
		this.combo.on('changed.bs.select', onOrgaoSelecionado.bind(this));						
		this.Etapas.on('inicializarOrgaos', onInicializar.bind(this));
	}
	
	function onInicializar() {						
		iniciarConfiguracoes.call(this);
		buscar.call(this);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione um ou mais órgãos',
			noneResultsText: 'Nenhum órgão encontrado para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} órgão selecionado':'{0} órgãos selecionados'},
			selectedTextFormat: 'count > 1'
		});
	}
	
	function buscar() {
		var resposta = $.ajax({
			url: 'orgaos',
			method: 'GET',											
			beforeSend: iniciarRequisicao.bind(this),
			complete: finalizarRequisicao.bind(this)
		});			
		resposta.done(onBuscarOrgaosFinalizado.bind(this));			
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.combo.parent().css({'width':'calc(100% - 30px)', 'transition':'width .2s'});
		this.spinner.show();
	}
	
	function finalizarRequisicao() {		
		this.spinner.hide();				
		this.combo.selectpicker('refresh');					
	}			
	
	function onBuscarOrgaosFinalizado(orgaos) {
		var options = [];		
		if(orgaos.list.length > 0) {
			orgaos.list.forEach(function(orgao){											
				options.push('<option value="' + orgao.idOrgaoUsu + '">' + orgao.nmOrgaoUsu + '</option>');													
			});															
			this.combo.html(options.join(''));	
			this.combo.removeAttr('disabled');			
			this.combo.parent().removeClass('disabled');
			this.combo.parent().children().removeClass('disabled');		
		} else {			
			reset.call(this);			
		} 			
	}
	
	function onOrgaoAlterado() {						
		if (this.combo && this.combo[0].options.length > 1) {
			if (this.combo[0].options[0].text.toLowerCase() == 'selecione') {
				this.combo[0].remove(0);				
				this.combo.removeAttr('disabled');			
				this.combo.parent().removeClass('disabled');
				this.combo.parent().children().removeClass('disabled');	
														
				this.combo.selectpicker('refresh');												
			}
		} else {
			reset.call(this);
		}					
	}	
	
	function onOrgaoSelecionado() {				
		this.emitter.trigger('selecionado');		
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().children().addClass('disabled');	
		this.spinner.parent().removeClass('disabled');				
		this.combo.selectpicker('refresh');					
	}
			
	return ComboOrgao;
	
}());

ExConfiguracao.ComboUnidade = (function() {
	
	function ComboUnidade(ComboOrgao) {
		this.ComboOrgao = ComboOrgao;
		this.combo = $(".js-siga-multiploselect--unidade");
		this.spinner = $('.js-spinner--unidade');				
	}
	
	ComboUnidade.prototype.iniciar = function() {		
		reset.call(this);	
		this.ComboOrgao.on('selecionado', onOrgaoSelecionado.bind(this));		
		var idOrgao = this.ComboOrgao.combo.val();		
		onInicializar.call(this, idOrgao);		
	}	
		
	function onOrgaoSelecionado() {			
		onInicializar.call(this, this.ComboOrgao.combo.val());		
	}
	
	function onInicializar(idOrgaoSelecao) {						
		iniciarConfiguracoes.call(this);
		buscar.call(this, idOrgaoSelecao);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione uma ou mais unidades',
			noneResultsText: 'Nenhuma unidade encontrada para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} unidade selecionada':'{0} unidades selecionadas'},
			selectedTextFormat: 'count > 1'
		});
	}
	
	function buscar(idOrgaoSelecao) {				
		if (idOrgaoSelecao) {		
			var dados = {'dados': { 'idOrgaoSelecao': idOrgaoSelecao }};
			var resposta = $.ajax({
				url: 'unidades',
				method: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(dados), 											
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onBuscarUnidadesFinalizado.bind(this));
		} else {			
			reset.call(this);
		}	
	}
	
	function onBuscarUnidadesFinalizado(unidades) {
		var options = [];
		var nomeOrgao = '';		
		if(unidades.list.length > 0) {
			unidades.list.forEach(function(unidade){		
				if (nomeOrgao != unidade.nomeOrgao) {
					if (nomeOrgao != '') options.push('</optgroup>');
					options.push('<optgroup label="' + unidade.nomeOrgao + '">');			
				}						
				options.push('<option value="' + unidade.id + '">' + unidade.sigla + ' - ' + unidade.nome + '</option>');
				nomeOrgao = unidade.nomeOrgao;												
			});				
			options.push('</optgroup>');
								
			this.combo.html(options.join(''));	
			this.combo.removeAttr('disabled');			
			this.combo.parent().removeClass('disabled');
			this.combo.parent().children().removeClass('disabled');					
		} else {			
			reset.call(this);			
		} 			
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().children().addClass('disabled');	
		this.spinner.parent().removeClass('disabled');				
		this.combo.selectpicker('refresh');			
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.combo.parent().css({'width':'calc(100% - 30px)', 'transition':'width .2s'});
		this.spinner.show();
	}
	
	function finalizarRequisicao() {		
		this.spinner.hide();				
		this.combo.selectpicker('refresh');			
	}
	
	return ComboUnidade;
	
}());

$(function() {
	$('[name=idTpConfiguracao]').addClass('siga-select2');
	$('[data-toggle="popover"]').popover();
	
	var etapas = new ExConfiguracao.Etapas();
	etapas.iniciar();
	
	var comboModelos = new ExConfiguracao.ComboModelo(etapas);
	comboModelos.iniciar();
	
	var comboOrgaos = new ExConfiguracao.ComboOrgao(etapas);
	comboOrgaos.iniciar();
	
	var comboUnidades = new ExConfiguracao.ComboUnidade(comboOrgaos);
	comboUnidades.iniciar();		
});