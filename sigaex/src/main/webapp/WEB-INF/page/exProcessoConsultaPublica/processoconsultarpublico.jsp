<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>


<c:set var="titulo"><fmt:message key="tela.consultaprocessopublico.titulo" /></c:set>
<c:set var="texto1"><fmt:message key="tela.consultaprocessopublico.texto1" /></c:set>
<c:set var="texto2"><fmt:message key="tela.consultaprocessopublico.texto2" /></c:set>

<siga:pagina titulo="Consulta de Processos Públicos" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<div class="container-fluid">
		<div class="row">
			<div class="col col-12 col-sm-4">
				<div class="card bg-light mb-3" >
					<div class="card-header">
						<h5>
							${titulo}
						</h5>
					</div>
					<div class="card-body">
		
						<form action="${request.contextPath}/public/app/processoconsultarpublico"	method="GET" class="form">
							<div class="row">
								<div class="col">
									<div class="form-group">
										<label>Número do Processo</label> 
										<input type="text" id="id_number" name="n" placeholder="Informe o número do Processo" class="form-control" value="${n}"/>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<div class="form-group">
										<label>Verificação</label> 
										<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col">
									<button type="submit" class="btn btn-lg btn-primary btn-block"><i class="fas fa-stamp"></i> Consultar</button>
								</div>
							</div>
						</form>
					</div>	
				</div>
			</div>
			
				<div class="col">
			
				<div class="card mb-3" >
				  <div class="card-header">
				    <h5>Informações Gerais</h5>
				  </div>
				  <div class="card-body">
				  
				    <c:if test="${!fn:startsWith(texto1,'???')}">	<p>    ${texto1} </p></c:if>
					<c:if test="${!fn:startsWith(texto2,'???')}">	<p><u> ${texto2} </u></p></c:if> 
					
					 
				  </div>
				</div>
			</div>
			
		</div>
	</div>
	</div>
	</div>

</siga:pagina>