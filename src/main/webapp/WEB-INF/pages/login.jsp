<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Dispatcher Login Page</title>
<link rel="shortcut icon" href="./images/favicon_globalsight.PNG"/>
<link rel="stylesheet" href="./resources/css/style.css" />
<link rel="stylesheet" href="./resources/jquery/jQueryUI.redmond.css" />
<style type="text/css">
.login{
 width : 150px;
}
</style>
<!--[if lt IE 9]>
<script src="./resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="./resources/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="./resources/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript">

function submitenter(myfield,e) {
  var keycode;
  if (window.event) {
    keycode = window.event.keyCode;
  }  else if (e) {
    keycode = e.which;
  }
  else {
    return true;
  }
  if (keycode == 13) {
    $("#LogIn").click();
    return false;
  }
  else return true;
}

function login(){
    $("#error").empty();
	var accountName = $("#accountName").val();
	var password = $("#password").val();

	$.ajax({
		// the URL for the request
		url: "./login/main.json",

		// the data to send (will be converted to a query string)
		data: {
			  accountName : accountName
		    , password : password
		},
		// whether this is a POST or GET request
		type: "POST",
		// the type of data we expect back
		dataType : "json",
		// code to run if the request succeeds;
		// the response is passed to the function
		success: function( data ) {
			if(data.error == null){
				location.href = "./home/main.htm";
			}else{
			    $("#error").html(data.error);
			}
		},
		// code to run if the request fails; the raw request and
		// status codes are passed to the function
		error: function( xhr, status ) {
		    var msg = "Sorry, there was a problem!";
		    $("error").html(msg);
		}
	});
}

</script>

</head>
<body>
<!-- Form Element -->
<FORM NAME="dForm" METHOD="POST" ACTION="">
</FORM>
	<DIV>
		<header>
			<div class="leftIcon"><img src="./images/jdeere.png" alt="Logo"></div>
			<nav>
				<div class="clear"></div>
			</nav>
		</header>

		<div id="content" style="height:150px;">
			<p/>
			Welcome to Dispatcher. <p/>
			Dispatcher will allow you to translate text with Machine Translation engines. <p/>
           <table border="0" cellpadding="2">
                <tr>
                    <td>User Name:</td>
                    <td><input type='text' id='accountName' class="login" autocomplete="off"  /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type='password' id='password' class="login"  autocomplete="off"  onKeyPress="return submitenter(this,event)"></td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan='2'><input type="button" name="LogIn" id="LogIn" value="Log In" onclick="login()">&nbsp;</td>
                </tr>
            </table>
		</div>
		<p/>
		<div id="error" style="color:red">
		</div>
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
	</DIV>
</body>
</html>