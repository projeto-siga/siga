<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<ww:url id="urlEmitir" action="emitir_permissao_usuario"
	namespace="/gi/relatorio" />

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
									propriedade="pessoa" />
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
		var t_strIdPessoa = document.getElementById("pessoa_pessoaSel_id");
		if (t_strIdPessoa) {
			if (t_strIdPessoa.value) {
				location.href = '${urlEmitir}?idPessoa=' + t_strIdPessoa.value;
			} else {
				alert("Por favor, é necessário preencher o campo pessoa!");
			}
		}
	}
</script>
