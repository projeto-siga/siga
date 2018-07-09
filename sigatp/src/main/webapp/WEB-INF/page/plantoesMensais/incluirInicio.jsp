<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
        <h2>Incluir Plant&atilde;o mensal</h2>
        
        <sigatp:erros/>
        
        <form action="${linkTo[PlantoesMensaisController].incluir}">
		    <div class="gt-content-box gt-form gt-cols-3 clearfix">
		        <div class="gt-sidebar-left">
		            <label for="mes" class= "obrigatorio">M&ecirc;s</label>
		            <select name="mes">
                        <c:forEach items="${optMes.values()}" var="m">
                            <option value="${m}" <c:if test="${m == mesDefault}">selected</c:if>>${m.nomeExibicao}</option>
                        </c:forEach>
                    </select>
                    
                    <label for= "ano" class= "obrigatorio">Ano</label>
                    <select name="ano">
                        <c:forEach items="${optAno}" var="a">
                            <option value="${a}" <c:if test="${a == anoDefault}">selected</c:if>>${a}</option>
                        </c:forEach>
                    </select>
                    
		            <label for="hora" class= "obrigatorio">Hora de in&iacute;cio do plant&atilde;o</label>
                    <select name="hora">
                        <c:forEach items="${optHora}" var="h">
                            <option value="${h}" <c:if test="${h == horaDefault}">selected</c:if>>${h}</option>
                        </c:forEach>
                    </select>
		       </div>
		    </div>
		    <span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
		    <div class="gt-table-buttons">
		        <input type="submit" value="<fmt:message key="views.botoes.continuar"/>" class="gt-btn-medium gt-btn-left" />
		        <input type="button" id="btnVoltar"  value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[PlantoesMensaisController].listar}'" class="gt-btn-medium gt-btn-left" />
		    </div>
		</form>
    </div>
</div>
</siga:pagina>