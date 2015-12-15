angular.module('dashboard.services', []).service('SessionMgr',[ '$rootScope', 'Restangular', function($rootScope, Restangular){
	var EMPTY_SESSION = {};
	this.get = function (data) {
        if (data.shouldRenewPassword) {
            Restangular.setDefaultHeaders({'Authorization': 'Custom '+data.sessionKey});
        } else{
        	$rootScope.sessionData = {
                userId: data.userId,
                authenticationKey: data.sessionKey,
                userPermissions: data.permissions,
                name: data.name
            };
            var user = {
            	userId: data.userId,
                userPermissions: data.permissions,
                name: data.name
            }
            if(data.sessionKey)
            	Restangular.setDefaultHeaders({'Authorization': 'Custom '+data.sessionKey});
            else
            	Restangular.setDefaultHeaders({'Authorization': ''});
            return {user: user};
        };
    }
    this.clear = function () {
    	$rootScope.sessionData = null;
        Restangular.setDefaultHeaders({'Authorization': ''});
        return EMPTY_SESSION;
    };

}])