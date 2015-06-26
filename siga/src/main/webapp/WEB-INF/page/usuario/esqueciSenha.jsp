<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<style>
#passwordStrengthMet1, #passwordStrengthMet2 {
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

.email-invalido {
	display: block;
	color: red;
	font-style: bold;
}

.oculto {
	display: none;
}


</style>

<script type="text/javascript" language="Javascript1.1">

function checkIntegradoAD(){
	
	if ('${proxima_acao}' == 'incluir_usuario_gravar'){
		var matricula = document.getElementById('txtMatricula').value;
		PassAjaxResponseToFunction('integracao_ldap?matricula=' + matricula, 'exibirDadosIntegracaoAD',  false,true, null);
	}
}

function exibirDadosIntegracaoAD(response,param){
	if (response != "0"){
		document.getElementById('dadosIntegracaoAD').style.display = 'block';
		document.getElementById('msgExplicacao').innerHTML = 'Seu órgão está integrado ao AD. Sua senha de rede email serão alteradas.';
	}else{
		document.getElementById('dadosIntegracaoAD').style.display = 'none';
		document.getElementById('passMet1').value = "";
		document.getElementById('pass2Met1').value = "";
		document.getElementById('msgExplicacao').innerHTML = 'O sistema gera uma nova senha aleatoriamente e a envia para o email da pessoa informada.';
	}
}

function checkEmailValido(){
	var matricula = document.getElementById('txtMatricula').value;
	$.ajax({
		method:'GET',
		url: 'check_email_valido?matricula=' + matricula,
		success: function(data){permitirInclusaoUsuario(data)},
		error: function(data){permitirInclusaoUsuario(data)} 
	});
}

function permitirInclusaoUsuario(response,param){
	if (response == '0'){
		document.getElementById('painel-dados-usuario').style.display = 'block';
		document.getElementById('msgEmail').className = 'oculto';
	}else{
		document.getElementById('painel-dados-usuario').style.display = 'none';
		document.getElementById('msgEmail').className = 'email-invalido';
		$('#msgEmail').text(response);

	}

}

/*  converte para maiúscula a sigla do estado  */
function converteUsuario(nomeusuario){
  re= /^[a-zA-Z0-9]{2}\d{4,6}$/;
  tmp = nomeusuario.value;
  if (tmp.match(re)){      
      nomeusuario.value=tmp.toUpperCase();
  }
}

function passwordStrength(password,metodo) {
        var desc = new Array();
        desc[0] = "Inaceitável";
        desc[1] = "Muito Fraca";
        desc[2] = "Fraca";
        desc[3] = "Razoável";
        desc[4] = "Boa";
        desc[5] = "Forte";
        var score   = 0;
        
        //if password bigger than 6 give 1 point
        if (password.length >= 6) score++;

        //if password has both lower and uppercase characters give 1 point      
        if ( ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;

        //if password has at least one number give 1 point
        if ( ( password.match(/[a-z]/) ||  password.match(/[A-Z]/) ) && (password.match(/\d+/))) score++;

        //if password has at least one special caracther give 1 point
        if ( password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) ) score++;

        //if password bigger than 12 give another 1 point
        if (password.length >= 12) score++;
        
        //mininum requirements to be accepted by the AD
        if (score > 2 && (password.length < 6 || !password.match(/[a-z]/) || !password.match(/[A-Z]/) || !password.match(/\d+/)))
        	score = 2;

         document.getElementById('passwordDescription' + metodo).innerHTML = desc[score];
         document.getElementById('passwordStrength' + metodo).className = "strength" + score;
}

function validateUsuarioForm(form,metodo) {
	
	if ((metodo == 'Met1') && document.getElementById('dadosIntegracaoAD').style.display == 'none'){
		return;
	}
	var s = document.getElementById('passwordStrength' + metodo).className;
	if (s == "strength0" || s == "strength1" || s == "strength2") {
		alert("Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números");
		return false;
	}
	var p1 = document.getElementById("pass" + metodo).value;
	var p2 = document.getElementById("pass2" + metodo).value;
	if (p1 != p2) {
		alert("Repetição da nova senha não confere, favor redigitar.");
		return false; 
	}
	return true;
}


function refreshWindow(){
	var e=document.getElementById("refreshed");
	if(e.value=="no")e.value="yes";
	else{
		e.value="no";
		document.getElementById('txtMatricula').value = '';
		document.getElementById('${proxima_acao}_cpf').value = '';
		window.location.reload();
	}
}

</script>

<%-- Controle de refresh  --%>

<input type="hidden" id="refreshed" value="no">
<body onload="javascript:refreshWindow()" />


<siga:pagina titulo="${titulo}">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<c:if test="${not empty mensagem}">
					<div id="mensagem" class="gt-success">${mensagem}</div>
					<script>
						setTimeout(function() {
							$('#mensagem').fadeTo(1000, 0, function() {
								$('#mensagem').slideUp(1000);
								window.location.replace("/siga");
							});
						}, 5000); // <-- time in milliseconds
					</script>
			</c:if>
		
			<c:if test="${baseTeste}">
					<p id="msgSenha" class="gt-error">ATENÇÃO: Esta é uma versão de testes. Para sua segurança, NÃO utilize a mesma senha da versão de PRODUÇÃO.</p>
			</c:if>
	
			<h1 class="gt-form-head">${titulo}</h1>

			<h2 class="gt-form-head">Método 1 - Envio de senha nova para o
				e-mail</h2>
			<p id="msgExplicacao"></p>
			<div class="gt-form gt-content-box">

				<form action="${proxima_acao}" onsubmit="return validateUsuarioForm(this,'Met1');" method="post">
					<input type="hidden" name="usuario.metodo" value="1" />
					<input type="hidden" name="usuario.page" value="1" />
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-33">
							<label>Matrícula</label>
							<input type="text" name="usuario.matricula" id="txtMatricula"
								onblur="javascript:converteUsuario(this);javascript:checkIntegradoAD(this);javascript:checkEmailValido(this);" 
								class="gt-form-text" />
						</div>
						<div class="gt-right-col gt-width-66">
							<label>&nbsp;</label>
							<p>Ex.: XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES,
								etc.) e 99999 é o número da sua matrícula.</p>
						</div>
					</div>

					
					<div id="painel-dados-usuario">

						<div class="gt-form-row gt-width-33">
							<label>CPF</label>
							<input name="usuario.cpf" type="text" class="gt-form-text" />
						</div>
						
						<div class="gt-form-row gt-width-100" id="dadosIntegracaoAD" style="display: none;">
	
								<div class="gt-left-col gt-width-33">
									<label>Nova Senha</label>
									<input type="password" name="usuario.senhaNova" id="passMet1"
										onkeyup="passwordStrength(this.value,'Met1')" 
										class="gt-form-text" />
								</div>
	
	
								<div class="gt-left-col gt-width-33">
									<label>Repetição da nova senha</label>
									<input type="password" name="usuario.senhaConfirma" id="pass2Met1"
										onblur="javascript:converteUsuario(this)" 
										class="gt-form-text" />
								</div>
	
								<div class="gt-left-col gt-width-33">
									<label>Força da senha</label>
									<div id="passwordDescriptionMet1">Senha não informada</div>
									<div id="passwordStrengthMet1" class="strength0"></div>
								</div>
						</div>
						
						<div class="gt-form-row">
							<button type="submit" class="gt-btn-medium gt-btn-left">OK</button>
						</div>
					</div>
					<p id="msgEmail" class="oculto"></p>
				</form>
			</div>

			<h2 class="gt-form-head">Método 2 - Alterar a senha com auxílio
				de 2 pessoas</h2>
			<p>
				O sistema altera a senha da pessoa conforme solicitado, porém é
				necessário o apoio de mais 2 pessoas para completar o processo. <br />
				As pessoas auxiliares devem estar na mesma lotação da pessoa
				indicada para ter a senha substituída ou devem estar na lotação
				imediatamente superior na hierarquia organizacional.
			</p>
			<div class="gt-form gt-content-box">
				<form action="${proxima_acao}"
					onsubmit="return validateUsuarioForm(this,'Met2');" method="post">
					<input type="hidden" name="usuario.metodo" value="2" />

					<h4>Dados do primeiro auxiliar:</h4>
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-33">
							<label>Matrícula<a href="#"
								title="Ex.: XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é o número da matrícula da primeira pessoa que auxiliará na alteração de senha."><img
									style="position: relative; margin-top: -3px; top: +3px; left: +3px;"
									src="/siga/css/famfamfam/icons/information.png" /> </a> </label>
							<input type="text"name="usuario.auxiliar1" onblur="javascript:converteUsuario(this)" class="gt-form-text" />
						</div>
						<div class="gt-left-col gt-width-33">
							<label>CPF</label>
							<input type="text" name="usuario.cpf1" class="gt-form-text" />
						</div>

						<div class="gt-left-col gt-width-33">
							<label>Senha</label>
							<input type="password" name="usuario.senha1"
								onblur="javascript:converteUsuario(this)" 
								class="gt-form-text" />
						</div>
					</div>

					<h4>Dados do segundo auxiliar:</h4>
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-33">
							<label>Matrícula<a href="#"
								title="Ex.: XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é o número da matrícula da segunda pessoa que auxiliará na alteração de senha."><img
									style="position: relative; margin-top: -3px; top: +3px; left: +3px;"
									src="/siga/css/famfamfam/icons/information.png" /> </a> </label>
							<input type="text" name="usuario.auxiliar2"
								onblur="javascript:converteUsuario(this)" 
								class="gt-form-text" />
						</div>
						<div class="gt-left-col gt-width-33">
							<label>CPF</label>
							<input type="text" name="usuario.cpf2" class="gt-form-text" />
						</div>

						<div class="gt-left-col gt-width-33">
							<label>Senha</label>
							<input type="password" name="usuario.senha2"
								onblur="javascript:converteUsuario(this)" 
								class="gt-form-text" />
						</div>
					</div>

					<h4>Alterar senha de:</h4>
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-33">
							<label>Matrícula<a href="#"
								title="Ex.: XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é o número da matrícula do usuário que terá a senha alterada."><img
									style="position: relative; margin-top: -3px; top: +3px; left: +3px;"
									src="/siga/css/famfamfam/icons/information.png" /> </a> </label>
							<input type="text" name="usuario.matricula"
								onblur="javascript:converteUsuario(this)" 
								class="gt-form-text" />
						</div>
						<div class="gt-left-col gt-width-33">
							<label>CPF</label>
							<input type="text" name="usuario.cpf" class="gt-form-text" />
						</div>
					</div>
					<div class="gt-form-row gt-width-100">

						<div class="gt-left-col gt-width-33">
							<label>Nova Senha</label>
							<input type="password" name="usuario.senhaNova" id="passMet2"
								onkeyup="passwordStrength(this.value,'Met2')" 
								class="gt-form-text" />
						</div>

						<div class="gt-left-col gt-width-33">
							<label>Repetição da nova senha</label>
							<input type="password" name="senhaConfirma" id="pass2Met2"
								onblur="javascript:converteUsuario(this)"
								class="gt-form-text" />
						</div>

						<div class="gt-left-col gt-width-33">
							<label>Força da senha</label>
							<div id="passwordDescriptionMet2">Senha não informada</div>
							<div id="passwordStrengthMet2" class="strength0"></div>
						</div>
					</div>

					<div class="gt-form-row">
						<button type="submit" class="gt-btn-medium gt-btn-left" >OK</button>
					</div>
				</form>
			</div>
		</div>
</siga:pagina>
