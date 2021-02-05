<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
	function validar() {		
		sigaSpinner.mostrar();
		document.getElementById("btnOk").disabled = true;
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
			sigaModal.alerta("Preencha o nome da pessoa.");				
			habilitarBotaoOk();	 
			document.getElementById('nmPessoa').focus();
			return;	
		}

		if(idOrgaoUsu==null || idOrgaoUsu == 0) {
			sigaModal.alerta("Preencha o órgão da pessoa.");				
			habilitarBotaoOk();	 			
			document.getElementById('idOrgaoUsu').focus();
			return;	
		}
		
		if(idCargo==null || idCargo == 0) {
			sigaModal.alerta("Preencha o cargo da pessoa.");				
			habilitarBotaoOk();	 						
			document.getElementById('idCargo').focus();
			return;	
		}

		if(idLotacao==null || idLotacao == 0) {
			var msg="<fmt:message key='usuario.lotacao'/>";
			sigaModal.alerta("Preencha a "+msg.toLowerCase()+" da pessoa.");				
			habilitarBotaoOk();	 									
			document.getElementById('idLotacao').focus();
			return;	
		}

		if(cpf==null || cpf == "") {
			sigaModal.alerta("Preencha o CPF da pessoa.");				
			habilitarBotaoOk();	 														
			document.getElementById('cpf').focus();
			return;
		}

		if(email==null || email == "") {
			sigaModal.alerta("Preencha o e-mail da pessoa.");				
			habilitarBotaoOk();	 									
			document.getElementById('email').focus();
			return;
		}

		if(!validarEmail(document.getElementsByName('email')[0])) {			
			sigaSpinner.ocultar();
			document.getElementById("btnOk").disabled = false;
			return;
		}

		if(dtNascimento != null && dtNascimento != "" && !data(dtNascimento)) {
			sigaSpinner.ocultar();
			document.getElementById("btnOk").disabled = false;
			return;
		}

		if(!validarCPF(cpf)) {
			sigaSpinner.ocultar();
			document.getElementById("btnOk").disabled = false;
			return;
		}
			
		document.getElementById("email").disabled = false
		frm.submit();
	}
			
	function habilitarBotaoOk() {		
		sigaSpinner.ocultar();
		document.getElementById("btnOk").disabled = false;
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
			var RegExp = /\b[\w]+@[\w-]+\.[\w]+/;
	
			if (campo.value.search(RegExp) == -1) {					
					sigaModal.alerta("E-mail inválido!");
					habilitarBotaoOk();
					document.getElementById('email').focus();
					return false;
			} else if (campo.value.search(',') > 0) {
				sigaModal.alerta("E-mail inválido! Não pode conter vírgula ( , )");				
				habilitarBotaoOk();
				document.getElementById('email').focus();
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
			sigaModal.alerta('Data inválida!');				
			habilitarBotaoOk();			
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
        	
        	sigaModal.alerta('CPF Inválido!');				
			habilitarBotaoOk();	        	
            return false;
		}
        Soma = 0;
        for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
        Resto = (Soma * 10) % 11;

        if ((Resto == 10) || (Resto == 11))  Resto = 0;
        if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
        	
        	sigaModal.alerta('CPF Inválido!');				
			habilitarBotaoOk();	 
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

	function validarNome(campo) {
		campo.value = campo.value.replace(/[^a-zA-ZáâãäéêëíïóôõöúüçñÁÂÃÄÉÊËÍÏÓÔÕÖÚÜÇÑ'' ]/g,'');
	}
	
	function validarNomeCpf(){
		var cpf = document.getElementById('cpf').value;
		var nome = document.getElementById('nmPessoa').value;
		var id = document.getElementById('id').value;
		$.ajax({
			method:'GET',
			url: 'check_nome_por_cpf?cpf=' + cpf + '&nome='+nome + '&id='+id,
			success: function(data){retornoValidaNome(data)},
			error: function(data){retornoValidaNome(data)} 
		});
	}

	function retornoValidaNome(response,param){
		if(response == 0) {
			validar();
		} else {		
			var titulo = '<h4 class="siga-modal__titulo  siga-modal__titulo--conteudo">' +
							'Foram localizados os seguintes cadastros com o mesmo CPF e nome diferente, deseja atualizar o nome de todos os registros?' +
						 '</h4>'
			sigaModal.enviarHTMLEAbrir('pessoasModal', titulo.concat(response));			
		}
	}

	</script>

<siga:pagina titulo="Cadastro de Pessoa">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />	

	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da Pessoa</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="${request.contextPath}/app/pessoa/gravar" method="POST">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="id" id="id" value="${id}" />
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label for="idOrgaoUsu">&Oacute;rg&atilde;o</label>
								<select name="idOrgaoUsu" value="${idOrgaoUsu}"  onchange="carregarRelacionados(this.value)" class="form-control  siga-select2">
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="idCargo">Cargo</label>
								<select name="idCargo" value="${idCargo}" class="form-control  siga-select2">
									<c:forEach items="${listaCargo}" var="item">
										<option value="${item.idCargo}"
											${item.idCargo == idCargo ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="idFuncao">Fun&ccedil;&atilde;o de Confian&ccedil;a</label>
								<select id="idFuncao" name="idFuncao" value="${idFuncao}" class="form-control  siga-select2">
									<c:forEach items="${listaFuncao}" var="item">
										<option value="${item.idFuncao}"
											${item.idFuncao == idFuncao ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group" id="idLotacaoGroup">
								<label for="idLotacao"><fmt:message key="usuario.lotacao"/></label>
								<select id="idLotacao" style="width: 100%" name="idLotacao" value="${idLotacao}" class="form-control  siga-select2">
									<c:forEach items="${listaLotacao}" var="item">
										<option value="${item.idLotacao}" ${item.idLotacao == idLotacao ? 'selected' : ''}>
											<c:if test="${item.descricao ne 'Selecione'}">${item.siglaLotacao} / </c:if>${item.descricao}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					
					<hr>
					
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label for="nmPessoa">CPF</label>
								<input type="text" id="cpf" name="cpf" value="${cpf}" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" class="form-control" />
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label for="nmPessoa">Nome</label>
								<input type="text" id="nmPessoa" name="nmPessoa" value="${nmPessoa}" maxlength="60" class="form-control" onkeyup="validarNome(this)"/>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label for="nmPessoa">Nome Abreviado</label>
								<input type="text" id="nmPessoaAbreviado" name="nomeExibicao" value="${nomeExibicao}" maxlength="60" class="form-control" data-toggle="tooltip"  data-placement="top" title="O campo nome abreviado só deverá ser preenchido caso o usuário opte por usar seu nome de registro civil com abreviação na assinatura dos documentos. Vale considerar que ainda sim no rodapé do documento será exibido seu nome completo." onkeyup="validarNome(this)"/>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="nmPessoa">Data de Nascimento</label>
								<input type="text" id="dtNascimento" name="dtNascimento" value="${dtNascimento}" maxlength="10" onkeyup="this.value = mascaraData( this.value )" class="form-control" />
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label for="nmPessoa">E-mail</label>
								<input type="text" id="email" name="email" value="${email}" maxlength="60" onchange="validarEmail(this)" onkeyup="this.value = this.value.toLowerCase().trim()" class="form-control" />
							</div>
						</div>
					</div>
					<hr>
					<!-- Alteracao cartao 1057 -->
					<fieldset class="form-group">					
						<div class="row bg-">
							<div class="col-md-4">
								<div class="form-group">
									<label for="nmPessoa">RG (Incluindo dígito)</label>
									<input type="text" id="identidade" name="identidade" value="${identidade}" maxlength="20" class="form-control"/>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label for="nmPessoa">Órgão Expedidor</label>
									<input type="text" id="orgaoIdentidade" name="orgaoIdentidade" value="${orgaoIdentidade}" maxlength="50" class="form-control" />
								</div>
							</div>
							
							<div class="col-md-2">					
								<div class="form-group">
									<label for="ufIdentidade">UF</label>
									<select name="ufIdentidade" id="ufIdentidade" value="${ufIdentidade}" class="form-control  siga-select2">
										<option value="" selected disabled hidden>Selecione uma UF</option>
										
										<c:forEach items="${ufList}" var="item">
											<option value="${item.sigla}" ${item.sigla== ufIdentidade ? 'selected' : ''}>
												${item.descricao}
											</option>
										</c:forEach>							
									</select>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label for="nmPessoa">Data de Expedição</label>
									<input type="text" id="dataExpedicaoIdentidade" name="dataExpedicaoIdentidade" value="${dataExpedicaoIdentidade}" maxlength="10" onkeyup="this.value = mascaraData( this.value )" class="form-control" />
								</div>
							</div>
						</div>
					</fieldset>
					<!-- Fim da alteracao cartao 1057 -->
					
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<button type="button" id="btnOk" onclick="javascript: validarNomeCpf();" class="btn btn-primary" >Ok</button> 
								<button type="button" onclick="javascript:history.back();" class="btn btn-primary" >Cancelar</button>
								
							</div>
						</div>
						<c:if test="${empty id}">
							<div class="col-sm-5">
								<input type="checkbox" class="form-check-input" id="enviarEmail" name="enviarEmail" value="1" checked /> Enviar automaticamente o e-mail com os dados de acesso para o Usuário.
							</div>
						</c:if>
					</div>
	
					<c:if test="${empty id}">
						<div class="card mt-2 mb-2">
							<div class="card-body">
								<h5 class="card-title">Importação por Planilha</h5>
								<div class="row">
									<div class="col-md-4 col-sm-6">
										<div class="form-group">
											<span>Carregar planilha para inserir múltiplos registros</span>
										</div>
	
										<div class="form-group">
											<button type="button" onclick="javascript:location.href='/siga/app/pessoa/carregarExcel';" class="btn btn-success btn-lg">
												<i class="far fa-file-excel"></i> Carregar planilha
											</button>
										</div>																											
									</div>
								</div>
							</div>
						</div>
					</c:if>
				</form>	
			</div>
		</div>					
		<siga:siga-modal id="pessoasModal" tamanhoGrande="true" exibirRodape="true" descricaoBotaoFechaModalDoRodape="Cancelar"			
			linkBotaoDeAcao="javascript:sigaModal.fechar('pessoasModal');validar();" descricaoBotaoDeAcao="Confirmar">
			<div class="modal-body"></div>			
		</siga:siga-modal>				
	</div>
</siga:pagina>
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
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


$(document).ready(function() {									
	 $(function () {
			$('[data-toggle="tooltip"]').tooltip()
	 });
	
});
</script>