<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim" iframe="sim">

    <c:if test="${not empty itens}">
        <p>Atenção: No Arquivamento em Lote – Permitido até 200 documentos por operação.</p>
        <div class="gt-content-box gt-for-table">
            <table class="table table-hover table-striped">
                <thead class="${thead_color} align-middle text-center">
                <tr>
                    <th rowspan="2" align="center">
                        <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                    </th>
                    <th rowspan="2" align="right">Número</th>
                    <th rowspan="2" align="center">Destinação da via</th>
                    <th colspan="3" align="center">Documento</th>
                    <th colspan="2" align="center">Última Movimentação</th>
                    <th rowspan="2">Descrição</th>
                </tr>
                <tr>
                    <th align="center">Data</th>
                    <th align="center">Lotação</th>
                    <th align="center">Pessoa</th>
                    <th align="center">Data</th>
                    <th align="center">Pessoa</th>
                </tr>
                </thead>
                <tbody class="table-bordered">

                <siga:paginador maxItens="200" maxIndices="50"
                                totalItens="${tamanho}" itens="${itens}" var="mobil">
                    <c:choose>
                        <c:when test="${f:podeAcessarDocumento(titular, lotaTitular, mobil)}">
                            <c:set var="classDisabled" value=""/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="classDisabled" value="disabled"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="even">
                        <td align="center" class="align-middle text-center">
                            <c:choose>
                                <c:when test='${classDisabled == "disabled"}'>
                                    <a class="fas fa-info-circle text-secondary ml-1"
                                       data-toggle="tooltip" data-placement="bottom" 
                                       title="Documento ${mobil.sigla} inacessível ao usuário ${titular.sigla}"></a>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="documentosSelecionados" value="${mobil.codigoCompacto}"
                                           id="${mobil.id}" class="chkMobil"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right ${classDisabled}">
                            <c:choose>
                                <c:when test='${param.popup!="true"}'>
                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobil.sigla}">
                                            ${mobil.sigla}
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:opener.retorna_${param.propriedade}('${mobil.id}','${mobil.sigla}','');">
                                            ${mobil.sigla}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:if test="${not mobil.geral}">
                            <td width="2%" class="${classDisabled}"
                                align="center">${f:destinacaoPorNumeroVia(mobil.doc, mobil.numSequencia)}</td>
                            <td class="text-center ${classDisabled}">${mobil.doc.dtDocDDMMYY}</td>
                            <td class="text-center ${classDisabled}">
                                <siga:selecionado isVraptor="true"
                                                  sigla="${mobil.doc.lotaSubscritor.sigla}"
                                                  descricao="${mobil.doc.lotaSubscritor.descricao}"/>
                            </td>
                            <td class="text-center ${classDisabled}">
                                <siga:selecionado isVraptor="true"
                                                  sigla="${mobil.doc.subscritor.iniciais}"
                                                  descricao="${mobil.doc.subscritor.descricao}"/></td>
                            <td class="text-center ${classDisabled}">${mobil.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
                            <td class="text-center ${classDisabled}">
                                <siga:selecionado isVraptor="true"
                                                  sigla="${mobil.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
                                                  descricao="${mobil.ultimaMovimentacaoNaoCancelada.resp.descricao}"/>
                            </td>
                        </c:if>
                        <c:if test="${mobil.geral}">
                            <td align="center"></td>
                            <td class="text-center ${classDisabled}">${mobil.doc.dtDocDDMMYY}</td>
                            <td class="text-center ${classDisabled}">
                                <siga:selecionado isVraptor="true"
                                                  sigla="${mobil.doc.subscritor.iniciais}"
                                                  descricao="${mobil.doc.subscritor.descricao}"/></td>
                            <td class="text-center ${classDisabled}">
                                <siga:selecionado isVraptor="true"
                                                  sigla="${mobil.doc.lotaSubscritor.sigla}"
                                                  descricao="${mobil.doc.lotaSubscritor.descricao}"/></td>
                            <td class="text-center"></td>
                            <td class="text-center"></td>
                        </c:if>
                        <td class="text-center ${classDisabled}">
                            <c:choose>
                                <c:when test="${siga_cliente == 'GOVSP'}">
                                    ${mobil.doc.descrDocumento}
                                </c:when>
                                <c:otherwise>
                                    ${f:descricaoConfidencial(mobil.doc, lotaTitular)}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </siga:paginador>
                </tbody>
            </table>
        </div>
        </div>
    </c:if>

    <script type="text/javascript">
        function sbmt(offset) {
            listarDocumentosParaArquivarCorrenteEmLote(offset);
        }

        function checkUncheckAll(theElement) {
            let isChecked = theElement.checked;
            Array.from(document.getElementsByClassName('chkMobil')).forEach(chk => chk.checked = isChecked);
        }

        function displaySel() {
            document.getElementById('checkall').checked =
                Array.from(document.getElementsByClassName('chkMobil')).every(chk => chk.checked);
        }
    </script>
</siga:pagina>