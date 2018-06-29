<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<style>
.thumb {
	height: 100px;
	border: 1px solid #000;
	margin: 10px 5px 0 0;
}

.botaoImagem {
	padding-left: 0.2cm;
	padding-right: 0.2cm;
}
</style>

<script language="javascript">
function carregarDadosDpPessoa(){
	//formulario_pessoa_pessoaSel_id - nome que retorna de selecao.tag
	params = escape($('#formulario_pessoa_pessoaSel_id').val());
	if(params != "")
		PassAjaxResponseToFunction('/sigatp/app/condutor/exibirDadosDpPessoa/' + params, 'carregouDadosDpPessoa', null, false, null);
}

function carregouDadosDpPessoa(response, param){
	$('#divItem').html(response);
	$("input[name='condutor.dtNascimento']").val($("input[name='dataNascimento']").val()); 
	$("input[name='condutor.lotacao']").val($("input[name='lotacao']").val()); 
	$("input[name='condutor.emailInstitucional']").val($("input[name='emailInstitucional']").val()); 
	$("input[name='condutor.dpPessoa']").val($("input[name='pessoa_pessoaSel.id']").val()); 
}

</script>

<siga:pagina titulo="Transportes">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${condutor.nomePessoaAI}</h2>
			<h3>${condutor.id > 0 ? "Editar Dados Cadastrais" : "Incluir Condutor" } </h3>
			<jsp:include page="menu.jsp" />
			<sigatp:erros />
			<br />
			<jsp:include page="form.jsp" />
		</div>
	</div>
</siga:pagina>