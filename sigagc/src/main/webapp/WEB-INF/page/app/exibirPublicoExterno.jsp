<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="${informacao.sigla}" desabilitarmenu="sim">
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-8">
			<h2>${informacao.sigla}</h2>
			<c:if test="${flash.success}">
				<p class="gt-success">${flash.success}</p>
			</c:if>

			<!-- Dados do documento -->
			<div id="knowledgeContent" class="card card-body mb-1">
				<h4>${informacao.arq.titulo}</h4>
				${conteudo}
			</div>
		</div>

		<div class="col-sm-4">
				<div class="gt-sidebar-content">
				<h3>${informacao.tipo.nome}</h3>
				<p>
					<b>Tipo: </b> ${informacao.tipo.nome}
				</p>
				<p>
					<b>&Oacute;rg&atilde;o Usu&aacute;rio:</b> ${informacao.ou.acronimoOrgaoUsu}
				</p>
				<p>
					<b>Visualiza&ccedil;&atilde;o:</b> ${informacao.visualizacao.nome}
				</p>
				<p>
					<b>Edi&ccedil;&atilde;o:</b> ${informacao.edicao.nome}
				</p>
				<c:if test="${not empty informacao.grupo}">
				<p>
					<b>Grupo:</b> ${informacao.grupo.siglaGrupo} - ${informacao.grupo.dscGrupo}
				</p>
				</c:if>	
				<p>
					<b>Autor:</b> ${informacao.autor.descricaoIniciaisMaiusculas} -
					${informacao.autor.sigla}
				</p>
				<p>
					<b>Lota&ccedil;&atilde;o:</b> ${informacao.lotacao.descricaoIniciaisMaiusculas} -
					${informacao.lotacao.sigla}
				</p>
				<p>
					<b>Data de cria&ccedil;&atilde;o:</b> ${informacao.dtIniString}
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
						<b>Classifica&ccedil;&otilde;es:</b>
					<ul>
						<c:forEach items="${cls}" var="cl">
							<li>${cl}</li>
						</c:forEach>
					</ul>
					</p>
				</c:if>
				<c:if test="${empty cls}">
					<p>
						<b>Classifica&ccedil;&atilde;o:</b> ${(not empty informacao.arq.classificacao) ? informacao.arq.classificacao : "Esse conhecimento ainda n&atilde;o possui uma classifica&ccedil;&atilde;o"}
					</p>
				</c:if>
				<c:if test="${informacao.contemArquivos}">
					<h3>Arquivos Anexos</h3>
					<c:forEach items="${informacao.movs}" var="m">
						<c:if
							test="${m.tipo.id == 13 && m.movCanceladora == null}">
							<p>
								<img style="margin-bottom: -4px;"
									src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
									target="_blank" href="${linkTo[AppController].baixarSemAutenticacao(m.arq.id,informacao.id)}">${m.arq.titulo}</a>
							</p>
						</c:if>
					</c:forEach>
				</c:if>
				
				<c:set var="papeis" value="${informacao.papeisVinculados}"/>
				<c:if test="${not empty papeis}">
				<div class="gt-sidebar-content" style="padding-top: 10px">
					<h3>Perfis</h3>
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
									</c:if>
								</li>
							</c:forEach>
						</ul>
					</c:forEach>
				</div>
				</c:if>
				
				
			</div>

			<div class="gt-sidebar-content" id="gc"></div>

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
		SetInnerHTMLFromAjaxResponse(
				"/sigagc/app/knowledgeSidebar?${informacao.gcTags}&id=${informacao.id}&estiloBusca=algumIgualNenhumDiferente&ts=${currentTimeMillis}",
				document.getElementById('gc'));
	</script>

	<!-- Movimentacoes -->
	<c:if test="${movimentacoes}">
		<div class="gt-bd" style="padding-bottom: 0px;">
			<div class="gt-content">
				<h3 id="ancora_mov">Movimenta&ccedil;&otilde;es</h3>
			</div>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
				<table class="gt-table mov">
					<thead>
						<tr>
							<th align="left" rowspan="2">Data</th>
							<th rowspan="2">Evento</th>
							<th colspan="2" align="left">Cadastrante</th>
							<th colspan="2" align="left">Titular</th>
							<th colspan="2" align="left">Atendente</th>
							<th colspan="2" align="left">Descri&ccedil;&atilde;o</th>
						</tr>
						<tr>
							<th align="left">Lota&ccedil;&atilde;o</th>
							<th align="left">Pessoa</th>
							<th align="left">Lota&ccedil;&atilde;o</th>
							<th align="left">Pessoa</th>
							<th align="left">Lota&ccedil;&atilde;o</th>
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
								<td>${m.tipo.nome}</td>
								<td align="left"><span title="${m.hisIdcIni.pessoaAtual.lotacao.descricao}">${m.hisIdcIni.pessoaAtual.lotacao.sigla}</span>
								</td>
								<td align="left"><span title="${m.hisIdcIni.dpPessoa.descricao}">${m.hisIdcIni.dpPessoa.nomeAbreviado}</span>
								</td>
								<td align="left"><span
									title="${m.lotacaoTitular.descricao}">${m.lotacaoTitular.sigla}</span></td>
								<td align="left"><span title="${m.pessoaTitular.descricao}">${m.pessoaTitular.nomeAbreviado}</span></td>
								<td align="left"><span title="${m.lotacaoAtendente.descricao}">${m.lotacaoAtendente.sigla}</span></td>
								<td align="left"><span title="${m.pessoaAtendente.descricao}">${m.pessoaAtendente.nomeAbreviado}</span></td>
								<td>
									${descricao}					
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
					"scrollTop" : $("#ancora_mov").offset().top
				}, 900);
			});
		</script>
	</c:if>

	<c:if test="${historico}">
		<div class="gt-bd" style="padding-bottom: 0px;">
			<div class="gt-content">
				<h3 id="ancora_his">Hist&oacute;rico de altera&ccedil;&otilde;es</h3>
			</div>

			<div class="gt-content-box gt-for-table" style="margin-bottom: 25px;">
				<table class="gt-table mov">
					<thead>
						<tr>
							<th align="center" rowspan="2">Data</th>
							<th colspan="2" align="left">Cadastrante</th>
							<th rowspan="2">Descri&ccedil;&atilde;o</th>
						</tr>
						<tr>
							<th align="left">Lota&ccedil;&atilde;o</th>
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
									<!-- Titulo --> <c:if test="${mapTitulo.containsKey(m)}">
										<h4>${mapTitulo.get(m)}</h4>
									</c:if> <!-- Conteudo --> <c:if test="${mapTxt.containsKey(m)}">${mapTxt.get(m)}</c:if>
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