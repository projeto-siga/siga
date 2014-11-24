/**
 * Prepara a tabela para que a mesma funcione com detalhes
 */

var detalheHelper = function($table, formatFunction0, dataTable0) {
	var formatFunction = formatFunction0,
		dataTable = dataTable0;
	
	$table.on('click', 'tbody td.details-control', function () {
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

$.fn.mostrarDetalhes = function(formatFunction, dataTable) {
	var $table = $(this),
		helper = detalheHelper($table, formatFunction, dataTable);
	
	$table.data('detalheHelper', helper);
};

$.fn.expandirContrairLinhas = function(expandir) {
	var $table = $(this),
		helper = $table.data('detalheHelper');
	
	if($table.data('expandir') != expandir) {
		$table.data('expandir', expandir);
		helper.expandirContrairLinhas(expandir);
	}
}

//$.fn.expandirContrairLinhas = function(btn, expandir, formatFunction, dataTable) {
//	var elements = $('tbody td.details-control');
//    	
//	if(expandir) {
//    	btn.addClass('expandido');
//    	
//    	elements.each(function() {
//        	var tr = $(this).closest('tr'),
//        		detail = tr.next('tr.detail');
// 		
//    		if(detail.size() == 0) {
//        		var data = tr.find('td');
//        		tr.addClass('shown');
//        		
//        		formatFunction(dataTable.row(tr).data()).insertAfter(tr);
//       		}
//       		detail.show();
//       		tr.addClass('shown');
//       	});
//    } else {
//    	btn.removeClass('expandido');
//    	
//    	elements.each(function() {
//      		var tr = $(this).closest('tr'),
//      			detail = tr.next('tr.detail');
//    		
//    		detail.hide();
//    		tr.removeClass('shown');
//       	});
//    }
//};
