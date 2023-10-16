$(document).ready(function() {
	$('#lotacaoSelect').select2();
});

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
