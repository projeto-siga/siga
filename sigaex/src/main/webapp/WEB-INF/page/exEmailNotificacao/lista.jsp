<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Lista Emails Notificação">
	<!-- main content -->
	
	<script type="text/javascript">
	function validar() {
		var descricao = $('#dscFeriado').val();		
		if (descricao==null || descricao=="") {			
			alert("Preencha a descricao do feriado");
			document.getElementById('dscFeriado').focus();		
		}else 
			frm.submit();					
	}
	</script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
				
			<h2 class="gt-table-head">Emails de notificação cadastrados</h2>
			<c:choose>
				<c:when test="${not empty itens}">
					<div class="gt-content-box gt-for-table">
						<table border="0" class="gt-table">
							<thead>
								<tr> 
									<th style="text-align: center;" colspan="2">Destinatário da Movimentação</th>
									<th rowspan="2" width="8%"></th>
									<th style="text-align: center;" colspan="3">Enviar notificação para:</th>							
								</tr>
								<tr>							
									<th align="left" width="15%">Pessoa</th>	
									<th align="right" width="15%">Lotacao</th>							
									<th align="right" width="25%">Email</th>	
									<th align="right" width="15%">Pessoa</th>
									<th align="right" width="15%">Lotação</th>
									<th colspan=2> </th>											
								</tr> 
							</thead>
							
							<tbody>
								<c:forEach var="email" items="${itens}">
									<tr>																		
										<td align="left"><c:if test="${not empty email.dpPessoa}">
											<siga:selecionado sigla="${email.dpPessoa.pessoaAtual.nomeAbreviado}"
													descricao="${email.dpPessoa.pessoaAtual.descricao} - ${email.dpPessoa.pessoaAtual.sigla}" /></c:if>
										</td>
										<td><c:if test="${not empty email.dpLotacao}">
											<siga:selecionado sigla="${email.dpLotacao.lotacaoAtual.sigla}"
												descricao="${email.dpLotacao.lotacaoAtual.descricaoAmpliada}" /></c:if>
										</td>	
										<td></td>
										<td>${email.email}</td>	
										<td><c:if test="${not empty email.pessoaEmail}">
											<siga:selecionado sigla="${email.pessoaEmail.pessoaAtual.nomeAbreviado}"
													descricao="${email.pessoaEmail.pessoaAtual.descricao} - ${email.pessoaEmail.pessoaAtual.sigla}" /></c:if>
										</td>
										<td><c:if test="${not empty email.lotacaoEmail}">
											<siga:selecionado sigla="${email.lotacaoEmail.lotacaoAtual.sigla}"
												descricao="${email.lotacaoEmail.lotacaoAtual.descricaoAmpliada}" /></c:if>
										</td>								
										<td align="center" width="5%">									
			 			 					<a href="javascript:if (confirm('Deseja excluir o email?')) location.href='/sigaex/app/expediente/emailNotificacao/excluir?id=${email.idEmailNotificacao}';">
													<img style="display: inline;"
													src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir email"							
													onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
													onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
											</a>															
										</td>																			 							
									</tr>
								</c:forEach>
							</tbody>
						</table>								
					</div>	
				</c:when>
				<c:otherwise>
					<b>Não há emails cadastrados</b>
				</c:otherwise>
			</c:choose>
			
			<div class="gt-table-buttons">
				<c:url var="url" value="editar" />
				<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="gt-btn-medium gt-btn-left">
			</div>	
			
		</div>		
	</div>		
</siga:pagina>
