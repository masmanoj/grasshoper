angular.module('dashboard.controllers').controller('CreateProductController', ['$scope',  '$rootScope', '$http', 'Restangular', '$routeParams','$location',
	function(scope, $http,  $rootScope, Restangular, routeParams, location){
		scope.product = {};
		scope.product.quantity =0;
		scope.product.minimumQuantity = 0;
		scope.product.pricePerUnit = 0;
		scope.isedit = true;
		scope.isUpdate = false;
		scope.isQuickUpdate = false;
		scope.selectedTags = [];
		scope.allPackingStyleTags =[];

		Restangular.one("product/template").get().then(function(templateData){
			scope.templateData =  templateData.plain();
			scope.allPackingStyleTags = scope.templateData.allPkgingStyles;
		});
		if(routeParams.productId){
			var data  = Restangular.one("product/" +routeParams.productId).get()
			.then(function(data){
				scope.product =  data.plain();
				scope.isedit = false;
				scope.selectedTags = scope.product.packagingStyles;
                var tempTags = [];
                scope.product.packagingStyles.forEach(function(tag){
                    tempTags.push(tag.id+"");
                });
                scope.product.packingStyleIds = tempTags;
                scope.imageUrl = scope.product.productImages[0].imageUrl
			});
			
		}


		scope.packingStyleLoader = function(){
            scope.selectedTags = [];
            for(var i =0; i< scope.product.packingStyleIds.length; i++ ){
                for(var j= 0; j< scope.allPackingStyleTags.length;j++){
                    if(scope.product.packingStyleIds[i] == scope.allPackingStyleTags[j].id){
                        scope.selectedTags.push(scope.allPackingStyleTags[j]);
                        break;
                    }                    
                }
            }

            /*if(scope.processMap.roleIds.length > 0 ){
                scope.noRoleSelected = false; 
            }*/
        };
		scope.removeThisFromTagList = function(index){
            var idToRemove = scope.selectedTags[index].id ;
            var indexToremove = null;
            for(var i = 0; i< scope.product.packingStyleIds.length; i++ ){
                if(scope.product.packingStyleIds[i] == idToRemove){
                    indexToremove = i;
                    break;
                }                    
            }
            if(indexToremove!=null){
                scope.product.packingStyleIds.splice(indexToremove,1)
                scope.selectedTags.splice(index,1);
            }
           /* if(scope.product.packingStyleIds.length == 0 ){
                scope.noTagSelected = true; 
            } */
        }

		scope.submit = function(){

			scope.product.locale = $rootScope.locale;
			if(scope.isedit){
				Restangular.all("product/").post(scope.product)
					.then(function(data){
						location.path( "/manage/products");
					});
			}else{
				Restangular.all("product/" + scope.product.id).customPUT(scope.product)
				.then(function(data){
					location.path( "/manage/products");
				});
			}
		};

		scope.cancel = function(){
			location.path( "/manage/products");
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
	    scope.showImage = function(img){
	    	scope.imageUrl = img.imageUrl;
	    }

	    scope.deleteImage = function(index){
	    	Restangular.all("product/" + scope.product.id + "/image/" + scope.product.productImages[index].id).remove()
	    	.then(function(data){
	    		scope.product.productImages.splice(index,1);
	    	});
	    }

	    scope.saveImage = function (index){
	    	scope.product.productImages[index].locale = "en";
	    	if(scope.product.productImages[index].id){	
		    	Restangular.all("product/" + scope.product.id + "/image/" + scope.product.productImages[index].id).customPUT(scope.product.productImages[index])
				.then(function(data){
					console.log(data);
					scope.product.productImages[index].edit = false;
				});
			}else{
				Restangular.all("product/" + scope.product.id + "/image/" ).post(scope.product.productImages[index])
				.then(function(data){
					scope.product.productImages[index].id = data.resourceId;
					scope.product.productImages[index].edit = false;
				});
			}
	    }


	}
]);