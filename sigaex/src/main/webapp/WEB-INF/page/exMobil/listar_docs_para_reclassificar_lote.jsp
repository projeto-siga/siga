<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<c:if test="${not empty itens}">
    <div class="gt-content-box gt-for-table">
        <table class="table table-hover table-striped">
            <thead class="${thead_color} align-middle text-center">
            <tr>
                <th width="3%" align="center">
                    <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                </th>
                <th width="13%" align="right">N&uacute;mero</th>
                <th width="5%" align="center">Classifica&ccedil;&atilde;o Atual</th>
                <th width="15%" align="center">Unidade</th>
                <th width="15%" align="center">Matr&iacute;cula</th>
                <th width="49%" align="center">Descri&ccedil;&atilde;o</th>
            </tr>
            </thead>
            <tbody class="table-bordered">

            <siga:paginador maxItens="50" maxIndices="50" totalItens="${tamanho}"
                            itens="${itens}" var="documento">
                <c:set var="x" scope="request">
                    chk_${documento.idDoc}
                </c:set>
                <tr class="even">
                    <td width="3%" align="center">
                        <input type="checkbox" name="documentosSelecionados"
                               value="${documento.idDoc}" id="${x}" class="chkDocumento" onclick="displaySel()"/>
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