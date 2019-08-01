<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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
			mensagemAlerta("Preencha o nome da pessoa.");
			document.getElementById('nmPessoa').focus();
			return;	
		}

		if(idOrgaoUsu==null || idOrgaoUsu == 0) {
			mensagemAlerta("Preencha o órgão da pessoa.");
			document.getElementById('idOrgaoUsu').focus();
			return;	
		}
		
		if(idCargo==null || idCargo == 0) {
			mensagemAlerta("Preencha o cargo da pessoa.");
			document.getElementById('idCargo').focus();
			return;	
		}

		if(idLotacao==null || idLotacao == 0) {
			mensagemAlerta("Preencha a lotação da pessoa.");
			document.getElementById('idLotacao').focus();
			return;	
		}

		if(cpf==null || cpf == "") {
			mensagemAlerta("Preencha o CPF da pessoa.");
			document.getElementById('cpf').focus();
			return;
		}

		if(email==null || email == "") {
			mensagemAlerta("Preencha o e-mail da pessoa.");
			document.getElementById('email').focus();
			return;
		}

		if(!validarEmail(document.getElementsByName('email')[0])) {
			return;
		}

		if(dtNascimento != null && dtNascimento != "" && !data(dtNascimento)) {
			return;
		}

		if(!validarCPF(cpf)) {
			return;
		}
		frm.submit();
	}

	function mensagemAlerta(mensagem) {
		$('#alertaModal').find('.mensagem-Modal').text(mensagem);
		$('#alertaModal').modal();
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
			mensagemAlerta('Data inválida!');
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
        	
        	mensagemAlerta('CPF Inválido!');
            return false;
		}
        Soma = 0;
        for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
        Resto = (Soma * 10) % 11;

        if ((Resto == 10) || (Resto == 11))  Resto = 0;
        if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
        	
        	mensagemAlerta('CPF Inválido!');
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

	function validarNome(campo) {
		campo.value = campo.value.replace(/[^a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ'' ]/g,'');
	}
</script>

<siga:pagina titulo="Cadastro de Pessoa">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da Pessoa</h5>
			</div>
			<div class="card-body">
			<form name="frm" action="${request.contextPath}/app/pessoa/gravar" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" />
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label for="idOrgaoUsu">&Oacute;rg&atilde;o</label>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}"  onchange="carregarRelacionados(this.value)" class="form-control">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label for="idCargo">Cargo</label>
							<select name="idCargo" value="${idCargo}" class="form-control">
								<c:forEach items="${listaCargo}" var="item">
									<option value="${item.idCargo}"
										${item.idCargo == idCargo ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label for="idFuncao">Fun&ccedil;&atilde;o de Confian&ccedil;a</label>
							<select name="idFuncao" value="${idFuncao}" class="form-control">
								<c:forEach items="${listaFuncao}" var="item">
									<option value="${item.idFuncao}"
										${item.idFuncao == idFuncao ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label for="idLotacao">Lota&ccedil;&atilde;o</label>
							<select name="idLotacao" value="${idLotacao}" class="form-control">
								<c:forEach items="${listaLotacao}" var="item">
									<option value="${item.idLotacao}" ${item.idLotacao == idLotacao ? 'selected' : ''}>
										<c:if test="${item.descricao ne 'Selecione'}">${item.siglaLotacao} / </c:if>${item.descricao}
									</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label for="nmPessoa">Nome</label>
							<input type="text" id="nmPessoa" name="nmPessoa" value="${nmPessoa}" maxlength="60" class="form-control" onkeyup="validarNome(this)"/>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label for="nmPessoa">Data de Nascimento</label>
							<input type="text" id="dtNascimento" name="dtNascimento" value="${dtNascimento}" maxlength="10" onkeyup="this.value = mascaraData( this.value )" class="form-control" />
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label for="nmPessoa">CPF</label>
							<input type="text" id="cpf" name="cpf" value="${cpf}" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" class="form-control" />
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label for="nmPessoa">E-mail</label>
							<input type="text" id="email" name="email" value="${email}" maxlength="60" onchange="validarEmail(this)" onkeyup="this.value = this.value.toLowerCase().trim()" class="form-control" />
						</div>
					</div>
				</div>
				<c:if test="${empty id}">
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<span>Carregar planilha para inserir múltiplos registros:</span>
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<input type="button" value="Carregar planilha" onclick="javascript:location.href='/siga/app/pessoa/carregarExcel';" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</c:if>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="button" onclick="javascript: validar();" class="btn btn-primary" >Ok</button> 
							<button type="button" onclick="javascript:history.back();" class="btn btn-primary" >Cancelar</button>
						</div>
					</div>
				</div>
			</form>
			<!-- Modal -->
			<div class="modal fade" id="alertaModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
			    	<div class="modal-content">
			      		<div class="modal-header">
					        <h5 class="modal-title" id="alertaModalLabel">Alerta</h5>
					        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
					          <span aria-hidden="true">&times;</span>
					    	</button>
					    </div>
				      	<div class="modal-body">
				        	<p class="mensagem-Modal"></p>
				      	</div>
						<div class="modal-footer">
						  <button type="button" class="btn btn-primary" data-dismiss="modal">Fechar</button>
						</div>
			    	</div>
			  	</div>
			</div>				
			<!--Fim Modal -->
			</div>
		</div>
	</div>
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