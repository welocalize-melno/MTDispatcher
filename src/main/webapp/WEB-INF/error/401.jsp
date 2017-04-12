<%--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
 <head>
  <title>401 Unauthorized</title>
  <style type="text/css">
    <!--
    BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;font-size:12px;}
    H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;}
    PRE, TT {border: 1px dotted #525D76}
    A {color : black;}A.name {color : black;}
    -->
  </style>
  
 </head>
 <body>
  <%
  String requestURL = request.getHeader("Host").toString();
  String withoutProtocol = requestURL.replaceAll("(.*\\/{2})", "");
  String withoutPort = withoutProtocol.replaceAll("(:\\d*)", "") ;
  String domain = withoutPort.replaceAll("(\\/.*)", "");
   
	if(domain.equalsIgnoreCase("mtdispatcher.deere.com")) {
		 System.out.println( "redirect from 401 page" );  
		response.sendRedirect("http://mtdispatcher.deere.com/dispatcherMW");
	}else{ 
	%>
   <h1>401 Unauthorized</h1>
   <p>
    You are not authorized to view this page. Click <a href="../../">here</a> to Home page.
   </p>
   <% }
	%>
 </body>

</html>
