$(document).ready(function() {
	$('#lotacaoSelect').select2();
});

function init() {
    $('#lotacaoSelect').select2();
    $('#modeloSelect').select2();
    ordenaOpcoesOrdemAlfabetica(document.getElementById('lotacaoSelect'));
    removeDuplicateOptions();
    getModelosFromSigaExAPI();
}

function toggleFilter() {
    if (isFiltered) {
        showAllRows();
        document.getElementById('filterButton').textContent = 'Filtrar';
        isFiltered = false;
    } else {
        applyCombinedFilters();
        document.getElementById('filterButton').textContent = 'Ver Todos';
        isFiltered = true;
    }
}

function applyCombinedFilters() {
    const selectedLotacoes = Array.from(document.getElementById('lotacaoSelect').selectedOptions).map(option => option.value);
    const isLotacaoFilterActive = selectedLotacoes.length > 0;

    const modelosSelecionados = removerAcentos(getModelosSelecionados());
    const isModeloFilterActive = modelosSelecionados.length > 0;

    const movimentacoes = getMovimentacoes();

    movimentacoes.forEach(row => {
        const lotacaoMatches = !isLotacaoFilterActive || selectedLotacoes.includes(row.querySelector('td:nth-child(2)').textContent.trim());
        const documento = getDocumentoDaMovimentacao(row);
        const modeloDoDocumento = removerAcentos(getModeloDoDocumento(documento));
        const modeloMatches = !isModeloFilterActive || modelosSelecionados.includes(modeloDoDocumento);

        if (lotacaoMatches && modeloMatches) {
            row.classList.remove('hidden-row');
        } else {
            row.classList.add('hidden-row');
        }
    });
}

function removerAcentos(str) {
	//TODO: desenvolver lógica para tirar acento do modelo e prevenir erros nos filtros
    return str;
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

function getMovimentacoes(){
	return document.querySelectorAll('#movsTable tbody tr');
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

function getModeloDoDocumento(SiglaDoDocumentoDaMovimentacao) {
	let nomeDoModelo = getNomeDoModeloDoDocumentoBySigla(SiglaDoDocumentoDaMovimentacao);	
    return nomeDoModelo;
}

function getNomeDoModeloDoDocumentoBySigla(sigla) {
    var xhr = new XMLHttpRequest();
    var url = window.location.origin + '/sigaex/api/v1/documentos/' + compactarSigla(sigla) + '/consultar-modelo';
    
    xhr.open('GET', url, false); // false para requisição síncrona
    try {
        xhr.send();
        if (xhr.status === 200) {
            var resposta = JSON.parse(xhr.responseText);
            return resposta.nomeDoModelo;
        } else {
            console.error('Erro na API: ' + xhr.status);
            return 'Modelo Desconhecido';
        }
    } catch (erro) {
        console.error('Erro na requisição: ' + erro);
        return 'Modelo Desconhecido';
    }
}

function compactarSigla(sigla) {
    if (sigla === null || sigla === undefined) {
        return null;
    }
    return sigla.replace(/-/g, "").replace(/\//g, "");
}


function getDocumentoBySigla(sigla) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/sigaex/api/v1/documentos/" + compactarSigla(sigla), false); // false para requisição síncrona
    xhr.setRequestHeader("Content-Type", "application/json");

    try {
        xhr.send();
        if (xhr.status === 200) {
            return JSON.parse(xhr.responseText);
        } else {
            console.error('Erro ao buscar documento: ' + xhr.statusText);
            return null;
        }
    } catch (err) {
        console.error('Erro ao buscar documento: ' + err);
        return null;
    }
}

function getModeloById(id) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/sigaex/api/v1/modelos/" + id, false); // false para requisição síncrona
    xhr.setRequestHeader("Content-Type", "application/json");

    try {
        xhr.send();
        if (xhr.status === 200) {
            return JSON.parse(xhr.responseText);
        } else {
            console.error('Erro ao buscar modelo: ' + xhr.statusText);
            return null;
        }
    } catch (err) {
        console.error('Erro ao buscar modelo: ' + err);
        return null;
    }
}

function formataSigla(sigla) {
	if (sigla === null || sigla === undefined) {
        return null;
    }
    return sigla.replace("/", "").replace("-", "");
}

function compactarSigla(sigla) {
    if (sigla === null || sigla === undefined) {
        return null;
    }
    return sigla.replace(/-/g, "").replace(/\//g, "");
}

function getDocumentoPelaSigla(siglaDoDocumento){
    url = "/sigaex/api/v1/documentos/{sigla}";
    url = url.replace("{sigla}", siglaDoDocumento);
    console.log("url" + url);
    let documento = null;
    $.ajax({
        url: url,
        contentType: "application/json",
        dataType: 'json',
        success: function(result) {
            documento = result;
        },
        error: function(result) {
            console.log("Erro ao buscar documento: " + result.errormsg);
        },
    });    
    return documento;
}

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

let isFiltered = false;

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

function isMovimentacaoDoModeloSelecionado(modeloSelecionado, movimentacao) {
	let documentoDaMovimentacao = getDocumentoDaMovimentacao(movimentacao);
    let modeloDoDocumento = getModeloDoDocumento(documentoDaMovimentacao);
    return modeloDoDocumento === modeloSelecionado;
}

async function buscaIdPorSigla(sigla) {
    var siglaFormatted = formataSigla(sigla);
    try {
        let documentoId = await consultaApiPorSigla(siglaFormatted);
        return documentoId;
    } catch (error) {
        console.error('Erro ao buscar ID:', error);
        throw error; 
    }
}

async function consultaApiPorSigla(siglaFormatted) {
    var response = await fetch(`/sigaex/api/v1/documentos/${siglaFormatted}/detalhes`, {
        method: 'GET',
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer your_jwt_token_here"
        }
    });
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    var documento = await response.json();
    return documento.id; 
}

document.addEventListener('DOMContentLoaded', init);