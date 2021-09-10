<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	var idOrgaoUsu = document.getElementsByName('idOrgaoUsu')[0].value;

	if(idOrgaoUsu==null || idOrgaoUsu == 0) {
		sigaModal.alerta("Selecione um órgão");
		document.getElementById('idOrgaoUsu')[0].focus();
		return;	
	}

	if (offset == null) {
		offset = 0;
		frm.elements["paramTamanho"].value = null;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
	
	frm.method = "POST";
	frm.submit();	
	frm.method = "GET";
}

function enviar() {
	var frm = document.getElementById('enviarEmail');
	frm.action = 'enviar';
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

<siga:pagina titulo="Listar Pessoas">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/css/siga.multiploselect.css" type="text/css" media="screen, projection" />

	<!-- main content -->
	<div class="container-fluid">
		<form name="frm" id="enviarEmail" action="enviarEmail" class="form100" method="Post">
			<input type="hidden" name="paramoffset" value="0" />
			<input type="hidden" name="p.offset" value="0" />
			<input type="hidden" name="retornarEnvioEmail" value="true" />
			<input type="hidden" name="paramTamanho" value="${tamanho}" />
			<div class="card bg-light mb-3" >
				<div class="card-header">
					<h5>Envio de E-mail para Novos Usuários</h5>
				</div>			
				<div class="card-body">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label for="uidOrgaoUsu">Órgão</label>
								<select name="idOrgaoUsu" id="idOrgaoUsu" value="${idOrgaoUsu}" onchange="carregarRelacionados(this.value)" class="form-control  siga-select2">
									<option value="0">Selecione</option> 
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}</option>
									</c:forEach>
								</select>
							</div>					
						</div>
						<div class="col-md-5">
							<div class="form-group">
								<label for="lotacao"><fmt:message key="usuario.lotacao"/></label>
								<input type="hidden" name="idLotacaoPesquisa" value="${idLotacaoPesquisa}" id="inputHiddenLotacoesSelecionadas" />
								<select id="lotacao" class="form-control  siga-multiploselect  js-siga-multiploselect--lotacao">
									<c:forEach items="${listaLotacao}" var="item">
										<option value="${item.idLotacao}">${item.descricao}</option>
									</c:forEach>
								</select>
							</div>					
						</div>
						<div class="col-md-4">
							<div class="form-group">  																													
								<label for="usuario">Usuário</label>
									<div style="display: flex; width: 100%">
										<input type="hidden" name="idUsuarioPesquisa" value="${idUsuarioPesquisa}" id="inputHiddenUsuariosSelecionados" />																			
					                    <select id="usuario" class="form-control  siga-multiploselect  js-siga-multiploselect--usuario">
					                        <optgroup label="Secretaria de Governo">
					                            <option>User 1</option>                  
					                            <option>User 2</option>
					                            <option>User 3</option>
					                            <option>User 4</option>
					                        </optgroup>				                        
					                    </select>	
				                    <div>
				                    	<span class="spinner"></span>
				                    </div>
				            	</div>		                    
		                    </div>					
						</div>					
					</div>
					<div class="row">
						<c:if test="false"> <!-- Desativada por deterioração da consulta -->
							<div class="col-md-3">
								<div class="form-group">
									<label for="nome">Nome</label>
									<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" class="form-control"/>
								</div>					
							</div>		
						</c:if>			
						<div class="col-md-2">
							<div class="form-group">
								<label for="nome">CPF</label>
								<input type="text" id="cpfPesquisa" name="cpfPesquisa" value="${cpfPesquisa}" maxlength="14" onkeyup="this.value = cpf_mask(this.value)" class="form-control"/>
							</div>					
						</div>					
					</div>
					<div class="row">
						<div class="col-sm-2">
							<button type="button" onclick="javascript: sbmt();" class="btn btn-primary">Pesquisar</button>
						</div>
					</div>				
				</div>
			</div>
		
			<h3 class="gt-table-head">Pessoas</h3>
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color}">
					<tr>
						<th align="left">Nome</th>
						<th align="left">CPF</th>
						<th align="left">Data de Nascimento</th>
						<th align="left">Matrícula</th>
						<th align="left"><fmt:message key="usuario.lotacao"/></th>			
					</tr>
				</thead>
				<tbody>
					<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
						itens="${itens}" var="pessoa">
						<tr>
							<td align="left">${pessoa.descricao}</td>
							<td align="left">${pessoa.cpfFormatado}</td>
							<td align="left"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${pessoa.dataNascimento}" /></td>
							<td align="left">${pessoa.sigla}</td>
							<td align="left">${pessoa.lotacao.nomeLotacao}</td>				
						</tr>
					</siga:paginador>
				</tbody>
			</table>				
			<div class="gt-table-buttons">
				<c:url var="url" value="/app/pessoa/enviar"></c:url>
				<button type="button" class="btn btn-primary" data-siga-modal-abrir="confirmacaoModal">
				  Enviar E-mail
				</button>
			</div>				
		</form>					
		<siga:siga-modal id="confirmacaoModal" exibirRodape="true" descricaoBotaoFechaModalDoRodape="Cancelar" linkBotaoDeAcao="javascript:enviar();">
			<div class="modal-body">Deseja realmente enviar e-mail para Novo(s) Usuário(s)?</div>				
		</siga:siga-modal>	
	</div>

<script>
function carregarRelacionados(id) {
	frm.method = "POST";
	frm.action = 'carregarCombos';
	frm.submit();
	frm.method = "GET";
}
</script>

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
<script type="text/javascript" src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.multiploselect.js"></script>
<script type="text/javascript" src="/siga/javascript/pessoa.combo-usuario-lotacao.js"></script>
</siga:pagina>