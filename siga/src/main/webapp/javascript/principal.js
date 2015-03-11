Siga.principal = {

    modules: {
        sigaex: {
        	name: "sigaex",
        	url: "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=1&apenasQuadro=true",
            viewId: "left"
        },
        sigawf: {
        	name: "sigawf",
        	url: "/sigawf/inbox.action",
            viewId: "right"
        },
        sigasr: {
        	name: "sigasr",
        	url: "/sigasr/solicitacao/gadget",
            viewId: "rightbottom"
        },
        sigagc: {
        	name: "sigagc",
        	url: "/sigagc/app/gadget",
            viewId: "rightbottom2"
        },
        sigatp: {
        	name: "sigatp",
        	url: "/sigatp/gadget",
            viewId: "rightbottom3"
        },
        processos: {
        	name: "processos",
        	url: "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=2",
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

        Siga.ajax(model.url, {}, "GET", function(response){
        		target.html(response);
        });
    });
});
