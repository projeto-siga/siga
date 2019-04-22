<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Fun&ccedil;&atilde;o de Confian&ccedil;a">

<script type="text/javascript">
	function validar() {
		var nmFuncao = document.getElementsByName('nmFuncao')[0].value;		
		var id = document.getElementsByName('id')[0].value;	
		if (nmFuncao==null || nmFuncao=="") {			
			alert("Preencha o nome da Função.");
			document.getElementById('nmFuncao').focus();		
		}else {
			frm.submit();
		}			
	}
</script>

<body>

<div class="container-fluid">
	<div class="card bg-light mb-3" >	
		<form name="frm" action="${request.contextPath}/app/funcao/gravar" method="POST">
			<input type="hidden" name="postback" value="1" />
			<input type="hidden" name="id" value="${id}" />
			<div class="card-header"><h5>Cadastro de Fun&ccedil;&atilde;o de Confian&ccedil;a</h5></div>
			<div class="card-body">
				
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>&Oacute;rg&atilde;o</label>
								<c:choose>
									<c:when test="${empty id || podeAlterarOrgao}">
										<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control">
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
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nmFuncao" name="nmFuncao" value="${nmFuncao}" maxlength="100" size="100" class="form-control"/>
							</div>
						</div>
					</div>
					<c:if test="${empty id}">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									Carregar planilha para inserir múltiplos registros:
									<input type="button" value="Carregar planilha" onclick="javascript:location.href='/siga/app/funcao/carregarExcel';" class="btn btn-primary" />
								</div>
							</div>
						</div>
					</c:if>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="button" value="Ok" onclick="javascript: validar();" class="btn btn-primary" /> 
								<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/funcao/listar';" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</div>
		</form>
	</div>
</div>

</body>

</siga:pagina>