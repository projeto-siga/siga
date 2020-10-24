<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<script type="text/javascript" language="Javascript1.1">
	function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}
		frm.elements['p.offset'].value = offset;
		frm.submit();
	}
</script>

<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>
<siga:pagina titulo="Busca de ${cpTipoGrupo.dscTpGrupo}">
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Cadastro de ${cpTipoGrupo.dscTpGrupo}</h5></div>
				<div class="card-body">
					<table border="0" class="table table-sm table-striped">
						<thead class="${thead_color}">
							<th align="left">Sigla</th>
							<th align="left">Descrição</th>
							<th align="left">Sigla Grupo Pai</th>
						</thead>
						<c:set var="evenorodd" value="" />
						<c:set var="tamanho" value="0" />
						<siga:paginador maxItens="1000" maxIndices="10"
							totalItens="${tamanho}" itens="${itens}" var="grupoItem">
							<tr class="${evenorodd}">
								<td align="left"><a
									href="editar?idCpGrupo=${grupoItem.idGrupo }">${grupoItem.siglaGrupo}</a></td>
								<td align="left"><a
									href="editar?idCpGrupo=${grupoItem.idGrupo }">${grupoItem.dscGrupo}</a></td>
								<td align="left"><a
									href="editar?idCpGrupo=${grupoItem.idGrupo }">${grupoItem.cpGrupoPai.siglaGrupo}</a></td>
							</tr>
						</siga:paginador>
					</table>
					<c:if test="${cpTipoGrupo.idTpGrupo != 2 or (cpTipoGrupo.idTpGrupo == 2 and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;INC:Incluir'))}">
						<div class="row">
							<div class="col-sm">
								<div class="form-group">
									<input type="button" value="Incluir" onclick="javascript:window.location.href='editar'" class="btn btn-primary">
								</div>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>