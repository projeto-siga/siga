<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Arquivamento Intermediário em Lote">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

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

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Arquivamento Intermediário em Lote</h2>

			<div class="gt-content-box gt-for-table">


	<form name="frm" action="arquivar_intermediario_lote_gravar"
		method="GET" theme="simple">	
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="paramOffset" value="${paramOffset}" />
		<input type="hidden" name="p.offset" value="${p.offset}" />
		<table class="gt-form-table">
			<tr class="header">
				<td colspan="2">Arquivamento Intermediário</td>
			</tr>
			<tr>
				<td>Data:</td>
				<td><input type="text" name="dtMovString" value="${dtMovString}"
					onblur="javascript:verifica_data(this,0);" /></td>
			</tr>
			<tr>
				<td>Responsável:</td>
				<td><siga:selecao tema="simple" propriedade="subscritor" modulo="siga/pessoa"  />
				&nbsp;&nbsp;<input type="checkbox" theme="simple" name="substituicao" value="${substituicao}"
					onclick="javascript:displayTitular(this);" /> Substituto</td>
			</tr>
			<c:choose>
				<c:when test="${!substituicao}">
					<tr id="tr_titular" style="display: none">
				</c:when>
				<c:otherwise>
					<tr id="tr_titular" style="">
				</c:otherwise>
			</c:choose>

			<td>Titular:</td>
			<input type="hidden" name="campos" value="titularSel.id" />
			<td colspan="1"><siga:selecao propriedade="titular"
				tema="simple" modulo="siga/pessoa"  /></td>
			</tr>
			<tr>
				<td>Função do Responsável:</td>
				<td colspan="1"><input type="hidden" name="campos"
					value="nmFuncaoSubscritor" /> <input type="text"
					name="nmFuncaoSubscritor" value="${nmFuncaoSubscritor}" size="50" maxLength="128" /> (opcional)</td>
			</tr>
			<tr>
				<td>Localização</td>
				<td>
					<input type="text" name="descrMov" maxlength="80" value="${descrMov}"
						size="80"></textarea>
				</td>
				</tr>
				
				<tr class="button">
					<td colspan="2"><input type="submit" value="Ok" class="gt-btn-medium gt-btn-left"/> <input type="button"
						value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
				</tr>
		</table>
		</div>

		<%-- Trecho adaptado da tag paginador--%>
		<pg:pager id="p" maxPageItems="100"
			maxIndexPages="20" scope="request" isOffset="true"
			items="${tamanho}" export="offset,pageOffset;currentPageNumber=pageNumber">	
			<div class="gt-table-controls gt-table-controls-btm clearfix">
				<p>Total: ${tamanho} itens</p>
				<p style="float: left" class="gt-table-pager">
					<c:set var="firstpgpages" value="${true}"/>
				<pg:pages>
						<c:if test="${not firstpgpages}"> | </c:if>
						<c:set var="firstpgpages" value="${false}"/>
					<a href="javascript:sbmt(${(pageNumber-1)*100});" <c:if test="${pageNumber == currentPageNumber}">class="current"</c:if>>
						<c:out value="${pageNumber}" />
					</a>
				</pg:pages>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="pag" type="text" value="${currentPageNumber}" size="3"/>
				<input id="pag_btn" type="button" value="Ir para página" onclick="javascript: sbmt(($('#pag').val()-1)*100);" class="gt-btn-medium" style="display: inline;" />
				<script>
				$('#pag').keypress(function(eve) {
				     var key = eve.keyCode || e.which ;
				     if (key == 13) {
				          $('#pag_btn').click();
				     }        
				});
				</script>
				</p>
			</div>
		</pg:pager>

		<c:forEach var="topico" items="${itens}">
			<br />
			<h2>${topico.titulo}</h2>
			<div class="gt-content-box gt-for-table">
				<table class="gt-table">
					<tr class="header">
						<td rowspan="2" align="center">
							<c:if test="${topico.selecionavel}">
							<input type="checkbox"
								name="checkall" onclick="checkUncheckAll(this)" />
							</c:if>
						</td>
						<td rowspan="2" align="right">Número</td>
						<td colspan="3" align="center">Documento</td>
						<td colspan="2" align="center">Última Movimentação</td>
						<td rowspan="2">Descrição</td>
					</tr>
					<tr class="header">
						<td align="center">Data</td>
						<td align="center">Lotação</td>
						<td align="center">Pessoa</td>
						<td align="center">Data</td>
						<td align="center">Pessoa</td>
					</tr>
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
			</table>
			</div>
		</c:forEach>
	</form>
	
	</div></div>
</siga:pagina>