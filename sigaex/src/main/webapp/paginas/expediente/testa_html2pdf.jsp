<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="TransferÃªncia">

<ww:form name="frm" action="testar_pdf_gravar"
				namespace="/expediente/doc" theme="simple" method="GET">
<FCK:editor id="htmlTeste" basePath="/fckeditor/" height="300" toolbarSet="DefaultSemSave">	
</FCK:editor>
<br/>
<ww:select name="htmlTesteFormato" list="#{'pdf':'PDF', 'rtf':'RTF'}" />
<input type="submit" />
</ww:form>
</siga:pagina>