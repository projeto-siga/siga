<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Cargo">

<script type="text/javascript">
	function validar() {
		var nmCargo = document.getElementsByName('nmCargo')[0].value;	
		var id = document.getElementsByName('id')[0].value;	
		if (nmCargo==null || nmCargo=="") {			
			alert("Preencha o nome do Cargo.");
			document.getElementById('nmCargo').focus();		
		}else {
			frm.submit();
		}			
	}
</script>

<body>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
		<form name="frm" action="${request.contextPath}/app/cargo/gravar" method="POST">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="id" value="${id}" />
			<h1>Cadastro de Cargo</h1>
			<div class="gt-content-box gt-for-table">
				<table class="gt-form-table" width="100%">
					<tr class="header">
						<td colspan="2">Dados do Cargo</td>
					</tr>
					<tr><td></td></tr>
					
					<tr><td></td></tr>
					<tr>
						<td><label>Órgão:</label></td>
						<td>
							<c:choose>
								<c:when test="${empty id || podeAlterarOrgao}">
									<select name="idOrgaoUsu" value="${idOrgaoUsu}">
										<c:forEach items="${orgaosUsu}" var="item">
											<option value="${item.idOrgaoUsu}"
												${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
												${item.nmOrgaoUsu}</option>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
									${nmOrgaousu}
									<input type="hidden" name="idOrgaoUsu" value="${idOrgaoUsu}" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>				
						<td>
							<label>Nome:</label>
						</td>
						<td>
							<input type="text" id="nmCargo" name="nmCargo" value="${nmCargo}" maxlength="100" size="100" />
						</td>
					</tr>
					<c:if test="${empty id}">
						<tr class="button">
							<td>Carregar planilha para inserir múltiplos registros:</td>
							<td><input type="button" value="Carregar planilha" onclick="javascript:location.href='/siga/app/cargo/carregarExcel';" class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</c:if>
					<tr class="button">
						<td>
							<input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-large gt-btn-left" /> 
							<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/cargo/listar';" class="gt-btn-medium gt-btn-left" />
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