<html>
<head>
    <title>SIGA - Provedor de identidades</title>
    <!-- 
    <META HTTP-EQUIV="refresh" CONTENT="0;URL=/siga">
    -->
</head>
<body>
<h1> Siga IDP </h1>
<hr/>
<div>
	<%= request.getUserPrincipal() != null ? "<div align='right'> Logged as "+request.getUserPrincipal().getName()+"</div>" : "" %>
</div>
</body>
</html>