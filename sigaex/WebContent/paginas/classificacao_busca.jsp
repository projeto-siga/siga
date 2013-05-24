<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<c:if
	test="${param['discriminarVias'] eq true}">
	<c:set var="marcado" value="true" />
</c:if>


<siga:pagina titulo="Buscar Classificação Documental" popup="true">

	<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements["p.offset"].value=offset;
	frm.submit();
}

function alterarNivel(nivelAlterado){
	document.getElementById("nivelAlterado").value = nivelAlterado;
}



</script>
	<div class="gt-bd clearfix">
		<div class="gt-content">
			<h2 class="gt-table-head">Pesquisa de Classificação Documental</h2>

			<div class="gt-content-box gt-for-table">

				<ww:form name="frm" action="buscar" namespace="/classificacao"
					cssClass="form" theme="simple" method="POST">
					<input type="hidden" name="propriedade"
						value="${param.propriedade}" />
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="p.offset" value="0" />
					<input type="hidden" id="nivelAlterado" name="nivelAlterado" />

					<table class="gt-form-table">
						<colgroup>
							<col style="width: 10em" />
						</colgroup>
						
						<tr class="header">
							<td align="center" valign="top" colspan="2">Dados da Classificação</td>
						</tr>
						
						<tr>
							<td>Palavra-chave:</td>
							<td><ww:textfield name="nome" size="50" />
							</td>
						</tr>

						<c:forEach items="${listaNiveis}" var="itemLista" varStatus="i">
							<ww:set value="%{getClassificacoesDoNivel(${i.index})}"
								name="classificacoesDoNivel" />
							<ww:set value="%{getNomeDoNivel(${i.index})}" name="nomeDoNivel" />
							<tr>
								<td>${nomeDoNivel}</td>
								<td><ww:select name="nivelSelecionado[${i.index}]"
										list="classificacoesDoNivel" listKey="codificacao"
										listValue="descrClassificacao" headerKey="-1"
										headerValue="[Todos]"
										onchange="javascript:alterarNivel(${i.index});javascript:sbmt(0);" />
								</td>
							</tr>
						</c:forEach>

						<tr>
							<td><input type="submit" value="Pesquisar"
								class="gt-btn-medium gt-btn-left" />&nbsp;</td>
							<td><ww:checkbox name="discriminarVias" id="check"
									fieldValue="true" onclick="javascript: sbmt();"
									value="${marcado}" />Discriminar vias na listagem</td>
						</tr>

					</table>


				</ww:form>



			</div>

			<div class="gt-form gt-content-box">

				<table border="0" class="gt-table">
					<thead>
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
					
		<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
			itens="${itens}" var="classificacao">
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
					href="javascript: opener.retorna_${param.propriedade}('${classificacao.id}','${classificacao.sigla}','${classificacao.descricao}');window.close()">${classificacao.sigla}</a>
				</td>
				<td align="left"${rowSpan}>${classificacao.descricao}</td>
				<c:choose>
					<c:when test="${marcado}">
						<c:forEach var="via" items="${classificacao.exViaSet}"
							varStatus="status">
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
