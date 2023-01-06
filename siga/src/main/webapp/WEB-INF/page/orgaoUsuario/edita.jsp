<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Cadastro de Orgãos">

<script type="text/javascript">
	function validar() {
		var nmOrgaoUsuario = document.getElementsByName('nmOrgaoUsuario')[0].value;
		var siglaOrgaoUsuario = document.getElementsByName('siglaOrgaoUsuario')[0].value;	
		var id = document.getElementsByName('id')[0].value;
		
		if (nmOrgaoUsuario==null || nmOrgaoUsuario=="") {			
			sigaModal.alerta("Preencha o nome do Órgão.");
			document.getElementById('nmOrgaoUsuario').focus();		
			return;
		}
		
		if (siglaOrgaoUsuario==null || siglaOrgaoUsuario=="") {			
			sigaModal.alerta("Preencha a sigla do Órgão.");
			document.getElementById('siglaOrgaoUsuario').focus();
			return;
		}
		if(id!=null && id!="") {
			var marco = document.getElementById('marco').value;
			var dataAlteracao = document.getElementById('dataAlteracao').value;
			
			if(marco==null || marco=="") {
				sigaModal.alerta("Preencha o Marco Regulatório.");
				document.getElementById('marco').focus();
				return;
			}
			
			if(dataAlteracao==null || dataAlteracao=="") {
				sigaModal.alerta("Preencha a Data de Alteração.");
				document.getElementById('dataAlteracao').focus();
				return;
			}
			sigaModal.abrir('confirmacaoModal');
		} else {
			sbmt();
		}
			
			
		/*if(id==null || id=="") {
			sigaModal.alerta("Preencha ID do Órgão.");
			document.getElementById('id').focus();
		} else {*/
			//frm.submit();
		//}
	}

	function somenteLetras(){
		tecla = event.keyCode;
		if ((tecla >= 65 && tecla <= 90) || (tecla >= 97 && tecla <= 122)){
		    return true;
		}else{
		   return false;
		}
	}
	
	function sbmt() {
		frm.submit();
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
						<div class="col-md-3">
							<div class="form-group">
								<label>ID</label>
								<c:choose>
								    <c:when test="${empty id}">
								        <input type="text" id="id" name="id" value="${id}" maxlength="5" size="5" onKeypress="return verificaNumero(event);" class="form-control" readonly/>
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
						<div class="col-md-5">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nmOrgaoUsuario" name="nmOrgaoUsuario" value="${nmOrgaoUsuario}" maxlength="80" size="80" class="form-control"/>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>Sigla</label>
								<input type="text" name="siglaOrgaoUsuario" id="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}" maxlength="10" size="10" style="text-transform:uppercase"  onKeypress="return somenteLetras(event);" onkeyup="this.value = this.value.trim()" class="form-control"/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label>Data de Assinatura do Contrato</label>
								<c:choose>
								    <c:when test="${empty id}">
										<input type="text" id="dtContrato" name="dtContrato" value="${dtContrato}" 
											 onblur="javascript:verifica_data(this,0);" class="form-control"/>
									</c:when>
									 <c:otherwise>
									 	<input type="text" id="dtContrato" name="dtContrato" value="${dtContrato}" 
											 onblur="javascript:verifica_data(this,0);" class="form-control" readonly/>
									 </c:otherwise>
								</c:choose>
							</div>
						</div>
						<c:if test="${not empty id}">
							<div class="col-md-7">
								<div class="form-group">
									<label>Marco Regulatório</label>
									<input type="text" id="marco" name="marco" value="${marco}" maxlength="500" size="80" class="form-control"/>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Data de Alteração</label>
									<input type="text" id="dataAlteracao" name="dataAlteracao" value="${dataAlteracao}" class="form-control" onblur="javascript:verifica_data(this,0);"/>
								</div>
							</div>
						</c:if>
					</div>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								 <label>Tipo de Órgão</label>
								 <div class="form-check">
								    <input type="checkbox" class="form-check-input" id="isExternoOrgaoUsu" name="isExternoOrgaoUsu" value="1" <c:if test="${isExternoOrgaoUsu == 1}">checked</c:if> />
								    <label class="form-check-label" for="isExternoOrgaoUsu">Órgão com Acesso Externo</label>
								 </div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="button" value="Ok" class="btn btn-primary"onclick="javascript:validar();"/> 
								<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<h5>Histórico de Alteração / Inativação de Cadastro de órgão.</h5>
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color} align-middle text-center">
					<tr>
						<th style="width: 10%" class="text-center" rowspan="2">Data</th>
						<th style="width: 20%" class="text-left" rowspan="2">Evento</th>
						<th style="width: 20%" colspan="2" align="left">Responsável Pelo Evento</th>
						<th style="width: 35%" class="text-center" rowspan="2">Marco Regulatório</th>
						<th style="width: 5%" class="text-center" rowspan="2">Data de Alteração</th>
					</tr>
					<tr>
						<th class="text-left">
							<fmt:message key="usuario.lotacao"/>
						</th>
						<th class="text-left">
							<fmt:message key="usuario.pessoa"/>
						</th>
					</tr>
				</thead>
				<c:forEach var="hist" items="${listaHistorico}">
					<tr>
						<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${hist.hisDtIni}"/></td>
						<td class="text-left">Alteração de Cadastro do órgão</td>
						<td class="text-left">
							<siga:selecionado isVraptor="true" sigla="${hist.hisIdcIni.dpPessoa.lotacao.orgaoUsuario.siglaOrgaoUsu}${hist.hisIdcIni.dpPessoa.lotacao.sigla}"
											descricao="${hist.hisIdcIni.dpPessoa.lotacao.descricaoAmpliada}" 
											lotacaoParam="${hist.hisIdcIni.dpPessoa.lotacao.orgaoUsuario.siglaOrgaoUsu}${hist.hisIdcIni.dpPessoa.lotacao.sigla}" />
						</td>
						<td class="text-left">
							<siga:selecionado isVraptor="true" sigla="${hist.hisIdcIni.dpPessoa.nomePessoa}"
											descricao="${hist.hisIdcIni.dpPessoa.descricao} - ${hist.hisIdcIni.dpPessoa.sigla}" 
											pessoaParam="${hist.hisIdcIni.dpPessoa.sigla}"/>
						</td>
						<td class="text-center">${hist.marcoRegulatorio}</td>
						<td class="text-center"><fmt:formatDate pattern="dd/MM/yyyy" value="${hist.dataAlteracao}"/></td>
					</tr>
				</c:forEach>
			
			</table>
			<br />
		</form>
		<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirma&ccedil;&atilde;o">
			<div class="modal-body">
	       		Esse tipo de alteração poderá impactar na matrícula dos usuários e produção de documentos. Deseja realmente alterar o cadastro do órgão? 
	     	</div>
	     	<div class="modal-footer">
	       		<button type="button" class="btn btn-danger" data-dismiss="modal">Não</button>		        
	       		<a href="javascript:sbmt();" class="btn btn-success btn-confirmacao-inativacao-cadastro" role="button" aria-pressed="true">Sim</a>
			</div>
		</siga:siga-modal>	
	</div>
</div>

</body>

</siga:pagina>