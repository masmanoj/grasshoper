angular.module('dashboard.controllers').controller('DashBoardMainController', ['$scope',  '$rootScope', '$http', 'Restangular', 'SessionMgr', '$translate',
	function(scope, $http,  $rootScope, Restangular, SessionMgr, $translate){
		scope.domReady = true;

		scope.authCredentials = {
			username : 'masmatrics',
			password : 'asd123'
		}

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
		};
		scope.logout = function(){
			scope.currentSession = null;
			Restangular.all("authentication/logout").post()
			    .then(function(data) {
			  		//scope.currentSession = null;
					SessionMgr.get(data);
				});
			
		}

	}
]);