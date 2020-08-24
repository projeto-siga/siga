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

<script type="text/javascript" language="Javascript1.1">
	/*  converte para maiúscula a sigla do estado  */
	function converteUsuario(nomeusuario) {
		nomeusuario.value = nomeusuario.value.toUpperCase();
	}

	function refreshCheckboxValue(checkbox) {
		checkbox.value = checkbox.checked;
	}
</script>
<siga:pagina popup="false" titulo="Troca de Email">
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
				<h5>Trocar email</h5>
			</div>

			<div class="card-body">
				<form action="trocar_email_gravar" method="post" >
					<input type="hidden" name="page" value="1" />
					<siga-mensagem:sucesso texto="${mensagem}"></siga-mensagem:sucesso>					
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Sua matrícula: <b>${matricula}</b> - Email Atual:
									<b>${email}</b></label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Novo endereço de email</label>
								<input type="text"
									name="usuario.emailNovo" id="pass"
									onkeyup="passwordStrength(this.value)" theme="simple"
									class="form-control"
									oncopy="return false" onpaste="return false"/>
							</div>
						</div>
						<div class="col-sm">
							<div class="form-group">
								<label>Repetição do novo email</label>
								<input type="text"
									name="usuario.emailConfirma"
									id="pass2"
									class="form-control"
									oncopy="return false" onpaste="return false"/>
							</div>
						</div>
					</div>
					<c:if test="${variosPerfis}">
						<h3 class="gt-table-head">Foram localizados os registros abaixo.</h3>
						<div class="table-responsive">
							<table border="0" class="table table-sm table-striped">
								<thead class="thead-dark">
									<tr>
										<th align="left">Nome</th>
										<th align="left">Orgão</th>
										<th align="left">Unidade</th>
										<th align="left">CPF</th>
										<th align="left">Email</th>
										<th align="left">Matricula</th>
									</tr>
								</thead>
								<tbody>
									<siga:paginador maxItens="15" maxIndices="10"
										totalItens="${tamanho}" itens="${usuarios}" var="pessoa">
										<tr>
											<td align="left">${pessoa.nome}</td>
											<td align="left">${pessoa.orgao}</td>
											<td align="left">${pessoa.lotacao}</td>
											<td align="left">${pessoa.cpf}</td>
											<td align="left">${pessoa.email}</td>
											<td align="left">${pessoa.matricula}</td>
										</tr>
									</siga:paginador>
								</tbody>
							</table>
						</div>
						<br />
						<div class="row">
							<div class="col-sm-2">
								<input type="checkbox" name="usuario.teste" value="TRUE">Deseja aplicar o novo e-mail para todos os registros? </input>
							</div>
						</div>
						<br />
					</c:if>
					<div class="row">
						<div class="col-sm-1">
							<button type="submit" class="btn btn-primary">OK</button>
						</div>
					</div>
				</form>
				<br />				
			</div>
		</div>		
	</div>
</siga:pagina>

