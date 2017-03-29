<%-- 
    Document   : result
    Created on : Dec 7, 2016, 11:49:26 PM
    Author     : zigchg
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="assets/js/jquery.min.js"></script>
        <script src="assets/js/jquery.cookie.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/css/style.css" rel="stylesheet">
        <script>
            $(document).ready(function () {
                
                //console.log("111");
                var isReviewEmpty = $.cookie("isReviewEmpty");
                //console.log(isReviewEmpty);
                if(isReviewEmpty === "true"){
                    //console.log("222");
                    $("#sorry").append('<div class="alert alert-danger" role="alert">No reviews related to your input, please try with other meaningful search input</div>');
                    //console.log("333");
                }else{
                    
                    $("#sorry").empty();
                    
                    var business_id = [];
                    var i = 0;

                    var tag_num = [];
                    var j = 0;

                    var textbox = document.getElementById('index_searchbox');
                    var searchQuery = $.cookie("originalQuery");
                    textbox.value = searchQuery;

                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        //console.log(business_id[i]);
                        findBusiness(business_id[i]);
                        i++;
                    }
                    

//                    
//                    if(tag_num.length > 5){
//                        for(i = 0; i < 6; i++){
//                            findTag(tag_num[i]);
//                        }
//                    }else{
//                        for(i = 0; i < tag_num.length; i++){
//                            findTag(tag_num[i]);
//                        }
//                    }
//                    console.log("tag num is" + tag_num.length);
                    while (($.cookie("tag_num" + j)) != null) {
                        //console.log(tag_num[j]);
                        //findTag(tag_num[j]);
                        tag_num[j] = $.cookie("tag_num" + j);
                        j++;
                    }

                    //console.log(tag_num.length);

                    var k = 1;
                    var seed = tag_num.length - 1;
                    if (tag_num.length > 5) {
                        while (k < 7) {
                            //var picker = Math.floor(seed * Math.random());
                            //console.log(picker);
                            findTag(tag_num[k]);
                            k++;
                        }
                    } else {
                        while (tag_num[k] != null) {
                            //var picker = Math.floor(seed * Math.random());
                            findTag(tag_num[k]);
                            k++;
                        }
                    }
                }
            });



            function findBusiness(business_id) {
                var url = "FindBusiness?businessID=" + business_id;
                $.getJSON(url, function (data) {
                    console.log(data);

                    var resultDiv = document.getElementById('result');

                    var address = data.full_address;
                    var business_name = data.name;

                    var longitude = data.longitude;
                    var latitude = data.latitude;

                    var stars = data.stars;
                    var price = data["attributes"]["Price Range"];

                    var starUrl;
                    var priceUrl;

                    if (stars < 2.0) {
                        starUrl = "assets/img/stars/1.png";
                    } else if (stars < 3.0) {
                        starUrl = "assets/img/stars/2.png";
                    } else if (stars < 4.0) {
                        starUrl = "assets/img/stars/3.5.png";
                    } else if (stars === 4.0) {
                        starUrl = "assets/img/stars/4.png";
                    } else if (stars < 5.0) {
                        starUrl = "assets/img/stars/4.5.png";
                    } else if (stars === 5.0) {
                        starUrl = "assets/img/stars/5.png";
                    } else if (stars === null) {
                        starUrl = "";
                    }

                    if (price < 2) {
                        priceUrl = "assets/img/price/1.png";
                    } else if (price < 3) {
                        priceUrl = "assets/img/price/2.png";
                    } else if (price < 4) {
                        priceUrl = "assets/img/price/3.png";
                    } else if (price = 4) {
                        priceUrl = "assets/img/price/4.png";
                    } else if (price === null) {
                        priceUrl = "";
                    }

                    var starPlace;
                    var pricePlace;

                    if (priceUrl === "") {
                        pricePlace = "";
                    } else {
                        pricePlace = "<img src='" + priceUrl + "'/>";
                    }

                    if (starUrl === "") {
                        starPlace = "";
                    } else {
                        starPlace = "<img src='" + starUrl + "'/>";
                    }

//                    starPlace = "<img src='" + starUrl + "'/>";
//                    pricePlace = "<img src='" + priceUrl + "'/>";

                    console.log(starPlace);

                    var api_id = business_name.split(' ').join('-') + "-pittsburgh";
                    var api_id = api_id.split('&').join('and');

                    var api_id = api_id.split(',').join('');

                    var api_id = api_id.split("'").join('');

                    api_id = api_id.toLowerCase();

                    console.log(api_id);

                    var url_api = "YelpAPI?apiID=" + api_id;

//                    var image_url = findImage(url_api);
//
//                    console.log(image_url);

                    var img1 = "<div class='items'> <div class='row'>"
                            + "<div class='col-md-2 col-sm-3 col-xs-3'>"
                            + "<img class='img-responsive' src='";
                    var img2 = "'/>"
                            + "</div><div class='col-md-7 col-sm-6 col-xs-6'>"
                            + "<a href='";

                    var reviewDetails = "reviews.jsp?businessID=" + business_id;

                    var business_name_link = business_name.split("'").join("%27");
                    business_name_link = business_name_link.split("&").join("%26");

                    reviewDetails = reviewDetails + "&name=" + business_name_link + "&address=" + address + "&longitude=" + longitude + "&latitude=" + latitude;

                    var nameAndAddress = "'>" + business_name + "</a><br/>" + starPlace + "<br/>" + pricePlace + "</div>"
                            + "<div class='col-md-3 col-sm-3 col-xs-3'>"
                            + "<p>" + address + "</p></div></div></div>";

                    $.get(url_api, function (data_api) {
                        console.log(data_api);

                        if (data_api == "")
                            data_api = "assets/img/default.png";

                        resultDiv.innerHTML = resultDiv.innerHTML + img1 + data_api + img2 + reviewDetails + nameAndAddress;
                    });

                });
            }

            function findTag(tag_num) {
                var tagDiv = document.getElementById('tag_area');

                var tag = "<button type='button' class='btn btn-default tag_button'>" + tag_num + "</button>";

                tagDiv.innerHTML = tagDiv.innerHTML + tag;
            }


        </script>
        <title>Result</title>
    </head>
    <body>
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-2 col-sm-2 col-xs-4">
                        <a href="index.jsp"><img class="img-responsive" id="logo" src="assets/img/yelp-logo.png"/></a>
                    </div>
                    <form id="frmLogin" action="Scoring" method="post">
                        <div class="col-md-8 col-sm-8 col-xs-6">
                            <center><input id="index_searchbox" name="index_searchbox" maxlength="200" class="textbox" type="text"/></center>
                        </div>
                        <div class="col-md-2 col-sm-2 col-xs-2">
                            <input value="Search" type="submit" id="searchbutton" name="searchbutton" class="btn btn-default home_button" style="margin: 20px;">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </nav>

    <div id="wrapper">
        <div class="container-fluid">
            <div class="row">
                <p style="padding-left: 10px; font-size:14pt; font-style: italic;">You may also searching:</p>
                <div id="tag_area">

                </div>
            </div>

            <div class="row">                    
                <div id="filter">
                    <label style="color: #b30000;">Rating:</label>
                    <input id="all_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">Show All</label>
                    <input id="five_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">5 Stars</label>
                    <input id="fourfive_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">4.5 Stars</label>
                    <input id="four_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">4 Stars</label>
                    <input id="threefive_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">3.5 Stars</label>
                    <input id="three_stars" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">3 Stars Or Less</label>
                    <br/>
                    <label style="color: #b30000;">Price:</label>
                    <input id="four_dollar" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">$$$$</label>
                    <input id="three_dollar" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">$$$</label>
                    <input id="two_dollar" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">$$</label>
                    <input id="one_dollar" type="radio" name="filter" class="btn btn-default" style="margin: 13px 5px 15px 20px;"><label style="margin:0px;">$</label>
                </div>
            </div>

            <script>
                var star_num;
                var dollar_num;

                $("#all_stars").click(function () {
                    $("#result").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findBusiness(business_id[i]);
                        i++;
                    }
                });

                $("#five_stars").click(function () {
                    star_num = 5.0;
                    $("#result").empty();
                    $("#sorry").empty();
                    if($("#sorry").html() ===""){
                        console.log("empty!!!!")
                    }
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findStarBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#fourfive_stars").click(function () {
                    star_num = 4.5;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findStarBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#four_stars").click(function () {
                    star_num = 4.0;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findStarBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#threefive_stars").click(function () {
                    star_num = 3.5;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findStarBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#three_stars").click(function () {
                    star_num = 3.0;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findStarBusiness2(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });


                $("#four_dollar").click(function () {
                    dollar_num = 4;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findPriceBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#three_dollar").click(function () {
                    dollar_num = 3;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findPriceBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#two_dollar").click(function () {
                    dollar_num = 2;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findPriceBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                $("#one_dollar").click(function () {
                    dollar_num = 1;
                    $("#result").empty();
                    $("#sorry").empty();
                    var business_id = [];
                    var i = 0;
                    while ((business_id[i] = $.cookie("business_id" + i)) != null) {
                        findPriceBusiness(business_id[i]);
                        i++;
                    }
                    if (document.getElementById("result").innerHTML==null || document.getElementById("result").innerHTML=="") {
                        $("#sorry").append('<div class="alert alert-warning" role="alert">No more results</div>');
                    }
                });

                function findStarBusiness(business_id) {
                    var url = "FindBusiness?businessID=" + business_id;
                    $.getJSON(url, function (data) {
                        console.log(data);

                        var resultDiv = document.getElementById('result');

                        var address = data.full_address;
                        var business_name = data.name;

                        var longitude = data.longitude;
                        var latitude = data.latitude;

                        var stars = data.stars;
                        var price = data["attributes"]["Price Range"];

                        var starUrl;
                        var priceUrl;



                        if (stars !== star_num) {
                            return;
                        } else {

                            star_count = parseInt(star_num);

                            if (star_count - star_num !== 0) {
                                star_count = star_num;
                            }

                            starUrl = "assets/img/stars/" + star_count + ".png";

                            if (price < 2) {
                                priceUrl = "assets/img/price/1.png";
                            } else if (price < 3) {
                                priceUrl = "assets/img/price/2.png";
                            } else if (price < 4) {
                                priceUrl = "assets/img/price/3.png";
                            } else if (price = 4) {
                                priceUrl = "assets/img/price/4.png";
                            } else if (price === null) {
                                priceUrl = "";
                            }

                            var starPlace;
                            var pricePlace;

                            if (priceUrl === "") {
                                pricePlace = "";
                            } else {
                                pricePlace = "<img src='" + priceUrl + "'/>";
                            }

                            if (starUrl === "") {
                                starPlace = "";
                            } else {
                                starPlace = "<img src='" + starUrl + "'/>";
                            }

                            //                    starPlace = "<img src='" + starUrl + "'/>";
                            //                    pricePlace = "<img src='" + priceUrl + "'/>";

                            console.log(starPlace);

                            var api_id = business_name.split(' ').join('-') + "-pittsburgh";
                            var api_id = api_id.split('&').join('and');

                            var api_id = api_id.split(',').join('');

                            var api_id = api_id.split("'").join('');

                            api_id = api_id.toLowerCase();

                            console.log(api_id);

                            var url_api = "YelpAPI?apiID=" + api_id;

                            //                    var image_url = findImage(url_api);
                            //
                            //                    console.log(image_url);

                            var img1 = "<div class='items'> <div class='row'>"
                                    + "<div class='col-md-2 col-sm-3 col-xs-3'>"
                                    + "<img class='img-responsive' src='";
                            var img2 = "'/>"
                                    + "</div><div class='col-md-7 col-sm-6 col-xs-6'>"
                                    + "<a href='";

                            var reviewDetails = "reviews.jsp?businessID=" + business_id;

                            var business_name_link = business_name.split("'").join("%27");
                            business_name_link = business_name_link.split("&").join("%26");

                            reviewDetails = reviewDetails + "&name=" + business_name_link + "&address=" + address + "&longitude=" + longitude + "&latitude=" + latitude;

                            var nameAndAddress = "'>" + business_name + "</a><br/>" + starPlace + "<br/>" + pricePlace + "</div>"
                                    + "<div class='col-md-3 col-sm-3 col-xs-3'>"
                                    + "<p>" + address + "</p></div></div></div>";

                            $.get(url_api, function (data_api) {
                                console.log(data_api);

                                if (data_api == "")
                                    data_api = "assets/img/default.png";

                                resultDiv.innerHTML = resultDiv.innerHTML + img1 + data_api + img2 + reviewDetails + nameAndAddress;
                            });
                        }
                    });
                }


                function findStarBusiness2(business_id) {
                    var url = "FindBusiness?businessID=" + business_id;
                    $.getJSON(url, function (data) {
                        console.log(data);

                        var resultDiv = document.getElementById('result');

                        var address = data.full_address;
                        var business_name = data.name;

                        var longitude = data.longitude;
                        var latitude = data.latitude;

                        var stars = data.stars;
                        var price = data["attributes"]["Price Range"];

                        var starUrl;
                        var priceUrl;



                        if (stars >= star_num) {
                            return;
                        } else {

                            star_count = parseInt(star_num);

                            starUrl = "assets/img/stars/" + star_count + ".png";

                            if (price < 2) {
                                priceUrl = "assets/img/price/1.png";
                            } else if (price < 3) {
                                priceUrl = "assets/img/price/2.png";
                            } else if (price < 4) {
                                priceUrl = "assets/img/price/3.png";
                            } else if (price = 4) {
                                priceUrl = "assets/img/price/4.png";
                            } else if (price === null) {
                                priceUrl = "";
                            }

                            var starPlace;
                            var pricePlace;

                            if (priceUrl === "") {
                                pricePlace = "";
                            } else {
                                pricePlace = "<img src='" + priceUrl + "'/>";
                            }

                            if (starUrl === "") {
                                starPlace = "";
                            } else {
                                starPlace = "<img src='" + starUrl + "'/>";
                            }

                            //                    starPlace = "<img src='" + starUrl + "'/>";
                            //                    pricePlace = "<img src='" + priceUrl + "'/>";

                            console.log(starPlace);

                            var api_id = business_name.split(' ').join('-') + "-pittsburgh";
                            var api_id = api_id.split('&').join('and');

                            var api_id = api_id.split(',').join('');

                            var api_id = api_id.split("'").join('');

                            api_id = api_id.toLowerCase();

                            console.log(api_id);

                            var url_api = "YelpAPI?apiID=" + api_id;

                            //                    var image_url = findImage(url_api);
                            //
                            //                    console.log(image_url);

                            var img1 = "<div class='items'> <div class='row'>"
                                    + "<div class='col-md-2 col-sm-3 col-xs-3'>"
                                    + "<img class='img-responsive' src='";
                            var img2 = "'/>"
                                    + "</div><div class='col-md-7 col-sm-6 col-xs-6'>"
                                    + "<a href='";

                            var reviewDetails = "reviews.jsp?businessID=" + business_id;

                            var business_name_link = business_name.split("'").join("%27");
                            business_name_link = business_name_link.split("&").join("%26");

                            reviewDetails = reviewDetails + "&name=" + business_name_link + "&address=" + address + "&longitude=" + longitude + "&latitude=" + latitude;

                            var nameAndAddress = "'>" + business_name + "</a><br/>" + starPlace + "<br/>" + pricePlace + "</div>"
                                    + "<div class='col-md-3 col-sm-3 col-xs-3'>"
                                    + "<p>" + address + "</p></div></div></div>";

                            $.get(url_api, function (data_api) {
                                console.log(data_api);

                                if (data_api == "")
                                    data_api = "assets/img/default.png";

                                resultDiv.innerHTML = resultDiv.innerHTML + img1 + data_api + img2 + reviewDetails + nameAndAddress;
                            });
                        }
                    });
                }

                function findPriceBusiness(business_id) {
                    var url = "FindBusiness?businessID=" + business_id;
                    $.getJSON(url, function (data) {
                        //console.log(data);

                        var resultDiv = document.getElementById('result');

                        var address = data.full_address;
                        var business_name = data.name;

                        var longitude = data.longitude;
                        var latitude = data.latitude;

                        var stars = data.stars;
                        var price = data["attributes"]["Price Range"];

                        var starUrl;
                        var priceUrl;



                        if (price !== dollar_num) {
                            return;
                        } else {


                            priceUrl = "assets/img/price/" + dollar_num + ".png";

                            if (stars < 2.0) {
                                starUrl = "assets/img/stars/1.png";
                            } else if (stars < 3.0) {
                                starUrl = "assets/img/stars/2.png";
                            } else if (stars < 4.0) {
                                starUrl = "assets/img/stars/3.5.png";
                            } else if (stars === 4.0) {
                                starUrl = "assets/img/stars/4.png";
                            } else if (stars < 5.0) {
                                starUrl = "assets/img/stars/4.5.png";
                            } else if (stars === 5.0) {
                                starUrl = "assets/img/stars/5.png";
                            } else if (stars === null) {
                                starUrl = "";
                            }

                            var starPlace;
                            var pricePlace;

                            if (priceUrl === "") {
                                pricePlace = "";
                            } else {
                                pricePlace = "<img src='" + priceUrl + "'/>";
                            }

                            if (starUrl === "") {
                                starPlace = "";
                            } else {
                                starPlace = "<img src='" + starUrl + "'/>";
                            }

                            //                    starPlace = "<img src='" + starUrl + "'/>";
                            //                    pricePlace = "<img src='" + priceUrl + "'/>";

                            console.log(starPlace);

                            var api_id = business_name.split(' ').join('-') + "-pittsburgh";
                            var api_id = api_id.split('&').join('and');

                            var api_id = api_id.split(',').join('');

                            var api_id = api_id.split("'").join('');

                            api_id = api_id.toLowerCase();

                            console.log(api_id);

                            var url_api = "YelpAPI?apiID=" + api_id;

                            //                    var image_url = findImage(url_api);
                            //
                            //                    console.log(image_url);

                            var img1 = "<div class='items'> <div class='row'>"
                                    + "<div class='col-md-2 col-sm-3 col-xs-3'>"
                                    + "<img class='img-responsive' src='";
                            var img2 = "'/>"
                                    + "</div><div class='col-md-7 col-sm-6 col-xs-6'>"
                                    + "<a href='";

                            var reviewDetails = "reviews.jsp?businessID=" + business_id;

                            var business_name_link = business_name.split("'").join("%27");
                            business_name_link = business_name_link.split("&").join("%26");

                            reviewDetails = reviewDetails + "&name=" + business_name_link + "&address=" + address + "&longitude=" + longitude + "&latitude=" + latitude;

                            var nameAndAddress = "'>" + business_name + "</a><br/>" + starPlace + "<br/>" + pricePlace + "</div>"
                                    + "<div class='col-md-3 col-sm-3 col-xs-3'>"
                                    + "<p>" + address + "</p></div></div></div>";

                            $.get(url_api, function (data_api) {
                                console.log(data_api);

                                if (data_api == "")
                                    data_api = "assets/img/default.png";

                                resultDiv.innerHTML = resultDiv.innerHTML + img1 + data_api + img2 + reviewDetails + nameAndAddress;
                            });
                        }
                    });
                }
            </script>

            <div class="row">
                <div id="result" class="col-md-10 col-md-offset-1">

                </div>
                <div id="sorry" class="col-md-10 col-md-offset-1">

                </div>
            </div>


        </div>        
    </div>
</body>
</html>
