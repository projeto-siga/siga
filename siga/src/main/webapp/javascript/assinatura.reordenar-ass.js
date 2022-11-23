var Assinatura = Assinatura || {};

Assinatura.ReordenarAss = (function() {
	
	function ReordenarAss() {
		tabelaOriginal = '';
		idInicial = '';
		idFinal = '';
		this.btnOrdenar = $('#ordemAssinatura');
		this.divMenuOrdenacao = $('.menu-ordenacao');
		this.tabelaDosAssinaturas = $('.tabela-assinaturas');
		this.btnCancelar = $('#btnCancelarOrdenacao');
		this.btnSalvar = $('#btnSalvarOrdenacao');		
		this.inputHiddenIds = $('#inputHiddenIds');
		this.form = $('#formReordenarAss');								
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
				
	ReordenarAss.prototype.iniciar = function() {	
		this.btnOrdenar.on('click', onInicializar.bind(this));
		this.btnCancelar.on('click', onCancelar.bind(this));
		this.btnSalvar.on('click', onSalvar.bind(this));			
	}
	
	function onInicializar() {		
		this.btnOrdenar.css({'display': 'none'});		
		this.divMenuOrdenacao.css({'max-height':'152px', 'opacity':'1', 'left':'0'});			
		idInicial = montarIds(this.tabelaDosAssinaturas);	
		tabelaOriginal = this.tabelaDosAssinaturas.html();		
		this.tabelaDosAssinaturas.addClass('tabela-ordenavel');						

		this.tabelaDosAssinaturas.find('tbody').sortable({
			update: function(event, ui) {
				idFinal = montarIds($(this).parent());
				if (idInicial == idFinal)
					$('#btnSalvarOrdenacao').attr('disabled', 'disabled');
				else
					$('#btnSalvarOrdenacao').removeAttr('disabled');
			}
		});
		this.tabelaDosAssinaturas.find('tbody').disableSelection();
	}
	
	function onCancelar() {
		this.btnOrdenar.css({'display': 'inline-block'});
		this.divMenuOrdenacao.css({'max-height':'0', 'opacity':'0','left':'-999px'});		
		this.tabelaDosAssinaturas.removeClass('tabela-ordenavel');
		this.tabelaDosAssinaturas.html(tabelaOriginal);
	}
	
	function onSalvar() {				
		idFinal = montarIds(this.tabelaDosAssinaturas);		
		this.inputHiddenIds.val(idFinal);		
	}
		
	function montarIds(tabela) {		
		var ids = '';
		
		$(tabela).find('tbody').find('tr').each(function(index) {
			id = this.children[0].textContent.trim();
			
			if (ids.length > 0) ids += ";";
				ids += id;	
								
		});	
		
		return ids;
	}	
				
	return ReordenarAss;
	
}());

$(function() {	
	var ReordenarAss = new Assinatura.ReordenarAss();
	ReordenarAss.iniciar();	
});