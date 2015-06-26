<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<style>
#passwordStrength {
	height: 10px;
	display: block;
	float: left;
}

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

<siga:pagina popup="false" titulo="Troca de Senha">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:if test="${baseTeste}">
				<div id="msgSenha"
					style="font-size: 12pt; color: red; font-weight: bold;">ATENÇÃO:
					Esta é uma versão de testes. Para sua segurança, NÃO utilize a
					mesma senha da versão de PRODUÇÃO.</div>
			</c:if>
			<h1 class="gt-form-head">${param.titulo}</h1>

			<h2>${mensagem}</h2>
			<h2 class="gt-form-head">Trocar senha</h2>
			<div class="gt-form gt-content-box tabela-senha">
				<form action="trocar_senha_gravar"
					onsubmit="return validateUsuarioForm(this);" method="post">
					<input type="hidden" name="page" value="1" />
					<h1>${mensagem }</h1>

					<table>
						<tr>
							<td><label>Matrícula<a href="#"
									title="Ex.:	XX99999, onde XX é a sigla do seu órgão (T2, RJ e ES) e 99999 é o número da sua matrícula."><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px; z-index: 0;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
							<td><label>Senha atual</label></td>
						</tr>

						<tr>

							<td><input type="text" name="usuario.nomeUsuario"
									onblur="javascript:converteUsuario(this)"
									class="gt-form-text" /></td>
							<td><input type="password" name="usuario.senhaAtual" 
									class="gt-form-text" /></td>
						</tr>

						<tr>
							<td><label>Nova Senha<a href="#"
									title="Utilize maiúsculas, minúsculas e números para aumentar a força da senha."><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
							<td><label>Repetição da nova senha</label></td>
							<td><label>Força da nova senha</label></td>
						</tr>

						<tr>
							<td><input type="password" name="usuario.senhaNova" id="pass"
									onkeyup="passwordStrength(this.value)" theme="simple"
									class="gt-form-text" /></td>

							<td><input type="password" name="usuario.senhaConfirma" id="pass2"
									class="gt-form-text" /></td>
							<td><div id="passwordDescription">Senha não informada</div>
								<div id="passwordStrength" class="strength0"></div></td>
						</tr>
						<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;INT_LDAP:Integrar ao Ldap')}">
							<tr>
								<td><input type="checkbox" checked="checked"
									id="trocarSenhaRede" name="usuario.trocarSenhaRede" style="float: left"
									class="gt-form-checkbox"></input><label>Trocar também a
										senha do computador, da rede e do e-mail</label></td>
							</tr>
						</c:if>
						<tr>
							<td><button type="submit" class="gt-btn-medium gt-btn-left">OK</button></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>

