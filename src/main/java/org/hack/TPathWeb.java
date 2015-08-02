package org.hack;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.hack.util.AppProperties;
import org.tlog.TLogger;
import org.tlog.TPath;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.view.Viewable;

@Path("/tweb")
public class TPathWeb {
	@Context
	HttpServletRequest request;
	
	@GET 
	@Produces(MediaType.TEXT_HTML)
	public Viewable getPage() {
		TLogger.logInfo("TPathWeb", "getPage", "Entry & Exit");
		return new Viewable("/view.jsp");
	}

	@GET 
	@Path("/getUserDetails")
	@Produces(MediaType.TEXT_HTML)
	public Viewable getUserDetails(@QueryParam("email") String email) {
		TLogger.logInfo("TPathWeb", "getUserDetails", "Entry");
		TPath.startTPath("TraversePathWeb");
		String response = null;
		String statusMsg = "SUCCESS";
		String statusCode = "0000";
		try{
			response = getSvcResp(email);
		}catch(Exception e){
			e.printStackTrace();
			TLogger.logError("TPathWeb", "getUserDetails",  e.getMessage(), e);
			statusMsg = "FAILURE: "+e.getMessage();
			statusCode = "0006";
		}
		if(response != null){
			JsonParser parser = new JsonParser();
			JsonObject jsonobj = parser.parse(response).getAsJsonObject();
			TPath.addTPath(jsonobj.get("tpath").getAsString(), "TraversePathWeb");
		}
		request.setAttribute("email", email);
		request.setAttribute("response", response);
		TLogger.logInfo("TPathWeb", "getUserDetails", "Response: "+response);
		TPath.endTPath("TraversePathWeb", "TPathWeb", "getUserDetails", statusMsg, statusCode);
		TPath.execTPathReport("TraversePathWeb", null);
		TLogger.logInfo("TPathWeb", "getUserDetails", "Exit");
		return new Viewable("/view.jsp");
		
	}

	private String getSvcResp(String email)throws Exception {
		TLogger.logInfo("TPathWeb", "getSvcResp", "Entry");
		StringBuilder output = new StringBuilder("");
		String urlStr = AppProperties.getProp("getUser");
		urlStr = urlStr + "?email=" + email;
		TLogger.logInfo("TPathWeb", "getSvcResp", "URL Invoking: "+urlStr);
		
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.addRequestProperty("request-id", (String)TPath.getThreadVal().get("request-id"));
		TLogger.logInfo("TPathWeb", "getSvcResp", "Adding request-id to header: "+(String)TPath.getThreadVal().get("request-id"));
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String outputStr = null;
		while ((outputStr = br.readLine()) != null) {
			output.append(outputStr);
		}
		TLogger.logInfo("TPathWeb", "getSvcResp", "Output from Server: "+output.toString());
		conn.disconnect();
		TLogger.logInfo("TPathWeb", "getSvcResp", "Exit");
		return output.toString();
	}
	
}
