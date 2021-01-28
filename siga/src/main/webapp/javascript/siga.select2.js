var Componente = Componente || {};

Componente.Select2 = (function() {
	
	function Select2() {}	
	
	Select2.prototype.aplicar = function() {
		$.each($('.siga-select2'), function() {					
			transformarEmSelect2(this, null, null, $(this).data('sigaSelect2Placeholder'));					
		});				
	}	
	
	return Select2;
	
}());

$(function() {
	var select2 = new Componente.Select2();
	select2.aplicar();
});


function transformarEmSelect2(select, idSelect, idDivContainer, descricao) {		
	if(select || (idSelect && idDivContainer)) {
		var select = idSelect ? $(idSelect) : new jQuery(select);
		var container = idSelect ? $(idDivContainer) : select.parent();	    
		var theme = "bootstrap";
		var width = "resolve";
		var language = "pt-BR";	
		var placeholder = ''; 
			
		if (typeof descricao !== 'undefined' && descricao != null) {
			if (descricao.length > 0) {
				placeholder = descricao;
			}			
		}
		
		var matcher = function(argument, selectOptionText) {
			if ($.trim(argument.term) === '') {
				return selectOptionText;
			}
			if (typeof selectOptionText === 'undefined') {
				return null;
			}
			var args = argument.term.toString();
			var texto = selectOptionText.text.toString().toLowerCase();
			var non_asciis = {'a': '[àáâãäå]', 'c': 'ç', 'e': '[èéêë]', 'i': '[ìíîï]', 'n': 'ñ', 'o': '[òóôõö]', 'u': '[ùúûűü]'};
			for (i in non_asciis) { texto = texto.toLowerCase().replace(new RegExp(non_asciis[i], 'g'), i); }
			
			var terms = (args + "").split(" ");
			var strRegex = "";
			for (var i=0; i < terms.length; i++){
				if (terms[i] != "") { 
					var strRegex = strRegex + "(?=.*" + terms[i] + ")";
				}
			}
			var tester = new RegExp(strRegex, "i");
			if (tester.test(texto) || tester.test(selectOptionText.text) ){
				var modifiedData = $.extend({}, selectOptionText, true);
				return modifiedData;
			} else {
				return null;
			}
		};	    	    
		
		if (placeholder.length > 0) {
			select.select2({
				matcher: matcher,
				theme: theme,
				width: width,
				language: language,
				placeholder: placeholder,				
				dropdownParent: container				
			});
		} else {
			select.select2({
				matcher: matcher,
				theme: theme,
				width: width,
				language: language,
				dropdownParent: container
			});
		}
		
		container.on('keyup', function(event) {
			if (event.key == "Escape") {
				select.val('0');
				select.trigger('change');
			}	        	       
		});  		
	}		
}