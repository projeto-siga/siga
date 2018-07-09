<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:telefone/>

<form name="formCondutor" id="formCondutor"
	action="${linkTo[CondutorController].salvar}" method="post"
	cssClass="form" enctype="multipart/form-data">
	<div class="gt-content-box gt-form">
		<c:choose>
			<c:when test="${condutor.id == 0}">
				<div id="divItem">
					<jsp:include page="exibirDadosDpPessoa.jsp" />
				</div>

				<label for="condutor.dpPessoa.id">Servidor </label>
				<sigatp:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" funcaoBuscar="carregarDadosDpPessoa()"  
				urlAcao="buscar" siglaInicial="${condutor.dpPessoa}" idInicial="${condutor.dpPessoa.id}" descricaoInicial="${condutor.dpPessoa.descricao}"/>

				<input type="hidden" name="condutor.dpPessoa" value="" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="condutor.dpPessoa" value="${condutor.dpPessoa.id}" />
			</c:otherwise>
		</c:choose>

		<input type="hidden" name="condutor" value="${condutor.id}"/>
		
		<div class="clearfix">
			<div class="coluna margemDireitaG">
				<label for="condutor.dtNascimento">Data de nascimento</label> 
				<input type="text" readonly="readonly" name="condutor.dtNascimento"
					value="<fmt:formatDate pattern="dd/MM/yyyy" value="${condutor.dpPessoa.getDataNascimento()}" />" />

				<label for="condutor.numeroCNH" class="obrigatorio">N&uacute;mero da CNH</label> 
				<input type="text" name="condutor.numeroCNH" value="${condutor.numeroCNH}" /> 
					
				<label for="categoriaCNH" class="obrigatorio">Categoria CNH</label>
				<siga:select name="condutor.categoriaCNH" list="listCategorias" listKey="descricao" listValue="descricao" value="${condutor.categoriaCNH}"/>

				<label for="condutor.dataVencimentoCNH" class="obrigatorio">Data de Vencimento da CNH</label> 
				<input type="text" name="condutor.dataVencimentoCNH" class="datePicker"
					value="<fmt:formatDate pattern="dd/MM/yyyy" value="${condutor.dataVencimentoCNH.time}" />" />
			</div>
			<div class="coluna margemDireitaG">
				<label for="condutor.lotacao">Lota&ccedil;&atilde;o</label> 
				<input type="text" name="condutor.lotacao" size="46" readonly="readonly" value="${condutor.dpPessoa.getLotacao() == null ? '' : condutor.dpPessoa.getLotacao().getDescricao()}" />

				<label for="condutor.endereco">Endere&ccedil;o</label>
				<input type="text" size="46" name="condutor.endereco" value="${condutor.endereco}" /> 
				
				<label for="condutor.emailPessoal">Email pessoal </label> 
				<input type="text" name="condutor.emailPessoal" value="${condutor.emailPessoal}" /> 
				
				<label for="condutor.emailInstitucional">Email institucional </label> 
				<input type="text" readonly="readonly" name="condutor.emailInstitucional" value="${condutor.dpPessoa.getEmailPessoa()}" />
			</div>
			<div class="coluna">
				<label for="condutor.telefoneInstitucional" class="obrigatorio">Telefone fixo institucional</label> 
				<input type="text" class="telefone" name="condutor.telefoneInstitucional" value="${condutor.telefoneInstitucional}" /> 
				
				<label for="condutor.celularInstitucional">Telefone celular	institucional</label> 
				<input type="text" class="celular" name="condutor.celularInstitucional"	value="${condutor.celularInstitucional}" /> 
				
				<label for="condutor.telefonePessoal">Telefone fixo pessoal </label> 
				<input type="text" class="telefone" name="condutor.telefonePessoal"	value="${condutor.telefonePessoal}" /> 
				
				<label for="condutor.celularPessoal">Telefone celular pessoal </label> 
				<input type="text" class="celular" name="condutor.celularPessoal" value="${condutor.celularPessoal}" />
			</div>
		</div>

		<input type="hidden" id="situacaoImagem" name="condutor.situacaoImagem" value="${condutor.situacaoImagem}" />
		<label for="condutor.observacao">Observa&ccedil;&atilde;o</label>
		<sigatp:controleCaracteresTextArea nomeTextArea="condutor.observacao" rows="4" cols="60" valorTextArea="${condutor.observacao}" />
										   
		<label>Anexar arquivo </label> 
		<input type="file" name="arquivo" size="30" id="arquivo"  /> 
		<img id="imgArquivo" class="thumb" src="${imgArquivo}" />
		<br /> 
		<input type="button" class="botaoImagem" id="exibirImagem" value="<fmt:message key="views.botoes.exibir"/>" /> 
		<input type="button" class="botaoImagem" id="excluirImagem" onclick="removerArquivo();" value="<fmt:message key="views.botoes.excluir"/>" />

	</div>

	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio" /></span>
	
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" /> 
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" class="gt-btn-medium gt-btn-left"
							  onclick="javascript:window.location = '${linkTo[CondutorController].listar}';" />
	</div>

	<jsp:include page="formScript.jsp"/>
</form>