<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<style>
.strength0 {
	width: 250px;
	background: #cccccc;
}

.strength1 {
	width: 50px;
	background: #ff0000;
}

.strength2 {
	width: 100px;
	background: #ff5f5f;
}

.strength3 {
	width: 150px;
	background: #56e500;
}

.strength4 {
	background: #4dcd00;
	width: 200px;
}

.strength5 {
	background: #399800;
	width: 250px;
}

.tabela-senha td {
	padding: 3px 5px 3px 5px;
}
</style>

<script type="text/javascript" language="Javascript1.1">
	/*  converte para maiúscula a sigla do estado  */
	function converteUsuario(nomeusuario) {
		nomeusuario.value = nomeusuario.value.toUpperCase();
	}

	function validateUsuarioForm(form) {
		var s = document.getElementById("passwordStrength").className;
		if (s == "strength0" || s == "strength1" || s == "strength2") {
			alert("Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números");
			return false;
		}
		var p1 = document.getElementById("pass").value;
		var p2 = document.getElementById("pass2").value;
		if (p1 != p2) {
			alert("Repetição da nova senha não confere, favor redigitar.");
			return false;
		}
		return true;
	}

	function passwordStrength(password) {
		var desc = new Array();
		desc[0] = "Inaceitável";
		desc[1] = "Muito Fraca";
		desc[2] = "Fraca";
		desc[3] = "Razoável";
		desc[4] = "Boa";
		desc[5] = "Forte";
		var score = 0;

		//if password bigger than 6 give 1 point
		if (password.length >= 6)
			score++;

		//if password has both lower and uppercase characters give 1 point      
		if ((password.match(/[a-z]/)) && (password.match(/[A-Z]/)))
			score++;

		//if password has at least one number give 1 point
		if ((password.match(/[a-z]/) || password.match(/[A-Z]/))
				&& (password.match(/\d+/)))
			score++;

		//if password has at least one special caracther give 1 point
		if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/))
			score++;

		//if password bigger than 12 give another 1 point
		if (password.length >= 12)
			score++;

		//mininum requirements to be accepted by the AD
		if (score > 2
				&& (password.length < 6 || !password.match(/[a-z]/)
						|| !password.match(/[A-Z]/) || !password.match(/\d+/)))
			score = 2;

		document.getElementById("passwordDescription").innerHTML = desc[score];
		document.getElementById("passwordStrength").className = "strength"
				+ score;
	}
</script>

<siga:pagina popup="false" titulo="Alteração de dados pessoais">
	<!-- main content -->


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.15/jquery.mask.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$( ".celular" ).mask('(99) 99999-9999');

	});
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:if test="${baseTeste}">
				<div id="msgTeste"
					style="font-size: 12pt; color: red; font-weight: bold;">ATENÇÃO:
					Esta é uma versão de testes.</div>
			</c:if>
			<h1 class="gt-form-head">${param.titulo}</h1>

			<h2>${mensagem}</h2>
			<h2 class="gt-form-head">Alteração de dados pessoais</h2>
			<div id="msgInfo" style="font-size: 10pt; color: red;">
				As informações abaixo poderão ser utilizadas na recuperação de sua senha do sistema, em caso de esquecimento ou perda. 
				<br/>Por motivo de segurança, mantenha estes dados sempre atualizados! 
			</div>
			<div class="gt-form gt-content-box tabela-senha">
				<form action="dados_pessoais_gravar"
					onsubmit="return validateUsuarioForm(this);" method="post">
					<input type="hidden" name="page" value="1" />
					<h1>${mensagem }</h1>

					<table>
					<tr>
							<td><label>Senha atual<a href="#"
									title="Informe sua senha para alterar seus dados pessoais."><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px; z-index: 0;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
						</tr>

						<tr>
							<td><input type="password" name="usuario.senhaAtual" class="gt-form-text" /></td>
						</tr>
						<tr>
							<td></td>
						</tr>
						<tr>
							<td><label>E-mail pessoal<a href="#"
									title="Informe um e-mail pessoal. NÃO cadastre seu e-mail institucional!"><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px; z-index: 0;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
						</tr>

						<tr>
							<td><input type="text" name="usuario.emailPessoal"
									onblur="javascript:converteUsuario(this)"
									class="gt-form-text" /></td>
							
						</tr>
						<tr>
							<td></td>
						</tr>

						<tr>
							<td><label>Celular pessoal<a href="#"
									title="Digite o código DDD e os 9 dígitos de seu celular; apenas números."><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px; z-index: 0;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
						</tr>
						
						<tr>
							<td><input type="text" name="usuario.celularPessoal" 
									class="gt-form-text celular" /></td>
						</tr>
						
						<tr>
							<td></td>
						</tr>

						<tr>
							<td><button type="submit" class="gt-btn-medium gt-btn-left">OK</button></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>

