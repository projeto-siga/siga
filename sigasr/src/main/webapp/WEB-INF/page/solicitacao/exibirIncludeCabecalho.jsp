<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<h2>${solicitacao.codigo}</h2>
<p></p>
<h3>
	${solicitacao.getMarcadoresEmHtml(titular, lotaTitular)}
	<c:if test="${solicitacao.solicitacaoPrincipalJuntada != null}"> -
             <a style="text-decoration: none"
			href="${linkTo[SolicitacaoController].exibir(solicitacao.solicitacaoPrincipalJuntada.siglaCompacta)}">
			${solicitacao.solicitacaoPrincipalJuntada.codigo} </a>
	</c:if>
</h3>

<sigasr:linkSr acoes="${solicitacao.operacoes(titular, lotaTitular)}" />
			


 