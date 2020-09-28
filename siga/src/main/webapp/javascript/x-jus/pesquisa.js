var app = angular.module('app', [ 'ngSanitize',
		'angularUtils.directives.dirPagination' ]);

app.controller('ctrlSearch', function($scope, $http, $templateCache, $interval,
		$window, $location) {
	$scope.promise = null;
	$scope.facetNames = [];
	$scope.facets = [];
	$scope.resultsPerPage = 5;

	$scope.addFacet = function(name, f) {
		var index = $scope.facets.indexOf(f);
		if (index == -1) {
			$scope.facetNames.push(name);
			$scope.facets.push(f);
			$scope.getResultsPage(1);
		}
		$location.search("facets", $scope.encodeFacets());
	};

	$scope.removeFacet = function(name, f) {
		var index = $scope.facets.indexOf(f);
		if (index !== -1) {
			$scope.facets.splice(index, 1);
			$scope.facetNames.splice(index, 1);
		}
		$location.search("facets", $scope.encodeFacets());
		$scope.getResultsPage(1);
	};

	$scope.pageChanged = function(newPage) {
		$scope.getResultsPage(newPage);
	};

	$scope.encodeFacets = function() {
		var f
		if ($scope.facets.length > 0) {
			for (var i = 0; i < $scope.facets.length; i++) {
				if (f)
					f += ",";
				else
					f = ""
				f += $scope.facetNames[i] + "~" + $scope.facets[i];
			}
		}
		return f;
	};

	$scope.encodeFacetsRequest = function() {
		var f
		if ($scope.facets.length > 0) {
			for (var i = 0; i < $scope.facets.length; i++) {
				if (f)
					f += ",";
				else
					f = ""
				f += $scope.facets[i];
			}
		}
		return f;
	};

	$scope.decodeFacets = function(s) {
		$scope.facetNames = [];
		$scope.facets = [];
		var a = s.split(',');
		for (var i = 0; i < a.length; i++) {
			var f = a[i].split('~', 2);
			$scope.facetNames.push(f[0]);
			$scope.facets.push(f[1]);
		}
	};

	$scope.getResultsPage = function(pageNumber) {
		$scope.pagination.current = pageNumber;

		f = $scope.encodeFacetsRequest();

		$scope.loading = true;
		$scope.promise = $http(
				{
					url : '/siga/app/xjus/query?filter=' + $scope.filter
							+ '&page=' + $scope.pagination.current
							+ '&perpage=' + $scope.resultsPerPage
							+ (f ? '&facets=' + f : ''),
					method : "GET"
				}).then(function(response) {
			$scope.loading = false;
			$scope.results = response.data;
		}, function(response) {
			$scope.loading = false;
			$scope.apresentarProblema = true;
			$scope.msgProblema = response.data.error;
		});
	}

	$scope.$watch('filter', function() {
		$location.search("filter", $scope.filter);
		$scope.getResultsPage(1);
	});

	$scope.$watch('pagination', function() {
		$location.search("page", $scope.pagination.current == 1 ? undefined
				: $scope.pagination.current);
	}, true);

	$scope.show = function(key) {
		// $scope.promise = $http({
		// url : '/api/v1/locator?key=' + key,
		// method : "GET"
		// }).then(function(response) {
		// $location.path('/' + response.data.locator + '/show/' + key);
		// }, function(response) {
		// $scope.apresentarProblema = true;
		// $scope.msgProblema = response.data.error;
		// });
	};

	$scope.filter = $location.search().filter;
	$scope.pagination = {
		current : $location.search().page ? $location.search().page : 1
	};
	if ($location.search().facets) {
		$scope.decodeFacets($location.search().facets);
	}

	// angular.element('input#filter').focus();
	if ($scope.filter)
		$scope.getResultsPage(1);
});
