<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="${solicitacao.codigo}">
	<jsp:include page="../main.jsp"></jsp:include>

	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/cronometro.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/atributo.editavel.js"></script>

	<style>
		ul.lista-historico li span {
			text-decoration: line-through;
		}
		
		ul.lista-historico li {
			list-style: none;
		}
		
		ul.lista-historico li.unico {
			margin-left: 0px !important;
		}
		
		button.button-historico {
			padding-left: 2px;
			padding-right: 2px;
			width: 16px;
		}
		
		.historico-label {
			font-weight: bold;
			margin-right: 4px;
		}
		
		.hidden {
			display: none;
		}	
		

	</style>

	<div class="gt-bd gt-cols clearfix" style="padding-bottom: 0px;">
		<div class="gt-content">
			<h2>${solicitacao.codigo}</h2>
			<p></p>
			<h3>
				${solicitacao.getMarcadoresEmHtml(titular, lotaTitular)}
				<c:if test="${solicitacao.solicitacaoPrincipalJuntada != null}"> -
                <a style="text-decoration: none"
						href="${linkTo[SolicitacaoController].exibir[solicitacao.solicitacaoPrincipalJuntada.siglaCompacta]}">
						${solicitacao.solicitacaoPrincipalJuntada.codigo} </a>
				</c:if>
			</h3>

			<sigasr:linkSr acoes="${solicitacao.operacoes(titular, lotaTitular)}" />
			<div class="gt-content-box" style="padding: 10px">
				<p style="font-size: 11pt; font-weight: bold; color: #365b6d;">
					${solicitacao.descrItemAtual}
					-
					${solicitacao.descrAcaoAtual}
				</p>
				<p id="descrSolicitacao" style="font-size: 9pt;">${solicitacao.descricao}</p>
				<script language="javascript">

					function formatStr(str, n) {
					   var strTmp = [], start=0;
					   while(start<str.length) {
						   strTmp.push(str.slice(start, start+n));
					      start+=n;
					   }
					   return strTmp.join(" ");
					};
					
					function parseDescricao(id){
						var descricao = document.getElementById(id);
						if (!descricao)
							return;
						
					    var wordsArray = descricao.innerHTML.split(/(\s+)/);
					    var temStringLonga = false;
						for(var i=0; i < wordsArray.length; i++){						
				    		if (wordsArray[i].length > 50 && wordsArray[i].substr(0,7) != "http://" && wordsArray[i].substr(0,8) != "https://") {
				    			wordsArray[i] = formatStr(wordsArray[i],60);
				    			temStringLonga = true;	    			
							}
						}
						if(temStringLonga){
							descricao.innerHTML = wordsArray.join(" ");
						}																			
						descricao.innerHTML = descricao.innerHTML.replace(/\n\r?/g, ' <br />');
						descricao.innerHTML = descricao.innerHTML.replace(/(\w{2,4}\-(GC|SR)\-\d{4}\/\d{5}(?:\.\d{2})?)/g, function(a, b, c){
							if (c.toLowerCase() == 'sr')
								return '<a href=\'/sigasr/app/solicitacao/exibir/'+ a.replace('/','') + '\'>' + a + '</a>';
							else
								return '<a href=\'/sigagc/app/exibir/' + a.replace('/','') + '\'>' + a + '</a>';;
						});
						descricao.innerHTML = descricao.innerHTML.replace(/(https?:\/\/.*?(?=[.,;""''?!)]*$|[.,;""''?!)]*[\s\n]))/g, function(a, b){
							return '<a href="' + b + '">' + b + '</a>' + a.replace(b, '');
						});
					}
					parseDescricao('descrSolicitacao');
				</script>
				<c:forEach items="${atributos}" var="att">
					<c:if test="${att.valorAtributoSolicitacao != null && !att.valorAtributoSolicitacao.isEmpty()}">
						<div>
							<p style="float: left; font-size: 9pt; padding: 0px"><b>${att.atributo.nomeAtributo}: &nbsp</b></p>
							<div class="atributo-editavel">
								<c:if test="${solicitacao.podeEditarAtributo(titular, lotaTitular)}">
									<div class="gt-table-action-list" style="float:right;">
										<a href="#" onclick="editarAtributo('${att.id}', '${att.atributo.tipoAtributo.name()}', '${att.atributo.preDefinidoSet}');">
											<img src="/siga/css/famfamfam/icons/pencil.png" title="editar" style="width: 40%; margin-left: 20px;" />
										</a>		
										<span class="gt-separator">|</span>			
										<a href="#" onclick="excluirAtributo('${att.id}', '${att.atributo.nomeAtributo}');">
											<img src="/siga/css/famfamfam/icons/delete.png" title="excluir" style="width: 80%;"/>
										</a>
									</div>
								</c:if>
								<p style="font-size: 9pt;">
									<span class="valor-atributo">${att.valorAtributoSolicitacao}</span>
								</p>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>

			<br /> <br />
			<div class="gt-content-box gt-form"
				style="margin-bottom: 0px !important">
				<form action="${linkTo[SolicitacaoController].darAndamento}"
					method="post" onsubmit="javascript: return block();"
					enctype="multipart/form-data">
					<input type="hidden" name="todoOContexto" value="${todoOContexto}" />
					<input type="hidden" name="ocultas" value="${ocultas}" /> <input
						type="hidden" name="movimentacao.solicitacao.idSolicitacao"
						value="${movimentacao.solicitacao.idSolicitacao}" />

					<c:choose>
						<c:when test="${solicitacao.podeTrocarAtendente(titular, lotaTitular)}">
							<div class="gt-form-row">
								<label>Atendente</label>
								<div id="divAtendente">
									<select name="movimentacao.atendente.id" id="selectActendente">
	            						<option value=""></option>
										<c:if test="${atendentes.size() >= 1}">
											<c:forEach items="${atendentes}" var="pessoa">
												<option value="${pessoa.pessoaAtual.idPessoa}" ${movimentacao.atendente.idInicial.equals(pessoa.idInicial) ? 'selected' : ''}>${pessoa.pessoaAtual.descricaoIniciaisMaiusculas}</option>
											</c:forEach>
										</c:if>
										<c:set var="substitutos" value="${solicitacao.substitutos}" />
										<c:if test="${substitutos.size() >= 1}">
											<optgroup label="Substitutos">
												<c:forEach items="${substitutos}" var="pessoaSubst">
													<option value="${pessoaSubst.substituto.pessoaAtual.idPessoa}" ${movimentacao.atendente.idInicial.equals(pessoaSubst.substituto.idInicial) ? 'selected' : ''}>${pessoaSubst.substituto.pessoaAtual.descricaoIniciaisMaiusculas}</option>
												</c:forEach>
											</optgroup>
										</c:if>
									</select>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="movimentacao.atendente.id" value="${movimentacao.solicitacao.atendente.pessoaAtual.idPessoa}" />
						</c:otherwise>
					</c:choose>
					<div style="display: inline" class="gt-form-row gt-width-66">
						<label>Pr&oacute;ximo Andamento</label>
						<textarea style="width: 100%"
							name="movimentacao.descrMovimentacao" id="descrSolicitacao"
							cols="70" rows="4" value="${movimentacao.descrMovimentacao}"></textarea>
					</div>

					<div class="gt-form-row">
						<input type="submit" value="Gravar"
							class="gt-btn-medium gt-btn-left" />
					</div>
				</form>
			</div>
			<c:set var="exibirEtapas" value="${solicitacao.jaFoiAtendidaPor(titular, lotaTitular, true) || exibirMenuAdministrar}" />
			<p style="padding-top: 30px; font-weight: bold; color: #365b6d;">
				<c:if test="${exibirEtapas}">
					<input type="radio" name="exibirEtapas" value="false" id="radioMovs" onclick="exibirMovs();" />&nbsp;Movimenta&ccedil;&otilde;es&nbsp;
					<input type="radio" name="exibirEtapas" value="true" id="radioEtapas" onclick="exibirEtapas();" />&nbsp;Atendimentos&nbsp;&nbsp;
				</c:if>
				<span id="todoOContexto">
					<c:if test="${solicitacao.parteDeArvore}">
						<siga:checkbox name="todoOContexto" value="${todoOContexto}" onchange="postback();"></siga:checkbox> Todo o Contexto
                    	&nbsp;&nbsp;
           			</c:if>
           		</span>
				<span id="maisDetalhes"><siga:checkbox name="ocultas" value="${ocultas}" onchange="postback();"></siga:checkbox>
				Mais Detalhes</span>
			</p>
			<div class="gt-content-box" id="movs">
				<table border="0" width="100%" class="gt-table mov">
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
			<c:if test="${exibirEtapas}">
			<div class="gt-content-box" id="etapas">
				<script>
					function clica(obj, force){
						var jObj = $(obj);

						//Se clicou no + geral...
						if (obj.id == 'sinal' ){
							$("a[id^=sinal]").not("#sinal").each(function(o){
								clica(this, jObj.html());
							})
						}
						
						if ((!force && jObj.html() == '+') || force == '+'){
							jObj.parent().parent().next().show();
							jObj.html('-');
						} else {
							jObj.parent().parent().next().hide();
							jObj.html('+');
						}
					}
				</script>
				<table border="0" width="100%" class="gt-table mov">
					<thead>
						<tr>
							<th><a href="" onclick="clica(this); return false;" style="text-decoration: none;" id="sinal">+</a></th>
							<th>Etapa</th>
							<c:if test="${todoOContexto}">
								<th>Solicita&ccedil;&atilde;o</th>
							</c:if>
							<th>Equipe</th>
							<th>Início</th>
							<th>Fim</th>
							<th>Decorrido</th>
							<th>Restante</th>
						</tr>
					</thead>

					<tbody>
						<c:choose>
							<c:when test="${not empty etapas}">
								<c:forEach items="${etapas}" var="etapa">
									<tr>
										<td><a href="" onclick="clica(this); return false;" style="text-decoration: none;" id="sinal${etapa.id}">+</a></td>
										<td>${etapa.descricao}</td>
										<c:if test="${todoOContexto}">
											<td>
											     <c:if test="${etapa.solicitacao.filha}">
												     <a style="color: #365b6d;" 
												        href="${linkTo[SolicitacaoController].exibir(etapa.solicitacao.siglaCompacta)}" 
												        style="text-decoration: none">
														${etapa.solicitacao.numSequenciaString} </a>
											     </c:if>
											</td>
										</c:if>
										<td>${not empty etapa.lotaResponsavel ? etapa.lotaResponsavel : ''}</td>
										<td>${etapa.inicioString}</td>
										<td>${etapa.fimString}</td>
										<td class="crono resumido decorrido ${etapa.ativo ? 'ligado' : 'desligado'}">
											<nobr><span class="descrValor"></span><span class="valor">${etapa.decorridoEmSegundos}</span></nobr>
										</td>
										<td class="crono resumido restante ${etapa.ativo ? 'ligado' : 'desligado'}">
											<nobr><span class="descrValor"></span><span class="valor">${etapa.restanteEmSegundos}</span></nobr>
										</td>
									</tr>
									<tr style="display: none">
										<c:set var="haDetalhes" value="false" />
										<td></td>
										<td></td>
										<td colspan="6">
											<c:if test="${not empty etapa.paramAcordo}">
												<c:set var="haDetalhes" value="true" />
												<p><b>Acordo:</b> ${etapa.paramAcordo.acordo.nomeAcordo}</p>
											</c:if>
											<c:if test="${not empty etapa.item}">
												<c:set var="haDetalhes" value="true" />
												<p><b>Item de Configuração:</b> ${etapa.item.sigla} - ${etapa.item.descricao}</p>
											</c:if>
											<c:if test="${not empty etapa.acao}">
												<c:set var="haDetalhes" value="true" />
												<p><b>Ação:</b> ${etapa.acao.sigla} - ${etapa.acao.descricao}</p>
											</c:if>
											<c:if test="${not empty etapa.prioridade}">
												<c:set var="haDetalhes" value="true" />
												<p><b>Prioridade Técnica:</b> ${etapa.prioridade.descPrioridade}</p>
											</c:if>
										</td>
										<c:if test="${not haDetalhes}">
											<script>$("#sinal${etapa.id}").remove();</script>
										</c:if>
											
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="10">N&atilde;o h&aacute;
										atendimentos nesse modo de
										exibi&ccedil;&atilde;o</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<script>
				function exibirMovs(){
					$("#movs").show();
					$("#maisDetalhes").show();
					$("#todoOContexto").show();
					$("#etapas").hide();
				}
				function exibirEtapas(){
					$("#etapas").show();
					$("#movs").hide();
					$("#maisDetalhes").hide();
					$("#todoOContexto").show();
				}
				$("#radioMovs").trigger("click");
			</script>
			</c:if>
		</div>

		<div class="gt-sidebar">
			<jsp:include page="exibirCronometro.jsp"></jsp:include>
			<jsp:include page="exibirPendencias.jsp"></jsp:include>
		
			<div class="gt-sidebar-content">
				<h3>Solicita&ccedil;&atilde;o</h3>
				<p>
					<b>Solicitante:</b>
					${solicitacao.solicitante.descricaoIniciaisMaiusculas},
					${solicitacao.solicitante.siglaCompleta},
					${solicitacao.solicitante.funcaoStringIniciaisMaiusculas},
					${solicitacao.solicitante.lotacao.siglaCompleta},
                    ${solicitacao.local.nomeComplexo}
				</p>
				<c:if test="${solicitacao.interlocutor != null}">
					<p>
						<b>Interlocutor:</b>
						${solicitacao.interlocutor.descricaoIniciaisMaiusculas}
					</p>
				</c:if>
				<c:if test="${solicitacao.dtOrigem != null}">
					<p>
						<b>Contato Inicial:</b> ${solicitacao.dtOrigemString}
						<c:if test="${solicitacao.meioComunicacao != null}">
                        por ${solicitacao.meioComunicacao.descrMeioComunicacao}
                    </c:if>
					</p>
				</c:if>
				<c:if test="${solicitacao.telPrincipal != null}">
					<p>
						<b>Telefone:</b> ${solicitacao.telPrincipal}
					</p>
				</c:if>
				<c:if test="${solicitacao.endereco != null}">
					<p>
						<b>Endereço:</b> ${solicitacao.endereco}
					</p>
				</c:if>
				<p>
					<b>Cadastrante:</b>
					${solicitacao.cadastrante.descricaoIniciaisMaiusculas},
					${solicitacao.lotaTitular.siglaLotacao}
				</p>
				<p><b>Cadastrado em:</b> ${solicitacao.dtRegDDMMYYYYHHMM}</p>
				<p>
					<b>Item de Configura&ccedil;&atilde;o:</b> ${solicitacao.descrItemAtualCompleta}
				</p>
				<p>
					<b>A&ccedil;&atilde;o:</b> ${solicitacao.descrAcaoAtualCompleta}
				</p>
				<p>
					<b>Prioridade:</b> <span style="">${solicitacao.prioridade.descPrioridade}</span>
				</p>
				<c:if test="${solicitacao.prioridadeTecnica != solicitacao.prioridade}">
					<p>
						<b>Prioridade T&eacute;cnica:</b> <span style="">${solicitacao.prioridadeTecnica.descPrioridade}</span>
					</p>
				</c:if>
				<p>
					<b>Notifica&ccedil;&atilde;o:</b>
					${solicitacao.formaAcompanhamento.descrFormaAcompanhamento}
				</p>
				<c:if
					test="${solicitacao.isFechadoAutomaticamente() != null && solicitacao.isPai()}">
					<p>
						<b>Fechamento Autom&aacute;tico:</b>
						${solicitacao.isFechadoAutomaticamente() ? "Sim" : "Não"}
					</p>
				</c:if>
			</div>

		<c:if test="${vinculadas != null && !vinculadas.isEmpty()}">
				<div class="gt-sidebar-content">
					<h3>Veja Tamb&eacute;m</h3>
					<p>
						<c:forEach items="${vinculadas}" var="vinculada">
							<a
								href="${linkTo[SolicitacaoController].exibir(vinculada.siglaCompacta)}">
								${vinculada.codigo} </a>
							<br />
						</c:forEach>
					</p>
				</div>
		</c:if>
		<c:if test="${solicitacao.parteDeArvore}">
				<div class="gt-sidebar-content">
					<h3>Contexto</h3>
					<p>
						<sigasr:listaArvore solicitacao="${solicitacao.paiDaArvore}"
							visualizando="${solicitacao}" />
					</p>
				</div>
		</c:if>
		<c:if test="${not empty arqs}">
				<div class="gt-sidebar-content">
					<h3>Arquivos Anexos</h3>
					<p>
						<c:forEach items="${arqs}" var="anexacao">
							<sigasr:arquivo arquivo="${anexacao}" descricao="sim"/> 
							<br />
                    	</c:forEach>
                	</p>
	            </div>
	    </c:if>

	    <c:if test="${juntadas != null && !juntadas.isEmpty()}">
	            <div class="gt-sidebar-content">
	                <h3>Solicita&ccedil;&otilde;es juntadas</h3>
	                <p>
	                    <c:forEach items="${juntadas}" var="juntada">
	                        <a href="${linkTo[SolicitacaoController].exibir(juntada.siglaCompacta)}">
	                        ${juntada.codigo} </a> <br/> 
	                    </c:forEach>
	                </p>
	            </div>
	    </c:if>
	    <c:if test="${not empty listas}">
	            <div class="gt-sidebar-content">
	                <h3>Listas de Prioridade</h3>
	                    <c:forEach items="${listas}" var="listas">
	                        <p>
	                            <input type="hidden" name="idlista"
	                            value="${listas.idLista}" /> <a
	                            style="color: #365b6d; text-decoration: none"
	                            href="${linkTo[SolicitacaoController].exibirLista[listas.idLista]}">
	                                ${listas.listaAtual.nomeLista} </a>
	                        </p>
	                    </c:forEach>
	            </div>
	    </c:if>
  
	    <div id="divConhecimentosRelacionados">
	        <jsp:include page="exibirConhecimentosRelacionados.jsp"></jsp:include>
	    </div>
    </div>
    </div>
    <sigasr:modal nome="anexarArquivo" titulo="Anexar Arquivo">
        <div class="gt-content-box gt-form">
            <form action="${linkTo[SolicitacaoController].anexarArquivo}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">               
                <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
                <input type="hidden" name="ocultas" value="${ocultas}" />
                <input type="hidden" name="movimentacao.atendente.id" value="${movimentacao.solicitacao.atendente.pessoaAtual.idPessoa}" />
                <input type="hidden" name="movimentacao.solicitacao.idSolicitacao"
                    value="${solicitacao.idSolicitacao}" /> 
                <input type="hidden" name="movimentacao.tipoMov.idTipoMov" value="12" />
                <input type="hidden" name="movimentacao.tipoMov.nome" value="Anexação de Arquivo" />
                <div class="gt-form-row">
                    <label>Arquivo</label> <input type="file" name="movimentacao.arquivo" />
                </div>
                <div style="display: inline" class="gt-form-row gt-width-66">
                    <label>Descri&ccedil;&atilde;o</label>
                    <textarea style="width: 100%" name="movimentacao.descrMovimentacao"
                        id="descrSolicitacao" cols="70" rows="4"></textarea>
                </div>
                <div class="gt-form-row">
                    <input type="submit" value="Gravar"
                        class="gt-btn-medium gt-btn-left" />
                </div>
            </form>
        </div>
    </sigasr:modal> 
    <sigasr:modal nome="fechar" titulo="Fechar" url="${linkTo[SolicitacaoController].fechar}?solicitacao.codigo=${solicitacao.siglaCompacta}" />
    
    <sigasr:modal nome="incluirEmLista" titulo="Definir Lista" url="${linkTo[SolicitacaoController].incluirEmLista}?sigla=${solicitacao.siglaCompacta}" />
    
    <sigasr:modal nome="escalonar" titulo="Escalonar Solicitação" url="${linkTo[SolicitacaoController].escalonar}?solicitacao.codigo=${solicitacao.siglaCompacta}" />

	<sigasr:modal nome="reclassificar" titulo="Reclassificar" url="${linkTo[SolicitacaoController].reclassificar}?solicitacao.codigo=${solicitacao.siglaCompacta}" />

    <sigasr:modal nome="juntar" titulo="Juntar">
        <form action="${linkTo[SolicitacaoController].juntar}" method="post" enctype="multipart/form-data" id="formGravarJuncao">
            <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
            <input type="hidden" name="ocultas" value="${ocultas}" />
            <input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
            <div style="display: inline; padding-top: 10px;" class="gt-form-row gt-width-66">
                <label>Solicita&ccedil;&atilde;o</label> <br />
                <siga:selecao2 propriedade="solRecebeJuntada" tipo="solicitacao" tema="simple" modulo="sigasr" onchange="validarAssociacao('Juncao');"
                	tamanho="grande"/>
                <span id="erroSolicitacaoJuncao" style="color: red; display: none;">Solicita&ccedil;&atilde;o n&atilde;o informada.</span>
            </div>
            <div class="gt-form-row gt-width-100" style="padding: 10px 0;">
                <label>Justificativa</label>
                <textarea style="width: 100%;" cols="70" rows="4" name="justificativa" id="justificativaJuncao" maxlength="255" onkeyup="validarAssociacao('Juncao')"></textarea>
                <span id="erroJustificativaJuncao" style="color: red; display: none;"><br />Justificativa n&atilde;o informada.</span>
            </div>
            <div style="display: inline" class="gt-form-row ">
                <input type="button" onclick="gravarAssociacao('Juncao');" value="Gravar" class="gt-btn-medium gt-btn-left" />
            </div>
        </form>
    </sigasr:modal>
    <sigasr:modal nome="vincular" titulo="Vincular">
        <form action="${linkTo[SolicitacaoController].vincular}" method="post" enctype="multipart/form-data" id="formGravarVinculo">
            <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
            <input type="hidden" name="ocultas" value="${ocultas}" />
            <input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
            <div style="display: inline; padding-top: 10px;" class="gt-form-row gt-width-66">
                <label>Solicita&ccedil;&atilde;o</label> <br />
                <siga:selecao2 propriedade="solRecebeVinculo" tipo="solicitacao" tema="simple" modulo="sigasr" onchange="validarAssociacao('Vinculo');"
                	tamanho="grande"/>
                <span id="erroSolicitacaoVinculo" style="color: red; display: none;">Solicita&ccedil;&atilde;o n&atilde;o informada.</span>
            </div>
            <div class="gt-form-row gt-width-100" style="padding: 10px 0;">
                <label>Justificativa</label>
                <textarea style="width: 100%;" cols="70" rows="4" name="justificativa" id="justificativaVinculo" maxlength="255" onkeyup="validarAssociacao('Vinculo')"></textarea>
                <span id="erroJustificativaVinculo" style="color: red; display: none;"><br />Justificativa n&atilde;o informada.</span>
            </div>
            <div style="display: inline" class="gt-form-row ">
                <input type="button" onclick="gravarAssociacao('Vinculo');" value="Gravar" class="gt-btn-medium gt-btn-left" />
            </div>
        </form>
    </sigasr:modal>

    <sigasr:modal nome="associarLista" titulo="Definir Lista" url="associarLista.jsp" />

    <sigasr:modal nome="responderPesquisa" titulo="Responder Pesquisa" url="responderPesquisa?sigla=${solicitacao.siglaCompacta}" />

    <sigasr:modal nome="deixarPendente" titulo="Pendência">
            <div class="gt-content-box gt-form clearfix">
                <form action="${linkTo[SolicitacaoController].deixarPendente}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
                    <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
                    <input type="hidden" name="ocultas" value="${ocultas}" />
                    <div class="gt-form-row gt-width-66">
                        <label>Data de T&eacute;rmino</label>
                        <siga:dataCalendar nome="calendario" id="calendario"/>
                    </div>
                    <div class="gt-form-row gt-width-66">
                        <label>Hor&aacute;rio de T&eacute;rmino</label>
                        <input type="text" name="horario" id="horario" />
                    </div>
                    <div class="gt-form-row gt-width-66">
                        <label>Motivo</label>
                        <siga:select name="motivo" id="motivo" list="motivosPendencia"
	                            listValue="descrTipoMotivoPendencia" theme="simple" isEnum="true"/>
                    </div>
                    <div class="gt-form-row gt-width-66">
                        <label>Detalhamento do Motivo</label>
                        <textarea name="detalheMotivo" cols="50" rows="4"> </textarea>
                    </div>
                    <div class="gt-form-row">
                        <input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> <input
                            type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
                    </div>
                </form>
            </div>
    </sigasr:modal> 
    <sigasr:modal nome="alterarPrioridade" titulo="Alterar Prioridade">
        <div class="gt-form gt-content-box">
            <form action="${linkTo[SolicitacaoController].alterarPrioridade}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
                <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
                <input type="hidden" name="ocultas" value="${ocultas}" />
                <div class="gt-form-row gt-width-33">
					<label>Prioridade</label> 
					<siga:select name="prioridade" id="prioridade" list="prioridadeList" listValue="descPrioridade" listKey="idPrioridade" isEnum="true"
							value="${solicitacao.prioridadeTecnica}" 
							style="width:235px"  />
 				</div>
                <div class="gt-form-row">
                	<input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
                	<input type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
                </div>
            </form>
        </div>
    </sigasr:modal>
    <sigasr:modal nome="desentranhar" titulo="Desentranhar">
        <form action="${linkTo[SolicitacaoController].desentranhar}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
            <div style="display: inline" class="gt-form-row gt-width-66">
                <label>Justificativa</label>
                <textarea style="width: 100%" name="justificativa" cols="50" rows="4"> </textarea>
            </div>
            <input type="hidden" name="completo" value="${completo}" /> <input
                type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> <input
                type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
        </form>
    </sigasr:modal>   
    <sigasr:modal nome="terminarPendenciaModal" titulo="Terminar Pendência">
        <form action="${linkTo[SolicitacaoController].terminarPendencia}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
            <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
            <input type="hidden" name="ocultas" value="${ocultas}" />
            <div style="display: inline" class="gt-form-row gt-width-66">
                <label>Descri&ccedil;&atilde;o</label>
                <textarea style="width: 100%" name="descricao" cols="50" rows="4"> </textarea>
            </div>
            <input
                type="hidden" name="idMovimentacao" id="movimentacaoId" value="" /><input
                type="hidden" name="motivo" id="motivoId" value="" /><input
                type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> <input
                type="submit" value="Gravar" class="gt-btn-medium gt-btn-left" />
        </form>
    </sigasr:modal>    
</siga:pagina>

<script language="javascript">	
	function terminarPendencia(idMov) {
		$("#movimentacaoId").val(idMov);
		$("#terminarPendenciaModal_dialog").dialog("open");
	}

	function validarAssociacao(tipo) {
		$("#erroSolicitacao" + tipo).hide();
		$("#erroJustificativa" + tipo).hide();

		if ((tipo == 'Juncao' && $("#solicitacaoRecebeJuntadaSpan").html() == "")
				|| (tipo == 'Vinculo' && $("#solicitacaoRecebeVinculoSpan")
						.html() == "")) {
			$("#erroSolicitacao" + tipo).show();
			return false;
		}
		if ($("#justificativa" + tipo).val() == "") {
			$("#erroJustificativa" + tipo).show();
			return false;
		}
		return true;
	}

	function gravarAssociacao(tipo) {
		if (!block())
			return false;
		if (validarAssociacao(tipo)) {
			document.getElementById("formGravar" + tipo).submit();
		} else {
			unblock();
	    }
	}
	$('#checksolicitacao.fechadoAutomaticamente').change(
			function() {
				if (this.checked) {
					$('#checksolicitacao.fechadoAutomaticamente').prop('value',
							'true');
					return;
				}
				$('#checksolicitacao.fechadoAutomaticamente').prop('value',
						'false');
			});

	$(function() {
		$("#horario").mask("99:99");
		$("#horarioReplanejar").mask("99:99");
	});

	function postback() {
		var todoOContexto = ($("#todoOContexto").val() != null ? $(
				"#todoOContexto").val() : false);
		var ocultas = ($("#ocultas").val() != null ? $("#ocultas").val()
				: false);

		location.href = "${linkTo[SolicitacaoController].exibir(solicitacao.siglaCompacta)}?todoOContexto="
				+ todoOContexto + "&ocultas=" + ocultas;
	}

	$("#terminarPendenciaModal_dialog").dialog({
		autoOpen : false,
		height : 'auto',
		width : 'auto',
		modal : true,
		resizable : false
	});
		
	function editarAtributo(idAtributo, tipoAtributo, preDefinidoSet) {
		event.preventDefault();
		var valorAtributo = $(event.target).closest(".atributo-editavel").find(".valor-atributo").text();
		var propriedades = {
			id: idAtributo,
			valor: valorAtributo,
			tipo: tipoAtributo,
			valoresPreDefinidos: preDefinidoSet,
			elemento: event.target,
			nome: 'atributo.valorAtributoSolicitacao',
			urlDestino: '${linkTo[SolicitacaoController].gravarAtributo}'
		};
		var atributoSolicitacao = new AtributoEditavel(propriedades);
		atributoSolicitacao.editar();
	}

	function excluirAtributo(idAtributo, nome) {
		event.preventDefault();
		if (!confirm('Confirmar exclusão do atributo ' + nome))
			return;
		var propriedades = {
			elemento: event.target,
			urlDestino: '${linkTo[SolicitacaoController].excluirAtributo}?id=' + idAtributo
		};
		var atributoSolicitacao = new AtributoEditavel(propriedades);
		atributoSolicitacao.excluir();
	}
</script>
