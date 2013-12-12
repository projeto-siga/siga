<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>


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
			<div class="gt-content-box gt-for-table" style="width: 80% !important;">
				<table border="0" class="gt-table">
					<thead>
						<tr>							
							<th align="left">Pessoa</th>	
							<th align="right">Lotacao</th>
							<th align="right">Email</th>	
							<th align="right">Email de Pessoa</th>
							<th align="right">Email de Lotação</th>											
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="email" items="${itens}">
							<tr>							
								<ww:url id="url" action="listar" namespace="/feriado">
									<ww:param name="id">${email.idEmailNotificacao}</ww:param>
								</ww:url>												
								<td align="left"><c:if test="${not empty email.dpPessoa}">
									<siga:selecionado sigla="${email.dpPessoa.iniciais}"
											descricao="${email.dpPessoa.descricao}" /></c:if>
								</td>
								<td><c:if test="${not empty email.dplotacao}">
									<siga:selecionado sigla="${email.dplotacao.sigla}"
										descricao="${email.dplotacao.descricao}" /></c:if>
								</td>	
								<td>email.email</td>	
								<td><c:if test="${not empty email.pessoaEmail}">
									<siga:selecionado sigla="${email.pessoaEmail.iniciais}"
											descricao="${email.pessoaEmail.descricao}" /></c:if>
								</td>
								<td><c:if test="${not empty email.emailLotacao}">
									<siga:selecionado sigla="${email.emailLotacao.sigla}"
										descricao="${email.emailLotacao.descricao}" /></c:if>
								</td>																			 							
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</div>				
		</div>	
	</div>		
</siga:pagina>
