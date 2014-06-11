<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<ww:select
	label="Tipo" name="idFormaDoc" id="forma"
	headerKey="0" headerValue="[Todos]"
	list="todasFormasDocPorTipoForma" listKey="idFormaDoc"
	listValue="descrFormaDoc" theme="simple" onchange="javascript:alteraForma();" />