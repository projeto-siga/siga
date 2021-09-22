<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="Índice">
	<div class="container-fluid">
		<h2>Árvore de Conhecimentos</h2>
		<c:choose>
			<c:when test="${arvore.getContador() == 0}">
				<p class="alert alert-warning">Nenhum
					conhecimento com uma classificação para ser listado.</p>
			</c:when>
			<c:otherwise>
				${arvore.toHTML('')}
			</c:otherwise>
		</c:choose>
	</div>
</siga:pagina>

<script type="text/javascript">
	$(document).ready(function(){
		var placeHolder = "Digite as palavras da busca e tecle enter";
				
		$("[id^='imgMenos']").hide();

		$("[id^='imgMenos'],[id^='imgMais']").css({"margin-left" :"10px"});
			
		$("[id^='imgMenos'],[id^='imgMais']").mouseenter(function(){
			$(this).css({"cursor": "pointer", "opacity":"0.8","filter":"alpha(opacity=70)"});
		}).mouseleave(function(){
			$(this).css({"cursor": "default","opacity":"1","filter":"alpha(opacity=100)"});
		});
		$("[id^='imgMenos']").click(function(){	
			var idImg = $(this).attr("id");
			var id = idImg.split("imgMenos")[1];
			$(this).hide("fast");
			$("#imgMais" + id).show("fast");
			$(".li" + id).hide("fast");
		});		
		$("[id^='imgMais']").click(function(){	
			var idImg = $(this).attr("id");
			var id = idImg.split("imgMais")[1];
			$(this).hide("fast");
			$("#imgMenos" + id).show("fast");
			$(".li" + id).show("fast");
		});	

		$("ul > li > b > a").click(function(){	
			var classif = $(this).text();
			$("#classificacao").val(classif);

			if ($("#classificacao").val() != null & $("#classificacao").val() != "Conhecimentos_Sem_Classificacao") {
				 if($("#texto").val() == placeHolder)
						$("#texto").removeAttr("value");
				 $('#frm').submit();
			}
		});	
		
		$("[class^='classificacao']").mouseover(function(){	
			var classificacao = $(this).text();
			if(classificacao != "Conhecimentos_Sem_Classificacao"){
				$(this).css({"cursor": "pointer", "background-color": "yellow"});
			}
		});
		$("[class^='classificacao']").mouseleave(function(){
			$(this).css({"background-color": "#f1f4e2"});
		});

	});
</script>