<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Arquivamento Intermediário em Lote">
	<script type="text/javascript" language="Javascript1.1">	
	function sbmt(offset) {
		if (offset==null) {
			offset=0;
		}
		$("[name='paramOffset']").val(offset);
		$("[name='p\\.offset']").val(offset);
		frm.action = '${pageContext.request.contextPath}/app/expediente/mov/arquivar_intermediario_lote';
		frm.submit();
	}
	
	function checkUncheckAll(theElement) {
		$(theElement).closest("table").find('input:checkbox')
			.prop('checked', theElement.checked);
	}
</script>
	<div class="container-fluid">
	<form name="frm" action="arquivar_intermediario_lote_gravar" method="GET" theme="simple">	
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="paramOffset" value="${paramOffset}" />
		<input type="hidden" name="p.offset" value="${p.offset}" />
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Arquivamento Intermediário</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<label for="dtMovString">Data</label>
							<input type="text" name="dtMovString" value="${dtMovString}" onblur="javascript:verifica_data(this,0);" class="form-control"/>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="dtMovString">Responsável</label>
							<siga:selecao tema="simple" propriedade="subscritor" modulo="siga/pessoa"  />
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<div class="form-group form-check mt-4">
								<input type="checkbox" name="substituicao" value="${substituicao}" onclick="javascript:displayTitular(this);" />
								<label class="form-check-label" for="substituicao">Substituto</label> 
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<c:choose>
						<c:when test="${!substituicao}">
							<c:set var="display" value="none" />
						</c:when>
						<c:otherwise>
							<c:set var="display" value="none" />
						</c:otherwise>
					</c:choose>
					<div id="tr_titular" class="col-sm-6" style="display: ${display}">
						<div class="form-group">
							<label for="nmFuncaoSubscritor">Titular</label>
							<input type="hidden" name="campos" value="titularSel.id" />
							<siga:selecao propriedade="titular" tema="simple" modulo="siga/pessoa" />
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="nmFuncaoSubscritor">Função do Responsável</label>
							<input type="hidden" name="campos" value="nmFuncaoSubscritor" /> 
							<input type="text" name="nmFuncaoSubscritor" value="${nmFuncaoSubscritor}" maxLength="128" class="form-control" /> 
							<small class="form-text text-muted">(opcional)</small> 
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label for="dtMovString">Localização</label>
							<input type="text" name="descrMov" maxlength="80" value="${descrMov}" class="form-control">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="submit" class="btn btn-primary">Ok</button> 
							<button type="button" onclick="javascript:history.back();" class="btn btn-primary">Cancela</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- Trecho adaptado da tag paginador--%>
		<pg:pager id="p" maxPageItems="100" maxIndexPages="20" scope="request" isOffset="true" items="${tamanho}" export="offset,pageOffset;currentPageNumber=pageNumber">	
			<div class="row">
				<div class="col-2">
					<p>Total: ${tamanho} itens</p>
				</div>
			</div>
			<div class="row">
				<div class="col-1">
					<c:set var="firstpgpages" value="${true}"/>
					<pg:pages>
						<c:if test="${not firstpgpages}"> | </c:if>
						<c:set var="firstpgpages" value="${false}"/>
						<a href="javascript:sbmt(${(pageNumber-1)*100});" <c:if test="${pageNumber == currentPageNumber}">class="current"</c:if>>
							<c:out value="${pageNumber}" />
						</a>
					</pg:pages> 
				</div>
				<div class="col-1">
					<input id="pag" type="text" value="${currentPageNumber}" size="3" class="form-control"/>
				</div>
				<div class="col-2">
					<input id="pag_btn" type="button" value="Ir para página" onclick="javascript: sbmt(($('#pag').val()-1)*100);" class="btn btn-secondary" style="display: inline;" />
				</div>
			</div>
			<script>
			$('#pag').keypress(function(eve) {
			     var key = eve.keyCode || e.which ;
			     if (key == 13) {
			          $('#pag_btn').click();
			     }        
			});
			</script>
		</pg:pager>
		<c:forEach var="topico" items="${itens}">
			<h5>${topico.titulo}</h5>
			<div>
				<table class="table table-hover table-striped">
					<thead class="${thead_color} align-middle text-center">
						<tr class="header">
							<th rowspan="2" align="center">
								<c:if test="${topico.selecionavel}">
								<input type="checkbox"
									name="checkall" onclick="checkUncheckAll(this)" />
								</c:if>
							</th>
							<th rowspan="2" align="right">Número</th>
							<th colspan="3" align="center">Documento</th>
							<th colspan="2" align="center">Última Movimentação</th>
							<th rowspan="2">Descrição</th>
						</tr>
						<tr class="header">
							<th align="center">Data</th>
							<th align="center"><fmt:message key="usuario.lotacao"/></th>
							<th align="center"><fmt:message key="usuario.pessoa2"/></th>
							<th align="center">Data</th>
							<th align="center"><fmt:message key="usuario.pessoa2"/></th>
						</tr>
					</thead>
				    <tbody class="table-bordered">
					<c:forEach var="item" items="${topico.itens}">
						<c:set var="mob" value="${item.mob}" />
						<c:set var="mar" value="${item.marca}" />
						<c:choose>
							<c:when test='${evenorodd == "even"}'>
								<c:set var="evenorodd" value="odd" />
							</c:when>
							<c:otherwise>
								<c:set var="evenorodd" value="even" />
							</c:otherwise>
						</c:choose>
						<tr class="${evenorodd}">
							<c:choose>
								<c:when test="${topico.selecionavel}">
									<c:set var="x" scope="request">chk_${mob.id}</c:set>
									<c:remove var="x_checked" scope="request" />
									<c:if test="${param[x] == 'true'}">
										<c:set var="x_checked" scope="request">checked</c:set>
									</c:if>
									<td width="2%" align="center"><input type="checkbox"
										name="${x}" value="true" ${x_checked} /></td>
								</c:when>
								<c:otherwise><td width="2%" align="center"></td></c:otherwise>
							</c:choose>
							<td width="11.5%" align="right">
								<c:choose>
								<c:when test='${param.popup!="true"}'>							
									<a href="javascript:void(0)"
													onclick="javascript: window.open('${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mob.sigla}&popup=true', '_new', 'width=700,height=500,scrollbars=yes,resizable')">${mob.sigla}</a>
								</c:when>
								<c:otherwise>
									<a
										href="javascript:opener.retorna_${param.propriedade}('${mob.id}','${mob.sigla},'');">${mob.sigla}</a>
								</c:otherwise>
								</c:choose>
							</td>
							<c:if test="${not mob.geral}">
								<td width="5%" align="center">${mob.doc.dtDocDDMMYYYY}</td>
								<td width="5%" align="center"><siga:selecionado
									sigla="${mob.doc.lotaSubscritor.sigla}"
									descricao="${mob.doc.lotaSubscritor.descricao}" /></td>
								<td width="5%" align="center"><siga:selecionado
									sigla="${mob.doc.subscritor.iniciais}"
									descricao="${mob.doc.subscritor.descricao}" /></td>
								<td width="5%" align="center">${mob.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
								<td width="4%" align="center"><siga:selecionado
									sigla="${mob.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
									descricao="${mob.ultimaMovimentacaoNaoCancelada.resp.descricao}" /></td>
							</c:if>
							<c:if test="${mob.geral}">
								<td width="2%" align="center"></td>
								<td width="5%" align="center">${mob.doc.dtDocDDMMYYYY}</td>
								<td width="4%" align="center"><siga:selecionado
									sigla="${mob.doc.subscritor.iniciais}"
									descricao="${mob.doc.subscritor.descricao}" /></td>
								<td width="4%" align="center"><siga:selecionado
									sigla="${mob.doc.lotaSubscritor.sigla}"
									descricao="${mob.doc.lotaSubscritor.descricao}" /></td>
								<td width="5%" align="center"></td>
								<td width="4%" align="center"></td>
								<td width="4%" align="center"></td>
								<td width="10.5%" align="center"></td>
							</c:if>
							<td width="44%">${f:descricaoSePuderAcessar(mob.doc, titular,
								lotaTitular)}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</c:forEach>
	</form>
	</div>
</siga:pagina>