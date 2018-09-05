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

    isIE: function(){
    	return (!!navigator.userAgent.match(/MSIE/));
    },

    _picketlinkResponse: function(textResponse){
        var form = $(textResponse)[0];
        if ($(form).attr("action") == null || $(form).attr("action") == 'undefined'){
          form = $(textResponse)[1];
        }

        var action = $(form).attr("action");

        // Pode ser o SAMLRequest ou SAMLResponse
        var samlParamName = $(form).find("input").attr("name");
        var samlParamValue = $(form).find("input").attr("value");
        var samlJson = {};
        samlJson[samlParamName] = samlParamValue;

        return {url: action, params: samlJson};
    },

    _ajaxCall: function(ajax, doneCallback){
      var cacheValue = true;
      if (Siga.isIE()){
       cacheValue = false;
      }

      if (ajax.params == null || ajax.params == 'undefined'){
       ajax.params = {};
      }
      ajax.params["idp"] = sessionStorage.getItem("idp");

      $.ajax({
         url: ajax.url,
         type: ajax.type,
         data: ajax.params,
         cache: cacheValue,
         timeout: ajax.timeout
      }).fail(function(jqXHR, textStatus, errorThrown){
         doneCallback(jqXHR.statusText,jqXHR.status);
      }).done(function(data, textStatus, jqXHR ){
       if (typeof data == "string" && data.indexOf("Não foi Possível completar a Operação.") > 1){
         data = "Não foi Possível completar a Operação.";
       }
       doneCallback(data);
      });
    },

    /**
     * url: url a ser requisitada
     * params: parametros a ser passados nessa chamada
     * httpMethod: Metodo HTTP, ex: GET, POST
     * callback: funcao que sera chamada passando o conteudo pego
     */
    ajax: function(url, params, httpMethod, callback, timeout){
        var self = this;

         self._ajaxCall({url: url, type: httpMethod, params: params, timeout: timeout}, function(textResponse,statusReponse) {
            // Verifica se o SP foi previamente inicializado, caso nao tenha sido apenas renderiza.
            if (typeof textResponse == "string" && textResponse.indexOf("SAMLRequest") > -1){
                var SAMLRequest = self._picketlinkResponse(textResponse);

                // Envia um POST para o IDP com o atributo SAMLRequest da ultima requisicao
                self._ajaxCall({url: SAMLRequest.url, type: "POST", params: SAMLRequest.params}, function(textResponse){
                    var SAMLResponse = self._picketlinkResponse(textResponse);
                    // Envia um POST para o SP com o atributo SAMLResponse da ultima requisicao
                    self._ajaxCall({url: SAMLResponse.url, type: "POST", params: SAMLResponse.params}, function(textResponse){
                      self._ajaxCall({url: url, type: httpMethod, params: params}, function(final){
                          callback(final);
                      });
                    });
                });
            }else{ // Quando nao precisa de passar pelo fluxo do SAML.
                callback(textResponse,statusReponse);
            }
        });
    }
}

var Siga = window.Siga;