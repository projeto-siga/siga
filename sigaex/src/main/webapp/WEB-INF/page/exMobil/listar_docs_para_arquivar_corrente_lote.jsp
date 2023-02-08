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
                                totalItens="${tamanho}" itens="${itens}" var="documento">

                    <c:if test="${f:podeTransferir(titular, lotaTitular, documento)}">
                        <tr class="even">
                            <td align="center" class="align-middle text-center">
                                <input type="checkbox" name="documentosSelecionados" value="${documento.codigoCompacto}"
                                       id="${documento.idDoc}" class="chkDocumento"/>
                            </td>
                            <td class="text-right">
                                <c:choose>
                                    <c:when test='${param.popup!="true"}'>
                                        <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
                                                ${documento.sigla}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:opener.retorna_${param.propriedade}('${documento.id}','${documento.sigla}','');">
                                                ${documento.sigla}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${not documento.geral}">
                                <td width="2%" align="center">${f:destinacaoPorNumeroVia(documento.doc, documento.numSequencia)}</td>
                                <td class="text-center">${documento.doc.dtDocDDMMYY}</td>
                                <td class="text-center">
                                    <siga:selecionado isVraptor="true"
                                                      sigla="${documento.doc.lotaSubscritor.sigla}"
                                                      descricao="${documento.doc.lotaSubscritor.descricao}"/>
                                </td>
                                <td class="text-center">
                                    <siga:selecionado isVraptor="true"
                                                      sigla="${documento.doc.subscritor.iniciais}"
                                                      descricao="${documento.doc.subscritor.descricao}"/></td>
                                <td class="text-center">${documento.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
                                <td class="text-center">
                                    <siga:selecionado isVraptor="true"
                                                      sigla="${documento.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
                                                      descricao="${documento.ultimaMovimentacaoNaoCancelada.resp.descricao}"/>
                                </td>
                            </c:if>
                            <c:if test="${documento.geral}">
                                <td align="center"></td>
                                <td class="text-center">${documento.doc.dtDocDDMMYY}</td>
                                <td class="text-center">
                                    <siga:selecionado isVraptor="true"
                                                      sigla="${documento.doc.subscritor.iniciais}"
                                                      descricao="${documento.doc.subscritor.descricao}"/></td>
                                <td class="text-center">
                                    <siga:selecionado isVraptor="true"
                                                      sigla="${documento.doc.lotaSubscritor.sigla}"
                                                      descricao="${documento.doc.lotaSubscritor.descricao}"/></td>
                                <td class="text-center"></td>
                                <td class="text-center"></td>
                            </c:if>
                            <td>
                                <c:choose>
                                    <c:when test="${siga_cliente == 'GOVSP'}">
                                        ${documento.doc.descrDocumento}
                                    </c:when>
                                    <c:otherwise>
                                        ${f:descricaoConfidencial(documento.doc, lotaTitular)}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </siga:paginador>
                </tbody>
            </table>
        </div>
        </div>
    </c:if>

    <script type="text/javascript">
        function sbmt(offset) {
            listarDocumentosParaTramitarEmLote(offset);
        }

        function checkUncheckAll(theElement) {
            let isChecked = theElement.checked;
            Array.from(document.getElementsByClassName('chkDocumento')).forEach(chk => chk.checked = isChecked);
        }

        function displaySel() {
            document.getElementById('checkall').checked =
                Array.from(document.getElementsByClassName('chkDocumento')).every(chk => chk.checked);
        }
    </script>
</siga:pagina>