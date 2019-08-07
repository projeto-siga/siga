<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">
	function validar() {
		var nmLotacao = document.getElementsByName('nmLotacao')[0].value;
		var siglaLotacao = document.getElementsByName('siglaLotacao')[0].value;		
		var id = document.getElementsByName('id')[0].value;
		var idLocalidade = document.getElementsByName('idLocalidade')[0].value;	
		if (nmLotacao==null || nmLotacao=="") {			
			alert("Preencha o nome da Lotação.");
			document.getElementById('nmLotacao').focus();		
		}else {
			if(siglaLotacao==null || siglaLotacao=="") {
				alert("Preencha a sigla da Lotação.");
			} else {
				if(idLocalidade == 0) {
					alert("Preencha a localidade da Lotação.");	
				} else {
					frm.submit();
				}
			}
		}			
	}
	
	function carregarExcel() {
		document.form.action =  "carregarExcel";
		document.form.submit();
	}

	function validarNome(campo) {
		campo.value = campo.value.replace(/[^a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9./,-]/g,'');
	}

	function validarSigla(campo) {
		campo.value = campo.value.replace(/[^a-zA-ZçÇ0-9/,-]/g,'');
	}
</script>
<siga:pagina titulo="Cadastro de Lota&ccedil;&atilde;o">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">	
			<form name="frm" action="${request.contextPath}/app/lotacao/gravar" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" />
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label for="idOrgaoUsu">&Oacute;rg&atilde;o</label>
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
									<input type="hidden" name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label for="nmLotacao">Nome</label>
							<input type="text" id="nmLotacao" name="nmLotacao" value="${nmLotacao}" maxlength="100" class="form-control" onkeyup="validarNome(this)"/>
							<small id="emailHelp" class="form-text text-muted">(Inserir nome oficial, conforme legislação. Não abreviar. Iniciar cada palavra com letra maiúscula, exceto para palavras tais como: "de", "para", etc. Exemplo: Unidade do Arquivo Público do Estado).</small>
							
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label for="siglaLotacao">Sigla</label>
							<input type="text" id="siglaLotacao" name="siglaLotacao" value="${siglaLotacao}" maxlength="20" style="text-transform:uppercase" onkeyup="validarSigla(this)" class="form-control"/>
							<small id="emailHelp" class="form-text text-muted">(Sigla: Letras maiúsculas).</small>
							
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label for="idLocalidade">Localidade</label>
							<select name="idLocalidade" value="${idLocalidade}" class="form-control">
								<option value="0">Selecione</option>
								<c:forEach items="${listaLocalidades}" var="item">
									<option value="${item.id}"
										${item.id == idLocalidade ? 'selected' : ''}>
										${item.descricao}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-3">
						<div class="form-group">
							<label for="siglaLotacao">Situa&ccedil;&atilde;o</label><br/>
							<div class="form-check-inline">
								<label class="form-check-label">
									<input type="radio" name="situacao" value="true" id="situacaoAtivo" ${empty dtFimLotacao ? 'checked' : ''} />Ativo
								</label>
							</div>							
							<div class="form-check-inline">
								<label class="form-check-label">
									<input type="radio" name="situacao" value="false" id="situacaoInativo" ${not empty dtFimLotacao ? 'checked' : ''} />Inativo
								</label>
							</div>							
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
								<input type="button" value="Carregar planilha" onclick="javascript:location.href='/siga/app/lotacao/carregarExcel';" class="btn btn-primary" />
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
			</div>
		</div>
	</div>
</siga:pagina>