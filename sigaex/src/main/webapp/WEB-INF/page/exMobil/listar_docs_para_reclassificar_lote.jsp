<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim" iframe="sim">
    
    <c:if test="${not empty itens}">
        <p>Atenção: Na Reclassifica&ccedil;&atilde;o em Lote – Permitido até 200 documentos por opera&ccedil;&atilde;o.</p>
        <div class="gt-content-box gt-for-table">
            <table class="table table-hover table-striped">
                <thead class="${thead_color} align-middle text-center">
                <tr>
                    <th width="3%" align="center">
                        <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                    </th>
                    <th width="13%" align="right">N&uacute;mero</th>
                    <th width="5%" align="center">Classifica&ccedil;&atilde;o Atual</th>
                    <th width="15%" align="center"><fmt:message key="usuario.lotacao"/></th>
                    <th width="15%" align="center">Matr&iacute;cula</th>
                    <th width="49%" align="center">Descri&ccedil;&atilde;o</th>
                </tr>
                </thead>
                <tbody class="table-bordered">

                <siga:paginador maxItens="200" maxIndices="50" totalItens="${tamanho}"
                                itens="${itens}" var="documento">
                    <tr class="even">
                        <td width="3%" align="center">
                            <input type="checkbox" name="documentosSelecionados"
                                   value="${documento.sigla}" id="${documento.idDoc}" class="chkDocumento"/>
                        </td>
                        <td width="13%" align="right">${documento.sigla}</td>
                        <td width="5%" align="center">${documento.classificacaoSigla}</td>
                        <td width="15%" align="center">${documento.lotaCadastranteString}</td>
                        <td width="15%" align="center">${documento.cadastranteString}</td>
                        <td width="49%" align="left">${documento.descrDocumento}</td>
                    </tr>
                </siga:paginador>
                </tbody>
            </table>
        </div>
    </c:if>

    <script type="text/javascript">
        function sbmt(offset) {
            listarDocumentosParaReclassificarEmLote(offset);
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