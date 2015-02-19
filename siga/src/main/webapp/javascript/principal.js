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
        processos: {
        	name: "processos",
            viewId: "leftbottom"
        }
    }

}

// Funcao principal que sera chamada apos o load da pagina
$(function() {
       
    $.each(Siga.principal.modules, function(){ 
    	var model = this;     
    	var target = $("#"+model.viewId);
    	$(target.find(".loading")).show();
    	
        Siga.ajax("/siga/app/principalQuadros/carregaModulo?modulo="+model.name, {}, "GET", function(response){ 
        	target.html(response);
        });
    });
});