"use strict"
function testarLDAP(){
	$.get('/siga/app/admin/ldap/testar?localidade=' + $('#localidade').val())
	.done(function(dados){
		$('.resultado-testarConexao').html(dados)
	})
	.fail(function(dados){
		$('.resultado-testarConexao').html(dados.responseText)
	})
	event.preventDefault()
}

function testarJCE(){
	$.get('/siga/app/admin/ldap/testarjce')
	.done(function(dados){
		$('.resultado-testarJCE').html(dados)
	})
	.fail(function(dados){
		$('.resultado-testarJCE').html(dados.responseText)
	})
	event.preventDefault()
}


function trocarSenhaLDAP(){
	var dados = {}
	dados.dn = $('#distinguishedName').val()
	dados.senha = $('#password').val()
	dados.localidade = $('#localidade').val()
	
	$.ajax({
		  type: "POST",
		  url: '/siga/app/admin/ldap/trocarsenha',
		  data: dados,
		  success: function(dados){
				$('.resultado-trocarSenha').html(dados)
			},
		})
		.fail(function(dados){
			$('.resultado-trocarSenha').html(dados.responseText)
		})

		event.preventDefault()
		
}

function listarPropriedadesLDAP(){
	$.get('/siga/app/admin/ldap/propriedades?localidade=' + $('#localidade').val())
	.done(function(dados){
		$('.resultado-propriedades').html(dados)
	})
	.fail(function(dados){
		$('.resultado-propriedades').html(dados.responseText)
	})
	event.preventDefault()
}


function showStacktrace(nomeDaFuncionalidade){
	$('#stacktrace-' + nomeDaFuncionalidade).show(); 
	$('#linkShowStack-' + nomeDaFuncionalidade).hide();
}