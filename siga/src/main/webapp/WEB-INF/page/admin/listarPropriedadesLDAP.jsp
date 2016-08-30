<%@ page language="java" contentType="text/html; charset=UTF-8"	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<div>
	<p>Propriedades do ambiente</p>
	<ul>
		<li>ambiente  : ${ambiente}</li>
		<li>localidade: ${localidade}</li>
	</ul>
</div>
<br>
<div>
	<p>Propriedades do LDAP</p>
	<ul>
		<li>dnUsuarios: ${dnUsuarios}</li>
		<li>keystore  : ${keystore}</li>
		<li>ssl.porta : ${sslPorta}</li>
		<li>senha     : ${senha}</li>
		<li>servidor  : ${servidor}</li>
		<li>usuario   : ${usuario}</li>
	</ul>
<br>
	<p>prefixo definido: ${prefixo}</p>
	
	<p>O que é prefixo?</p>
	Define os níveis em que o sistema busca a propriede. Por exemplo se o prefixo for a.b.c.d, a propriedade <strong>servidor</strong> pode estar definida em:
	<ul>
		<li>a.b.c.d.servidor</li>
		<li>b.c.d.servidor</li>
		<li>c.d.servidor</li>
		<li>d.servidor</li>
		<li>servidor</li>
	</ul>
</div>
