<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="${lista.nomeLista}">
	<jsp:include page="../main.jsp"></jsp:include>
	
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>		
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		<h2 id="tituloPagina"> ${lista.nomeLista}</h2>
		
			<p class="gt-table-action-list">
				<c:if test="${podeEditar}">
					<a href="#" onclick='javascript: editarLista(event, ${lista.toJson()})'> 
						<img src="/siga/css/famfamfam/icons/pencil.png" style="margin-right: 5px;">Editar
					</a>
				</c:if>
			</p>
			<sigasr:solicitacao solicitacaoListaVO="solicitacaoListaVO" filtro="filtro" modoExibicao="lista"></sigasr:solicitacao>
		</div>
		
		<!-- /content box -->
		<div class="gt-table-buttons">
			<input type="hidden" name="idLista" value="${lista.idLista}">
			<c:if test="${podePriorizar}">
				<input type="button" id="btn" value="Gravar" class="gt-btn-medium gt-btn-left" />
			</c:if>
			<a href="${linkTo[SolicitacaoController].listarLista(false)}" class="gt-btn-medium gt-btn-left">Cancelar</a>
		</div>
	</div>
	
	<sigasr:modal nome="editarLista" titulo="Editar Lista">
		<div id="divEditarLista"><jsp:include page="editarLista.jsp"></jsp:include></div>		
	</sigasr:modal>
	
	<!-- modal de posicao -->	
	<sigasr:modal nome="posicao" titulo="Posição de Solicitação na Lista">
		<div class="gt-form gt-content-box" style="width: 280px; height: 100px;">
			<form id="posicaoForm">
				<input id="idPrioridadePosicao" type="hidden" name="idSolicitacao" />
				
				<div id="numPosicao" class="gt-form-row gt-width-66">
					<label>Mover Para</label> 
					<input type="number" min="0" name="posicaoNaLista"/>
				</div>
				
				<div class="gt-form-row">
					<input type="button" value="Ok" class="gt-btn-medium gt-btn-left" 
						onclick="reposicionar()" />
					<a class="gt-btn-medium gt-btn-left" onclick="modalPosicaoFechar()">Cancelar</a>
				</div>
			</form>
		</div>	
	</sigasr:modal>
	
	<!-- modal de prioridade -->
	<sigasr:modal nome="prioridade" titulo="Alterar Prioridade">
		<div class="gt-form gt-content-box">
			<form id="prioridadeForm">
				<input id="idPrioridadePrior" type="hidden" name="idSolicitacao" />
				
				<div id="prioridade" class="gt-form-row gt-width-66">
					<label>Prioridade</label> 
					<td>
						<siga:select name="prioridadeNaLista" id="selectPrioridade" list="prioridadeList"
								listKey="idPrioridade" listValue="descPrioridade" theme="simple"
								headerValue="[Nenhuma]" headerKey="0" value="${solicitacao.prioridade.idPrioridade}" isEnum="true"/>
							 
					</td>					
				</div>
				<div id="naoReposicionarAutomatico" class="gt-width-250">
					<label>
						<siga:checkbox name="naoReposicionarAutomatico"
									   value="${naoReposicionarAutomatico}">
						</siga:checkbox> 
						<b>N&atilde;o reposicionar automaticamente</b>
					</label>
				</div>				
				
				<div class="gt-form-row">
					<input type="button" value="Ok" class="gt-btn-medium gt-btn-left" 
						onclick="gravarPrioridade()" />
					<a class="gt-btn-medium gt-btn-left" onclick="modalPrioridadeFechar()">Cancelar</a>					
				</div>
			</form>
		</div>
		
		<div id="jsonPrioridades" data-json='${jsonPrioridades}'></div>
		<form id="frm">
			<input type="hidden" name="filtro.idListaPrioridade" value="${filtro.idListaPrioridade}" />
			<input type="hidden" name="telaDeListas" value="true" />
		</form>
	</sigasr:modal>	
	
</siga:pagina>

<script type="text/javascript">
	var solicitacaoTable,
		listaJson,
		QueryString = {};

	$(function(){
	    $('#btn').click(function() {
	        var prioridades="";
	        var idLista = $('[name=idLista]').val();
	    	$("#sortable > tr").each(function(index) {
	    		var solicitacaoString = $(this).attr('data-json'),
	    			solicitacao = JSON.parse(solicitacaoString);
    			if (solicitacao){
    				prioridades += "listaPrioridadeSolicitacao["+index+"].naoReposicionarAutomatico="+solicitacao.naoReposicionarAutomaticoNaLista+"&";
    				prioridades += "listaPrioridadeSolicitacao["+index+"].numPosicao="+solicitacao.posicaoNaLista+"&";
   					prioridades += "listaPrioridadeSolicitacao["+index+"].prioridade="+ (solicitacao.prioridadeNaLista ? solicitacao.prioridadeNaLista : "") +"&";
    				prioridades += "listaPrioridadeSolicitacao["+index+"].idPrioridadeSolicitacao="+solicitacao.idPrioridadeSolicitacao+"&";
    				//prioridades += "listaPrioridadeSolicitacao["+index+"].lista.id="+idLista+"&";
    				//prioridades += "listaPrioridadeSolicitacao["+index+"].solicitacao.id="+solicitacao.idSolicitacao+"&";
    				
    			}
	 	    });
	 	    if (prioridades.length > 0) {
	 	    	jQuery.blockUI(objBlock);
	 	    	$.ajax({
		 	    	type: "POST",
		 	    	url: "${linkTo[SolicitacaoController].priorizarLista}",
		 	    	data: "id=" + $('[name=idLista]').val() + "&" + prioridades,
		 	    	success: function() {
		 	    		alert('Lista gravada com sucesso');
		 	    		jQuery.unblockUI(objBlock);
		 	    	},
		 	    	error: function(XMLHttpRequest, textStatus, errorThrown) {
		 	    		alert('Não foi possível efetivar a priorização');
		 	    		jQuery.unblockUI(objBlock);
		 	    	}
	 	    	});
		 	}
	    });
	});

	var opts = {
			urlGravar : '${linkTo[SolicitacaoController].gravarLista}',
			dialogCadastro : $('#editarLista_dialog'),
			objectName : 'lista',
			formCadastro : $('#formLista')
	};	

	// Define a "classe" listaService
	function ListaService(opts) {
		// super(opts)
		BaseService.call(this, opts);
	}
	// listaService extends BaseService
	ListaService.prototype = Object.create(BaseService.prototype);

	var listaService = new ListaService(opts);
	// Sobescreve o metodo cadastrar para limpara tela
	listaService.cadastrar = function(title) {
		BaseService.prototype.cadastrar.call(this, title);
	}

	listaService.getId = function(lista) {
		return lista.idLista;
	}
	/**
	 * Customiza o metodo editar
	 */
	listaService.editar = function(lista, title) {
		BaseService.prototype.editar.call(this, lista, title); // super.editar();

		limparDadosListaModal();
		// carrega as permissões da lista
		carregarPermissoes(lista.idLista);
		configuracaoInclusaoAutomaticaService.carregarParaLista(lista.idLista);
	}
	/**
	* Customiza o método onGravar()
	*/
	listaService.onGravar = function(obj, objSalvo) {
		listaJson = objSalvo;

		if (listaJson)
			$("#tituloPagina").html(listaJson.nomeLista);
	}

	listaService.alterarPosicao = function(event) {
		var tr = $(event.target).parent().parent().parent(),
			obj = JSON.parse(tr.attr('data-json'));

		if (obj) {
			posicao();
			//new Formulario($('#posicaoForm')).populateFromJson(obj);
			$("#idPrioridadePosicao").val(obj.idSolicitacao);
			$("input[name='posicaoNaLista']").val(obj.posicaoNaLista);
		}
	}

	listaService.alterarPrioridade = function(event) {
		var tr = $(event.target).parent().parent().parent(),
			obj = JSON.parse(tr.attr('data-json'));

		if (obj) {
			prioridade();
			$("#idPrioridadePrior").val(obj.idSolicitacao);
			$("select[name='prioridadeNaLista']").val(obj.prioridadeNaLista);
			$("input[name='naoReposicionarAutomatico']").val(obj.naoReposicionarAutomaticoNaLista);
			$("#checknaoReposicionarAutomatico")[0].checked = obj.naoReposicionarAutomaticoNaLista;
		}
	}

	function editarLista(event, jSon) {
		event.stopPropagation();

		if (!listaJson)
			listaJson = jSon;

		listaService.editar(listaJson, 'Alterar Lista');
	}
	
	function carregarPermissoes(id) {
        $.ajax({
        	type: "GET",
        	url: '${linkTo[SolicitacaoController].buscarPermissoesLista}?idLista='+id,
        	dataType: "text",
        	success: function(lista) {
        		var permissoesJSon = JSON.parse(lista);
        		populatePermissoesFromJSonList(permissoesJSon);
        	},
        	error: function(error) {
            	alert("Não foi possível carregar as Permissões desta Lista.");
        	}
       	});
    }

	function getAcaoPermissao(permissao) {
		if(permissao.ativo) {
	 			return '<a class="once desassociarPermissao" onclick="desativarPermissaoUsoListaEdicao(event, '+permissao.idConfiguracao+')" title="Remover permissão">' + 
						'<input class="idPermissao" type="hidden" value="'+permissao.idConfiguracao+'}"/>' + 
						'<img id="imgCancelar" src="/siga/css/famfamfam/icons/delete.png" style="margin-right: 5px;">' + 
					'</a>';
		}
		return new String();
	}
	
	function reposicionar() {
		var novaPosicao = $('[name=posicaoNaLista]').val(),
			lista = $("#sortable > tr"),
			idSolicitacao = $('#idPrioridadePosicao').val(),
			tr = $('[data-json-id= '+ idSolicitacao + ']'),
			size = lista.size(),
			stringSolicitacao = $(tr).attr('data-json'),
			solicitacao = JSON.parse(stringSolicitacao);

		if (novaPosicao <= 0) {
			tr.insertBefore($(lista[0]));
		} 
		else if (novaPosicao >= size) {
			tr.insertAfter($(lista[size-1]));
		}
		else {
			if (solicitacao && solicitacao.posicaoNaLista && solicitacao.posicaoNaLista < novaPosicao) {
				tr.insertBefore($(lista[novaPosicao]));
			}
			else tr.insertBefore($(lista[novaPosicao-1]));
		}
		recalcularPosicao();
		modalPosicaoFechar();
	}

	function modalPosicaoFechar() {
		posicao_fechar();
	}
	
	function recalcularPosicao() {
		var posicao = 0;
		$("#sortable > tr").each(function() {
			var me = $(this),
				objString = me.attr('data-json'),
				obj = JSON.parse(objString),
				numPosicaoAntiga = -1;

			if (obj) {
				numPosicaoAntiga = obj.posicaoNaLista;
				posicao++;
				obj.posicaoNaLista = posicao;
			}
			me.attr('data-json', JSON.stringify(obj));
			me.find('td:first').find('a').html(posicao);
		});		
		
	}
	
	function gravarPrioridade() {
		var novaPrioridade = $('#selectPrioridade').val(),
			idSolicitacao = $('#idPrioridadePrior').val(),
			tr = $('[data-json-id= '+ idSolicitacao + ']'),
			objString = $(tr).attr('data-json'),
			obj = JSON.parse(objString);
		
		if (obj) {
			var prioridadeAntiga = obj.prioridadeNaLista;
			tr.removeClass('PRIORIDADE-' + obj.prioridadeNaLista);
			tr.addClass('PRIORIDADE-' + novaPrioridade);

			obj.prioridadeNaLista = novaPrioridade;
			obj.naoReposicionarAutomaticoNaLista = $('#checknaoReposicionarAutomatico').is(':checked');
			
			tr.attr('data-json', JSON.stringify(obj));
			
			if(prioridadeAntiga != novaPrioridade) {
				if (!obj.naoReposicionarAutomaticoNaLista) {
					reposicionarPorPrioridade(obj, tr);
				}
			}
		}
		recalcularPosicao();
		modalPrioridadeFechar();
	}

	function modalPrioridadeFechar() {
		prioridade_fechar();
	}
	
	function reposicionarPorPrioridade(listaVO, tr) {
		var lista = $("#sortable > tr"),
			reposicionou = reposicionarAposIgual(lista, tr, listaVO);
		
		if (!reposicionou) {
			reposicionou = reposicionarPorPrecedenciaPrioridade(lista, tr, listaVO);
		}

		if (!reposicionou) {
			// Insere no final
			tr.insertAfter($(lista[lista.size() - 1]));
		}		
	}

	function reposicionarAposIgual(lista, tr, listaVO) {
		for (var i = lista.size() - 1; i >= 0; i--) {
			var trAdicionado = $(lista[i]), 
				listaVOAdicionado = JSON.parse(trAdicionado.attr('data-json'));

			if (registrosPrioridadesDiferentes(listaVO, listaVOAdicionado)) {
				if (listaVO && listaVO.prioridadeNaLista && listaVO.prioridadeNaLista == listaVOAdicionado.prioridadeNaLista) {
					tr.insertAfter(trAdicionado);
					return true;
				}
			}
		}
		return false;
	}

	function reposicionarPorPrecedenciaPrioridade(lista, tr, listaVO) {
		var jsonPrioridades = $('#jsonPrioridades').data('json');
		
		for (var i = 0; i <= lista.size() - 1; i++) {
			if(listaVO && listaVO.prioridadeNaLista) {
				var trAdicionado = $(lista[i]),
					listaVOAdicionado = JSON.parse(trAdicionado.attr('data-json')),
					idPrioridadeNovo = jsonPrioridades[listaVO.prioridadeNaLista],
					idPrioridadeAntigo = jsonPrioridades[listaVOAdicionado.prioridadeNaLista];
				
				if (idPrioridadeNovo > idPrioridadeAntigo) {
					tr.insertBefore(trAdicionado);
					return true;
				}
			}
		}
		return false;
	}

	function registrosPrioridadesDiferentes(listaVO, listaVOAdicionado) {
		return listaVO.idPrioridadeSolicitacao != listaVOAdicionado.idPrioridadeSolicitacao;
	}
</script>

