<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:catch var="catchException">
	<siga:pagina titulo="Siga - Logout" desabilitarbusca="sim"  meta="<META HTTP-EQUIV='refresh' CONTENT='1;URL='/siga/'>" >
		
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>
					SIGA - Logout
				</h5>
			</div>
			<div class="card-body">
				<h3>Saindo...</h3>
			</div>
		</div>
	</div>
		
	
	</siga:pagina>
</c:catch>