<script>
$(function(){
    $('#formSubmit').submit(function() {
    	$.ajax({
 	  	   type: "POST",
 	  	   async: false,
 	  	   data:  $("#formSubmit").serialize(),
 	  	   url: "/sigasr/solicitacao/responderPesquisa"
 	  	});
    });
});
</script>
<div class="gt-bd gt-cols clearfix" style="padding: 20px;">
<div class="gt-content-box gt-for-table gt-form" style="margin-top: 15px;">
		<form action="@Application.responderPesquisaGravar()" id="formSubmit" enctype="multipart/form-data" onSubmit="javascript: return block();">
		 <input type="hidden" name="id" value="${id}" />
		 <input type="hidden" name="completo" value="${completo}" />
		<c:forEach items="${pesquisa.perguntaSet}" var="pergunta">
		<c:choose>
		<c:when test="${pergunta.tipoPergunta.idTipoPergunta == 1}">
			<div class="gt-form-row">
				<label>${pergunta.descrPergunta}</label><p>
				<textarea cols="50" rows="6" id="idPergunta"
					name="respostaMap[${pergunta.idPergunta}]"></textarea>
			</div> 
		</c:when>
		<c:when test="${pergunta.tipoPergunta.idTipoPergunta == 2}">
		<div class="gt-form-row">
				<label>${pergunta.descrPergunta}</label>
				<c:forEach items="${SrGrauSatisfacao.values()}" var="grau">
				<input type="radio" id="idGrauSatisfacao" name="respostaMap[${pergunta.idPergunta}]" value="${grau}">&nbsp;&nbsp;${grau.descrGrauSatisfacao}&nbsp;
				</c:forEach>
		</div>
		</c:when>
		</c:choose>
		</c:forEach>
		<div class="gt-form-row">
				<input type="submit" value="Enviar" class="gt-btn-medium gt-btn-left" 
					  />
		</div>
		</form>
</div>
</div>