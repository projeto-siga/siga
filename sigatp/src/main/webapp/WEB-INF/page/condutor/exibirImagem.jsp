<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
	 .imagem {
	   height: 500px;
	   border: 1px solid #000;
	   margin: 10px 5px 0 0;
	 }
</style>

<siga:pagina>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<img id="imgArquivo" class="imagem" src="${imgArquivo}" />
		</div>
	</div>
</siga:pagina>