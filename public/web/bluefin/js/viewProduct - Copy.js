(function() {



    /* load  product */

    var product = window.location.search;
   
	
	var ajaxCallSearchItems = new ajaxRequest();
        var stageSearchJson = ajaxCallSearchItems.ajax("", apiUrl+"/stage/search-items"+product, "GET");

        stageSearchJson.onreadystatechange = function(e) {



            if (stageSearchJson.readyState == 4) {

                stageSearchJson = JSON.parse(stageSearchJson.responseText);				
				
				parseData(stageSearchJson[0]);
				console.log(stageSearchJson[0]);

                
            }
        };
	
	
    /* load  product ends*/
	


})(); // Loads first

$(window).load(function() {

    $("#loader").hide();
});




function parseData(item) {

    
	var carousalInner="";
    item.productImages.forEach(function(val) {

        $("#myCarousel").find(".carousel-indicators").append("<li class='item' data-target='#myCarousel' data-slide-to='" + val.displayOrder + "'></li>");
        
		carousalInner+="<div class='item'>"           + "<img src='" + val.imageUrl + "'  alt='" + val.label + "' /></div>";
		//$("#myCarousel .carousel-inner").append("<div class='item'>"           + "<img src='" + val.imageUrl + "'  alt='" + val.label + "' /></div>");

    });
	
	$("#myCarousel").append("<div class='carousel-inner' role='listbox'>"+carousalInner+"</div>");

    $("#itemProduct").html("<div class='container-fluid'>" 
	
	+ "<h2>" + item.name + "</h2></div>" 
	
	+ "<div class='container-fluid'>" + "Minimum qty: 500gm <br>" + "<h3>" + item.pricePerUnit + " " + item.currencyCode + "</h3>"
        //+"<span class='link'>"+item.currencyCode+"</span>"
        + "<p>" + item.desc0 + "</p>" 
		+ "<p>" + item.currencyCode + "</p>" 
		+ "<p><strong>Qty:</strong> <input type='number' id='itemQty' placeholder='enter quantity' value='1' />" + "</p>" 
		//+ "<button class='btn btn-success btn-lg' >Buy Now</button> "
		
		//+ "<button class='btn btn-warning btn-lg' onclick='addToCart(&#34;" + item.productUid + "&#34;,&#34;" + item.name + "&#34;," + 1 + ",&#34;" + item.pricePerUnit + "&#34;)'>Add to Cart</button>" 
		
		+ "<button class='btn btn-warning btn-lg' onclick='addToCart("+JSON.stringify(item)+")'>Add to Cart</button>" 
	
	
	+ "</div>");

$("#productDesc").html("<p>" + item.desc1 + "</p><br>"+"<p>" + item.desc2 + "</p>");


    $("#myCarousel").find(".carousel-indicators").find("li:first").addClass("active");
    $("#myCarousel").find(".carousel-inner").find("div:first").addClass("active");
}


function addToCart(data) {

console.log(data);
qty=$("#itemQty").val();

var cartItem = [];

	if(loadLocalStorage()!=""){cartItem = loadLocalStorage();}
	
    cartItem.push(JSON.stringify(data));

    localStorage.setItem("LS_cartItems", cartItem);
    
    loadCartData();
	$("#cartAlert").show();
	$("#cartAlert #cartInfo").html("<strong>"+data.name+"</strong> added to Cart");
	//$("#viewCartBtn").click();
	
/**/
}

