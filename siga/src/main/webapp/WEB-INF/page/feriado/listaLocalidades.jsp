<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<select name="idLocalidade" class="form-control">
	<c:forEach var="item" items="${listaLocalidades}">
		<option value="${item.idLocalidade}">${item.nmLocalidade}</option>
	</c:forEach>
</select>
