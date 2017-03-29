<%-- 
    Document   : reviews
    Created on : Dec 9, 2016, 5:02:53 PM
    Author     : zigchg
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reviews</title>
        <script src="assets/js/jquery.min.js"></script>
        <script src="assets/js/jquery.cookie.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/css/style.css" rel="stylesheet">
        <link href="assets/css/review.css" rel="stylesheet">
        <%
            String bizID = request.getParameter("businessID");
            String bizName = request.getParameter("name");
            String bizAddress = request.getParameter("address");
            String longitude = request.getParameter("longitude");
            String latitude = request.getParameter("latitude");
        %>
        
        <script>
            var businessID = "<%=bizID%>";
            var businessName = "<%=bizName%>";
            var businessAddress = "<%=bizAddress%>";
            var lng = "<%=longitude%>";
            var lat = "<%=latitude%>";
            
            var x = parseFloat(lng);
            var y = parseFloat(lat);
            
            console.log(lng);
            console.log(lat);
            console.log(businessID);
            console.log(businessAddress);
            

            
            console.log(x);
            console.log(y);
            

            var url = "ReviewDetails?businessID=" + businessID;
            
            function initMap() {
                var cairo = {
                    lat: y,
                    lng: x
                };
                var mapDiv = document.getElementById('map');
                var map = new google.maps.Map(mapDiv, {
                    scaleControl: true,
                    center: cairo,
                    zoom: 16
                });

                var marker = new google.maps.Marker({
                    map: map,
                    position: cairo
                });

                marker.addListener('click', function () {
                    infowindow.open(map, marker);
                });
            }
            


            $.get(url, function (data) {
                try {
                    var bizDiv = document.getElementById('bizInfo');
                    bizDiv.innerHTML = "<h1 id='bizName'>" + businessName + "</h1><p id='bizAddress'>" + businessAddress + "</p>";

                    // console.log(data);
                    var reviewDiv = document.getElementById('reviews_area');
                    reviewDiv.innerHTML = data;
                    // document.write(data);
                } catch (err) {
                    console.log(err.message);
                }
            });

        </script>
        <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDWWDa0QMMtD54yARjB8B8-KI2v-67eHIw&callback=initMap" type="text/javascript">
        </script>

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
                <div class="restaurant" id="bizInfo">
                    
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div id="map" style="height: 300px;"></div>
                                  
                </div>
            </div>


            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <h1>Reviews</h1>

                    <div id="reviews_area">

                    </div>

                </div>
            </div>

        </div>        
    </div>

</body>
</html>
