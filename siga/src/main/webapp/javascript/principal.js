Siga.principal = {

	modules: {
		sigaex: {
            url: "/sigaex/expediente/doc/gadget.action",
            params: {
                idTpFormaDoc: 1,
                apenasQuadro: true
            },
            viewId: "left"            
        },
        sigawf: {
            url: "/sigawf/inbox.action",
            params: {},
            viewId: "right"
        },        
        sigasr: {
            url: "/sigasr/solicitacao/gadget",
            viewId: "rightbottom"
        },
        sigagc: {
            url: "/sigagc/app/gadget",
            viewId: "rightbottom2"
        },
        processos: {
            url: "/sigaex/expediente/doc/gadget.action",
            params: {
                idTpFormaDoc: 2
            },
            viewId: "leftbottom"
        }
    },

    loadModules: function(){
        var self = this;
        
        // Itera sobre o objeto modules definido no inicio desse script.
        $.each(self.modules, function(){
        	var model = this;
        	// Efetua a requisicao e retorna o conteudo 
            var content = Siga.ajax(model.url, model.params, "GET", function(content){  
	        	// Renderiza o conteudo onde o viewId do modulo esta posicionado
	            self.display(model, content);
            });
        });
    },

    display: function(model, content){
    	var self = this;
    	var target = $("#"+model.viewId); // Local onde o conteúdo será renderizado
    	
        if (Siga.isUnauthenticated(content)){        
        	target.html("M&oacute;dulo indispon&iacute;vel");
        }else{
            target.append(content);
            $(target.find(".loading")).hide();
        }
    }
}

// Funcao principal que sera chamada apos o load da pagina
$(function() {
	// Cache false pois o I.E tem serios problemas com cache de chamadas assincronas.
    $.ajaxSetup({ cache: false });
    Siga.principal.loadModules();
});