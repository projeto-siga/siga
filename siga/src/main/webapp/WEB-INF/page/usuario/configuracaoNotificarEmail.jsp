<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mensagem" prefix="siga-mensagem"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<siga:pagina popup="false" titulo="Receber notificação por email">
	<script type="text/javascript" src="/siga/javascript/jquery.blockUI.js"></script>

	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header"> 
				<h5 id="et">Receber notificação por e-mail</h5>
			</div> <!-- card-header -->
			<div class="card-body"> 
				<form name="frm" id="listar" action="listar" method="GET">
					<div class="row">
						<div class="col-sm">
							<div class="form-group">  
								<div class="table-responsive">
								    <table border="0" class="table table-sm table-striped">
								      <thead class="thead-dark">
									      <tr>
									        <th style="width: 70%;">Ações</th>
									        <th>Receber</th>
									      </tr>  
								      </thead><!-- thead-dark -->
								      <tbody>
								      	<c:forEach items="${ITENS}" var="conf" >
										  <tr> 
										  	<c:url var="ativar" value="editar">   
												<c:param name="siglaServ" value="${conf.cpServico.siglaServico}"></c:param>
												<c:param name="idSituacao" value="1"></c:param>
											</c:url> 
											<c:url var="inativar" value="editar">
												<c:param name="siglaServ" value="${conf.cpServico.siglaServico}"></c:param>
												<c:param name="idSituacao" value="2"></c:param>
											</c:url> 
											<c:if test="${conf.cpServico.siglaServico != SERV_PAI}"> 
												<td style="width: 70%;">
										      		${conf.cpServico.dscServico}  
										      		<a
														class="fas fa-info-circle text-secondary ml-1  ${hide_only_TRF2}"
														data-toggle="tooltip" data-trigger="click"
														data-placement="bottom"
														title="${conf.cpServico.labelServico} ">
										    	</td> 
											</c:if>
										    <c:choose>   
											    <c:when test="${conf.cpSituacaoConfiguracao.getId() == PODE and conf.cpServico.siglaServico != SERV_PAI}">
												   <td> 
												   	  <button class="btn btn-primary btnAcao" style="min-width: 100px;" type="button" onclick="javascript:submitPost('${inativar}')">Inativar</button>
												   </td> 
										        </c:when>   
									        </c:choose> 
									        <c:choose>  
									          	<c:when test="${conf.cpSituacaoConfiguracao.getId() == NAO_PODE and conf.cpServico.siglaServico != SERV_PAI}">  
												   <td>  				        			
										              <button class="btn btn-danger btnAcao" style="min-width: 100px;" type="button" onclick="javascript:submitPost('${ativar}')">Ativar</button>
										           </td>
										        </c:when> 
									        </c:choose>
									        <c:choose>   
									            <c:when test="${conf.cpSituacaoConfiguracao.getId() == RECB_OBRIGATORIO and conf.cpServico.siglaServico != SERV_PAI}">
												   <td>
												      <p style="font-size: 12px; color: grey;">Recebimento obrigatório</p>  
												   </td> 
										        </c:when>  
									        </c:choose>
										    	</tr><!-- tr -->
									      	</c:forEach>
								      	</tbody><!-- body -->
								    </table><!-- table -->
								</div><!-- table-responsive -->
							</div><!-- form-group -->
						</div><!-- col-sm -->
					</div><!-- row -->
				</form><!-- form -->
			</div><!-- card-body -->
		</div><!-- card bg-light mb-3 -->	
	</div><!-- container-fluid -->
	<script type="text/javascript">
		$(document).ready(function() {	
			if ('${mensagemPesquisa}'.length > 0) $('.mensagem-pesquisa').css({'display':'block'});
		});
		
		$(function () {
			  $('[data-toggle="tooltip"]').tooltip()
		})
		
		function sbmtAction(codigo, action) {
			var frm = document.getElementById(codigo);
			frm.action = action;
			frm.submit();
			return ;
		}
		function submitPost(url) {
			$(".btnAcao").attr("disabled", true);
			var spinner = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>Aguarde'
			$(".btnAcao").html(spinner);
			setTimeout(function () {
				var frm = document.getElementById('listar');    
				frm.method = "POST";
				sbmtAction('listar',url);
			}, 1000)
		}
		
	</script>
</siga:pagina> 