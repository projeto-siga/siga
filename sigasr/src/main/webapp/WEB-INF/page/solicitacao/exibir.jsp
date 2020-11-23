<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="${solicitacao.codigo}">
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/siga/javascript/siga.js"></script>

	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/cronometro.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/atributo.editavel.js"></script>

	<style>
		ul.lista-historico li span {
			text-decoration: line-through;
		}
		
		ul.lista-historico li {
			list-style: none;
		}
		
		ul.lista-historico li.unico {
			margin-left: 0px !important;
		}
		
		button.button-historico {
			padding-left: 2px;
			padding-right: 2px;
			width: 16px;
		}
		
		.historico-label {
			font-weight: bold;
			margin-right: 4px;
		}
		
		.hidden {
			display: none;
		}	
	</style>


	<div class="container-fluid mb-2">
		<div class="row">
			<div class="col-sm-12">
				<jsp:include page="exibirIncludeCabecalho.jsp"/>
			</div>
		</div>
			
		<div class="row">
			<div class="col-sm-8">
				<div class="card card-body mb-1">
					<jsp:include page="exibirIncludeDescricao.jsp"/>
				</div>
				
				<div class="card card-body  mb-1">
					<jsp:include page="exibirIncludeConteudoPrincipal.jsp"/>
				</div>
			
				<c:set var="exibirEtapas" value="${solicitacao.jaFoiAtendidaPor(titular, lotaTitular, true) || exibirMenuAdministrar}" />
				
				<div class="card card-body mb-3">
					<jsp:include page="exibirIncludeDetalhes.jsp">
						 <jsp:param name="exibirEtapas" value="${exibirEtapas}"/>
					</jsp:include>
					<c:if test="${exibirEtapas}">
						<jsp:include page="exibirIncludeEtapas.jsp"/>
					</c:if>
				</div>
			</div>
					
			<!-- Side-bar -->
			<div class="col-sm-4">
				<jsp:include page="exibirCronometro.jsp"></jsp:include>
				<jsp:include page="exibirPendencias.jsp"></jsp:include>
				<jsp:include page="exibirIncludeSidebarSolicitacao.jsp"></jsp:include>
				<jsp:include page="exibirIncludeSidebarOutros.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<jsp:include page="exibirIncludeModal.jsp"/>
</siga:pagina>



<script language="javascript">	
	function terminarPendencia(idMov) {
		$("#movimentacaoId").val(idMov);
		$("#terminarPendenciaModal_dialog").dialog({
			autoOpen: false,
	    	height: 'auto',
		    width: '80%',
		    modal: true,
		    resizable: false
		})
		$("#terminarPendenciaModal_dialog").dialog("open");
	}

	function validarAssociacao(tipo) {
		$("#erroSolicitacao" + tipo).hide();
		$("#erroJustificativa" + tipo).hide();

		if ((tipo == 'Juncao' && $("#solicitacaoRecebeJuntadaSpan").html() == "")
				|| (tipo == 'Vinculo' && $("#solicitacaoRecebeVinculoSpan")
						.html() == "")) {
			$("#erroSolicitacao" + tipo).show();
			return false;
		}
		if ($("#justificativa" + tipo).val() == "") {
			$("#erroJustificativa" + tipo).show();
			return false;
		}
		return true;
	}

	function gravarAssociacao(tipo) {
		if (!block())
			return false;
		if (validarAssociacao(tipo)) {
			document.getElementById("formGravar" + tipo).submit();
		} else {
			unblock();
	    }
	}
	$('#checksolicitacao.fechadoAutomaticamente').change(
			function() {
				if (this.checked) {
					$('#checksolicitacao.fechadoAutomaticamente').prop('value',
							'true');
					return;
				}
				$('#checksolicitacao.fechadoAutomaticamente').prop('value',
						'false');
			});

	$(function() {
		$("#horario").mask("99:99");
		$("#horarioReplanejar").mask("99:99");
	});

	function postback() {
		var todoOContexto = ($("#todoOContexto").val() != null ? $(
				"#todoOContexto").val() : false);
		var ocultas = ($("#ocultas").val() != null ? $("#ocultas").val()
				: false);

		location.href = "${linkTo[SolicitacaoController].exibir(solicitacao.siglaCompacta)}?todoOContexto="
				+ todoOContexto + "&ocultas=" + ocultas;
	}

	$("#terminarPendenciaModal_dialog").dialog({
		autoOpen : false,
		height : 'auto',
		width : 'auto',
		modal : true,
		resizable : false
	});
		
	function editarAtributo(idAtributo, tipoAtributo, preDefinidoSet) {
		event.preventDefault();
		var valorAtributo = $(event.target).closest(".atributo-editavel").find(".valor-atributo").text();
		var propriedades = {
			id: idAtributo,
			valor: valorAtributo,
			tipo: tipoAtributo,
			valoresPreDefinidos: preDefinidoSet,
			elemento: event.target,
			nome: 'atributo.valorAtributoSolicitacao',
			urlDestino: '${linkTo[SolicitacaoController].gravarAtributo}'
		};
		var atributoSolicitacao = new AtributoEditavel(propriedades);
		atributoSolicitacao.editar();
	}

	function excluirAtributo(idAtributo, nome) {
		event.preventDefault();
		if (!confirm('Confirmar exclusão do atributo ' + nome))
			return;
		var propriedades = {
			elemento: event.target,
			urlDestino: '${linkTo[SolicitacaoController].excluirAtributo}?id=' + idAtributo
		};
		var atributoSolicitacao = new AtributoEditavel(propriedades);
		atributoSolicitacao.excluir();
	}
</script>
