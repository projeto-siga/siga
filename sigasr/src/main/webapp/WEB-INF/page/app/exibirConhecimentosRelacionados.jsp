<div class="gt-sidebar">
		<div class="gt-sidebar-content" id="gc">
		</div>
</div>
#{if solicitacao.itemConfiguracao && controllers.SigaApplication.podeUtilizarServico('SIGA;GC')}
<script type="text/javascript">
	SetInnerHTMLFromAjaxResponse("/../sigagc/app/knowledgeSidebar?${solicitacao.gcTags.raw()}&testarAcesso=true&popup=true&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",document.getElementById('gc'));
</script>
#{/if} 



