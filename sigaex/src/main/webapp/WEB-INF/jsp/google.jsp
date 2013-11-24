<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>
<i>This index references <font
color="#0000cc">G</font><font color="#cccc00">oo</font><font
color="#0000cc">g</font><font color="#00cc00">l</font><font
color="#cc0000">e</font>.com for images.<br>
If you're not currently connected to the Internet you will see broken images.</i><br>
&nbsp;<br>
<table border=0 cellpadding=0 width=10% cellspacing=0>
<tr align=center valign=top>
<td valign=bottom><font face=arial,sans-serif
  size=-1>Result&nbsp;Page:&nbsp;</font></td>
<pg:prev export="pageUrl" ifnull="<%= true %>">
  <% if (pageUrl != null) { %>
    <td align=right><A HREF="<%= pageUrl %>"><IMG
      SRC=http://www.google.com/nav_previous.gif alt="" border=0><br>
    <b>Previous</b></A></td>
  <% } else { %>
    <td><IMG SRC=http://www.google.com/nav_first.gif alt="" border=0></td>
  <% } %>
</pg:prev>
<pg:pages>
  <% if (pageNumber == currentPageNumber) { %>
    <td><IMG SRC=http://www.google.com/nav_current.gif alt=""><br>
    <font color=#A90A08><%= pageNumber %></font></td>
  <% } else { %>
    <td><A HREF="<%= pageUrl %>"><IMG
      SRC=http://www.google.com/nav_page.gif alt="" border=0><br>
    <%= pageNumber %></A></td>
  <% } %>
</pg:pages>
<pg:next export="pageUrl" ifnull="<%= true %>">
  <% if (pageUrl != null) { %>
    <td><A HREF="<%= pageUrl %>"><IMG
      SRC=http://www.google.com/nav_next.gif alt="" border=0><br>
    <b>Next</b></A></td>
  <% } else { %>
    <td><IMG SRC=http://www.google.com/nav_last.gif alt="" border=0></td>
  <% } %>
</pg:next>
</tr>
</table>
