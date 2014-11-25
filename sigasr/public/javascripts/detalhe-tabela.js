/**
 * Helper que auxilia na manipulacao dos detalhes da tabela.
 */
var detalheHelper = function($table, formatFunction0, dataTable0) {
	var formatFunction = formatFunction0,
		dataTable = dataTable0;

	/**
	 * Adiciona o evento de expandir contrair linha
	 */
	$table.find('tbody td.details-control').bind('click', function (event) {
		event.stopPropagation();
		
		var tr = $(this).closest('tr'),
			detail = tr.next('tr.detail');
		
		if(detail.size() == 0) {
			var data = tr.find('td');
			tr.addClass('shown');
    		formatFunction(dataTable.row(tr).data()).insertAfter(tr);
		}
		else {
			// Se alguma linha já está com os detalhes abertos, fecha-o
			if ( detail.is(':visible') ) {
	    		detail.hide();
	    		tr.removeClass('shown');
	    	}
	    	else {
	    		// Abre a linha
	    		detail.show();
	    		tr.addClass('shown');
	    	}
		}
    });
	
	
	/**
	 * Adiciona o evento de expandir todos no botao
	 */
	$table.find('.bt-expandir').on('click', function(e) {
		var btn = $table.find('.bt-expandir'),
			expandir = !btn.hasClass('expandido');
		
		$table.expandirContrairLinhas(expandir);
	});
	
	return {
		expandirContrairLinhas : function(expandir) {
			var elements = $table.find('tbody td.details-control'),
				btn = $table.find('.bt-expandir');
			
			if(expandir) {
		    	btn.addClass('expandido');
		    	
		    	elements.each(function() {
		        	var tr = $(this).closest('tr'),
		        		detail = tr.next('tr.detail');
		 		
		    		if(detail.size() == 0) {
		        		var data = tr.find('td');
		        		tr.addClass('shown');
		        		
		        		formatFunction(dataTable.row(tr).data()).insertAfter(tr);
		       		}
		       		detail.show();
		       		tr.addClass('shown');
		       	});
		    } else {
		    	btn.removeClass('expandido');
		    	
		    	elements.each(function() {
		      		var tr = $(this).closest('tr'),
		      			detail = tr.next('tr.detail');
		    		
		    		detail.hide();
		    		tr.removeClass('shown');
		       	});
		    }
		}
	}
}

/**
 * Adiciona a funcionalidade de mostrar detalhes em um DataTable
 * formatFuncion: A function a ser utilizada para a cricao do detalhe.
 * dataTable: A tabela onde a funcionalidade deve ser adicionada.
 */
$.fn.mostrarDetalhes = function(formatFunction, dataTable) {
	var $table = $(this),
		helper = detalheHelper($table, formatFunction, dataTable);
	
	$table.data('detalheHelper', helper);
};

/**
 * Expande todas ou contrai todas as linhas da tabela
 */
$.fn.expandirContrairLinhas = function(expandir) {
	var $table = $(this),
		helper = $table.data('detalheHelper');
	
	$table.data('expandir', expandir);
	helper.expandirContrairLinhas(expandir);
}