package caris.framework.main;

import java.io.IOException;

import com.mashape.unirest.http.exceptions.UnirestException;

import caris.framework.utilities.TheBlueAllianceAPI;

public class TestAPI {
	
	public static void main(String[] args) throws UnirestException, IOException {
		TheBlueAllianceAPI api = new TheBlueAllianceAPI();
		System.out.println(api.getJSON("event/2016casj/oprs"));
	}
	
}
