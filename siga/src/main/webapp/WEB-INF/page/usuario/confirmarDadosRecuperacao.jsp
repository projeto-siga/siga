conform<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<style>
.tabela-senha td {
	padding: 3px 5px 3px 5px;
}

.botaoDica {
	style="position: relative; 
	margin-top: -3px; 
	top: +3px; 
	left: +3px; 
	z-index: 0;
}
</style>

<siga:pagina popup="false" titulo="Alteração de dados de recuperação">
	<!-- main content -->
	<div class="card bg-light mb-3" >

		<div class="card-header">
			<h5>${param.titulo}</h5>
			<h2 class="gt-form-head">Alteração de dados de recuperação</h2>
		</div>

		<div class="card-body">
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<div class="gt-form gt-content-box tabela-senha">
							<h5>${mensagem } </h5>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</siga:pagina>

