<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Siga - Testes" desabilitarbusca="sim"  >


	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>
					Testes
				</h5>
			</div>
			<div class="card-body">
				<ol>
					<li><a href="${siga_test_url}">SIGA</a>: ${siga_test}</li>
					<li><a href="${siga_ex_test_url}">SIGA-DOC</a>: ${siga_ex_test}</li>
					<li><a href="${siga_sr_test_url}">SIGA-SR</a>: ${siga_sr_test}</li>
					<li><a href="${siga_gc_test_url}">SIGA-GC</a>: ${siga_gc_test}</li>
					<li><a href="${siga_wf_test_url}">SIGA-WF</a>: ${siga_wf_test}</li>
				</ol>
			</div>
		</div>
	</div>

</siga:pagina>