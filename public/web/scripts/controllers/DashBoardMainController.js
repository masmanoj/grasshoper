angular.module('dashboard.controllers').controller('DashBoardMainController', ['$scope',  '$rootScope', '$http', 'Restangular', 'SessionMgr', '$translate','$location',  '$interval',
	function(scope, $http,  $rootScope, Restangular, SessionMgr, $translate, location,  $interval){
		/*global init*/
		$rootScope.locale= "en"
		scope.newOrders = 0;

		scope.domReady = true;
		scope.isTestMode = false;

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
                        	location.path( "/main" );
                        }
			  		}
				  	//console.log(data);
				  	//console.log(scope.currentSession);
				  	Restangular.one("order/ordernoti").get().then(function(data){
						scope.newOrders = data;
					});
				  	scope.fetchOrderNoti();
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
		var noti ;
		scope.fetchOrderNoti = function(){
			noti = $interval(function() {
				if(scope.currentSession && scope.currentSession.user != null){
					
					Restangular.one("order/ordernoti").get().then(function(data){
						scope.newOrders = data;
					});
				}else{
					$interval.cancel(noti);
					noti = undefined;
				}
          	}, 60000);
			
		}

	}
]);