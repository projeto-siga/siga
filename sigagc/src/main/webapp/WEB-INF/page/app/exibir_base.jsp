<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Exibição de Tópico de Informação">
<div class="gt-bd gt-cols clearfix">
	<div class="gt-content">
		<h2>${informacao.sigla}</h2>
		<c:if test="${flash.success}"> <p class="gt-success">${flash.success}</p> </c:if>
		<h3 style="margin-bottom: 0px;">${informacao.getMarcadoresEmHtml(titular,lotaTitular)}</h3>
		<!-- Links para as ações de cada mobil -->
		#{links acoes:informacao.acoes(idc,titular,lotaTitular), base:_base /}
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
				<b>Lotação:</b> ${informacao.lotacao.descricaoIniciaisMaiusculas}
				- ${informacao.lotacao.sigla}
			</p>
			<p>
				<b>Data de criação:</b> ${informacao.dtIniString}
			</p>
			<p>
				<b>Finalizado em:</b> ${informacao.elaboracaoFim.format('dd/MM/yyyy HH:mm:ss')}
			</p>
			<!--  -->
			<c:var name="cls" value="${informacao.arq.classificacao.split(",")}"/>
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
				<b>Classificação:</b> ${informacao.arq.classificacao?: "Esse conhecimento ainda não possui uma classificação"}
			</p>
			</c:if>
			<c:if test="${informacao.contemArquivos}">
				<h3>Arquivos Anexos</h3>
				<c:forEach items="${informacao.movs}" var="m">
					<c:if test="${m.tipo.id == models.GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO && m.movCanceladora == null}">
						<p>
							<img style="margin-bottom: -4px;" src="/siga/css/famfamfam/icons/${m.arq.icon}.png" />
							<a target="_blank" href="@{Application.baixar(m.arq.id)}">${m.arq.titulo}</a>
						</p>
					</c:if>
				</c:forEach>
			</c:if>
		</div>

		<div class="gt-sidebar-content" id="gc"></div>

		<!-- / sidebar -->
	</div>
</div>

#{doLayout /}

<script type="text/javascript">
		$(document).ready(function(){			
			$(".gt-success").delay(5000).fadeOut("slow", "linear");
		});
</script>

<script type="text/javascript">
		SetInnerHTMLFromAjaxResponse("knowledge?estilo=sidebar&ts=${currentTimeMillis}${informacao.gcTags.raw()}&id=${informacao.id}",document.getElementById('gc'));
</script>