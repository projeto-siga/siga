<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mensagem" prefix="siga-mensagem"%>
<style>
#passwordStrength {
	height: 10px;
	display: block;
	float: left;
}

.strength0 {
	width: 250px;
	background: #cccccc;
}

.strength1 {
	width: 50px;
	background: #ff0000;
}

.strength2 {
	width: 100px;
	background: #ff5f5f;
}

.strength3 {
	width: 150px;
	background: #56e500;
}

.strength4 {
	background: #4dcd00;
	width: 200px;
}

.strength5 {
	background: #399800;
	width: 250px;
}

.tabela-senha td {
	padding: 3px 5px 3px 5px;
}
</style>

<siga:pagina popup="false" titulo="Receber notificação por email">
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<c:if test="${baseTeste}">
			<div id="msgSenha"
				style="font-size: 12pt; color: red; font-weight: bold;">ATENÇÃO:
				Esta é uma versão de testes. Para sua segurança, NÃO utilize a mesma
				senha da versão de PRODUÇÃO.</div>
		</c:if>

		<h1 class="gt-form-head">${param.titulo}</h1>	

		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Receber notificação por email</h5>
			</div>

			<div class="card-body">
				<form name="frm" id="rec_notificacao_por_email" action="rec_notificacao_por_email" method="GET">
					<input type="hidden" name="page" value="1" />
					<siga-mensagem:sucesso texto="${mensagem}"></siga-mensagem:sucesso>		
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								 <div class="table-responsive">
								    <table border="0" class="table table-sm table-striped">
								      <thead class="thead-dark">
									      <tr>
									        <th style="width: 70%;">Ações</th>
									        <th style="text-align: center;">Não configurável</th>
									        <th style="text-align: center;">Configurável</th>
									      </tr> 
								      </thead>
								      
								      <tbody>
								      	<c:forEach items="${itens}" var="email">
										  <tr>
										      <td style="width: 70%;">${email.nomeDaAcao}</td>
										      
										      	<c:url var="url" value="/app/notificarPorEmail/editar">
													<c:param name="id" value="${email.id}"></c:param>
												</c:url>
												<c:url var="urlHabilitaDesabilitaConfiguravel" value="/app/notificarPorEmail/rec_notificacao_por_email2">
													<c:param name="id" value="${email.id}"></c:param>
												</c:url>
												
												 <c:choose>
													<c:when test="${!email.isConfiguravel()}">
														<c:choose>
															<c:when test="${email.restringir()}">
															  	<td style="text-align: center;" title="Este item não pode ser alterado pelo usuário">
																	<div class="form-check form-check-inline">
																		<a href="return false" class="disabled"  aria-pressed="true"><i class="fas fa-check-square"></i></a>
																	</div>
																</td>
															</c:when>
															<c:otherwise> 
																<td style="text-align: center;">
																	<div class="form-check form-check-inline">
																		<a href="javascript:submitPost('${urlHabilitaDesabilitaConfiguravel}')" aria-pressed="true"><i class="fas fa-check-square"></i></a>
																	</div>
																</td>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${email.restringir()}">
																<td style="text-align: center;" title="Este item não pode ser alterado pelo usuário">
																	<div class="form-check form-check-inline">
																		<a href="return false" class="disabled" aria-pressed="true"><i class="far fa-square"></i></a>
																	</div>
																</td>
															</c:when>
															<c:otherwise>
																<td style="text-align: center;">
																	<div class="form-check form-check-inline">
																		<a href="javascript:submitPost('${urlHabilitaDesabilitaConfiguravel}')" aria-pressed="true"><i class="far fa-square"></i></a>
																	</div>
																</td>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
													</c:choose>
										        
											        <c:choose>
													<c:when test="${!email.notConfiguravel()}">
														<c:choose>
															<c:when test="${email.restringir()}">
																	<td style="text-align: center;" title="Este item não pode ser alterado pelo usuário">
																		<div class="form-check form-check-inline">
																			<a href="return false" class="disabled" aria-pressed="true"><i class="fas fa-check-square"></i></a>
																		</div>
																	</td>
															</c:when>
															<c:otherwise>
																<td style="text-align: center;">
																	<div class="form-check form-check-inline">
																			<a href="javascript:submitPost('${urlHabilitaDesabilitaConfiguravel}')" aria-pressed="true"><i class="fas fa-check-square"></i></a>
																		</div>
																</td>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${email.restringir()}">
																<td style="text-align: center;" title="Este item não pode ser alterado pelo usuário">
																	<div class="form-check form-check-inline">
																		<a href="return false" class="disabled" aria-pressed="true"><i class="far fa-square"></i></a>
																	</div>
																</td>
															</c:when>
															<c:otherwise>
																<td style="text-align: center;">
																	<div class="form-check form-check-inline">
																		<a href="javascript:submitPost('${urlHabilitaDesabilitaConfiguravel}')" aria-pressed="true"><i class="far fa-square"></i></a>
																	</div>
																</td>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
													</c:choose>
													
										    	</tr>
									      	</c:forEach>
								      </tbody>
								    </table>
									  
								  </div>
							</div>
						</div>
						
					</div>
				</form>	
			</div>
		</div>		
	</div>
	
	<script type="text/javascript">
	$(document).ready(function() {	
		if ('${mensagemPesquisa}'.length > 0) $('.mensagem-pesquisa').css({'display':'block'});
	});
	
	function csv(id, action) {
		var frm = document.getElementById(id);
		frm.method = "POST";
		sbmtAction(id, action);
		
		$('.mensagem-pesquisa').alert('close');
		
		frm.action = 'rec_notificacao_por_email';
		frm.method = "GET";
	}
	
	function sbmtAction(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}
	
	function submitPost(url) {
		var frm = document.getElementById('rec_notificacao_por_email');
		frm.method = "POST";
		sbmtAction('rec_notificacao_por_email',url);
	}
	
	function atualizarUrl(url, msg){
		$('.btn-confirmacao').attr("href", url);
		document.getElementById("msg").innerHTML = msg;
	}
	
	$('.disabled').click(function(e){
    	e.preventDefault();
	});
	
</script>
	
</siga:pagina>
