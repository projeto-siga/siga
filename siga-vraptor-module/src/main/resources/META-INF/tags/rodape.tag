
<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ attribute name="popup"%>
<%@ attribute name="pagina_de_erro"%>
<%@ attribute name="incluirJs"%>
<%@ attribute name="incluirBS" required="false"%>

<!--[if gte IE 5.5]><script language="JavaScript" src="/siga/javascript/jquery.ienav.js" type="text/javascript"></script><![endif]-->

<c:choose>
	<c:when test="${not empty sigaModalAlerta}">
		<siga:siga-modal id="sigaModalAlerta" centralizar="${sigaModalAlerta.centralizar}" abrirAoCarregarPagina="true" exibirRodape="true" 
			tituloADireita="${empty sigaModalAlerta.titulo ? 'Alerta' : sigaModalAlerta.titulo}" >
			<div class="modal-body">${sigaModalAlerta.mensagem}</div>
		</siga:siga-modal>	
	</c:when>
	<c:otherwise>
		<siga:siga-modal id="sigaModalAlerta" exibirRodape="true" tituloADireita="Alerta">
			<div class="modal-body">Mensagem de alerta</div>
		</siga:siga-modal>
	</c:otherwise>
</c:choose>
<siga:siga-spinner />

<script src="/siga/javascript/jquery/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

<script src="/siga/javascript/siga.js?v=1622040065" type="text/javascript" charset="utf-8"></script>

<script src="/siga/javascript/picketlink.js" type="text/javascript" charset="utf-8"></script>

<script	src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/siga/javascript/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css" type="text/css" media="screen, projection">
<script src="/siga/popper-1-14-3/popper.min.js"></script>

<c:if test="${empty incluirBS or incluirBS}" >
 	<script src="/siga/bootstrap/js/bootstrap.min.js?v=4.1.1" type="text/javascript"></script>
</c:if> 

<script src="/siga/javascript/datepicker-pt-BR.js" type="text/javascript"></script>

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
 		$('.campoData').datepicker({
           	onSelect: function(){
                   ${onSelect}
			}
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
					<div class="row pt-5">
						<div class="col-md-4 pb-4">
							<a href="https://linksiga.trf2.jus.br" role="link" target="_Blank" class="text-decoration-none">
								<img src="/siga/imagens/logo-siga-novo-38px.png" />
								<img id="logo-header2" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABsAAAAmCAYAAAA1MOAmAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAB3RJTUUH4QUPFCQBHI8nPQAAA2pJREFUWMO9l0uIllUYx3/P+DkalpO3RU1hoQ7jpUDSTS1Gogi6EAm1icxFi4aKFkHShSgolAgqyhYhtGhX1sKwoqhFg1BYQS1EjJpoSiq7OEmTM47+WvS8cnqZyzvTNz3wcs57zvme/3n/z/WDWYi6RO3l/xD1UnVopr/rmCXeGWDZnIKpZ6ezuWHHTIAiAnUrcF4FWFygbTaqxuv9R9aoJ9SuuQK6Ux0pwH5Wn27716l3+W9Zo36n/qVubgrY1GY3197HgdPAQmC/uh1YpXapi/PpUqP8Uash2Kna+xDwFXAJsAJ4BTgGjNTOfabeCpyJiMY0vlGjsaWuV0edXl5UO2dis/drChbm+lXqFw0AP1CXRQOgxcBBoKdai+SkiL2NwOa05WRyqDWVu6fOrcDqYnt/DWgNsBHozkDvzHG0br/pvmqbOlbQcUK9oLZ/vEbZM+omdUPxrFcXoS5K7rfk05cBfLBQcEodUvsKoC01kGPq5RPk0bMSajdwExC1rD5W2OAX4JOI+C0VzQM+Aq4sYzEi9lX0TiSt5Pt24OQkbP4KHADeK258IXBxcWYwIvYVdp7ULi11dwP3Hc2Mj7oyaa1kd6MSExHjEXEP8NA0ZzuBveraTFWljDUBaxVuvEvtAPprxbETWJJjAHuAG9tSSiZYP0e9Tj1U0HZRZv1KXmpbLcv5UvXbWokx43Ddf24LSs9Kt3++2J6XntoXEYfmoj3YnF/Tm7T2zGW/2JtgK9QFM20JWjPEq3gdjoixqbJFO5vUmDZbtAFsOJ+Ohh7cLL6mULaqqtQz7oizAPaq36iDxfhj1qsHM6a+Vr8EromIkxnY3+fZQfVAxuIj6h/pSG+pS+u3Xa7ep+5Ux9W38/0y9eUskNvU11PJberanO/Ns9vVc9Uj6gPqh7l/72Qcb1D/VB8t1vaogzm/Wj2t7lB7Ull/7eLzc7wl9+8/6/oNPEpgufoccANwGHgVOD/371A3AT8AT0VE1Xv0Zx8yUIVIU28czwZ0NfBxRBwtPHIk09Zw8c9mB3At8HBEfD6Zp01H4wtJzUp1Xc7vrumo6Hu8aCEmjLOO7N/n1+pZ5eqv5fhEsb+gAOoG3szieoX6LrBzsnR1FNgFvFOs7QU+zYwxoD4GHAd+Ap4FBoqzvwNPZsNUXf5Itfk35wXnXBwGnuIAAAAASUVORK5CYII="
									alt="Logo TRF2" height="38" class="ml-2" />
							</a>
						</div>
						<div class="col-md-4  pb-4">		
							<a href="http://www.prodesp.sp.gov.br/" role="link" target="_Blank"><img class="mx-auto d-block" src="/siga/imagens/logo-prodesp-branco-e-azul.png" style="width:50%"></a>
						</div>
						<div class="col-md-4  pb-4">
							<a href="http://www.saopaulo.sp.gov.br/" role="link" target="_Blank"><img class="mx-auto d-block" src="/siga/imagens/logo-gesp-slogan-horizontal-cor-texto-branco.png" alt="Governo do Estado de São Paulo" width="50%"></a></p>
						</div>			
					</div>
			</div>
			
			<hr class="p-0 m-0 mb-1">			
			<div class="text-right text-white">
				<b>SIGA.doc </b>${siga_version}
			</div>
		</div>
	</footer>

</c:if>

</body>
</html>