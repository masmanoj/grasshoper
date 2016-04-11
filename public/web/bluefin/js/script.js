/* APiUrl */
var apiUrl = "https://localhost:8443/grasshoper-core";
//var apiUrl = "https://192.168.0.104:8443/grasshoper-core";
/* APiUrl urls end*/



var _lan = "en";

(function() {
    //load localdata stored in cart
    loadCartData();
    //load tooltip
     $('[data-toggle="tooltip"]').tooltip(); 

    if (localStorage.getItem("selectedLanguage") != "null"||localStorage.getItem("selectedLanguage") != null) {
      
        _lan = localStorage.getItem("selectedLanguage");  

        //console.log(_lan);
    } else {
        _lan = "en";

    }



    // jQuery for page scrolling feature - requires jQuery Easing plugin
    $('a.page-scroll').bind('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top - 50)
        }, 1250, 'easeInOutExpo');
        event.preventDefault();
    });

    // Highlight the top nav as scrolling occurs
    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 51
    });




    $('#mainNav').affix({
        offset: {
            top: 100
        }
    });
    $("canvas").hide();
    $(window).scroll(function(val) {



        if ($(window).scrollTop() >= $(window).height()) {
            $(".panelFilterTop").addClass("fix");
            $("canvas").show();

        } else {

            $(".panelFilterTop").removeClass("fix");
            $("canvas").hide();

        }


    });

    // Initialize WOW.js Scrolling Animations
    new WOW().init();


console.log(window.location);
console.log(window.location.search.indexOf("product"));
    //if ((window.location.pathname.indexOf("index") != -1 && window.location.search == "") || window.location.pathname.indexOf("index") != -1) {
    if (window.location.search.indexOf("?product") == -1||window.location.search== "") {
        // LoadJSON Statically


        var ajaxCallStage = new ajaxRequest();
        var stageJson = ajaxCallStage.ajax("", apiUrl + "/stage", "GET", "");

        stageJson.onreadystatechange = function(e) {



            if (stageJson.readyState == 4) {

                stageJson = JSON.parse(stageJson.responseText);

                // createLeftPanel(stageJson.allPkgingStyles, "packingStle");
                createLeftPanel(stageJson.allCategories, "category");
                createLeftPanel(stageJson.allSortOrders, "sortOrders");
                $("#sortBy").prepend("<option>Sort by Price</option>");
            }
        };




    } //end of if




    //load data in to stage

    if ($("#displayContainer").length != 0) {


        var av = window.location.search;

        if (av != "") {
            //console.log(av.split("?")[1].split("=")[1]);
            doSearch(av.split("?")[1].split("=")[1]);
        } else {
            loadItemsToStage("");

        }

    }


    $("#sortBy").on("change", function(e) {


        var productLi;

        var product = $("#displayContainer");

        productLi = product.children('div.fish');
        productLi = sortItems(productLi, e.target.value);
        productLi.remove();

        productLi.each(function(keyy, vall) {

            $("#displayContainer").append(vall);

        });

    });


    $("#searchInput").on("keypress", function(e) {
        if (e.which == 13) {
            $("#searchBtn").click();
        }
    });

    $("#searchBtn").on("click", function() {


        doSearch($("#searchInput").val());
        //scrollDown
        $('html, body').animate({
            scrollTop: 708
        }, 'slow');
    });


    callSignUp = function(e) {

        var form = fetchInput($("#signupForm"));


        var name = form.name;
        var email = form.email;
        var password = form.password;
        var phone = form.phone;
        var returnUrl = "www.google.com";

        var params = {
            name: name,
            email: email,
            password: password,
            phone: phone,
            returnUrl: returnUrl
        };

        params = JSON.stringify(params);


        var ajaxCallSignup = new ajaxRequest();

        var signupJson = ajaxCallSignup.ajax(params, apiUrl + "/userapi/signup", "POST", "");

        signupJson.onreadystatechange = function(e) {

            if (signupJson.readyState == 4) {

                signupJson = JSON.parse(signupJson.responseText);

				//console.log(signupJson.statusMsg=="Success")

                if (signupJson.statusMsg=="Success") {
				$("#alertLogin").addClass("hide");
				
				$('#myModalLogin').modal('toggle');
				$('#modelMsg').modal('toggle');
				$('#modelMsg').find("#msgBody").html('Your account is created successfully. Please Confirm the mail to complete registration.');
				}else{
				
				$("#alertLogin").removeClass("hide");
				$("#alertLogin").html(signupJson.errors);
				
				
				
				}
                /*if (confirm("Your account is created successfully. Please Confirm the mail to complete registration.")) {

                    window.location = "index.html";
                }*/
            }
        };




    };



    callLogin = function(form,type) {

        
        var username = form.name;
        var password = form.password;

        var params = {
            username: username,
            password: password
        };
        params = JSON.stringify(params);


        var ajaxCallLogin = new ajaxRequest();
        loginJson = ajaxCallLogin.ajax(params, apiUrl + "/authentication", "POST", "");
        //console.log(params);
        loginJson.onreadystatechange = function(e) {

            if (loginJson.readyState == 4) {

                loginJson = JSON.parse(loginJson.responseText);



                if ($.isEmptyObject(loginJson) != true) {
				
				//console.log(loginJson);
				if (loginJson.authenticated==true) {
				
				
				//manoj.masmatrics@gmail.com
				
                if(type=="main"){

					$("#alertLogin").addClass("hide");
                    $("#myModalLoginBtn").click();
                    $("#myModalLoginBtn").html("Hi " + loginJson.name);
                }else if(type=="checkout"){


                    $("#LoginSection").hide();
                    $("#myAccountBtn").removeClass("hide");

                    $("#myAccountBtn").append("<ul class='dropdown-menu'><li><a href='#' >profile</a></li><li><a  onclick='getAddress(&#34;"+loginJson.sessionKey+"&#34;)'>Addresses</a></li><li><a href='#'>Past Orders</a></li><li><a onclick='showCart()' >Cart</a></li></ul>");

                    $("#addAddressSection").removeClass("hide");
                    fetchAddress(loginJson.sessionKey);
                    $("#loader").show();



                }
					
					
                }else{
				
				$("#alertLogin").removeClass("hide");
				$("#alertLogin").html(loginJson.errors);
				
				
				
				}
				
				
				}

            }
        };



    }


    $("#_clearall").click(function() {

        localStorage.removeItem("LS_cartItems");
        loadCartData();


    });




    $('#scene').parallax();


    changeLang(_lan);

    $('#changeLan').click(function() {

        if (_lan == "en") {

            _lan = "ar";
            //
            $("#changeLan").find("span").html("لعربية");

        } else {

            _lan = "en";
             $("#changeLan").find("span").html("English");

        }
        changeLang(_lan);
        localStorage.setItem("selectedLanguage", _lan);

    });


    $("#_loginBtn").click(function() {

        $('#loginForm').validator('validate');
     
        var inputVal = "";
        $("#loginForm").find("input").each(function(key, val) {

            inputVal = $(val).val();


        });


        if (inputVal != "") {
            callLogin(fetchInput($("#loginForm")),"main");
        }
    });


    $("#_signupBtn").click(function() {

        $('#signupForm').validator('validate');
       
        var inputVal = "";
        $("#signupForm").find("input").each(function(key, val) {

            inputVal = $(val).val();


        });


        if (inputVal != "") {
            callSignUp();
        }

    });
	
	
	

})(); // End of use strict




function changeLang(_lan) {

    if (_lan == "en") {

        transalteLan(enObj);$("#changeLan").find("span").html("English")



    } else {

        transalteLan(arObj);$("#changeLan").find("span").html("لعربية");;


    }
}

//console.log($(".translate").length);

function transalteLan(tranObj) {
    var objVal = "";
    var tranValue = "";
    $(".translate").each(function(key, val) {


        objVal = ($(val).attr("translate"));
        tranValue = tranObj[objVal];
        //console.log(objVal);


        $(val).html(tranValue);

    });

}

$(window).load(function() {

    //$("#loader").hide();
});

function doSearch(val) {


    if (window.location.pathname.indexOf("index") !== -1) {

        loadItemsToStage(val);
        //console.log(val);

        $("#searchHeading").html("Results for\t" + val);

    } else {

        window.location = "index.html?search=" + val;
    } /**/
}



function loadItemsToStage(qry) {
    if (qry != "") {

        qry = "?qry=" + qry;
    }

    var ajaxCallSearchItems = new ajaxRequest();
    var stageSearchJson = ajaxCallSearchItems.ajax("", apiUrl + "/stage/search-items" + qry, "GET", "");

    stageSearchJson.onreadystatechange = function(e) {



        if (stageSearchJson.readyState == 4) {

            stageSearchJson = JSON.parse(stageSearchJson.responseText);

            resultJson = stageSearchJson;
            displayData(resultJson);
            $("#loader").hide();


        }
    };
}



function sortItems(e, type) {

    if (type == 'Price Low to High') {

        return e.sort(function(a, b) {

            var compA = $(a).attr("price");
            var compB = $(b).attr("price");

            return compA - compB;

        });
    } else if (type == 'Price High to Low') {
        return e.sort(function(a, b) {

            var compB = $(a).attr("price");
            var compA = $(b).attr("price");

            return compA - compB;

        });



    }

}




function loadCartData() {


    var data = loadLocalStorage();
    var totalLabel = 0;
    if (data == "" || data == null) {

        $("#_crt_items").html(0);
        $("#_crt_items").addClass("wow flip animated");
        $("#myModalCart").find("#items").html("<div class='alert alert-warning'>No items in cart!</div>");
        $("#_checkout").attr("disabled", true);
        $("#_clearall").prop("disabled", true);


    } else {
        var product = "";
        var item = "";

        $("#_crt_items").html(data.length);
        $("#_crt_items").addClass("wow flip animated");
        $("#_checkout").attr("disabled", false);
        $("#_clearall").prop("disabled", false);

        data.forEach(function(val) {

            var items = val.split("|");

            product = "";
            product += "<td><span class='' title='remove item'><a class='btn btn-default' onclick='removeFromCart(&#34;" + items[0] + "&#34;)'><i class='fa fa-trash-o'></i></a></span> &nbsp;&nbsp;" + items[1] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><span class=''>Quantity:  " + items[2] + "</span></td><td>" + "Price per Unit: " + items[3] + " </td><td>Total: " + items[4] + "<b> " + items[5] + "</b></td>";

            totalLabel += parseInt(items[4]);

            items.forEach(function(data) {



                //product+=data;


            }); /**/


            item += "<tr class='' id='" + items[0] + "'>" + product + "</tr>";

        });

        $("#myModalCart").find("#items").html(item);
    }

   
    $("#totalLabel").find("span").html(totalLabel + " QAR");

}

function removeFromCart(id) {


    //console.log(event.target.value);
    $("#" + id).remove();




    var finalCartData = loadLocalStorage();
    var idCartItem = [];

    finalCartData.forEach(function(val) {

        idCartItem.push(val.split("|")[0]);

    });

    if (idCartItem.indexOf(id) == -1) {

    } else {



        finalCartData.splice(idCartItem.indexOf(id), 1);

        localStorage.setItem("LS_cartItems", finalCartData);
        if (finalCartData.length == 0) {

            $("#_checkout").attr("disabled", true);
            $("#_clearall").prop("disabled", true);
        } else {

            $("#_checkout").attr("disabled", false);
            $("#_clearall").prop("disabled", false);
        }


    }


    loadCartData();
}

function loadLocalStorage() {

    var data = localStorage.getItem("LS_cartItems");
    var cartItems = '';
    if (data != null) {
        cartItems = data.split(",");
    }
    return cartItems;
}

function fetchInput(identifier) {

    var form_data = identifier.serialize().split('&');
    var input = {};

    $.each(form_data, function(key, value) {
        var data = value.split('=');
        input[data[0]] = decodeURIComponent(data[1].replace("+", " "));
    });

    return input;
}