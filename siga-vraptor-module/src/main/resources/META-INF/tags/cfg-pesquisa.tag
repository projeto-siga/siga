<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista Configurações">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css"
		type="text/css" media="screen, projection" />
	<link rel="stylesheet"
		href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
		media="screen, projection" />

	<script type="text/javascript">
		function sbmt(id, action) {
			document.getElementById('myfrm').submit();
			return;
		}

		function montaTableCadastradas() {
			var idTpConf = $('#idTpConfiguracao').val();
			var idOrgao = $('#idOrgaoUsu').val();
			$('#tableCadastradas').html('Carregando...');
			$.ajax({
				url : 'listar_cadastradas',
				type : "GET",
				data : {
					idTpConfiguracao : idTpConf,
					idOrgaoUsu : idOrgao
				},
				success : function(data) {
					$('#tableCadastradas').html(data);
				},
				error : function(data) {
					$('#tableCadastradas').html(data.responseText);
				}
			});
		}
	</script>

	<div class="container-fluid">
		<div class="card bg-light mb-3">

			<div class="card-header">
				<h5>Configurações Cadastradas</h5>
			</div>

			<div class="card-body">
				<input type="hidden" name="postback" value="1" />
				<form id="myfrm" name="frm">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Tipo de Configuração</label>
								<siga:select value="${tipoDeConfiguracao.id}"
									name="idTpConfiguracao" list="listaTiposConfiguracao"
									listKey="id" id="idTpConfiguracao" listValue="descr"
									theme="simple" onchange="javascript:sbmt();" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Órgão</label>
								<siga:select value="${param.idOrgaoUsu}" name="idOrgaoUsu"
									id="idOrgaoUsu" list="orgaosUsu" listKey="idOrgaoUsu"
									listValue="nmOrgaoUsu" theme="simple" headerValue="[Todos]"
									headerKey="0" onchange="javascript:sbmt();" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<p>${tipoDeConfiguracao.explicacao}</p>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<input type="button" value="Incluir Configuração"
								class="btn btn-primary"
								onclick="javascript:window.location.href='editar?idTpConfiguracao=${tipoDeConfiguracao.id}'" />
						</div>
					</div>
				</form>

			</div>

		</div>
		<div id="tableCadastradas"></div>
	</div>

	<script>
		$(document).ready(function() {
			$('[name=idTpConfiguracao]').addClass('siga-select2');
			$('[name=idOrgaoUsu]').addClass('siga-select2');
			montaTableCadastradas();
		});
	</script>
	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>
