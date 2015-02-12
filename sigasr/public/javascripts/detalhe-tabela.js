/**
 * Helper que auxilia na manipulacao dos detalhes da tabela.
 */
function DetalheHelper($table, formatFunction, dataTable) {
	/**
	 * Adiciona o evento de expandir contrair linha
	 */
	$table.find('tbody td.details-control').each(function() {
		var $me = jQuery(this), 
			hasEvent = $me.hasClass('has-event');
		
		if(!hasEvent) {
			$me.addClass('has-event');
			$me.bind('click', function (event) {
				event.stopPropagation();
				
				var tr = $me.closest('tr'),
					detail = tr.next('tr.detail');
				
				if(detail.size() == 0) {
					var data = tr.find('td'),
						obj = tr.data('json');
					tr.addClass('shown');
					$me.html("-");
					
					formatFunction(dataTable.api().row(tr).data(), obj)
						.insertAfter(tr);
				}
				else {
					detail.remove();
					tr.removeClass('shown');
					$me.html("+");
				}
		    });
		}
	});
	
	/**
	 * Adiciona o evento de expandir todos no botao
	 */
	$table.find('.bt-expandir').each(function() {
		var $btn = jQuery(this);

		if ($btn.hasClass('expandido'))
			$btn.html("-");
		else 
			$btn.html("+");
		
		if(!$btn.hasClass('has-event')) {
			$btn.addClass('has-event');

			$btn.on('click', function(e) {
				var btn = $table.find('.bt-expandir'),
					expandir = !btn.hasClass('expandido');
				
				$table.expandirContrairLinhas(expandir);
				if (expandir == true)
					btn.html("-");
				else 
					btn.html("+");					
				
			});
		}
	});
	
	this.atualizar = function(tr) {
		var detail = tr.next('tr.detail');
		
		if(detail.size() > 0 && tr.hasClass('shown')) {
			detail.remove();
			var data = tr.find('td'),
				obj = tr.data('json');
			
			formatFunction(dataTable.api().row(tr).data(), obj)
				.insertAfter(tr);
		}
	}
	
	this.expandirContrairLinhas = function(expandir) {
		var elements = $table.find('tbody td.details-control'),
			btn = $table.find('.bt-expandir');
		
		if(expandir) {
	    	btn.addClass('expandido');
	    	
	    	elements.each(function() {
	        	var tr = jQuery(this).closest('tr'),
	        		detail = tr.next('tr.detail');
	 		
	    		if(detail.size() == 0) {
	        		var data = tr.find('td'),
	        			obj = tr.data('json');
	        		tr.addClass('shown');
	        		
	        		formatFunction(dataTable.api().row(tr).data(), obj)
	        			.insertAfter(tr);
	       		}
	       		detail.show();
	       		tr.addClass('shown');
	       		elements.html("-");
	       	});
	    } else {
	    	btn.removeClass('expandido');
	    	
	    	elements.each(function() {
	      		var tr = jQuery(this).closest('tr'),
	      			detail = tr.next('tr.detail');
	      		
	      		detail.remove();
				tr.removeClass('shown');
				elements.html("+");
	       	});
	    }
	}
}

function SigaTable (tableSelector) {
	this.table = jQuery(tableSelector);
	this.dataTable = null;
	this.formatFunction = null;
	
	this.dataTableConfig = {
			"language": {
			"emptyTable":     "Não existem resultados",
		    "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
		    "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
		    "infoFiltered":   "(filtrando do total de _MAX_ registros)",
		    "infoPostFix":    "",
		    "thousands":      ".",
		    "lengthMenu":     "Mostrar _MENU_ registros",
		    "loadingRecords": "Carregando...",
		    "processing":     "Processando...",
		    "search":         "Filtrar:",
		    "zeroRecords":    "Nenhum registro encontrado",
		    "paginate": {
		        "first":      "Primeiro",
		        "last":       "Último",
		        "next":       "Próximo",
		        "previous":   "Anterior"
		    },
		    "aria": {
		        "sortAscending":  ": clique para ordenação crescente",
		        "sortDescending": ": clique para ordenação decrescente"
		    }
		}
	};
	
	this.configurar = function(attr, config) {
		this.dataTableConfig[attr] = config;
		return this;
	}
	
	this.criar = function() {
		this.dataTable = this.table.dataTable(this.dataTableConfig);
		return this;
	}
	
	this.detalhes = function(formatFunction) {
		var table = this.table,
			dataTable = this.dataTable;
		
		this.formatFunction = formatFunction;
		this.table.mostrarDetalhes(formatFunction, this.dataTable);
		this.table.on('draw.dt', function() {
			var btn = jQuery('.bt-expandir'),
				expandir = btn.hasClass('expandido');
			
				table.mostrarDetalhes(formatFunction, dataTable);
				table.expandirContrairLinhas(expandir);
		});
		return this;
	}
	
	this.atualizarDetalhes = function(id) {
		var tr = this.table.find("tr[data-json-id=" + id + "]"),
			helper = this.table.data('detalheHelper');
		
		helper.atualizar(tr);
		return this;
	}
}

jQuery.fn.mostrarDetalhes = function(formatFunction, dataTable) {
	var $table = jQuery(this),
		helper = new DetalheHelper($table, formatFunction, dataTable);
	
	$table.data('detalheHelper', helper);
};

/**
 * Expande todas ou contrai todas as linhas da tabela
 */
jQuery.fn.expandirContrairLinhas = function(expandir) {
	var $table = jQuery(this),
		helper = $table.data('detalheHelper');
	
	$table.data('expandir', expandir);
	helper.expandirContrairLinhas(expandir);
}