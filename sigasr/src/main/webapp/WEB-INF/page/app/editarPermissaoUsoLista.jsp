 #<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Cadastro de Permissao">

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<h2 class="gt-form-head">Cadastro de PermissÃ£o para Uso de Lista de Prioridade</h2>

		<div class="gt-form gt-content-box">
			<form action="@Application.gravarPermissaoUsoLista()" enctype="multipart/form-data">
			<c:if test="${permissao?.idConfiguracao}"> <input type="hidden"
				name="permissao.idConfiguracao"
				value="${permissao.idConfiguracao}"> </c:if>
			<c:if test="${not empty errors}">
			<p class="gt-error">Alguns campos obrigatÃ³rios nÃ£o foram
				preenchidos <sigasr:error /></p>
			</c:if>
			<div class="gt-form-row gt-width-66">
				<label>ÃrgÃ£o</label> 
				<sigasr:select name="permissao.orgaoUsuario" items="${orgaos}" valueProperty="idOrgaoUsu"
				labelProperty="nmOrgaoUsu" value="${permissao.orgaoUsuario?.idOrgaoUsu}">
				<sigasr:opcao valor="0">Nenhum</sigasr:opcao> 
				</sigasr:select>
			</div>
			<span style="color: red"><sigasr:error
					nome="permissao.orgaoUsuario" /></span>
			<div class="gt-form-row gt-width-66">		
				<label>Local</label> 
				<sigasr:select name="permissao.complexo" items="${locais}", valueProperty="idComplexo"
				labelProperty="nomeComplexo" value="${permissao.complexo?.idComplexo}">
				<sigasr:opcao valor="0">Nenhum</sigasr:opcao> 
				</sigasr:select>
			</div>		
			<div class="gt-form-row gt-width-66">
				<label>LotaÃ§Ã£o</label><sigasr:selecao
					tipo="lotacao" nome="permissao.lotacao"
					value="${permissao.lotacao?.lotacaoAtual}" />
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>Pessoa</label><sigasr:selecao
					tipo="pessoa" nome="permissao.dpPessoa"
					value="${permissao.dpPessoa?.pessoaAtual}" />
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>Cargo</label><sigasr:selecao
					tipo="cargo" nome="permissao.cargo"
					value="${permissao.cargo}" />
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>FunÃ§Ã£o</label><sigasr:selecao
					tipo="funcao" nome="permissao.funcaoConfianca"
					value="${permissao.funcaoConfianca}" />
			</div>
				<div class="gt-form-row gt-width-66">
				<label>Lista de Prioridade</label> 
				<sigasr:select name="permissao.listaPrioridade" items="${listasPrioridade}" valueProperty="idLista"
				labelProperty="nomeLista" value="${permissao.listaPrioridade?.idLista}">
				<sigasr:opcao valor="0">Nenhuma</sigasr:opcao> 
				</sigasr:select>
			</div>
			<div class="gt-form-row">
				<input type="submit" value="Gravar"
					class="gt-btn-medium gt-btn-left" />
				<a href="@{Application.listarPermissaoUsoLista}" class="gt-btn-medium gt-btn-left">Cancelar</a>
				<c:if test="${permissao?.idConfiguracao}"> <input type="button" value="Desativar"
					class="gt-btn-medium gt-btn-left"
					onclick="location.href='@{Application.desativarPermissaoUsoLista()}?id=${permissao.idConfiguracao}'" />
				</c:if>
			</div>
			</form>
		</div>
	</div>
</div>

