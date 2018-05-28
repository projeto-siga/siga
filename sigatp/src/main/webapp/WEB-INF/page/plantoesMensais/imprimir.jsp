<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<style type="text/css">

    .tituloPagina {
    }
    
    table {
    }
    
    tr td:first-child, tr td:last-child, tr th:first-child, tr th:last-child {
        background-color: gray;
    }
    
    th {
        text-transform: uppercase;
        font: 15px verdana, arial, sans-serif;
        font-weight: bolder;
        padding: 5px;
    }
    
    td {
        vertical-align: top;
    }
    
    .diaDoMes {
        display: block;
        font: 16px verdana, arial, sans-serif;
        font-weight: bold;
        text-align: center;
        padding: 2px;
    }
    
    .dadosCondutor {
        display: block;
        font: 12px verdana, arial, sans-serif;
        padding: 2px;
        text-align: center;
    }
    
    @media print
    {    
        .naoImprimir, .naoImprimir *
        {
            display: none !important;
        }
    }
    
</style>

<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
        <h2 class="tituloPagina">Plant&atilde;o Mensal - ${dadosParaTitulo}</h2>
        <c:choose>
            <c:when test="${not empty plantoes}">
                <div class="gt-content-box gt-for-table">     
		            <table border="1" cellspacing="0" cellpadding="0">
		                <tr>
		                <c:forEach items="${diaDaSemana.values()}" var="dia">
		                    <th width="14%">${dia.nomeAbreviado}</th>
		                </c:forEach>
		                </tr>
		                
		                <c:set var="cont" value="0"/>
		                <c:forEach items="${plantoes}" var="plantao">
		                 
		                  <c:if test="${cont == 0 && diaDaSemana.ordem != 0}">
		                      <c:forEach items="${plantoes}" begin="0" end="${diaDaSemana.ordem - 1}">
		                          <td>&nbsp;</td>
                                  <c:set var="cont" value="${cont + 1}"/>
		                      </c:forEach>
		                  </c:if>
	                      <td>
                            <span class="diaDoMes"><fmt:formatDate pattern="dd" value="${plantao.dataHoraInicio.time}"/></span>
                            <span class="dadosCondutor">${plantao.condutor.dadosParaExibicao.split("-")[1]}</span>
                          </td>
                             
                          <c:set var="cont" value="${cont + 1}"/>
                          <c:if test="${cont % 7 == 0}">
                             </tr><tr>
                          </c:if>                              
		                </c:forEach>

		                <c:set var="cont" value="${(cont % 7) + 1}"/>
		                
		                <c:forEach items="${plantoes}" begin="${cont}" end="7">
	                        <td>&nbsp;</td>
	                        <c:set var="cont" value="${cont + 1}"/>
		                </c:forEach>
		                </tr>
		             </table>
		        </div>
            </c:when>
            <c:otherwise>
                <br/><h3>O plant&atilde;o mensal informado n&atilde;o existe.</h3>
            </c:otherwise>
        </c:choose>
        
<br />

<input type="button" id="btnImprimir" value="<fmt:message key="views.botoes.imprimir"/>" onClick="window.print();" class="naoImprimir" />
<input type="button" id="btnVoltar"  value="<fmt:message key="views.botoes.voltar"/>" onClick="javascript:location.href='${linkTo[PlantoesMensaisController].listar}'" class="naoImprimir" />

</div>
</div>