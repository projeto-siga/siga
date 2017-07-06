<html>
	<body>
		<p>
			Informamos que a solicita&ccedil;&atilde;o <b>${(sol.codigo)!}</b>
			recebeu a seguinte movimenta&ccedil;&atilde;o em ${(movimentacao.dtIniMovDDMMYYYYHHMM)!}:
		</p>
		<blockquote>
			<p>Descri&ccedil;&atilde;o: ${(sol.descrSolicitacao)!}</p>
			<p>Tipo de movimenta&ccedil;&atilde;o: ${(movimentacao.tipoMov.nome)!}</p>
			<p>${(movimentacao.descrMovimentacao)!}</p>
			<p>
				Por ${(movimentacao.cadastrante.descricaoIniciaisMaiusculas)!}
					(${(movimentacao.lotaCadastrante.siglaLotacao)!})
			</p>
		</blockquote>
		<p>
			Para acessar a solicita&ccedil;&atilde;o, clique <a href="${link}">aqui</a>.
		</p>
	</body>
</html>