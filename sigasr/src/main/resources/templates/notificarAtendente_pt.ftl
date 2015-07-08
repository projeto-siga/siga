<html>
<body>
	<p>
		Informamos que a solicita&ccedil;&atilde;o <b>${(sol.codigo)!}</b>
		recebeu a seguinte movimenta&ccedil;&atilde;o em ${(movimentacao.dtIniMovDDMMYYYYHHMM)!}:
	</p>
	<blockquote>
		<p>Tipo de movimenta&ccedil;&atilde;o: ${(movimentacao.tipoMov.nome)!}</p>
		<p>Atendente: <#if (movimentacao.atendente)??> ${(movimentacao.atendente.descricaoIniciaisMaiusculas)!}, </#if>
                        ${(movimentacao.lotaAtendente.siglaLotacao)!}</p>
		<p>Solicitante: ${(sol.solicitante.descricaoIniciaisMaiusculas)!}, ${(sol.lotaSolicitante.siglaLotacao)!} </p>
		<p>${(sol.descrSolicitacao)!}</p>
	</blockquote>
	<#if (movimentacao.tipoMov.idTipoMov == models.SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)>
	<p>
		<#if (sol.atendente != null)>
        	<#assign descricao = (sol.atendente.descricaoIniciaisMaiusculas)!>
		<#else>
			<#assign descricao = (sol.lotaAtendente.descricao)!> 
		</#if>
		Este email foi enviado porque <b>${descricao}</b> é atendente atual da solicitação <b>${(sol.codigo)!}</b>,
		que gerou a solicitação acima através da ação <b>Escalonar</b>.
	</p>
	</#if>
	<p>
		Para acessar a solicita&ccedil;&atilde;o, clique <a href="${link}">aqui</a>.
	</p>
</body>
</html>