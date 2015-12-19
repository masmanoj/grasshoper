angular.module('dashboard.controllers').controller('TagsController', ['$scope',  '$rootScope', '$http', 'Restangular', 
	function(scope, $http,  $rootScope, Restangular){
		scope.tags = [] ;
		scope.subtags = [];
		scope.activeIndex = 0;
		Restangular.all("tag").getList()
		    .then(function(data) {
			  	scope.tags = data.plain();
			  	scope.showSubtag(0);
			});

		scope.showSubtag = function(index){
			scope.tags[scope.activeIndex].active =false;
			scope.tags[index].active = true;
			scope.activeIndex = index;

			Restangular.all("tag/" + scope.tags[index].id + "/subtag").getList()
		    .then(function(data) {
			  	scope.subtags = data.plain();
			  	console.log(scope.subtags);

			});
		}

		scope.saveTag = function(index){
			if(scope.tags[index].id){	
				Restangular.all("tag/" + scope.tags[index].id).customPUT(scope.tags[index])
				.then(function(data){
					scope.tags[index].edit = false;
				});
			}else{
				Restangular.all("tag/").post(scope.tags[index])
				.then(function(data){
					console.log(data);
					scope.tags[index].id = data.resourceId;
					scope.tags[index].edit = false;
				});
			}
		}

	    scope.saveSubTag = function (index){
	    	scope.subtags[index].locale = "en";
	    	if(scope.subtags[index].id){	
		    	Restangular.all("tag/" + scope.subtags[index].tagId + "/subtag/" + scope.subtags[index].id).customPUT(scope.subtags[index])
				.then(function(data){
					console.log(data);
					scope.subtags[index].edit = false;
				});
			}else{
				Restangular.all("tag/" + scope.tags[scope.activeIndex].id + "/subtag/" ).post(scope.subtags[index])
				.then(function(data){
					scope.subtags[index].id = data.resourceId;
					scope.subtags[index].edit = false;
				});
			}
	    }
	    scope.addNew = function(array){
	    	var obj= {};
	    	obj.edit = true;
	    	array.push(obj);
	    }
	    scope.cancelOrRemove = function(array, index){
	    	if(array[index].id){
	    		array[index].edit = false;
	    	}else{
	    		array.splice(index,1);
	    	}
	    }
	    scope.deleteSubTag = function(index){
	    	Restangular.all("tag/" + scope.subtags[index].tagId + "/subtag/" + scope.subtags[index].id).remove()
	    	.then(function(data){
	    		scope.subtags.splice(index,1);
	    	});
	    }
	    scope.deleteTag = function(index){
	    	Restangular.all("tag/" + scope.tags[index].id).remove()
	    	.then(function(data){
	    		scope.tags.splice(index,1);
	    	});
	    }
	}
]);