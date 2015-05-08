<%@ include file="/WEB-INF/page/include.jsp"%> #{extends
'Application/exibir_base.html' /} #{set title:'Movimentações'/}

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
					<th colspan="2" align="left">Atendente</th>
					<th rowspan="2">Descrição</th>
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
										value="${m.movRef.tipo.nome + ': ' + m.movRef.arq.titulo}" />
								</c:when>
								<c:when test="${m.movRef.tipo.id == 4 || m.movRef.tipo.id == 6}">
									<c:set var="pessoa"
										value="${m.movRef.pessoaAtendente.nomeAbreviado}" />
									<c:set var="lotacao" value="${m.movRef.lotacaoAtendente.sigla}" />
									<c:set var="descricao" value="${m.movRef.tipo.nome + " : " + lotacao + pessoa}"/>
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
						<td align="left">${m.hisDtIni.format('dd/MM/yyyy HH:mm:ss')}</td>
						<c:choose>
							<c:when test="${informacao.podeDesfazer(titular, m)}">
								<td>${m.tipo.nome} [ <img
									style="margin-bottom: -2px; width: 11px;"
									src="/siga/css/famfamfam/icons/cross.png" /> <span
									class="gt-table-action-list"> <a
										href="javascript:if (confirm('Deseja desfazer essa movimentação?')) location.href = '@{Application.desfazer(informacao.sigla, m.id)}';">desfazer</a></span>&nbsp;]
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
						<td align="left"><span title="${m.lotacaoTitular?.descricao}">${m.lotacaoTitular.sigla}</span></td>
						<td align="left"><span title="${m.pessoaTitular?.descricao}">${m.pessoaTitular.nomeAbreviado}</span></td>
						<td align="left"><span
							title="${m.lotacaoAtendente?.descricao}">${m.lotacaoAtendente.sigla}</span></td>
						<td align="left"><span
							title="${m.pessoaAtendente?.descricao}">${m.pessoaAtendente.nomeAbreviado}</span></td>
						<td>${descricao}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function(){
		$("body").css({"overflow-x":"hidden"});
        $("html, body").stop().animate({
        	"scrollTop":$("#ancora_mov").offset().top
        }, 900);
        //event.preventDefault();
	});
</script>
