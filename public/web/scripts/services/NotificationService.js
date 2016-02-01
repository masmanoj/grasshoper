angular.module('dashboard.services').service('NotificationService',[ '$rootScope', '$timeout', function($rootScope, $timeout){
	
    this.showSuccess = function (msg) {
        if(msg)
            $rootScope.notiMsg = msg;
        else
            $rootScope.notiMsg = "Action completed successfully!";
    	$rootScope.showNoti = true;
       //    $timeout(this.hideNoti(), 4000);

       $timeout(function() {
            $rootScope.showNoti = false;

        }, 5000);
    };
}])