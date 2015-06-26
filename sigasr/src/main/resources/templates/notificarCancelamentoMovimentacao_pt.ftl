<html>
	<body>
		<p>
			Informamos que foi <b>cancelada</b>, em
			${(movimentacao.movCanceladora.dtIniMovDDMMYYYYHHMM)!}, a <b>movimenta\u00e7\u00e3o</b> de ${(movimentacao.tipoMov.nome)!},
			dada na solicita\u00e7\u00e3o <b>${sol.codigo}</b>
		</p>
		<blockquote>
			<p style="text-decoration: line-through;">${(movimentacao.descrMovimentacao)!}</p>
			<p>
				<b>Cancelada</b> por
				${(movimentacao.movCanceladora.cadastrante.descricaoIniciaisMaiusculas)!}
				(${(movimentacao.movCanceladora.lotaCadastrante.siglaLotacao)!})
			</p>
			<p>Movimenta\u00e7\u00e3o havia sido realizada por
				${(movimentacao.cadastrante.descricaoIniciaisMaiusculas)!}
				(${(movimentacao.lotaCadastrante.siglaLotacao)!})
			</p>
		</blockquote>
		<p>
			Para acessar a solicita\u00e7\u00e3o, clique <a href="${link}">aqui</a>.
		</p>
	</body>
</html>