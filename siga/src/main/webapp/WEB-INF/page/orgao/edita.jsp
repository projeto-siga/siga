<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Orgãos Externos">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

<script type="text/javascript">
	function validar() {
		var nmOrgao = document.getElementsByName('nmOrgao')[0].value;
		var siglaOrgao = document.getElementsByName('siglaOrgao')[0].value;		
		if (nmOrgao==null || nmOrgao=="") {			
			sigaModal.alerta("Preencha o nome do Órgão.");
			document.getElementById('nmOrgao').focus();		
		}else {
			if (siglaOrgao==null || siglaOrgao=="") {			
				sigaModal.alerta("Preencha a sigla do Órgão.");
				document.getElementById('siglaOrgao').focus();	
			}else 
				frm.submit();				
		}			
	}
</script>

<div class="container-fluid">
	<div class="card bg-light mb-3" >		
		<form name="frm" action="${request.contextPath}/app/orgao/gravar" method="POST">
			<input type="hidden" name="postback" value="1" /> 
			<input type="hidden" name="id" value="${id}" /> 
			<div class="card-header"><h5>Cadastro de Órgão Externo</h5></div>
				<div class="card-body">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nmOrgao" name="nmOrgao" value="${nmOrgao}" maxlength="80" size="80" class="form-control"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label>Sigla</label>
								<input type="text" name="siglaOrgao" id="siglaOrgao" value="${siglaOrgao}" maxlength="30" size="30" class="form-control"/>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-sm-1">
							<div class="form-group">
								<label>Ativo</label>
								<select name="ativo" value="${ativo}" class="form-control">
									<option value="S"  ${ativo == 'S' ? 'selected' : ''}>Sim</option>  
									<option value="N" ${ativo == 'N' ? 'selected' : ''}>Não</option>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-5">
							<div class="form-group"><label>Órgão Solicitante</label>
								<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}" ${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}
										</option>  
									</c:forEach>
								</select>
							</div>	
						</div>
					</div>		
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<input type="button" value="Ok" onclick="javascript: validar();" class="btn btn-primary" /> 
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<br />
		</form>						
	</div>
</div>

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>