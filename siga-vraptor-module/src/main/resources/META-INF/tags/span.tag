<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="id" required="true"%>
<%@ attribute name="depende"%>
<span id="${id}" depende=";${depende};"><!--ajax:${id}--><jsp:doBody/><!--/ajax:${id}--></span>