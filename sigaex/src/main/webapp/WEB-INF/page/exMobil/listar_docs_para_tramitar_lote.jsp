<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim" iframe="sim">
    <c:if test="${not empty itens}">
        <input type="hidden" name="paramoffset" value="0"/>
        <input type="hidden" name="p.offset" value="0"/>
        
        <div class="gt-content-box gt-for-table">
            <table class="table table-hover table-striped">
                <thead class="${thead_color} align-middle text-center">
                <tr>
                    <th rowspan="2" align="center">
                        <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                    </th>
                    <th rowspan="2" class="text-right">Número</th>
                    <th colspan="3">Documento</th>
                    <th colspan="2">Última Movimentação</th>
                    <th rowspan="2">Descrição</th>
                </tr>
                <tr>
                    <th class="text-center">Data</th>
                    <th class="text-center"><fmt:message key="usuario.lotacao"/></th>
                    <th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
                    <th class="text-center">Data</th>
                    <th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
                </tr>
                </thead>
                <tbody class="table-bordered">

                <siga:paginador maxItens="200" maxIndices="50"
                                totalItens="${tamanho}" itens="${itens}" var="documento">

                    <c:set var="chk" scope="request">process_chk_${documento.id}</c:set>
                    <c:if test="${f:podeTransferir(titular, lotaTitular, documento)}">
                        <tr class="even">
                            <td align="center" class="align-middle text-center">
                                <input type="checkbox" name="documentosSelecionados"
                                       value="${documento.codigoCompacto}" id="${chk}" class="chkDocumento"
                                       onclick="displaySel()"/>
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