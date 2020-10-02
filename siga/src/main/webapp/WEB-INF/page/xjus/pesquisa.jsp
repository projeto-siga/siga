<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<siga:pagina titulo="Busca Textual" popup="false">

	<script src="https://code.angularjs.org/1.6.10/angular.min.js"></script>
	<script src="https://code.angularjs.org/1.6.10/angular-route.min.js"></script>
	<script src="https://code.angularjs.org/1.6.10/angular-sanitize.min.js"></script>
	<script src="/siga/javascript/x-jus/pesquisa.js"></script>
	<script src="/siga/javascript/x-jus/dir-pagination/dirPagination.js"></script>


	<div class="wrapper" ng-app="app" ng-controller="ctrlSearch">
		<div class="container content mt-3">
			<div class="row justify-content-center">
				<!-- Sidebar -->
				<div class="col col-auto text-center mb-3">
					<form name="Pessoa" style="border: none;"
						class="sky-form ng-pristine ng-valid">
						<label for="code" style="padding-top: 0px;" title="" class="label">Pesquisar:
							&nbsp;</label><label for="filtro" style="margin-bottom: 0px;" title=""
							class="input"><input ng-model="filter"
							ng-model-options="{ debounce: 1000 }" name="filter" id="filter"
							type="text"
							class="form-control ng-pristine ng-valid ng-empty ng-touched"
							style=""></label><div class="ml-2" style="visibility:  {{loading ? 'visible' : 'hidden'}}; display: inline-block"><div class="loader"></div></div>
					</form>
				</div>
				<div class="col col-auto"></div>
			</div>
			<div class="row">
				<!-- Sidebar -->
				<div class="col-lg-3">
					<!-- Facets -->
					<div ng-repeat="facet in results.facets"
						style="padding-bottom: 1em;">
						<h5 class="text-uppercase g-color-gray-dark-v1 mb-0">{{facet.name}}</h5>
						<hr class="g-brd-gray-light-v4" style="margin: 5px 0">
						<ul class="list-unstyled mb-3 ml-0 p-0">
							<li class="m-0 p-0" style="list-style-type: none"
								ng-repeat="value in facet.values"
								ng-if="facetNames.indexOf(facet.name) == -1 || facets.indexOf(value.refinementToken) !== -1"><a
								class="">{{value.name}} <span
									ng-click="addFacet(facet.name, value.refinementToken)"
									class="badge badge-secondary float-right">{{value.count}}</span>


									<span ng-click="removeFacet(facet.name, value.refinementToken)"
									class="badge badge-secondary float-right"
									style="margin-right: 0.5em;"
									ng-if="facets.indexOf(value.refinementToken) !== -1">X</span>
							</a></li>
						</ul>
					</div>
					<!-- End Facets -->
				</div>
				<!-- End Sidebar -->

				<!-- Search Results -->
				<div class="col-lg-9">
					<!-- Search Info -->
					<div class="mb-3">
						<h6 class="text-uppercase" ng-if="results.count>0">
							<span class="g-color-gray-dark-v1">{{results.count}}</span>
							resultados
						</h6>
					</div>
					<!-- End Search Info -->

					<!-- Search Result -->
					<article
						dir-paginate="i in results.results | itemsPerPage: resultsPerPage"
						total-items="results.count" current-page="pagination.current">
						<h5 class="mb-0">
							<a href="{{i.url}}">{{i.code}} {{i.title}}</a>
						</h5>
						<p class="mb-4" ng-bind-html="i.content"></p>
					</article>
					<!-- End Search Result -->

					<dir-pagination-controls
						on-page-change="pageChanged(newPageNumber)"></dir-pagination-controls>
				</div>
				<!-- End Search Results -->
			</div>
		</div>
	</div>


</siga:pagina>