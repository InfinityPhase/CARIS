package caris.framework.utilities;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import caris.framework.library.Constants;

public class TheBlueAllianceAPI {
	
	public static JSONObject getJSON(String query) {
		GetRequest request = Unirest.get(Constants.TBA_ENDPOINT + query);
		HttpResponse<JsonNode> jsonNode;
		try {
			jsonNode = request.asJson();
		} catch (UnirestException e) {
			Logger.error("Failed to reach TBA endpoint");
			return null;
		}
		JSONObject jsonObject = jsonNode.getBody().getObject();
		return jsonObject;
	}
	
}
