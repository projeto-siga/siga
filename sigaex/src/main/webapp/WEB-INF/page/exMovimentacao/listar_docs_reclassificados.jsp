<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"
         buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina titulo="Documentos reclassificados" popup="true">

    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Documentos reclassificados</h5>
            </div>
        </div>

        <c:if test="${not empty movsDocumentosNaoArquivadosCorrente}">
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5>Documentos n&atilde;o reclassificados</h5>
                </div>

                <div class="card-body">
                    <table class="table table-hover table-striped">
                        <thead class="${thead_color} align-middle text-center">
                        <tr>
                            <th align="center">N&uacute;mero</th>
                            <th>Classifica&ccedil;&atilde;o Atual</th>
                            <th><fmt:message key="usuario.lotacao"/></th>
                            <th class="text-left">Mensagem</th>
                        </tr>
                        </thead>
                        <c:forEach var="mov" items="${movsDocumentosNaoReclassificados}">
                            <tr>
                                <td align="center">
                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mov.exMobil.sigla}">
                                            ${mov.exMobil.codigo}
                                    </a>
                                <td align="center">
                                    <siga:selecionado sigla="${mov.exMobil.exDocumento.lotaSubscritor.sigla}"
                                                      descricao="${mov.exMobil.exDocumento.lotaSubscritor.descricao}"/>
                                </td>
                                <td align="center">
                                    <siga:selecionado sigla="${mov.exMobil}" descricao="${mov.exMobil}"/>
                                </td>
                                <td class="text-left">${mov.descrMov}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty mobisDocumentosReclassificados}">
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5>Documentos reclassificados com sucesso</h5>
                </div>

                <div class="card-body">
                    <table class="table table-hover table-striped">
                        <thead class="${thead_color} align-middle text-center">
                        <tr>
                            <th align="center">N&uacute;mero</th>
                            <th>Classifica&ccedil;&atilde;o Atual</th>
                            <th><fmt:message key="usuario.lotacao"/></th>
                            <th class="text-left">Descri&ccedil;&atilde;o</th>
                        </tr>
                        </thead>
                        <tbody class="table-bordered">
                        <c:forEach var="mobil" items="${mobisDocumentosReclassificados}">
                            <tr>
                                <td class="text-right">
                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobil.sigla}">
                                            ${mobil.codigo}
                                    </a>
                                </td>
                                <td class="text-center">${mobil.doc.exClassificacaoAtual}</td>
                                <td class="text-center">
                                    <siga:selecionado sigla="${mobil.doc.lotaSubscritor.sigla}"
                                                      descricao="${mobil.doc.lotaSubscritor.descricao}"/>
                                </td>
                                <td class="text-left">
                                    <c:choose>
                                        <c:when test="${siga_cliente == 'GOVSP'}">
                                            ${mobil.doc.descrDocumento}
                                        </c:when>
                                        <c:otherwise>
                                            ${f:descricaoConfidencial(mobil, lotaTitular)}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </c:if>
        <a href="/sigaex/app/expediente/mov/reclassificar_lote" class="btn btn-cancel btn-primary">Voltar</a>
    </div>
</siga:pagina>