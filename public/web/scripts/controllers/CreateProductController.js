angular.module('dashboard.controllers').controller('CreateProductController', ['$scope',  '$rootScope', '$http', 'Restangular', 
	function(scope, $http,  $rootScope, Restangular){
		scope.product = {};
		scope.isedit = true;

		scope.submit = function(){
			Restangular.all("product/").post(scope.product)
				.then(function(data){
					console.log(data);
					scope.isedit = false;
				});
		}
	}
]);