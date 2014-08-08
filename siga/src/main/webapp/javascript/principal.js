/**
 * Created by hodrigohamalho@gmail.com on 24/07/14.
 */

/**
 * Os modulos definido na variavel modules abaixo, serao carregados
 * carregados na pagina principal do siga, a variavel params corresponde
 * ao parametros que serao passados na requisicao GET ou POST.
 * O viewID corresponde ao local onde sera renderizado na tela o resultado
 * da consulta
 */
var MAXIMUM_RETRY_ATTEMPTS = 3;
var modules = {};
var subModule = {};

modules["sigawf"] = {
    name: "sigawf",
    url: "/sigawf/inbox.action",
    params: {
      ts: getCurrentTimeInMillis()
    },
    viewId: "right"
}
modules["sigaex"] = {
    name: "sigaex",
    url: "/sigaex/expediente/doc/gadget.action",
    params: {
        apenasQuadro: true,
        idTpFormaDoc: 1,
        ts: getCurrentTimeInMillis()
    },
    viewId: "left"
}

subModule["processos"] = {
    name: "processos",
    url: "/sigaex/expediente/doc/gadget.action",
    params: {
        idTpFormaDoc: 2,
        ts: getCurrentTimeInMillis()
    },
    viewId: "leftbottom"
}
modules["sigasr"] = {
    name: "sigasr",
    url: "/sigasr/solicitacao/gadget",
    params: {
        ts: getCurrentTimeInMillis()
    },
    viewId: "rightbottom"
}

modules["sigagc"] = {
    name: "sigagc",
    url: "/sigagc/app/gadget",
    params: {
        ts: getCurrentTimeInMillis()
    },
    viewId: "rightbottom2"
}

function getCurrentTimeInMillis(){
    return new Date($.now()).getTime();
}

function display(target, text, model){
    debugger;
    console.log("entrando no display");
    var tmp = $("<div id='tmp'/>");
    tmp.html(text);

    //  Verifica se a pagina deu erro e se deve fazer uma nova tentativa
    if ((tmp.find(".gt-error-page-hd").length && (target.find("[name='retry-count']").val() <= MAXIMUM_RETRY_ATTEMPTS))){
        httpGet(model);
    }else{
        target.append(text);
        $(target.find(".loading")).hide();
    }
}

function incrementRetryCount(target){
    console.log("Incrementando retry-count");
    console.log("de -> "+target.find("[name='retry-count']").val());
    target.find("[name='retry-count']").val( function(i, oldval) {
        return ++oldval;
    });
}

function httpGet(model){
    console.log("entrando no metodo httpGet")
    console.log("modulo -> "+model.name);
    console.log("url -> "+model.url);
    var target = $("#"+model.viewId);

    $.ajax({
        url: model.url,
        type: "GET",
        data: model.params,
        beforeSend: function(){
            incrementRetryCount(target);
        }
    }).done(function(result) {
        display(target, result);
    })
}

function loadSubModule(model){
    if (model.name == "sigaex"){
        httpGet(subModule["processos"]);
    }
}

// Quando a pagina for carregada ele inicia este metodo.
// Eh neste trecho que e o main
$(function() {
    // Itera sobre todos os modulos do siga
    $.each(modules, function(k, model) {
        var target = $("#"+model.viewId);

        // Efetua um GET para o SP (siga, sigaex, sigawf)
        $.ajax({
            url: model.url,
            type: "GET",
            data: model.params,
            beforeSend: function(){
                incrementRetryCount(target);
                $(target.find(".loading")).show();
                console.log("Efetuando o primeiro request (GET) ");
                console.log(" name -> "+model.name);
                console.log(" url -> "+model.url);
                console.log(" params -> "+JSON.stringify(model.params));
                console.log("  -------- ")
            }
        }).done(function(result) {
            // Verifica se o SP foi previamente inicializado
            // Caso nao tenha sido apenas renderiza.
            debugger;
            if (result.indexOf("SAMLRequest") > -1){
                var form = ($.browser.msie ? $(result)[0] : $(result)[1]);
                var action = $(form).attr("action");
                var samlRequestName = $(form).find("input").attr("name");
                var samlRequestValue = $(form).find("input").attr("value");
                var samlJson = {};
                samlJson[samlRequestName] = samlRequestValue;

                console.log("Efetuando o segundo Request (POST) ");
                console.log(" url -> "+action);
                console.log("parametros -> "+JSON.stringify(samlJson));
                console.log("  -------- ")

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                $.ajax({
                    url: action,
                    type: "POST",
                    data: samlJson
                }).done(function(result) {
                    var form = ($.browser.msie ? $(result)[0] : $(result)[1]);
                    var action = $(form).attr("action");

                    var samlResponseName = $(form).find("input").attr("name");
                    var samlResponseValue = $(form).find("input").attr("value");
                    var jsonData = {};
                    jsonData[samlResponseName] = samlResponseValue;

                    console.log("Efetuando o terceiro Request (POST) ");
                    console.log(" url -> "+action);
                    console.log("parametros -> "+JSON.stringify(jsonData));
                    console.log("  -------- ")
                    // Envia um POST para o SP com o SAMLResponse da ultima requisicao
                    $.ajax({
                        url: action,
                        type: "POST",
                        data: jsonData
                    }).done(function(result) {
                        if (result instanceof Array) {
                            console.log("Autenticacao falhou para: "+model.name);
                            console.log(" result -> "+result);
                        }else{
                            loadSubModule(model);
                        }
                        display(target, result, model);
                    });
                });
            }else{
                console.log("Pulou para o else que assume que nao foi capturado pelo picketlink");
                console.log("conferindo --> "+model.name);
                loadSubModule(model);
                display(target, result, model);
            }
        }).fail(function() {
            console.log("Request falhou para o objeto: "+model.name);
            // Se ocorrer qualquer erro diferente de 200 exibe que o modulo esta indisponivel
            display(target, "M&oacute;dulo indispon&iacute;vel", null);
        });
    });
});