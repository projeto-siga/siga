var Documento = Documento || {};

Documento.ReordenarDoc = (function() {
	
	function ReordenarDoc() {
		tabelaOriginal = '';
		idDocsInicial = '';
		idDocsFinal = '';
		this.btnOrdenar = $('#btnOrdenarDocumentos');
		this.tituloTabelaDocumentos = $('.titulo-docs');
		this.divMenuOrdenacao = $('.menu-ordenacao');
		this.tabelaDosDocumentos = $('.tabela-documentos');
		this.btnCancelar = $('#btnCancelarOrdenacao');
		this.btnSalvar = $('#btnSalvarOrdenacao');		
		this.inputHiddenIdDocs = $('#inputHiddenIdDocs');
		this.form = $('#formReordenarDocs');								
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
				
	ReordenarDoc.prototype.iniciar = function() {	
		this.btnOrdenar.on('click', onInicializar.bind(this));
		this.btnCancelar.on('click', onCancelar.bind(this));
		this.btnSalvar.on('click', onSalvar.bind(this));			
	}
	
	function onInicializar() {		
		this.btnOrdenar.css({'display': 'none'});		
		this.tituloTabelaDocumentos.css({'display': 'none'});
		this.divMenuOrdenacao.css({'max-height':'152px', 'opacity':'1', 'left':'0'});			
		idDocsInicial = montarIdDocs(this.tabelaDosDocumentos);	
		tabelaOriginal = this.tabelaDosDocumentos.html();		
		this.tabelaDosDocumentos.addClass('tabela-ordenavel');						

		this.tabelaDosDocumentos.find('tbody').sortable({
			update: function(event, ui) {
				idDocsFinal = montarIdDocs($(this).parent());
				if (idDocsInicial == idDocsFinal)
					$('#btnSalvarOrdenacao').attr('disabled', 'disabled');
				else
					$('#btnSalvarOrdenacao').removeAttr('disabled');
			}
		});
		this.tabelaDosDocumentos.find('tbody').disableSelection();
	}
	
	function onCancelar() {
		this.btnOrdenar.css({'display': 'inline-block'});
		this.tituloTabelaDocumentos.css({'display': 'block'});
		this.divMenuOrdenacao.css({'max-height':'0', 'opacity':'0','left':'-999px'});		
		this.tabelaDosDocumentos.removeClass('tabela-ordenavel');
		this.tabelaDosDocumentos.html(tabelaOriginal);
	}
	
	function onSalvar() {				
		idDocsFinal = montarIdDocs(this.tabelaDosDocumentos);		
		this.inputHiddenIdDocs.val(idDocsFinal);		
	}
		
	function montarIdDocs(tabela) {		
		var idDocs = '';
		
		$(tabela).find('tbody').find('tr').each(function(index) {
			id = this.children[0].textContent.trim();
			
			if ($.isNumeric(id)) {
				if (idDocs.length > 0) idDocs += ";";
				idDocs += id;	
			}									
		});	
		
		return idDocs;
	}	
				
	return ReordenarDoc;
	
}());

$(function() {	
	var ReordenarDoc = new Documento.ReordenarDoc();
	ReordenarDoc.iniciar();	
});