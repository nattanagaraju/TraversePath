<!DOCTYPE html>
<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.JsonParser"%>
<%@ page import="java.util.*" %>
<html>
<head>
<title>TPathWeb</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/view.css">
<script type="text/javascript">
		function confirmInput(){
			document.getElementById("result").innerHTML = "Processing..";
			return true;
		}
	</script>
</head>
<body>
	<div id="main">
		<h1>Search Users here</h1>
		<form id="dbform"
			action="${pageContext.request.contextPath}/tweb/getUserDetails"
			onsubmit="{return confirmInput()}">
			<input type="text" name="email" value="${email}" placeholder="Entery email Here" style="width:250px;height:24px;"/>
			<br /> <input type="submit" value="Get Through Web Service"/>
		</form>
	</div>
	<div>
			<%
				String resp = (String)request.getAttribute("response");
				if(resp != null){
					JsonParser parser = new JsonParser();
					JsonObject parent = parser.parse(resp).getAsJsonObject();
					String tpath = parent.get("tpath").getAsString();
					JsonObject responseresult = parent.get("responseresult").getAsJsonObject();
					String message = responseresult.get("message").getAsString();
					Iterator<JsonElement> columnsIt = responseresult.get("columnList").getAsJsonArray().iterator();
					Iterator<JsonElement> resultIt = responseresult.get("result").getAsJsonArray().iterator();
					int i = 0;
					Map<Integer, String> clsMap = new HashMap<Integer, String>();
					boolean header = false;
					boolean found = false;
			%>
			Result : <span id="result"> <%= message %></span>
			<div>
			<br/> <h3>Users Details</h3>
					 <table id="ql_rest" cellspacing="0"> 
					 <tr> 
					 <%
						while(columnsIt.hasNext()){
							String columnName = columnsIt.next().getAsString();
							clsMap.put(i, columnName);
							i++;
							%> <th><%= columnName %></th> <%
						}
					%> 
					</tr> 
					<%
						int size = clsMap.size();
						while(resultIt.hasNext()){
							found = true;
						JsonObject rowObj = resultIt.next().getAsJsonObject();
					%>
						<tr>
							<%for(i=0; i<size; i++){ %>
								<td><%= rowObj.get(clsMap.get(i)).getAsString() %></td>
							<%} %>
						</tr>
					<% } 
						if(!found){
					%>
							<tr><td colspan="<%= size %>">No Data Found</td></tr>
					<% 
						}
					%> 
						</table> 
				<br/>
				 <% } %>
			</div>
		
	</div>
</body>
</html>