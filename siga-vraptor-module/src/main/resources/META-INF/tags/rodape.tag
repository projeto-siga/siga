
<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ attribute name="popup"%>
<%@ attribute name="pagina_de_erro"%>
<%@ attribute name="incluirJs"%>

<!--[if gte IE 5.5]><script language="JavaScript" src="/siga/javascript/jquery.ienav.js" type="text/javascript"></script><![endif]-->


<script src="/siga/javascript/jquery/jquery-migrate-1.2.1.min.js"
	type="text/javascript"></script>

<script src="/siga/javascript/siga.js"
	type="text/javascript" charset="utf-8"></script>

<script
	src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"
	type="text/javascript"></script>
<link rel="stylesheet" href="/siga/javascript/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" type="text/css" media="screen, projection">
<script src="/siga/popper-1-14-3/popper.min.js"></script>
<script language="JavaScript" src="/siga/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script language="JavaScript" src="/siga/javascript/datepicker-pt-BR.js" type="text/javascript"></script>

<c:if test="${not empty incluirJs}">
	<script src="${incluirJs}" type="text/javascript"></script>
</c:if>




<script type="text/javascript">
	$(document).ready(function() {
		$('.links li code').hide();
		$('.links li p').click(function() {
			$(this).next().slideToggle('fast');
		});
		$('.once').click(function(e) {
			if (this.beenSubmitted)
				e.preventDefault();
			else
				this.beenSubmitted = true;
		});
		//$('.autogrow').css('overflow', 'hidden').autogrow();
	});
</script>

<script>
	$('.dropdown-menu a.dropdown-toggle').on(
			'click',
			function(e) {
				if (!$(this).next().hasClass('show')) {
					$(this).parents('.dropdown-menu').first().find('.show')
							.removeClass("show");
				}
				var $subMenu = $(this).next(".dropdown-menu");
				$subMenu.toggleClass('show');

				$(this).parents('li.nav-item.dropdown.show').on(
						'hidden.bs.dropdown', function(e) {
							$('.dropdown-submenu .show').removeClass("show");
						});

				return false;
			});
</script>


<c:if test="${siga_cliente == 'GOVSP' and popup != true}">
	<footer class="text-center text-white align-middle" style="background-color: #20313b;">
		<div class="container">						
			<div class="content pt-2">
				<c:if test="${!f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA')}">
					<div class="row">
						<div class="col-md-4">
							<div class="row">	
								<div class="col-12 text-left text-white">
									<h5>Sem Papel</h5>		              
									<p>Eficiência, agilidade e respeito ao meio ambiente</p> 
								</div>
							</div>
							<div class="row">
								<div class="col-6">
									<a href="http://www10.trf2.jus.br/portal/" role="link"><img class="mx-auto d-block" src="/siga/imagens/topo-logo-trf2.png" style="width:100%"></a>
								</div>
								<div class="col-6">
									<a href="http://www.prodesp.sp.gov.br/" role="link"><img class="mx-auto d-block" src="/siga/imagens/logo-prodesp-branco-e-azul.png" style="width:90%"></a>
								</div>
							</div>
						</div>
						<div class="col-md-4 col-xs-3">		
							<div style="padding-top:10px;">
								<img class="" src="/siga/imagens/logo-siga-doc-sp-colorido.png" width="65%">
							</div>
						</div>
						<div class="col-md-4">
							<div class="row mt-2">
								<div class="col-12" style="padding-top:5px;">
									<a href="http://www.saopaulo.sp.gov.br/" role="link"><img class="mx-auto d-block" src="/siga/imagens/logo-gesp-slogan-horizontal-cor-texto-branco.png" alt="Governo do Estado de São Paulo" width="50%"></a></p>
								</div>
							</div>
						</div>			
					</div>
				</c:if>
				<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA')}">
					<div class="row mt-2">
						<div class="col-md-4">
							<div class="text-left text-white small">
								<h5>Sem Papel</h5>		              
								<p class="">Eficiência, agilidade e respeito ao meio ambiente</p> 
							</div>
						</div>
						<div class="col-md-4">
							<div style="padding-top:10px;">
								<img class="" src="/siga/imagens/logo-siga-doc-sp-colorido.png" width="50%">
							</div>
							
						</div>
						<div class="col-sm-4" style="padding-top:5px;">
							<a href="http://www.saopaulo.sp.gov.br/" role="link"><img class="mx-auto d-block" src="/siga/imagens/logo-gesp-slogan-horizontal-cor-texto-branco.png" alt="Governo do Estado de São Paulo" width="40%"></a></p>
						</div>
					</div>
				</c:if>
			</div>
			
			<hr class="p-0 m-0 mb-1">			
			<div class="text-right text-white">
				<b>SIGA.doc</b> 8.0.6 <c:if test="${siga.versao != ''}">${siga.versao}</c:if> <span style="white-space:nowrap;"><i class="fa fa-code"></i> Desenvolvido por Prodesp e TRF2 </span>				
			</div>
		</div>
	</footer>

</c:if>

</body>
</html>