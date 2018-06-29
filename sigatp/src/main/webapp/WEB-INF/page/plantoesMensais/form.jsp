<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<form action="${linkTo[PlantoesMensaisController].salvar}" method="POST">
    <input type="hidden" id="dadosParaTitulo" name="dadosParaTitulo" value="${dadosParaTitulo}" />
    <input type="hidden" id="mes" name="mes" value="${mes}" />
    <input type="hidden" id="ano" name="ano" value="${ano}" />
    <input type="hidden" id="hora" name="hora" value="${hora}" />
    
    <div class="gt-content-box gt-for-table tabelaFormGrande">     
        <table class="gt-table" >
            <c:set var="cont" value="0"/>
            <c:forEach items="${plantoes}" var="plantao">
			    <tr>
			    <th><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantoes[cont].dataHoraInicio.time}"/></th>
				    <td>
				        <input type="hidden" name="plantoes[${cont}].id" value="${plantao.id}" />
				        <input type="hidden" name="plantoes[${cont}].referencia" value="${plantao.referencia}" />
				        <input type="hidden" name="plantoes[${cont}].dataHoraInicio" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantoes[cont].dataHoraInicio.time}"/>" />
				        <input type="hidden" name="plantoes[${cont}].dataHoraFim" value="<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${plantoes[cont].dataHoraFim.time}"/>" />
				        <select name="plantoes[${cont}].condutor.id">
				            <c:forEach items="${condutores}" var="condutor">
				                <option value="${condutor.id}" <c:if test="${plantao.condutor.id == condutor.id}">selected</c:if>>${condutor.dadosParaExibicao}</option>
				            </c:forEach>
				        </select>
				        <c:set var="cont" value="${cont + 1}"/>
				    </td>
			    </tr>
		   </c:forEach>
        </table>
    </div>
        
	<div id="btnAcoes" class="gt-table-buttons">
	    <input id="btnSalvar" type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
	    <input type="button" id="btnVoltar"  value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[PlantoesMensaisController].listar}'" class="gt-btn-medium gt-btn-left" />
	</div>
    
</form>