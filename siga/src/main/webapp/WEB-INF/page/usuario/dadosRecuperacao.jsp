<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.15/jquery.mask.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$( ".celular" ).mask('(99) 99999-9999');
	});
</script>

<c:choose>
	<c:when test="${empty mensagem_erro}">
		<div class="container-fluid">
		<div class="card bg-light mb-3" >

			<div class="card-header">
				<h5>${param.titulo}</h5>
				<h2 class="gt-form-head">Alteração de dados de recuperação</h2>
			</div>

			<div class="card-body">
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<div id="msgInfo" style="font-size: 10pt; color: red;">
								As informações abaixo poderão ser utilizadas APENAS na recuperação de sua senha do sistema, em caso de esquecimento ou perda.
								<br/>Por razões de segurança, lembre-se de manter estes dados sempre atualizados! 
							</div>
						</div>
					</div>
				</div>
				
				<form action="dados_recuperacao_gravar" method="post">
				
				<input type="hidden" name="page" value="1" />
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<h5>${mensagem } ${mensagemEmail }</h5>
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label for="usuario.senhaAtual" class="col-sm-2x col-form-labelx">
								Senha atual
								<a href="#"title="Para alterar seus dados pessoais, é necessário informar sua senha.">
									<img class="botaoDica" src="/siga/css/famfamfam/icons/information.png" /> 
								</a>
							</label>
							<br />
							<input type="password" name="usuario.senhaAtual" class="gt-form-text" />
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label for="usuario.emailRecuperacao" class="col-sm-2x col-form-labelx">
								E-mail de recuperação
								<a href="#" title="Informe um e-mail pessoal. NÃO cadastre seu próprio e-mail institucional!">
									<img class="botaoDica" src="/siga/css/famfamfam/icons/information.png" /> 
								</a>
							</label>
							<br />
							<input type="text" name="usuario.emailRecuperacao" value="${emailRecup}" class="gt-form-text" />
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<label for="usuario.celularRecuperacao" class="col-sm-2x col-form-labelx">
								Celular de recuperação
								<a href="#" title="Digite o código DDD e os 9 dígitos de seu celular pessoal; apenas números.">
									<img class="botaoDica" src="/siga/css/famfamfam/icons/information.png" /> 
								</a>
							</label>
							<br />
							<input type="text" name="usuario.celularRecuperacao" value="${celularRecup}" class="gt-form-text celular" />
						</div>
					</div>
				</div>


				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<siga:monobotao inputType="submit" value="Ok" cssClass="btn btn-primary" />
						</div>
					</div>
				</div>
				</form>
			</div>
	</c:when>
	<c:otherwise>
				<br/>
				<br/>
				<br/>
		<h2 class="gt-form-head">${mensagem_erro }</h2>
	</c:otherwise>
</c:choose>

</siga:pagina>

