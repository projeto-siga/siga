<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="Sobrestar">
<script type="text/javascript" language="Javascript1.1">
function submeter() {
	document.getElementById("button_ok").onclick = function(){console.log("Aguarde requisição")};	
	document.getElementById('frm').submit();
}
</script>
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Sobrestar - ${sigla}</h5>
			</div>
			<div class="card-body">
			<form name="frm" id="frm" action="sobrestar_gravar" method="post">
				<input type="hidden" name="id" value="${id}" />
				<input type="hidden" name="sigla" value="${sigla}" id="transferir_gravar_sigla" />
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label>Data que o documento volte a ficar ativo</label> 
							<input type="text" name="dtDesobrestarString"onblur="javascript:verifica_data(this,0);" value="${dtDesobrestarString}" class="form-control"/>					 
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button accesskey="o" id="button_ok" onclick="javascript:submeter();" class="btn btn-primary"><u>O</u>k</button>														
							<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" class="btn btn-secondary">Cancela</a>
						</div>
					</div>
				</div>				
			</form>
			</div>
		</div>
	</div>
</siga:pagina>