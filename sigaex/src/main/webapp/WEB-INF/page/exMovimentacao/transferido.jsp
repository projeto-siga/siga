<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<script language="javascript">
		debugger;
		
		function fechaJanela(){
			var sURL = unescape(window.opener.location.pathname);
			window.opener.location.href = window.opener.location.href;
			// Tentar isso para fazer funcionar no chrome: window.opener.location.replace(window.opener.location);
			self.close();
		}
		
		function mostraProtocolo(){
			var width=0.9*screen.width;
			var height=0.9*screen.height;
			var left = screen.width / 2 - width / 2 ;
			var top = screen.height / 2 - height / 2;
			window.open('/sigaex/app/expediente/mov/protocolo_unitario?popup=true&id=${mov.idMov}&sigla=${doc.sigla}','${id}','width='+width+',height='+height+', scrollbars=1, resizable=1, top='+top+', left='+left);
	
		}
		
		function redireciona(){
			self.location.href='/sigaex/app/expediente/doc/exibir?sigla=' + ${doc.sigla};
		}
		
	</script>
	<body onload="javascript:fechaJanela();mostraProtocolo();redireciona();">
	</body>
</html>