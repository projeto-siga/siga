var Componente = Componente || {};

Componente.MultiploSelect = (function() {
	
	function MultiploSelect() {}	
	
	MultiploSelect.prototype.aplicar = function() {
		$.each($('.siga-multiploselect'), function() {
			transformarEmMultiploSelect(this);
		});				
	}	
	
	function transformarEmMultiploSelect(select) {
		$(select).addClass('selectpicker').attr('multiple', '').val('');		
		aplicarConfiguracoes();				
	}		
	
	function aplicarConfiguracoes() {
		var config = $.fn.selectpicker.Constructor.DEFAULTS;
		
		config.style = "";
        config.noneSelectedText = "Nenhum item selecionado";        
        config.noneResultsText = "Nenhum resultado encontrado para {0}";
        config.countSelectedText = function(e,t){return 1==e?"{0} item selecionado":"{0} itens selecionados"},
        config.maxOptionsText = function(e,t){return[1==e?"Limite alcançado ({n} item max)":"Limite alcançado ({n} items max)",1==t?"Limite do grupo alcançado ({n} item max)":"Limite do grupo alcançado ({n} items max)"]},
        config.selectAllText ="Marcar Todos",
        config.deselectAllText = "Desmarcar Todos",        
        config.doneButtonText = "Fechar",    
        config.width = "100%";
        config.liveSearch = true;
        config.actionsBox = true;
        config.selectedTextFormat = "count > 3";        
        
        var screenHeight = screen.height;
        if (screenHeight >= 1080) {
        	config.size = 17;
        } else if(screenHeight >= 1024 && screenHeight < 1080) {
        	config.size = 15;
        } else if (screenHeight >= 900 && screenHeight < 1024) {
        	config.size = 12;
        } else if (screenHeight >= 720 && screenHeight < 900) {
        	config.size = 6;
        } else if (screenHeight >= 600 && screenHeight < 720) {
        	config.size = 2;
        } else {
        	config.size = 'auto';
        }
        
	}
	
	return MultiploSelect;
	
}());

$(function() {
	var MultiploSelect = new Componente.MultiploSelect();
	MultiploSelect.aplicar();			
});
