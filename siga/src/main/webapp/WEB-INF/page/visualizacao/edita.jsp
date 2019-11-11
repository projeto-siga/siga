<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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
		var dataFim = document.getElementById("dtFimDeleg").value;
		var dataInicio = document.getElementById("dtIniDeleg").value;
		var mensagem = "Data de fim vazia. A delegação ficará valendo até que seja manualmente cancelada.";
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
		document.getElementById("dtFimDeleg").focus;
		return false;
	}

	function aviso() {
		var dataFim = document.getElementById("dtFimDeleg").value;
		var dataInicio = document.getElementById("dtIniDeleg").value;
		var mensagem = "Caso a 'Data de Início' não seja informada, será assumida a data atual.<br/>Caso a 'Data de Fim' não seja informada, a delegação ficará valendo até que seja manualmente cancelada.";
		var atencao = "Importante";
		document.getElementById("dataFim").innerHTML = mensagem;
		document.getElementById("dataFim").value = mensagem;
		document.getElementById("atencao").innerHTML = atencao;
		document.getElementById("atencao").value = atencao;
		document.getElementById("dtFimDeleg").focus;
	}
</script>   
<siga:pagina titulo="Cadastro de Delegação">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da Delegação</h5>
			</div>
			<div class="card-body">
			
				<form action="gravar" onsubmit="verificaData()" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="visualizacao.idVisualizacao" value="${visualizacao.idVisualizacao}"/>
					<c:set var="dataFim" value="" />
					<div class="form-group">
						<div class="row">
							<div class="col-sm-4">
								<div class="form-group">
									<label for="tipoTitular">Titular Delegante (matrícula)</label>					
								</div> 
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-12">
								${titularSel }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${titularSel.nomePessoa}
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Delegado (matrícula)</label>
								<span id="spanSubstituto" style="${substitutoStyle}"> 
									<siga:selecao modulo="siga" propriedade="delegado" tema="simple"/> 
								</span>
							</div>
						</div>
					</div> 
					
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Data de Início</label>
								<input type="text" id="dtIniDeleg" name="dtIniDeleg" label="Data de Início" value="${dtIniDeleg}"
									onblur="javascript:verifica_data(this, true);" theme="simple" class="form-control" />
									<small id="emailHelp" class="form-text text-muted">(opcional)</small>
								
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<label for="tipoTitular">Data de Fim</label>
								<input type="text" id="dtFimDeleg" name="dtFimDeleg" label="Data de Fim" value="${dtFimDeleg}"
									onblur="javascript:verifica_data(this, true);" theme="simple" class="form-control" />
									<small id="emailHelp" class="form-text text-muted">(opcional)</small>
							</div>
						</div>
					</div>
					<c:if test="${ empty dataFim }">
					<div class="row">
						<div class="col-sm-1">
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
						<div class="col-sm-4">
							<button type="submit" class="btn btn-primary">OK</button>
							<button type="button"  class="btn btn-primary" onclick="javascript:history.back();">Cancela</button>
						</div>						
					</div>
					

				</form>
			</div>
			<script>
				aviso();
			</script>
		</div>
	</div>
</siga:pagina>