<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="reler"%>
<%@ attribute name="relertab"%>
<%@ attribute name="obrigatorio"%>

<mod:selecionavel tipo="lotacao" titulo="${titulo}" var="${var}" reler="${reler}" relertab="${relertab}" obrigatorio="${obrigatorio}" />