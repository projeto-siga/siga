<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="somenteOrdenacao"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="${pageContext.request.contextPath}/public/javascripts/tablesorter/jquery.tablesorter.min.js"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/public/javascripts/tablesorter/jquery.tablesorter.pager.css"></script> 
<script type="text/javascript" src="${pageContext.request.contextPath}/public/javascripts/tablesorter/jquery.tablesorter.pager.js"></script> 

<script>
	 $(function() 
	 { 
			$("#htmlgrid").tablesorter();
			var somenteOrdenacao="${somenteOrdenacao}";
			if (somenteOrdenacao=="" || somenteOrdenacao!="sim") {
		         $("#htmlgrid").tablesorterPager({container: $("#pager")});
		    } 
			else {
				$("#pager").hide();
			}			
	 }); 
</script>

<div id="pager" class="pager">
	<form>
		<img src="/sigatp/public/javascripts/tablesorter/first.png" class="first"/> 
   		<img src="/sigatp/public/javascripts/tablesorter/prev.png" class="prev" />
   		<input type="text" class="pagedisplay" size="4" /> 
   		<img src="/sigatp/public/javascripts/tablesorter/next.png" class="next" />
   		<img src="/sigatp/public/javascripts/tablesorter/last.png" class="last" /> 

   		<select class="pagesize">
    		<option value="5">5</option>
    		<option selected="selected" value="10">10</option>
    		<option value="20">20</option>
    		<option value="30">30</option>
    		<option value="40">40</option>
   		</select>
  	</form>
 </div>