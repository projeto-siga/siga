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
		this.comboTipoConfiguracao = $('.js-siga-select2--configuracao');		
		this.comboMovimentacao = $('.js-siga-select2--tipo-movimentacao');
		this.comboDestinatario = $('.js-siga-select2--destinatario');
		this.comboVisibilidade = $('.js-siga-select2--visibilidade');
		this.comboOrgaos = $('.js-siga-multiploselect--orgao.selectpicker');		
		this.comboUnidades = $('.js-siga-multiploselect--unidade.selectpicker');		
		this.comboCargos = $('.js-siga-multiploselect--cargo.selectpicker');
		this.comboFuncoes = $('.js-siga-multiploselect--funcao.selectpicker');
		this.comboPessoas = $('.js-siga-multiploselect--pessoa.selectpicker');				
		this.containerMovimentacao = $('.js-container-campos--tipo-movimentacao');
		this.containerTipoConfiguracao = $('.js-container-campos--tipo-configuracao');
		this.containerDestinatario = $('.js-container-campos--destinatario');
		this.containerVisibilidade = $('.js-container-campos--visibilidade');
		this.containerOrgaos = $('.js-container-campos--orgao');
		this.containerUnidades = $('.js-container-campos--unidade');
		this.containerCargos = $('.js-container-campos--cargo');
		this.containerFuncoes = $('.js-container-campos--funcao');
		this.containerPessoas = $('.js-container-campos--pessoa');
		this.labelVisibilidade = $('.js-label-visibilidade');
		this.labelDestinatarioDefinicao = $('.js-label-destinatario-definicao');		
		this.labelMovimentacao = $('.js-label-movimentacao');		
		this.tituloNovaConfiguracao = $('.js-titulo-nova-configuracao');
		this.spinner = $('.js-spinner--salvando');
		this.selecaoModelosOK = false;
		this.selecaoConfiguracaoOK = false;
		this.selecaoDestinatarioOK = false;
		this.selecaoDestinatarioDefinicaoOK = false;								
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);				
	}
	
	Etapas.prototype.iniciar = function() {
		this.btnPesquisa.on('click', onBtnPesquisaClicado.bind(this));
		this.btnAnterior.on('click', onBtnAnteriorClicado.bind(this));
		this.btnProximo.on('click', onBtnProximoClicado.bind(this));
		this.btnConfirmacaoModal.on('click', onBtnConfirmacaoModalClicado.bind(this));					
		this.comboModelos.on('change', atualizarBtnProximo.bind(this, this.comboModelos));
		this.comboTipoConfiguracao.on('change', onComboTipoConfiguracaoSelecionado.bind(this));
		this.comboMovimentacao.on('change', onComboMovimentacaoSelecionada.bind(this));
		this.comboDestinatario.on('change', onComboDestinatarioSelecionado.bind(this));
		this.comboVisibilidade.on('change', onComboVisibilidadeSelecionada.bind(this));
		this.comboUnidades.on('change', atualizarBtnProximo.bind(this, this.comboUnidades));
		
		this.comboOrgaos.on('change', onDestinatarioDefinido.bind(this, this.comboOrgaos));
		this.comboUnidades.on('change', onDestinatarioDefinido.bind(this, this.comboUnidades));
		this.comboCargos.on('change', onDestinatarioDefinido.bind(this, this.comboCargos));
		this.comboFuncoes.on('change', onDestinatarioDefinido.bind(this, this.comboFuncoes));
		this.comboPessoas.on('change', onDestinatarioDefinido.bind(this, this.comboPessoas));
		desabilitarBtnProximo.call(this);
		exibirEtapa.call(this, this.etapaAtual);			
	}	
	
	function onDestinatarioDefinido(combo) {
		if (this.etapaAtual + 1 >= this.etapas.length) {		
			if (this.comboDestinatario.val() != 'id_ORGAOS') {
				if (this.comboOrgaos.val() != null && !combo.hasClass('js-siga-multiploselect--orgao') && combo.val() != null) {
					atualizarBtnProximo.call(this, combo);
				} else {
					desabilitarBtnProximo.call(this);
				}
			} else {
				atualizarBtnProximo.call(this, combo);
			}
		}
	}
	
	function onBtnPesquisaClicado() {
		if (this.comboModelos.val() != null) {
			this.btnConfirmacaoModal.addClass('is-pesquisa');
			this.btnConfirmacaoModal.removeClass('hidden');
			sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Tem certeza que deseja cancelar o cadastro e ir para a página de Pesquisa de Configurações.');			
		} else {
			location.href = '../configuracao/listar';
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
			case 'selecaoDestinatario':				
				atualizarBtnProximo.call(this, this.comboDestinatario);
				break;
			case 'selecaoDestinatarioDefinicao':				
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
				this.containerTipoConfiguracao.show();				
				atualizarBtnProximo.call(this, this.comboTipoConfiguracao);
				break;
			case 'selecaoDestinatario':
				this.containerDestinatario.show();
				atualizarBtnProximo.call(this, this.comboDestinatario);
				break;
			case 'selecaoDestinatarioDefinicao':				
				this.containerOrgaos.show();
				this.emitter.trigger('inicializarOrgaos');				
				
				switch (this.comboDestinatario.val()) {
				case 'id_ORGAOS':	
					this.labelDestinatarioDefinicao.text('Por fim, selecione os órgãos desejados');
					this.containerOrgaos.removeClass('col-sm-6').addClass('col-sm-12');					
					this.containerUnidades.hide();
					this.containerCargos.hide();
					this.containerFuncoes.hide();
					this.containerPessoas.hide();
					atualizarBtnProximo.call(this, this.comboOrgaos);
					break;
				case 'id_UNIDADES':
					this.labelDestinatarioDefinicao.text('Selecione os órgãos e por fim as unidades desejadas');
					this.containerOrgaos.removeClass('col-sm-12').addClass('col-sm-6');
					this.containerUnidades.show();
					this.containerCargos.hide();
					this.containerFuncoes.hide();
					this.containerPessoas.hide();
					atualizarBtnProximo.call(this, this.comboUnidades);
					break;
				case 'id_CARGOS':
					this.labelDestinatarioDefinicao.text('Selecione os órgãos e por fim os cargos desejados');
					this.containerOrgaos.removeClass('col-sm-12').addClass('col-sm-6');					
					this.containerUnidades.hide();
					this.containerCargos.show();
					this.containerFuncoes.hide();
					this.containerPessoas.hide();
					atualizarBtnProximo.call(this, this.comboCargos);
					break;
				case 'id_FUNCOES':
					this.labelDestinatarioDefinicao.text('Selecione os órgãos e por fim as funções desejadas');
					this.containerOrgaos.removeClass('col-sm-12').addClass('col-sm-6');					
					this.containerUnidades.hide();
					this.containerCargos.hide();
					this.containerFuncoes.show();
					this.containerPessoas.hide();
					atualizarBtnProximo.call(this, this.comboFuncoes);
					break;
				case 'id_PESSOAS':
					this.labelDestinatarioDefinicao.text('Selecione os órgãos e por fim as pessoas desejadas');
					this.containerOrgaos.removeClass('col-sm-12').addClass('col-sm-6');
					this.containerUnidades.hide();
					this.containerCargos.hide();					
					this.containerFuncoes.hide();					
					this.containerPessoas.show();
					atualizarBtnProximo.call(this, this.comboPessoas);
					break;
				}											
				break;
			}
			
			atualizarEtapa.call(this, 1);
		} else if (this.etapaAtual + 1 >= this.etapas.length) {
			salvar.call(this);
		}
	}
	
	function onBtnConfirmacaoModalClicado() {		
		if (this.btnConfirmacaoModal.hasClass('is-pesquisa')) {			
			location.href = '../configuracao/listar';
			return false;
		}		
		
		switch (this.etapas[this.etapaAtual].id) {
		case 'selecaoModelos':
			this.selecaoModelosOK = true;
			break;
		case 'selecaoConfiguracao':
			this.selecaoConfiguracaoOK = true;
			break;
		case 'selecaoDestinatario':
			this.selecaoDestinatarioOK = true;
			break;
		case 'selecaoDestinatarioDefinicao':
			this.selecaoDestinatarioDefinicaoOK = true;
			break;
		}
				
		onBtnProximoClicado.call(this, false);			
	}
	
	function onComboDestinatarioSelecionado() {		
		this.comboVisibilidade.html('');
		this.comboVisibilidade.append('<option value="">Agora, selecione como deve ser a visibilidade</option>');
		switch (this.comboDestinatario.val()) {
		case 'id_ORGAOS':	
			this.comboVisibilidade.append('<option value="id_PODE">Pode - órgaos selecionados poderão visualizar os modelos</option>');
			this.comboVisibilidade.append('<option value="id_NAO_PODE">Não Pode - órgãos selecionados não poderão visualizar os modelos</option>');
			break;
		case 'id_UNIDADES':
			this.comboVisibilidade.append('<option value="id_PODE">Pode - unidades selecionadas poderão visualizar os modelos</option>');
			this.comboVisibilidade.append('<option value="id_NAO_PODE">Não Pode - unidades selecionadas não poderão visualizar os modelos</option>');
			break;
		case 'id_CARGOS':
			this.comboVisibilidade.append('<option value="id_PODE">Pode - cargos selecionados poderão visualizar os modelos</option>');
			this.comboVisibilidade.append('<option value="id_NAO_PODE">Não Pode - cargos selecionados não poderão visualizar os modelos</option>');
			break;
		case 'id_FUNCOES':
			this.comboVisibilidade.append('<option value="id_PODE">Pode - funções selecionadas poderão visualizar os modelos</option>');
			this.comboVisibilidade.append('<option value="id_NAO_PODE">Não Pode - funções selecionadas não poderão visualizar os modelos</option>');
			break;
		case 'id_PESSOAS':
			this.comboVisibilidade.append('<option value="id_PODE">Pode - pessoas selecionadas poderão visualizar os modelos</option>');
			this.comboVisibilidade.append('<option value="id_NAO_PODE">Não Pode - pessoas selecionadas não poderão visualizar os modelos</option>');
			break;
		}										
		this.labelVisibilidade.css('opacity', '1');
		this.containerVisibilidade.css({'visibility': 'visible', 'opacity': '1', 'max-width': 'calc(50% - 30px)'});
		this.containerDestinatario.removeClass('col-sm-12').addClass('col-sm-6').css('max-width', '50%');
		desabilitarBtnProximo.call(this);
	}
	
	function onComboVisibilidadeSelecionada() {
		this.labelVisibilidade.css('opacity', '0');
		atualizarBtnProximo.call(this, this.comboVisibilidade);
	}
	
	function onComboTipoConfiguracaoSelecionado() {
		var movimentar = 1;
		if (this.comboTipoConfiguracao.val() == movimentar) {						
			if (this.comboMovimentacao.find('option').length <= 1) {
				this.emitter.trigger('opcaoMovimentarSelecionada');
			}
			
			if (this.comboMovimentacao.val() == null || this.comboMovimentacao.val() === '') {
				this.labelMovimentacao.css('opacity', '1');
			}			
			this.containerMovimentacao.css({'visibility': 'visible', 'opacity': '1', 'max-width': '50%'});
			this.containerTipoConfiguracao.removeClass('col-sm-12').addClass('col-sm-6').css('max-width', '50%');
			atualizarBtnProximo.call(this, this.comboMovimentacao);			
		} else {
			this.containerMovimentacao.css({'visibility': 'collapse', 'opacity': '0', 'max-width': '0px'});
			this.containerTipoConfiguracao.removeClass('col-sm-6').addClass('col-sm-12').css('max-width', '100%');
			this.labelMovimentacao.css('opacity', '0');
			atualizarBtnProximo.call(this, this.comboTipoConfiguracao);
		}				
	}
	
	function onComboMovimentacaoSelecionada() {
		this.labelMovimentacao.css('opacity', '0');
		atualizarBtnProximo.call(this, this.comboMovimentacao);
	}
	
	function atualizarBtnProximo(combo, evento) {						
		if (combo.val() > 0 || 		
				(combo.hasClass('js-siga-select2--destinatario') && combo.val() !== '') ||				
				(combo.hasClass('js-siga-select2--visibilidade') && combo.val() !== '') ||
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
	  
		exibirEtapa.call(this, this.etapaAtual);
	}		
	function salvar() {																			
		var orgaos, unidades, cargos, funcoes, pessoas;			
		
		switch (this.comboDestinatario.val()) {
		case 'id_ORGAOS':	
			orgaos = this.comboOrgaos.val();
			break;
		case 'id_UNIDADES':
			unidades = this.comboUnidades.val();
			break;
		case 'id_CARGOS':
			cargos = this.comboCargos.val();
			break;
		case 'id_FUNCOES':
			funcoes = this.comboFuncoes.val();
			break;
		case 'id_PESSOAS':
			pessoas = this.comboPessoas.val();					
			break;
		}
		
		var dados = {
			'configuracao': {
				'modelos' : this.comboModelos.val(),
				'tipoConfiguracao' : this.comboTipoConfiguracao.val(),
				'movimentacao' : this.comboMovimentacao.val(),
				'destinatarios' : this.comboDestinatario.val().replace('id_', ''),
				'visibilidade': this.comboVisibilidade.val().replace('id_', ''),
				'orgaos': orgaos, 
				'unidades' : unidades, 
				'cargos' : cargos, 
				'funcoes' : funcoes, 
				'pessoas' : pessoas 
			}
		};
								
		$.ajax({
			url: 'nova',
			method: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(dados), 											
			beforeSend: iniciarRequisicao.bind(this),
			complete: finalizarRequisicao.bind(this)
		});									
	}
	
	function iniciarRequisicao() {
		$(this.etapas[this.etapaAtual]).hide();
		this.spanIndicadorEtapa.parent().hide();
		this.btnProximo.parent().hide();					
		this.spinner.parent().parent().parent().show();
		this.spinner.css({'border': '15px solid rgba(0, 0, 0, .1)', 'border-left-color': '#28a745'});		
		this.spinner.parent().parent().parent().find('h1').html('Salvando configurações...');
		this.spinner.parent().find('.icone-salvo-sucesso').css('opacity', '0');		
	}
	
	function finalizarRequisicao() {
		this.spinner.css('border-color', '#28a745');		
		this.spinner.parent().parent().parent().find('h1').html('Pronto! Suas configurações foram definidas!<br/><a class="btn btn-default text-center mt-2" href="nova" title="Cadastrar Nova Configuração"><i class="fas fa-plus"></i> Cadastrar Nova Configuração</a>');
		this.spinner.parent().parent().parent().find('.icone-salvo-sucesso').css('opacity', '1');					
	}	
	
	function validarCampos(numeroEtapa) {				
		var retorno = { resultado: true, alertaConfirmado: false };
		
		switch (this.etapas[numeroEtapa].id) {
			case 'selecaoModelos':
				retorno.alertaConfirmado = this.selecaoModelosOK; 				
				this.emitter.trigger('validarModelos', retorno);				
				break;
			case 'selecaoConfiguracao':
				break;
			case 'selecaoDestinatario':
				break;
			case 'selecaoDestinatarioDefinicao':
				break;
		}
		
		return retorno.resultado;					
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
			case 'selecaoDestinatario':				
				this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Destinatário</span>');							
				break;
			case 'selecaoDestinatarioDefinicao':	
				switch (this.comboDestinatario.val()) {
				case 'id_ORGAOS':	
					this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Órgãos</span>');
					break;
				case 'id_UNIDADES':
					this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Unidades</span>');
					break;
				case 'id_CARGOS':
					this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Cargos</span>');
					break;
				case 'id_FUNCOES':
					this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Funções</span>');
					break;
				case 'id_PESSOAS':
					this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo  js-titulo-etapa-topo">&nbsp;<i class="fa fa-angle-right" style="color: #000;font-size: 1rem;"></i>&nbsp;Pessoas</span>');
					break;
				}												
				break;
			}					
		}	
		
		switch (this.etapas[this.etapaAtual].id) {				
		case 'selecaoModelos':					
			this.tituloNovaConfiguracao.append('<span class="titulo-etapa-topo--duvida  js-titulo-etapa-topo--duvida"><i class="fas fa-info-circle"></i></span>');		
			this.tituloNovaConfiguracao.find('.js-titulo-etapa-topo--duvida').popover({
				title: 'Seleção de modelos',
				html: true,
				content: 'Nessa etapa você deve selecionar os modelos que deseja aplicar a configuração'
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
		case 'selecaoDestinatario':												
			break;
		case 'selecaoDestinatarioDefinicao':																
			break;
		}
	}
	
	function atualizarSpanIndicadorEtapa(numeroEtapa) {  
		this.spanIndicadorEtapa.removeClass('active');	   
	  	this.spanIndicadorEtapa[numeroEtapa].className += " active";
	}

	return Etapas;	
}());

ExConfiguracao.ComboMovimentacao = (function() {
	
	function ComboMovimentacao(Etapas) {
		this.Etapas = Etapas;		
		this.combo = Etapas.comboMovimentacao;
		this.spinner = $('.js-spinner--tipo-movimentacao');				
	}
	
	ComboMovimentacao.prototype.iniciar = function() {		
		reset.call(this);	
		this.Etapas.on('opcaoMovimentarSelecionada', onTipoConfiguracaoSelecionada.bind(this));								
	}	
		
	function onTipoConfiguracaoSelecionada() {
		onInicializar.call(this);		
	}
	
	function onInicializar() {								
		var resposta = $.ajax({
			url: 'movimentacoes',
			method: 'GET',									 										
			beforeSend: iniciarRequisicao.bind(this),
			complete: finalizarRequisicao.bind(this)
		});			
		resposta.done(onBuscarMovimentacoesFinalizado.bind(this));		
	}
	
	function onBuscarMovimentacoesFinalizado(movimentacoes) {
		var options = [];		
		if(movimentacoes.list.length > 0) {						
			options.push('<option value="">' + this.combo.data('sigaSelect2Placeholder') + '</option>');
			
			movimentacoes.list.forEach(function(movimentacao){											
				options.push('<option value="' + movimentacao.idTpMov + '">' + movimentacao.descrTipoMovimentacao + '</option>');				
			});							
								
			this.combo.html(options.join(''));	
			this.combo.removeAttr('disabled');									
		} else {			
			reset.call(this);			
		} 			
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');											
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
	
	return ComboMovimentacao;
	
}());

ExConfiguracao.ComboModelo = (function() {
	
	function ComboModelo(Etapas) {		
		this.combo = Etapas.comboModelos;				
		this.spinner = $('.js-spinner--modelo');
		this.Etapas = Etapas;
		this.btnConfirmacaoModal = Etapas.btnConfirmacaoModal;
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
				options.push('<option value="' + modelo.idMod + '">' + modelo.nmMod + '</option>');													
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
			if (this.combo.val().length >= this.quantidadeModelos) {
				this.btnConfirmacaoModal.addClass('hidden');
				sigaModal.enviarTextoEAbrir('confirmacaoModal', 'Você selecionou todos os modelos, se o seu objetivo é criar uma configuração que impacte' 
						+ ' todos os modelos, então você deve criar uma configuração genérica pelo cadastro de configurações antigo.');		  		  
				validacao.resultado = false;
				return false;
			}
			
			if (!validacao.alertaConfirmado && this.combo.val().length > 10) {
				this.btnConfirmacaoModal.removeClass('hidden');
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
		this.combo = Etapas.comboOrgaos;				
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
		if (this.combo[0].length <= 0) {					
			iniciarConfiguracoes.call(this);
			buscar.call(this);
		} else {
			onOrgaoSelecionado.call(this);
		}					
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
		this.comboDestinatario = $('.js-siga-select2--destinatario');
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
		if (this.comboDestinatario.val() === 'id_UNIDADES') {				
			onInicializar.call(this, this.ComboOrgao.combo.val());
		} else {
			reset.call(this);
		}
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


ExConfiguracao.ComboCargo = (function() {
	
	function ComboCargo(ComboOrgao) {
		this.ComboOrgao = ComboOrgao;
		this.comboDestinatario = $('.js-siga-select2--destinatario');
		this.combo = $(".js-siga-multiploselect--cargo");
		this.spinner = $('.js-spinner--cargo');				
	}
	
	ComboCargo.prototype.iniciar = function() {		
		reset.call(this);	
		this.ComboOrgao.on('selecionado', onOrgaoSelecionado.bind(this));		
		var idOrgao = this.ComboOrgao.combo.val();		
		onInicializar.call(this, idOrgao);		
	}	
		
	function onOrgaoSelecionado() {	
		if (this.comboDestinatario.val() === 'id_CARGOS') {				
			onInicializar.call(this, this.ComboOrgao.combo.val());
		} else {
			reset.call(this);
		}
	}
	
	function onInicializar(idOrgaoSelecao) {						
		iniciarConfiguracoes.call(this);
		buscar.call(this, idOrgaoSelecao);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione um ou mais cargos',
			noneResultsText: 'Nenhum cargo encontrado para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} cargo selecionado':'{0} cargos selecionados'},
			selectedTextFormat: 'count > 2'
		});
	}
	
	function buscar(idOrgaoSelecao) {				
		if (idOrgaoSelecao) {		
			var dados = {'dados': { 'idOrgaoSelecao': idOrgaoSelecao }};
			var resposta = $.ajax({
				url: 'cargos',
				method: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(dados), 											
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onBuscarCargosFinalizado.bind(this));
		} else {			
			reset.call(this);
		}	
	}
	
	function onBuscarCargosFinalizado(cargos) {
		var options = [];
		var nomeOrgao = '';		
		if(cargos.list.length > 0) {
			cargos.list.forEach(function(cargo){		
				if (nomeOrgao != cargo.nomeOrgao) {
					if (nomeOrgao != '') options.push('</optgroup>');
					options.push('<optgroup label="' + cargo.nomeOrgao + '">');			
				}						
				options.push('<option value="' + cargo.id + '">' + cargo.nome + '</option>');
				nomeOrgao = cargo.nomeOrgao;												
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
	
	return ComboCargo;
	
}());

ExConfiguracao.ComboFuncao = (function() {
	
	function ComboFuncao(ComboOrgao) {
		this.ComboOrgao = ComboOrgao;
		this.comboDestinatario = $('.js-siga-select2--destinatario');
		this.combo = $(".js-siga-multiploselect--funcao");
		this.spinner = $('.js-spinner--funcao');				
	}
	
	ComboFuncao.prototype.iniciar = function() {		
		reset.call(this);	
		this.ComboOrgao.on('selecionado', onOrgaoSelecionado.bind(this));		
		var idOrgao = this.ComboOrgao.combo.val();		
		onInicializar.call(this, idOrgao);		
	}	
		
	function onOrgaoSelecionado() {	
		if (this.comboDestinatario.val() === 'id_FUNCOES') {				
			onInicializar.call(this, this.ComboOrgao.combo.val());
		} else {
			reset.call(this);
		}
	}
	
	function onInicializar(idOrgaoSelecao) {						
		iniciarConfiguracoes.call(this);
		buscar.call(this, idOrgaoSelecao);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione uma ou mais funções',
			noneResultsText: 'Nenhuma função encontrada para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} função selecionada':'{0} funções selecionadas'},
			selectedTextFormat: 'count > 2'
		});
	}
	
	function buscar(idOrgaoSelecao) {				
		if (idOrgaoSelecao) {		
			var dados = {'dados': { 'idOrgaoSelecao': idOrgaoSelecao }};
			var resposta = $.ajax({
				url: 'funcoes',
				method: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(dados), 											
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onBuscarFuncoesFinalizado.bind(this));
		} else {			
			reset.call(this);
		}	
	}
	
	function onBuscarFuncoesFinalizado(funcoes) {
		var options = [];
		var nomeOrgao = '';		
		if(funcoes.list.length > 0) {
			funcoes.list.forEach(function(funcao){		
				if (nomeOrgao != funcao.nomeOrgao) {
					if (nomeOrgao != '') options.push('</optgroup>');
					options.push('<optgroup label="' + funcao.nomeOrgao + '">');			
				}						
				options.push('<option value="' + funcao.id + '">' + funcao.nome + '</option>');
				nomeOrgao = funcao.nomeOrgao;												
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
	
	return ComboFuncao;
	
}());

ExConfiguracao.ComboPessoa = (function() {
	
	function ComboPessoa(ComboOrgao) {
		this.ComboOrgao = ComboOrgao;
		this.comboDestinatario = $('.js-siga-select2--destinatario');
		this.combo = $(".js-siga-multiploselect--pessoa");
		this.spinner = $('.js-spinner--pessoa');				
	}
	
	ComboPessoa.prototype.iniciar = function() {		
		reset.call(this);	
		this.ComboOrgao.on('selecionado', onOrgaoSelecionado.bind(this));		
		var idOrgao = this.ComboOrgao.combo.val();		
		onInicializar.call(this, idOrgao);		
	}	
		
	function onOrgaoSelecionado() {	
		if (this.comboDestinatario.val() === 'id_PESSOAS') {				
			onInicializar.call(this, this.ComboOrgao.combo.val());
		} else {
			reset.call(this);
		}
	}
	
	function onInicializar(idOrgaoSelecao) {						
		iniciarConfiguracoes.call(this);
		buscar.call(this, idOrgaoSelecao);
	}
	
	function iniciarConfiguracoes() {
		this.combo.selectpicker({
			noneSelectedText: 'Selecione uma ou mais pessoas',
			noneResultsText: 'Nenhuma pessoa encontrada para {0}',
			countSelectedText: function(e,t){return 1==e?'{0} pessoa selecionada':'{0} pessoa selecionadas'},
			selectedTextFormat: 'count > 2'
		});
	}
	
	function buscar(idOrgaoSelecao) {				
		if (idOrgaoSelecao) {		
			var dados = {'dados': { 'idOrgaoSelecao': idOrgaoSelecao }};
			var resposta = $.ajax({
				url: 'pessoas',
				method: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(dados), 											
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onBuscarPessoasFinalizado.bind(this));
		} else {			
			reset.call(this);
		}	
	}
	
	function onBuscarPessoasFinalizado(pessoas) {
		var options = [];
		var nomeOrgao = '';		
		if(pessoas.list.length > 0) {
			pessoas.list.forEach(function(pessoa){		
				if (nomeOrgao != pessoa.nomeOrgao) {
					if (nomeOrgao != '') options.push('</optgroup>');
					options.push('<optgroup label="' + pessoa.nomeOrgao + '">');			
				}						
				options.push('<option value="' + pessoa.id + '">' + pessoa.nome + '</option>');
				nomeOrgao = pessoa.nomeOrgao;												
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
	
	return ComboPessoa;
	
}());

$(function() {
	$('[name=idTpConfiguracao]').addClass('siga-select2');
	$('[data-toggle="popover"]').popover();
	
	var etapas = new ExConfiguracao.Etapas();
	etapas.iniciar();
	
	var comboModelos = new ExConfiguracao.ComboModelo(etapas);
	comboModelos.iniciar();
	
	var comboMovimentacao = new ExConfiguracao.ComboMovimentacao(etapas);
	comboMovimentacao.iniciar();
	
	var comboOrgaos = new ExConfiguracao.ComboOrgao(etapas);
	comboOrgaos.iniciar();
	
	var comboUnidades = new ExConfiguracao.ComboUnidade(comboOrgaos);
	comboUnidades.iniciar();	
	
	var comboCargos = new ExConfiguracao.ComboCargo(comboOrgaos);
	comboCargos.iniciar();
	
	var comboFuncoes = new ExConfiguracao.ComboFuncao(comboOrgaos);
	comboFuncoes.iniciar();
	
	var comboPessoas = new ExConfiguracao.ComboPessoa(comboOrgaos);
	comboPessoas.iniciar();
});