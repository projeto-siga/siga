<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../popupHeader.jsp"></jsp:include>
<!-- <script src="//code.jquery.com/jquery-1.11.0.min.js"></script> -->
<jsp:include page="../main.jsp"></jsp:include>
<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css"	type="text/css" media="screen, projection" />

<script language="javascript">
function sbmt(nivel){
	document.getElementById('alterou').value=nivel;
	frm.submit();
}
</script>

<head><title>Pesquisa de Itens de Configuração</title></head>


<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<div class="gt-content-box gt-for-table">
			<form action="${linkTo[ItemConfiguracaoController].buscar}" id="frm" enctype="multipart/form-data">
				<input type="hidden" name="popup" value="true" />
				<table class="table gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="5">Dados do item</td>
					</tr>
					<tr>
						<td colspan="1">Código</td>
						<td colspan="4">
							<input class="form-control" type="text" name="siglaItemConfiguracao" value="${filtro.siglaItemConfiguracao}" />
						</td>
					</tr>
					<tr>
						<td colspan="1">Título</td>
						<td colspan="4">
							<input class="form-control" type="text" name="tituloItemConfiguracao" value="${filtro.tituloItemConfiguracao}" />
						</td>
					</tr>
					<tr>
						<td colspan="5"><input type="hidden" name="nome" value="${nome}" />
							<input type="hidden" name="propriedade" value="${propriedade}" />
							<input type="hidden" name="sol.id" value="${sol.idSolicitacao}" />
							<input type="hidden" name="sol.solicitante.id" value="${sol.solicitante.idPessoa}" />
							<input type="hidden" name="sol.titular.id" value="${sol.titular.idPessoa}" />
							<input type="hidden" name="sol.lotaTitular.id" value="${sol.lotaTitular.idLotacao}" />
							<input type="hidden" name="sol.local.id" value="${sol.local.idComplexo}" />
							<input type="submit" class="btn btn-primary" value="Pesquisar" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<div class="gt-content-box gt-for-table table-responsive">
		<table class="table gt-table gt-table-nowraps display" id="tabela">
	  		<thead>
				<tr id="plim">
					<th style="width: 30%">Título</th>
					<th style="width: 20%">Código</th>
					<th style="width: 25%">Descrição</th>
					<th style="width: 25%">Itens Similares</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${items}" var="item">
					<tr name="${item.nivel}" >
						<td class="gt-celula-nowrap" style="padding-left: ${item.nivel*13}px;">
							<c:choose>
								<c:when test="${!item.especifico}">
									<a href="" onclick="clica(this); return false;" style="text-decoration: none; font-size: 14pt" name="sinal">-</a>
								</c:when>
								<c:otherwise>
									&nbsp;&nbsp;
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${item.especifico || (empty sol.solicitante && empty sol.local) }">
									<a href="javascript:parent.retorna_${param.propriedade}${nome}('${item.id}','${item.sigla}','${item.descricao}');">${item.tituloItemConfiguracao}</a>
								</c:when>
								<c:otherwise>
									<span>${item.tituloItemConfiguracao}</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="gt-celula-nowrap">${item.siglaItemConfiguracao}</td>
						<td class="gt-celula-nowrap">${item.descrItemConfiguracao}</td>
						<td class="gt-celula-nowrap">${item.descricaoSimilaridade}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>	

<script language="javascript">

$(document).ready(function() {
	var titulo = "${filtro.tituloItemConfiguracao}"; 
	if (titulo == "" || titulo == undefined) {
		var sinais = $("[name='sinal']"); 
		for(i=0; i<sinais.length; i++ ){
	        clica(sinais[i]);
	    }
	}
});

function clica(obj){
	var ope = obj.innerHTML; 
	obj.innerHTML = (ope == '+' ? '-' : '+');
	var tr = $(obj).parent().parent().next();
	var nivelAtual = parseInt(tr.attr('name'));
	var nivelMinimo = nivelMaximo = nivelAtual;
	while (nivelAtual >= nivelMinimo){
		if (ope == '-')
			tr.hide();
		else {
			if (nivelAtual <= nivelMaximo){
				tr.show();
				var span = tr.find('td:first').find("[name='sinal']")[0];
				var sinal = span ? span.innerHTML : '';
				nivelMaximo = nivelAtual + (sinal == '-' ? 1 : 0)
			}
		}
		tr = tr.next();
		nivelAtual = parseInt(tr.attr('name'));
	}
}
</script>