<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<siga:pagina titulo="Relatório de Permissão de Usuários">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Relatório de Permissão de Usuários</h2>
			<div class="gt-content-box gt-for-table">
				<form method="get" action="javascript:submeter()">
					<table class="gt-form-table">
						<tr class="">
							<td><label>Matrícula: </label>
							</td>
							<td><siga:selecao tipo="pessoa" tema="simple"
									propriedade="pessoa" modulo="siga"/>
							</td>
						</tr>
						<tr class="">
							<td colspan="2"><input class="gt-btn-medium gt-btn-left"
								type="submit" value="Gerar..." />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript">
	function submeter() {
		var t_strIdPessoa = document.getElementsByName("pessoa_pessoaSel.id")[0];
		if (t_strIdPessoa) {
			if (t_strIdPessoa.value) {
				location.href = 'emitir_permissao_usuario?idPessoa=' + t_strIdPessoa.value;
			} else {
				alert("Por favor, é necessário preencher o campo pessoa!");
			}
		}
	}
</script>
