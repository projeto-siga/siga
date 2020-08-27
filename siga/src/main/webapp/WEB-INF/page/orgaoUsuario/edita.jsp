<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Orgãos">

<script type="text/javascript">
	function validar() {
		var nmOrgaoUsuario = document.getElementsByName('nmOrgaoUsuario')[0].value;
		var siglaOrgaoUsuario = document.getElementsByName('siglaOrgaoUsuario')[0].value;	
		var id = document.getElementsByName('id')[0].value;	
		if (nmOrgaoUsuario==null || nmOrgaoUsuario=="") {			
			sigaModal.alerta("Preencha o nome do Órgão.");
			document.getElementById('nmOrgaoUsuario').focus();		
		}else {
			if (siglaOrgaoUsuario==null || siglaOrgaoUsuario=="") {			
				sigaModal.alerta("Preencha a sigla do Órgão.");
				document.getElementById('siglaOrgaoUsuario').focus();	
			}else {
				if(id==null || id=="") {
					sigaModal.alerta("Preencha ID do Órgão.");
					document.getElementById('id').focus();
				} else {
					frm.submit();
				}
			}
								
		}			
	}

	function somenteLetras(){
		tecla = event.keyCode;
		if ((tecla >= 65 && tecla <= 90) || (tecla >= 97 && tecla <= 122)){
		    return true;
		}else{
		   return false;
		}
	}
</script>

<body>

<div class="container-fluid">
	<div class="card bg-light mb-3" >		
		<form name="frm" action="${request.contextPath}/app/orgaoUsuario/gravar" method="POST">
			<input type="hidden" name="postback" value="1" /> 
			<div class="card-header"><h5>Cadastro de Órgão Usuário</h5></div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>ID</label>
								<c:choose>
								    <c:when test="${empty id}">
								        <input type="text" id="id" name="id" value="${id}" maxlength="5" size="5" onKeypress="return verificaNumero(event);" class="form-control"/>
								        <input type="hidden" name="acao" value="i"/>
								    </c:when>    
								    <c:otherwise>
								    	<label class="form-control">${id}</label>
								        <input type="hidden" name="id" value="${id}"/>
								        <input type="hidden" name="acao" value="a"/>
								    </c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nmOrgaoUsuario" name="nmOrgaoUsuario" value="${nmOrgaoUsuario}" maxlength="80" size="80" class="form-control"/>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>Sigla</label>
								<c:choose>
									<c:when test="${empty siglaOrgaoUsuario || podeAlterarSigla}">
										<input type="text" name="siglaOrgaoUsuario" id="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}" maxlength="10" size="10" style="text-transform:uppercase"  onKeypress="return somenteLetras(event);" onkeyup="this.value = this.value.trim()" class="form-control"/>	
									</c:when>
									<c:otherwise>
										<label class="form-control">${siglaOrgaoUsuario }</label>
										<input type="hidden" name="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}"/>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Data de Assinatura do Contrato</label>
								<input type="text" id="dtContrato" name="dtContrato" value="${dtContrato}" 
									 onblur="javascript:verifica_data(this,0);" class="form-control"/>
							</div>
						</div>
						<div class="col-md-2">
						  <label>Tipo de Órgão</label>
						  <div class="form-check">
						    <input type="checkbox" class="form-check-input" id="isExternoOrgaoUsu" name="isExternoOrgaoUsu" value="1" <c:if test="${isExternoOrgaoUsu == 1}">checked</c:if> />
						    <label class="form-check-label" for="isExternoOrgaoUsu">Órgão com Acesso Externo</label>
						  </div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="button" value="Ok" onclick="javascript: validar();" class="btn btn-primary" /> 
								<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<br />
		</form>		
	</div>
</div>

</body>

</siga:pagina>