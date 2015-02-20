/**
 * @author Rodrigo Ramalho da Silva
 *         hodrigohamalho@gmail.com
 *         
 */

window.Siga = {
      
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
        $.ajax({
            url: ajax.url,
            type: ajax.type,
            data: ajax.params
        }).fail(function(jqXHR, textStatus, errorThrown){            
            doneCallback(jqXHR.statusText);
        }).done(function(data, textStatus, jqXHR ){
            doneCallback(data);
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
                var plparams = self._picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                self._ajaxCall({url: plparams.url, type: "POST", params: plparams.params}, function(textResponse){                    
                    var plparams = self._picketlinkResponse(textResponse);
                    // Envia um POST para o SP com o atributo SAMLResponse da ultima requisicao
                    self._ajaxCall({url: plparams.url, type: "POST", params: plparams.params}, function(textResponse){
                        callback(textResponse);                                                
                    });
                });
            }else{ // Quando nao precisa de passar pelo fluxo do SAML.
                callback(textResponse);
            }
        });
    }    
}

var Siga = window.Siga;
