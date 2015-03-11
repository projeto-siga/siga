Siga.principal = {

    modules: {
        sigaex: {
        	name: "sigaex",
            viewId: "left"
        },
        sigawf: {
        	name: "sigawf",
            viewId: "right"
        },        
        sigasr: {
        	name: "sigasr",
            viewId: "rightbottom"
        },
        sigagc: {
        	name: "sigagc",
            viewId: "rightbottom2"
        },
        sigatp: {
        	name: "sigatp",
            viewId: "rightbottom3"
        },        
        processos: {
        	name: "processos",
            viewId: "leftbottom"
        }
    }

}

// Funcao principal que sera chamada apos o load da pagina
$(function() {
	if (Siga.isIE()){
		$.ajaxSetup({ cache: false });
	}
    
	$.each(Siga.principal.modules, function(){ 
    	var model = this;     
    	var target = $("#"+model.viewId);
    	$(target.find(".loading")).show();
    	
        Siga.ajax("/siga/principalQuadros/carregaModulo.action", {modulo: model.name}, "GET", function(response){
        	if (response.indexOf("siga-modules") > -1 && response.indexOf("siga-box") > -1){
        		Siga.ajax("/siga/principalQuadros/carregaModulo.action", {modulo: model.name}, "GET", function(sec){
        			target.html(sec);
        		});
        	}else{
        		target.html(response);
        	}
        });
    });
});