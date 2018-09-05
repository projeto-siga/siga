function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}

$(function() {

	$("[class^='gt-btn-']").not(".btnSelecao").not("a").each(function() {
		var executar = $(this).attr("onClick");
		if (!isBlank(executar)) {
			$(this).attr("executar", executar);
			$(this).removeAttr("onClick");
		}
	});

	$("[class^='gt-btn-']").not(".btnSelecao").not("a").click(function(event) {
		event.preventDefault();
		$("[class^='gt-btn-']").prop('disabled', true);
		$(this).val('Processando...');

		var executar = $(this).attr("executar");
		if (!isBlank(executar)) {
			eval(executar);
		} else {
			$(this.form).submit();
		}
	});

	$(".lnkMotivoLog").click(function(event) {
		event.preventDefault();
		var $formularioMaisPerto = $("#formulario");
		$("#dialog-form").data("link", this).dialog("open");
	});

	// paginacao - inicio
	if (!($("#pagination").length === 0)) {

		var items = $("#htmlgrid tbody tr");

		var numItems = items.length;
		var perPage = 10;

		// only show the first 2 (or "first per_page") items initially
		items.slice(perPage).hide();

		// now setup pagination
		$("#pagination").pagination({
			items : numItems,
			itemsOnPage : perPage,
			cssStyle : "light-theme",
			onPageClick : function(pageNumber) { // this is where the magic happens
				// someone changed page, lets hide/show trs appropriately
				var showFrom = perPage * (pageNumber - 1);
				var showTo = showFrom + perPage;

				items.hide() // first hide everything, then show for the new page
				.slice(showFrom, showTo).show();
			}
		});
	}

	// paginacao - fim

});