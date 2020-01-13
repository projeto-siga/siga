<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
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
</script>

<siga:pagina popup="false" titulo="Troca de Senha">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<c:if test="${baseTeste}">
				<div id="msgSenha"
					style="font-size: 12pt; color: red; font-weight: bold;">ATENÇÃO:
					Esta é uma versão de testes. Para sua segurança, NÃO utilize a
					mesma senha da versão de PRODUÇÃO.</div>
			</c:if>
			<h1 class="gt-form-head">${param.titulo}</h1>

			<h2>${mensagem}</h2>
			<h2 class="gt-form-head">Trocar senha</h2>
			<div class="gt-form gt-content-box tabela-senha">
				<form action="troca_senha_p2"
					onsubmit="return validateUsuarioForm(this);" method="post">
					<input type="hidden" name="page" value="1" />
					<h1>${mensagem }</h1>

					<table>
						<tr>
							<td><label>Matrícula<a href="#"
									title="Ex.:	XX99999, onde XX é a sigla do seu órgão (T2, RJ e ES) e 99999 é o número da sua matrícula."><img
										style="position: relative; margin-top: -3px; top: +3px; left: +3px; z-index: 0;"
										src="/siga/css/famfamfam/icons/information.png" /> </a>
							</label></td>
						</tr>

						<tr>

							<td><input type="text" name="usuario.nomeUsuario" value="${nomeUsuario }"
									onblur="javascript:converteUsuario(this)"
									class="gt-form-text" /></td>
						</tr>
						<tr>
							<td><button type="submit" class="gt-btn-medium gt-btn-left">OK</button></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>

