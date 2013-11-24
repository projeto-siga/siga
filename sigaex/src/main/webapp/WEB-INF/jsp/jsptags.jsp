<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>
<i>This index references JSPTags.com for images.<br>
If you're not currently connected to the Internet you will see broken images.</i><br>
&nbsp;<br>
<table bgcolor="#ffcc00" border="0" cellspacing="0" cellpadding="2">
<tr><td><table bgcolor="#003399" width="100%" border="0" cellspacing="0" cellpadding="6">
<tr><td align="center"><table border="0" cellspacing="0" cellpadding="0">
<tr><td width="15" height="21"><img src="http://jsptags.com/images/pager/left.gif" width="15" height="21" border="0"></td>
<th height="21" bgcolor="#3366cc"
    background="http://jsptags.com/images/pager/bg.gif"
    nowrap><font face="Lucida,San-Serif,Arial,Helvetica">
<pg:prev export="pageUrl">
  <a href="<%= pageUrl %>" class="nodec"><font
    color="#ffcc00">&lt;&lt;</font></a>
</pg:prev>
<pg:pages>&nbsp;<%
  if (pageNumber == currentPageNumber) {
    %><font color="#ffffff"><%= pageNumber %></font><%
  } else {
    %><a href="<%= pageUrl %>" class="nodec"><font
      color="#ffcc00"><%= pageNumber %></font></a><%
  }
%>&nbsp;</pg:pages>
<pg:next export="pageUrl">
  <a href="<%= pageUrl %>" class="nodec"><font
    color="#ffcc00">&gt;&gt;</font></a>
</pg:next>
</font></th>
<td width="17" height="21"><img src="http://jsptags.com/images/pager/right.gif"
width="17" height="21" border="0"></td></tr>
</table></td></tr>
</table></td></tr>
</table>
