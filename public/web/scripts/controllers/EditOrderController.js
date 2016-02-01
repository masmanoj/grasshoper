angular.module('dashboard.controllers').controller('EditOrderController', ['$scope',  '$rootScope', '$http', 'Restangular', '$routeParams','$location', '$route', 'NotificationService', 
	function(scope, $http,  $rootScope, Restangular, routeParams, location, route, NotificationService){
		scope.order = {};
		scope.orderStatusOptions = [];
		scope.formData = {};
		scope.formData.emailchk = true;
		scope.orderId = routeParams.orderId;
		var data  = Restangular.one("order/" + scope.orderId).get()
			.then(function(data){
				scope.order =  data.plain();
				scope.orderStatusOptions = scope.order.allStatus;
			}
		);
		scope.submit = function(){
			scope.formData.locale = $rootScope.locale;
			Restangular.all("order/" + scope.orderId).customPUT(scope.formData)
				.then(function(data){
					NotificationService.showSuccess();
					route.reload();
			});
		};

	}
]);