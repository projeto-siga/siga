Siga.principal = {

    modules: {
        sigaex: {
            url: "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=1&apenasQuadro=true",
            params: {},
            viewId: "left",
            child: {
                processos: {
                    url: "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=2",
                    params: {},
                    viewId: "leftbottom"
                }
            }            
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
        }
    },

    _findModule: function(content, modules){
        var self = this;
        var model = {};

        var array = Object.keys(modules);
        for (var i=0; i < array.length; i++){
            var mod = modules[array[i]];

            if ($($.parseHTML(content)).filter("div#"+array[i]).length > 0){
                model = mod;
                break;                
            }else if(mod.child){
                model = self._findModule(content, mod.child); // recursividade para procurar em todos modulos filhos                
            }            
        };   

        return model;
    },

    loadModule: function(model){
        var self = this;
        
        var target = $("#"+model.viewId);
        $(target.find(".loading")).show();
        var content = Siga.ajax(model.url, model.params, "GET", function(a){                                        
            var model = self._findModule(a, self.modules);
            var target = $("#"+model.viewId);
            self.display(a, target);
            if (model.child){
               self.loadModules(model.child); // recursividade para carregar todos modulos filhos
            }  
        });


        //    url = url.replace(/.*\/\/[\d|\w|\.|-]*(:[0-9]*)*/g, ""); // url a partir do contexto
        //    if (url.indexOf("_=") > -1){
        //        url = url.substring(0, url.indexOf("_=")-1); // remove parametro de cache do ajax
        //    }
                  
        //});
    },

    loadModules: function(modules){
        var self = this;
        $.each(modules, function(){    
            var model = this;     
            self.loadModule(model);
        });
    },

    display: function(content, target){         
        if (Siga.isUnauthenticated(content)){ 
            //alert("certo");
            target.html("Ocorreu um erro, por favor recarregue a página: ");
                             
        }else{
            target.html(content);
        }
    }
}

// Funcao principal que sera chamada apos o load da pagina
$(function() {
    
    // Necessario pro I.E
    if (!Object.keys) {
      Object.keys = function(obj) {
        var keys = [];

        for (var i in obj) {
          if (obj.hasOwnProperty(i)) {
            keys.push(i);
          }
        }

        return keys;
      };
    };

    // Cache false pois o I.E tem serios problemas com cache de chamadas assincronas.    
    Siga.principal.loadModules(Siga.principal.modules);
});