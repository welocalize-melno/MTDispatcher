<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Account Detail Page</title>
<link rel="shortcut icon" href="../images/favicon_globalsight.PNG"/>
<link rel="stylesheet" href="../resources/css/demo_page.css" />
<link rel="stylesheet" href="../resources/css/demo_table.css" />
<link rel="stylesheet" href="../resources/css/demo_table_jui.css" />
<link rel="stylesheet" href="../resources/css/style.css" />
<link rel="stylesheet" href="../resources/jquery/jQueryUI.redmond.css" />
<!--[if lt IE 9]>
<script src="../resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="../resources/js/utilityScripts.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery.dataTables.min.js"></script>
<script type="text/javascript">

var msg_validate_inpute_fullName = "Please enter the Account Holder name.";
var msg_validate_inpute_email = "Please enter the valid e-mail address.";
var msg_validate_inpute_password_val = "Paasword's minimum length is 7 characters, maximum length is 25 characters, and HTML special characters are not allowed";
var msg_validate_inpute_passwords_do_match = "Password and confirmed password do not match.";
var msg_validate_inpute_allthreefields = "Old password, new password, and confirmation of new password shall be provided for password update";


function fnCancel(){
	location.href= "../home/main.htm";
}

function fnSaveProfile(){

    $("#error").empty();

	var fullName = stripBlanks($("#fullName").val());
	var password = stripBlanks($("#password").val());
	var oldpassword = stripBlanks($("#oldpassword").val());
	var confirmPassword = stripBlanks($("#confirmPassword").val());
	var email = stripBlanks($("#email").val());

	if(isEmptyString(fullName)) {
		 $("#error").html((msg_validate_inpute_fullName));
		 return false;
	}

    if(!validEmail(email)) {
        $("#error").html(msg_validate_inpute_email);
		return false;
	}

	if(!isEmptyString(oldpassword) && !isEmptyString(password) && !isEmptyString(confirmPassword)) {
	   if(hasHtmlSpecialChars(password) || !isNotShorterThan(password,7) || !isNotLongerThan(password,25)) {
	      $("#error").html(msg_validate_inpute_password_val);
	      return false;
	   }

	   if(!(password == confirmPassword)) {
	      $("#error").html(msg_validate_inpute_passwords_do_not_match);
	      return false;
	   }
	} else if(!(isEmptyString(oldpassword) && isEmptyString(password) && isEmptyString(confirmPassword))) {
		  $("#error").html(msg_validate_inpute_allthreefields);
		  return false;
	}

	$.ajax({
		// the URL for the request
		url: "../profile/updateProfile.json",
		// the data to send (will be converted to a query string)
		data: {
			  fullName : fullName
			, oldpassword : oldpassword
		    , password : password
		    , email : email
		},
		// whether this is a POST or GET request
		type: "POST",
		// the type of data we expect back
		dataType : "json",
		// code to run if the request succeeds;
		// the response is passed to the function
		success: function( data ) {
			if(data.error == null){
				location.href = "../home/main.htm";
			}else{
			    $("#error").html(data.error);
			}
		},
		// code to run if the request fails; the raw request and
		// status codes are passed to the function
		error: function( xhr, status ) {
		    var msg = "Sorry, there was a problem!";
		    $("#error").html(msg);		}
	});
}

</script>
<style>
.boxed {
  border: 1px solid red;
}
</style>
</head>
<body>
<!-- Form Element -->
<FORM NAME="dForm" id="dForm" METHOD="POST" ACTION="">
</FORM>

	<DIV>
		<%@ include file="/WEB-INF/pages/header.jspIncl" %>

		<div>
			<p />
			<p />
			User Profile
			<p />
			<p />
			<p />
			<p />
		</div>

		<div id="content">
		 
		<table border="0" class="standardText" cellpadding="2">
			<tr>
				<td class="standardText">User Name<span class="asterisk">*</span>:</td>
				<td>
				  <input type="text" class="standardText" id="accountName" name="accountName"
				  	 disabled value="${theAccount.accountName}" style="width:98%;">
				</td>
			</tr>
			
			<tr>
				<td class="standardText">Full Name<span class="asterisk">*</span>:</td>
				<td>
				  <input type="text" class="standardText" id="fullName" name="fullName"
				  <c:if test="${theAccount.ssoUser == 'Yes'}"> disabled </c:if>	value="${theAccount.fullName}" style="width:98%;">
				</td>
			</tr>

			<tr>
				<td class="standardText">E-mail Address<span class="asterisk">*</span>:</td>
				<td>
				  <input type="email" class="standardText" id="email" name="email"
				  	<c:if test="${theAccount.ssoUser == 'Yes'}"> disabled </c:if>value="${theAccount.email}" style="width:98%;">
				</td>
			</tr>
			 
			<c:if test="${theAccount.ssoUser == 'Yes'}">
			<tr>
				<td class="standardText">Type<span class="asterisk">*</span>:</td>
				<td>
				<c:if test="${theAccount.type == 'Administrator'}">
				   Administrator 
				</c:if>
				<c:if test="${theAccount.type != 'Administrator'}">
				   User 
 				</c:if>
 				</td>
			</tr>
			</c:if>
        </table>

        <p/>
       
 		 <c:if test="${theAccount.ssoUser == 'No'}">
	 		<table border="0" cellpadding="2">
	         <tr>
	           <td colspan=2 class="standardText" align=center>Change Password</td></tr>
				<tr>
					<td class="standardText">Old Password:</td>
					<td>
					  <input type="password" class="standardText" id="oldpassword" name="oldpassword" autocomplete="off" style="width:98%;">
					</td>
				</tr>
	
				<tr>
					<td class="standardText">New Password:</td>
					<td>
					  <input type="password" class="standardText" id="password" name="password" autocomplete="off" style="width:98%;">
					</td>
				</tr>
	
				<tr>
					<td class="standardText">Confirm New Password:</td>
					<td>
					  <input type="password" class="standardText" id="confirmPassword" name="confirmPassword" autocomplete="off" style="width:98%;">
					</td>
				</tr>
	         </table>	         
		 
        <table border="0" class="standardText" cellpadding="2">
			<tr><td colspan="2">&nbsp;</td></tr>
      		<tr>
        		<td colspan="2">
          			<input type="button" name="Cancel" value="Cancel" onclick="fnCancel()">
          			<input type="button" name="Save" value="Save" onclick="fnSaveProfile()">
        		</td>
      		</tr>
		</table>
		</c:if>
		</div>
		<div id="error" style="color:red">
		</div>
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
	</DIV>
</body>
</html>