<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<pg:first>
  <a href="<%= pageUrl %>"><nobr>[ #<%= pageNumber %> First ]</nobr></a>
</pg:first>
<pg:skip pages="<%= -5 %>">
  <a href="<%= pageUrl %>"><nobr>[ #<%= pageNumber %> Skip ]</nobr></a>
</pg:skip>
<pg:prev>
  <a href="<%= pageUrl %>"><nobr>[ #<%= pageNumber %> Prev ]</nobr></a>
</pg:prev>
<pg:pages>
   <a href="<%= pageUrl %>"><%= pageNumber %></a> 
</pg:pages>
<pg:next>
  <a href="<%= pageUrl %>"><nobr>[ Next #<%= pageNumber %> ]</nobr></a>
</pg:next>
<pg:skip pages="<%= 5 %>">
  <a href="<%= pageUrl %>"><nobr>[ Skip #<%= pageNumber %> ]</nobr></a>
</pg:skip>
<pg:last>
  <a href="<%= pageUrl %>"><nobr>[ Last #<%= pageNumber %> ]</nobr></a>
</pg:last>
