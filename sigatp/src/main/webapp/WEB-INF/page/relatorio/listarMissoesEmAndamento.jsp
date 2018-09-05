<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="Transportes">
	<script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization', 'version':'1','packages':['timeline']}]}"></script>
	<script type="text/javascript">
		google.setOnLoadCallback(drawChart);
	
		function drawChart() {
		    var isIE10 = false;
		    /*@cc_on
		        if (/^10/.test(@_jscript_version)) {
		            isIE10 = true;
		        }
		    @*/
			
	  		var container = document.getElementById('missoes');
	
	  		if ("${registros}" != ""){
	  		
		  		var chart = new google.visualization.Timeline(container);
		  		var dataTable = new google.visualization.DataTable();
		
				dataTable.addColumn({ type: 'string', id: 'Veículo' });
		  		dataTable.addColumn({ type: 'string', id: 'Condutor' });
		  		dataTable.addColumn({ type: 'date', id: 'Start' });
		  		dataTable.addColumn({ type: 'date', id: 'End' });
	
				dataTable.addRows([
					${registros}
		    	]);
	
			    var options = {
		    		    timeline: { colorByRowLabel: true },
		    		    enableInteractivity: !isIE10	    		    
					  };
	
	        	chart.draw(dataTable,options);
		        
	  		} else
				container.innerHTML = "<h3>N&atilde;o existem miss&otilde;es em andamento.</h3>";
	}
	</script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		<h2>Miss&otilde;es em andamento</h2>
			<div class="gt-content-box gt-for-table">
			<table id="htmlgrid" class="gt-table" >
				<tr>
					<td> 
						<div id="missoes" ></div>
					</td>
				</tr>
			</table>	
			</div>	
		</div>
	</div>
</siga:pagina>