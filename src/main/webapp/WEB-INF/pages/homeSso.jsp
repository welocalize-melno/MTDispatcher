<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Dispatcher Main Page</title>
<link rel="shortcut icon" href="./images/favicon_globalsight.PNG"/>
<link rel="stylesheet" href="./resources/css/style.css" />
<link rel="stylesheet" href="./resources/jquery/jQueryUI.redmond.css" />
<!--[if lt IE 9]>
<script src="../resources/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="./resources/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="./resources/jquery/jquery-ui-1.8.18.custom.min.js"></script>
</head>
<body>
<!-- Form Element -->
<FORM NAME="dForm" METHOD="POST" ACTION="">
<input type="hidden" name="tmProfileID">
</FORM>

	<DIV>
		<%@ include file="/WEB-INF/pages/headerSso.jspIncl" %>

		<div id="content" style="height:150px;">
			<p/>
			Welcome to Dispatcher, <c:out value="${theAccount.fullName}"/>. <p/>
			Dispatcher will allow you to translate text with Machine Translation engines. <p/>
			<c:if test = "${theAccount.type == gAdministrator}">
  			 On the <b>MT Profile</b> page, indicate the name of the MT Engine profile, URL and account options for all MT engines that will be used.<p/>
			 On the <b>Accounts</b> page, indicate account name, description and security code for all Accounts that will be used.<p/>
			 On the <b>Languages</b> page, create language info and associate them with MT Profile and Accounts.<p/>
			</c:if>
			On the <b>Translate</b> page, translate text in the desired language combinations.<p/>
			<p/>
		</div>
		<div id="error" style="color:red">
		</div>
		<%@ include file="/WEB-INF/pages/footer.jspIncl" %>
	</DIV>
</body>
</html>