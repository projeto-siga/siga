/**
 * Created by hodrigohamalho@gmail.com on 24/07/14.
 */

/**
 * Os modulos definidos na variavel modules abaixo, serao carregados
 * carregados na pagina principal do siga, a variavel params corresponde
 * ao parametros que serao passados na requisicao GET ou POST.
 * O viewID corresponde ao local onde sera renderizado na tela o resultado
 * da consulta
 */

// Quando a pagina for carregada ele inicia este metodo.
// main
window.Siga = {

    modules: {
        sigawf: {
            name: "sigawf",
            url: "/sigawf/inbox.action",
            params: {},
            viewId: "right"
        },
        sigaex: {
            name: "sigaex",
            url: "/sigaex/expediente/doc/gadget.action",
            params: {
                idTpFormaDoc: 1
            },
            viewId: "left"
        },
        sigasr: {
            name: "sigasr",
            url: "/sigasr/solicitacao/gadget",
            params: {},
            viewId: "rightbottom"
        },
        sigagc: {
            name: "sigagc",
            url: "/sigagc/app/gadget",
            params: {},
            viewId: "rightbottom2"
        }
    },

    subModules: {
        processos: {
            name: "processos",
            url: "/sigaex/expediente/doc/gadget.action",
            params: {
                idTpFormaDoc: 2
            },
            viewId: "leftbottom"
        }
    },

    currentTimeInMillis: function(){
        return new Date($.now()).getTime();
    },

    display: function(target, text){
        target.append(text);
        $(target.find(".loading")).hide();
    },

    picketlinkResponse: function(textResponse){
        var form = ($.browser.msie ? $(textResponse)[0] : $(textResponse)[1]);
        var action = $(form).attr("action");

        // Pode ser o SAMLRequest ou SAMLResponse
        var samlParamName = $(form).find("input").attr("name");
        var samlParamValue = $(form).find("input").attr("value");
        var samlJson = {};
        samlJson[samlParamName] = samlParamValue;

        return {url: action, params: samlJson};
    },

    ajaxCall: function(ajax, doneCallback){
        var self = this;
        ajax.params["ts"] = self.currentTimeInMillis();

        $.ajax({
            url: ajax.url,
            type: ajax.type,
            data: ajax.params,
            beforeSend: function(){
                if (ajax.target != null){
                    $(ajax.target.find(".loading")).show();
                }
            }
        }).fail(function(){
            ajax.target.html("M&oacute;dulo indispon&iacute;vel");
        }).done(function(response){
            doneCallback(response);
        }).always(function(){
            $(ajax.target.find(".loading")).hide();
        });
    },

    loadSubModule: function(model){
        var self = this;
        if (model.name == "sigaex"){
            model = this.subModules["processos"];
            var target = $("#"+model.viewId);
            self.ajaxCall({url: model.url, type: "GET", params: model.params, target: target}, function(responseText){
                self.display(target, responseText);
            });
        }
    },

    loadModule: function(model){
        var self = this;
        var target = $("#"+model.viewId);

        self.ajaxCall({url: model.url, type: "GET", params: model.params, target: target}, function(textResponse) {
            // Verifica se o SP foi previamente inicializado, caso nao tenha sido apenas renderiza.
            if (textResponse.indexOf("SAMLRequest") > -1){
                var params = self.picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                self.ajaxCall({url: params.url, type: "POST", params: params.params, target: null}, function(textResponse){
                    var params = self.picketlinkResponse(textResponse);

                    self.ajaxCall({url: params.url, type: "POST", params: params.params, target: null}, function(textResponse){
                        if (textResponse instanceof Array) {
                            console.log("Autenticacao falhou para: "+model.name);
                            console.log(" result -> "+textResponse);
                        }else{
                            self.display(target, textResponse);
                            self.loadSubModule(model);
                        }
                    });
                });
            }else{
                self.display(target, textResponse);
                self.loadSubModule(model);
            }
        });
    },

    loadModules: function(){
        this.loadModule(this.modules["sigaex"]);
        this.loadModule(this.modules["sigawf"]);
        this.loadModule(this.modules["sigagc"]);
        this.loadModule(this.modules["sigasr"]);
    }
}
var Siga = window.Siga;

$(function() {
    $.ajaxSetup({ cache: false });
    Siga.loadModules();
});