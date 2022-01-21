<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="Integrar via WS">

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
				<h5>Integrar - ${mob.siglaEDescricaoCompleta}</h5>
			</div>
			<div class="card-body">
			<form name="frm" id="frm" action="integrar_gravar" method="post">				
				<input type="hidden" name="sigla" value="${sigla}" id="sigla" />
					
				<div class="row">
					<div class="col col-4">
						<div class="form-group">
							<label>Usuário</label> 
							<input type="text" size="30" name="usuario" class="form-control"/>			 
						</div>
					</div>
					
					<div class="col col-4">
						<div class="form-group">
							<label>Senha</label> 
							<input type="password" size="30" name="senha" class="form-control"/>			 
						</div>
					</div>					
				</div>
				<div class="row">
					<div class="col col-12">
						<div class="form-group">
							<a accesskey="o" id="button_ok" onclick="javascript:submeter();" class="btn btn-primary"><u>O</u>k</a>																			
							<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}" class="btn btn-light ml-2">Cancela</a>
						</div>
					</div>
				</div>				
			</form>
			</div>
		</div>
	</div>
</siga:pagina>
