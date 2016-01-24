
angular.module('dashboard.controllers').controller('HomeController', ['$scope',  '$rootScope', '$http', 'Restangular' ,'$location',
	function(scope, $http,  $rootScope, Restangular, location){

		scope.newOrdersList = [];
		scope.LIMIT = 10;
		scope.offset = 0;
		scope.totalItems = scope.LIMIT;
		scope.currentPage = 1;
		scope.itemsPerPage = scope.LIMIT;

		
		

		scope.pageChanged = function() {
			//console.log(scope.currentPage);
			scope.offset = (scope.currentPage - 1)*scope.LIMIT;
   			Restangular.all("order/").getList({limit:scope.LIMIT+1 ,offset: scope.offset, status: 100})
		    .then(function(data) {
			  	scope.newOrdersList = data.plain();
			  	if(scope.newOrdersList.length > scope.LIMIT){
			  		scope.totalItems +=1;
			  		scope.newOrdersList.splice(scope.newOrdersList.length - 1, 1);
			  	}
			});
  		};
  		if($rootScope.isAutherised )
  			scope.pageChanged();
  		
  		scope.viewOrder =  function(order){
			location.path( "/order/editorder/"+order.id );
		};
	}
]);