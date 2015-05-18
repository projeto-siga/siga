<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Exibição de Tópico de Informação">
	<div class="gt-bd gt-cols clearfix">
		<div class="gt-content">
			<h2>${informacao.sigla}</h2>
			<c:if test="${flash.success}">
				<p class="gt-success">${flash.success}</p>
			</c:if>
			<h3 style="margin-bottom: 0px;">${informacao.getMarcadoresEmHtml(titular,lotaTitular)}</h3>
			<!-- Links para as ações de cada mobil -->
			<c:set var="acoes"
				value="${informacao.acoes(idc,titular,lotaTitular)}" />
			<siga:links>
				<c:forEach var="acao" items="${acoes}">
					<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
						pre="${acao.pre}" pos="${acao.pos}" url="${acao.url}"
						test="${true}" popup="${acao.popup}"
						confirm="${acao.msgConfirmacao}"
						estilo="line-height: 160% !important" />
				</c:forEach>
			</siga:links>

			<!-- Dados do documento -->
			<div class="gt-content-box" style="padding: 10px;">
				<h2>${informacao.arq.titulo}</h2>
				${informacao.conteudoHTML}
			</div>
		</div>

		<div class="gt-sidebar">
			<div class="gt-sidebar-content">
				<h3>${informacao.tipo.nome}</h3>
				<p>
					<b>Tipo: </b> ${informacao.tipo.nome}
				</p>
				<p>
					<b>Órgão Usuário:</b> ${informacao.ou.acronimoOrgaoUsu}
				</p>
				<p>
					<b>Visualização:</b> ${informacao.visualizacao.nome}
				</p>
				<p>
					<b>Edição:</b> ${informacao.edicao.nome}
				</p>
				<p>
					<b>Autor:</b> ${informacao.autor.descricaoIniciaisMaiusculas} -
					${informacao.autor.sigla}
				</p>
				<p>
					<b>Lotação:</b> ${informacao.lotacao.descricaoIniciaisMaiusculas} -
					${informacao.lotacao.sigla}
				</p>
				<p>
					<b>Data de criação:</b> ${informacao.dtIniString}
				</p>
				<p>
					<b>Finalizado em:</b> ${informacao.elaboracaoFim}
				</p>
				<!--  -->
				<c:set var="cls"
					value="${informacao.arq.classificacao.split(&quot;,&quot;)}" />
				<!--  -->
				<c:if test="${not empty cls}">
					<p>
						<b>Classificações:</b>
					<ul>
						<c:forEach items="${cls}" var="cl">
							<li>${cl}</li>
						</c:forEach>
					</ul>
					</p>
				</c:if>
				<c:if test="${empty cls}">
					<p>
						<b>Classificação:</b> ${(not empty informacao.arq.classificacao) ? informacao.arq.classificacao : "Esse conhecimento ainda não possui uma classificação"}
					</p>
				</c:if>
				<c:if test="${informacao.contemArquivos}">
					<h3>Arquivos Anexos</h3>
					<c:forEach items="${informacao.movs}" var="m">
						<c:if
							test="${m.tipo.id == models.GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO && m.movCanceladora == null}">
							<p>
								<img style="margin-bottom: -4px;"
									src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
									target="_blank" href="@{Application.baixar(m.arq.id)}">${m.arq.titulo}</a>
							</p>
						</c:if>
					</c:forEach>
				</c:if>
			</div>

			<div class="gt-sidebar-content" id="gc"></div>

			<!-- / sidebar -->
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".gt-success").delay(5000).fadeOut("slow", "linear");
		});
	</script>

	<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse(
				"knowledge?estilo=sidebar&ts=${currentTimeMillis}${informacao.gcTags}&id=${informacao.id}",
				document.getElementById('gc'));
	</script>

	<!-- Movimentações -->
	<c:if test="${movimentacoes}">
		<div class="gt-bd" style="padding-bottom: 0px;">
			<div class="gt-content">
				<h3 id="ancora_mov">Movimentações</h3>
			</div>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
				<table class="gt-table mov">
					<thead>
						<tr>
							<th align="left" rowspan="2">Data</th>
							<th rowspan="2">Evento</th>
							<th colspan="2" align="left">Cadastrante</th>
							<th colspan="2" align="left">Titular</th>
						</tr>
						<tr>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
s						</tr>
					</thead>
					<tbody>
						<c:forEach var="m" items="${informacao.movs}">
							<c:choose>
								<c:when test="${m.tipo.id == 13}">
									<c:set var="descricao" value="${m.arq.titulo}" />
								</c:when>
								<c:when test="${m.tipo.id == 12 && m.movRef != null}">
									<c:choose>
										<c:when test="${m.movRef.tipo.id==13}">
											<c:set var="descricao"
												value="${m.movRef.tipo.nome + ': ' + m.movRef.arq.titulo}" />
										</c:when>
										<c:when
											test="${m.movRef.tipo.id == 4 || m.movRef.tipo.id == 6}">
											<c:set var="pessoa"
												value="${m.movRef.pessoaAtendente.nomeAbreviado}" />
											<c:set var="lotacao"
												value="${m.movRef.lotacaoAtendente.sigla}" />
											<c:set var="descricao"
												value="${m.movRef.tipo.nome} : ${lotacao} ${pessoa}" />
										</c:when>
										<c:otherwise>
											<c:set var="descricao" value="${m.movRef.tipo.nome}" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:set var="descricao" value="${m.descricao}" />
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${m.movCanceladora != null || m.tipo.id == 12}">
									<c:set var="classe" value="disabled" />
								</c:when>
								<c:otherwise>
									<c:set var="classe" value="" />
								</c:otherwise>
							</c:choose>
							<tr class="juntada ${classe}">
								<td align="left">${m.hisDtIni}</td>
								<c:choose>
									<c:when test="${informacao.podeDesfazer(titular, m)}">
										<td>${m.tipo.nome}[<img
											style="margin-bottom: -2px; width: 11px;"
											src="/siga/css/famfamfam/icons/cross.png" /> <span
											class="gt-table-action-list"> <a
												href="javascript:if (confirm('Deseja desfazer essa movimentação?')) location.href = '${linkTo[AppController].desfazer(informacao.siglaCompacta, m.id)}';">desfazer</a></span>&nbsp;]
										</td>
									</c:when>
									<c:otherwise>
										<td>${m.tipo.nome}</td>
									</c:otherwise>
								</c:choose>
								<td align="left"><span
									title="${m.hisIdcIni.pessoaAtual.lotacao.descricao}">${m.hisIdcIni.pessoaAtual.lotacao.sigla}</span>
								</td>
								<td align="left"><span
									title="${m.hisIdcIni.dpPessoa.descricao}">${m.hisIdcIni.dpPessoa.nomeAbreviado}</span>
								</td>
								<td align="left"><span
									title="${m.lotacaoTitular.descricao}">${m.lotacaoTitular.sigla}</span></td>
								<td align="left"><span title="${m.pessoaTitular.descricao}">${m.pessoaTitular.nomeAbreviado}</span></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>

	<c:if test="${historico}">
		<div class="gt-bd" style="padding-bottom: 0px;">
			<div class="gt-content">
				<h3 id="ancora_his">Histórico de alterações</h3>
			</div>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
				<table class="gt-table mov">
					<thead>
						<tr>
							<th align="center" rowspan="2">Data</th>
							<th colspan="2" align="left">Cadastrante</th>
							<th rowspan="2">Descrição</th>
						</tr>
						<tr>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="m">
							<tr class="juntada ">
								<td align="left">${m.hisDtIni}</td>
								<td align="left"><span
									title="${m.hisIdcIni.pessoaAtual.lotacao.descricao}">${m.hisIdcIni.pessoaAtual.lotacao.sigla}</span>
								</td>
								<td align="left"><span
									title="${m.hisIdcIni.pessoaAtual.descricao}">${m.hisIdcIni.pessoaAtual.nomeAbreviado}</span>
								</td>
								<td style="width: 80%;">
									<!-- Título --> <c:if test="${mapTitulo.containsKey(m)}">
										<h4>${mapTitulo.get(m)}</h4>
									</c:if> <!-- Conteúdo --> <c:if test="${mapTxt.containsKey(m)}">${mapTxt.get(m)}</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<script type="text/javascript">
			$(document).ready(function() {
				$("body").css({
					"overflow-x" : "hidden"
				});
				$("html, body").stop().animate({
					"scrollTop" : $("#ancora_his").offset().top
				}, 900);
				// event.preventDefault();
			});
		</script>
	</c:if>
</siga:pagina>