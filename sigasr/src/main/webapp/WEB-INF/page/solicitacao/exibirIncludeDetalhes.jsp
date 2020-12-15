<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<p>
	<c:if test="${param.exibirEtapas}">
		<input type="radio" name="exibirEtapas" value="false" id="radioMovs" onclick="exibirMovs();" />&nbsp;Movimenta&ccedil;&otilde;es&nbsp;
		<input type="radio" name="exibirEtapas" value="true" id="radioEtapas" onclick="exibirEtapas();" />&nbsp;Atendimentos&nbsp;&nbsp;
	</c:if>
	<span id="todoOContexto">
		<c:if test="${solicitacao.parteDeArvore}">
			<siga:checkbox name="todoOContexto" value="${todoOContexto}" onchange="postback();"></siga:checkbox> Todo o Contexto
                 	&nbsp;&nbsp;
        			</c:if>
        		</span>
	<span id="maisDetalhes">
		<siga:checkbox name="ocultas" value="${ocultas}" onchange="postback();"></siga:checkbox>
		Mais Detalhes</span>
</p>
	
	
<div class="table-responsive" id="movs">
	<table border="0" class="table">
		<thead>
			<tr>
				<th>Momento</th>
				<c:if test="${todoOContexto}">
				<th></th>
				</c:if>
				<th>Evento</th>
				<th colspan="2">Cadastrante</th>
				<th colspan="2">Atendente</th>
				<th>Descri&ccedil;&atilde;o</th>
			</tr>
		</thead>

		<tbody>
			<c:choose>
				<c:when test="${movs != null && !movs.isEmpty()}">
					<c:forEach items="${movs}" var="movimentacao">
						<tr <c:if test="${movimentacao.canceladoOuCancelador}"> class="disabled" </c:if>>
							<c:choose>
								<c:when test="${ocultas}">
									<td>${movimentacao.dtIniMovDDMMYYYYHHMM}</td>
								</c:when>
								<c:otherwise>
									<td>${movimentacao.dtIniString}</td>
								</c:otherwise>
							</c:choose>

							<c:if test="${todoOContexto}">
								<td>
								     <c:if test="${movimentacao.solicitacao.filha}">
									     <a style="color: #365b6d;" 
									        href="${linkTo[SolicitacaoController].exibir(movimentacao.solicitacao.siglaCompacta)}" 
									        style="text-decoration: none">
											${movimentacao.solicitacao.numSequenciaString} </a>
								     </c:if>
								</td>
							</c:if>
							<td>${movimentacao.tipoMov.nome}</td>
							<td><siga:selecionado
									sigla="${movimentacao.lotaTitular.lotacaoAtual.siglaLotacao}"
									descricao="${movimentacao.lotaTitular.lotacaoAtual.nomeLotacao}"></siga:selecionado>
							</td>
							<td><siga:selecionado
									sigla="${movimentacao.cadastrante.nomeAbreviado}"
									descricao="${movimentacao.cadastrante.descricaoIniciaisMaiusculas}"></siga:selecionado>
							</td>
							<td><siga:selecionado
									sigla="${movimentacao.lotaAtendente.lotacaoAtual.siglaLotacao}"
									descricao="${movimentacao.lotaAtendente.lotacaoAtual.nomeLotacao}"></siga:selecionado>
							</td>
							<td><siga:selecionado
									sigla="${movimentacao.atendente.nomeAbreviado}"
									descricao="${movimentacao.atendente.descricaoIniciaisMaiusculas}"></siga:selecionado>
							</td>
							<td id="descrMovimentacao${movimentacao.idMovimentacao}" class="movimentacao-descricao">
							    <c:choose>
							    <%-- Edson: se a descrição do fechamento for da nova versão... Depois, melhorar isto --%>
							    <c:when test="${movimentacao.descricaoAtomica}">
							    	<c:if test="${not empty movimentacao.descricao}">
							    		<p id="descrMovimentacaoTexto${movimentacao.idMovimentacao}">${movimentacao.descricao}</p>
							    	</c:if>
							    	<c:if test="${not empty movimentacao.motivoFechamento}">
								    	<p><i>Motivo do fechamento:</i> ${movimentacao.motivoFechamento.descrTipoMotivoFechamento}</p>
							    	</c:if>
							    	<%-- Fechamento, reclassificação, escalonamento,  --%>
							    	<c:if test="${movimentacao.tipoMov.idTipoMov == 7
							    					|| movimentacao.tipoMov.idTipoMov == 23 
							    					|| movimentacao.tipoMov.idTipoMov == 24}">
								    	<p><i>Item:</i> ${movimentacao.itemConfiguracao.descricaoCompleta}</p>
								    	<p><i>A&ccedil;&atilde;o:</i> ${movimentacao.acao.tituloAcao}</p>
							    	</c:if>
							    	<c:if test="${not empty movimentacao.conhecimento}">
								    	<p><i>Conhecimento:</i> <a target="${movimentacao.conhecimento}" href="/sigagc/app/exibir?sigla=${movimentacao.conhecimento}"> ${movimentacao.conhecimento}</a></p>
							    	</c:if>
								    <c:if test="${not empty movimentacao.motivoEscalonamento}">
								    	<p><i>Motivo do escalonamento:</i> ${movimentacao.motivoEscalonamento.descrTipoMotivoEscalonamento}</p>
								    </c:if>
								    <c:if test="${not empty movimentacao.motivoPendencia}">
								    	<p><i>Motivo da pendência:</i> ${movimentacao.motivoPendencia.descrTipoMotivoPendencia}</p>
								    </c:if>
								    <c:if test="${not empty movimentacao.dtAgenda}">
								    	<p><i>Fim previsto:</i> ${movimentacao.dtAgendaDDMMYYYYHHMM}</p>
								    </c:if>
								    <%-- Andamento --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 2 && movimentacao.trocaDePessoaAtendente}">
								    	<c:choose>
								    		<c:when test="${not empty movimentacao.atendente}">
								    			<p><i>Atribuindo a:</i> ${movimentacao.atendente.nomeAbreviado}</p>
								    		</c:when>
								    		<c:otherwise>
								    			<p><i>Retirando atribuição</i></p>
								    		</c:otherwise>
								    	</c:choose>
								    </c:if>
								    <%-- Término de pendência --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 11}">
								    	<p><i>Terminando pendencia iniciada em:</i> ${movimentacao.movFinalizada.dtIniMovDDMMYYYYHHMM}</p>
								    </c:if>
								    <%-- Juntada --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 18}">
								    	<p><i>Juntando a:</i> <a target="${movimentacao.solicitacaoReferencia.codigo}" href="/sigasr/app/solicitacao/exibir/${movimentacao.solicitacaoReferencia.siglaCompacta}"> ${movimentacao.solicitacaoReferencia.codigo}</a></p>
								    </c:if>
								    <%-- Vinculação --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 20}">
								    	<p><i>Veja também:</i> <a target="${movimentacao.solicitacaoReferencia.codigo}" href="/sigasr/app/solicitacao/exibir/${movimentacao.solicitacaoReferencia.siglaCompacta}"> ${movimentacao.solicitacaoReferencia.codigo}</a></p>
								    </c:if>
								    <%-- Desentranhamento --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 19}">
								    	<p><i>Desentranhando da solicitação:</i> <a target="${movimentacao.movFinalizada.solicitacaoReferencia.codigo}" href="/sigasr/app/solicitacao/exibir?sigla=${movimentacao.solicitacaoReferencia.codigo}"> ${movimentacao.solicitacaoReferencia.codigo}</a></p>
								    </c:if>
								    <%-- Alteração de prioridade --%>
								    <c:if test="${movimentacao.tipoMov.idTipoMov == 21}">
								    	<p><i>Prioridade Técnica:</i> ${movimentacao.prioridade.descPrioridade}</p>
								    </c:if>
								    <%-- Avaliação --%>
									<c:if test="${movimentacao.tipoMov.idTipoMov == 16}">
										<c:forEach items="${movimentacao.respostaSet}" var="resposta">
											<c:if test="${resposta.pergunta.tipoPergunta.idTipoPergunta == 1}">
												<b>- ${resposta.pergunta.descrPergunta}:</b> ${resposta.descrResposta}
                                                 	</c:if>
											<c:if test="${resposta.pergunta.tipoPergunta.idTipoPergunta == 2}">
												<b>- ${resposta.pergunta.descrPergunta}:</b> ${resposta.grauSatisfacao}
                                                 	</c:if>
										</c:forEach>
									</c:if>
									<c:if test="${movimentacao.arquivo != null}">
										<p><i>Arquivo:</i> <sigasr:arquivo arquivo="${movimentacao.arquivo}" /></p>
									</c:if>
								</c:when>
								<c:otherwise>
									<span id="descrMovimentacaoTexto${movimentacao.idMovimentacao}">${movimentacao.descrMovimentacao}
							    	</span>
								</c:otherwise>
								</c:choose>
							</td>
							<script language="javascript">
								parseDescricao('descrMovimentacaoTexto${movimentacao.idMovimentacao}');
							</script>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td align="center" colspan="10">N&atilde;o h&aacute;
							movimenta&ccedil;&otilde;es nesse modo de
							exibi&ccedil;&atilde;o</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>