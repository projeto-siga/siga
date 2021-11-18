<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

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
	
	#table-wrapper {
	  position:relative;
	}
	
	#table-scroll {
	  height:250px;
	  overflow:auto;  
	  margin-top:30px;
	  border: 1px solid #6bb8d3;
      padding: 10px;
	}
	
	#table-wrapper table {
	  width:100%;
	  font-size:12px;
	}
	
    #table-wrapper tr:nth-child(even) {
        background: #ccc;
    }
    
    #table-wrapper tr:nth-child(odd) {
        background: #fff;
    }
    
	#table-wrapper table thead th .text {
	  position:absolute;   
	  top:-20px;
	  z-index:2;
	  height:20px;
	  width:35%;
	  border:1px solid red;
	}
	
	#table-wrapper table tr td:nth-child(3) {
		text-align: right;
		padding-right: 12px;
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
		$("#msgErro").removeAttr("style").hide();
	}else{
		document.getElementById('painel-dados-usuario').style.display = 'none';
		$("#msgErro").show();
		$('#msgEmail').text(response);

	}

}

/*  converte para maiúscula a sigla do estado  */
function converteUsuario(nomeusuario){
  tmp = nomeusuario.value;
  nomeusuario.value=tmp.toUpperCase();
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
		sigaModal.alerta("Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números");
		return false;
	}
	var p1 = document.getElementById("pass" + metodo).value;
	var p2 = document.getElementById("pass2" + metodo).value;
	if (p1 != p2) {
		sigaModal.alerta("Repetição da nova senha não confere, favor redigitar");
		return false; 
	}
	return true;
}

function validarCPF(Objcpf){
	var strCPF = Objcpf.replace(".","").replace(".","").replace("-","").replace("/","");
    var Soma;
    var Resto;
    Soma = 0;
	
    for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
    Resto = (Soma * 10) % 11;
	
    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(9, 10)) ) {
    	
    	alert('CPF Inválido!');
        return false;
	}
    Soma = 0;
    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
    	
    	alert('CPF Inválido!');
    	return false;
    }
    return true;
         
}
function cpf_mask(v){
	v=v.replace(/\D/g,"");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");

	if(v.length == 14) {
    	validarCPF(v);
    }
	return v;
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
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="row">
			<div class="col">
				<div id="msgErro" style="display:none">
					<div id="msgEmail" class="alert alert-danger" >
					</div>
				</div>
				<script>

				</script>
			</div>
		</div>
	
		<c:if test="${not empty mensagem}">
			<div id="mensagem" class="alert  alert-success">${mensagem} </div>
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
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>${titulo}</h5></div>
			<div class="card-body">
				<c:if test="${f:resource('/siga.omitir.metodo2') != 'true'}">
					<h6 class="gt-form-head"><fmt:message key = "usuario.metodo1"/></h6>
				</c:if>

				<div class="card bg-white" >
					<div class="mx-2 mb-2" >
						<form action="${proxima_acao}" onsubmit="return validateUsuarioForm(this,'Met1');" method="post">
							<input type="hidden" name="usuario.metodo" value="1" />
							<input type="hidden" name="usuario.page" value="1" />
							<div class="row">
								<div class="col-sm-3">
									<div class="form-group">
										<label for="usuario.matricula">Login</label>
										<input type="text" name="usuario.matricula" id="txtMatricula"
											onblur="javascript:converteUsuario(this);javascript:checkIntegradoAD(this);javascript:checkEmailValido(this);" 
											class="form-control" />
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-group">
										<label>&nbsp;</label>
										<p><fmt:message key = "usuario.helpnovousuario"/></p>
									</div>
								</div>
								
								<div class="col-sm-5">
									<div class="form-group">
										<div id="table-wrapper">
											<div id="table-scroll">
												<table>
												<tbody>
												<tr>
												<td><strong>NOME&nbsp; </strong></td>
												<td width="53"><strong>SIGLA</strong></td>
												<td width="57"><strong>&nbsp;&nbsp; FM</strong></td>
												</tr>
												<tr>
												<td>Agência de Fomento do Município do Rio de Janeiro S.A. / FOMENTA RIO</td>
												<td width="53">FOM</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Centro de Feiras, Exposições e Congressos do Rio de Janeiro / RIO CENTRO S.A.</td>
												<td width="53">CEN</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Companhia Carioca de Securitização / RIO SECURITIZAÇÃO</td>
												<td width="53">RSC</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Companhia de Desenvolvimento Urbano da Região do Porto do Rio de Janeiro / CDURP</td>
												<td width="53">POR</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Companhia de Engenharia de Tráfego do Rio de Janeiro / CETRIO</td>
												<td width="53">CET</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Companhia Municipal de Energia e Iluminação / RIOLUZ</td>
												<td width="53">LUZ</td>
												<td width="57">2</td>
												</tr>
												<tr>
												<td>Companhia Municipal de Limpeza Urbana / COMLURB</td>
												<td width="53">CLB</td>
												<td width="57">4</td>
												</tr>
												<tr>
												<td>Controladoria Geral do Município do Rio de Janeiro / CGM</td>
												<td width="53">CGM</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Distribuidora de Filmes S.A. / RIOFILME</td>
												<td width="53">FIL</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Empresa de Turismo do Município do Rio de Janeiro / RIOTUR</td>
												<td width="53">TUR</td>
												<td width="57">2</td>
												</tr>
												<tr>
												<td>Empresa Municipal de Artes Gráficas S.A. / IMPRENSA DA CIDADE</td>
												<td width="53">IC</td>
												<td width="57">4</td>
												</tr>
												<tr>
												<td>Empresa Municipal de Informática S.A. / IPLANRIO</td>
												<td width="53">IPL</td>
												<td width="57">2</td>
												</tr>
												<tr>
												<td>Empresa Municipal de Multimeios Ltda. / MULTIRIO</td>
												<td width="53">MUL</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Empresa Municipal de Urbanização / RIO-URBE</td>
												<td width="53">URB</td>
												<td width="57">3</td>
												</tr>
												<tr>
												<td>Empresa Pública de Saúde do Rio de Janeiro S/A / RIOSAUDE</td>
												<td width="53">RSU</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Fundação Cidade das Artes / CIDADE DAS ARTES</td>
												<td width="53">ART</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Fundação Instituto das Águas do Município do Rio de Janeiro / RIOAGUAS</td>
												<td width="53">AGU</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Fundação Instituto de Geotécnica do Município do Rio de Janeiro / GEORIO</td>
												<td width="53">GEO</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Fundação Jardim Zoológico da Cidade do Rio de Janeiro / RIOZOO</td>
												<td width="53">ZOO</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Fundação Parques e Jardins / FPJ</td>
												<td width="53">FPJ</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Fundação Planetário da Cidade do Rio de Janeiro / PLANETÁRIO</td>
												<td width="53">PLA</td>
												<td width="57">3</td>
												</tr>
												<tr>
												<td>Gabinete do Prefeito / GBP</td>
												<td width="53">GAB</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Guarda Municipal do Rio de Janeiro / GMRIO</td>
												<td width="53">GM</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Instituto de Previdência e Assistência do Município do Rio de Janeiro / PREVIRIO</td>
												<td width="53">PVR</td>
												<td width="57">1</td>
												</tr>
												<tr>
												<td>Instituto Municipal de Urbanismo Pereira Passos / IPP</td>
												<td width="53">IPP</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Prefeitura da Cidade do Rio de Janeiro / PCRJ</td>
												<td width="53">RIO</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Procuradoria Geral do Município do Rio de Janeiro / PGM</td>
												<td width="53">PGM</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Especial da Juventude Carioca / JUV-RIO</td>
												<td width="53">JUV</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Especial de Ação Comunitária / SEAC-RIO</td>
												<td width="53">COM</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Especial de Cidadania / SECID</td>
												<td width="53">CID</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Especial de Políticas e Promoção da Mulher / SPM-RIO</td>
												<td width="53">LHE</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Mun. de Desenvolvimento Econômico, Inovação e Simplificação / SMDEIS</td>
												<td width="53">EIS</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal da Pessoa com Deficiência / SMPD</td>
												<td width="53">DEF</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Assistência Social / SMAS</td>
												<td width="53">ASS</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Ciência e Tecnologia / SMCT</td>
												<td width="53">TEC</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Conservação / SECONSERVA</td>
												<td width="53">CSV</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Cultura / SMC</td>
												<td width="53">SMC</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Educação / SME</td>
												<td width="53">SME</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Esportes / SMEL</td>
												<td width="53">ESL</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Fazenda e Planejamento / SMFP</td>
												<td width="53">SMF</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Governo e Integridade Pública / SEGOVI</td>
												<td width="53">GOV</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Habitação / SMH</td>
												<td width="53">HBT</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Infraestrutura / SMI</td>
												<td width="53">IFR</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Meio Ambiente da Cidade / SMAC</td>
												<td width="53">MAB</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Ordem Pública / SEOP</td>
												<td width="53">EOP</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Planejamento Urbano / SMPU</td>
												<td width="53">SMU</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Proteção e Defesa dos Animais / SMPDA</td>
												<td width="53">ANI</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Saúde / SMS</td>
												<td width="53">SMS</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Trabalho e Renda / SMTE</td>
												<td width="53">TRA</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal de Transportes / SMTR</td>
												<td width="53">MTR</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Secretaria Municipal do Envelhecimento Saudável, Qualidade de Vida / SEMESQV</td>
												<td width="53">QVE</td>
												<td width="57"></td>
												</tr>
												<tr>
												<td>Vice-Prefeitura</td>
												<td width="53">VP</td>
												<td width="57">&nbsp;</td>
												</tr>
												</tbody>
												</table>	
											</div>
										</div>
									</div>
								</div>
								
							</div>
							<div id="painel-dados-usuario">
								<div class="row">
									<div class="col-sm-3">
										<div class="form-group">
											<label for="usuario.cpf">CPF</label>
											<input name="usuario.cpf" id="idCpf" type="text" class="form-control" 
												maxlength="14" onkeyup="this.value = cpf_mask(this.value)" value="${valCpf}"/>
										</div>
									</div>
								</div>
								
								<div id="dadosIntegracaoAD" style="display: none;">
									<div class="row">
										<div class="col-sm-4">
											<label for="usuario.senhaNova">Nova Senha</label>
											<input type="password" name="usuario.senhaNova" id="passMet1"
												onkeyup="passwordStrength(this.value,'Met1')" 
												class="form-control" />
										</div>
										<div class="col-sm-4">
											<label for="usuario.senhaConfirma">Repetição da nova senha</label>
											<input type="password" name="usuario.senhaConfirma" id="pass2Met1"												
												class="form-control"/>
										</div>
										<div class="col-sm-4">
											<label>Força da senha</label>
											<div id="passwordDescriptionMet1">Senha não informada</div>
											<div id="passwordStrengthMet1" class="strength0"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-1">
										<button type="submit" class="btn btn-primary">OK</button>
									</div>
								</div>					
							</div>					
							<p  class="oculto"></p>
						</form>
					</div>
				</div>
				<c:if test="${f:resource('/siga.omitir.metodo2') != 'true'}">
					<br/>
					<h6 class="gt-form-head">Método 2 - Alterar a senha com auxílio de 2 pessoas</h6>			
					<p>
						O sistema altera a senha da pessoa conforme solicitado, porém é
						necessário o apoio de mais 2 pessoas para completar o processo. <br />
						As pessoas auxiliares devem estar na mesma lotação da pessoa
						indicada para ter a senha substituída ou devem estar na lotação
						imediatamente superior na hierarquia organizacional.
					</p>
					<div class="card bg-white" >
						<div class="mx-2 mb-2" >
							<form action="${proxima_acao}" onsubmit="return validateUsuarioForm(this,'Met2');" method="post">
								<input type="hidden" name="usuario.metodo" value="2" />
								<div class="row">
									<div class="col-sm">
										<div class="form-group">
											<h6>Dados do primeiro auxiliar:</h6>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.auxiliar1"><fmt:message key = "usuario.matricula"/></label>
											<input type="text"name="usuario.auxiliar1" onblur="javascript:converteUsuario(this)" class="form-control" />
											<small id="emailHelp" class="form-text text-muted"><fmt:message key = "usuario.helpauxiliar1"/></small>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.cpf1">CPF</label>
											<input type="text" name="usuario.cpf1" id="idCpf1" class="form-control" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" />
										</div>
									</div>				
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.senha1">Senha</label>
											<input type="password" name="usuario.senha1"  
												class="form-control" />
										</div>
									</div>
								</div>
					
								<div class="row">
									<div class="col-sm">
										<div class="form-group">
											<h6>Dados do segundo auxiliar:</h6>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.auxiliar2"><fmt:message key = "usuario.matricula"/></label>
											<input type="text"name="usuario.auxiliar2" onblur="javascript:converteUsuario(this)" class="form-control" />
											<small id="emailHelp" class="form-text text-muted"><fmt:message key = "usuario.helpauxiliar2"/></small>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.cpf2">CPF</label>
											<input type="text" name="usuario.cpf2" id="idCpf2" class="form-control" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" />
										</div>
									</div>				
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.senha2">Senha</label>
											<input type="password" name="usuario.senha2"  
												class="form-control" />
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-sm">
										<div class="form-group">
											<h6>Alterar senha de:</h6>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.matricula"><fmt:message key = "usuario.matricula"/></label>
											<input type="text"name="usuario.matricula" onblur="javascript:converteUsuario(this)" class="form-control" />
											<small id="emailHelp" class="form-text text-muted"><fmt:message key = "usuario.helpalterarsenhade"/></small>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.cpf2">CPF</label>
											<input type="text" name="usuario.cpf2" id="idCpf3" class="form-control" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" />
										</div>
									</div>				
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label for="usuario.senha2">Nova Senha</label>
											<input type="password" name="usuario.senhaNova" id="passMet2" onkeyup="passwordStrength(this.value,'Met2')" class="form-control" />
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label for="senhaConfirma">Repetição da nova senha</label>
											<input type="password" name="senhaConfirma" id="pass2Met2"  class="form-control" />
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label for="senhaConfirma">Força da senha</label>
											<div id="passwordDescriptionMet2">Senha não informada</div>
											<div id="passwordStrengthMet2" class="strength0"></div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-1">
										<button type="submit" class="btn btn-primary">OK</button>
									</div>
								</div>					
							</form>
						</div>				
					</div>
				</c:if>				
			</div>
		</div>
	</div>





</siga:pagina>
