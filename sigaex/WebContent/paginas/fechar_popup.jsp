<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<script language="javascript">
	function fechaJanela(){
		var sURL = unescape(window.opener.location.pathname);
		window.opener.location.href=window.opener.location.href;
		self.close();
	}
</script>
<body onload="javascript:fechaJanela();">
</body>
</html>
