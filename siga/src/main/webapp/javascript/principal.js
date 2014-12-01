// Funcao principal que sera chamada apos o load da pagina
$(function() {
	// Cache false pois o I.E tem serios problemas com cache de chamadas assincronas.
    $.ajaxSetup({ cache: false });
    Siga.loadModules();
});