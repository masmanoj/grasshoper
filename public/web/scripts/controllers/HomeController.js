
angular.module('dashboard.controllers').controller('HomeController', ['$scope',  '$rootScope', '$http', 'Restangular' ,'$location', 
	function(scope, $http,  $rootScope, Restangular, location){

		scope.newOrdersList = [];
		scope.LIMIT = 10;
		scope.offset = 0;
		scope.totalItems = scope.LIMIT;
		scope.currentPage = 1;
		scope.itemsPerPage = scope.LIMIT;

		scope.pageChanged = function() {
			//console.log(scope.currentPage);
			scope.offset = (scope.currentPage - 1)*scope.LIMIT;
   			Restangular.all("order/").getList({limit:scope.LIMIT+1 ,offset: scope.offset, status: 100})
		    .then(function(data) {
			  	scope.newOrdersList = data.plain();
			  	if(scope.newOrdersList.length > scope.LIMIT){
			  		scope.totalItems +=1;
			  		scope.newOrdersList.splice(scope.newOrdersList.length - 1, 1);
			  	}
			});
  		};
  		if($rootScope.isAutherised )
  			scope.pageChanged();
  		
  		scope.viewOrder =  function(order){
			location.path( "/order/editorder/"+order.id );
		};

		scope.options = {
            chart: {
                type: 'discreteBarChart',
                height: 250,
                margin : {
                    top: 20,
                    right: 20,
                    bottom: 60,
                    left: 50
                },
                
                x: function(d){return d.name;},
                y: function(d){return d.qty ;},
                showLabels : false,
                showValues: false,
                rotateValues: 30,
                valueFormat: function(d){
                    return d3.format(',.2f')(d);
                },
                duration: 500,
                xAxis: {
                    axisLabel: 'Products',
                    rotateLabels: 20
                },
                yAxis: {
                    axisLabel: 'Quantity',
                    axisLabelDistance: -10
                }
            }
        };

        scope.data = [
            {
                values: [
                ]
            }
        ]

        Restangular.all("product/").getList()
		    .then(function(data) {
			  	scope.products = data.plain();
			  	for(var i=0; i < scope.products.length ; i++){
			  		var productObj = {};
			  		productObj.name = scope.products[i].name;
			  		productObj.qty = scope.products[i].quantity;
			  		scope.data[0].values.push(productObj);
			  	}

			});
        
	}
]);