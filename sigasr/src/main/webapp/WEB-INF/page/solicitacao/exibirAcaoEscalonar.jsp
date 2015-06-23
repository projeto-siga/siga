<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script>
	$(document).ready(function() {
		removeSelectedDuplicado();
	});
	
	function carregarLotacaoDaAcao(){
		//preenche o campo atendente com a lotacao designada a cada alteracao da acao 
		var opcaoSelecionada = $("#selectAcao option:selected");
		var idAcao = opcaoSelecionada.val();
		try{
			var siglaLotacao = opcaoSelecionada.html().split("(")[1].slice(0,-2);
	
			var spanLotacao = $(".lotacao-" + idAcao + ":contains(" + siglaLotacao + ")");
			var descLotacao = spanLotacao.html();
			var idLotacao = spanLotacao.next().html();
			var idDesignacaoDaAcao = spanLotacao.prev().html();
	
			$("#idDesignacao").val(idDesignacaoDaAcao);
			$("#atendentePadrao").html(descLotacao);
			$("#idAtendente").val(idLotacao);
			//garante que quando alterar a acao o atendenteNaoDesignado fique vazio
			$("#atendenteNaoDesignado").val('');
		} catch(err){
			$("#idDesignacao").val('');
			$("#atendentePadrao").html('');
			$("#idAtendente").val('');
			//garante que quando alterar a acao o atendenteNaoDesignado fique vazio
			$("#atendenteNaoDesignado").val('');
		}
	}
	
	function removeSelectedDuplicado() {
		//solucao de contorno temporaria para opções no select com mesmo value.
		var primeiro = $("#selectAcao option:eq(0)");
		var segundo = $("#selectAcao option:eq(1)");
		if (primeiro.val() == segundo.val()) {
			segundo.prop("selected", false);
			primeiro.prop("selected", true);
		}
	}
</script>

<c:if test="${not empty solicitacao.itemConfiguracao && not empty acoesEAtendentes}"> 
	<div class="gt-form-row gt-width-66">
		<label>A&ccedil;&atilde;o</label>	
		<select name="acao.idAcao" id="selectAcao" value="${solicitacao.acao.idAcao}" onchage="carregarLotacaoDaAcao()">
		    <option value="0">#</option>
			<c:forEach items="${acoesEAtendentes.keySet()}" var="cat">
				<optgroup  label="${cat.tituloAcao}">
					<c:forEach items="${acoesEAtendentes.get(cat)}" var="tarefa">
						<option value="${tarefa.acao.idAcao}"> ${tarefa.acao.tituloAcao} (${tarefa.conf.atendente.siglaCompleta})</option>
					</c:forEach>					 
				</optgroup>
			</c:forEach>
		</select>
		<siga:error name="solicitacao.acao"></siga:error>
	</div>
	<div>
		<!-- Necessario listar novamente a lista "acoesEAtendentes" para ter a lotacao designada da cada acao
				ja que acima no select nao tem como "esconder" essa informacao -->
		<c:forEach items="${acoesEAtendentes.keySet()}" var="cat" varStatus="catPosition">
			<c:forEach items="${acoesEAtendentes.get(cat)}" var="t" varStatus="tPosition">
				<span class="idDesignacao-${t.acao.idAcao}" style="display:none;">${t.conf.idConfiguracao}</span>
				<span class="lotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.siglaCompleta} 
									- ${t.conf.atendente.descricao}</span>
				<span class="idLotacao-${t.acao.idAcao}" style="display:none;">${t.conf.atendente.idLotacao}</span>
				<c:if test="${catPosition.first && catPosition.last && tPosition.first}"> 
					<c:set var="lotacaoDesignada" value="${t.conf.atendente.siglaCompleta}' - '${t.conf.atendente.descricao}"/>  
					<c:set var="idLotaAtendente" value="${t.conf.atendente.idLotacao}"/>
					<c:set var="idDesignacao" value="${t.conf.idConfiguracao}"/>
				</c:if>
			</c:forEach>
		</c:forEach>

		<label>Atendente</label>
		<span id="atendentePadrao" style="display:block;">${lotacaoDesignada}</span>
		<input type="hidden" id="idDesignacao" name="idDesignacao" value="${idDesignacao}" />
		<input type="hidden" name="idAtendente" id="idAtendente" value="${idLotaAtendente}" />
	</div>
</c:if>
