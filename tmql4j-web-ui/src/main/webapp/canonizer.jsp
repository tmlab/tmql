<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page
	import="de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException"%>
<%@page import="de.topicmapslab.tmql4j.components.parser.IParserTree"%>
<%@page import="de.topicmapslab.tmql4j.exception.TMQLRuntimeException"%>
<%@page
	import="de.topicmapslab.tmql4j.path.components.processor.runtime.TmqlRuntime2007"%>
<%@page
	import="de.topicmapslab.tmql4j.components.processor.runtime.TMQLRuntimeFactory"%>
<%@page
	import="de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/favicon.ico" rel="shortcut icon" />
<link
	href="<%=request.getServletContext().getContextPath()%>/resources/css/screen.css"
	media="screen, projection" rel="stylesheet" type="text/css" />
<link
	href="<%=request.getServletContext().getContextPath()%>/resources/css/print.css"
	media="print" rel="stylesheet" type="text/css" />
<link
	href="<%=request.getServletContext().getContextPath()%>/resources/css/maiana.css"
	media="screen, projection" rel="stylesheet" type="text/css" />
<title>Tmql4J Canonizer</title>
<script type="text/javascript">
	
</script>
</head>
<body class="maintenance">
<div id="wrapper">
<div id="header">
<div class="container">
<div class="span-20 prepend-2 append-2 last"><img
	src="<%=request.getServletContext().getContextPath()%>/resources/images/logo.png"
	style="position: absolute; left: 5px; top: 5px;" />
<div id="logo"></div>
</div>
</div>
</div>
<div id="content">&nbsp; <%
 	String query = "// tm:subject";
 	String newQuery = query;
 	boolean isPost = request.getMethod().equalsIgnoreCase("POST");
 	String canonized = "";
 	String tree_ = "";
 	String error = null;
 	if (isPost) {
 		query = request.getParameter("query");
 		ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory().newRuntime(TmqlRuntime2007.TMQL_2007);
 		try {
 			IParserTree tree = runtime.parse(query);
 			canonized = tree.toQueryString();
 			StringBuilder stringTree = new StringBuilder();
 			tree.toStringTree(stringTree);
 			tree_ = stringTree.toString();

 		} catch (Exception e) {
 			Throwable realCause = e;
 			while (realCause.getCause() != null) {
 				realCause = realCause.getCause();
 			}
 			if (realCause instanceof TMQLInvalidSyntaxException) {
 				error = realCause.getMessage().replaceAll("\r\n", "<br />");
 				TMQLInvalidSyntaxException ex = (TMQLInvalidSyntaxException) realCause;
 				StringBuilder builder = new StringBuilder();
 				StringBuilder regexp = new StringBuilder(); 				
 				boolean first = true;
 				for (String token : ex.getTokens()) {
 					if (!first) {
 						builder.append(" ");
 						regexp.append("\\s*");
 					}
 					builder.append(token);
 					regexp.append("\\Q" + Matcher.quoteReplacement(token) +"\\E"); 					
 					first = false;
 				}
 				String part = builder.toString();
 				Matcher m = Pattern.compile(regexp.toString()).matcher(query);
 				newQuery = m.replaceFirst("<font style=\"color:#F00; font-weight: bold;\">" + part + "</font>"); 				
 			}
 		}
 	}
 %>
 <br />
 This service uses the TMQL draft of 2008 and the tmql4j query engine version 3.1.0.
 <hr />
<form action="canonizer.jsp" method="POST"><textarea rows="10"
	cols="100" id="query" name="query"><%=query%></textarea><br />
<input type="submit" value="Canonize"></form>
<br />
<%
	if (isPost) {
		if (error == null) {
%> <b>Canonized Query: </b>
<div><%=canonized%></div>
<br />
<b>Parser Tree:</b><br />
<textarea rows="100" cols="1000" style="width: 1000px; height: 500px;"><%=tree_%></textarea>
<%
	} else {
%> <b>Error:</b>
<div>The given query is invalid: <%=newQuery%></div>
<br />
<b>Cause:</b>
<div><%=error%></div>
<%
	}
	}
%>
</div>
<div id="footer">
<div class="container">
<div class="span-20 prepend-2">
<ul class="horizontal">
	<li class="noBullet"><a href="http://tmql4j.topicmapslab.de">Documentation</a></li>
	<li class="noBullet"><a href="http://topicmapslab.de/impressum">Imprint</a></li>
	<li>Tmql4J-WebUI is a service by the <a
		href="http://www.topicmapslab.de">Topic Maps Lab</a> &copy; 2011</li>
</ul>
</div>
</div>
</div>
</div>
</body>
</html>