<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<c:if
	test="${discriminarVias eq true}">
	<c:set var="marcado" value="true" />
</c:if>

<c:set var="maxItens" value="10" />


<siga:pagina titulo="Buscar Classificação Documental" popup="true">

<script type="text/javascript" language="Javascript1.1">
	function limpar() {
		$("#alertFiltros").hide(); 
		$('#nome').val("");
		
		$(".nivelSelecionado").each(function() {
			this.value = '-1';
		});

		$('#containerResult').remove();
		$('#alertNaoEncontrado').remove();
		$('#nome').focus();		
	}

	function valida() {
		let nivelSelecionado = false;
		$(".nivelSelecionado").each(function() {
		   if (this.value !== '-1') {
			   nivelSelecionado = true;
		   }
		});
		
		if (nivelSelecionado || $('#nome').val() !== "" ) {
			return true;
		} else {
			return false;
		}
	}
	
	function alterarNivel(nivelAlterado){
		document.getElementById("nivelAlterado").value = nivelAlterado;
	}

	
	function invalid(event) {
		$("#alertFiltros").show();
		$('#containerResult').remove();
		$('#alertNaoEncontrado').remove();
	}
	
	function submit(event) {

		if (!valida()) {
		  invalid(event);
		  event.preventDefault();
		} else {
			sbmt(0);
		}
	}
	
	function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}
		frm.elements["paramoffset"].value = offset;
		frm.elements["p.offset"].value = offset;
		frm.submit();
	}
	
	$( document ).ready(function() {
		$("#alertFiltros").hide();
		frm.onsubmit = submit;

		if ($('#sigla').val() === "" ) {
			$('#sigla').focus();
		}
			
	});


</script>

<c:choose>
	<c:when test="${param.modal != true}">
	    <!-- parteFuncao para fechar window -->
	    <c:set var="parteFuncao" value="opener" />
	</c:when>
	<c:otherwise>
	    <!-- parteFuncao para fechar modal -->
	    <c:set var="parteFuncao" value="parent" />
	</c:otherwise>	
</c:choose>	

	<div class="container-fluid">
		<div id="alertFiltros" class="alert alert-warning" role="alert" style="display: none;">
		  Obrigatório informar <strong>Palavra-chave</strong> e/ou informar algum<strong> Nível de Classificação</strong> para realizar a pesquisa.
		</div>
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Pesquisa de Classificação Documental</h5>
			</div>

			<div class="card-body">

				<form name="frm" action="buscar" namespace="/classificacao" cssClass="form" theme="simple" method="get">
					<input type="hidden" name="propriedade" value="${param.propriedade}" />
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="paramoffset" value="0" />
					<input type="hidden" name="p.offset" value="0" />
					<input type="hidden" id="nivelAlterado" name="nivelAlterado" />
				    <input type="hidden" name="modal" value="${param['modal']}" />					

					<div class="row">
						<div class="col">
							<h5>Dados da Classificação</h5>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label>Palavra-chave</label> 
								<input class="form-control" type="text" name="nome" id="nome" size="50" value="${nome}"/>
								<small class="text-muted">Obrigatório caso níveis abaixo não sejam filtrados</small>
							</div>
						</div>
					</div>

					<c:forEach items="${listaNiveis}" var="itemLista" varStatus="i">
						<div class="row">
							<div class="col-sm">
								<div class="form-group">
									<label>${nomeDoNivel[i.index]}</label> 
									<select class="custom-select nivelSelecionado" id="nivelSelecionado[${i.index}]" name="nivelSelecionado[${i.index}]" onchange="javascript:alterarNivel(${i.index});javascript:submit();" >
										<option value="-1" >[Todos]</option>									
										<c:forEach items="${classificacoesDoNivel[i.index]}" var="item">
											<option value="${item.codificacao}" ${item.codificacao == nivelSelecionado[i.index] ? 'selected' : ''}>${item.descrClassificacao} </option>  
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</c:forEach> 
					
					<div class="row pt-1">
						<div class="col-12 text-center">
							<div class="form-group ">
								<button type="submit" class="btn btn-primary" id="idSubmit">Pesquisar</button> 
								<button type="button" class="btn btn-secondary" onclick="limpar();" >Limpar</button> 
								<br />
								<input type="checkbox"  name="discriminarVias" id="check" fieldValue="true" onclick="javascript: submit();" <c:if test="${marcado}">checked</c:if>/>
								<label class="form-check-label" for="check">Discriminar vias na listagem</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			</div>

			<c:if test="${tamanho gt 0}">
				<div id="containerResult">
					<table class="table table-hover pt-2">
						<thead class="${thead_color}">
							<tr>
								<th>Codificação</th>
								<th>Descrição</th>
									<c:if test="${marcado}">
									<th>Via</th>
									<th>Destino</th>
									<th>Arq. Corrente</th>
									<th>Arq. Interm.</th>
									<th>Dest. Final</th>
									<th>Obs.</th>
								</c:if>
							</tr>
						</thead>
		
						<tbody>
							<siga:paginador maxItens="${maxItens}" maxIndices="10" totalItens="${tamanho}" itens="${itens}" var="classificacao">
							<c:set var="numVias" value="${classificacao.numVias}" />
							<tr class="${evenorodd}">
								<c:choose>
									<c:when test="${marcado}">
										<c:set var="rowSpan">rowspan="${numVias}"</c:set>
									</c:when>
									<c:otherwise>
										<c:set var="rowSpan" value="" />
									</c:otherwise>
								</c:choose>
								<td ${rowSpan}><a
									href="javascript: ${parteFuncao}.retorna_${param.propriedade}('${classificacao.id}','${classificacao.sigla}','${classificacao.descricao}');window.close()">${classificacao.sigla}</a>
								</td>
								<td align="left"${rowSpan}>${classificacao.descricao}</td>
								<c:choose>
									<c:when test="${marcado}">
										<c:forEach var="via" items="${classificacao.exViaSet}"	varStatus="status">
											<c:if test="${status.index > 0}">
												</tr>
												<tr class="${evenorodd}">
											</c:if>
											<td width="1%" align="center">${via.letraVia}</td>
											<td width="10%">${via.exTipoDestinacao.descrTipoDestinacao}</td>
											<td width="6%">${via.temporalidadeCorrente.descTemporalidade}</td>
											<td width="6%">${via.temporalidadeIntermediario.descTemporalidade}</td>
											<td width="6%">${via.exDestinacaoFinal.descrTipoDestinacao}</td>
											<td width="24%">${via.obs}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										</tr>
									</c:otherwise>
									</c:choose>
								</siga:paginador>
							</tbody>
						</table>
					</div>
				</c:if>
				<c:if test="${tamanho eq 0}">
					<div id="alertNaoEncontrado" class="alert alert-danger text-center leas" role="alert">
						<h5>Não foi possível encontrar resultados</h5>
						<p>
							Verifique as informações fornecidas
						</p>
					</div>
				</c:if>
			</div>
		</div>

</siga:pagina>
