<html>
<body>
	<p>
		Informamos que a solicitação <b>${(sol.codigo)!}</b>
		recebeu a seguinte movimentacao em ${(movimentacao.dtIniMovDDMMYYYYHHMM)!}:
	</p>
	<blockquote>
		<p>Tipo de movimentação: ${(movimentacao.tipoMov.nome)!}</p>
		<p>${(movimentacao.descrMovimentacao)?default("")}</p>
		<p>
			Por ${(movimentacao.cadastrante.descricaoIniciaisMaiusculas)!}
				(${(movimentacao.lotaCadastrante.siglaLotacao)!})
		</p>
	</blockquote>
	<p>
		<#if (sol.solicitacaoPai.atendente)??>
			<#assign descricao = (sol.solicitacaoPai.atendente.descricaoIniciaisMaiusculas)!>
		<#else>
			<#assign descricao = (sol.solicitacaoPai.lotaAtendente.descricao)!>
		</#if>
		Este email foi enviado porque <b>${(descricao)!}</b> é atendente atual da solicitação <b>${(sol.solicitacaoPai.codigo)!}</b>, 
		que gerou a solicitação acima através da ação <b>Escalonar</b>.
	</p>
	<p>
		Para acessar a solicitação, clique <a href="${link}">aqui</a>.
	</p>
</body>
</html>