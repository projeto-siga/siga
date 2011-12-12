<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<ww:url id="urlEmitir" action="emitir_permissao_usuario" namespace="/gi/relatorio" />
 
<siga:pagina titulo="Relatório de Permissão de Usuários">
	
	<h1>Relatório de Permissão de Usuários</h1>
		<br/>
		<form method="get" action="javascript:submeter()">
			<table class="form100">
				   <tr  class="">
				   		<td>
							<label>Matrícula: </label>
						</td>
						<td>
							<siga:selecao tipo="pessoa" tema="simple"
								propriedade="pessoa" />
						</td>
					</tr>
					<tr class="">
						<td>
							<input type="submit" value="Gerar..."/>
						</td>
						<td>
						</td>
					</tr>
			</table>
		</form>
		<br/>
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
</script>"
