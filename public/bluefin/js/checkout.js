var address = "";
var loginJson = "";
var cartData = "";
var addressId = "";
(function() {



    $("#_login-btn_Checkout").validator().on("click", function(e) {

        var form = fetchInput($("#checkoutLogin"));

        if ("1" === 1) {
            // handle the invalid form...



        } else {



            var username = form.name; //"manoj.masmatrics@gmail.com";
            var password = form.password; //"asd";

            var params = {
                username: username,
                password: password
            };
            params = JSON.stringify(params);


            var ajaxCallLogin = new ajaxRequest();
            loginJson = ajaxCallLogin.ajax(params, apiUrl + "/authentication", "POST");
         
            loginJson.onreadystatechange = function(e) {

                if (loginJson.readyState == 4) {

                    loginJson = JSON.parse(loginJson.responseText);

                    $("#LoginSection").hide();
                    $("#addAddressSection").removeClass("hide");
                    fetchAddress(loginJson.sessionKey);



                }
            };
        }




    });

    $("#addAddressBtn").click(function() {

        var form = fetchInput($("#addAddressForm"));
        address = JSON.stringify(form);
        
        addAddress(loginJson.sessionKey);
    });

})();

function fetchAddress(key) {

    $.ajax({
        cache: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        headers: {
            'Authorization': "Custom " + key
        },
        crossDomain: true,
        type: 'GET',
        url: apiUrl + "/address",
        success: function(e) {

           
            displayAddress(e);

        },
        error: function(xhr, status, errorThrown) {


            console.log(errorThrown + '\n' + status + '\n' + xhr.statusText);

        }

    });


}

function deleteAddress(id) {


    $.ajax({
        cache: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        headers: {
            'Authorization': "Custom " + loginJson.sessionKey
        },
        crossDomain: true,
        type: 'DELETE',
        url: apiUrl + "/address/" + id,
        success: function(e) {


            fetchAddress(loginJson.sessionKey);

        },
        error: function(xhr, status, errorThrown) {


            console.log(errorThrown + '\n' + status + '\n' + xhr.statusText);

        }

    });

}

function displayAddress(e) {


    var data = "";
    e.forEach(function(val) {

        data += "<div class='col-md-3 alert'><strong>"

        + val.name + "</strong><br>" + val.addressLine1 + "<br>" + val.addressLine2 + "<br>" + val.addressLine3 + "<br>" + val.area + "<br>" + val.city + "<br>" + val.contactNumber + "<br>" + val.extraInfo + "<br>" + val.landmark + "<br>" + val.pin + "<br><br>" + "<a class='btn btn-default btn-sm btn-block' onclick='deliverToAddress(" + val.id + ")'><i class='fa fa-envelope-o'></i>  Deliver to this Address</a><br>" + "<a class='btn btn-warning btn-sm btn-block' onclick='deleteAddress(" + val.id + ")'><i class='fa fa-trash'></i>  Delete</a>" + "</div>";


    });
    $("#showAddress").html(data);


}

function deliverToAddress(id) {
    addressId = id;
    $("#addAddressSection").hide();
    $("#confirmCartSection").removeClass("hide");


    //finalCart

    var cartData = loadLocalStorage();
    console.log(cartData);
    var tableRow = "";
    cartData.forEach(function(val) {

        var items = val.split("|");

        items.splice(0, 1);

        var tableColumns = "";
        for (var i = 0; i < items.length; i++) {
            tableColumns += "<td>" + items[i] + "</td>";
        }

        tableRow += "<tr>" + tableColumns + "</tr>"

    });

    $("#finalCart").append(tableRow);



}

function placeOrder() {


    var dropAddressId;
    var additionalNote="jk";
    var orderCartList;
    var quantity;
    var locale="en";
    var pkgStyleId;
	var cartData = loadLocalStorage();  
	
    var objRow =[];
	var orderList=["productUid","quantity","locale","pkgStyleId"];
	
	
	 var orderData = {
        dropAddressId:addressId,
        additionalNote: additionalNote,
        orderCartList: []

    };

   
    /*cartData.forEach(function(val) {

        var items = val.split("|");

        console.log(items);

        var objColumns = "";
	
        for (var i = 0; i < items.length; i++)
		{
            objColumns += orderList[i] +':'+ items[i]+',';
        }

        objRow.push('{'+objColumns+'}');
		

  });*/
   cartData.forEach(function(val) {

      var items = val.split("|");

      console.log(items);//  

      var objColumns = "";

       var cartObj = {
         productUid : items[0],
         quantity : items[2],
         pkgStyleId:1,
         locale:"en"
       }

      //objRow.push(cartObj);

      orderData.orderCartList.push(cartObj);


});
orderData=JSON.stringify(orderData);
console.log(orderData);

    $.ajax({
        cache: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: orderData,
        headers: {
            'Authorization': "Custom " + loginJson.sessionKey
        },
        crossDomain: true,
        type: 'POST',
        url: apiUrl + "/item-order",
        success: function(e) {
confirm("Order placed success fully");
        },
        error: function(xhr, status, errorThrown) {


            console.log(errorThrown + '\n' + status + '\n' + xhr.statusText);

        }

    });



}



function addAddress(key) {

    $.ajax({
        cache: false,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: address,
        headers: {
            'Authorization': "Custom " + key
        },
        crossDomain: true,
        type: 'POST',
        url: apiUrl + "/address",
        success: function(e) {


            fetchAddress(loginJson.sessionKey);
            $("#addAddressSection").scrollTop(0);

        },
        error: function(xhr, status, errorThrown) {


            console.log(errorThrown + '\n' + status + '\n' + xhr.statusText);

        }

    });
}