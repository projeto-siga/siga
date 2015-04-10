function TabelaDinamica (tableSelector, modoExibicao) {
	this.table = jQuery(tableSelector);
	this.jSon = null;
	this.objetoTabela = null;
	this.TIPO_COLUNA_TABELA = 't_' + modoExibicao;
	this.TIPO_COLUNA_DETALHE = 'd_' + modoExibicao;
	
	this.prepararDadosTabela = function(jSon) {
		var objetoTabela = new Object();
		objetoTabela.estrutura = [];
		objetoTabela.dados = [],
		objetoTabela.detalhes = [],
		objetoTabela.detalhesSelecionados = [],
		objetoTabela.colunasTabelaJson = [],
		objetoTabela.colunasDetalhamentoJson = [],
		objetoTabela.podePriorizar = false,
		objetoTabela.podeOrdenar = false,
		objetoTabela.podeRemover = false,
		objetoTabela.podeFiltrar = false,
		objetoTabela.podePaginar = false;

		if (jSon) {
			objetoTabela.podePriorizar = jSon.podePriorizar;
			objetoTabela.podeOrdenar = jSon.podeOrdenar;
			objetoTabela.podeRemover = jSon.podeRemover;
			objetoTabela.podeFiltrar = jSon.podeFiltrar;
			objetoTabela.podePaginar = jSon.podePaginar;
			
			if (jSon.itens)
				objetoTabela.dados = jSon.itens;
			
			if (jSon.colunas) {
				for (var i = 0; i < jSon.colunas.length; i++) {
					var coluna = jSon.colunas[i],
						estruturaColuna = { 
							"sTitle" : coluna.titulo, 
							"mDataProp" : coluna.nome,
							"sName" : coluna.nome,
							"sClass" : coluna.classe,
							"bVisible" : coluna.exibir,
							"sWidth" : coluna.largura};
					 
					objetoTabela.estrutura.push(estruturaColuna);
				}
				
				try {
					objetoTabela.colunasTabelaJson = JSON.parse(jSon.colunasTabelaJson);
				}
				catch(error) {
				}
			}

			if (jSon.colunasDetalhamento) {
				for (var j = 0; j < jSon.colunasDetalhamento.length; j++) {
					var colDetalhe = jSon.colunasDetalhamento[j],
						detalhe = {
							"sTitle" : colDetalhe.titulo,
							"mDataProp" : colDetalhe.nome,
							"bVisible" : coluna.exibir,
							"sName" : colDetalhe.nome
						};

					objetoTabela.detalhes.push(detalhe);
				}
				
				objetoTabela.detalhesSelecionados = objetoTabela.detalhes;
				
				try {
					objetoTabela.colunasDetalhamentoJson = JSON.parse(jSon.colunasDetalhamentoJson);
				}
				catch (error) {
				}
			}
		}

		return objetoTabela;
	}
	
	this.configurar = function(attr, config) {
		this.config[attr] = config;
		return this;
	}
	
	this.criar = function() {
		this.jSon = this.table.data("json");
		this.objetoTabela = this.prepararDadosTabela(this.jSon);
		return this;
	}
	
	this.alterarColunasTabela = function(listaOpcoes, selecionadas) {
		for (var i = 0; i < listaOpcoes.length; i++) {
			var opcao = listaOpcoes[i],
				showColumn = (jQuery.inArray(opcao, selecionadas) >= 0),
				coluna = tabelaDinamica.getColunaPorNome(tabelaDinamica.objetoTabela.estrutura, opcao.value);
			
			tabelaDinamica.table.dataTable.api().column(opcao.value + ':name').visible(showColumn);
			
			if (coluna) {
				coluna.bVisible = showColumn;
				tabelaDinamica.atualizarCookieColuna(coluna, tabelaDinamica.TIPO_COLUNA_TABELA);
			}
		}
	}
	
	this.alterarColunasDetalhamento = function(selecionadas) {
		this.objetoTabela.detalhesSelecionados = [];
		
		if (selecionadas) {
			for (var i = 0; i < this.objetoTabela.detalhes.length; i++) {
				var colunaDetalhes = this.objetoTabela.detalhes[i];
				
				for (var j = 0; j < selecionadas.length; j++) {
					var colunaSelecionada = selecionadas[j];
					
					if (colunaDetalhes.sName == colunaSelecionada.value) {
						this.objetoTabela.detalhesSelecionados.push(colunaDetalhes);
						break;
					}
				}
			}
		}
		
		for (var i = 0; i < this.objetoTabela.detalhes.length; i++) {
			var opcao = this.objetoTabela.detalhes[i],
				showColumn = (jQuery.inArray(opcao, tabelaDinamica.objetoTabela.detalhesSelecionados) >= 0),
				coluna = tabelaDinamica.getColunaPorNome(tabelaDinamica.objetoTabela.detalhes, opcao.sName);
			
			if (coluna) {
				coluna.bVisible = showColumn;
				tabelaDinamica.atualizarCookieColuna(coluna, tabelaDinamica.TIPO_COLUNA_DETALHE);
			}
		}
	}
	
	this.atualizarColunasSelecionadas = function(select, colunas, tipo) {
		this.updateColumnValuesFromCookies(colunas, tipo);
		this.addOptions(select, colunas);
		
		if (tipo == this.TIPO_COLUNA_TABELA && select)
			this.alterarColunasTabela(select[0].options, select[0].selectedOptions);
		else if (tipo == this.TIPO_COLUNA_DETALHE)
			this.alterarColunasDetalhamento(select[0].selectedOptions);
	}
	
	this.addOptions = function(select, colunas) {
		if (colunas) {
			for (var i = 0; i < colunas.length; i++) {
				var coluna = colunas[i],
					opt = $('<option />', {
					value: coluna.nome,
					text: coluna.titulo,
					selected: coluna.exibir,
					disabled: colunas.alteravel
				});

				opt.appendTo(select);
			}
		}

		select.multiselect('refresh');
	}
	
	this.updateColumnValuesFromCookies = function(colunas, tipo) {
		if (colunas) {
			for (var i = 0; i < colunas.length; i++) {
				var coluna = colunas[i],
					colunaInCookie = jQuery.cookie(tipo + coluna.nome);
				
				try {
					colunaInCookie = JSON.parse(colunaInCookie);
				}
				catch(error) {
				}
				
				// caso já exista no cookie, atualiza
				if (colunaInCookie)
					coluna.exibir = colunaInCookie.bVisible;
			}
		}
	}
	
	this.atualizarCookieColuna = function(coluna, tipo) {
		var colunaCookie = {};
		colunaCookie.bVisible = coluna.bVisible;
		
		jQuery.cookie(tipo + coluna.sName, JSON.stringify(colunaCookie), {expires:1000});
	}
	
	this.getColunaPorNome = function(listaColunas, nome) {
		for (var i = 0; i < listaColunas.length; i++) {
			var coluna = listaColunas[i];
			
			if (coluna.sName == nome)
				return coluna;
		}
		
		return null;
	}
}

/* Função de formatação para células de detalhes */
TabelaDinamica.prototype.formatarDetalhes = function( d, objetoTabela ) {
	var tr = $('<tr class="detail">'),
		// 'd' é o objeto contendo os dados da linha
		detailHTML = '<td colspan="6"><table class="datatable" cellpadding="5" cellspacing="0" border="0" style="margin-left:60px;">'+
			'<tr>';

	if (objetoTabela && objetoTabela.detalhesSelecionados) {
		for (var i = 0; i < objetoTabela.detalhesSelecionados.length; i++) {
			var detalhe = objetoTabela.detalhesSelecionados[i];
			
			detailHTML += '<tr><td style="min-width: 140px;"><b>' + detalhe.sTitle + ':</b></td>';

			if (detalhe.mDetalheFormatado)
				 detailHTML += d[detalhe.mDataProp];
			else
				detailHTML += '<td>' + d[detalhe.mDataProp] + '</td>';

			detailHTML += '</tr>';
		}
	}

	detailHTML += '</tr></table></td>';

    return tr.append(detailHTML);
}