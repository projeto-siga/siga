<%@ tag body-content="empty"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="assinante"%>
<%@ attribute name="idmov"%>
<ww:url id="url" action="assinar_mov_verificar" namespace="/expediente/mov">
	<ww:param name="id">${idmov}</ww:param>
</ww:url>
<span id="verificar_assinatura_${idmov}"> ${assinante} | <a
	href="javascript: SetInnerHTMLFromAjaxResponse('${url}', 'verificar_assinatura_${idmov}');">Verificar</a>
</span>
<span id="verificar_assinatura2_${idmov}"> </span>

