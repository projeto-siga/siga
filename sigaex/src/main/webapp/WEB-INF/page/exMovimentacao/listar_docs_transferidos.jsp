<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"
         buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>

<siga:pagina titulo="Protocolo de Transferência" popup="true">

    <form name="frm" action="protocolo_arq_transf" method="post">
        <input type="hidden" name="isTransf" value="true"/>

        <!-- main content bootstrap -->
        <div class="container-fluid">
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5><fmt:message key='protocolo.transferencia'/></h5>
                </div>

                <div class="card-body">
                    <table class="table table-hover table-striped">
                        <tr>
                            <td>De</td>
                            <td>${lotaTitular.descricao} - ${cadastrante.descricao}</td>
                            <input type="hidden" name="sigla" id="pessoa" value="${cadastrante.sigla}"/>
                        </tr>
                        <td>Para</td>
                        <td>${mov.respString}</td>
                        </tr>
                        <tr>
                            <td>Data</td>
                            <td colspan="2">${mov.dtRegMovDDMMYYYYHHMMSS}</td>
                            <input type="hidden" name="dt" id="id" value="${mov.dtRegMovDDMMYYYYHHMMSS}"/>
                        </tr>
                    </table>

                </div>
            </div>
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5><fmt:message key='documentos.nao.transferidos'/></h5>
                </div>

                <div class="card-body">
                    <table class="table table-hover table-striped">
                        <col width="30%"/>
                        <col width="16%"/>
                        <col width="54%"/>
                        <thead class="${thead_color} align-middle text-center">
                        <tr>
                            <th>Documento</th>
                            <th>Lotação</th>
                            <th>Descrição</th>
                        </tr>
                        </thead>
                        <c:forEach var="documento" items="${mobisDocumentosNaoTransferidos}">
                            <tr>
                                <td align="center">
                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
                                            ${documento.codigo}
                                    </a>
                                <td align="center">
                                    <siga:selecionado sigla="${documentodoc.lotaSubscritor.sigla}"
                                                      descricao="${documento.doc.lotaSubscritor.descricao}"/>
                                </td>
                                <td align="center">
                                    <siga:selecionado sigla="${documento}" descricao="${documento}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>


            <c:if test="${not empty mobisDocumentosTransferidos}">
            <input type="hidden" name="itens" value="${mobisDocumentosTransferidos}"/>
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5><fmt:message key='documentos.transferidos'/></h5>
                </div>

                <div class="card-body">
                    <table class="table table-hover table-striped">
                        <col width="22%"/>
                        <col width="5%"/>
                        <col width="4%"/>
                        <col width="4%"/>
                        <col width="5%"/>
                        <col width="4%"/>
                        <col width="4%"/>
                        <col width="4%"/>
                        <col width="4%"/>
                        <col width="44%"/>
                        <thead class="${thead_color} align-middle text-center">
                        <tr>
                            <th rowspan="2" class="text-right">Número</th>
                            <th colspan="3">Documento</th>
                            <th colspan="3">Última Movimentação</th>
                            <th colspan="2"><fmt:message key="tela.tramitarLote.tipoResponsavel"/></th>
                            <th rowspan="2" class="text-left">Descrição</th>
                        </tr>
                        <tr>
                            <th>Data</th>
                            <th>Lotação</th>
                            <th>Pessoa</th>
                            <th>Data</th>
                            <th>Lotação</th>
                            <th>Pessoa</th>
                            <th>Lotação</th>
                            <th>Pessoa</th>
                        </tr>
                        </thead>
                        <c:forEach var="documento" items="${mobisDocumentosTransferidos}">
                            <tr>
                                <td class="text-right">
                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.exMobil.sigla}">
                                            ${documento.exMobil.codigo}
                                    </a>
                                    <c:if test="${not documento.exMobil.geral}">
                                    <input type="hidden" name="itens" value="${documento.exMobil.idMobil}"/>
                                <td>
                                        ${documento.dtDocDDMMYY}
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.lotaSubscritor.sigla}"
                                                      descricao="${documento.lotaSubscritor.descricao}"/>
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.subscritor.iniciais}"
                                                      descricao="${documento.subscritor.descricao}"/>
                                </td>
                                <td>
                                        ${documento.dtMovDDMMYY}
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.lotaSubscritor.sigla}"
                                                      descricao="${documento.lotaSubscritor.descricao}"/>
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.subscritor.iniciais}"
                                                      descricao="${documento.subscritor.descricao}"/>
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.lotaResp.sigla}"
                                                      descricao="${documento.lotaResp.descricao}"/>
                                </td>
                                <td>
                                    <siga:selecionado sigla="${documento.resp.iniciais}"
                                                      descricao="${documento.resp.descricao}"/>
                                </td>
                                </c:if>
                                <c:if test="${documento.exMobil.geral}">
                                    <td>
                                            ${documento.dtDocDDMMYY}
                                    </td>
                                    <td>
                                        <siga:selecionado sigla="${documento.subscritor.iniciais}"
                                                          descricao="${documento.subscritor.descricao}"/>
                                    </td>
                                    <td>
                                        <siga:selecionado sigla="${documento.lotaSubscritor.sigla}"
                                                          descricao="${documento.lotaSubscritor.descricao}"/>
                                    </td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </c:if>
                                <td class="text-left">
                                    <c:choose>
                                        <c:when test="${siga_cliente == 'GOVSP'}">
                                            ${documento.descrDocumento}
                                        </c:when>
                                        <c:otherwise>
                                            ${f:descricaoConfidencial(documento, lotaTitular)}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <input type="hidden" name="campoDe" id="campoDe"
                   value="${lotaTitular.descricao} - ${cadastrante.descricao}"/>
            <input type="hidden" name="campoPara" id="campoPara" value="${mov.respString}"/>
            <input type="hidden" name="campoData" id="campoData" value="${mov.dtRegMovDDMMYYYYHHMMSS}"/>
            <button type="submit" class="btn btn-primary">Gerar Protocolo</button>

            </c:if>
            <button type="button" class="btn btn-primary" onclick="javascript:history.back();">Voltar</button>
    </form>
    </div>
    <br/>
</siga:pagina>