<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<script>
$(function() {
    $('#formConfiguracaoGI').submit(function() {
		$("input[name='cpConfiguracao.lotacao']").val($("input[name='lotacao_lotacaoSel.id']").val()); 
		$("input[name='cpConfiguracao.dpPessoa']").val($("input[name='pessoa_pessoaSel.id']").val()); 
        return true;
    });
});
</script>

<form id="formConfiguracaoGI" action="${linkTo[ConfiguracaoGIController].salvar}" method="post" cssClass="form" enctype="multipart/form-data">
	<input type="hidden" name="cpConfiguracao" value="${cpConfiguracao.id}" />	
	<input type="hidden" name="cpConfiguracao.idConfiguracao" value="${cpConfiguracao.idConfiguracao}" />
	<div class="gt-content-box gt-form clearfix">
		<div class="coluna margemDireitaG">
		    <input type="hidden" name="cpConfiguracao.orgaoUsuario" value="${cpConfiguracao.orgaoUsuario.idOrgaoUsu}"/>
		    <label for= "cpConfiguracao.lotacao" > Lota&ccedil;&atilde;o</label>
		    <input type="hidden" name="cpConfiguracao.lotacao" class="selecao">
			<siga:selecao tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga" urlAcao="buscar" siglaInicial="${cpConfiguracao.lotacao}" idInicial="${cpConfiguracao.lotacao.id}" descricaoInicial="${cpConfiguracao.lotacao.descricao}"/>
			
			<label for="cpConfiguracao.dpPessoa">Servidor</label>
			<input type="hidden" name="cpConfiguracao.dpPessoa" value="" />
			<siga:selecao tipo="pessoa" propriedade="pessoa" tema="simple" modulo="siga" urlAcao="buscar" siglaInicial="${cpConfiguracao.dpPessoa}" idInicial="${cpConfiguracao.dpPessoa.id}" descricaoInicial="${cpConfiguracao.dpPessoa.descricao}"/>
							
		    <label for= "cpConfiguracao.cpSituacaoConfiguracao" class="obrigatorio">  Situa&ccedil;&atilde;o Configura&ccedil;&atilde;o</label>	
   			<siga:select id="cpSituacaoConfiguracao" name="cpConfiguracao.cpSituacaoConfiguracao" list="cpSituacoesConfiguracao" listKey="id" listValue="dscSitConfiguracao" value="${cpConfiguracao.cpSituacaoConfiguracao.descr}"/>
			
			<label for= "cpConfiguracao.cpTipoConfiguracao" class= "obrigatorio"> Tipo de Configura&ccedil;&atilde;o</label>	
   			<siga:select id="cpTipoConfiguracao" name="cpConfiguracao.cpTipoConfiguracao" list="cpTiposConfiguracao" listKey="idTpConfiguracao" listValue="dscTpConfiguracao" value="${cpConfiguracao.cpTipoConfiguracao.idTpConfiguracao}" />
			
			<label for= "cpConfiguracao.complexo" class= "obrigatorio">Complexo</label>	
   			<siga:select id="complexo" name="cpConfiguracao.complexo" list="cpComplexos" listKey="idComplexo" listValue="nomeComplexo" value="${cpConfiguracao.complexo.idComplexo}"/>
		</div>
	</div>
	
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" onClick="javascript:location.href='${linkTo[ConfiguracaoGIController].pesquisar(cpConfiguracao.orgaoUsuario.idOrgaoUsu)}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>