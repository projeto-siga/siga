<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<div class="form-group" id="idFormaDocGroup">
	<label><fmt:message key="documento.label.especie"/> <span id="idFormaDoc-spinner" class="spinner-border text-secondary d-none"></span></label> 
	<select class="form-control siga-select2" id="idFormaDoc" name="idFormaDoc" onchange="javascript:alteraForma(false);">
		<option value="0">[Todos]</option>
		<c:forEach items="${todasFormasDocPorTipoForma}" var="item">
			<option value="${item.idFormaDoc}"
				${item.idFormaDoc == idFormaDoc ? 'selected' : ''}>
				${item.siglaFormaDoc} - ${item.descrFormaDoc}
			</option>
		</c:forEach>
	</select>
</div>


<script type="text/javascript">
	$(document.getElementById('idFormaDoc')).select2({theme: "bootstrap"});	
</script>