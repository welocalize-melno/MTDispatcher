<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="com.globalsight.dispatcher.bo.MTPLanguage"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Languages Detail Page</title>
<link rel="shortcut icon" href="../images/favicon_globalsight.PNG"/>
<link rel="stylesheet" href="../resources/css/style.css" />
<link rel="stylesheet" href="../resources/jquery/jQueryUI.redmond.css" />
<!--[if lt IE 9]>
<script src="../resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="../resources/js/utilityScripts.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript">

var msg_validate_inpute_name = "Please enter the User Name.";

var msg_validate_inpute_account = "Please select the Account.";
var msg_validate_inpute_srcLocale = "Please select the Source Locale.";
var msg_validate_inpute_trgLocale = "Please select the Target Locale.";
var msg_validate_inpute_mtProfileId = "Please select the Machine Translation Profile.";

function fnCancel(){
	location.href= "../mtpLanguages/main.htm";
}

function fnSaveOrUpdate(){
    $("#error").empty();
	var mtpLangName = stripBlanks($("#mtpLangName").val());
	var accountID = $("#account").val();
	var srcLocaleId = $("#srcLocale").val();
	var trgLocaleId = $("#trgLocale").val();
	var mtProfileId = $("#mtProfile").val();
	
	if(isEmptyString(mtpLangName)) {
	    $("#error").html(msg_validate_inpute_name);
		return false;
	}

	if("-1" == accountID) {
	     $("#error").html(msg_validate_inpute_account);
	     return false;
	}

	if("-1" == srcLocaleId) {
	    $("#error").html(msg_validate_inpute_srcLocale);
		return false;
	}

	if("-1" == trgLocaleId) {
	    $("#error").html(msg_validate_inpute_trgLocale);
		return false;
	}

	if("-1" == mtProfileId) {
	    $("error").html(msg_validate_inpute_mtProfileId);
		return false;
	}
	
	$.ajax({
		// the URL for the request
		url: "../mtpLanguages/saveOrUpdate.json",
		// the data to send (will be converted to a query string)
		data: {
			  mtpLangID : ${mtpLanguage.id}
			, mtpLangName : mtpLangName
			, accountID : accountID
			, mtpLangSrcLocaleID : srcLocaleId
			, mtpLangTrgLocaleID : trgLocaleId
			, mtProfileID : mtProfileId
		},
		// whether this is a POST or GET request
		type: "POST",
		// the type of data we expect back
		dataType : "json",
		// code to run if the request succeeds;
		// the response is passed to the function
		success: function( data ) {
			if(data.error == null){
				location.href = "../mtpLanguages/main.htm";
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
</script>
</head>
<body>
<DIV>
		<%@ include file="/WEB-INF/pages/header.jspIncl" %>	

		<div>
			<p />
			<p />
			Language Detail
			<p />
			<p />
			<p />
			<p />
		</div>
		<div id="content">
		<table border="0" class="standardText" cellpadding="2">
			<tr>
				<td class="standardText">Name<span class="asterisk">*</span>:</td>
				<td>
				  <input type="text" id="mtpLangName" name="mtpLangName" 
				  	value="${mtpLanguage.name}" style="width:98%;">
				</td>
			</tr>
			<tr>
				<td class="standardText">Source Locale<span class="asterisk">*</span>:
				</td>
				<td>
				  <select name="srcLocale" id="srcLocale" class="standardText" style="width:99%;">
					<c:if test="${mtpLanguage.id == -1}">
						<option value="-1">&nbsp;</option>
					</c:if>
					<c:forEach items="${allGlobalSightLocale}" var="locale">
						<c:choose>
							<c:when test="${locale.id == mtpLanguage.srcLocale.id}">
								<option value="${locale.id}" selected>${locale.displayName}</option>
							</c:when>
							<c:otherwise>
								<option value="${locale.id}">${locale.displayName}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				  </select>
				</td>
			</tr>
			<tr>
				<td class="standardText">Target Locale<span class="asterisk">*</span>:
				</td>
				<td>
				  <select name="trgLocale"  id="trgLocale" class="standardText" style="width:99%;">
					<c:if test="${mtpLanguage.id == -1}">
						<option value="-1">&nbsp;</option>
					</c:if>
					<c:forEach items="${allGlobalSightLocale}" var="locale">
						<c:choose>
							<c:when test="${locale.id == mtpLanguage.trgLocale.id}">
								<option value="${locale.id}" selected>${locale.displayName}</option>
							</c:when>
							<c:otherwise>
								<option value="${locale.id}">${locale.displayName}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				  </select>
				</td>
			</tr>
			<tr>
				<td class="standardText">Machine Translation<br/>Profile<span class="asterisk">*</span>:
				</td>
				<td>
				  <select name="mtProfile" id="mtProfile" class="standardText" style="width:99%;">					
					<c:if test="${mtpLanguage.id == -1}">
						<option value="-1">&nbsp;</option>
					</c:if>
					<c:forEach items="${allMTProfiles}" var="mtProfile">
						<c:choose>
							<c:when test="${mtProfile.id == mtpLanguage.mtProfile.id}">
								<option value="${mtProfile.id}" selected>${mtProfile.mtProfileName}</option>
							</c:when>
							<c:otherwise>
								<option value="${mtProfile.id}">${mtProfile.mtProfileName}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				  </select>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
      		<tr>
        		<td colspan="2">
          			<input type="button" name="Cancel" value="Cancel" onclick="fnCancel()">
          			<input type="button" name="Save" value="Save" onclick="fnSaveOrUpdate()">
        		</td>
      		</tr>
		</table>
		</div>
		<div id="error" style="color:red">
		</div>
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
</DIV>
</body>
</html>