/*
*CLASS: AJAXREQUEST
*DESIGNED FOR OOJS AJAX REQUEST
*/

function ajaxRequest() {
    var req;

    this.ajax = function(params, url,method) {

        if (window.XMLHttpRequest) {
		/* code for IE7+, Firefox, Chrome, Opera, Safari */
            req = new XMLHttpRequest();

        } else {
		/* code for IE6, IE5 */
            req = new ActiveXObject("Microsoft.XMLHTTP");

        }
        req.open(method, url, true);
        req.setRequestHeader("Access-Control-Allow-Headers","*");
        req.setRequestHeader("Content-Type", "application/json; charset=utf-8"); 
		req.send(params);
		return req;

    }

}