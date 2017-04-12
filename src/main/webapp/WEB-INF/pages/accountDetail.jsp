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

var msg_validate_inpute_name = "Please enter the user name.";
var msg_validate_inpute_name_val = "User name should be alphanumeric, minimum length is 3 characters, maximum length is 15 characters.";
var msg_validate_inpute_fullName = "Please enter the Account Holder name.";
var msg_validate_inpute_email = "Please enter the valid e-mail address.";
var msg_validate_inpute_passwords_do_not_match = "Password and confirmed password do not match.";
var msg_validate_inpute_password = "Password and confirmed password should not be empty.";

function fnCancel(){
	location.href= "../account/main.htm";
}

function fnSaveOrUpdate(){

    $("#error").empty();

	var accountName = stripBlanks($("#accountName").val());
	var fullName = stripBlanks($("#fullName").val());
	var password = stripBlanks($("#password").val());
	var confirmPassword = stripBlanks($("#confirmPassword").val());
	var type = stripBlanks($("input[name=type]:checked").val());
	var email = stripBlanks($("#email").val());
	var description = stripBlanks($("#description").val());
	var userType = "";
	if(typeof $("input[name = 'userType']:checked").val() === "undefined"){
		userType = "No";
	}else{
		userType = stripBlanks($("input[name = 'userType']:checked").val());
	}
	
	if((userType == "No") && (isEmptyString(password) || isEmptyString(confirmPassword))){
		$("#error").html(msg_validate_inpute_password);
		return false;
	}
	
	if(isEmptyString(accountName)) {
	    $("#error").html(msg_validate_inpute_name);
		return false;
	}

	if(hasSpecialChars(accountName) || !isNotShorterThan(accountName,3) || !isNotLongerThan(accountName,15)) {
	  $("#error").html(msg_validate_inpute_name_val);
	  return false;
	}

	if(isEmptyString(fullName)) {
	     $("#error").html(msg_validate_inpute_fullName);
		 return false;
	}

    if(!validEmail(email)) {
        $("#error").html(msg_validate_inpute_email);
		return false;
	}

	if(!(password == confirmPassword)) {
        $("#error").html(msg_validate_inpute_passwords_do_not_match);
		return false;
	}

	$.ajax({
		// the URL for the request
		url: "../account/saveOrUpdate.json",
		// the data to send (will be converted to a query string)
		data: {
			  accountId : ${account.id}
			, accountName : accountName
			, fullName : fullName
		    , password : password
		    , type : type
		    , email : email
			, description : description
			, userType : userType
		},
		// whether this is a POST or GET request
		type: "POST",
		// the type of data we expect back
		dataType : "json",
		// code to run if the request succeeds;
		// the response is passed to the function
		success: function( data ) {
			if(data.error == null){
				location.href = "../account/main.htm";
			}else{
                $("#error").html(data.error);
			}
		},
		// code to run if the request fails; the raw request and
		// status codes are passed to the function
		error: function( xhr, status ) {
		    var msg = "Sorry, there was a problem!";
            $("#error").html(msg);
		}
	});
}


$(document).ready(function() {
	$("input[name = 'type']").change(function() {
		roleCheck();
	});
	
	$("input[name = 'userType']").change(function() {
		roleCheck();
	});
	
	function roleCheck(){
		var userRole = $("input[name = 'userType']:checked").val();
		if(userRole == "Yes"){
			//SSO user
			$(".password,.confrimPassword").hide();
			$("#password,#confirmPassword").val('');
		}
		else{
			//non-SSO user
			$(".password,.confrimPassword").show();
			$("#password,#confirmPassword").val($('#oldPassword').val());
		}
 		return;
	}
});


</script>
</head>
<body>
<!-- Form Element -->
<FORM NAME="dForm" id="dForm" METHOD="POST" ACTION="">
<input type="hidden" name="selectedID">
</FORM>

	<DIV>
		<%@ include file="/WEB-INF/pages/header.jspIncl" %>

		<div>
			<p />
			<p />
			Account Detail
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
				 <c:choose>
                  <c:when test="${account.id < 0}">
    				  <input type="text" id="accountName" name="accountName"
				  	   value="${account.accountName}" style="width:98%;">
                  </c:when>
                  <c:otherwise>
       				  <input type="text" id="accountName" name="accountName"
       				      value="${account.accountName}" style="width:98%;" disabled>
                  </c:otherwise>
                 </c:choose>
				</td>
			</tr>
			<tr>
				<td class="standardText">Full Name<span class="asterisk">*</span>:</td>
				<td>
				  <input type="text" id="fullName"
				    name="fullName"
				  	value="${account.fullName}"
				  	style="width:98%;"
                    <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  >
				</td>
			</tr>
			
			<tr class="password">
				<td class="standardText">Password<span class="asterisk">*</span>:</td>
				<td>
				  <input type="password" id="password" name="password"
				  	value="${account.password}" style="width:98%;"
                    <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  >
				  <input type="hidden" id="oldPassword" name="oldPassword" value="${account.password}">
				</td>
			</tr>
			<tr class="confrimPassword">
				<td class="standardText">Confirm Password<span class="asterisk">*</span>:</td>
				<td>
				  <input type="password" id="confirmPassword" name="confirmPassword"
				  	value="${account.password}" style="width:98%;"
                    <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  >
				</td>
			</tr>
			 
			<tr>
				<td class="standardText">Account Type<span class="asterisk">*</span>:</td>
				<td>
				  <input type="radio" id="Administrator" name="type" value="Administrator"
                  <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  >
				  Administrator
				 				   
				  <input type="radio" id="User" name="type"	value="User" checked="true"
                   <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  >
				  User
				</td>
			</tr>

			<tr>
				<td class="standardText">E-mail Address<span class="asterisk">*</span>:</td>
				<td>
				  <input type="email" id="email" name="email"
				  	value="${account.email}" style="width:98%;"
                    <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
				  	>
				</td>
			</tr>
			
			<tr>
				<td class="standardText">Description:</td>
				<td>
				  <textarea id=description name="description" style="width:97%;" <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>>${account.description}</textarea>
				</td>
			</tr>
			<c:if test = "${account.accountName != null  &&  account.accountName != globalAdmin }">
			<tr>
				<td class="standardText">SSO User<span class="asterisk">*</span>:</td>
				<td>
				  <input type="radio" id="ssoUser" name="userType" value="Yes">Yes
				  <input type="radio" id="user" name="userType"	value="No">No
				</td>
			</tr>
			</c:if>
			
			<tr><td colspan="2">&nbsp;</td></tr>
      		<tr>
        		<td colspan="2">
          			<input type="button" name="Cancel" value="Cancel" onclick="fnCancel()">
          			<input type="button" name="Save" value="Save" onclick="fnSaveOrUpdate()"
                    <c:if test="${account.accountName == globalAdmin && theAccount.accountName != globalAdmin}">disabled</c:if>
          			>
        		</td>
      		</tr>
		</table>
		</div>
		<div id="error" style="color:red">
		</div>
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
		<c:if test = "${account.type == gAdministrator}">
		      <script>
			    $("#Administrator").prop("checked", true);
			  </script>
		</c:if>
		<c:if test = "${account.ssoUser == ssoUser}">
		      <script>
			    $("#ssoUser").prop("checked", true);
			    $(".password,.confrimPassword").hide();
			  </script>
		</c:if>
		<c:if test = "${account.ssoUser != ssoUser}">
		      <script>
			    $("#user").prop("checked", true);
			    $(".password,.confrimPassword").show();
			  </script>
		</c:if>
	</DIV>
</body>
</html>