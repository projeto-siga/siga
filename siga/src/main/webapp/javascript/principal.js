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
	if ($.browser.msie){
		$.ajaxSetup({ cache: false });
	}
    
	Siga.ajax("/sigaidp/IDPServlet", {}, "GET", function(idpID){
		if (idpID.indexOf("<html") > -1 || idpID.trim() == "error"){
			window.location.href = "/sigaidp";
		}else{
			$.each(Siga.principal.modules, function(){ 
		    	var model = this;     
		    	var target = $("#"+model.viewId);
		    	$(target.find(".loading")).show();
		    	
		        Siga.ajax("/siga/principalQuadros/carregaModulo.action", {modulo: model.name, idp: idpID.trim()}, "GET", function(response){ 
		        	target.html(response);
		        });
		    });
		}
	});
    
});