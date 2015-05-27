<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Edi&ccedil;&atilde;o de Lista">

	<jsp:include page="../main.jsp"></jsp:include>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/template.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script> 
	
	<div class="gt-form gt-content-box"
		style="width: 800px !important; max-width: 800px !important;">
		<form id="formLista" method="post" enctype="multipart/form-data">
			<input type="hidden" id="idLista" name="idLista" value="${lista.idLista}"> 
			<input type="hidden" id="id" name="id" value="${lista.idLista}"> 
			<input type="hidden" id="hisIdIni" name="hisIdIni" value="${lista.hisIdIni}">
		
		</form>
	</div>
	
	
</siga:pagina>

