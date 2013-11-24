<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>
<style type="text/css">
<!--
.resultInfo {
   color:#f80;
   background-color: transparent;
   text-transform:Uppercase;
   padding: 5px 5px 5px 0px;
   margin: 0;
   font-size: .7em;
}
.rnav {
    padding: 0;
    font-family: Verdana, Arial, Helvetica, Sans-serif;
    font-size: 1em;
    color:#333;
    background-color:#fff;
    font-weight:bold; 
    font-size: .7em;
}
.rnavLabel {
    text-transform: Uppercase;
    color:#f80;
    background-color: transparent;
}
a.rnavLink {
    color: #415481;
    background-color: transparent;
}
a:visited.rnavLink {
    color: #8A9CBD;
    background-color: transparent;
}
a:hover.rnavLink {
    color: #f80;
    text-decoration: none;
    background-color: transparent;
}
-->
</style>
<pg:index export="totalItems=itemCount">
<pg:page export="firstItem, lastItem">
<div class="resultInfo">
Displaying results <strong><%= firstItem %>-<%= lastItem %></strong> of <strong><%= totalItems %></strong> found
</div>
</pg:page>

<div class="rnav">
<span class="rnavLabel">Results:</span>&nbsp;
<pg:prev export="pageUrl">
<a href="<%= pageUrl %>" class="rnavLink">&#171;&nbsp;Prev</a>&nbsp;
</pg:prev>
<pg:pages export="pageUrl,pageNumber,firstItem,lastItem">
    <% if (pageNumber == currentPageNumber) { %> 
&nbsp;<span class="rnavCurr"><%= firstItem %>-<%= lastItem %></span>
    <% } else { %>
&nbsp;<a href="<%= pageUrl %>" class="rnavLink"><%= firstItem %>-<%= lastItem %></a>
    <% } %>
</pg:pages>
<pg:next export="pageUrl">
&nbsp;&nbsp;<a href="<%= pageUrl %>" class="rnavLink">Next&nbsp;&#187;</a>
</pg:next>
</div>
</pg:index>
