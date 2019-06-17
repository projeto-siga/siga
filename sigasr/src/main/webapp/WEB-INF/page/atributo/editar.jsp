<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<div class="gt-form gt-content-box">
	<form id="atributoForm" action="#" enctype="multipart/form-data">
		<input type="hidden" name="atributo.id" id="idAtributo" value="${idAtributo}">
		<input type="hidden" name="atributo.hisIdIni" id="hisIdIni" value="${hisIdIni}">
		<div class="gt-form-row box-wrapper">
			<div class="box box-left gt-width-50">
				<label>Nome <span>*</span></label> <input type="text"
					name="atributo.nomeAtributo"
					id="nomeAtributo"
					value="${nomeAtributo}" size="50" maxlength="255" required/>
			</div>
			<div class="box gt-width-50">
				<label>Descrição</label> <input maxlength="255" type="text"
					name="atributo.descrAtributo"
					id="descrAtributo"
					value="${descrAtributo}" 
					style="width: 372px;" />
			</div>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo</label> 
			<input type="text" name="atributo.codigoAtributo" id="codigoAtributo"
				value="${codigoAtributo}" size="60" maxlength="255"/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Objetivo do atributo<span>*</span></label>
			<select id="objetivoAtributo" name="atributo.objetivoAtributo.id" class="select-siga" style="width:393px;" onchange="javascript:ocultaAssociacoes();">
				<c:forEach items="${objetivos}" var="objetivo">
					<option value="${objetivo.idObjetivo}">${objetivo.descrObjetivo}</option>
				</c:forEach>
			</select>
		</div>
		<div class="gt-form-row gt-width-100">
			<label>Tipo de atributo</label>
			<select id="tipoAtributo" name="atributo.tipoAtributo.id" class="select-siga" style="width:100%;">
				<c:forEach items="${tiposAtributo}" var="tipoAtt">
					<option value="${tipoAtt}">${tipoAtt.descrTipoAtributo}</option>
				</c:forEach>
			</select>
		</div>
		<div class="gt-form-row gt-width-66" id="vlPreDefinidos" style="display: none;">
			<label>Valores pré-definidos (Separados por ponto-e-vígula(;))</label> 
			<input maxlength="512" type="text"
				name="atributo.descrPreDefinido"
				id="descrPreDefinido"
				value="${descrPreDefinido}" size="60" />
		</div>
	</form>
	
	<sigasr:configuracaoAssociacao orgaos="${orgaos}"
								 locais="${locais}"
								 itemConfiguracaoSet="${itemConfiguracaoSet}"
								 acoesSet="${acoesSet}"
								 modoExibicao='atributo'
								 urlGravar="${linkTo[AssociacaoController].gravarAssociacao}"></sigasr:configuracaoAssociacao>
		
	<div class="gt-form-row" style="padding-top: 10px;">
		<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="atributoService.gravar()"/>
		<a class="gt-btn-medium gt-btn-left" onclick="atributoService.cancelarGravacao()">Cancelar</a>
		<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="atributoService.aplicar()"/>
	</div>
</div>


<script>
	associacaoService.getUrlDesativarReativar = function(desativados) {
		var url = '${linkTo[AtributoController].listarAssociacaoAtributo}';
		var idAtributo = $("[id=idAtributo]").val();
		var exibirInativo = "";
		
		if(desativados)
			exibirInativo = "&exibirInativos=true";
			
		return url + "?idAtributo=" + idAtributo + exibirInativo;
	}

	var validatorAtributoForm;
	jQuery( document ).ready(function( $ ) {
		validatorAtributoForm = $("#atributoForm").validate({
			onfocusout: false
		});
	});
	
	function ocultaAssociacoes(){
		if ($("#objetivo").val() == 1){
			$("#associacoes").show();
		} else {
			$("#associacoes").hide();
		}
	}

	function htmlConteudo(d, titulo, sigla, descricao, table) {
		var trItem = $('<tr>'),
			tdTitulo = $('<td><b>' + titulo + '</b></td'),
			tdConteudo = $('<td>'),
			table = $('<table>'),
			trDetalhe = $('<tr>'),
			tdSigla = $('<td>' + sigla + "</td>"),
			tdDescricao = $('<td>' +  descricao + '</td>');
		
		trDetalhe.append(tdSigla);
		trDetalhe.append(tdDescricao);
		table.append(trDetalhe);
		tdConteudo.append(table);
		trItem.append(tdTitulo);
		return trItem.append(tdConteudo);
	};

	function transformStringToBoolean(value) {
		if (value.constructor.name == 'String')
			return value == 'true';
		else
			return value;
	}
	
	associacaoService.verificarTipoAtributo = function() {
		if($("select[id='tipoAtributo']").val() === 'VL_PRE_DEFINIDO') {
			$('#vlPreDefinidos').show();
			return;
		}
		$('#vlPreDefinidos').hide();
	};
	
	associacaoService.verificarTipoAtributo();
	
	$("select[id='tipoAtributo']").change(function() {
		associacaoService.verificarTipoAtributo(); 
	});
	
	if($('#erroDescrPreDefinido').html()) {
		$("select[id='tipoAtributo']").val('VL_PRE_DEFINIDO');
		$('#vlPreDefinidos').show();
	};

	function podeCadastrarAssociacao() {
		var atributoId = $("#idAtributo");
        if ((atributoId == undefined || atributoId.val() == "")  && atributoService.aplicar() == false) 
            return false;
        else
            return true;
    }
</script>