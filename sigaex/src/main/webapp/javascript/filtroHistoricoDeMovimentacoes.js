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

//alterna entre filtragem e ver todos
//verifica o estado e decide se deve filtrar ou mostrar todas as linhas
function toggleFilter() {
    if (isFiltered) {
        showAllRows();
        document.getElementById('filterButton').textContent = 'Filtrar';
        isFiltered = false;
    } else {
		//TODO: correção os 2 filtros não estão funcionando ao mesmo tempo
        //filtraPorLotacao();
        filtraPorModelo();
        //filtraPorLotacaoEModelo();
        document.getElementById('filterButton').textContent = 'Ver Todos';
        isFiltered = true;
    }
}

function removerAcentos(str) {
    return str;
}

function filtraPorLotacao() {
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

function filtraPorModelo() {
	let modelosSelecionados = removerAcentos(getModelosSelecionados());
	console.log("modelosSelecionados" + modelosSelecionados);
	
	const movimentacoes = getMovimentacoes()
	console.log("movimentacoes" + movimentacoes);
	
	movimentacoes.forEach(movimentacao => {
        let documento = getDocumentoDaMovimentacao(movimentacao); // Obtenha o documento de cada movimentação
        console.log("documento" + documento);
        
        let modeloDoDocumento = removerAcentos(getModeloDoDocumento(documento)); // Obtenha o modelo do documento
		console.log(" modeloDoDocumento" +  modeloDoDocumento);
		
        if (modelosSelecionados.includes(modeloDoDocumento)) {
			console.log("modelosSelecionados" +  modelosSelecionados);
            movimentacao.classList.remove('hidden-row'); // Se o modelo estiver selecionado, mostre a linha
        } else {
            movimentacao.classList.add('hidden-row'); // Se não, oculte a linha
        }
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

function getModeloDoDocumento(documentoDaMovimentacao) {
	//TODO: inserir chamada do endpoint aqui
	
	/* desativado para fazer os 2 filtros funcionarem ao mesmo tempo
	siglaDoDocumento = documentoDaMovimentacao;
	
	let documento = getDocumentoPelaSigla(formataSigla("OTZZ-DES-2023/00011-A"));
	console.log("documento" + documento); //documentoOTZZ-PAR-2023/00001-A

	let idDoDocumento = documento.id;
	
	let modeloDoDocumento = getModeloPeloIDdoDocumento(2);
	console.log("modeloDoDocumento" + modeloDoDocumento);
	*/
	
	
    //TODO: trocar o mock pela lògica real buscando o conteudo do endpoint
    //TODO: Modelos abrangente não ta vindo, corrigir erro
    /*
    let modeloMock = {
        "OTZZ-MEM-2023/00137": "Memorando",
        "OTZZ-DES-2023/00011-A": "Despacho",
        "OTZZ-DES-2023/00021-A": "Despacho",
        "OTZZ-DES-2023/00022-A": "Despacho",
        "OTZZ-CAP-2023/00014-A": "Modelos Abrangentes: Declaração (Modelo livre) (Cap)",
        "OTZZ-DES-2023/00024-A": "Despacho",
        "OTZZ-CAP-2023/00015-A": "Modelos Abrangentes: Declaração (Modelo livre) (Cap)",
        "OTZZ-PAR-2023/00001-A": "Parecer",
    };
	*/
	let modelo = null;
    //modelo = modeloMock[documentoDaMovimentacao] || 'Modelo Desconhecido';
    
    return modelo;
}

function getDocumentoPelaSigla(siglaDoDocumento){
    //get documento na url pelo sigla
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

function getModeloPeloIDdoDocumento(idDoDocumento){
    //get modelo na url pelo id do documento
    url = "/sigaex/api/v1/documentos/{id}/modelo";
    url = url.replace("{id}", idDoDocumento);
    console.log("url" + url);
    let modelo = null;
    $.ajax({
        url: url,
        contentType: "application/json",
        dataType: 'json',
        success: function(result) {
            modelo = result;
        },
        error: function(result) {
            console.log("Erro ao buscar modelo: " + result.errormsg);
        },
    });
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

function isMovimentacaoDoModeloSelecionado(modeloSelecionado, movimentacao) {
	let documentoDaMovimentacao = getDocumentoDaMovimentacao(movimentacao);
	console.log("documentoDaMovimentacao aaaa");
	console.log(documentoDaMovimentacao);
    let modeloDoDocumento = getModeloDoDocumento(documentoDaMovimentacao);
    return modeloDoDocumento === modeloSelecionado;
}

function buscaModeloPorId(modeloId, jwt, callback) {
    $.ajax({
        url: "/sigaex/api/v1/modelos/" + modeloId,
        type: 'GET',
        contentType: "application/json",
        headers: {"Authorization": jwt},
        dataType: 'json',
        success: function(result) {
            callback(null, result);
        },
        error: function (xhr, status, error) {
            var errorMsg = "Erro na busca do modelo: ";
            if (xhr.responseJSON && xhr.responseJSON.errormsg) {
                errorMsg += xhr.responseJSON.errormsg;
            } else {
                errorMsg += "Status: " + status + ", Error: " + error;
            }
            callback(errorMsg, null);
        }
    });
}


document.addEventListener('DOMContentLoaded', init);
