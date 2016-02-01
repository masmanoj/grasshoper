angular.module('dashboard.controllers').controller('UserListController', ['$scope',  '$rootScope', '$http', 'Restangular','$location', 
	function(scope, $http,  $rootScope, Restangular, location){
		scope.users = [];
		scope.LIMIT = 30;
		scope.offset = 0;
		scope.totalItems = scope.LIMIT;
		scope.currentPage = 1;
		scope.itemsPerPage = scope.LIMIT;

		scope.pageChanged = function() {
			console.log(scope.currentPage);
			scope.offset = (scope.currentPage - 1)*scope.LIMIT;
   			Restangular.all("user/").getList({limit:scope.LIMIT+1 ,offset: scope.offset})
		    .then(function(data) {
			  	scope.users = data.plain();
			  	//console.log(scope.users);
			  	if(scope.users.length > scope.LIMIT){
			  		scope.totalItems +=1;
			  		scope.users.splice(scope.users.length - 1, 1);
			  	}
			});
  		};
  		scope.pageChanged();
		scope.viewUser =  function(user){
			location.path( "/admin/users/editser/"+user.id );
		}
	}
]);