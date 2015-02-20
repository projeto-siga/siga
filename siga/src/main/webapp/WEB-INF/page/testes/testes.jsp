<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="32kb"%>
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
			<li><a href="${siga_wf_test_url}">SIGA-WF</a>: ${siga_wf_test}</li>
			<li><a href="${siga_cd_test_url}">SIGA-CD</a>: ${siga_cd_test}</li>
		</ol>
	</body>
</html>