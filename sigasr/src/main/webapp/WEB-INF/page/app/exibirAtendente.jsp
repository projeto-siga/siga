<c:choose>
<c:when test="${movimentacao.proxAtendenteAlteravel}">
	<c:set var="disabled" value="false" />
</c:when>
<c:otherwise>
	<c:set var="disabled" value="true" />
</c:otherwise>
</c:choose>
<sigasr:pessoaLotaSelecao2 nomeSelLotacao="movimentacao.lotaAtendente"
	nomeSelPessoa="movimentacao.atendente"
	valuePessoa="${movimentacao.atendente}"
	valueLotacao="${movimentacao.lotaAtendente}"
	disabled="${disabled}" />