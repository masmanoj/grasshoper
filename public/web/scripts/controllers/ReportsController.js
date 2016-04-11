angular.module('dashboard.controllers').controller('ReportsController', ['$scope',  '$rootScope', '$http', 'Restangular' ,'$location', '$routeParams',
	function(scope, $http,  $rootScope, Restangular, location, routeParams){

		scope.reportData = [];
		scope.LIMIT = 50;
		scope.offset = 0;
		scope.totalItems = scope.LIMIT;
		scope.currentPage = 1;
		scope.itemsPerPage = scope.LIMIT;
		scope.reportName = routeParams.reportname;
		
		

		scope.loadReport = function() {
			//console.log(scope.currentPage);
			scope.offset = (scope.currentPage - 1)*scope.LIMIT;
   			Restangular.one("report/"+ scope.reportName).get({limit:scope.LIMIT+1 ,offset: scope.offset})
		    .then(function(data) {
			  	scope.reportData = data.plain();
			  	console.log(scope.reportData)
			  	if(scope.reportData.dataList.length > scope.LIMIT){
			  		scope.totalItems +=1;
			  		scope.reportData.dataList.splice(scope.reportData.dataList.length - 1, 1);
			  	}
			});
  		};
  		scope.loadReport();
	}
]);