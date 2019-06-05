<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>--%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<script language="Javascript1.1" type="text/javascript">
function confirma(){
	var mensagem = "Essa movimentação não poderá ser desfeita. Prosseguir?";
	mensagemConfirmacao(mensagem, confirmar)
}

function mensagemConfirmacao(mensagem, funcaoConfirmacao) {
	$('#modalConfirmacao').find('.modal-body').html(mensagem);
	$('#btnConfirmacao').click(funcaoConfirmacao);
	$('#modalConfirmacao').modal();
}

var confirmar = function () {
	document.formulario.submit();
}
</script>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Registro de Publica&ccedil;&atilde;o do Boletim Interno - ${doc.codigo}</h5>
			</div>
			<div class="card-body">
			<form name="formulario" id="formulario" action="boletim_publicar_gravar" namespace="/expediente/mov" cssClass="form" method="get">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="sigla" value="${sigla}"/>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Data da Publica&ccedil;&atilde;o</label> 
							<input type="text" name="dtPubl" onblur="javascript:verifica_data(this,0);" label="Data da Publicação" value="${dtPubl}" class="form-control"/>
						</div>
					</div>
				</div>				
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="button" onclick="javascript: confirma();" class="btn btn-primary">Ok</button> 
							<button type="button" onclick="javascript:history.back();" class="btn btn-primary">Cancela</button>
						</div>
					</div>
				</div>
			</form>				
			</div>
		</div>
		<!-- Modal Confirmacao -->
		<div class="modal fade" id="modalConfirmacao" tabindex="-1" role="dialog" aria-labelledby="modalConfirmacao" aria-hidden="true">
			<div class="modal-dialog" role="document">
		    	<div class="modal-content">
		      		<div class="modal-header">
				        <h5 class="modal-title" id="exampleModalLabel">Confirma��o</h5>
				        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
				          <span aria-hidden="true">&times;</span>
				        </button>
					</div>
			      	<div class="modal-body">
					</div>
				    <div class="modal-footer">
				    	<button id="btnConfirmacao" type="button" class="btn btn-primary">Ok</button>
				        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
				    </div>
		  		</div>
		  	</div>
		</div>			
		<!-- Fim Modal Confirmacao-->
	</div>
</siga:pagina>