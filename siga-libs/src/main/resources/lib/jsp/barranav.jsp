<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<c:set var="xml">
	<c:import url="/paginas/menus/xml/barranav.xml"></c:import>
</c:set>

<c:set var="xsl">
	<c:import url="/sigalibs/barranav.xsl"></c:import>
</c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<div
	style="font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10px; width: 100%; background-color: gold;">
<span style="padding: 2px; padding-left: 8px;float:left;">P&aacute;gina
atual: <c:choose>
	<c:when test="${empty idpage}">
		<x:transform xml="${xml}" xslt="${xsl}" />
	</c:when>
	<c:otherwise>
		<x:transform xml="${xml}" xslt="${xsl}">
			<x:param name="id" value="${idpage}" />
		</x:transform>
	</c:otherwise>
</c:choose> </span><%-- <span align="right" style="float:right;padding: 2px;padding-right: 8px;color:red;">${env}</span>--%></div>
</html>
