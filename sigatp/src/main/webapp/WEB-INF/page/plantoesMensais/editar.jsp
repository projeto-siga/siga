<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
        <h2>Editar plant&atilde;o mensal (24 horas)<c:if test="${not empty dadosParaTitulo}"> - ${dadosParaTitulo}</c:if></h2>
        <sigatp:erros/>
        <jsp:include page="form.jsp"/>
    </div>
</div>

</siga:pagina>