<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina desabilitarbusca="sim" desabilitarmenu="sim" iframe="sim">

	<c:if test="${not empty itens}">  
        <p>Aten&ccedil;&atilde;o: No Recebimento em Lote – Permitido até 50 documentos por opera&ccedil;&atilde;o.</p>
        
            <table border="0" class="table table-hover">
                <thead class="${thead_color} align-middle text-center">
                <tr>
                    <th rowspan="2" align="text-center">
                        <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                    </th>
                    <th rowspan="2" align="text-right">N&uacute;mero</th>
                    <th colspan="3" align="text-center">Documento</th>
                    <th colspan="2" align="text-center">&uacute;ltima Movimenta&ccedil;&atilde;o</th>
                    <th rowspan="2">Descri&ccedil;&atilde;o</th>  
                </tr>
                </thead>
                <thead class="${thead_color} align-middle text-center">
					<th></th>
					<th></th>
					<th align="text-center">Data</th>
					<th align="text-center">Lotação</th>
					<th align="text-center">Pessoa</th>
					<th align="text-center">Data</th>
					<th align="text-center">Pessoa</th>
					<th></th>
				</thead>
				
                <tbody class="table-bordered">
                <siga:paginador maxItens="50" maxIndices="10" totalItens="${tamanho}"
					itens="${itens }" var="mobil">
                    <tr class="even"> 
                        <td width="3%" align="center">
                            <input type="checkbox" name="documentosSelecionados"
                                   value="${mobil.sigla}" id="${mobil.id}" class="chkMobil"/>
                        </td>
                        <td width="11.5%" align="right"><a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${mobil.sigla}">${mobil.sigla}</a></td>
						
						<c:if test="${not mobil.geral}">
							<td width="5%" align="center">${mobil.doc.dtDocDDMMYY}</td>
							<td width="5%" align="center"><siga:selecionado
								sigla="${mobil.doc.lotaSubscritor.sigla}"
								descricao="${mobil.doc.lotaSubscritor.descricao}" /></td>
							<td width="5%" align="center"><siga:selecionado
								sigla="${mobil.doc.subscritor.iniciais}"
								descricao="${mobil.doc.subscritor.descricao}" /></td>
							<td width="5%" align="center">${mobil.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${mobil.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
								descricao="${mobil.ultimaMovimentacaoNaoCancelada.resp.descricao}" /></td>
						</c:if>
						<c:if test="${mobil.geral}">
							<td width="5%" align="center">${mobil.doc.dtDocDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${mobil.doc.subscritor.iniciais}"
								descricao="${mobil.doc.subscritor.descricao}" /></td>
							<td width="4%" align="center"><siga:selecionado
								sigla="${mobil.doc.lotaSubscritor.sigla}"
								descricao="${mobil.doc.lotaSubscritor.descricao}" /></td>
							<td width="5%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="10.5%" align="center"></td>
						</c:if> 
						<td width="44%">
							<c:choose>
								<c:when test="${siga_cliente == 'GOVSP'}">
									${mobil.doc.descrDocumento}
								</c:when>
								<c:otherwise>
									${f:descricaoSePuderAcessar(mobil.doc, titular, lotaTitular)}
								</c:otherwise>
							</c:choose>
						</td>
                    </tr>
                </siga:paginador>
                
                </tbody>
            </table>
        </div>
    </c:if>
    
    <script type="text/javascript">
        function sbmt(offset) {
        	listarDocumentosParaReceberEmLote(offset);
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