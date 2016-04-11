(function() {



    /* load  product */

    var product = window.location.search;
   
	
	var ajaxCallSearchItems = new ajaxRequest();
        var stageSearchJson = ajaxCallSearchItems.ajax("", apiUrl+"/stage/search-items"+product, "GET","");

        stageSearchJson.onreadystatechange = function(e) {



            if (stageSearchJson.readyState == 4) {

                stageSearchJson = JSON.parse(stageSearchJson.responseText);				
				
				parseData(stageSearchJson[0]);
				//console.log(stageSearchJson[0].name);
				$("#loader").hide();
				$(".breadcrumb li:last").html("<a href='product.html"+product+"'>"+(stageSearchJson[0]).name+"</a>")

                
            }
        };
	
	
    /* load  product ends*/
	
checkIfAdded(product);


$('.jzoom').jzoom({

  // width / height of the magnifying glass
  width: 400,
  height: 400,

  // where to position the zoomed image
  position: "right",

  // x/y offset in pixels.
  offsetX: 20,
  offsetY: 0,

  // opacity level
  opacity: 0.6,

  // background color
  bgColor: "#fff",

  // loading text
  loading: "Loading..."
  
});

})(); // Loads first

function checkIfAdded(product)
{

var cartItem=[];
var idCartItem=loadLocalStorage();
console.log(idCartItem);
idCartItem.forEach(function(val){
 
cartItem.push(val.split("|")[0]);

});
var pdt=product.substr(9,product.length);
console.log(pdt);
if(cartItem.indexOf(pdt)==-1)
	{
  
	}
	else{
	
	$('#modelMsg').modal('toggle');
	
	}

}


function parseData(item) {
console.log(item);
    
	var carousalInner="";
    item.productImages.forEach(function(val) {

        $("#myCarousel").find(".carousel-indicators").append("<li class='item' data-target='#myCarousel' data-slide-to='" + val.displayOrder + "'></li>");
        
		carousalInner+="<div class='item jzoom'>"           + "<img src='" + val.imageUrl + "'  alt='" + val.label + "' /></div>";
		//$("#myCarousel .carousel-inner").append("<div class='item'>"           + "<img src='" + val.imageUrl + "'  alt='" + val.label + "' /></div>");

    });
	
	$("#myCarousel").append("<div class='carousel-inner' role='listbox'>"+carousalInner+"</div>");

    $("#itemProduct").html("<div class='container-fluid'>" 
	
	+ "<h1>" + item.name + "</h1><hr></div>" 
	
	+ "<div class='container-fluid'>" + "Minimum Quantity:"+item.minimumQuantity+" <br>" + "<h3>" + item.pricePerUnit + " " + item.currencyCode + "</h3>"
       
        + "<p>" + item.desc0 + "</p>" 
		
		//+ "<p><div class='input-group spinner'><label class='input-group-addon'><strong>Quantity:</strong> </label><input class='form-control' type='number' id='itemQty' placeholder='enter quantity' value='1' /><label class='input-group-addon' >"+item.quantityUnit+"</label></div>" + "</p>" 
		
			+"<p><div class='input-group number-spinner spinner'><span class='input-group-btn data-dwn'><button class='btn btn-default' data-dir='dwn'><span class='glyphicon glyphicon-minus'></span></button>			</span>	"
			
			+"<input type='text' class='form-control text-center' id='itemQty' value='1' min='-10' max='40'>			<span class='input-group-btn data-up'><button class='btn btn-default' data-dir='up'><span class='glyphicon glyphicon-plus'></span></button>				</span>	<label class='input-group-addon' >"+item.quantityUnit+"</label>			</div></p>"
		/**/
		+ "<button class='btn btn-warning btn-lg' onclick='addToCart(&#34;" + item.productUid + "&#34;,&#34;" + item.name + "&#34;," + 1 + ",&#34;" + item.pricePerUnit + "&#34;,&#34;"+item.currencyCode+"&#34;)'>Add to Cart</button>" 
	+ "</div>");

$("#productDesc").html("<p>" + item.desc1 + "</p><br>"+"<p>" + item.desc2 + "</p>");


    $("#myCarousel").find(".carousel-indicators").find("li:first").addClass("active");
    $("#myCarousel").find(".carousel-inner").find("div:first").addClass("active");
	
	callSpinner();
}


function addToCart(id, name, qty, price,currency) {

qty=$("#itemQty").val();

var cartItem = [];
var idCartItem = [];
var total=qty*price;
	
	if(loadLocalStorage()!=""){cartItem = loadLocalStorage();}
	
	
	cartItem.forEach(function(val){
	
	idCartItem.push(val.split("|")[0]);	
	
	});
	
	if(idCartItem.indexOf(id)==-1)
	{
    cartItem.push(id + "|" + name + "|" + qty + "|" + price+ "|" + total+"|" + currency);
	localStorage.setItem("LS_cartItems", cartItem);
    
    loadCartData();
	$("#cartAlert").show();
	$("#cartAlert #cartInfo").html("<strong>"+name+"</strong> added to Cart");
	
	}
	else{
	
	
	$('#modelMsg').modal('toggle');
	
	}
	console.log("index is thre\t"+cartItem.indexOf(id));

    

}

