<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div class="gt-content-box gt-form">
<form action="#" method="post" enctype="multipart/form-data" id="frm" class="form-group">
	<input type="hidden" name="todoOContexto" value="${todoOContexto}" />
	<input type="hidden" name="ocultas" value="${ocultas}" />
	<input type="hidden" name="solicitacao.codigo" id="sigla" value="${siglaCompacta}" autofocus="true"/>
	<sigasr:classificacao metodo="reclassificar" />
	<div class="gt-form-row">
		<input type="button" value="Gravar" class="btn btn-primary" onclick="gravar()"/>
	</div>
</form>
</div>

<script>
function postbackURL(){
	return '${linkTo[SolicitacaoController].reclassificar}?solicitacao.codigo='+$("#sigla").val()
		+'&solicitacao.itemConfiguracao.id='+$("#formulario_solicitacaoitemConfiguracao_id").val();
}

function submitURL() {
	return '${linkTo[SolicitacaoController].reclassificarGravar}';
}
</script>