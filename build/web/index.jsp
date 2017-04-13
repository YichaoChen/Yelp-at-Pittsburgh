<%-- 
    Document   : index
    Created on : Dec 7, 2016, 9:44:48 PM
    Author     : cyc
--%>
<%@page import="edu.pitt.is2140.processing.Scoring"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Yelp at Pittsburgh</title>
        <script src="assets/js/jquery.min.js"></script>
        <script src="assets/js/jquery.cookie.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/css/style.css" rel="stylesheet">
        <style type="text/css">

	body {
	    background-image: url(assets/img/background.jpg);
	}
        
        </style>
    </head>
    <body>
<!--        <form id="frmLogin" action="Scoring" method="post">
            <label for="txtQuery">Query: </label>&nbsp;<input type="text" id="txtQuery" name="txtQuery" value="">
            <br />
            <input type="submit" id="btnSubmit" name="btnSubmit" value="Search">&nbsp;
        </form>-->
        
        <div id="wrapper">
            <div id="index_content" style="padding-top: 30px;">
              <div class="container-fluid">
                <div class="row">
                  <center><a href="index.html"><img class="img-responsive" id="index_logo" src="assets/img/yelp-logo.png"/></a></center>
                </div>
                <form id="frmLogin" action="Scoring" method="post">
                    <div class="row">
                        <div class="col-md-8 col-md-offset-2">  
                            <center><input id="index_searchbox" maxlength="200" name="index_searchbox" class="index_textbox" type="text" placeholder="I am looking for......"/></center>
                        </div>
                    </div>
                    <div class="row">
                        <center>
                          <input type="submit" id="searchbutton" name="searchbutton" value="Search" class="btn btn-default home_button" style=" margin: 20px;">&nbsp;
                        </center>
                    </div>
                </form>
              </div>
            </div>
        </div>
    </body>
</html>
