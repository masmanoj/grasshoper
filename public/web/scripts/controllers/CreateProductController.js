angular.module('dashboard.controllers').controller('CreateProductController', ['$scope',  '$rootScope', '$http', 'Restangular', '$routeParams','$location',
	function(scope, $http,  $rootScope, Restangular, routeParams, location){
		scope.product = {};
		scope.product.quantity =0;
		scope.product.minimumQuantity = 0;
		scope.product.pricePerUnit = 0;
		scope.isedit = true;
		scope.isUpdate = false;
		scope.isQuickUpdate = false;

		if(routeParams.productId){
			var data  = Restangular.one("product/" +routeParams.productId).get()
			.then(function(data){
				scope.product =  data.plain();
				console.log(scope.product);
				scope.isedit = false;
			});
			
		}


		scope.submit = function(){

			scope.product.locale = $rootScope.locale;
			if(scope.isedit){
				Restangular.all("product/").post(scope.product)
					.then(function(data){
						location.path( "/manage/products");
					});
			}else{
				Restangular.all("product/" + scope.product.id).customPUT(scope.product)
				.then(function(data){
					location.path( "/manage/products");
				});
			}
		};

		scope.cancel = function(){
			location.path( "/manage/products");
		}

	}
]);