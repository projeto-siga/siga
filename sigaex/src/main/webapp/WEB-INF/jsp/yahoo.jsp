<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<table width=100% cellpadding=2 cellspacing=0 border=0 bgcolor=e3e9f8>
<tr><td><font face=arial size=2>&nbsp;<b>Matches</b></font>
</td><td align=right nowrap width=1%><font face=arial size=-1>
<pg:index export="total=itemCount">
<pg:page export="first,last">
    <%= first %> - <%= last %> of <%= total %>
</pg:page>
<pg:first export="url" unless="current">
&nbsp;<b><a href="<%= url %>">First Page</a></b>&nbsp;|
</pg:first>
<pg:prev export="url,first,last">
<% int prevItems = (last.intValue() - first.intValue()) + 1; %>
<b><a href="<%= url %>">Previous <%= prevItems %></a></b>
</pg:prev>
<pg:next export="url,first,last">
<% int nextItems = (last.intValue() - first.intValue()) + 1; %>
|&nbsp;<b><a href="<%= url %>">Next <%= nextItems %></a></b>
</pg:next>
 &nbsp;&nbsp;</font></td></tr>
</table>
</pg:index>
