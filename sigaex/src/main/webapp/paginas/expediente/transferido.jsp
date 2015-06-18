<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<html>
<script language="javascript">
	<%--<ww:url id="url" action="protocolo_unitario" namespace="/expediente/mov">
		<ww:param name="popup">true</ww:param>
		<ww:param name="id">${mov.idMov}</ww:param>
	</ww:url>--%>
	function mostraProtocolo(){
		var width=0.9*screen.width;
		var height=0.9*screen.height;
		var left = screen.width / 2 - width / 2 ;
		var top = screen.height / 2 - height / 2;
		window.open('/sigaex/expediente/mov/protocolo_unitario.action?popup=true&id=${mov.idMov}','${id}','width='+width+',height='+height+', scrollbars=1, resizable=1, top='+top+', left='+left);

	}
	
	<ww:url id="url" action="exibir" namespace="/expediente/doc">
		<ww:param name="id">${doc.idDoc}</ww:param>
	</ww:url>
	function redireciona(){
		self.location.href='<ww:property value="%{url}"/>';
	}
	
</script>
<body onload="javascript:mostraProtocolo();redireciona();">
</body>
</html>