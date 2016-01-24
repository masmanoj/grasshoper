angular.module('dashboard.controllers').controller('OrdersController', ['$scope',  '$rootScope', '$http', 'Restangular' ,'$location',
	function(scope, $http,  $rootScope, Restangular, location){

		scope.ordersList = [];
		scope.LIMIT = 30;
		scope.offset = 0;
		scope.totalItems = scope.LIMIT;
		scope.currentPage = 1;
		scope.itemsPerPage = scope.LIMIT;

		
		

		scope.pageChanged = function() {
			//console.log(scope.currentPage);
			scope.offset = (scope.currentPage - 1)*scope.LIMIT;
   			Restangular.all("order/").getList({limit:scope.LIMIT+1 ,offset: scope.offset})
		    .then(function(data) {
			  	scope.ordersList = data.plain();
			  	if(scope.ordersList.length > scope.LIMIT){
			  		scope.totalItems +=1;
			  		scope.ordersList.splice(scope.ordersList.length - 1, 1);
			  	}
			});
  		};
  		scope.pageChanged();

  		scope.viewOrder =  function(order){
			location.path( "/order/editorder/"+order.id );
		};
	}
]);