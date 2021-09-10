<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="form-group" id="idModGroup">
	<label><fmt:message key="documento.modelo2"/> <span id="idMod-spinner" class="spinner-border text-secondary d-none"></span></label> 
	<select class="form-control siga-select2" id="idMod" name="idMod">
		<c:forEach items="${modelos}" var="item">
			<option value="${item.idMod}" ${item.idMod == idMod ? 'selected' : ''}>
				${item.nmMod}</option>
		</c:forEach>
	</select>
</div>

<script type="text/javascript">
	$(document.getElementById('idMod')).select2({theme: "bootstrap"});	
</script>