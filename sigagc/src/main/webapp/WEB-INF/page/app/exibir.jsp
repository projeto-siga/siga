<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="${informacao.sigla}">
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-8">
				<h2>${informacao.sigla}</h2>
				<c:if test="${flash.success}">
					<p class="bg-success">${flash.success}</p>
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
					<a class="btn btn-sm btn-info text-white link-tag"
						href="javascript:visualizaImpressao('knowledgeContent');"> <img
						src="/siga/css/famfamfam/icons/printer.png" title="">Visualizar&nbsp;Impressão
					</a>
				</siga:links>

				<!-- Dados do documento -->

				<div id="knowledgeContent" class="card card-body mb-1 mt-2">
					<h4>${informacao.arq.titulo}</h4>
					${conteudo}
				</div>
			</div>

			<div class="col-sm-4">
				<div class="card-sidebar card border-alert bg-white mb-3">
					<div class="card-header">${informacao.tipo.nome}</div>
					<div class="card-body">
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
						<c:if test="${not empty informacao.grupo}">
							<p>
								<b>Grupo:</b> ${informacao.grupo.siglaGrupo} -
								${informacao.grupo.dscGrupo}
							</p>
						</c:if>
						<p>
							<b>Autor:</b> ${informacao.autor.descricaoIniciaisMaiusculas} -
							${informacao.autor.sigla}
						</p>
						<p>
							<b>Lotação:</b> ${informacao.lotacao.descricaoIniciaisMaiusculas}
							- ${informacao.lotacao.sigla}
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
					</div>
				</div>
				<c:if test="${informacao.contemArquivos}">
					<div class="card-sidebar card border-alert bg-white mb-3">
						<div class="card-header">Arquivos Anexos</div>
						<div class="card-body">
							<c:forEach items="${informacao.movs}" var="m">
								<c:if test="${m.tipo.id == 13 && m.movCanceladora == null}">
									<p style="word-break: break-all; word-wrap: break-word;">
										<img style="margin-bottom: -4px;"
											src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
											target="_blank"
											href="${linkTo[AppController].baixar(m.arq.id)}">${m.arq.titulo}</a>
									</p>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</c:if>

				<c:set var="papeis" value="${informacao.papeisVinculados}" />
				<c:if test="${not empty papeis}">
					<div class="card-sidebar card border-alert bg-white mb-3">
						<div class="card-header">Perfis</div>
						<div class="card-body">
							<c:forEach var="papel" items="${papeis}">
								<p style="margin-bottom: 3px;">
									<b>${papel.key.descPapel}:</b>
								</p>
								<ul>
									<c:forEach var="pessoaLotaOuGrupo" items="${papel.value}">
										<li><c:catch var="exception">${pessoaLotaOuGrupo.nomePessoa}</c:catch>
											<c:if test="${not empty exception}">
												<c:catch var="exception">${pessoaLotaOuGrupo.nomeLotacao}</c:catch>
												<c:if test="${not empty exception}">${pessoaLotaOuGrupo.dscGrupo}</c:if>
											</c:if></li>
									</c:forEach>
								</ul>
							</c:forEach>
						</div>
					</div>
				</c:if>
				<div class="gt-sidebar-content" id="gc"></div>
			</div>

			<!-- / sidebar -->
		</div>
	</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".gt-success").delay(5000).fadeOut("slow", "linear");
		});
	</script>

	<script type="text/javascript">
		$(document).ready(function() {
			$("img").css({
				"max-width" : "100%",
				"height" : "auto",
			});
		});
	</script>

	<script type="text/javascript">
		function visualizaImpressao(divId) {
			var printContents = document.getElementById(divId).innerHTML;
			var popupWin = window.open('', '_blank');
			popupWin.document.open()
			popupWin.document
					.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">'
							+ printContents + '</html>');
			popupWin.document.close();
		}
	</script>

	<script type="text/javascript">
		$
				.ajax({
					type : "GET",
					url : "/sigagc/app/knowledgeSidebar?${informacao.gcTags}&id=${informacao.id}&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",
					cache : false,
					success : function(response) {
						$('#gc').replaceWith(response);
					}
				});
	</script>

	<!-- Movimentações -->
	<c:if test="${movimentacoes}">
		<div class="col col-12" style="padding-bottom: 0px;">
			<div>
				<h3 id="ancora_mov">Movimentações</h3>
			</div>

			<div class="" style="margin-bottom: 25px;">
				<table class="table table-striped table-sm">
					<thead>
						<tr>
							<th align="left" rowspan="2">Data</th>
							<th rowspan="2">Evento</th>
							<th colspan="2" align="left">Cadastrante</th>
							<th colspan="2" align="left">Titular</th>
							<th colspan="2" align="left">Atendente</th>
							<th colspan="2" align="left">Descrição</th>
						</tr>
						<tr>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
							<th align="left">Lotação</th>
							<th align="left">Pessoa</th>
						</tr>
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
												value="${m.movRef.tipo.nome} : ${m.movRef.arq.titulo}" />

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
									<c:when
										test="${informacao.podeDesfazer(titular, lotaTitular, m)}">
										<td>${m.tipo.nome}<img
											style="margin-bottom: -2px; width: 11px;"
											src="/siga/css/famfamfam/icons/cross.png" /> <span
											class="gt-table-action-list"> <a
												href="javascript:if (confirm('Deseja desfazer essa movimentação?')) location.href = '${linkTo[AppController].desfazer(informacao.siglaCompacta,m.id)}';">desfazer</a></span>&nbsp;
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
								<td align="left"><span
									title="${m.lotacaoAtendente.descricao}">${m.lotacaoAtendente.sigla}</span></td>
								<td align="left"><span
									title="${m.pessoaAtendente.descricao}">${m.pessoaAtendente.nomeAbreviado}</span></td>
								<td>${descricao}</td>
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
					"scrollTop" : $("#ancora_mov").offset().top
				}, 900);
			});
		</script>
	</c:if>

	<c:if test="${historico}">
		<div class="col col-12" style="padding-bottom: 0px;">
			<div class="">
				<h3 id="ancora_his">Histórico de alterações</h3>
			</div>

			<div class="" style="margin-bottom: 25px;">
				<table class="table table-striped">
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
			});
		</script>
	</c:if>
</siga:pagina>