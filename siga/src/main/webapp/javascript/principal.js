/**
 * @author Rodrigo Ramalho da Silva
 *         hodrigohamalho@gmail.com
 */

/**
 * Os modulos definidos na variavel modules abaixo, serao carregados
 * na pagina principal do siga. A variavel params corresponde
 * ao parametros que serao passados na requisicao GET ou POST.
 * O viewID corresponde ao local onde sera renderizado na tela o resultado
 * da consulta
 */

window.Siga = {

    modules: {
        sigawf: {
            url: "/sigawf/inbox.action",
            params: {},
            viewId: "right"
        },
        sigaex: {
            url: "/sigaex/expediente/doc/gadget.action",
            params: {
                idTpFormaDoc: 1
            },
            viewId: "left"            
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
    
    display: function(target, text){
        var self = this;
        
        if (self._isUnauthenticated(text)){        
        	target.html("M&oacute;dulo indispon&iacute;vel");
        }else{
            target.append(text);
            $(target.find(".loading")).hide();
        }
    },

    _isUnauthenticated: function(text){
    	// essa primeira verificacao é pra verificar se é do picketlink
    	// a segunda eh pra ver se veio a pagina completa do siga ou soh o que interessa
    	return (text.indexOf("<HTML") > -1 || text.indexOf("<title>") > -1)
    },
    
    _picketlinkResponse: function(textResponse){
        var form = ($.browser.msie ? $(textResponse)[0] : $(textResponse)[1]);
        var action = $(form).attr("action");

        // Pode ser o SAMLRequest ou SAMLResponse
        var samlParamName = $(form).find("input").attr("name");
        var samlParamValue = $(form).find("input").attr("value");
        var samlJson = {};
        samlJson[samlParamName] = samlParamValue;

        return {url: action, params: samlJson};
    },

    loadSubModule: function(model){
        var self = this;

        if (model.submodules){
            $.each(model.submodules, function(){
                self.loadModule(this);
            });
        }
    },

    ajaxCall: function(ajax, doneCallback){
        var self = this;
        var fields = (ajax.type == "POST") ? { withCredentials: true } : {};
        $.ajax({
            url: ajax.url,
            type: ajax.type,
            data: ajax.params,
            // Necessário para cross domain
            xhrFields: fields,
            statusCode: {
                404: function() {
                    if (ajax.target != null)
                        ajax.target.html("M&oacute;dulo indispon&iacute;vel");
                },
                500: function() {
                    if (ajax.target != null)
                        ajax.target.html("Erro interno do servidor. Por favor, entre em contato com um administrador.");
                }
            },
            beforeSend: function(){
                if (ajax.target != null)
                    $(ajax.target.find(".loading")).show();
            }
        }).fail(function(jqXHR, textStatus, errorThrown){
            if (ajax.target != null)
                ajax.target.html("M&oacute;dulo indispon&iacute;vel");
        }).done(function(response){
            doneCallback(response);
        }).always(function(){
            if (ajax.target != null)
                $(ajax.target.find(".loading")).hide();
        });
    },

    loadModule: function(model){
        var self = this;
        // Local onde o conteúdo será renderizado
        var target = $("#"+model.viewId);
        																				   // Este function ja é a resposta da chamada feita (callback)
        self.ajaxCall({url: model.url, type: "GET", params: model.params, target: target}, function(textResponse) {
            // Verifica se o SP foi previamente inicializado, caso nao tenha sido apenas renderiza.
            if (textResponse.indexOf("SAMLRequest") > -1){
                var params = self._picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                self.ajaxCall({url: params.url, type: "POST", params: params.params, target: target}, function(textResponse){
                    var params = self._picketlinkResponse(textResponse);

                    // Envia um POST para o SP com o atributo SAMLResponse da ultima requisicao
                    self.ajaxCall({url: params.url, type: "POST", params: params.params, target: target}, function(textResponse){
                        self.display(target, textResponse);
                    });
                });
            }else{
                self.display(target, textResponse);
            }
        });
    },

    loadModules: function(){
        var self = this;
        $.each(self.modules, function(){
            self.loadModule(this);
        });
    }
}

var Siga = window.Siga;

// Funcao principal que sera chamada apos o load da pagina
$(function() {
	// Cache false pois o I.E tem serios problemas com cache de chamadas assincronas.
    $.ajaxSetup({ cache: false });
    Siga.loadModules();
});