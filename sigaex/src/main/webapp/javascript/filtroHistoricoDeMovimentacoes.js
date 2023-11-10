$(document).ready(function() {
	$('#lotacaoSelect').select2();
	//$('#modeloSelect').select2();
});

function ordenaOpcoesOrdemAlfabetica(selectElement) {
    const options = Array.from(selectElement.options);
    options.sort((a, b) => a.text.localeCompare(b.text));
    selectElement.innerHTML = '';
    options.forEach(option => selectElement.add(option));
}

function removeDuplicateOptions() {
    let select = document.getElementById('lotacaoSelect');
    let seenOptions = new Set();

    // Iterar sobre as opções da combobox em ordem reversa
    for (let i = select.options.length - 1; i >= 0; i--) {
        let optionValue = select.options[i].value;

        // Se o valor já foi visto, remova a opção
        if (seenOptions.has(optionValue)) {
            select.remove(i);
        } else {
            seenOptions.add(optionValue);
        }
    }
}

function filterTable() {
	// Pega valores das lotações selecionadas
	const selectedLotacoes = Array.from(document.getElementById('lotacaoSelect').selectedOptions).map(option => option.value);
	const movimentacoes = getMovimentacoes();
	
	// Exibe ou esconde linhas baseado nos filtros selecionados
	movimentacoes.forEach(row => {
		const lotacaoCell = row.querySelector('td:nth-child(2)');
		if (lotacaoCell && selectedLotacoes.includes(lotacaoCell.textContent.trim())) {
			row.classList.remove('hidden-row'); // Mostra a linha
		} else {
			row.classList.add('hidden-row');    // Oculta a linha
		}
	});
}

let isFiltered = false;

function getMovimentacoes(){
	return document.querySelectorAll('#movsTable tbody tr');
}

function filtraPorModelo() {
	let modelosSelecionados = getModelosSelecionados();
	const movimentacoes = getMovimentacoes()
	let modeloAtual = modelosSelecionados[0];
	let movimentacaoAtual = null
	
	
	//#####################################################
	//Retorna o modelo do documento
	
	documento = "documentoDaMovimentacao";
	modelo = getModeloDoDocumento(documento);
	console.log("modelo");
	console.log("modelo");
	console.log("modelo");
	console.log("modelo");
	console.log(modelo);
	//#####################################################
	
	if (movimentacoes.length > 0) {
	    movimentacaoAtual = movimentacoes[0];
	    console.log("movimentacaoAtual");
	    console.log(movimentacaoAtual);
	} else {
	    console.error('Nenhuma linha encontrada');
	}
	
	console.log(modelosSelecionados);
	console.log("modeloAtual");
	console.log(modeloAtual);
	console.log("movimentacaoAtual");
	console.log(movimentacaoAtual);
	
	
	
	//Para cada movimentação, faça:
		isMovDoModeloSelecionado = isMovimentacaoDoModeloSelecionado(modeloAtual, movimentacaoAtual);
		console.log(isMovDoModeloSelecionado);
		if (isMovDoModeloSelecionado){
			movimentacaoAtual.classList.remove('hidden-row'); // Mostra a linha
		}
		if (!isMovDoModeloSelecionado){
			movimentacaoAtual.classList.add('hidden-row');    // Oculta a linha
		}
}

//alterna entre filtragem e ver todos
//verifica o estado e decide se deve filtrar ou mostrar todas as linhas
function toggleFilter() {
    if (isFiltered) {
        showAllRows();
        document.getElementById('filterButton').textContent = 'Filtrar';
        isFiltered = false;
    } else {
        filterTable(); // Filtra por lotação
        filtraPorModelo() // Filtra por modelo
        document.getElementById('filterButton').textContent = 'Ver Todos';
        isFiltered = true;
    }
}

function showAllRows() {
    const tableRows = document.querySelectorAll('#movsTable tbody tr');
    tableRows.forEach(row => {
        row.classList.remove('hidden-row');
    });
}

function getModelosFromSigaExAPI() {
    $.ajax({
        url: "/sigaex/api/v1/modelos/lista-hierarquica",
        contentType: "application/json",
        dataType: 'json',
        success: function(result) {
            if (result.list && result.list.length > 0) {
                // Processa a lista de modelos
                addModelosToSelect(result.list);
            } else {
                console.log("Nenhum modelo encontrado.");
            }
        },
        error: function(result) {
            console.log("Erro ao buscar modelos: " + result.errormsg);
        },
    });
}

function addModelosToSelect(modelos) {
    var select = document.getElementById('modeloSelect');
    modelos.forEach(function(modelo) {
        var option = document.createElement('option');
        option.value = modelo.idModelo;
        option.text = modelo.nome;
        select.appendChild(option);
    });
}

function getModelosSelecionados() {
	let selectElement = document.getElementById('modeloSelect');
    if (!selectElement) {
        console.error('Elemento select não fornecido');
        return [];
    }
    var selectedOptions = selectElement.selectedOptions;
    var modelosSelecionados = Array.from(selectedOptions).map(function(option) {
        return option.text;
    });
    return modelosSelecionados;
}

function isMovimentacaoDoModeloSelecionado(modeloSelecionado, movimentacao) {
	let documentoDaMovimentacao = getDocumentoDaMovimentacao(movimentacao);
	console.log("documentoDaMovimentacao aaaa");
	console.log(documentoDaMovimentacao);
    let modeloDoDocumento = getModeloDoDocumento(documentoDaMovimentacao);
    return modeloDoDocumento === modeloSelecionado;
}

function getDocumentoDaMovimentacao(movimentacao) {
    // Obtém a quarta célula (td) da movimentação
    const celulaDocumento = movimentacao.cells[3];
    
    if (celulaDocumento) {
        // Encontra o primeiro elemento <a> dentro da célula do documento
        const elementoDocumento = celulaDocumento.querySelector('a');
        
        // Se o elemento <a> foi encontrado, extrai o texto do documento
        if (elementoDocumento) {
            const documento = elementoDocumento.textContent.trim();
            return documento;
        } else {
            console.error('Elemento <a> não encontrado na célula do documento');
            return null;
        }
    } else {
        console.error('Célula do documento não encontrada');
        return null;
    }
}


function getModeloDoDocumento(documentoDaMovimentacao) {
    //TODO: trocar o hardcoded pelo parametro real
    let documento = "OTZZ-PAR-2023/00001-A";
    
    //TODO: Buscar o documento no backend
    //Como buscar um documento pelo numero no backend
    
    //TODO: Buscar o modelo do documento no backend(documento)
    let modelo = buscaModeloNoBackend(documento);
    
    
    //TODO: salvar o modelo do documento na variável
    
    //modelo = document.getElementById('modeloTeste').textContent;  // assume que você tem um elemento com id 'modelo'
    return modelo;
}

function buscaDocumentoNoBackend(numeroDoDocumento){
	//TODO: busca documento no backend
	let documento = null;
	return documento;
}

function buscaModeloNoBackend(documento){
	//TODO: busca modelo no backend
	let modelo = null;
	return modelo;
}

//TODO: Testar
function buscaModeloPorId(modeloId, jwt, callback) {
    $.ajax({
        url: "/sigaex/api/v1/modelos/" + modeloId, // Adjust the endpoint as needed
        type: 'GET',
        contentType: "application/json",
        headers: {"Authorization": jwt}, // Include the JWT in the request header for authorization
        dataType: 'json',
        success: function(result) {
            // If the API returns a result, invoke the callback with no error and the result
            callback(null, result);
        },
        error: function (xhr, status, error) {
            // Construct an error message based on the response
            var errorMsg = "Erro na busca do modelo: ";
            if (xhr.responseJSON && xhr.responseJSON.errormsg) {
                errorMsg += xhr.responseJSON.errormsg;
            } else {
                errorMsg += "Status: " + status + ", Error: " + error;
            }
            // Invoke the callback with the error message
            callback(errorMsg, null);
        }
    });
}
/*
//TODO: Testar
// Example
buscaModeloPorId('12345', 'your_jwt_token_here', function(err, modelo) {
    if(err) {
        // Handle error
        console.error(err);
    } else {
        // Process the fetched model
        console.log('Modelo encontrado:', modelo);
    }
});
*/

//TODO: Testar as funções e validar a funcionalidade

// This function prepares the acronym and initiates the API call
async function buscaIdPorSigla(sigla) {
    var siglaFormatted = formataSigla(sigla);
    try {
        var documentoId = await consultaApiPorSigla(siglaFormatted);
        return documentoId; // Return the ID from the API call
    } catch (error) {
        console.error('Erro ao buscar ID:', error);
        throw error; // Rethrow the error to be handled by the caller
    }
}

// This function formats the acronym to be URL-safe
function formataSigla(sigla) {
    return sigla.replace("/", "").replace("-", "");
}

// This function performs the API call to retrieve the document ID
async function consultaApiPorSigla(siglaFormatted) {
    var response = await fetch(`/sigaex/api/v1/documentos/${siglaFormatted}/detalhes`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer your_jwt_token_here" // Replace with your actual JWT
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    var documento = await response.json();
    return documento.id; // Assuming the API returns an object with an 'id' property
}

/*
// Example usage:
(async () => {
    try {
        var id = await buscaIdPorSigla('DOC123');
        console.log('ID do documento:', id);
        // Use the ID for further processing here
    } catch (error) {
        // Handle errors here
        console.error('Erro ao obter ID do documento:', error);
    }
})();
*/
function init() {
    $('#lotacaoSelect').select2();
    $('#modeloSelect').select2();
    ordenaOpcoesOrdemAlfabetica(document.getElementById('lotacaoSelect'));
    removeDuplicateOptions();
    getModelosFromSigaExAPI();
    
    
    
    
}


document.addEventListener('DOMContentLoaded', init);
