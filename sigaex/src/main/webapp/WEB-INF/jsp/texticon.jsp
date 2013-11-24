<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>
<font face="fixed">
<pg:first export="firstPageUrl=pageUrl" unless="current">
  <a href="<%= firstPageUrl %>">|&lt;</a>
</pg:first>
<pg:skip export="skipBackPageUrl=pageUrl" pages="<%= -10 %>">
  <a href="<%= skipBackPageUrl %>">&lt;&lt;&lt;</a>
</pg:skip>
<pg:prev export="prevPageUrl=pageUrl">
  <a href="<%= prevPageUrl %>">&lt;&lt;</a>
</pg:prev>
<pg:pages><%
  if (pageNumber == currentPageNumber) {
    %> <b><%= pageNumber %></b> <%
  } else {
    %> <a href="<%= pageUrl %>"><%= pageNumber %></a> <%
  }
%></pg:pages>
<pg:next export="nextPageUrl=pageUrl">
  <a href="<%= nextPageUrl %>">&gt;&gt;</a>
</pg:next>
<pg:skip export="skipForwardPageUrl=pageUrl" pages="<%= 10 %>">
  <a href="<%= skipForwardPageUrl %>">&gt;&gt;&gt;</a>
</pg:skip>
<pg:last export="lastPageUrl=pageUrl" unless="current">
  <a href="<%= lastPageUrl %>">&gt;|</a>
</pg:last>
</font>
