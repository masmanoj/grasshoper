angular.module('dashboard.controllers').controller('ProductsController', ['$scope',  '$rootScope', '$http', 'Restangular','$location', 
	function(scope, $http,  $rootScope, Restangular, location){
		scope.products = [];

		Restangular.all("product/").getList()
		    .then(function(data) {
			  	scope.products = data.plain();
			  	console.log(scope.products);

			});


		scope.viewProduct =  function(product){
			location.path( "/product/editproduct/"+product.id );
		}
	}
]);