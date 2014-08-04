/**
 * Created by hodrigohamalho@gmail.com on 24/07/14.
 */
var modules = {};
modules["sigaex"] = { name: "sigaex",
                      url: "/sigaex/expediente/doc/gadget.action?apenasQuadro=true&idTpFormaDoc=1",
                      viewId: "left"
                    }
modules["processos"] = { name: "processos",
                         url: "/sigaex/expediente/doc/gadget.action?idTpFormaDoc=2",
                         viewId: "leftbottom"
                       }

modules["sigawf"] = { name: "sigawf",
                      url: "/sigawf/inbox.action",
                      viewId: "right"
                    }

modules["sigasr"] = { name: "sigasr",
                      url: "/sigasr/solicitacao/gadget",
                      viewId: "rightbottom"
                    }

modules["sigagc"] = { name: "sigagc",
                      url: "/sigagc/app/gadget",
                      viewId: "rightbottom2"
                    }

function getCurrentTimeInMillis(){
    return new Date($.now()).getTime();
}

function addTsAttributeToUrl(url){
    if (url.indexOf("?") != -1){
        url += "&";
    }else{
        url += "?";
    }

    return url + "ts="+getCurrentTimeInMillis();
}
$(function() {
    // Itera sobre todos os modulos do siga
    $.each(modules, function(k, model) {
        var target = $("#"+model.viewId);
        $.ajax({
            url: addTsAttributeToUrl(model.url),
            type: "GET",
            beforeSend: function(){
                $(target.find(".loading")).show();
            }
        }).done(function(result) {
            // Verifica se o SP foi previamente inicializado
            if (result.indexOf("SAMLRequest") > -1){
                var form = $(result)[1];
                var action = $(form).attr("action");
                var samlRequestName = $(form).find("input").attr("name");
                var samlRequestValue = $(form).find("input").attr("value");
                var samlJson = {};
                samlJson[samlRequestName] = samlRequestValue;

                // SEND POST request to IDP with SAMLRequest param
                $.ajax({
                    url: action,
                    type: "POST",
                    data: samlJson
                }).done(function(result) {
                    var form = $(result)[1];
                    var action = $(form).attr("action");

                    var samlResponseName = $(form).find("input").attr("name");
                    var samlResponseValue = $(form).find("input").attr("value");
                    var jsonData = {};
                    jsonData[samlResponseName] = samlResponseValue;

                    // SEND POST request to SP with SAMLResponse param
                    $.ajax({
                        url: action,
                        type: "POST",
                        data: jsonData
                    }).done(function(result) {
                        target.html(result);
                    });
                });
            }else{
                target.html(result);
            }
        }).fail(function() {
            target.html("M&oacute;dulo indispon&iacute;vel");
        });
    });
});