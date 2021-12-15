<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:set var="msg">
	<fmt:message key="tela.substituto.mensagem" />
</c:set>

<script>
	function hideShowSel(combo) {
		var sel1Span = document.getElementById('span'
				+ combo.name.substring(4));
		var sel2Span = document.getElementById('spanLota'
				+ combo.name.substring(4));
		if (combo.selectedIndex == 0) {
			sel1Span.style.display = "";
			sel2Span.style.display = "none";
		} else {
			sel1Span.style.display = "none";
			sel2Span.style.display = "";
		}
	}

	function verificaData() {
		var dataFim = document.getElementById("dtFimSubst").value;
		var dataInicio = document.getElementById("dtIniSubst").value;
		var mensagem = "Data de fim é obrigatória e limitada a 2 anos após Data de Início.";
		var atencao = " Atenção!";
		if (!dataFim == "" || !dataFim == null) {
			document.getElementById("atencao").innerHTML = "";
			document.getElementById("atencao").value = "";
			document.getElementById("dataFim").innerHTML = "";
			document.getElementById("dataFim").value = "";
		} else {
			document.getElementById("dataFim").innerHTML = mensagem;
			document.getElementById("dataFim").value = mensagem;
			document.getElementById("atencao").innerHTML = atencao;
			document.getElementById("atencao").value = atencao;
		}
		document.getElementById("dtFimSubst").focus;
		return false;
	}

	function aviso(msg) {
		var dataFim = document.getElementById("dtFimSubst").value;
		var dataInicio = document.getElementById("dtIniSubst").value;
		var mensagem = msg;
		var atencao = "Importante";
		document.getElementById("dataFim").innerHTML = mensagem;
		document.getElementById("dataFim").value = mensagem;
		document.getElementById("atencao").innerHTML = atencao;
		document.getElementById("atencao").value = atencao;
		document.getElementById("dtFimSubst").focus;
	}
</script>

<siga:pagina titulo="Cadastro de substituição">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da substituição</h5>
			</div>
			<div class="card-body">
			
				<form action="gravar" onsubmit="verificaData()" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="substituicao.idSubstituicao" value="${substituicao.idSubstituicao}"/>
					<c:set var="dataFim" value="" />
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Titular</label>
								<select  name="tipoTitular" onchange="javascript:hideShowSel(this);" class="form-control">
									<c:forEach items="${listaTipoTitular}" var="item">
										<option value="${item.key}" ${item.key == tipoTitular ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>
								<c:choose>
									<c:when test="${tipoTitular == 1}">
										<c:set var="titularStyle" value="" />
										<c:set var="lotaTitularStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoTitular == 2}">
										<c:set var="titularStyle" value="display:none" />
										<c:set var="lotaTitularStyle" value="" />
									</c:when>
								</c:choose>
							</div> 
						</div>
						<div class="col-sm-10">
							<div class="form-group">
								<label>&nbsp;&nbsp;&nbsp;</label>
								<span id="spanTitular" class="align-botton" style="${titularStyle}"> 
									<siga:selecao modulo="siga" propriedade="titular" tema="simple" /> 
								</span> 
								<span id="spanLotaTitular" class="align-botton" style="${lotaTitularStyle}"> 
									<siga:selecao modulo="siga" propriedade="lotaTitular" tema="simple" paramList="${strBuscarFechadas}"/> 
								</span>
							</div>									
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Substituto</label>
								<select  name="tipoSubstituto" onchange="javascript:hideShowSel(this);" class="form-control">
									<c:forEach items="${listaTipoSubstituto}" var="item">
										<option value="${item.key}" ${item.key == tipoSubstituto ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>									

								<c:choose>
									<c:when test="${tipoSubstituto == 1}">
										<c:set var="substitutoStyle" value="" />
										<c:set var="lotaSubstitutoStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoSubstituto == 2}">
										<c:set var="substitutoStyle" value="display:none" />
										<c:set var="lotaSubstitutoStyle" value="" />
									</c:when>
								</c:choose> 
							</div>
						</div>
						<div class="col-sm-10">
							<div class="form-group ">
								<label>&nbsp;&nbsp;&nbsp;</label>
								<span id="spanSubstituto" style="${substitutoStyle}"> 
									<siga:selecao modulo="siga" propriedade="substituto" tema="simple"/> 
								</span> 
								<span id="spanLotaSubstituto" style="${lotaSubstitutoStyle}"> 
									<siga:selecao  modulo="siga" propriedade="lotaSubstituto" tema="simple" /> 
								</span>
							</div>									
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Data de Início</label>
								<input type="text" id="dtIniSubst" name="dtIniSubst" label="Data de Início" value="${dtIniSubst}"
									onblur="javascript:verifica_data(this, true);" theme="simple" class="form-control" />
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Data de Fim</label>
								<input type="text" id="dtFimSubst" name="dtFimSubst" label="Data de Fim" value="${dtFimSubst}"
									onblur="javascript:verifica_data(this, true);" theme="simple" class="form-control" />
									<small id="emailHelp" class="form-text text-muted">(obrigatório e limitado a 2 anos a partir da data inicial)</small>
							</div>
						</div>
					</div>
					<c:if test="${ empty dataFim }">
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<b><span id="atencao" ></span></b>
							</div>
						</div>
						<div class="col-sm">
							<div class="form-group">
								<span id="dataFim"></span>					
							</div>
						</div>
					</div>					
					</c:if>
					<div class="row">
						<div class="col-sm-2">
							<button type="submit" class="btn btn-primary">OK</button>
							<button type="button"  class="btn btn-primary" onclick="javascript:history.back();">Cancela</button>
						</div>						
					</div>
				</form>
			</div>
			<script>
				aviso("${msg}");
			</script>
		</div>
	</div>
</siga:pagina>