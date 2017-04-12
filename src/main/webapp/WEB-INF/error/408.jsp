<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
 <head>
  <title>401 Unauthorized</title>
 </head>
 <body>
  <%
  String requestURL = request.getHeader("Host").toString();
  String withoutProtocol = requestURL.replaceAll("(.*\\/{2})", "");
  String withoutPort = withoutProtocol.replaceAll("(:\\d*)", "") ;
  String domain = withoutPort.replaceAll("(\\/.*)", "");
  System.out.println(requestURL+"================"+withoutProtocol+"======="+withoutPort+"======"+domain); 
	if(domain.equalsIgnoreCase("localhost")) {
		response.sendRedirect("http://localhost:8080/dispatcherMW");
 
	}else{ 
		System.out.println( "redirect from 401 page" );  
		response.sendRedirect("http://mtdispatcher.deere.com/dispatcherMW");
	}
	%>
 </body>
</html>
