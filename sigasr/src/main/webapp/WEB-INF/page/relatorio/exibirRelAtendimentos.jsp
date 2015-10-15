<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Relatório de Atendimentos">

<jsp:include page="../main.jsp"></jsp:include>

<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
<script src="/sigasr/javascripts/jquery.cookie.js"></script>
<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
	    <h2>Relatório de Atendimentos</h2>
    	<div class="gt-content-box gt-for-table" style="margin-top: 15px;">
        <form action="${linkTo[RelatorioController].gerarRelAtendimentos}" method="POST" 
        	onsubmit="javascript: limparTodosPlaceHolder(); blockUIForDownload();" >
		 	<table class="gt-form-table">
                <tr class="header">
                	<td align="center" valign="top" colspan="4">Dados do Relatório</td>
                </tr>
                <tr> 
                	<td style="width: 20%;"> Lotação Atendente</td>
                	<td style="width: 80%;"> 
                		<select name="tipo" id="tipo" onchange="javascript: selecionarTipoLotacao(this);limparTagSelecao();definirPlaceHolder(this);" 
                			style="float:left; margin-right:10px;">
                			<option value="lotacao">Lotação</option>
                			<option value="lista_lotacao">Lista de Lotações</option>
                			<option value="expressao">Expressão</option>
                		</select>
                		<div id="lotacao"> <siga:selecao2 tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga"/> </div>	
                		<div id="lista_lotacao"> <input type="text" size="100" name=listaLotacoes value="" /> </div>	
                		<div id="expressao"> <input type="text"  size="25" name=siglaLotacao value="" /> </div>	
					</td> 
	    		</tr>
	    		<tr id="orgao">
					<td style="width: 20%;">Órgão da Lotação</td>
					<td style="width: 80%;">
						<select name="idOrgaoUsu" >
							<c:forEach items="${orgaos}" var="orgao">
								<option value="${orgao.idOrgaoUsu}" ${orgao.idOrgaoUsu == titular.orgaoUsuario.idOrgaoUsu ? 'selected' : ''}>
									${orgao.nmOrgaoUsu}
								</option>  
							</c:forEach>
						</select>
					</td>
	    		</tr>
				<tr>
					<td style="width: 20%;">Data inicial e final</td>
					<td style="width: 80%;">
						<input type="text" name="dtIni" value="" maxlength="10"
							onblur="javascript:verifica_data(this,true);" theme="simple"/> a
						<input type="text" name="dtFim" value="" maxlength="10" 
							onblur="javascript:verifica_data(this,true);" theme="simple" />
					</td>
				</tr>
				<tr>					
					<td colspan="2">
						<input type="submit" class="gt-btn-medium gt-btn-left" value="Gerar">
						<input type="hidden" name="downloadToken" id="downloadToken" value="" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){			
		$.removeCookie('fileDownloadToken');
		$('#tipo').val("lotacao");
		$('#lotacao').show();
		$('#lista_lotacao, #expressao, #orgao').hide();
	});
	var lista_lotacao_placeHolder = "exemplo: CENTRAL 2R;SEDGET;NUINT";
	var expressao_placeHolder = "exemplo: SE*GET";
	var fileDownloadCheckTimer;
	var token;
	function blockUIForDownload() {
		token = new Date().getTime(); 
		$('#downloadToken').val(token);
		$.blockUI({
			css: {border: 'none', 
			    padding: '15px', 
			    backgroundColor: '#000', 
			    '-webkit-border-radius': '10px', 
			    '-moz-border-radius': '10px', 
			    'border-radius' : '10px',
			    opacity: .5, 
			    color: '#fff'}, 
			message: '<h2 style="color : #fff;"> Aguarde! Gerando relatório... </h2>' });
		fileDownloadCheckTimer = setInterval(checkDownload, 1000);
	}
	function checkDownload() {
		var cookieValue = $.cookie('fileDownloadToken');
		if (cookieValue == token) {
			finishDownload();
		}
	}
	function finishDownload() {
		clearInterval(fileDownloadCheckTimer);
		$.removeCookie('fileDownloadToken'); 
		$.unblockUI();
	}
	function selecionarTipoLotacao(selecao) {
		var tipoSelecao = selecao.value;
		if (tipoSelecao == 'lotacao') {
			$('#lotacao').show();
			$('#lista_lotacao, #expressao, #orgao').hide();
		}
		else if (tipoSelecao == 'lista_lotacao') {
			$('#lista_lotacao, #orgao').show();
			$('#lotacao, #expressao').hide();
		}
		else {
			$('#expressao, #orgao').show();
			$('#lotacao, #lista_lotacao').hide();
		}
	}
	//-- inicio: essas funcoes existem porque IE9+ nao aceitam o atributo placeholder
	function definirPlaceHolder(selecao) {
		var tipo = selecao.value;
		if (tipo != "lotacao") {
			$("#" + tipo + " > input").attr("value", window[tipo + '_placeHolder']).css({"color": "#999"})
				.focusin(function() {
					limparPlaceHolder(tipo)
				}).focusout(function() {
					adicionarPlaceHolder(tipo)
				});
		}
	}
	function limparTodosPlaceHolder() {
		limparPlaceHolder("lista_lotacao");
		limparPlaceHolder("expressao");
	}
	function limparPlaceHolder(tipo) {
		if($("#" + tipo + " > input").val() == window[tipo + '_placeHolder'])
			$("#" + tipo + " > input").removeAttr("value").css({"color": "#000"});
	}
	function adicionarPlaceHolder(tipo) {
		if($("#" + tipo + " > input").val() == "")
			$("#" + tipo + " > input").attr("value", window[tipo + '_placeHolder']).css({"color": "#999"});
	}
	//-- fim
	// limpa campos do componente de busca - tag selecao da lotacao
	function limparTagSelecao() {
		$("#formulario_lotacao_id").val('');
		$("#formulario_lotacao_descricao").val('');
		$("#formulario_lotacao_sigla").val('');
		$("#lotacaoSpan").html('');
	}

</script>

</siga:pagina>
