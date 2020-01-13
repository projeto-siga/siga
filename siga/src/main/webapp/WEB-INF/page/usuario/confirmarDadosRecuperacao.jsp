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
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h1 class="gt-form-head">${param.titulo}</h1>
			<h2 class="gt-form-head">Dados de recuperação</h2>
			
			<br/>
			<div class="gt-form gt-content-box tabela-senha">
					<h1>${mensagem } </h1>
			</div>
		</div>
	</div>

</siga:pagina>

