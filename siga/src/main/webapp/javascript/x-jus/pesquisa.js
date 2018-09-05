var app = angular.module('app', [ 'ngSanitize', 'angularUtils.directives.dirPagination' ]);

app.controller('ctrlSearch', function($scope, $http, $templateCache, $interval, $window, $location) {
	$scope.promise = null;
	$scope.facetNames = [];
	$scope.facets = [];
	$scope.resultsPerPage = 5;

	$scope.pagination = {
		current : 1
	};

	$scope.addFacet = function(name, f) {
		var index = $scope.facets.indexOf(f);
		if (index == -1) {
			$scope.facetNames.push(name);
			$scope.facets.push(f);
			$scope.getResultsPage(1);
		}
	};

	$scope.removeFacet = function(name, f) {
		var index = $scope.facets.indexOf(f);
		if (index !== -1) {
			$scope.facets.splice(index, 1);
			$scope.facetNames.splice(index, 1);
		}
		$scope.getResultsPage(1);
	};

	$scope.pageChanged = function(newPage) {
		$scope.getResultsPage(newPage);
	};

	$scope.getResultsPage = function(pageNumber) {
		$scope.pagination.current = pageNumber;

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

		$scope.promise = $http({
			url : '/siga/app/xjus/query?filter=' + $scope.filter + '&page=' + $scope.pagination.current + '&perpage=' + $scope.resultsPerPage + (f ? '&facets=' + f : ''),
			method : "GET"
		}).then(function(response) {
			$scope.results = response.data;
		}, function(response) {
			$scope.apresentarProblema = true;
			$scope.msgProblema = response.data.error;
		});
	}

	$scope.$watch('filter', function() {
		$scope.getResultsPage(1);
	});

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

	// angular.element('input#filter').focus();
	if ($scope.filter)
		$scope.getResultsPage(1);
});
