<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Pesquisar Solicitações">

<jsp:include page="../main.jsp"></jsp:include>

<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>

<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
	    <h2>Pesquisa de Solicita&ccedil;&otilde;es</h2>
	    <c:choose>
	        <c:when test="${not empty solicitacaoListaVO.getItens()}">
	           <siga:solicitacao solicitacaoListaVO="solicitacaoListaVO" filtro="filtro" modoExibicao="solicitacao" />
	        </c:when>
	        <c:when test="${filtro.pesquisar}">
	            <div align="center" style="font-size: 14px; color: #365b6d; font-weight: bold">Nenhum item foi encontrado.</div>
	        </c:when>
	    </c:choose>
	    
	    <div class="gt-content-box gt-for-table" style="margin-top: 15px;">
	        <form action="${linkTo[SolicitacaoController].buscar}" method="GET" onsubmit="javascript: return block();" >
	            <input type="hidden" name="filtro.pesquisar" value="true" />
	            <input type="hidden" name="propriedade" value="${propriedade}" />
	            <input type="hidden" name="popup" value="${popup}" />
	            <table class="gt-form-table">
	                <tr class="header">
	                   <td align="center" valign="top" colspan="4">Dados para busca</td>
	                </tr>
	                    <tr>
	                        <td>Situa&ccedil;&atilde;o</td>
	                        <td>
	                           <siga:select name="filtro.situacao.idMarcador" 
	                                        list="marcadores"
	                                        listKey="idMarcador"
	                                        listValue="descrMarcador"
	                                        headerKey="0"
	                                        headerValue="Todas"
	                                        value="${filtro.situacao.idMarcador}"
	                                        theme="simple"/> 
                                com
                                <input type="hidden" name="filtro.atendenteSel" value="" />
                                <input type="hidden" name="filtro.lotaAtendenteSel" value="" />
                                <siga:pessoaLotaSelecao2 propriedadePessoa="filtro.atendenteSel" propriedadeLotacao="filtro.lotaAtendenteSel"/>
	                            <div id="chkNaoDesignados" class="gt-form-row gt-width-66" style="padding-top: 6pt;">
	                                <label> 
	                                   <siga:checkbox nameInput="filtro.naoDesignados" name="filtro.naoDesignados" value="${filtro.naoDesignados}"/> 
	                                   Apenas n&atilde;o designadas
	                                </label>
	                            </div>
	                            <script language="javascript">
	                                $("#chkNaoDesignados").appendTo("#spanLotacaofiltrolotaAtendenteSel");
	                            </script>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Cadastrante</td>
	                        <td>
	                           <input type="hidden" name="filtro.cadastranteSel" value="" />
	                           <input type="hidden" name="filtro.lotaCadastranteSel" value="" />
	                           <siga:pessoaLotaSelecao2 propriedadePessoa="filtro.cadastranteSel" propriedadeLotacao="filtro.lotaCadastranteSel"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Solicitante</td>
	                        <td>
	                           <input type="hidden" name="filtro.solicitanteSel" value="" />
                               <input type="hidden" name="filtro.lotaSolicitanteSel" value="" />
	                           <siga:pessoaLotaSelecao2 propriedadePessoa="filtro.solicitanteSel" propriedadeLotacao="filtro.lotaSolicitanteSel"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Data de cria&ccedil;&atilde;o</td>
	                        <td>
	                           <siga:dataCalendar nome="filtro.dtIni" value="${filtro.dtIni}"/> a
	                           <siga:dataCalendar nome="filtro.dtFim" value="${filtro.dtFim}"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Item</td>
	                        <td>
	                           <input type="hidden" name="filtro.itemConfiguracaoSel" value="" />
	                           <siga:selecao2 propriedade="filtro.itemConfiguracaoSel" tipo="itemConfiguracao" tema="simple" modulo="sigasr"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>A&ccedil;&atilde;o</td>
	                        <td>
	                           <input type="hidden" name="filtro.acaoSel" value="" />
	                           <siga:selecao2 propriedade="filtro.acaoSel" tipo="acao" tema="simple" modulo="sigasr"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Prioridade M&iacute;nima</td>
	                        <td>
	                           <select name="filtro.prioridade" id="filtroPrioridade" value="${filtro.prioridade}" style="width:250px;">
                                   <option value="">Todas</option>
                                   <c:forEach items="${prioridadesEnum}" var="item">
                                       <option value="${item}" ${item == filtro.prioridade ? 'selected' : ''}>${item.descPrioridade }</option>
                                   </c:forEach>
                               </select>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>Lista de Prioridade</td>
	                        <td>
	                           <select name="filtro.idListaPrioridade" id="filtroidListaPrioridade" style="width:250px;">
	                               <option value="-1" ${-1 == filtro.idListaPrioridade ? 'selected' : ''}>Qualquer Uma</option>
	                               <option value="0" ${0 == filtro.idListaPrioridade ? 'selected' : ''}>Nenhuma</option>
	                               <c:forEach items="${listasPrioridade}" var="item">
	                                   <option value="${item.idLista}" ${item.idLista == filtro.idListaPrioridade ? 'selected' : ''}>${item.nomeLista }</option>
	                               </c:forEach>
	                           </select>
                            </td>                                            
                        </tr>
                        <tr>
                            <td>Descri&ccedil;&atilde;o</td>
                            <td>
                                <input type="text" name="filtro.descrSolicitacao" style="width: 247px;" id="filtro.descrSolicitacao" value="${filtro.descrSolicitacao}"/>
                            </td>
                        </tr>
	                        
	                    <tr>
	                        <td>Acordo</td>
	                        <td>
	                           <input type="hidden" name="filtro.acordoSel" value="" />
	                           <siga:selecao2 tamanho="grande" propriedade="filtro.acordoSel" tipo="acordo" tema="simple" modulo="sigasr" paramList="popup=true;"/>
	                           <div id="chkNaoSatisfatorios" class="gt-form-row gt-width-66" style="padding-top: 6pt;">
	                              <label>
		                             <siga:checkbox nameInput="filtro.naoSatisfatorios" name="filtro.naoSatisfatorios" value="${filtro.naoSatisfatorios}"/>
		                             Apenas solicita&ccedil;&otilde;es em descumprimento dos seus acordos
	                              </label>
	                           </div>
	                           <script language="javascript">
	                               $("#chkNaoSatisfatorios").appendTo("#spanAcordofiltroacordo");
	                           </script>
	                        </td>
	                    </tr>   
	                    <tr>
	                        <td colspan="2"><input type="submit" value="Buscar" class="gt-btn-medium gt-btn-left" /></td>
	                    </tr>
	            </table>
	        </form>
	    </div>
    </div>
</div>

</siga:pagina>