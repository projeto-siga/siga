<c:if test="${empty td or piAnterior != pi}" >
        <c:set var="td" value="${pi.definicaoDeTarefaCorrente}" scope="request"/>
</c:if>
<c:set var="piAnterior" value="${pi}" scope="request"/>
<c:set var="msgAviso" value="${pi.getMsgAviso(titular, lotaTitular)}" />
<c:set var="desabilitarFormulario" value="${pi.isDesabilitarFormulario(titular, lotaTitular)}" />