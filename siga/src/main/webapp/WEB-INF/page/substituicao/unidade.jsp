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
						<th align="left" style="width: 0%">Padrão</th>
					</tr>
				</thead>

				<tbody>
					<siga:paginador maxItens="15" maxIndices="10"
						 itens="${itens}" var="identidade">
						<tr>
							<td align="left" style="width: 30%">${identidade.cpOrgaoUsuario.nmOrgaoUsu}</td>
							<td align="left" style="width: 30%">${identidade.dpPessoa.lotacao.nomeLotacao}</td>
							<td align="left" style="width: 30%">
								<label> 
									<input type="radio" name="usuario" id="usuarioPadrao" value="true" checked
										${(identidade.dpPessoa.usuarioPadrao.equals(1)) ? 'checked' : ''}
									/>
								</label>
							</td>
						</tr>
					</siga:paginador>
				</tbody>
			</table>
			<div class="gt-table-buttons">
				<input type="submit" value="Ok"
					value="${linkTo[SubstituicaoController].trocarUsuarioPadrao()}" class="btn btn-primary">
				<input type="button" value="Cancelar"
					onclick="javascript:history.back();"
					class="btn btn-primary">
			</div>
		</form>
	</div>

	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			if ('${mensagemPesquisa}'.length > 0)
				$('.mensagem-pesquisa').css({
					'display' : 'block'
				});
		});

		function csv(id, action) {
			var frm = document.getElementById(id);
			frm.method = "POST";
			sbmtAction(id, action);

			$('.mensagem-pesquisa').alert('close');

			frm.action = 'listar';
			frm.method = "GET";
		}

		function sbmtAction(id, action) {
			var frm = document.getElementById(id);
			frm.action = action;
			frm.submit();
			return;
		}
	</script>
</siga:pagina>
