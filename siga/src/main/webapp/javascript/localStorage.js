/**
 * Funções para manipular o Local Storage no navegador do usuário
 * 
 * @global SEPARADOR, IDENTIFICADOR: definir os valores das variáveis globais 
 * @example
 * 		var IDENTIFICADOR = "sigasr/app/solicitacao/editar"; 
		var SEPARADOR = "-"
 */

/**
 * Salva os dados no Local Storage
 * O dado será armazenado com a chave (key) id+SEPARADOR+IDENTIFICADOR
 * 
 *  @param {array} ids: identificadores dos campos que terão seus dados mantidos no local storage
 *  @example 
 *  	var ids = ["solicitacaosolicitanteSpan","formulario_solicitacaoitemConfiguracao_id"];
 */
function salvarLS(ids) {
	for (var i = 0; i < ids.length; i++) {
		if (contemString(ids[i], "Span"))
			localStorage.setItem(ids[i]+SEPARADOR+IDENTIFICADOR, $("#"+ids[i]).html());
		else
			localStorage.setItem(ids[i]+SEPARADOR+IDENTIFICADOR, $("#"+ids[i]).val());
	}
}

/**
 * Recupera os dados do Local Storage e preenche os campos (input, span...) encontrados pelo id
 */
function recuperarLS() {
	var id = "";
	var dado = "";
	for (var i = 0; i < localStorage.length; i++) {
		if (contemString(localStorage.key(i), IDENTIFICADOR)) {
			dado = getItem(localStorage.key(i));
			if (dado != "undefined" || dado != "null") {
				id = localStorage.key(i).split(SEPARADOR)[0];
				if (contemString(id, "Span"))
					$("#"+id).html(dado);
				else
					$("#"+id).val(dado);
			}
		}
	}
}

/**
 * Recupera o dado no Local Storage de acordo com o nome da chave (key)
 * @param {string} nome: nome da chave que identifica o dado armazenado no Local Storage
 * @return {string} retorna o dado armazenado na chave recebida por parâmetro
 */
function getItem(nome) {
	return localStorage.getItem(nome);
}

/**
 * Remove os dados do Local Storage de uma mesma página usando o IDENTIFICADOR
 */
function removerLS() {
	for (var i = localStorage.length - 1, j = 0; i >= j; i--) {
		if (contemString(localStorage.key(i), IDENTIFICADOR)) 
			removeItem(localStorage.key(i));
	}
}

/**
 * Remove o dado no Local Storage de acordo com o nome da chave (key)
 * @param {string} nome: nome da chave que identifica o dado armazenado no Local Storage
 */
function removeItem(nome) {
	localStorage.removeItem(nome);
}

function contemString(campo, string) {
	if (campo.indexOf(string) > -1)
		return true;
	return false;
}