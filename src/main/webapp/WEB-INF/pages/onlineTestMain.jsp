<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Translate</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="shortcut icon" href="../images/favicon_globalsight.PNG"/>
<link rel="stylesheet" href="../resources/css/style.css" />
<link rel="stylesheet" href="../resources/jquery/jQueryUI.redmond.css" />
 
<style type="text/css">
select {
	width: 250px;
}
#swapBtn[disabled], #feedbackBtn[disabled], #transBtn[disabled]{
	background-color: #559e5c;
    opacity: 0.5;
}
#trgText,#srcText { width: 353px; height: 75px; padding: 0.5em; }
.ajax_loader {background: url("../images/spinner_squares_circle.gif") no-repeat center center transparent;width:100%;height:100%;}
.blue-loader .ajax_loader {background: url("../images/ajax-loader_blue.gif") no-repeat center center transparent;}
#swapBtn{
	margin-left:30px;
    width: 70px;
    height: 25px;
    margin-bottom : 4px;
}
.btnColor{
	background-color:#288132;
	color: white;
}
</style>
<!--[if lt IE 9]>
<script src="../resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="../resources/js/utilityScripts.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript" src="../resources/jquery/jquery-ui-1.10.4.custom.min.js"></script>
<script type="text/javascript" src="../resources/jquery/script.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../resources/jquery/common.js"></script>
<script type="text/javascript">
 
var basicURl = '${basicURL}';
var msg_validate_inpute_account = "Please login to the Account.";
var msg_validate_inpute_srcLocale = "Please select the Source Locale.";
var msg_validate_inpute_trgLocale = "Please select the Target Locale.";
var msg_validate_inpute_srcText = "The Source Text is empty.";
var msg_no_language_pairs = "No language pairs were configured for this account!";
var msg_validate_inpute_domain = "Please select domain.";
$(document).ready(function() {
	$("#srcLocale,#trgLocale,#srcText,#trgText,#transBtn").prop( "disabled", true);
	fnGetDomains();
	var feedbackForm = $('#feedbackModalForm').html(); 
	// function to initialize feedback modal window on shown and initialize form fields
	$('#feedbackModal').on('shown.bs.modal', function(){	
		if($.trim($("#srcText").val()) == '' || $.trim($("#trgText").val()) == ''){
			return;
		}
		$('#feedbackModalForm').html(feedbackForm);		
		$('#loading').addClass("hide");
 		$('#sendFeedbackBtn').prop("disabled",true);
 		if (GetIEVersion() > 0) {
			$("#sourceText,#targetText,#usrFeedback").resizable();
 			
 			$("#sourceText,#targetText,#usrFeedback").css('height','75px');
 			$("#sourceText,#targetText,#usrFeedback").css('width','353px');
 			$(".ui-wrapper").css('margin-top','10px');
 			$('#feedbackModal .ui-resizable-se').css('bottom','0px');
 			$('#feedbackModal .ui-resizable-se').css('right','21px');
 		 	$("#sourceText,#targetText,#usrFeedback").resizable({
 			    resize: function() {
 			    	$(this).parent().find('.ui-resizable-se').css('bottom','30px');
 			    	$(this).parent().find('.ui-resizable-se').css('right','35px');
 			    }
 			}); 
 		}
 		
		//Assigning values to feedback form
		$('#feedbackModalForm .sourceLocale').text($('#srcLocale option:selected').html());
		$('#feedbackModalForm .sourceText').val($("#srcText").val());
		$('#feedbackModalForm .targetLocale').text($('#trgLocale option:selected').html());
		$('#feedbackModalForm .targetText').val($("#trgText").val());
    });
	
	
	<!--To resize textarea boxes in IE.  -->
	function GetIEVersion() {
	  var sAgent = window.navigator.userAgent;
	  var Idx = sAgent.indexOf("MSIE");

	  // If IE, return version number.
	  if (Idx > 0) 
	    return parseInt(sAgent.substring(Idx+ 5, sAgent.indexOf(".", Idx)));

	  // If IE 11 then look for Updated user agent string.
	  else if (!!navigator.userAgent.match(/Trident\/7\./)) 
	    return 11;

	  else
	    return 0; //It is not IE
	}
	if (GetIEVersion() > 0) {
		$("#trgText,#srcText").resizable();
		$("#trgText,#srcText").css('height','75px');
		$("#trgText,#srcText").css('width','353px');
		$(".ui-wrapper").css('margin-top','10px');
		  $('.ui-resizable-se').css('bottom','2px');
		$('.ui-resizable-se').css('right','2px');
		
	 	$("#srcText,#trgText").resizable({
		    resize: function() {
		    	$(this).parent().find('.ui-resizable-se').css('bottom','29px');
		    	$(this).parent().find('.ui-resizable-se').css('right','24px');
		    }
		});  
	}
	
});

	function fnTranslate(){  
	    $("#error").empty();
		var accountName = "${theAccount.accountName}";
		var srcLang = $.trim($("#srcLocale").val());
		var trgLang = $.trim($("#trgLocale").val());
		var src = $.trim($("#srcText").val());
		var target = $.trim($("#trgText").val());
		var domain = $.trim($('#domain').val());
		if(isEmptyString(accountName)) {
			$("#error").html(msg_validate_inpute_account);
			return false;
		}
		if("-1" == domain) {
			$("#error").html(msg_validate_inpute_domain);
			return false;
		}
		if("-1" == srcLang) {
			$("#error").html(msg_validate_inpute_srcLocale);
			return false;
		}
	
		if("-1" == trgLang) {
		    $("#error").html(msg_validate_inpute_trgLocale);
			return false;
		}
	
		if(isEmptyString(src)) {
		    $("#error").html(msg_validate_inpute_srcText);
			return false;
		}
		
		if(!isEmptyString(target)) {
			return false;
		}
		
		var params = "";
		params += "&accountName=" + accountName;
		params += "&srcLang=" + srcLang;
		params += "&trgLang=" + trgLang;
		params += "&src=" + encodeURIComponent(src);
		params += "&srcLocale=" + $.trim($('#srcLocale option:selected').html());
		params += "&trgLocale=" + $.trim($('#trgLocale option:selected').html());
		params += "&domain=" + $.trim($('#domain option:selected').html());
		params += "&domainId=" + $("#domain").val();
		$.ajax({
	        type: 'POST',
	        url: '../translate/?' + params,
	        dataType: 'json',
	     //   timeout: 100000000,
	        success: function (data) {
	        	if("timeout" == data.status) {
	               location.href = "../home/error.htm";
	            } 
	            if("success" == data.status) {
	                $("#trgText").val(data.trg);
	                fnSaveTranslatedData(params,data.trg);
	    			fnChkFeedbackAccess();               
	            } else {
	                $("#error").html("Translation fails. " + data.errorMsg);
	                $('#swapBtn').prop('disabled',true);
	            }
	        },
	        error: function (jqXHR, textStatus, errorThrown) {
	        	if(jqXHR.status == 0){
	        		location.href = "../home/error.htm";
	        	}else{
	           		 var msg = "No Response.";
	            	 $("#error").html(msg);
	        	}
	        }
	    });
	}
//function used to get all domains
function fnGetDomains(){
	$.ajax({
		url: "../domain/getDomains.json",
		type: "GET",
		dataType : "json",
		success: function( data ) {
			var domains = data.allDomains;
			if(domains != null && domains.length > 0){
				for(i=0; i<domains.length; i++){
					$("#domain").append("<option value='"+domains[i].id+"'>"+domains[i].domainName+"</option>");
				}
				fnGetLanguagePair();
			}
		},
		error: function( xhr, status ) {
			if(xhr.status == 0){
        		location.href = "../home/error.htm";
        	}else{
				var msg = "Sorry, there was a problem!";
				$("#error").html(msg);
        	}
		}
	});
	
	return;
}
//Function to get locales based on anohter(value)
function fnGetLocales(value,id,selectedLocaleType,targetSelect){
	var engLangAvialible = false;
	$("#error").html("");
	$('#swapBtn').prop('disabled',true);
	if(targetSelect != ""){// Swap button disable
		$('#swapBtn').prop('disabled',false);
	}
	var accountID = ${theAccount.id};
	var selectedLocaleValue = value;
	var targetedLocaleId = "#"+id;
	$(targetedLocaleId).empty();  
	$("#error").empty();

	if("-1" == accountID ) { 
		return;
	}
	
	$.ajax({
		url: "../onlineTest/getLocales.json",
		data: {
			"accountID" : accountID,
			"selectedLocale" : selectedLocaleValue,
			"selectedLocaleType" : selectedLocaleType,
			"domain" : $('#domain').val()
			},
		type: 'POST',
		dataType : "json",
		success: function(data) {
			if("timeout" == data.status) {
				location.href = "../home/error.htm";
	        } 
			var trgLocales = data.trgLocales;
			if(trgLocales != null && trgLocales.length > 0){
				//$(targetedLocaleId).append("<option value='-1'></option>")
				for(i=0; i<trgLocales.length; i++){
					var shortName = trgLocales[i].language + "_" + trgLocales[i].country;
					//console.log("target"+shortName+'==='+checkLangExists(shortName));
					var optionName = (checkLangExists(shortName)== -1) ?trgLocales[i].displayLanguage: removeLangShorName(trgLocales[i].displayName);
					if(shortName == "en_US"){
						engLangAvialible = true;
						continue;
					}
					$(targetedLocaleId).append("<option value='"+shortName+"'>"+optionName+"</option>");
				}
				
				if(trgLocales.length == i && engLangAvialible){
					$("<option value='en_US' selected='selected'>English (United States)</option>").prependTo(targetedLocaleId);
				}
				// while swaping autoselect target value 
				if(targetSelect != ""){
					$(targetedLocaleId).val(targetSelect);
				}
				// Empty source and target text values 
				if(targetSelect == "" ){
					fnEmptySrcTrgValues();
					$('#feedbackBtn').prop('disabled',true);
				}
				
				//check swap button 
				fnSwapBtnEnableDisable();
			}
		},
		error: function( xhr, status ) {
			if(xhr.status == 0){
        		location.href = "../home/error.htm";
        	}else{
				var msg = "Sorry, there was a problem!";
				$("#error").html(msg);
        	}
		}
	});
	
	return;
}
//function to enable/disable feedback button
function fnChkFeedbackAccess(){
	var accountID = ${theAccount.id};
	var srcLocale = $("#srcLocale").val();
	var trgLocale = $("#trgLocale").val();
	var srcValue = $("#srcText").val();
	var trgValue = $("#trgText").val();	
	
	// feedback button will enable only when all translation page fileds are non-empty
	if("-1" == srcLocale || "-1" == trgLocale || isEmptyString(srcValue) || isEmptyString(trgValue) ) {
		 $('#feedbackBtn').prop('disabled',true);
		return false;
	}
 	$("#error").empty();
	if("-1" == accountID) {
		return;
	}
	$.ajax({
		url: "../onlineTest/checkFeedbackAccess",
		data: {
			"accountID" : accountID,
			"srcLocale" : srcLocale,
			"trgLocale" : trgLocale,
			"domain"    : $("#domain").val()
		},
		type: "POST",
		dataType : "json",
		success: function( data ) {
			 if(data.accessStatus == 'true'){
				 $('#feedbackBtn').prop('disabled',false);
			 }else{
				 $('#feedbackBtn').prop('disabled',true);
			 }
		},
		error: function( xhr, status ) {
			if(xhr.status == 0){
        		location.href = "../home/error.htm";
        	}else{
				var msg = "Sorry, there was a problem!";
				$("#error").html(msg);
				$('#feedbackBtn').prop('disabled',true);
        	}
		}
	});
	return;
}

// function to send feeback email to admin
function fnFeedback(){
	if($('#usrFeedback').val() == '' ){
		return;
	}
	$('#loading').removeClass("hide");
	$.ajax({
        type: 'POST',
        url: '../translate/feedback',
        data:{
        	"accountID"    : ${theAccount.id},
			"srcLocale"    : $('#srcLocale').val(),
			"trgLocale"    : $('#trgLocale').val(),
			"srcLocaleTxt" : $('#feedbackModalForm .sourceLocale').text(),
			"trgLocaleTxt" : $('#feedbackModalForm .targetLocale').text(),
			"srcText"      : $('#feedbackModalForm .sourceText').val(),
			"trgText"      : $('#feedbackModalForm .targetText').val(),
			"usrFeedback"  : $('#feedbackModalForm .usrFeedback').val(),
        },
        dataType: 'json',
        success: function (data) {
        	$('#feedbackModal').modal('toggle');       
        	if("timeout" == data.status) {
	               location.href = "../home/error.htm";
	            } 
        	if(data.status = "success"){
        		$('#successMsgModal').modal('show');
            	$('#successMsgModal .modal-body p').html("Feedback has been sent successfully.");
        	}
        	else{
        		$('#successMsgModal').modal('show');
            	$('#successMsgModal .modal-body p').html("Seems there is some problem ,Please try again.");
        	}
         },
        error: function (data) {
        	$('#feedbackModal').modal('toggle');
        	$('#successMsgModal').modal('show');
        	$('#successMsgModal .modal-body p').html("Seems there is some problem ,Please try again.");
         }
    });
}

//function to enable/disable swap button
function fnSwapBtnEnableDisable(){
	
	var accountID = ${theAccount.id};
	var trgLocale = $("#trgLocale").val();
	var srcLocale = $("#srcLocale").val();
	var domain = $("#domain").val();
	var srcFlag = trgFlag= false;
	
	if(trgLocale == null && srcLocale == null){
		$('#swapBtn').prop('disabled',true);
		$('#feedbackBtn').prop('disabled',true);
		return;
	}
	$.ajax({
		url: "../onlineTest/checkSwapBtnAccess",
		data: {
			"accountID" : accountID,
 			"srcLocale" : trgLocale,
			"trgLocale" : srcLocale,
			"domain"    : domain
		},
		type: "POST",
		dataType : "json",
		success: function( data ) {	
			 if("timeout" == data.status) {
				 location.href = "../home/error.htm";
	         } 
			 if(data.accessStatus == 'true'){
				 $('#swapBtn').prop('disabled',false);
			 }else{
				 $('#swapBtn').prop('disabled',true);
			 }
		},
		error: function( xhr, status ) {
			if(xhr.status == 0){
        		location.href = "../home/error.htm";
        	}else{
				var msg = "Sorry, there was a problem!";
				$("#error").html(msg);
				$('#swapBtn').prop('disabled',true);
        	}
		}
	});
	$("#trgText").val('');	
	// target locale on change check feedback button access
	fnChkFeedbackAccess();
}

// function to swap Locales and values 
function fnSwap() {
	var sourceLocaleVal = $('#srcLocale').val();
	var trgLocaleVal = $('#trgLocale').val();
	var srcValue = $("#srcText").val();
	var trgValue = $("#trgText").val();	
  	
 	// Swap Locales
	fnSwapLocale(trgLocaleVal,sourceLocaleVal);
	
	if(srcValue =='' && trgValue == ''){
		return ;
	}
	
	// Both source and target values are not empty
	if(srcValue !='' && trgValue != ''){
		$("#srcText").val(trgValue);
		$("#trgText").val(srcValue);
		return;
	}
	
	// target value (afterswap source value) not empty and soruce value (afterswap target) value empty,so translation
	if(trgValue != '' &&  srcValue ==''){
		$("#srcText").val(trgValue);
		fnTranslate();		
		return;
	} 
	
	// Empty source and target values when source value (after swap target value) is exists
	if(trgValue == '' &&  srcValue !=''){ 
		fnEmptySrcTrgValues();
	}
	 
	return;
}

function fnSwapLocale(trgLocaleVal,sourceLocaleVal){
	$("#error").empty();
	var accountID = ${theAccount.id};
	if("-1" == accountID) {
		$("#srcLocale").prop( "disabled", true );
		$("#trgLocale").prop( "disabled", true );
		$("#srcText").prop( "disabled", true );
        $("#transBtn").prop( "disabled", true );
		$("#error").html(msg_validate_inpute_account);
		return;
	}
	$("#srcLocale").val(trgLocaleVal);
	
	//after swap autopopulate target field language pairs
	fnGetLocales(trgLocaleVal,"trgLocale","source",sourceLocaleVal);
}

// function to display message 
function fnShowErrorMsg(msg){
	$("#error").html('');
	$("#error").html(msg);
	return;
}

// functin to enable/disable send button in translate feedback modal window
function sendFeedbackBtnEnable(){	
 	if($('#usrFeedback').val() != ''){
		$('#sendFeedbackBtn').prop('disabled',false);
	}
 	else{
 		$('#sendFeedbackBtn').prop('disabled',true);
 	}
 	return;
}
// functin to make source and target text value empty
function fnEmptySrcTrgValues(){
	$("#srcText").val('');
	$("#trgText").val('');	
	return;
}
function fnSrcTextChange(){
	if($("#trgText").val()!= ""){
		$("#trgText").val('');	
	}
	return;
}
// show modal window only when source and text values are not empty
function fnFeedbackModal(){
	if($.trim($("#srcText").val()) == '' || $.trim($("#trgText").val()) == '' || $.trim($("#srcLocale").val()) == '' || $.trim($("#trgLocale").val()) == ''){
		 $('#feedbackBtn').prop('disabled',true);
		return ;
	}
	$('#feedbackModal').modal("show"); 
}
function fnSaveTranslatedData(params,targetText){
	var accountID = ${theAccount.id};
	params+= "&trg=" + encodeURIComponent(targetText)+"&accountID="+accountID;
	$.ajax({
        type: 'POST',
        url: '../translate/saveTranslatedData/?'+ params,
        dataType: 'json',
        success: function (data) {
            if("success" == data.status) {
            } 
            if("timeout" == data.status) {
              location.href = "../home/error.htm";
            } 
            if("error" == data.status) {
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var msg = "";
            $("#error").html(msg);
        }
    });
	return;
}
function fnGetLanguagePair(){
	$("#error").empty("");
	$('#srcText,#trgText,#error').val('');
	$("#srcLocale,#trgLocale").empty();
	$("#srcLocale,#trgLocale,#srcText,#trgText,#transBtn").prop("disabled", true);
	var domain = $.trim($('#domain').val());
	if(domain == ""){
		$("#error").html(msg_validate_inpute_domain);
		return;
	}
	if(domain == "-1"){
 		return;
	}
 	$.ajax({
        type: 'POST',
        url: '../mtpLanguages/getLanguagePair',
        data: {
			"domain" : domain
		},
        dataType: 'json',
        success: function (data) {
           	if("success" == data.status) {
        		var locales = data.globalSightLocaleList;
    			if(locales != null && locales.length > 0){
    				$("#srcLocale,#trgLocale,#srcText,#trgText,#transBtn").prop( "disabled", false);
    			    var targetLocalesList = [];
    				for(i=0; i<locales.length; i++){
    					
    					var shortName = locales[i].language + "_" + locales[i].country;
    					//console.log("source"+shortName+'==='+checkLangExists(shortName));
    					var optionName = (checkLangExists(shortName) == -1) ?  locales[i].displayLanguage: removeLangShorName(locales[i].displayName);
	   					$("#srcLocale").append("<option value='"+shortName+"'>"+optionName+"</option>");
    					
    					//Automatically select source when only one language pair found
    					if(i == 0) {
    					   $("#srcLocale").val(shortName);
    					   //autopopulate target language parirs
    					   fnGetLocales(shortName,"trgLocale","source","");
    					}
    				}
      			}
    			else{
    				$("#error").append("No language pair found.");
    			}
         	}
        	if("error" == data.status){
                $("#error").html( "Problem while retrieving language domain");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	if(jqXHR.status == 0){
        		location.href = "../home/error.htm";
        	}else{
           		 var msg = "No Response.";
           		 $("#error").html(msg);
        	}
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
			Translate Text
			<p />
			<p />
			<p />
			<p />
		</div>
		 

		 <div id="tabs-1">
		  <table border="0" class="standardText" cellpadding="2">
		  	<tr>
				<td class="standardText" style="width:85px;">Domain</td>
				<td>
					<select id="domain" class="standardText" onChange="fnGetLanguagePair()" style="margin-bottom: 6px;">
					</select>
				</td>
			</tr>
			<tr>
				<td class="standardText" style="width:85px;">Source Locale</td>
				<td>
					<select id="srcLocale" class="standardText" onChange="fnGetLocales(this.value,'trgLocale','source','')">
					<!-- <option value="-1">&nbsp;</option> -->
					<%-- <c:forEach items="${allGlobalSightLocale}" var="locale">
						<option value="${locale}">${locale.displayName}</option>
					</c:forEach> --%>
					</select>
				</td>
			</tr>
			<tr>
				<td class="standardText">Target Locale</td>
				<td>
					<select id="trgLocale" class="standardText" onChange="fnSwapBtnEnableDisable();">
					<!-- <option value="-1">&nbsp;</option> -->
					<%-- <c:forEach items="${allGlobalSightLocale}" var="locale">
						<option value="${locale}">${locale.displayName}</option>
					</c:forEach> --%>
					</select>
					
					<input type="button" name="swap" id="swapBtn" class="btnColor" disabled value="Swap" onclick="fnSwap();">
				</td>
			</tr>
			<tr>
				<td class="standardText">Source Text</td>
				<td>
					<textarea id="srcText"  onkeyup="fnSrcTextChange()"></textarea>
				</td>
			</tr>
			<tr>
				<td class="standardText">Target Text</td>
				<td>
					<textarea id="trgText"></textarea>
					<input type="hidden" name="langCodes"  id="langCodes" value="${langCodes}" >
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td colspan="2">
					<input type="button" id="transBtn" value="Translate" class="btnColor" onclick="fnTranslate();">&nbsp;&nbsp;&nbsp;	
					<input type="button" id="feedbackBtn" value="Feedback" class="btnColor"  onClick="fnFeedbackModal();" disabled>
				</td>
			</tr>
		  </table>
		</div>
		<br>
		<%if(false) {%>
		<div class="note"><P>The user feedback functionality is enabled for the following translation directions:&nbsp;&nbsp;&nbsp; <span id="nonGeneralLangPairs"></span></P></div>
		<%} %>
		<div id="error" style="color:red">
		</div>
		
		<!--  Feedback Modal -->
		<%@ include file="/WEB-INF/pages/feedback.jspIncl" %>
 		
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
		
</DIV>

</body>
</html>