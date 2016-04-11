var stageJson; //loads stage json from stage
var resultJson; //loads stage json from result
/* APiUrl */
var apiUrl = "https://grasshoper-ghbluefin.rhcloud.com/grasshoper-core";
/* APiUrl urls end*/

var displayData = function(data) {
    var displayItems = "";
    var img = "";
    data.forEach(function(val) {


        if (val.productImages[0] != undefined) {
            img = val.productImages[0].imageUrl;
        } else {
            img = "img/e.png";
        }
        for (var i = 0; i < val.packagingStyles.length; i++) {
            //console.log(val.packagingStyles[i].label);
            //console.log(val.packagingStyles[i].displayOrder);
        }

        displayItems += "<div class='col-lg-3 col-md-3 col-xs-12 col-sm-6 fish '><div  class='itemFish ' price='" + val.pricePerUnit + "'>"

        + "<img src='" + img + "'>" + "<h4 class='fishName'>" + val.name + "</h4>" 
		+ "<p><span class='link'> Price Per Unit: " + val.pricePerUnit + " " + val.currencyCode + "</span></p>" 
		//+ "<hr>" 
		+ "<a class='btn btn-warning hvr-wobble-horizontal' href='" + "product.html?product=" + val.productUid + "'>View Item</a>"

        + "</div></div>"; //itemFish
    });

    document.getElementById("displayContainer").innerHTML = "<div class='row'>"+displayItems+"</div>"

};


var createLeftPanel = function(data, category) {

    var filterListSort = "";
    var filterList = "";

    data.forEach(function(val) {
       

        if (category == "sortOrders") {

            filterListSort += "<option><a "

            + "id='" + val.id + "' displayOrder='" + val.displayOrder + "' tag='" + val.tag + "' taglabel='" + val.taglabel

                + "' >"

            //+ "<input type='radio' name='" + category + "' > " //create checkbox
            + val.label


                + "</a></option>";

        } else {
            filterList += "<div class='col-md-2 col-xs-6 fil hvr-wobble-vertical filter"+val.subTag+"'><span class='icon "+val.subTag+" sm'></span><span "

            + "id='" + val.id + "' class='iconLabel' displayOrder='" + val.displayOrder + "' tag='" + val.tag + "' taglabel='" + val.taglabel

                + "' >"

            + "<label>" + "<input id='chk" + val.subTag + "' type='checkbox' onchange='fetchCategory(&#34;" + val.subTag + "&#34;,this)' name='" + category + "' > " //create checkbox
                + val.label + "</label>" //create label


            + "</span></div>";
            console.log('val\t' + val);
        }
    });

  

    $("#filters").append( filterList );
    //console.log(filterListSort);
    $("#sortBy").append(filterListSort);



};


function fetchCategory(item, elm) {
var catagory = [];
   
    //alert($("#chk"+item).is(":checked"));
    /*if ($("#chk" + item).is(":checked")) {
        catagory.push(item);
        str = str + catagory;
    } else {
        catagory.splice(item, 1);
        str = "";
    }*/
	
	$("#filters").find("input").each(function(key,val){
	
	
	if ($(val).is(":checked")) {
		
		//$(".filter"+item).addClass("filterColor");
        catagory.push($(val).attr("id").substring(3,$(val).attr("id").length));
		console.log(catagory);
       
    } else{console.log("false");
	
	//$(".filter"+item).removeClass(".filterColor");
	
	}
	
	});
	
	
	catagory.forEach(function(val){


	
	
	});
	
	$("#filters").find(".fil").each(function(key,val){
	
	if ($(val).find("input").is(":checked")) {
	
	$(val).removeClass("filterColor");
	$(val).addClass("filterColor");
	
	
	}else{
	$(val).removeClass("filterColor");
	
	}
	
	
	});

    
    var ajaxCallSearchItems = new ajaxRequest();
    var stageSearchJson = ajaxCallSearchItems.ajax("", apiUrl + "/stage/search-items?types=" +catagory, "GET", "");

    stageSearchJson.onreadystatechange = function(e) {



        if (stageSearchJson.readyState == 4) {

            stageSearchJson = JSON.parse(stageSearchJson.responseText);
           
            displayData(stageSearchJson);
            $("#loader").hide();


        }
    };

}