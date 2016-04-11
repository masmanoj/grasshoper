var address = "";
var loginJson="";
var cartData = "";
var addressId = "";
var fetchAddressJson = "";
var addressArray = [];
(function() {

    $("#loader").hide();

	$("#_login-btn_Checkout").click(function() {

        $('#checkoutLoginForm').validator('validate');
     
        var inputVal = "";
        $("#checkoutLoginForm").find("input").each(function(key, val) {

            inputVal = $(val).val();


        });


        if (inputVal != "") {
            callLogin(fetchInput($("#checkoutLoginForm")),"checkout");
        }
    });
	
   

    $("#addAddressBtn").click(function() {

        var form = fetchInput($("#addAddressForm"));
        address = JSON.stringify(form);

        addAddress(loginJson.sessionKey);
    });
	



})();




function fetchAddress(key) {


    var ajaxCallFetch = new ajaxRequest();

    fetchAddressJson = ajaxCallFetch.ajax("", apiUrl + "/address", "GET", "Custom " + key);

    fetchAddressJson.onreadystatechange = function(e) {

        if (fetchAddressJson.readyState == 4) {

            fetchAddressJson = JSON.parse(fetchAddressJson.responseText);


           // displayAddress(fetchAddressJson);
             $("#showAddress").html(displayAddress(fetchAddressJson));
            $("#loader").hide();


        }
    };
}


getAddress=function(key){

$("section").addClass("hide");
$("#viewAddressSection").removeClass("hide");

$("#viewAddress").html(displayAddress(fetchAddressJson));
$("#viewAddress").find(".btn-default").hide();

};

function deleteAddress(id) {

    var deleteAddress = new ajaxRequest();
    var deleteAddressJson = deleteAddress.ajax("", apiUrl + "/address/" + id, "DELETE", "Custom " + loginJson.sessionKey);

    deleteAddressJson.onreadystatechange = function(e) {

        if (deleteAddressJson.readyState == 4) {

            deleteAddressJson = JSON.parse(deleteAddressJson.responseText);


            fetchAddress(loginJson.sessionKey);



        }
    };




}
var arrayCount = 0;

function displayAddress(e) {


    var data = "";
    e.forEach(function(val) {

        addressArray.push(val);

        data += '<div class="col-md-3 alert"><strong>'

        + val.name + '</strong><br>' + val.addressLine1 + '<br>' + val.addressLine2 + '<br>' + val.addressLine3 + '<br>' + val.area + '<br>' + val.city + '<br>' + val.contactNumber + '<br>' + val.extraInfo + '<br>' + val.landmark + '<br>' + val.pin + '<br><br>' + '<a class="btn btn-default btn-sm btn-block" onclick="deliverToAddress(&#34;' + val.id + '&#34;,&#34;' + arrayCount + '&#34;)"><i class="fa fa-envelope-o"></i>  Deliver to this Address</a><br>' + '<a class="btn btn-warning btn-sm btn-block" onclick="deleteAddress(' + val.id + ')"><i class="fa fa-trash"></i>  Delete</a>' + '</div>';
        arrayCount++;

    });

    return data;
   


}

function deliverToAddress(id, index) {

    addressId = id;

    $("#addressConfirm").html("<strong>" + addressArray[index].name + "</strong><br>" + addressArray[index].addressLine1 + "<br>" + addressArray[index].addressLine2 + "<br>" + addressArray[index].addressLine3 + "<br>" + addressArray[index].area + "<br>" + addressArray[index].city + "<br>" + addressArray[index].contactNumber + "<br>" + addressArray[index].extraInfo + "<br>" + addressArray[index].landmark + "<br>" + addressArray[index].pin);

    $("#addAddressSection").hide();
    $("#confirmCartSection").removeClass("hide");


    //finalCart

    var cartData = loadLocalStorage();

    var tableRow = "";
    cartData.forEach(function(val) {

        var items = val.split("|");
        var tableColumns = "";

        /*for (var i = 1; i < items.length; i++) {
		
			if(i==2){
			tableColumns += "<td ><div class='input-group spinner'></label><input type='number' onchange='changeQuantity(&#34;"+items[0]+"&#34;)' class=' form-control disabled' value='"+items[i]+"'/>"  
			
			+"</div></td>";
			}else{
            tableColumns += "<td>" + items[i] + "</td>";
			}
        }*/

        tableColumns = "<td><strong>" + items[1] + "</strong></td>" + "<td ><div class='input-group spinner'><input pattern='[0-9.]+' type='number'"
		
		+" onchange='changeQuantity(&#34;"+items[1]+"&#34;,&#34;" + items[0] + "&#34;," + items[3] + "," + items[4] + ",&#34;" + items[5] + "&#34;,this.value)'"
		
		
		+" class=' form-control disabled' value='" + items[2] + "'/></div></td>" +

            "<td>" + items[3] + " " + items[5] + "</td>" +
            "<td><span class='amt'>" + items[4] + "</span> " + items[5] + "</td>";


        tableRow += "<tr id='" + items[0] + "'>" + tableColumns + "<td><a class='btn btn-default disabled'><i class='fa  fa-trash'></i></a></td></tr>";

        setTimeout(function() {
            changeQuantity(items[0], items[3], items[4], items[5], items[2]);
        }, 1000);
    });

    $("#finalCart").append(tableRow);




}

function changeQuantity(name,id, pricePerUnit, subTotal, crncyCode, qty) {

    console.log(id, pricePerUnit, subTotal, crncyCode, qty);

    var total = $("#finalCart").find("#" + id).find(".amt");
    var amtPayable = 0;
    var subTtl = qty * pricePerUnit;
    $(total).html(subTtl);

    $("#finalCart .amt").each(function(key, val) {        
        amtPayable += Number($(val).text());
    });

    $("#totalAmt").html(amtPayable + " QAR");
	var finalCartData = loadLocalStorage();
	var idCartItem=[];
	
	finalCartData.forEach(function(val){
	
	idCartItem.push(val.split("|")[0]);	
	
	});
	
	if(idCartItem.indexOf(id)==-1)
	{
	
	}
	else{
	
	console.log(finalCartData[idCartItem.indexOf(id)]);
	
	finalCartData[idCartItem.indexOf(id)]=id + "|" + name + "|" + qty + "|" + pricePerUnit+ "|" + subTtl+"|" + crncyCode;
	localStorage.setItem("LS_cartItems", finalCartData);
	
	
	
	}
	
	
	
	
	
	
	
}

function placeOrder() {


    var dropAddressId;
    var additionalNote = "order from bluefin.com";
    var orderCartList;
    var quantity;
    var locale = "en";
    var pkgStyleId;
    var cartData = loadLocalStorage();

    var objRow = [];



    var orderData = {
        dropAddressId: addressId,
        additionalNote: additionalNote,
        orderCartList: []

    };



    cartData.forEach(function(val) {

        var items = val.split("|");

        console.log(items); //  

        var objColumns = "";

        var cartObj = {
            productUid: items[0],
            quantity: parseInt(items[2]),
            locale: "en",
            pkgStyleId: 1
        }

        //objRow.push(cartObj);

        orderData.orderCartList.push(cartObj);


    });
    orderData = JSON.stringify(orderData);
    console.log(loginJson);


    var placeOrder = new ajaxRequest();
    var placeOrderJson = placeOrder.ajax(orderData, apiUrl + "/item-order", "POST", "Custom " + loginJson.sessionKey);

    placeOrderJson.onreadystatechange = function(e) {

        if (placeOrderJson.readyState == 4) {

            placeOrderJson = JSON.parse(placeOrderJson.responseText);

            console.log(placeOrderJson);
            // {resourceId: 15, statusMsg: "Success"}
            //
            if ($.isEmptyObject(placeOrderJson) != true) {


			
			 if (placeOrderJson.statusMsg=="Success") {

             $('#msg').modal('toggle');
			
             
			   $('#msg').find('.modal-footer').show();
			   $("#continueShoppingBtn").click(function(){
			   
			    window.location = "index.html";
                localStorage.removeItem("LS_cartItems");
                loadCartData();
			   
			   });
            }else{

                 alert("Some error occurred!");
            }
				
            } else {
                alert("Some error occurred!");
            }


        }
    };



}



function addAddress(key) {


    var addAddress = new ajaxRequest();
    var addAddressJson = addAddress.ajax(address, apiUrl + "/address", "POST", "Custom " + key);

    addAddressJson.onreadystatechange = function(e) {

        if (addAddressJson.readyState == 4) {

            addAddressJson = JSON.parse(addAddressJson.responseText);
			
			
			
			
			
			if (addAddressJson.errors==undefined) {
			
			$('#msg').modal('toggle');
			$('#msg').find('#msgBody').html("Address Added Successfully");
			$('#msg').find('.modal-footer').hide();
            fetchAddress(loginJson.sessionKey);
            $("#addAddressSection").scrollTop(0);
			}else{
			
			var errorMsg="";
			
			addAddressJson.errors.forEach(function(val){
			errorMsg+="<p>"+val.defaultUserMessage+"</p>"
			console.log(val);
			});
			
			$('#msg').modal('toggle');
			$('#msg').find('#msgBody').html(errorMsg);
			$('#msg').find('.modal-footer').hide();
			
			
			}/**/

        }
    };
}

showCart=function(){


    $("section").addClass("hide");
$("#addAddressSection").removeClass("hide");
}