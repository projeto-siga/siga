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
            viewId: "left",
            submodules: {
                processos: {
                    name: "processos",
                    url: "/sigaex/expediente/doc/gadget.action",
                    params: {
                        idTpFormaDoc: 2
                    },
                    viewId: "leftbottom"
                }
            }
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

    currentTimeInMillis: function(){
        return new Date($.now()).getTime();
    },

    display: function(target, text){
        var self = this;
        console.log("entrei no display");
        if (text.indexOf("<HTML") > -1 || text.indexOf("<title>") > -1){
            console.log("autenticação falhou");

            $.each(self.modules, function(module){
                if (target.attr("id") == this.viewId){
                    self.loadModule(module);
                }
            });
        }

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

    loadSubModules: function(model){
        var self = this;
        if (model.submodules){
            $.each(model.submodules, function(){
                self.loadModule(this);
            });
        }
    },

    ajaxCall: function(ajax, doneCallback){
        var self = this;
        ajax.params["ts"] = self.currentTimeInMillis();

        $.ajax({
            url: ajax.url,
            type: ajax.type,
            data: ajax.params,
            timeout: 1500,
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
        }).fail(function(){
            console.log("fail!");
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
        var target = $("#"+model.viewId);

        console.log("Ajax request to: "+model.url);
        self.ajaxCall({url: model.url, type: "GET", params: model.params, target: target}, function(textResponse) {
            console.log("resposta do 1 GET-> "+model.name);
            // Verifica se o SP foi previamente inicializado, caso nao tenha sido apenas renderiza.
            if (textResponse.indexOf("SAMLRequest") > -1){
                console.log("Efetuando processamento pelo picketlink: "+model.name);
                var params = self.picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                console.log("Efetuando primeiro POST para: "+model.name);
                self.ajaxCall({url: params.url, type: "POST", params: params.params, target: target}, function(textResponse){
                    console.log("resposta do 1 POST -> "+model.name);
                    var params = self.picketlinkResponse(textResponse);

                    console.log("Efetuando o 2 POST para: "+model.name);
                    self.ajaxCall({url: params.url, type: "POST", params: params.params, target: target}, function(textResponse){
                        console.log("resposta do 2 POST -> "+model.name);
                        self.display(target, textResponse);
                        self.loadSubModules(model);
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

$(function() {
    $.ajaxSetup({ cache: false });
    Siga.loadModules();
});