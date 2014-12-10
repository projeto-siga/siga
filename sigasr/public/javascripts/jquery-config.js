/**
 * Registra o pre-filter que ira mostrar/esconder o sinal de "carregando..."
 */
$.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
	var carregando = $('#carregando'),
		originalPosition = carregando.css('position');
	
	carregando.css('position', 'fixed');
	carregando.css('z-index', '999');
	carregando.css('display', 'block');
	
	jqXHR.complete(function() {
		carregando.css('position', originalPosition);
		carregando.css('display', 'none');
	});
});