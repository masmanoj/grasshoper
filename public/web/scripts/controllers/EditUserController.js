angular.module('dashboard.controllers').controller('EditUserController', ['$scope',  '$rootScope', '$http', 'Restangular', '$routeParams','$location', '$route',
	function(scope, $http,  $rootScope, Restangular, routeParams, location, route){
		scope.user = {};
		scope.updatePasswdFormData ={};
		scope.updatePasswd = {};
		scope.userId = routeParams.userId;
		var data  = Restangular.one("user/" + scope.userId).get()
			.then(function(data){
				scope.user =  data.plain();
			}
		);
		scope.submitPassword = function(){
			Restangular.all("user/" + scope.userId+"/passwd").customPUT(scope.updatePasswdFormData)
				.then(function(data){
					route.reload();
			});
		};

	}
]);