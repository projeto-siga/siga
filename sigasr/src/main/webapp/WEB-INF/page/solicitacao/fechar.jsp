<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div class="gt-content-box gt-form">
<form action="#" method="post" enctype="multipart/form-data" id="frm">
	<input type="hidden" name="todoOContexto" value="${todoOContexto}" />
	<input type="hidden" name="ocultas" value="${ocultas}" />
	<input type="hidden" name="solicitacao.codigo" id="sigla" value="${siglaCompacta}"/>
	<input type="hidden" name="conhecimento" id="conhecimento" value="" />
	<div class="gt-form-row gt-width-66">
		<label>Motivo</label>
			<siga:select name="tpMotivo" id="tpMotivo" list="motivosFechamento"
			listValue="descrTipoMotivoFechamento" theme="simple" isEnum="true"/>
	</div>
	<div style="display: inline" class="gt-form-row gt-width-66">
		<label>Detalhes</label>
		<textarea style="width: 100%" name="motivoFechar" cols="50" rows="4"> </textarea>
    </div><br/>
	<sigasr:classificacao metodo="fechar" />
	<div class="gt-form-row">
		<label>Conhecimento usado na solução</label>
		<div class="gt-sidebar-content" id="selecaoConhecimento"></div>
		<script type="text/javascript">
				SetInnerHTMLFromAjaxResponse("/../sigagc/app/selecaoInplace", document.getElementById('selecaoConhecimento'));
		</script>
	</div>
	<div class="gt-form-row">
		<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="carregarConhecimento();gravar();"/>
	</div>
</form>
</div>

<script>
function postbackURL(){
	return '${linkTo[SolicitacaoController].fechar}?solicitacao.codigo='+$("#sigla").val()
		+'&solicitacao.itemConfiguracao.id='+$("#formulario_solicitacaoitemConfiguracao_id").val();
}
function submitURL() {
	return '${linkTo[SolicitacaoController].fecharGravar}';
}
function carregarConhecimento() {
	$('#conhecimento').val($('#formulario_informacao_sigla').val());
}
</script>