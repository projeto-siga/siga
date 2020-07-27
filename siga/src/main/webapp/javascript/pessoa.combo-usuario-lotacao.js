var MultiploSelect = MultiploSelect || {};

MultiploSelect.ComboLotacao = (function() {
	
	function ComboLotacao() {		
		this.combo = $('.js-siga-multiploselect--lotacao');				
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
		this.inputHiddenLotacoesSelecionadas = $('#inputHiddenLotacoesSelecionadas');
	}
				
	ComboLotacao.prototype.iniciar = function() {	
		this.combo.on('change', onLotacaoAlterada.call(this));
		this.combo.on('changed.bs.select', onLotacaoSelecionada.bind(this));		
		inicializarLotacoes.call(this);
	}
	
	function inicializarLotacoes() {
		var idLotacaoSelecionada = this.inputHiddenLotacoesSelecionadas.val();
		if (idLotacaoSelecionada) {
			var idLotacoes = idLotacaoSelecionada.split(',');
			this.combo.selectpicker('val', idLotacoes);
			this.combo.selectpicker('render');			
		} /* else {
			// this.combo.selectpicker('selectAll');
		}*/
	}
	
	function onLotacaoAlterada() {						
		if (this.combo && this.combo[0].options.length > 1) {
			if (this.combo[0].options[0].text.toLowerCase() == 'selecione') {
				this.combo[0].remove(0);				
				this.combo.removeAttr('disabled');			
				this.combo.parent().removeClass('disabled');
				this.combo.parent().children().removeClass('disabled');	
														
				this.combo.selectpicker('refresh');												
			}
		} else {
			reset.call(this);
		}					
	}	
	
	function onLotacaoSelecionada(e, clickedIndex, isSelected, previousValue) {
		var idLotacaoSelecao = this.combo.val() ? this.combo.val().toString() : '';
		this.inputHiddenLotacoesSelecionadas.val(idLotacaoSelecao);		
		this.emitter.trigger('selecionado', idLotacaoSelecao);		
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().addClass('disabled');
		this.combo.parent().children().addClass('disabled');
				
		this.combo.selectpicker('refresh');
		
		this.inputHiddenLotacoesSelecionadas.val('');
	}
			
	return ComboLotacao;
	
}());

MultiploSelect.ComboUsuario = (function() {
	
	function ComboUsuario(ComboLotacao) {
		this.ComboLotacao = ComboLotacao;
		this.combo = $(".js-siga-multiploselect--usuario");
		this.comboOrgao = $('#idOrgaoUsu');
		this.spinner = $('.spinner');		
		this.inputHiddenUsuariosSelecionados = $('#inputHiddenUsuariosSelecionados');		
	}
	
	ComboUsuario.prototype.iniciar = function() {		
		reset.call(this);	
		this.combo.on('changed.bs.select', onUsuarioSelecionado.bind(this));
		this.ComboLotacao.on('selecionado', onLotacaoSelecionada.bind(this));		
		var idLotacao = this.ComboLotacao.combo.val();		
		inicializarUsuarios.call(this, idLotacao);		
	}	
	
	function onUsuarioSelecionado() {
		atualizarUsuario.call(this);
	}
	
	function atualizarUsuario() {
		var idUsuarioSelecao = this.combo.val() ? this.combo.val().toString() : '';
		this.inputHiddenUsuariosSelecionados.val(idUsuarioSelecao);
	}
	
	function onLotacaoSelecionada(evento, idLotacaoSelecao) {	
		this.inputHiddenUsuariosSelecionados.val('');
		inicializarUsuarios.call(this, idLotacaoSelecao);		
	}
	
	function inicializarUsuarios(idLotacaoSelecao) {		
		if (idLotacaoSelecao) {		
			var dados = {'dados': {'idOrgaoUsu':this.comboOrgao.val(), 'idLotacaoSelecao': idLotacaoSelecao.toString() }};
			var resposta = $.ajax({
				url: '/siga/app/pessoa/usuarios/envioDeEmailPendente',
				method: 'POST',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(dados), 											
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});			
			resposta.done(onBuscarUsuariosFinalizado.bind(this));
		} else {			
			reset.call(this);
		}
	}
	
	function onBuscarUsuariosFinalizado(usuarios) {
		var options = [];
		var nomeLotacao = '';		
		if(usuarios.list.length > 0) {
			usuarios.list.forEach(function(usuario){		
				if (nomeLotacao != usuario.nomeLotacao) {
					if (nomeLotacao != '') options.push('</optgroup>');
					options.push('<optgroup label="' + (usuario.nomeLotacao.length > 50 ? usuario.nomeLotacao.substring(0, 50) + '...' : usuario.nomeLotacao) + '">');			
				}						
				options.push('<option value="' + usuario.id + '">' + (usuario.nome.length > 50 ? usuario.nome.substring(0, 50) + '...': usuario.nome) + '</option>');
				nomeLotacao = usuario.nomeLotacao;												
			});				
			options.push('</optgroup>');
								
			this.combo.html(options.join(''));	
			this.combo.removeAttr('disabled');			
			this.combo.parent().removeClass('disabled');
			this.combo.parent().children().removeClass('disabled');		
			
			var idUsuarioSelecionado = this.inputHiddenUsuariosSelecionados.val();
			if (idUsuarioSelecionado) {
				var idUsuarios = idUsuarioSelecionado.split(',');							
				this.combo.selectpicker('val', idUsuarios);
				this.combo.selectpicker('render');				
			}
		} else {			
			reset.call(this);			
		} 			
	}
	
	function reset() {		
		this.combo.html('');				
		this.combo.attr('disabled', 'disabled');				
		this.combo.parent().children().addClass('disabled');	
		this.spinner.parent().removeClass('disabled');
				
		this.combo.selectpicker('refresh');			
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.combo.parent().css({'width':'90%', 'transition':'width .3s'});
		this.spinner.show();
	}
	
	function finalizarRequisicao() {		
		this.spinner.hide();				
		this.combo.selectpicker('refresh');			
		if (!this.inputHiddenUsuariosSelecionados.val()) {
			// this.combo.selectpicker('selectAll');
			atualizarUsuario.call(this);
		}		
	}
	
	return ComboUsuario;
	
}());

$(function() {	
	var ComboLotacao = new MultiploSelect.ComboLotacao();
	ComboLotacao.iniciar();
	
	var ComboUsuario = new MultiploSelect.ComboUsuario(ComboLotacao);
	ComboUsuario.iniciar();		
});