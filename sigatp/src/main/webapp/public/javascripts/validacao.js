/**
 * Funcao que verifica campos obrigatorios em branco,
 * deixando suas bordas vermelhas.
 */

$(document).ready(function() { 
	var nome = window.location.pathname.split("/").pop();
	if (nome == "salvar") {
	   $(".obrigatorio").each(function() {
	       if ($(this).next().get(0).nodeName.indexOf("H") == 0)  {
	    	   if ($(this).next().next().val() == null || $(this).next().next().val() == "" || $(this).next().next().val() == "0,00") {
	    		   $(this).next().next().css({"border": "2px solid red"});
	    	   }
	       }
	       else if ($(this).next().get(0).nodeName == "DIV") {
	           $(this).next().children().each(function() {
	              if ($(this).get(0).nodeName == "SELECT") {
	                 if ($(this).val() == null || $(this).val() == "") {
	                     $(this).css({"border": "2px solid red"}); 
	                 }
	              }
	           });
	       }
	       else { 
	    	   if ($(this).next().val() == null || $(this).next().val() == "" || $(this).next().val() == "0,00") {
	    	 	  $(this).next().css({"border": "2px solid red"});
	    	   }
 	       }
	   });
	}
});  

/*
********************************************************************************************
 Exibe o número de caracteres restantes de uma textarea.
 Para cada textarea, é necessário haver um label vinculado através do atributo 'for'. 

 Exemplo:
<label for="requisicaoTransporte.itinerarios" id="itinerario">Itiner&aacute;rio </label>
<textarea id="itinerarios" name="requisicaoTransporte.itinerarios" rows="6" cols="60" maxLength=${MAX}>${requisicaoTransporte.itinerarios}</textarea>

 Observe que o atributo 'for' do label tem o mesmo valor que o atributo 'name' da textarea. 
********************************************************************************************
mrf, 28/jan/2016
*/
$('textarea').live("keydown", function(e) {
	exibirLimiteCaracteres(this);
});

$('textarea').live("keyup", function(e) {
	exibirLimiteCaracteres(this);
});

$('textarea').live("focus", function(e) {
	exibirLimiteCaracteres(this);
});

//deleta o span com número de caracteres restantes ao deixar a textarea
 $('textarea').live("blur", function(e) {
   	var label = $("label[for='" + this.name +  "']")        
	$(label).find("span.caracteresRestantes").remove();
});

function exibirLimiteCaracteres(textarea){
   	var label = $("label[for='" + textarea.name +  "']")        
	var limite = textarea.getAttribute("maxlength");
	var texto  = textarea.value.length;
	if($(label).find("span.caracteresRestantes").length==0) {
    	var novoSpan = $("<span>").text(" (" + parseInt(limite - texto) + " caracteres restantes)");
    	$(novoSpan).addClass("caracteresRestantes");
    	$(label).append($(novoSpan));
    }
	else {
		$(label).find("span.caracteresRestantes").text(" (" + parseInt(limite - texto) + " caracteres restantes)");
	}   
}    		