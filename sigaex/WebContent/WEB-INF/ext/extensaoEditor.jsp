<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<tags:fixeditor var="${var}">
	<td colspan="3"><FCK:editor id="xxxeditorxxx"
		basePath="/fckeditor/" height="300" toolbarSet="${toolbarSet}">${v}<c:if
			test="${empty v}">
			<p style="TEXT-INDENT: 2cm" align="justify">&nbsp;</p>
		</c:if>
	</FCK:editor></td>
</tags:fixeditor>



