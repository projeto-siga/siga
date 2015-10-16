<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<siga:pagina titulo="Busca Textual" popup="false">

	<script type="text/javascript" language="Javascript1.1">
		
	</script>

	<div class="container">
		<header id="topo" class="row">
			<div class="col-md-7">
				<form name="headerSearch" class="form-inline pull-right topoForm">
					<div class="form-group">
						<input type="text" class="form-control" name="q"
							placeholder="Pesquisa Siga">
					</div>
					<button type="submit" class="btn btn-default">Pesquisar</button>
				</form>
			</div>

		</header>
		<div class="row">
			<div role="main" class="col-md-9 col-md-push-3">
				<div id="main"></div>
			</div>
			<nav class="col-md-3 col-md-pull-9">
				<h3>Filtros</h3>

				<div id="gsa-dynamic-navigation"></div>


			</nav>
		</div>
		<footer class="row">
			<div class="col-md-12 text-center">
				<h3>Desenvolvimento Busca Siga</h3>
				<div>
		</footer>

	</div>

	<script data-require="bootstrap@3.3.5" data-semver="3.3.5"
		src="/siga/bootstrap/js/bootstrap.min.js"></script>
	<script src="/siga/javascript/busca_textual_gsa.js"></script>
	</body>

</siga:pagina>