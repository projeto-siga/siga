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
    },

    loadModules: function(modules){
      var self = this;

      $.each(modules, function(){
        	var model = this;
        	var target = $("#"+model.viewId);
        	$(target.find(".loading")).show();

          Siga.ajax(model.url, {}, "GET", function(response){
              if (response.indexOf("HTTP Post Binding") > -1){
                Siga.ajax(model.url, {}, "GET", function(sec){
                  target.html(sec);
                });
              }else{
                target.html(response);
              }
          });
      });
    }
}

// Funcao principal que sera chamada apos o load da pagina
$(function() {
	if (Siga.isIE()){
		$.ajaxSetup({ cache: false });
	}

  Siga.principal.loadModules(Siga.principal.modules);
});
