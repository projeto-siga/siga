<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Listar Lota&ccedil;&atilde;o">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css"
		type="text/css" media="screen, projection" />
	<link rel="stylesheet"
		href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
		media="screen, projection" />
	<script type="text/javascript" language="Javascript1.1">
		
	</script>
	<!-- main content -->
	<div class="container-fluid">
		<form name="frm" action="unidade" id="unidade" class="form100"
			method="POST">
			<input type="hidden" name="paramoffset" value="0" /> <input
				type="hidden" name="p.offset" value="0" />
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>
						Gerenciar minhas
						<fmt:message key="usuario.lotacao" />
					</h5>
				</div>

			</div>

			<h3 class="gt-table-head">
				<fmt:message key="usuario.lotacoes" />
				cadastradas
			</h3>
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color}">
					<tr>
						<th align="left" style="width: 30%">Órgão</th>
						<th align="left" style="width: 30%">Unidade</th>
						<th align="left" style="width: 30%">Padrão</th>
					</tr>
				</thead>

				<tbody>
					<siga:paginador maxItens="15" maxIndices="10" itens="${itens}"
						var="identidade">
						<tr>
							<td align="left" style="width: 30%">${identidade.cpOrgaoUsuario.nmOrgaoUsu}</td>
							<td align="left" style="width: 30%">${identidade.dpPessoa.lotacao.nomeLotacao}</td>
							<td align="left" style="width: 30%">
							<label>
								<input style="align-content: center" type="radio" name="usuario" id="usuarioPadrao" 
									value="${identidade.dpPessoa.idPessoa}"
										${identidade.dpPessoa.usuarioPadrao == 1 ? 'checked' : ''}/>
							 </label></td>
						</tr>
					</siga:paginador>
				</tbody>
			</table>
			<div class="gt-table-buttons">
				<input type="button" value="Ok" id="btnOk"
					onclick="trocarUsuario()"
					class="btn btn-primary" /> 
				<input type="button" value="Cancelar"
					onclick="javascript:history.back()" class="btn btn-primary" />
			</div>
		</form>
	</div>

	<script type="text/javascript">

		function trocarUsuario(){
			var troca = $("input[name='usuario']:checked").val();
			$.ajax({
				url: '/siga/app/substituicao/unidade',
				type: 'POST',
				data: {id: troca}
			})
			.done(function (response, status, jqXHR){
		            document.location.reload(true);
			})
			.fail(function (jqXHR, errorThrown){
		            console.error("Ocorreu um erro ao trocar o usuário: " + errorThrown)
			});
		}
			
	</script>
</siga:pagina>
