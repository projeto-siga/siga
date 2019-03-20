<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Pessoa">

<script type="text/javascript">
	function validar() {
		var idOrgaoUsu = document.getElementsByName('idOrgaoUsu')[0].value;
		var nmPessoa = document.getElementsByName('nmPessoa')[0].value;	
		var idCargo = document.getElementsByName('idCargo')[0].value;
		var idLotacao = document.getElementsByName('idLotacao')[0].value;
		var idFuncao = document.getElementsByName('idFuncao')[0].value;
		var dtNascimento = document.getElementsByName('dtNascimento')[0].value;
		var cpf = document.getElementsByName('cpf')[0].value;
		var email = document.getElementsByName('email')[0].value;
		var id = document.getElementsByName('id')[0].value;	
		if (nmPessoa==null || nmPessoa=="") {			
			alert("Preencha o nome da pessoa.");
			document.getElementById('nmPessoa').focus();
			return;	
		}

		if(idOrgaoUsu==null || idOrgaoUsu == 0) {
			alert("Preencha o órgão da pessoa.");
			document.getElementById('idOrgaoUsu').focus();
			return;	
		}
		
		if(idCargo==null || idCargo == 0) {
			alert("Preencha o cargo da pessoa.");
			document.getElementById('idCargo').focus();
			return;	
		}

		if(idLotacao==null || idLotacao == 0) {
			alert("Preencha a lotação da pessoa.");
			document.getElementById('idLotacao').focus();
			return;	
		}

		if(idFuncao==null || idFuncao == 0) {
			alert("Preencha a função da pessoa.");
			document.getElementById('idFuncao').focus();
			return;	
		}

		if(dtNascimento==null || dtNascimento == "") {
			alert("Preencha a data de nascimento da pessoa.");
			document.getElementById('dtNascimento').focus();
			return;
		}

		if(cpf==null || cpf == "") {
			alert("Preencha o CPF da pessoa.");
			document.getElementById('cpf').focus();
			return;
		}

		if(email==null || email == "") {
			alert("Preencha o e-mail da pessoa.");
			document.getElementById('email').focus();
			return;
		}

		if(!validarEmail(document.getElementsByName('email')[0])) {
			return;
		}

		if(!data(dtNascimento)) {
			return;
		}

		if(!validarCPF(cpf)) {
			return;
		}
		frm.submit();
	}

	function mascaraData( v)
	{
    	v=v.replace(/\D/g,"");
    	v=v.replace(/(\d{2})(\d)/,"$1/$2");
    	v=v.replace(/(\d{2})(\d)/,"$1/$2");
    	if(v.length == 10) {
			data(v);
        }
    	
    	return v;
	}

	function validarEmail(campo) {
		if(campo.value != "") {
			var RegExp = /\b[\w]+@[\w]+\.[\w]+/;
	
			if (campo.value.search(RegExp) == -1) {
					alert("E-mail inválido!");
					return false;
			}
			return true;
		} else {
			return false;
		}
	}

	function data(campo) {
		var reg = /(([0-2]{1}[0-9]{1}|3[0-1]{1})\/(0[0-9]{1}|1[0-2]{1})\/[0-9]{4})/g; //valida dd/mm/aaaa
		if(campo.search(reg) == -1) {
			alert('Data inválida!');
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
</script>

<body>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
		<form name="frm" action="${request.contextPath}/app/pessoa/gravar" method="POST">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="id" value="${id}" />
			<h1>Cadastro de Pessoa</h1>
			<div class="gt-content-box gt-for-table">
				<table class="gt-form-table" width="100%">
					<tr class="header">
						<td colspan="2">Dados da Pessoa</td>
					</tr>
					<tr><td></td></tr>
					
					<tr><td></td></tr>
					<tr>
						<td><label>&Oacute;rg&atilde;o:</label></td>
						<td>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}"  onchange="carregarRelacionados(this.value)">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><label>Cargo:</label></td>
						<td>
							<select name="idCargo" value="${idCargo}">
								<c:forEach items="${listaCargo}" var="item">
									<option value="${item.idCargo}"
										${item.idCargo == idCargo ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><label>Fun&ccedil;&atilde;o de Confian&ccedil;a:</label></td>
						<td>
							<select name="idFuncao" value="${idFuncao}">
								<c:forEach items="${listaFuncao}" var="item">
									<option value="${item.idFuncao}"
										${item.idFuncao == idFuncao ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><label>Lota&ccedil;&atilde;o:</label></td>
						<td>
							<select name="idLotacao" value="${idLotacao}">
								<c:forEach items="${listaLotacao}" var="item">
									<option value="${item.idLotacao}"
										${item.idLotacao == idLotacao ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					
					<tr>				
						<td>
							<label>Nome:</label>
						</td>
						<td>
							<input type="text" id="nmPessoa" name="nmPessoa" value="${nmPessoa}" maxlength="60" size="60" />
						</td>
					</tr>
					<tr>				
						<td>
							<label>Data de Nascimento:</label>
						</td>
						<td>
							<input type="text" id="dtNascimento" name="dtNascimento" value="${dtNascimento}" maxlength="10" size="10" onkeyup="this.value = mascaraData( this.value )"/>
						</td>
					</tr>
					<tr>				
						<td>
							<label>CPF:</label>
						</td>
						<td>
							<input type="text" id="cpf" name="cpf" value="${cpf}" maxlength="14" size="14" onkeyup="this.value = cpf_mask(this.value)"/>
						</td>
					</tr>
					<tr>				
						<td>
							<label>E-mail:</label>
						</td>
						<td>
							<input type="text" id="email" name="email" value="${email}" maxlength="60" size="60" onchange="validarEmail(this)" onkeyup="this.value = this.value.toLowerCase().trim()"/>
						</td>
					</tr>
					
					<c:if test="${empty id}">
						<tr class="button">
							<td>Carregar planilha para inserir múltiplos registros:</td>
							<td><input type="button" value="Carregar planilha" onclick="javascript:location.href='/siga/app/pessoa/carregarExcel';" class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</c:if>
					<tr class="button">
						<td>
							<input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-large gt-btn-left" /> 
							<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/pessoa/listar';" class="gt-btn-medium gt-btn-left" />
						</td>
					</tr>
				</table>
			</div>
			<br />
		</form>
	</div>
</div>

</body>

</siga:pagina>
<script>
function voltar() {
	frm.action = 'listar';
	frm.submit();
}

function carregarRelacionados(id) {
	frm.action = 'carregarCombos';
	frm.submit();
	//jQuery.blockUI(objBlock);
    /*Siga.ajax('/siga/app/pessoa/carregarCombos?ajax=true&idOrgaoUsuario='+id, null, "GET", function(response){		
				carregouRelacionados(response);
	});*/
	
	/*var frm = document.getElementById('frm');
		ReplaceInnerHTMLFromAjaxResponse(
				'${pageContext.request.contextPath}/app/pessoa/carregarCombos?idOrgaoUsuario='
						+ id
						, frm, document
						.getElementById('divRelacionados'), function() {carregouRelacionados()});
		*/

}
</script>