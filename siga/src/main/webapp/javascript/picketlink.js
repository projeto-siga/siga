/**
 * @author Rodrigo Ramalho da Silva
 *         hodrigohamalho@gmail.com
 *         
 */

/**
 * Os modulos definidos na variavel modules abaixo, serao carregados
 * na pagina principal do siga. A variavel params corresponde
 * ao parametros que serao passados na requisicao GET ou POST.
 * O viewID corresponde ao local onde sera renderizado na tela o resultado
 * da consulta
 * 
 * Os metodos que comecam com underline sao privados.
 */

window.Siga = {
 
    httpMethod: {
    	GET: "GET",
    	POST: "POST",
    	DELETE: "DELETE",
    	UPDATE: "UPDATE"
    },
     
    isUnauthenticated: function(text){
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

    _ajaxCall: function(ajax, doneCallback){
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

    /**
     * url: url a ser requisitada
     * params: parametros a ser passados nessa chamada
     * httpMethod: Metodo HTTP, ex: GET, POST
     * callback: funcao que sera chamada passando o conteudo pego
     */
    ajax: function(url, params, httpMethod, callback){
        var self = this;
        
        self._ajaxCall({url: url, type: httpMethod, params: params}, function(textResponse) {
            // Verifica se o SP foi previamente inicializado, caso nao tenha sido apenas renderiza.
            if (textResponse.indexOf("SAMLRequest") > -1){
                var params = self._picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                self._ajaxCall({url: params.url, type: "POST", params: params.params}, function(textResponse){
                    var params = self._picketlinkResponse(textResponse);

                    // Envia um POST para o SP com o atributo SAMLResponse da ultima requisicao
                    self._ajaxCall({url: params.url, type: "POST", params: params.params}, function(textResponse){
                        callback(textResponse);
                    });
                });
            }else{
            	callback(textResponse);
            }
        });
    }    
}

var Siga = window.Siga;
