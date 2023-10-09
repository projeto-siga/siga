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