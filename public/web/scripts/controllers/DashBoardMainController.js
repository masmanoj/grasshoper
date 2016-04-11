angular.module('dashboard.controllers').controller('DashBoardMainController', ['$scope',  '$rootScope', '$http', 'Restangular', 'SessionMgr', '$translate','$location',  '$interval',
	function(scope, $http,  $rootScope, Restangular, SessionMgr, $translate, location,  $interval){
		/*global init*/
		$rootScope.locale= "en"
		scope.newOrders = 0;
		$rootScope.silentAjaxs = [];
		scope.userRingerOn = true;
		scope.domReady = true;
		scope.isTestMode = false;
		$rootScope.isAutherised = false;
    	scope.sound = document.getElementById("audio");	

		scope.authCredentials = {
			username : 'masmatrics',
			password : 'asd123'
		}
		scope.toggleUserRinger = function(){
			if(scope.userRingerOn)
				scope.userRingerOn = false;
			else
				scope.userRingerOn = true;
		}
		scope.authenticate = function(){
			//console.log(scope.authCredentials);
			Restangular.all("authentication").post(scope.authCredentials)
			    .then(function(data) {
			  		if(data.authenticated){
			  			if (SessionMgr.get(data)) {
                        	scope.currentSession = SessionMgr.get(data);
                        	$rootScope.isAutherised = true;
                        	scope.authCredentials = {};
                        	$rootScope.silentAjaxs.push("order/ordernoti");
                        	scope.updateNewOrderCount();
				  			scope.fetchOrderNoti();
				  			location.path( "/" );
                        	//location.path( "/main" )
                        }
			  		}
				  	//console.log(data);
				  	//console.log(scope.currentSession);
				  	//Restangular.one("order/ordernoti").get().then(function(data){
					//	scope.newOrders = data;
					//});
				  	//scope.fetchOrderNoti();
				});
		};
		scope.logout = function(){
			scope.currentSession = null;
			$rootScope.isAutherised = false;
			Restangular.all("authentication/logout").post()
			    .then(function(data) {
			  		scope.currentSession = null;
					SessionMgr.get(data);
					location.path( "/" );
				});
			
		}
		scope.updateNewOrderCount = function(){
			Restangular.one("order/ordernoti").get().then(function(data){
				if(data == undefined) data = 0;
				scope.newOrders = data;
			});
		}
 
		var noti ;
		scope.fetchOrderNoti = function(){

			noti = $interval(function() {
				if(scope.currentSession && scope.currentSession.user != null){
					
					Restangular.one("order/ordernoti").get().then(function(data){
						if(data == undefined) data = 0;
						//console.log(scope.newOrders , data);
						if(scope.newOrders < data){
							//scope.snd.play();
							if(scope.userRingerOn)
          						scope.sound.play()
						}
						scope.newOrders = data;
					});
				}else{
					$interval.cancel(noti);
					noti = undefined;
				}
          	}, 120000);
			
		}
	}
]);