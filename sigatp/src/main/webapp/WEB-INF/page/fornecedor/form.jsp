<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:telefone/>
<sigatp:cnpj/>
<sigatp:cep/>
<sigatp:email/>
<sigatp:erros/>

<script>
 	$(document).ready(function() { 

 		$('.cep').blur(function() {

   			var cep = $('.cep').val().replace("-","");
   			var url = "http://cep.correiocontrol.com.br/" + cep + ".json";

   			$.ajax({
   	            type: "GET",
   	            url: url,
   	            dataType: 'json',
   	            data: "",
   	            success: function(data) {
   	                if(data != null){
   	                	$("#logradouro").val(data["logradouro"].toUpperCase());
   	   	                $("#bairro").val(data["bairro"].toUpperCase());
   	   	          		$("#localidade").val(data["localidade"].toUpperCase());
   	   	          		$("#uf").val(data["uf"].toUpperCase());
   	                }    
   	            }
   	        });
 		});
	});
</script>

<form action="${linkTo[FornecedorController].salvar}" enctype='multipart/form-data' method="post">
	<input type="hidden" name="fornecedor" value="${fornecedor.id}" />
	<div class="gt-content-box gt-form  clearfix">     
       	<label for="fornecedor.ramoDeAtividade" class= "obrigatorio">Ramo de Atividade</label>
		<select name="fornecedor.ramoDeAtividade" >
			<c:forEach items="${fornecedor.ramoDeAtividade.values()}" var="ramoDeAtividade">
				<option value="${ramoDeAtividade}" ${ramoDeAtividade == fornecedor.ramoDeAtividade ? 'selected' : ''}>${ramoDeAtividade.descricao}</option>
			</c:forEach>
		</select>	
       	<label for="fornecedor.razaoSocial" class= "obrigatorio">Razão Social</label>
        <input class="clearfix" type="text" name="fornecedor.razaoSocial" value="${fornecedor.razaoSocial}" size="100"/>
		<div class="coluna margemDireitaG">
	       	<label for="fornecedor.cnpj" class= "obrigatorio">CNPJ</label>
	        <input type="text" name="fornecedor.cnpj" value="${fornecedor.cnpj}" size="18" class="cnpj"/>
	        <label for="fornecedor.nomeContato">Contato</label>
	        <input type="text" name="fornecedor.nomeContato" size="40" value="${fornecedor.nomeContato}" />
	       	<label for="fornecedor.telefone">Telefone</label>
	       	<input type="text" name="fornecedor.telefone" value="${fornecedor.telefone}" size="14" class="telefone"/>
	       	<label for="fornecedor.fax">Fax</label>
	       	<input type="text" name="fornecedor.fax" value="${fornecedor.fax}" size="14" class="telefone"/>
	       	<label for="fornecedor.eMail">E-Mail</label>
	       	<input type="text" name="fornecedor.eMail" value="${fornecedor.eMail}" size="40" class="email" />
	       	<label for="fornecedor.enderecoVirtual">Endereço Virtual</label>
	       	<input type="text" name="fornecedor.enderecoVirtual" value="${fornecedor.enderecoVirtual}" size="40" />
		</div>
		<div class="coluna">
	       	<label for="fornecedor.cep">CEP</label>
	       	<input type="text" name="fornecedor.cep" value="${fornecedor.cep}" size="6" class="cep"/>
	       	<label for="fornecedor.Logradouro">Logradouro</label>
	       	<input type="text" id="logradouro" name="fornecedor.logradouro" value="${fornecedor.logradouro}" size="70" />
	       	<label for="fornecedor.complemento">Complemento</label>
	       	<input type="text" id="complemento" name="fornecedor.complemento" value="${fornecedor.complemento}" size="40" />
	       	<label for="fornecedor.bairro">Bairro</label>
	       	<input type="text" id="bairro" name="fornecedor.bairro" value="${fornecedor.bairro}" size="40"/>
	       	<label for="fornecedor.localidade">Localidade</label>
	       	<input type="text" id="localidade" name="fornecedor.localidade" value="${fornecedor.localidade}" size="40" />

	       	<label for="fornecedor.uf">UF</label>
				<siga:select name="fornecedor.uf"
					list="listaUF" listKey="sigla"
					id="sigla" headerValue="" headerKey="0"
					listValue="sigla" theme="simple" value="${fornecedor.uf}" />
		</div>
	</div>
	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" /> 
		<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" class="gt-btn-medium gt-btn-left"
			onclick="javascript:window.location = '${linkTo[FornecedorController].listar}';" />
	</div>
</form>	
