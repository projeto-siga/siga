<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-1.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/i18n/jquery.ui.datepicker-pt-BR.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery.maskedinput-1.2.2.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.css" type="text/css" media="screen">

<script>
	var escalas = {};
	
	escalas.componentes = function(){
		escalas.salvar = document.getElementById('salvar');
		escalas.finalizar = document.getElementById('finalizar');
		escalas.cancelar = document.getElementById('cancelar');
		escalas.incluir = document.getElementById("btn-Incluir-DiasDeTrabalho");
		
		escalas.formulario = document.getElementById('formEscalasDeTrabalho');
		escalas.exluir = document.getElementsByName('linkExcluirSelecionados');
	};
	
	escalas.init = function(){
		escalas.componentes();
		
		escalas.salvar.addEventListener("click", function(){
				escalas.submitForm(urlSalvar);
			});
	
		escalas.finalizar.addEventListener('click', function(){
				escalas.submitForm(urlFinalizar);
			});
	
		escalas.cancelar.addEventListener('click', function(){
				window.location = urlCancelar;
			});
	
		escalas.incluir.addEventListener('click', function(){
				var divrow = document.getElementById("rowDiasDeTrabalho");
				var htmlNaoSelecionado = divrow.innerHTML;
				var htmlSelecionado = htmlNaoSelecionado.replace(/naoSelecionado/g, "selecionado");
				var html = '<tr>';
				html = html	+ '<th width="15%" class="obrigatorio">Dia Inicio / Fim </th>';
				html = html + '<td>';
				html = html + htmlSelecionado;
				html = html + '</td>';
				html = html	+ '<td width="8%" ><a class="linkExcluir" name="linkExcluirSelecionados" onclick="escalas.apagaLinha(this)" style="display:inline" href="#">Excluir</a></td>';
				html = html + '</tr>';
	
				$("#htmlgridDiasDeTrabalho tbody").append(html);
				carregarMascaraHora();
			});
	};
	
	escalas.submitForm = function(acao) {
		escalas.formulario.setAttribute('action', acao);
		escalas.formulario.setAttribute('method', 'GET');
		var idxSelect = 0;
		var isSelect = false;
		var x = 0;
		var inputsDiaDeTrabalho =  $('.selecionado');
		for (var i = 0; i < inputsDiaDeTrabalho.length; i++)
		{
			var nome = inputsDiaDeTrabalho[i].name;
			if(nome === undefined) {
				isSelect = true;
				nome = $(inputsDiaDeTrabalho[i]).parent()[idxSelect].name;
			}
			x = ~~(i / 5); 
			var nomeCompleto = (nome == "id") ? "diasDeTrabalho[" + x + "]" : "diasDeTrabalho[" + x + "]." + nome;
			
			if(isSelect) {
				isSelect = false;
				if(nome != nomeCompleto)
					$(inputsDiaDeTrabalho[i]).parent()[idxSelect].setAttribute("name", nomeCompleto);
			} else
				if(nome != nomeCompleto)
					inputsDiaDeTrabalho[i].setAttribute("name", nomeCompleto);
		}
	
	 	var tbodydiadetrabalho = document.getElementById("tbody");
	 	escalas.formulario.submit();
	};
	
	escalas.escapeRegExp = function(str) {
		return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
	};
	
	escalas.replaceAll = function(find, replace, str) {
		return str.replace(new RegExp(escalas.escapeRegExp(find), 'g'), replace);
	};
	
	escalas.apagaLinha = function(link) {
		if ($(link).attr('disabled'))
			return false;
	
		var html = "";
		if (confirm('Tem certeza de que deseja excluir este dia?')) {
			var trExcluir = link.parentNode.parentNode;
			var tabela = trExcluir.parentNode;
			tabela.removeChild(trExcluir);
		}
	};
	
	$(function() {
		escalas.init();
	});
</script>