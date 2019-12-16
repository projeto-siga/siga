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
	function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}
		frm.elements["paramoffset"].value = offset;
		frm.elements["p.offset"].value = offset;
		frm.submit();
	}
	
	function alterarNivel(nivelAlterado){
		document.getElementById("nivelAlterado").value = nivelAlterado;
	}



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
								<input class="form-control" type="text" name="nome" size="50" value="${nome}"/>
							</div>
						</div>
					</div>

					<c:forEach items="${listaNiveis}" var="itemLista" varStatus="i">
						<div class="row">
							<div class="col-sm">
								<div class="form-group">
									<label>${nomeDoNivel[i.index]}</label> 
									<select class="custom-select"  name="nivelSelecionado[${i.index}]" onchange="javascript:alterarNivel(${i.index});javascript:sbmt(0);" >
										<option value="-1" >[Todos]</option>									
										<c:forEach items="${classificacoesDoNivel[i.index]}" var="item">
											<option value="${item.codificacao}" ${item.codificacao == nivelSelecionado[i.index] ? 'selected' : ''}>${item.descrClassificacao} </option>  
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</c:forEach> 
					
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<input type="submit" value="Pesquisar" class="btn btn-primary" />
								<input type="checkbox"  name="discriminarVias" id="check" fieldValue="true" onclick="javascript: sbmt();" <c:if test="${marcado}">checked</c:if>/>
								<label class="form-check-label" for="check">Discriminar vias na listagem</label>
							</div>
						</div>
					</div>
				</form>
			</div>
			</div>

			<div>

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
			</div>
		</div>

</siga:pagina>
