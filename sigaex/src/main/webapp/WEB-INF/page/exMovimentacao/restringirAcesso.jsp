<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Restringir Acesso">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>

	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Restringir Acesso - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="restringir_acesso_gravar" namespace="/expediente/mov" theme="simple" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
       				<input type="hidden" name="usu" id="usu" value=""/>
       				<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Nível de Acesso</label>
								<select class="form-control" name="nivelAcesso" theme="simple" value="1">
							      <c:forEach var="item" items="${listaNivelAcesso}">
							      	<c:if test="${(siga_cliente ne 'GOVSP') || ( (item.idNivelAcesso eq 5) )}">
								        <option value="${item.idNivelAcesso}"  <c:if test="${item.idNivelAcesso == nivelAcesso}">selected</c:if> >
								          <c:out value="${item.nmNivelAcesso}" />
								        </option>
							        </c:if>
							      </c:forEach>
							    </select>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-sm-12">
							<label>Indique os usuários que terão acesso ao documento</label>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<siga:selecao tipo="pessoa" propriedade="pessoaObjeto"
									tema="simple" modulo="siga" />
						</div>
						<div class="col-sm-6">
							<button type="button" class="btn btn-success" id="adicionar">+</button>
						</div>
					</div>	
					<div class='row bg-light' id="divUsuarios">
						<div class="col-sm-12">
							<label>Pessoas Selecionadas</label>
						</div>
						<hr/>
					</div>					
					
					
					<div class="row">
						<div class="col-sm">
							<input type="button" value="Ok" id="ok" class="btn btn-primary"/> 
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> 
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>


<script>
	$("#adicionar").click(function(){
		var id = document.getElementById('formulario_pessoaObjeto_pessoaSel_id').value;
		var matricula = document.getElementById('formulario_pessoaObjeto_pessoaSel_sigla').value;
		var nome = document.getElementById('pessoaObjeto_pessoaSelSpan').value;
		var usu = document.getElementById("usu").value;
		if(id != "") {
	    	$("#divUsuarios").append("<div class='col-sm-12 mb-1' id='div"+id+"'>" + nome  + " (" + matricula + ") <button type='button' class='btn btn-danger d-inline' style='float: right;' onclick='remover("+id+");'> - </button></div>");
	    	if(usu == "") {
				usu = ";";
			}
			
	    	document.getElementById("usu").value = usu + id + ";";
		}
		
	});

	$("#ok").click(function(){
		var usuarios = document.getElementById("usu").value;
		if(usuarios.length > 1) {
			frm.submit();
		} else {
			alert("Selecione pelo menos uma pessoa.");
		}
	});

	function remover(id) {
		$('#div' + id).remove();
		document.getElementById("usu").value = document.getElementById("usu").value.replace(";"+id+";",";"); 
	}
</script>
