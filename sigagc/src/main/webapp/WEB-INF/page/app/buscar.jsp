<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Busca Textual">
	<!-- 	 <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >
	 -->
	<div class="container-fluid">
		<div class="">
			<h2>Busca Textual</h2>
			<c:if test="${not empty success}"> <p class="gt-success">${flash.success}</p> </c:if>
			<div class="gt-sidebar-box gt-sidebar-box-blue">
				<!-- search -->
				<div class="gt-search">
					<div class="gt-search-inner">
						<form id="frm" action="${linkTo[AppController].buscar}" method="get">
							<input type="text" id="texto" name="texto" class="form-control">
								<input type="hidden" id="classificacao" name="classificacao">
						</form>
					</div>
				</div>
				<!-- /search -->
			</div>
		
			<c:if test="${not empty texto}">
				<c:if test="${arvore.getContador() == 0}">
					<p class="gt-notice-box">A busca não retornou resultados.</p>
					<br />
					<div class="gt-cancel" style="float:left;"><a href="/sigagc/app/buscar">voltar</a></div>
				</c:if>
				<c:if test="${arvore.getContador() > 0}">
					<c:if test="${not empty texto}">
						<div style="float:right;"><a href="/sigagc/app/buscar">remover filtro: "${texto}"</a></div>
						<br />	
					</c:if>
					<div>${arvore.toHTML(texto)}</div>
				</c:if>
			</c:if>
			<c:if test="${empty texto}">
				<div>${arvore.toHTML(texto)}</div>
			</c:if>
			
			<c:if test="${not empty classificacao}">
				<div id="gt-cancel-filtro"><a href="/sigagc/app/buscar">Voltar</a></div>	
			</c:if>	
			<c:if test="${empty classificacao}">
				<div id="gt-cancel-filtro" style="display:none;"><a href="/sigagc/app/buscar">Voltar</a></div>
			</c:if>
		</div>
	</div>
	
	<script type="text/javascript">
		$(document).ready(function(){
			var placeHolder = "Digite as palavras da busca e tecle enter";
						
			$("#texto").attr("value",placeHolder).keydown(function() {
				if($(this).val() == placeHolder)
					$("#texto").removeAttr("value");
			}).focusout(function() {
				if($(this).val() == "")
					$("#texto").attr("value", placeHolder);
			});
		    $('form').each(function() {
		        $('input').keypress(function(e) {
		            // Enter pressed?
		            if(e.which == 10 || e.which == 13) {
		                this.form.submit();
		            }
		        });
		    });
	
		    $(".gt-success").delay(5000).fadeOut("slow", "linear");
	
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
	
	<script>try{document.getElementById('texto').focus();document.getElementById('texto').select()}catch(e){};</script>

</siga:pagina>