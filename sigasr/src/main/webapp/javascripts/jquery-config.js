/**
 * Registra o block que ira mostrar/esconder o sinal de "carregando..."
 */

var objBlock = { css: { 
    border: 'none', 
    padding: '15px', 
    backgroundColor: '#000', 
    '-webkit-border-radius': '10px', 
    '-moz-border-radius': '10px', 
    'border-radius' : '10px',
    opacity: .5, 
    color: '#fff' 
}, message: '<h2 style="color : #fff;"> Carregando... </h2>' };

$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
	if(/*originalOptions.blockUI == undefined || */originalOptions.blockUI == true) {
		jQuery.blockUI(objBlock);
		
		jqXHR.complete(function() {
			jQuery.unblockUI();
		});
	}
});

/**
 * Configura o jquery datatable para salvar o estado em todas as tabelas (valor padrao)
 */
if($.fn.dataTable) {
	$.extend( $.fn.dataTable.defaults, {
		stateSave: true
	});
}
