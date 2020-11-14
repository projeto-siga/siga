<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<sigasr:modal nome="anexarArquivo" titulo="Anexar Arquivo" largura="80%">
	<div class="gt-content-box gt-form">
	    <form action="${linkTo[SolicitacaoController].anexarArquivo}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">               
	        <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
	        <input type="hidden" name="ocultas" value="${ocultas}" />
	        <input type="hidden" name="movimentacao.atendente.id" value="${movimentacao.solicitacao.atendente.pessoaAtual.idPessoa}" />
	        <input type="hidden" name="movimentacao.solicitacao.idSolicitacao"
	            value="${solicitacao.idSolicitacao}" /> 
	        <input type="hidden" name="movimentacao.tipoMov.idTipoMov" value="12" />
	        <input type="hidden" name="movimentacao.tipoMov.nome" value="Anexação de Arquivo" />
	        <div class="form-group">
	            <label>Arquivo</label> 
	            <div class="form-control custom-file">
		            <input class="custom-file-input" id="custom-file-input" type="file" name="movimentacao.arquivo"/>
		            <label class="custom-file-label text-truncate" for="idSelecaoArquivo" data-browse="Escolha o Arquivo">Clique para selecionar o arquivo a anexar</label>
	            </div>
	        	<script js>
					$('.custom-file-input').on('change', function() { 
						   let fileName = $(this).val().split('\\').pop(); 
						   $(this).next('.custom-file-label').addClass("selected").html(fileName); 
						});
				</script>
	        </div>
	        <div style="display: inline" class="form-group">
	            <label>Descri&ccedil;&atilde;o</label>
	            <textarea name="movimentacao.descrMovimentacao"
	            	class="form-control"
	                id="descrAnexoArquivo" cols="70" rows="4"></textarea>
	        </div>
	        <div class="mt-2">
	            <input type="submit" value="Gravar"
	                class="btn btn-primary" />
	        </div>
	    </form>
	</div>
</sigasr:modal> 

<sigasr:modal nome="fechar" titulo="Fechar" url="${linkTo[SolicitacaoController].fechar}?solicitacao.codigo=${solicitacao.siglaCompacta}" largura="80%"/>

<sigasr:modal nome="incluirEmLista" titulo="Definir Lista" url="${linkTo[SolicitacaoController].incluirEmLista}?sigla=${solicitacao.siglaCompacta}" largura="80%"/>

<sigasr:modal nome="escalonar" titulo="Escalonar Solicitação" url="${linkTo[SolicitacaoController].escalonar}?solicitacao.codigo=${solicitacao.siglaCompacta}" largura="80%" />

<sigasr:modal nome="reclassificar" titulo="Reclassificar" url="${linkTo[SolicitacaoController].reclassificar}?solicitacao.codigo=${solicitacao.siglaCompacta}" largura="80%"/>

<sigasr:modal nome="juntar" titulo="Juntar" largura="80%">
    <form action="${linkTo[SolicitacaoController].juntar}" method="post" enctype="multipart/form-data" id="formGravarJuncao">
        <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
        <input type="hidden" name="ocultas" value="${ocultas}" />
        <input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
        <div class="form-group">
            <sigasr:selecao 
            	titulo="Solicita&ccedil;&atilde;o" 
            	propriedade="solRecebeJuntada" 
            	urlAcao="solicitacao/buscar" 
            	urlSelecionar="solicitacao/selecionar" 
            	onchange="validarAssociacao('Juncao');"
            	modulo="sigasr"/>  
            <span id="erroSolicitacaoJuncao" style="color: red; display: none;">Solicita&ccedil;&atilde;o n&atilde;o informada.</span>
        </div>
        <div class="form-group">
            <label>Justificativa</label>
            <textarea class="form-control" rows="4" name="justificativa" id="justificativaJuncao" maxlength="255" 
            	onkeyup="validarAssociacao('Juncao')"></textarea>
            <span id="erroJustificativaJuncao" style="color: red; display: none;"><br />Justificativa n&atilde;o informada.</span>
        </div>
        <div style="display: inline" class="gt-form-row ">
            <input type="button" onclick="gravarAssociacao('Juncao');" value="Gravar" class="btn btn-primary" />
        </div>
    </form>
</sigasr:modal>

<sigasr:modal nome="vincular" titulo="Vincular" largura="80%">
    <form action="${linkTo[SolicitacaoController].vincular}" method="post" enctype="multipart/form-data" id="formGravarVinculo">
        <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
        <input type="hidden" name="ocultas" value="${ocultas}" />
        <input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
        <div style="display: inline; padding-top: 10px;" class="form-group">
            
			<sigasr:selecao titulo="Solicita&ccedil;&atilde;o" propriedade="solRecebeVinculo" urlAcao="solicitacao/buscar" urlSelecionar="solicitacao/selecionar" modulo="sigasr"/>            	
            	
            <span id="erroSolicitacaoVinculo" style="color: red; display: none;">Solicita&ccedil;&atilde;o n&atilde;o informada.</span>
        </div>
        <div class="form-group mt-2">
            <label>Justificativa</label>
            <textarea class="form-control" 
            			style="width: 100%;" cols="70" rows="4" 
            			name="justificativa" id="justificativaVinculo" maxlength="255" 
            			onkeyup="validarAssociacao('Vinculo')"></textarea>
            <span id="erroJustificativaVinculo" style="color: red; display: none;"><br />Justificativa n&atilde;o informada.</span>
        </div>
        <input type="button" onclick="gravarAssociacao('Vinculo');" value="Gravar" class="btn btn-primary" />
    </form>
</sigasr:modal>

<sigasr:modal nome="associarLista" titulo="Definir Lista" url="associarLista.jsp" largura="80%"/>

<sigasr:modal nome="responderPesquisa" titulo="Responder Pesquisa" url="responderPesquisa?sigla=${solicitacao.siglaCompacta}" largura="80%"/>

<sigasr:modal nome="deixarPendente" titulo="Pendência" largura="80%">
        <div class="mb-2">
            <form action="${linkTo[SolicitacaoController].deixarPendente}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
                <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
                <input type="hidden" name="ocultas" value="${ocultas}" />                
                <div class="form-group">
                    <label>Data de T&eacute;rmino</label>
                    <siga:dataCalendar nome="calendario" id="calendario"/>
                </div>
                <div class="form-group">
                    <label>Hor&aacute;rio de T&eacute;rmino</label>
                    <input type="text" name="horario" id="horario" class="form-control"/>
                </div>
                <div class="form-group">
                    <label>Motivo</label>
                    <siga:select name="motivo" id="motivo" list="motivosPendencia"
                         listValue="descrTipoMotivoPendencia" theme="simple" isEnum="true"/>
                </div>
                <div class="form-group">
                    <label>Detalhamento do Motivo</label>
                    <textarea name="detalheMotivo" rows="4" class="form-control"> </textarea>
                </div>
               	<input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
               	<input type="submit" value="Gravar" class="btn btn-primary" />
            </form>
        </div>
</sigasr:modal> 

<sigasr:modal nome="alterarPrioridade" titulo="Alterar Prioridade" largura="80%">
    <div class="gt-form gt-content-box">
        <form action="${linkTo[SolicitacaoController].alterarPrioridade}" method="post" onsubmit="javascript: return block();" enctype="multipart/form-data">
            <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
            <input type="hidden" name="ocultas" value="${ocultas}" />
            <div class="form-group">
	<label>Prioridade</label> 
	<siga:select name="prioridade" id="prioridade" list="prioridadeList" listValue="descPrioridade" listKey="idPrioridade" isEnum="true"
			value="${solicitacao.prioridadeTecnica}"
			style="width:235px"  />
	</div>
            <div class="gt-form-row">
            	<input type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> 
            	<input type="submit" value="Gravar" class="btn btn-primary" />
            </div>
        </form>
    </div>
</sigasr:modal>

<sigasr:modal nome="desentranhar" titulo="Desentranhar" largura="80%">
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

<sigasr:modal nome="terminarPendenciaModal" titulo="Terminar Pendência" largura="80%">
    <form action="${linkTo[SolicitacaoController].terminarPendencia}"    	
    		method="post" 
    		onsubmit="javascript: return block();" 
    		enctype="multipart/form-data">
        <input type="hidden" name="todoOContexto" value="${todoOContexto}" />
        <input type="hidden" name="ocultas" value="${ocultas}" />
        <div class="form-group">
            <label>Descri&ccedil;&atilde;o</label>
            <textarea style="width: 100%" name="descricao" rows="4" class="form-control"> </textarea>
        </div>
        <input
            type="hidden" name="idMovimentacao" id="movimentacaoId" value="" /><input
            type="hidden" name="motivo" id="motivoId" value="" /><input
            type="hidden" name="sigla" value="${solicitacao.siglaCompacta}" /> <input
            type="submit" value="Gravar" class="btn btn-primary" />
    </form>
    </sigasr:modal>  