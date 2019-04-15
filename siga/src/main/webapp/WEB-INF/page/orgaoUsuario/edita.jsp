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
			alert("Preencha o nome do Órgão.");
			document.getElementById('nmOrgaoUsuario').focus();		
		}else {
			if (siglaOrgaoUsuario==null || siglaOrgaoUsuario=="") {			
				alert("Preencha a sigla do Órgão.");
				document.getElementById('siglaOrgaoUsuario').focus();	
			}else {
				if(id==null || id=="") {
					alert("Preencha ID do Órgão.");
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

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
		<form name="frm" action="${request.contextPath}/app/orgaoUsuario/gravar" method="POST">
			<input type="hidden" name="postback" value="1" /> 
			<h1>Cadastro de Órgão Usuário</h1>
			<div class="gt-content-box gt-for-table">
				<table class="gt-form-table" width="100%">
					<tr class="header">
						<td colspan="2">Dados do Orgão Usuário</td>
					</tr>
					<tr><td></td></tr>
					<tr>				
						<td>
							<label>ID:</label>
						</td>						
						<td>
							<c:choose>
							    <c:when test="${empty id}">
							        <input type="text" id="id" name="id" value="${id}" maxlength="5" size="5" onKeypress="return verificaNumero(event);"/>
							        <input type="hidden" name="acao" value="i"/>
							    </c:when>    
							    <c:otherwise>
							        <c:out value="${id}"/>
							        <input type="hidden" name="id" value="${id}"/>
							        <input type="hidden" name="acao" value="a"/>
							    </c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr><td></td></tr>
					<tr>				
						<td>
							<label>Nome:</label>
						</td>
						<td>
							<input type="text" id="nmOrgaoUsuario" name="nmOrgaoUsuario" value="${nmOrgaoUsuario}" maxlength="80" size="80" />
						</td>
					</tr>
					<tr><td></td></tr>
					<tr>
						<td>
							<label>Sigla:</label>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty siglaOrgaoUsuario || podeAlterarSigla}">
									<input type="text" name="siglaOrgaoUsuario" id="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}" maxlength="5" size="5" style="text-transform:uppercase"  onKeypress="return somenteLetras(event);" onkeyup="this.value = this.value.trim()"/>	
								</c:when>
								<c:otherwise>
									<c:out value="${siglaOrgaoUsuario }"/>
									<input type="hidden" name="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}"/>
								</c:otherwise>
							</c:choose>
							
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