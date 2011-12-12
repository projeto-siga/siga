<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<c:set var="xml">
	<c:import url="/paginas/menus/xml/barranavxml.jsp"></c:import>
</c:set>

<c:set var="xsl">
	<c:import url="/sigalibs/barranav.xsl"></c:import>
</c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<div style="width: 100%;height: 13px; background-color: gold; font-family: verdana; font-size: 10px; ">
<span style="padding-left: 10px;font-weight: bold">P&aacute;gina atual:</span>
	<c:choose>
		<c:when test="${empty idpage}">
			<x:transform xml="${xml}" xslt="${xsl}" />
		</c:when>
		<c:otherwise>
			<x:transform xml="${xml}" xslt="${xsl}">
				<x:param name="id" value="${idpage}" />
			</x:transform>
		</c:otherwise>
	</c:choose>
</div>
</html>
