

// Funcao principal que sera chamada apos o load da pagina
$(function() {
	var principal = {

		    modules: {
		        sigaex: {
		        	name: "sigaex",
		        	url: "/sigaex/app/expediente/gadget?idTpFormaDoc=1&apenasQuadro=true",
		          viewId: "left"
		        },
		        sigawf: {
		        	name: "sigawf",
		        	url: "/sigawf/app/inbox",
		          viewId: "right"
		        },
		        sigasr: {
		        	name: "sigasr",
		        	url: "/sigasr/app/solicitacao/gadget", 
		          viewId: "rightbottom"
		        },
		        sigagc: {
		        	name: "sigagc",
		        	url: "/sigagc/app/gadget",
		          viewId: "rightbottom2"
		        },
		        sigatp: {
		        	name: "sigatp",
		        	url: "/sigatp/app/application/gadget",
		          viewId: "rightbottom3"
		        },
		        processos: {
		        	name: "processos",
		        	url: "/sigaex/app/expediente/gadget?idTpFormaDoc=2",
		          viewId: "leftbottom"
		        }
		    },

		    render: function(target, text){
		      if (text.indexOf("Bad Request") > -1){
		        text = "<span style='color:red' class='error'>Módulo indisponível</span>";
		      }else if (text.indexOf("Not Found") > -1){
		        text = "<span style='color:red' class='error'>Módulo não encontrado</span>";
		      }

		      target.html(text);
		    },

		    loadModules: function(modules){
		      var self = this;

		      $.each(modules, function(){
		        	var model = this;
		        	var target = $("#"+model.viewId);
		        	$(target.find(".loading")).show();
		        	
		            $.ajax({
		                url: model.url,
		                type: "GET"
		            }).fail(function(jqXHR, textStatus, errorThrown){
		                self.render(target, errorThrown);
		            }).done(function(data, textStatus, jqXHR ){
		                self.render(target, data);
		            });
		      });
		    }
		}	
	
  principal.loadModules(principal.modules);
});
