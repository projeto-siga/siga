/*
* uteis formatar campo moeda
*/
$(function() {
	var vetorValores = $(".valorFormatado").val().split('.');
	if(vetorValores[1].length == 1) {
		$(".valorFormatado").val($(".valorFormatado").val() + '0');
	}
	$(".valorFormatado").maskMoney({showSymbol:false, decimal:',', thousands:'.', precision:2});
	$(".valorFormatado").maskMoney('mask');
});