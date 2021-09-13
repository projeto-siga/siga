<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Ações">

	<jsp:include page="../main.jsp"></jsp:include>

	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>

	<div class="container-fluid mb-2">
		<h2>A&ccedil;&otilde;es</h2>
		
		<div class="card card-body mb-2">
			<label>
				<siga:checkbox name="mostrarDesativados" value="${mostrarDesativados}"></siga:checkbox>
				<b>Incluir Inativas</b>
			</label>
			<div class="table-responsive">
				<table id="acoes_table" class="gt-table display">
					<thead>
						<tr>
							<th>C&oacute;digo</th>
							<th>T&iacute;tulo</th>
							<th>Descri&ccedil;&atilde;o</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${acoes}" var="acao">
							<tr data-json-id="${acao.idAcao}" data-json='${acao.toJson()}' onclick="acaoService.editar($(this).data('json'), 'Alterar A&ccedil;&atilde;o')" style="cursor: pointer;">
								<td>${acao.siglaAcao}</td>
								<td>
									<span style="margin-left: ${(acao.nivel-1)*2}em; <c:if test="${acao.nivel == 1}">font-weight: bold;</c:if>">
										<siga:selecionado sigla="${acao.atual.tituloAcao}" descricao="${acao.atual.descricao}"></siga:selecionado>
									</span>
								</td>
								<td>${acao.descrAcao}</td>
								<td class="acoes">
									<sigasr:desativarReativar id="${acao.idAcao}" onReativar="acaoService.reativar" onDesativar="acaoService.desativar" isAtivo="${acao.isAtivo()}"></sigasr:desativarReativar>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<!-- Incluir -->
		<a onclick="acaoService.cadastrar('Incluir A&ccedil;&atilde;o')" class="btn btn-primary" style="color: #fff">Incluir</a>
		
		
	</div>
	<sigasr:modal nome="acao" titulo="Cadastrar A&ccedil;&atilde;o" largura="80%">
		<div id="divEditarAcaoForm"><jsp:include page="editar.jsp"></jsp:include></div>
	</sigasr:modal>

</siga:pagina>

<script type="text/javascript">
	var validatorAcaoForm;
	var colunasAcao = {
			codigo: 0,
			titulo: 1,
			descrAcao: 2,
			acoes: 3
		};
	var QueryString = function () {
		// This function is anonymous, is executed immediately and
		// the return value is assigned to QueryString!
		var query_string = {};
		var query = window.location.search.substring(1);
		var vars = query.split("&");
		for (var i=0;i<vars.length;i++) {
			var pair = vars[i].split("=");
	    	// If first entry with this name
	    	if (typeof query_string[pair[0]] === "undefined") {
				query_string[pair[0]] = pair[1];
				// If second entry with this name
			} else if (typeof query_string[pair[0]] === "string") {
				var arr = [ query_string[pair[0]], pair[1] ];
				query_string[pair[0]] = arr;
				// If third or later entry with this name
			} else {
				query_string[pair[0]].push(pair[1]);
			}
		}
		return query_string;
	}();

	var opts = {
			 urlDesativar : "${linkTo[AcaoController].desativar}",
			 urlReativar : "${linkTo[AcaoController].reativar}",
			 urlGravar : "${linkTo[AcaoController].gravar}",
			 dialogCadastro : $('#acao_dialog'),
			 tabelaRegistros : $('#acoes_table'),
			 objectName : 'acao',
			 formCadastro : $('#acaoForm'),
			 mostrarDesativados : $('#checkmostrarDesativados').attr('checked') ? true : false, //QueryString.mostrarDesativados,
			 colunas : colunasAcao.acoes
	};
	
	$(document).ready(function() {
		if (QueryString.mostrarDesativados != undefined) {
			document.getElementById('checkmostrarDesativados').checked = QueryString.mostrarDesativados == 'true';
			document.getElementById('checkmostrarDesativados').value = QueryString.mostrarDesativados == 'true';
		}
			
		$("#checkmostrarDesativados").click(function() {
			jQuery.blockUI(objBlock);
			if (document.getElementById('checkmostrarDesativados').checked)
				location.href = "${linkTo[AcaoController].listar}"  + "?mostrarDesativados=true";
			else
				location.href = "${linkTo[AcaoController].listar}"  + "?mostrarDesativados=false";
		});
		
		/* Table initialization */
		opts.dataTable = $('#acoes_table').dataTable({
			stateSave : true,
			"language": {
				"emptyTable":     "N&aacute;o existem resultados",
			    "info":           "Mostrando de _START_ a _END_ do total de _TOTAL_ registros",
			    "infoEmpty":      "Mostrando de 0 a 0 do total de 0 registros",
			    "infoFiltered":   "(filtrando do total de _MAX_ registros)",
			    "infoPostFix":    "",
			    "thousands":      ".",
			    "lengthMenu":     "Mostrar _MENU_ registros",
			    "loadingRecords": "Carregando...",
			    "processing":     "Processando...",
			    "search":         "Filtrar:",
			    "zeroRecords":    "Nenhum registro encontrado",
			    "paginate": {
			        "first":      "Primeiro",
			        "last":       "&Uacute;timo",
			        "next":       "Pr&oacute;ximo",
			        "previous":   "Anterior"
			    },
			    "aria": {
			        "sortAscending":  ": clique para ordena&ccedil;&atilde;o crescente",
			        "sortDescending": ": clique para ordena&ccedil;&atilde;o decrescente"
			    }
			},
			"columnDefs": [{
				"targets": [colunasAcao.acoes],
				"searchable": false,
				"sortable" : false
			}],
			"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
				var acao = undefined;
				
				try {
					acao = JSON.parse($(nRow).data('json'));
				}
				catch(err) {
					acao = $(nRow).data('json');
				}
				
				if (acao) {
					if (acao.ativo == false)
						$('td', nRow).addClass('item-desativado');
					else
						$('td', nRow).removeClass('item-desativado');
				}
			}
		});
	});

	// Define a "classe" AcaoService
	function AcaoService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// AcaoService extends BaseService
	AcaoService.prototype = Object.create(BaseService.prototype);
	
	var acaoService = new AcaoService(opts);
	
	acaoService.getId = function(acao) {
		return acao.id || acao['acao.idAcao'];
	}

	acaoService.editar = function(obj, title) {
		acaoService.limparTipoAcao();
		BaseService.prototype.editar.call(this, obj, title);
	}

	acaoService.cadastrar = function(title) {
        acaoService.limparTipoAcao();
        BaseService.prototype.cadastrar.call(this, title);
	}

	acaoService.limparTipoAcao = function() {
	    $("#formulario_tipoAcaoSel_id").val('');
	    $("#formulario_tipoAcaoSel_descricao").val('');
	    $("#formulario_tipoAcaoSel_sigla").val('');
	    $("#tipoAcaoSelSpan").html('');

	}

	acaoService.serializar = function(obj) {
        var query = BaseService.prototype.serializar.call(this, obj);
        return query + "&acao=" + this.getId(obj);
    }

	acaoService.getRow = function(acao) {
		var marginLeft = (acao.nivel-1) * 2,
			fontWeight = (acao.nivel == 1) ? 'bold' : 'normal',
			span = $('<span></span>');

		var spanHTML = '<span style="margin-left:{margin-left};font-weight:{font-weight}">{descricao}</span>';
		spanHTML = spanHTML.replace('{margin-left}', marginLeft + 'em');
		spanHTML = spanHTML.replace('{font-weight}', fontWeight);
		spanHTML = spanHTML.replace('{descricao}', acao.tituloAcao);
		
		return [acao.sigla, spanHTML, acao.descrAcao, 'COLUNA_ACOES'];
	}
	acaoService.onRowClick = function(acao) {
		acaoService.editar(acao, 'Alterar A&ccedil;&atilde;o');
	}

</script>