<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
<script src="/sigasr/javascripts/detalhe-tabela.js"></script>

<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<div>
	<form id="formLista" method="post" enctype="multipart/form-data">

		<input type="hidden" id="idLista" name="idLista" value="${lista.idLista}"> 
		<input type="hidden" id="id" name="id" value="${lista.idLista}"> 
		<input type="hidden" id="hisIdIni" name="hisIdIni" value="${lista.hisIdIni}">
		
		<div class="gt-form-row gt-width-66">
			<label>Nome <span>*<span></label> <input type="text"
				id="nomeLista" name="nomeLista" value="${nomeLista}" size="98"
				maxlength="255" required />
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Abrangência</label>
			<textarea cols="98" rows="5" name="descrAbrangencia"
				id="descrAbrangencia" maxlength="8192">${descrAbrangencia}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Justificativa</label>
			<textarea cols="98" rows="5" name="descrJustificativa"
				id="descrJustificativa" maxlength="8192">${descrJustificativa}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Priorização</label>
			<textarea cols="98" rows="5" name="descrPriorizacao"
				id="descrPriorizacao" maxlength="8192">${descrPriorizacao}</textarea>
		</div>

		<div class="container">
			<div class="title-table">
				<h3 style="padding-top: 25px;">Permissões</h3>
			</div>
		</div>
		
		<div class="gt-content-box gt-for-table dataTables_div">
			<div class="gt-form-row dataTables_length">
				<label> <siga:checkbox name="mostrarAssocDesativada"
						value="${mostrarAssocDesativada}"></siga:checkbox> <b>Incluir
						Inativas</b>
				</label>
			</div>
			<table id="permissoes_table" border="0" class="gt-table display">
				<thead>
					<tr>
						<th>ID Org&atilde;o</th>
						<th>Org&atilde;o</th>
						<th>ID Local</th>
						<th>Local</th>
						<th>ID Lota&ccedil;&atilde;o</th>
						<th>Nome Lota&ccedil;&atilde;o</th>
						<th>Lota&ccedil;&atilde;o</th>
						<th>ID Pessoa</th>
						<th>Nome Pessoa</th>
						<th>Pessoa</th>
						<th>ID Cargo</th>
						<th>Cargo</th>
						<th>Cargo</th>
						<th>ID Fun&ccedil;&atilde;o</th>
						<th>Fun&ccedil;&atilde;o</th>
						<th>Fun&ccedil;&atilde;o</th>
						<th>Tipo Permiss&atilde;o JSON</th>
						<th>ID Tipo Permissao</th>
						<th>Tipo Permiss&atilde;o</th>
						<th>Tipo Permiss&atilde;o</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lista.getPermissoes()}" var="perm">
						<tr style="cursor: pointer !important;">
							<td>${perm.orgaoUsuario.id}</td>
							<td>${perm.orgaoUsuario.acronimoOrgaoUsu}</td>
							<td>${perm.complexo.idComplexo}</td>
							<td>${perm.complexo.nomeComplexo}</td>
							<td>${perm.lotacao.lotacaoAtual.id}</td>
							<td>${perm.lotacao.lotacaoAtual.nomeLotacao}</td>
							<td>${perm.lotacao.lotacaoAtual.siglaLotacao}</td>
							<td>${perm.dpPessoa.id}</td>
							<td>${perm.dpPessoa.nomePessoa}</td>
							<td>${perm.dpPessoa.nomeAbreviado}</td>
							<td>${perm.cargo.id}</td>
							<td>${perm.cargo.sigla}</td>
							<td>${perm.cargo.descricao}</td>
							<td>${perm.funcaoConfianca.id}</td>
							<td>${perm.funcaoConfianca.sigla}</td>
							<td>${perm.funcaoConfianca.descricao}</td>
							<td>${perm.getSrConfiguracaoTipoPermissaoJson()}</td>
							<td>${perm.id}</td>
							<td></td>
							<td>${perm.descrTipoPermissao}</td>
							<td class="gt-celula-nowrap"
								style="font-size: 13px; font-weight: bold; border-bottom: 1px solid #ccc !important; padding: 7px 10px;">
								<a class="once desassociarPermissao"
								onclick="desativarPermissaoUsoListaEdicao(event, ${perm.id})"
								title="Remover permissão"> <input class="idPermissao"
									type="hidden" value="${perm.id}" /> <img id="imgCancelar"
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;">
							</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form> 
</div>

