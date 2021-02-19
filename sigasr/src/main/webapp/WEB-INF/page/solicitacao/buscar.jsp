<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<siga:pagina titulo="Pesquisar Solicitações">

<jsp:include page="../main.jsp"></jsp:include>

<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>

<div class="container-fluid">
    <div class="gt-content clearfix">
	    <h2>Pesquisa de Solicita&ccedil;&otilde;es</h2>
	     <%--<c:choose>
	       <c:when test="${not empty solicitacaoListaVO.getItens()}"> --%>
	       
	           <sigasr:solicitacao solicitacaoListaVO="solicitacaoListaVO" filtro="filtro" modoExibicao="solicitacao" />
	           
	           
	        <%--</c:when>
	        <c:when test="${filtro.pesquisar}">
	            <div align="center" style="font-size: 14px; color: #365b6d; font-weight: bold">Nenhum item foi encontrado.</div>
	        </c:when>
	    </c:choose> --%>
	    
	    <div class="card mb-2">
	    	<div class="card-header">
	    		<h5>Dados para busca</h5>
	    	</div>
	    	<div class="card-body">
	    		<form id="frm" action="${linkTo[SolicitacaoController].buscar}" method="get" onsubmit="javascript: return block();" >
		        	
		            <input type="hidden" name="propriedade" value="${propriedade}" />
		            <input type="hidden" name="popup" value="${popup}" />
		            <div class="row">
		            	<div class="col-sm-3">
		            		<div class="form-group">
		            			<label>Situa&ccedil;&atilde;o</label>
		            			<siga:select name="filtro.situacao.idMarcador" 
		                                        list="marcadores"
		                                        listKey="idMarcador"
		                                        listValue="descrMarcador"
		                                        headerKey=""
		                                        headerValue="Todas"
		                                        value="${filtro.situacao.idMarcador}"
		                                        theme="simple"/>
		            		</div>
		            	</div>
		            	<div class="col-sm-9">
                           	<sigasr:pessoaLotaSelecao2 propriedadePessoa="filtro.atendente" propriedadeLotacao="filtro.lotaAtendente" labelPessoaLotacao="com"/>
                            	
                           	<div id="chkNaoDesignados" class="gt-form-row gt-width-66" style="padding-top: 6pt;">
                                <label> 
                                   <siga:checkbox nameInput="filtro.naoDesignados" name="filtro.naoDesignados" value="${filtro.naoDesignados}"/> 
                                   Apenas n&atilde;o atribu&iacute;das a pessoa
                                </label>
                            </div>
                            <script language="javascript">
                                $("#chkNaoDesignados").appendTo("#spanLotacaofiltrolotaAtendente");
                            </script>
		            	</div>
	            	</div>
	            	
	            	
	            	<div class="row">
	            		<div class="col-sm-12">
	            			<sigasr:pessoaLotaSelecao2 propriedadePessoa="filtro.cadastranteBusca" propriedadeLotacao="filtro.lotaCadastranteBusca" labelPessoaLotacao="Cadastrante"/>
	            		</div>
	            	</div>
	            	
	            	<!-- Solicitante -->
	            	<div class="row">
	            		<div class="col-sm-12">
            		         <sigasr:pessoaLotaSelecao2 propriedadePessoa="filtro.solicitante" propriedadeLotacao="filtro.lotaSolicitante" labelPessoaLotacao="Solicitante"/>
	            		</div>
            		</div>
            		
            		<div class="row">
            				<div class="col-sm-3 form-group">
            					<label>Data de cria&ccedil;&atilde;o</label>
           					</div>
           					<div class="col-sm-4" style="z-index: 2">
            					<siga:dataCalendar nome="filtro.dtIni" value="${filtro.dtIni}"/>
           					</div>           				
           					<span class="col-sm">a</span>	
           					<div class="col-sm-4" style="z-index: 2">
            					<siga:dataCalendar nome="filtro.dtFim" value="${filtro.dtFim}"/>
           					</div>
            		</div>
            		
            		<div class="row">
            			<div class="col-sm-12">
            				<div class="form-group">
            					<label>Item</label>
								<sigasr:selecao3 
										tamanho="grande" 
										propriedade="filtro.itemConfiguracao" 
										tipo="itemConfiguracao" 
										tema="simple"
										modulo="sigasr"/>
            				</div>
            			</div>
            		</div>
            		
            		<div class="row">
            			<div class="col-sm-12">
            				<div class="form-group">
            					<label>A&ccedil;&atilde;o</label>
            					<sigasr:selecao3 tamanho="grande" propriedade="filtro.acao" tipo="acao" tema="simple" modulo="sigasr"/>
            				</div>
            			</div>
            		</div>
            		
            		<div class="row">
            			<div class="col-sm-6">
            				<div class="form-group">
            					<label>Atributo</label>
            					<sigasr:selecao3 
            							tamanho="grande" 
            							propriedade="filtro.atributoSolicitacao.atributo" 
            							tipo="atributo"
            							ocultardescricao="sim" 
            							tema="simple" modulo="sigasr"
		                    				onchange="toggleInputValorAtributo();"/>
            				</div>
            			</div>
            			<div class="col-sm-6">
              				<div id="valorAtributo" class="form-group">
              					<label>&nbsp;</label>
               					<input type="text"
               							class="form-control" 
               							name="filtro.atributoSolicitacao.valorAtributoSolicitacao" 
               							value="${filtro.atributoSolicitacao.valorAtributoSolicitacao}" />
                    		</div>
            			</div>
            		</div>
            		
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Prioridade M&iacute;nima</label>
								<select name="filtro.prioridade" id="filtroPrioridade" value="${filtro.prioridade}" class="form-control">
									<option value="">Todas</option>
									<c:forEach items="${prioridadesEnum}" var="item">
										<option value="${item}" ${item == filtro.prioridade ? 'selected' : ''}>${item.descPrioridade }</option>
									</c:forEach>
					     		</select>
					 		</div>
						</div>
					</div>
             
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Lista de Prioridade</label>
								<select name="filtro.idListaPrioridade" id="filtroidListaPrioridade" class="form-control">
		                               <option value="-1" ${-1 == filtro.idListaPrioridade ? 'selected' : ''}>Qualquer Uma</option>
		                               <option value="0" ${0 == filtro.idListaPrioridade ? 'selected' : ''}>Nenhuma</option>
		                               <c:forEach items="${listasPrioridade}" var="item">
		                                   <option value="${item.idLista}" ${item.idLista == filtro.idListaPrioridade ? 'selected' : ''}>${item.nomeLista }</option>
		                               </c:forEach>
	                           	</select>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Descri&ccedil;&atilde;o</label>
								<input type="text" name="filtro.descrSolicitacao" 
										class="form-control" 
										id="filtro.descrSolicitacao" 
										value="${filtro.descrSolicitacao}"/>
							</div>
						</div>
					</div>
					
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Local</label>
								<select name="filtro.local.idComplexo" id="filtro.local.idComplexo" class="form-control">
	        						<option value="-1" ${-1 == filtro.local.idComplexo ? 'selected' : ''}>Qualquer Um</option>
	        						<c:forEach items="${locaisDisponiveis.keySet()}" var="orgao">
	        								<optgroup label="${orgao.acronimoOrgaoUsu}">
	                						<c:forEach items="${locaisDisponiveis.get(orgao)}" var="local">
	                        						<option value="${local.idComplexo}" ${filtro.local.idComplexo.equals(local.idComplexo) ? 'selected' : ''}>${local.nomeComplexo}</option>
	                						</c:forEach>
	                						</optgroup>
	        						</c:forEach>
								</select>
							</div>
						</div>
					</div>
									
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Acordo</label>
								<sigasr:selecao3 tamanho="grande" 
											propriedade="filtro.acordo" 
											ocultardescricao="sim" 
											tipo="acordo" 
											tema="simple" 
											modulo="sigasr" 
											paramList="popup=true;"/>
							</div>
						</div>
					</div>
            					
		            <input type="submit" value="Buscar" class="btn btn-primary"/>
		            		           
	        	</form>
	    	</div>
	    </div>
	    
    </div>
</div>
<script>
	
	function toggleInputValorAtributo() {
		var show = true;
		var inputNomeAtributo = $('#formulario_filtroatributoSolicitacaoatributo_sigla');

		if (inputNomeAtributo.val() === "") {
			show = false;
			limparInputValorAtributo();
		}
		$('#valorAtributo').toggle(show);
	};

	function limparInputValorAtributo() {
		var inputValorAtributo = $('#valorAtributo input');
		inputValorAtributo.val('');
	}

	toggleInputValorAtributo();
</script>

</siga:pagina>