angular.module('dashboard.controllers').controller('CreateProductController', ['$scope',  '$rootScope', '$http', 'Restangular', '$routeParams','$location', 'NotificationService', '$route',
	function(scope, $http,  $rootScope, Restangular, routeParams, location ,NotificationService, route ){
		scope.product = {};
		scope.product.quantity =0;
		scope.product.minimumQuantity = 0;
		scope.product.pricePerUnit = 0;
		scope.isedit = true;
		scope.isUpdate = false;
		scope.isQuickUpdate = false;
		scope.selectedTags = [];
		scope.selectedCategoryTags = [];
		scope.allPackingStyleTags =[];
		scope.allCategoryTags = [];
		scope.addQty = {locale : $rootScope.locale};
		scope.addQty.quantity = 0;
		scope.showAddQuantity = false;

		Restangular.one("product/template").get().then(function(templateData){
			scope.templateData =  templateData.plain();
			scope.allPackingStyleTags = scope.templateData.allPkgingStyles;
			scope.allCategoryTags = scope.templateData.allCategories;
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
                if(scope.product.productImages.length > 0)
                	scope.imageUrl = scope.product.productImages[0].imageUrl;

                scope.selectedCategoryTags = scope.product.categories;
                console.log("scope.selectedCategoryTags ", scope.selectedCategoryTags );
                var tempCatTags = [];
                scope.product.categories.forEach(function(tag){
                    tempCatTags.push(tag.id+"");
                });
                scope.product.categoryIds = tempCatTags;
			});
		}

		scope.categoriesLoader = function(){
            scope.selectedCategoryTags = [];
            for(var i =0; i< scope.product.categoryIds.length; i++ ){
                for(var j= 0; j< scope.allCategoryTags.length;j++){
                    if(scope.product.categoryIds[i] == scope.allCategoryTags[j].id){
                        scope.selectedCategoryTags.push(scope.allCategoryTags[j]);
                        break;
                    }                    
                }
            }
        };
		scope.removeThisFromCategoryTagList = function(index){
            var idToRemove = scope.selectedCategoryTags[index].id ;
            var indexToremove = null;
            for(var i = 0; i< scope.product.categoryIds.length; i++ ){
                if(scope.product.categoryIds[i] == idToRemove){
                    indexToremove = i;
                    break;
                }                    
            }
            if(indexToremove!=null){
                scope.product.categoryIds.splice(indexToremove,1)
                scope.selectedCategoryTags.splice(index,1);
            }
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
        }

		scope.submit = function(){

			scope.product.locale = $rootScope.locale;
			if(scope.isedit){
				Restangular.all("product/").post(scope.product)
					.then(function(data){
						NotificationService.showSuccess();
						location.path( "/manage/products");
					});
			}else{
				Restangular.all("product/" + scope.product.id).customPUT(scope.product)
				.then(function(data){
					NotificationService.showSuccess();
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
	    		NotificationService.showSuccess();
	    		scope.product.productImages.splice(index,1);
	    	});
	    }

	    scope.saveImage = function (index){
	    	scope.product.productImages[index].locale = "en";
	    	if(scope.product.productImages[index].id){	
		    	Restangular.all("product/" + scope.product.id + "/image/" + scope.product.productImages[index].id).customPUT(scope.product.productImages[index])
				.then(function(data){
					console.log(data);
					NotificationService.showSuccess();
					scope.product.productImages[index].edit = false;
				});
			}else{
				Restangular.all("product/" + scope.product.id + "/image/" ).post(scope.product.productImages[index])
				.then(function(data){
					console.log(data);
					NotificationService.showSuccess();
					scope.product.productImages[index].id = data.resourceId;
					scope.product.productImages[index].edit = false;
				});
			}
	    }
	    scope.resetQuantity = function(){
	    	Restangular.all("product/" + scope.product.id+"/qty/reset").customPUT()
				.then(function(data){
					NotificationService.showSuccess();
					route.reload();
			});
	    }

	    scope.saveQty = function(){
	    	Restangular.all("product/" + scope.product.id+"/qty/add").customPUT(scope.addQty)
				.then(function(data){
					NotificationService.showSuccess();
					route.reload();
			});
	    }

	}
]);