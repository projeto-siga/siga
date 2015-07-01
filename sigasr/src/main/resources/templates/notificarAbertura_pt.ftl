<html>
	<body>
		<#if sol.filha>
			<p>Informamos que a solicita&ccedil;&atilde;o <b>${(sol.solicitacaoPai.codigo)!}</b> foi escalonada 
			em ${sol.dtRegDDMMYYYYHHMM} por ${(sol.cadastrante.descricaoIniciaisMaiusculas)!}
			(${(sol.lotaCadastrante.siglaLotacao)!}), gerando a solicita&ccedil;&atilde;o filha n&ordm 
			<b>${(sol.codigo)!}</b>.</p>
			
		<#else>
			<p>
				Informamos que foi aberta por ${(sol.cadastrante.descricaoIniciaisMaiusculas)!} 
				(${(sol.lotaCadastrante.siglaLotacao)!}), em ${(sol.dtRegDDMMYYYYHHMM)!}, 
				a solicita&ccedil;&atilde;o <b>${(sol.codigo)!}</b>, com a seguinte descri&ccedil;&atilde;o:
			</p>
			<blockquote>
				<p>${(sol.descrSolicitacao)!}</p>	
			</blockquote>
		</#if>
		
		<p>
			Para acessar a solicita&ccedil;&atilde;o, clique <a href="${link}">aqui</a>.
		</p>
	</body>
</html>