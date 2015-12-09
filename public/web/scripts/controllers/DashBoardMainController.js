angular.module('dashboard.controllers').controller('DashBoardMainController', ['$scope',  '$rootScope', '$http', 'Restangular', 'SessionMgr', 
	function(scope, $http,  $rootScope, Restangular, SessionMgr){
		scope.domReady = true;

		scope.authenticate = function(){
			console.log(scope.authCredentials);
			Restangular.all("authentication").post(scope.authCredentials)
			    .then(function(data) {
			  		if(data.authenticated){
			  			if (SessionMgr.get(data)) {
                        	scope.currentSession = SessionMgr.get(data);
                        }
			  		}
				  	console.log(data);
				  	console.log(scope.currentSession);
				});
		}

	}
]);