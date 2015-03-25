<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Cadastro de A&ccedil;&atilde;o">

<div class="gt-bd clearfix">
	<div class="gt-content">
		<h2>Cadastro de Ação</h2>

		<div class="gt-form gt-content-box">
			<form action="@Application.gravarAcao()" enctype="multipart/form-data"  onsubmit="javascript=: return block();" > 
			<c:if test="${acao?.idAcao}">
			 <input type="hidden"
				name="acao.idAcao" value="${acao.idAcao}">
			</c:if>
			<c:if test="${not empty errors}">
			<p class="gt-error">Alguns campos obrigatórios não foram
				preenchidos <sigasr:error /></p>
			</c:if>
			<div class="gt-form-row gt-width-66">
				<label>Código</label> <input type="text"
					name="acao.siglaAcao"
					value="${acao?.siglaAcao}" />
					<span style="color="${ red"><sigasr:error
					name="acao.siglaAcao" /></span>
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Título</label> <input type="text"
					name="acao.tituloAcao"
					value="${acao?.tituloAcao}" size="100"/>
					<span style="color="${ red"><sigasr:error
					name="acao.tituloAcao" /></span>
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Descrição</label> <input type="text"
					name="acao.descrAcao"
					value="${acao?.descrAcao}" size="100"/>
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>Tipo de ação</label>
					<sigasr:select name="acao.tipoAcao" 
					items="${SrTipoAcao.values()}"
					labelProperty="descrTipoAcao" 
					value="${acao.tipoAcao}"
					class="select-siga" />
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>Tipo de execução pelo Pré-atendimento</label>
					<sigasr:select name="acao.tipoExecucao" 
					items="${SrTipoExecucaoAcao.values()}"
					labelProperty="descrTipoExecucaoAcao" 
					value="${acao.tipoExecucao}"
					class="select-siga" />
			</div>
			
			<div class="gt-form-row gt-width-66">
				<label>Forma de atendimento</label>
					<sigasr:select name="acao.formaAtendimento" 
					items="${SrFormaAtendimentoAcao.values()}"
					labelProperty="descFormaAtendimentoAcao" 
					value="${acao.formaAtendimento}"
					class="select-siga" />
			</div>
			
			<div class="gt-form-row">
				<input type="submit" value="Gravar"
					class="gt-btn-medium gt-btn-left" />
				<a href="@{Application.listarAcao}" class="gt-btn-medium gt-btn-left">Cancelar</a>	
			</div>
			</form>
		</div>
	</div>
</div>

