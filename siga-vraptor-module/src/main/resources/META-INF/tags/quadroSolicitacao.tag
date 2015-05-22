<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="solicitacoes" required="true"%>
<%@ attribute name="modoCompleto" required="false"%>

<!--%{ if (_solicitacoes == null){ _solicitacoes = new ArrayList();
_solicitacoes.add(_solicitacao); } }%-->
<div class="gt-content-box">
	<table border="0" class="gt-table gt-user-table">
		<tbody>
			<c:forEach items="${solicitacoes}" var="sol">
			<tr><td style="width: 70%">
					<h4>
						<c:if test="${modoCompleto == null}">
							<a href="${linkTo[SolicitacaoController].exibir("${sol.idSolicitacao}")}">
								${sol.codigo} -
						</c:if>
						${sol.itemConfiguracao?.descricao}&nbsp;-&nbsp;${sol.acao?.descricao}
						<c:if test="${modoCompleto == null}">
							</a>
						</c:if>
					</h4>
					<!--  <p></p> -->
					<p style="font-size: 9pt">
							<i>Abertura:${sol.solicitante.descricaoIniciaisMaiusculas},
								${sol.lotaSolicitante.siglaLotacao}
								<c:if test="${sol.local != null}">
									&nbsp;(${sol.local.nomeComplexo})
								</c:if>
								, Tel.: ${sol.telPrincipal}
								- ${sol.solicitacaoInicial.dtRegString}
							</i>
						<c:if test="${modoCompleto != null && sol.solicitante != sol.cadastrante}"
							<i>Cadastrado por ${sol.cadastrante.descricaoIniciaisMaiusculas},
								${sol.lotaCadastrante.siglaLotacao}</i>
						</c:if>
					<!--  </p> -->
					<p style="font-size: 9pt; padding-top: 3pt" id="descrSolicitacao">
					${sol.descrSolicitacao}<br/><!--<b>${sol.atributosString}</b>--></p>
					<script language="javascript">
						var descricao = document.getElementById('descrSolicitacao');
						descricao.innerHTML = descricao.innerHTML.replace(/\n\r?/g, '<br />');
					</script>
					<p style="font-size: 9pt">
							<i>Última Movimentação:${sol.ultimaMovimentacao.solicitacao.solicitante.descricaoIniciaisMaiusculas},
							${sol.lotaSolicitante.siglaLotacao}
							<c:if test="${sol.local != null}">
								&nbsp;(${sol.local.nomeComplexo})
							</c:if>
							Tel.: ${sol.telPrincipal} - ${sol.ultimaMovimentacao.dtIniString}</i>
							<br>
							${sol.ultimoMovimentacao.descrMovimentacao}
				</td>
				<td style="padding-left: 60px">
					<!-- Categories Sub Table -->
					<table border="0" class="gt-table-categories">
						<tr>
							<th>Situação:</th>
							<td>${sol.situacaoString}</td>
						<!-- </td> -->	
						</tr>
						<c:choose>
							<c:when test="${sol.GUT > 80}">
								<c:set var="priorColor" value="'color: red'"/>
							</c:when>
							<c:when test="${sol.GUT > 60}">
								<c:set var="priorColor" value="'color: orange'"/>
							</c:when>
							<c:otherwise>
								<c:set var="priorColor" value="''"/>
							</c:otherwise>
						</c:choose>
						<tr >
							<th style="color: ${priorColor}">Prioridade:</th> 
							<td width="60%" style="${priorColor}" >${sol.GUTPercent}
							<c:if test="${modoCompleto != null}"> &nbsp;&nbsp;${sol.GUTString}</c:if>
							</td>
						</tr>
						<c:if test="${sol.arquivo != null}">
						<tr>
							<th>Anexo:</th>
							<td><siga:arquivo arquivo="${sol.arquivo}"/></td>
						<!-- </td> -->	
						</tr>
						</c:if>
						<c:if test="${modoCompleto != null && sol.parteDeArvore != null}"> 
							<tr>
								<th>Contexto:</th>
								<td><siga:listaArvore solicitacao="${sol.paiDaArvore}" visualizando="${sol}"/></td>
							<!-- </td> -->	
							</tr>
						</c:if>
					</table>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
