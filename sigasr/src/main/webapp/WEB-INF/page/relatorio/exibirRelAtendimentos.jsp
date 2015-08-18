<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Relatório de Atendimentos">

<jsp:include page="../main.jsp"></jsp:include>

<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
<script src="/sigasr/javascripts/jquery.cookie.js"></script>
<script type="text/javascript">
	$(document).ready(function(){			
		$.removeCookie('fileDownloadToken');
	});
	var fileDownloadCheckTimer;
	var token;
	function blockUIForDownload() {
		token = new Date().getTime(); 
		$('#downloadToken').val(token);
		$.blockUI({
			css: {border: 'none', 
			    padding: '15px', 
			    backgroundColor: '#000', 
			    '-webkit-border-radius': '10px', 
			    '-moz-border-radius': '10px', 
			    'border-radius' : '10px',
			    opacity: .5, 
			    color: '#fff'}, 
			message: '<h2 style="color : #fff;"> Aguarde! Gerando relatório... </h2>' });
		fileDownloadCheckTimer = setInterval(checkDownload, 1000);
	}
	function checkDownload() {
		var cookieValue = $.cookie('fileDownloadToken');
		if (cookieValue == token) {
			finishDownload();
		}
	}
	function finishDownload() {
		clearInterval(fileDownloadCheckTimer);
		$.removeCookie('fileDownloadToken'); 
		$.unblockUI();
	}
</script>

<div class="gt-bd clearfix">
    <div class="gt-content clearfix">
	    <h2>Relatório de Atendimentos</h2>
    	<div class="gt-content-box gt-for-table" style="margin-top: 15px;">
        <form action="${linkTo[RelatorioController].gerarRelAtendimentos}" method="POST" onsubmit="javascript: blockUIForDownload();" >
		 	<table class="gt-form-table">
                <tr class="header">
                	<td align="center" valign="top" colspan="4">Dados do Relatório</td>
                </tr>
                <tr> 
                	<td> Lotação Atendente</td>
                	<td> <siga:selecao2 tipo="lotacao" propriedade="lotacao" tema="simple" modulo="siga"/> </td>		
	    		</tr>
				<tr>
					<td>Data inicial e final</td>
					<td>
						<input type="text" name="dtIni" value="" maxlength="10"
							onblur="javascript:verifica_data(this,true);" theme="simple"/> a
						<input type="text" name="dtFim" value="" maxlength="10" 
							onblur="javascript:verifica_data(this,true);" theme="simple" />
					</td>
				</tr>
				<tr>					
					<td colspan="2">
						<input type="submit" class="gt-btn-medium gt-btn-left" value="Gerar">
						<input type="hidden" name="downloadToken" id="downloadToken" value="" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</siga:pagina>
