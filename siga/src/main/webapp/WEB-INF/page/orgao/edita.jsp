<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Orgãos Externos">

<script type="text/javascript">
	function validar() {
		var nmOrgao = document.getElementsByName('nmOrgao')[0].value;
		var siglaOrgao = document.getElementsByName('siglaOrgao')[0].value;		
		if (nmOrgao==null || nmOrgao=="") {			
			alert("Preencha o nome do Órgão.");
			document.getElementById('nmOrgao').focus();		
		}else {
			if (siglaOrgao==null || siglaOrgao=="") {			
				alert("Preencha a sigla do Órgão.");
				document.getElementById('siglaOrgao').focus();	
			}else 
				frm.submit();				
		}			
	}
</script>

<body>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
		<form name="frm" action="${request.contextPath}/app/orgao/gravar" method="POST">
			<input type="hidden" name="postback" value="1" /> 
			<input type="hidden" name="id" value="${id}" /> 
			<h1>Cadastro de Órgão Externo</h1>
			<div class="gt-content-box gt-for-table">
				<table class="gt-form-table" width="100%">
					<tr class="header">
						<td colspan="2">Dados do Orgão Externo</td>
					</tr>
					<tr><td></td></tr>
					<tr>				
						<td>
							<label>Nome:</label>
						</td>
						<td>
							<input type="text" id="nmOrgao" name="nmOrgao" value="${nmOrgao}" maxlength="80" size="80" />
						</td>
					</tr>
					<tr><td></td></tr>
					<tr>
						<td>
							<label>Sigla:</label>
						</td>
						<td>
							<input type="text" name="siglaOrgao" id="siglaOrgao" value="${siglaOrgao}" maxlength="30" size="30" />
						</td>	
					</tr>
					<tr>
						<td>
							<label>Ativo:</label>
						</td>
						<td>
							<select name="ativo" value="${ativo}">
								<option value="S"  ${ativo == 'S' ? 'selected' : ''}>Sim</option>  
								<option value="N" ${ativo == 'N' ? 'selected' : ''}>Não</option>
							</select>
						</td>
					</tr>
					<tr>		
						<td>Órgão Solicitante:</td>
						<td>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}">
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}" ${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}
									</option>  
								</c:forEach>
							</select>	
						</td>
					</tr>			
					<tr class="button">
						<td>
							<input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-large gt-btn-left" /> 
							<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" />
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