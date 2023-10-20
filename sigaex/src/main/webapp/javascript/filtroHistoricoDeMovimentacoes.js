$(document).ready(function() {
	$('#lotacaoSelect').select2();
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
	
	const tableRows = document.querySelectorAll('#movsTable tbody tr');
	
	// Exibe ou esconde linhas baseado nos filtros selecionados
	tableRows.forEach(row => {
		const lotacaoCell = row.querySelector('td:nth-child(2)');
		if (lotacaoCell && selectedLotacoes.includes(lotacaoCell.textContent.trim())) {
			row.classList.remove('hidden-row'); // Mostra a linha
		} else {
			row.classList.add('hidden-row');    // Oculta a linha
		}
	});
}

let isFiltered = false;

//alterna entre filtragem e ver todos
//verifica o estado e decide se deve filtrar ou mostrar todas as linhas
function toggleFilter() {
    if (isFiltered) {
        showAllRows();
        document.getElementById('filterButton').textContent = 'Filtrar';
        isFiltered = false;
    } else {
        filterTable();
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

function init() {
    $('#lotacaoSelect').select2();
    ordenaOpcoesOrdemAlfabetica(document.getElementById('lotacaoSelect'));
    removeDuplicateOptions();
}

document.addEventListener('DOMContentLoaded', init);
