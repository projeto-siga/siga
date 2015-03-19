function TabelaDinamica (tableSelector) {
	this.table = jQuery(tableSelector);
	this.jSon = null;
	this.objetoTabela = null;
	
	this.prepararDadosTabela = function(jSon) {
		var objetoTabela = new Object();
		objetoTabela.estrutura = [];
		objetoTabela.dados = [],
		objetoTabela.detalhes = [],
		objetoTabela.podePriorizar = false,
		objetoTabela.podeOrdenar = false,
		objetoTabela.podeRemover = false;

		if (jSon) {
			objetoTabela.podePriorizar = jSon.podePriorizar;
			objetoTabela.podeOrdenar = jSon.podeOrdenar;
			objetoTabela.podeRemover = jSon.podeRemover;
			
			if (jSon.itens)
				objetoTabela.dados = jSon.itens;
			
			if (jSon.colunas)
				for (var i = 0; i < jSon.colunas.length; i++) {
					var coluna = jSon.colunas[i],
						estruturaColuna = { 
							"sTitle" : coluna.titulo, 
							"mDataProp" : coluna.nome,
							"sClass": coluna.classe,
							"bVisible": coluna.exibir,
							"sWidth" : coluna.largura};
					 
					objetoTabela.estrutura.push(estruturaColuna);
				}

			if (jSon.colunasDetalhamento)
				for (var j = 0; j < jSon.colunasDetalhamento.length; j++) {
					var colDetalhe = jSon.colunasDetalhamento[j],
						detalhe = {
							"sTitle" : colDetalhe.titulo,
							"mDataProp" : colDetalhe.nome,
							"mDetalheFormatado" : colDetalhe.temDetalheFormatado};

					objetoTabela.detalhes.push(detalhe);
				}
			
			
		}

		return objetoTabela;
	}
	
	/* Função de formatação para células de detalhes */
	this.formatarDetalhes = function( d ) {
		var tr = $('<tr class="detail">'),
			// 'd' é o objeto contendo os dados da linha
			detailHTML = '<td colspan="6"><table class="datatable" cellpadding="5" cellspacing="0" border="0" style="margin-left:60px;">'+
				'<tr>';

		if (this.objetoTabela && this.objetoTabela.detalhes) {
			for (var i = 0; i < this.objetoTabela.detalhes.length; i++) {
				var detalhe = this.objetoTabela.detalhes[i];
				
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
	
	this.criar = function() {
		this.jSon = this.table.data("json");
		this.objetoTabela = this.prepararDadosTabela(this.jSon);
		return this;
	}
}