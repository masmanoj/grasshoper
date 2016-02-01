var stageJson; //loads stage json from stage
var resultJson; //loads stage json from result


var displayData = function(data) {
    var displayItems = "";
    var img = "";
    data.forEach(function(val) {

        if (val.productImages[0] != undefined) {
            img = val.productImages[0].imageUrl;
        } else {
            img = "img/e.png";
        }
        
		displayItems += "<div class='col-md-3 itemFish wow zoomIn' price='" + val.pricePerUnit + "'>"

        + "<img src='" + img + "'>" + "<a class='fishName'>" + val.name + "</a>" + "<p>WILD Minimum qty:" + val.pricePerUnit + "<br><span class='link'> Price Per Unit: <br/>" + val.pricePerUnit + " " + val.currencyCode + "</span></p>" + "<a class='btn btn-default' href='" + "product.html?product=" + val.productUid + "'>View Item</a>"

        + "</div>"; //itemFish
    });

    document.getElementById("displayContainer").innerHTML = displayItems;

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
            filterList += "<li><a "

            + "id='" + val.id + "' displayOrder='" + val.displayOrder + "' tag='" + val.tag + "' taglabel='" + val.taglabel

                + "' >"

            + "<label>" + "<input type='checkbox' name='" + category + "' > " //create checkbox
                + val.label + "</label>" //create label


            + "</a></li>";
        }
    });

    filterList = "<ul class='nav nav-list'>" + filterList + "</ul>";
    console.log(filterList);
    $("#filters").append("<div class='panel-body'>" + filterList + "</div>");
    //console.log(filterListSort);
    $("#sortBy").append(filterListSort);


};