<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>

<script type="text/javascript">
    carregarConhecimentosRelacionados();
    
    // Codigo para atualizacao dos filtros na tela
    removerFiltrosSemCampo();
    carregarFiltrosAtributos();
    carregarSolRelacionadas();
</script>

<c:if test="${not empty solicitacao.itemConfiguracao && not empty solicitacao.acao && podeUtilizarServicoSigaGC}">
    <div style="display: inline-block" >
        <div id="gc-ancora-item-acao"></div>
    </div>
    <script type="text/javascript">
        var url = "/../sigagc/app/knowledgeInplace?testarAcesso=true&popup=true&podeCriar=${exibirMenuConhecimentos}&msgvazio=&titulo=${solicitacao.gcTituloAbertura}${solicitacao.gcTagAbertura}";
        Siga.ajax(url, null, "GET", function(response){
            document.getElementById('gc-ancora-item-acao').innerHTML = response;
        });
    </script>
</c:if>

<script type="text/javascript"> 
    carregarConhecimentosRelacionados();
</script>

<c:set var="atributoSolicitacaoMap" value="${solicitacao.atributoSolicitacaoMap}"/>

<div id="atributos" class="gt-form-row gt-width-66" style="margin-top: 10px;">
    <c:forEach items="${solicitacao.atributoAssociados}" var="atributo" varStatus="loop">
        <div class="gt-form-row gt-width-66">
            <label>
                ${atributo.nomeAtributo} 
                <c:if test="${atributo.descrAtributo != null && atributo.descrAtributo != ''}">
                    (${atributo.descrAtributo})
                </c:if>
            </label>
            <c:if test="${atributo.tipoAtributo != null}">
                <c:if test="${atributo.tipoAtributo.name() == 'TEXTO'}">
                	<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" class="${atributo.idAtributo}"
                        onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');" size="70" maxlength="255" />
                    <siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo"/>
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'TEXT_AREA'}">
                
                	<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                	<textarea cols="85" rows="10" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" class="${atributo.idAtributo}"
                    	onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');" maxlength="255">${solicitacao.atributoSolicitacaoMap[atributo.idAtributo]}</textarea>
                    <siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'DATA'}">
                	<input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <siga:dataCalendar nome="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" id="calendarioAtributo${atributo.idAtributo}"
                         value="${atributoSolicitacaoMap[loop.index].valorAtributo}" onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');"
                         cssClass="${atributo.idAtributo}"/>
                    <siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'NUM_INTEIRO'}">
                    <input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <input type="text" class="${atributo.idAtributo}"
                        onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
                        onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');"
                        name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" maxlength="9"/>
                    <siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'NUM_DECIMAL'}">
                    <input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" 
                        id="numDecimal" pattern="^\d*(\,\d{2}$)?" title="Somente número e com duas casas decimais EX: 222,22" class="${atributo.idAtributo}"
                        onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');" maxlength="9"/>
					<siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'HORA'}">
                    <input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <input type="text" name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" id="horarioAtributo${atributo.idAtributo}" class="${atributo.idAtributo}"
                        onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}', '${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');" />
                    <siga:error name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" />
                    <span style="color: red; display: none;" id="erroHoraAtributo${atributo.idAtributo}">Hor&aacute;rio inv&aacute;lido</span>
                    <script>
                         $(function() {
                             $("#horarioAtributo${atributo.idAtributo}").mask("99:99");
                             $("#horarioAtributo${atributo.idAtributo}").blur(function() {
                                 var hora = this.value;
                                 var array = hora.split(':');
                                 if (array[0] > 23 || array[1] > 59) {
                                     $('#erroHoraAtributo${atributo.idAtributo}').show(); 
                                     return;
                                 }
                                 $('#erroHoraAtributo${atributo.idAtributo}').hide();    
                             });
                         });
                     </script>
                </c:if>
                <c:if test="${atributo.tipoAtributo.name() == 'VL_PRE_DEFINIDO'}" >
                    <input type="hidden" name="solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
                    <select name="solicitacao.atributoSolicitacaoMap[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[loop.index].valorAtributo}" class="${atributo.idAtributo}"
                         onchange="notificarCampoAtributoMudou('.${atributo.idAtributo}','${atributo.nomeAtributo}', 'solicitacao.atributoSolicitacaoMap[${loop.index}].idAtributo');"} >
                        <c:forEach items="${atributo.preDefinidoSet}" var="valorAtributoSolicitacao">
                            <option value="${valorAtributoSolicitacao}" <c:if test="${atributoSolicitacaoMap[loop.index].valorAtributo == valorAtributoSolicitacao}">selected</c:if> >
                                ${valorAtributoSolicitacao}
                            </option>
                        </c:forEach>
                    </select>
                </c:if>
            </c:if>
        </div>
    </c:forEach>
</div>