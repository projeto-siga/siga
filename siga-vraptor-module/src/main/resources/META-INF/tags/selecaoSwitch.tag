<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ attribute name="titulo" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="propriedade"%>
<%@ attribute name="tema"%>
<%@ attribute name="modulo" required="false"%>
<%@ attribute name="paramList" required="false"%>
<%@ attribute name="idInicial" required="false"%>
<%@ attribute name="siglaInicial" required="false"%>
<%@ attribute name="descricaoInicial" required="false"%>
<%@ attribute name="inputName" required="false"%>
<%@ attribute name="urlAcao" required="false"%>
<%@ attribute name="urlSelecionar" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="onblur" required="false"%>
<%@ attribute name="prefix" required="false"%>
<%@ attribute name="matricula" required="false"%>

<c:choose>
  <c:when test="${siga_cliente == 'GOVSP'}">
    <siga:selecaoWindow modulo="${modulo}" propriedade="${propriedade}" tema="${tema}" paramList="${paramList}"
                   idInicial="${idInicial}" siglaInicial="${siglaInicial}" descricaoInicial="${descricaoInicial }"
                   inputName="${inputName}" urlAcao="${urlAcao}" urlSelecionar="${urlSelecionar}"
                   onchange="${onchange}" onblur="${onblur}" 
                   prefix="${prefix}" matricula="${matricula}"/>
  </c:when>
  <c:otherwise>
     <siga:selecao     modulo="${modulo}" propriedade="${propriedade}" tema="${tema}" paramList="${paramList}"
                   idInicial="${idInicial}" siglaInicial="${siglaInicial}" descricaoInicial="${descricaoInicial }"
                   inputName="${inputName}" urlAcao="${urlAcao}" urlSelecionar="${urlSelecionar}"
                   onchange="${onchange}" onblur="${onblur}" 
                   prefix="${prefix}" matricula="${matricula}"/>
  </c:otherwise>
</c:choose>
    