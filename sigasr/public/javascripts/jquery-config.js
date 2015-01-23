/**
 * Registra o block que ira mostrar/esconder o sinal de "carregando..."
 */
var objBlock = { message: '<h2> Carregando... </h2>' };

$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
	jQuery.blockUI(objBlock);
	
	jqXHR.complete(function() {
		jQuery.unblockUI();
	});
});