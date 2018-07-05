<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Testes</title>
</head>
<body>
	<h1>Testes</h1>
	<ol>
		<li><a href="${siga_test_url}">SIGA</a>: ${siga_test}</li>
		<li><a href="${siga_ex_test_url}">SIGA-DOC</a>: ${siga_ex_test}</li>
		<li><a href="${siga_sr_test_url}">SIGA-SR</a>: ${siga_sr_test}</li>
		<li><a href="${siga_gc_test_url}">SIGA-GC</a>: ${siga_gc_test}</li>
		<li><a href="${siga_wf_test_url}">SIGA-WF</a>: ${siga_wf_test}</li>
	</ol>
</body>
</html>