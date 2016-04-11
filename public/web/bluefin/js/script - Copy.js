/* APiUrl */
var apiUrl = "https://grasshoper-ghbluefin.rhcloud.com/grasshoper-core";

/* APiUrl urls end*/

(function() {
    //load localdata stored in cart
    loadCartData();


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

    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 51
    })



    // Fit Text Plugin for Main Header
    $("header h1").fitText(
        1.2, {
            minFontSize: '40px',
            maxFontSize: '80px'
        }
    );

    // Offset for Main Navigation
    $('#mainNav').affix({
        offset: {
            top: 100
        }
    });

    // Initialize WOW.js Scrolling Animations
    new WOW().init();


console.log(window.location);
    if ((window.location.pathname.indexOf("index") != -1&&window.location.search=="")||window.location.pathname.indexOf("index") != -1) {
        // LoadJSON Statically


        var ajaxCallStage = new ajaxRequest();
        var stageJson = ajaxCallStage.ajax("", apiUrl + "/stage", "GET");

        stageJson.onreadystatechange = function(e) {



            if (stageJson.readyState == 4) {

                stageJson = JSON.parse(stageJson.responseText);

               // createLeftPanel(stageJson.allPkgingStyles, "packingStle");
                createLeftPanel(stageJson.allCategories, "category");
                createLeftPanel(stageJson.allSortOrders, "sortOrders");
            }
        };


    } //end of if

    $("#_signup").click(function() {
	
	var form = fetchInput($("#signup"));
	

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
        var signupJson = ajaxCallSignup.ajax(params, apiUrl + "/userapi/signup", "POST");

        signupJson.onreadystatechange = function(e) {

            if (signupJson.readyState == 4) {

                signupJson = JSON.parse(signupJson.responseText);

                console.log(signupJson);
            }
        };

    }); //end of signup click




    //load data in to stage

    if ($("#displayContainer").length != 0) {

        loadItemsToStage("");       

    }


    $("#sortBy").on("change", function(e) {

        sortBy(e.target.value);

    });



    
    $("#searchInput").on("keyup",function() {

        
		 $('html, body').stop().animate({
            scrollTop: ($("#displayContainer").offset().top+100)
        }, 1250, 'easeInOutExpo');

        loadItemsToStage($("#searchInput").val());
		
		$("#searchHeading").html("Results for\t"+$("#searchInput").val());
		

    });


    // $("#login").validator();
    $("#signup").validator();

    $("#_login-btn").validator().on("click", function(e) {

        var form = fetchInput($("#login"));

        if ("1" === 1) {
            // handle the invalid form...



        } else {



            var username = form.name; 
            var password = form.password; 

            var params = {
                username: username,
                password: password
            };
            params = JSON.stringify(params);


            var ajaxCallLogin = new ajaxRequest();
            var loginJson = ajaxCallLogin.ajax(params, apiUrl + "/authentication", "POST");
            console.log(params);
            loginJson.onreadystatechange = function(e) {

                if (loginJson.readyState == 4) {

                    loginJson = JSON.parse(loginJson.responseText);

                    console.log(loginJson.name);
					$("#myModalLoginBtn").click();
					$("#myModalLoginBtn").html("Hi "+loginJson.name);

                }
            };
        }




    });
	
	
	$("#_clearall").click(function(){
	
	localStorage.removeItem("LS_cartItems");
	loadCartData();
	
	
	});

    //var scene = document.getElementById("_section");
    //var parallax = new Parallax(scene);




})(); // End of use strict

$(window).load(function() {

    $("#loader").hide();
});
var qry = "";

function loadItemsToStage(qry) {
    if (qry != "") {

        qry = "?qry=" + qry;
    }

    var ajaxCallSearchItems = new ajaxRequest();
    var stageSearchJson = ajaxCallSearchItems.ajax("", apiUrl + "/stage/search-items" + qry, "GET");

    stageSearchJson.onreadystatechange = function(e) {



        if (stageSearchJson.readyState == 4) {

            stageSearchJson = JSON.parse(stageSearchJson.responseText);

            resultJson = stageSearchJson;
            displayData(resultJson);


        }
    };
}

function sortBy(e) {

    var productLi;
    $("#displayContainer").find(".itemFish").each(function(key, val) {


        var product = $(val),

            productLi = product.children('div.productList');
        productLi = sortItems(productLi, 'alpha');
        productLi.remove();

        productLi.each(function(keyy, vall) {

            $(val).append(vall);

        });
    });



}


function sortItems(e, type) {

    if (type == 'numeric') {
        return e.sort(function(a, b) {

            var compA = $(a).attr("seq");
            var compB = $(b).attr("seq");

            return compA - compB;

        });
    } else {

        return e.sort(function(a, b) {

            var compA = $(a).text().toUpperCase();
            var compB = $(b).text().toUpperCase();
            return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;

        });
    }

}




function loadCartData() {
console.log("loading data");

    var data = loadLocalStorage();
	
    if (data == "" || data == null) {
	
		$("#_crt_items").html(0);
        $("#_crt_items").addClass("wow flip animated");
		$("#myModalCart").find("#items").html("<div class='alert alert-warning'>No items in cart!</div>");
	
	
	} else {
	
	
        var product = "";
        var item = "";

        $("#_crt_items").html(data.length);
        $("#_crt_items").addClass("wow flip animated");
		console.log(data);
        data.forEach(function(val) {

            var items = val.split("|");

            product = "";
            product += "<span class='' title='remove item'><a onclick='removeFromCart(&#34;" + data.name + "&#34;)'><i class='fa fa-trash-o'></i></a></span> " + items[1] + "<span class='label label-warning pull-right'>" + items[2]*items[3] + "</span>";
            


            item += "<p class='cartItem' id='" + items[0] + "'>" + product + "</p>";

        });

        $("#myModalCart").find("#items").html(item);
    }


}

function removeFromCart(id) {


    alert(id);
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
        input[data[0]] = decodeURIComponent(data[1]);
    });

    return input;
}