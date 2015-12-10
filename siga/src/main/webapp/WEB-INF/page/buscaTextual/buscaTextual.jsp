<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<siga:pagina titulo="Busca Textual" popup="false">

	<div class="container-fluid	">
		 <header id="topo" class="row">
            <div class="col-md-7 topo">
                <form name="headerSearch" class="form-inline pull-right topoForm">
                    <div class="form-group">
                        <input type="text" class="form-control typeahead" name="q" placeholder="Pesquisa Siga">
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
				<div id="gsa-dynamic-navigation"></div>
			</nav>
		</div>
	</div>

	<script src="/siga/bootstrap/js/bootstrap.min.js"></script>
	<script src="/siga/bootstrap/js/typeahead.bundle.min.js"></script>
		
	<script src="/siga/javascript/busca_textual_gsa.js"></script>
	</body>

</siga:pagina>