<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<siga:pagina titulo="Listar Pessoas">
<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['offset'].value=offset;
	frm.submit();
}
function validarCPF(Objcpf){
	var strCPF = Objcpf.replace(".","").replace(".","").replace("-","").replace("/","");
    var Soma;
    var Resto;
    Soma = 0;
	
    for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
    Resto = (Soma * 10) % 11;
	
    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(9, 10)) ) {
    	
    	alert('CPF Inválido!');
        return false;
	}
    Soma = 0;
    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
    Resto = (Soma * 10) % 11;

    if ((Resto == 10) || (Resto == 11))  Resto = 0;
    if (Resto != parseInt(strCPF.substring(10, 11) ) ) {
    	
    	alert('CPF Inválido!');
    	return false;
    }
    return true;
         
}
function cpf_mask(v){
	v=v.replace(/\D/g,"");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");

	if(v.length == 14) {
    	validarCPF(v);
    }
	return v;
	}
</script>
<form name="frm" action="listar" class="form" method="POST">
	<input type="hidden" name="offset" value="0" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">
				<h2 class="gt-table-head">Dados da Pessoa</h2>
				<div class="gt-content-box gt-for-table">
					<table border="0" class="gt-table">
						<tr>
							<td><label>Órgão:</label></td>
							<td><select name="idOrgaoUsu" value="${idOrgaoUsu}" onchange="carregarRelacionados(this.value)">
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td><label>Cargo:</label></td>
							<td>
								<select name="idCargoPesquisa" value="${idCargoPesquisa}">
									<c:forEach items="${listaCargo}" var="item">
										<option value="${item.idCargo}"
											${item.idCargo == idCargoPesquisa ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label>Fun&ccedil;&atilde;o de Confian&ccedil;a:</label></td>
							<td>
								<select name="idFuncaoPesquisa" value="${idFuncaoPesquisa}">
									<c:forEach items="${listaFuncao}" var="item">
										<option value="${item.idFuncao}"
											${item.idFuncao == idFuncaoPesquisa ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label>Lota&ccedil;&atilde;o:</label></td>
							<td>
								<select name="idLotacaoPesquisa" value="${idLotacaoPesquisa}">
									<c:forEach items="${listaLotacao}" var="item">
										<option value="${item.idLotacao}"
											${item.idLotacao == idLotacaoPesquisa ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><label>Nome:</label></td>
							<td><input type="text" id="nome" name="nome" value="${nome}" maxlength="100" size="30"/></td>
						</tr>
						<tr>
							<td><label>CPF:</label></td>
							<td><input type="text" id="cpfPesquisa" name="cpfPesquisa" value="${cpfPesquisa}" maxlength="14" size="14" onkeyup="this.value = cpf_mask(this.value)"/></td>
						</tr>
						
					</table>
				</div>
				<div class="gt-table-buttons">
					<input type="submit" value="Pesquisar" class="gt-btn-medium gt-btn-left"/>
				</div>
			</div>
		</div>
	
	
	
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">		
			<h2 class="gt-table-head">Pessoas cadastradas</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="left">Nome</th>
							<th align="left">CPF</th>
							<th align="left">Data de Nascimento</th>
							<th align="left">Matricula</th>
							<th colspan="2" align="center">Op&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="pessoa">
							<tr>
								<td align="left">${pessoa.descricao}</td>
								<td align="left">${pessoa.cpfFormatado}</td>
								<td align="left"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${pessoa.dataNascimento}" /></td>
								<td align="left">${pessoa.sigla}</td>
								<td align="left">
									<c:url var="url" value="/app/pessoa/editar">
										<c:param name="id" value="${pessoa.id}"></c:param>
									</c:url>
									<c:url var="urlAtivarInativar" value="/app/pessoa/ativarInativar">
										<c:param name="id" value="${pessoa.id}"></c:param>
									</c:url>
									<siga:link title="Alterar" url="${url}" />
									<c:choose>
										<c:when test="${empty pessoa.dataFimPessoa}">
											<siga:link title="Inativar" url="${urlAtivarInativar}" />		
										</c:when>
										<c:otherwise>
											<siga:link title="Ativar" url="${urlAtivarInativar}" />
										</c:otherwise>
									</c:choose>
								</td>
							<%--	<td align="left">									
	 			 					<a href="javascript:if (confirm('Deseja excluir o orgão?')) location.href='/siga/app/orgao/excluir?id=${orgao.idOrgao}';">
										<img style="display: inline;"
										src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir orgão"							
										onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
										onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
									</a>															
								</td>
							 --%>							
							</tr>
						</siga:paginador>
					</tbody>
				</table>				
			</div>	
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/pessoa/editar"></c:url>
					<c:url var="urlAtivarInativar" value="/app/pessoa/ativarInativar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
				</div>				
		</div>			
	</div>
</form>
<script>
function carregarRelacionados(id) {
	frm.action = 'carregarCombos';
	frm.submit();
}
</script>
</siga:pagina>