<div id="divPrioridade" class="gt-form-row gt-width-66">
	<label style="float: left">Prioridade: &nbsp;</label>
	<span>${solicitacao.prioridade?.descPrioridade ?: models.SrPrioridade.PLANEJADO.descPrioridade}</span>
	<sigasr:select
		name="solicitacao.prioridade" id="prioridade" items="${SrPrioridade.values()}" 
		labelProperty="descPrioridade" value="${solicitacao.prioridade ?: 'PLANEJADO'}" style="width:235px;border:none;display:none;"  />
	<br />
</div>
