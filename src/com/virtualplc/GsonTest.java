package com.virtualplc;

import com.google.gson.JsonObject;

public class GsonTest {
	public static void main(String[] args) {
		JsonObject json = new JsonObject();
		json.addProperty("name", "VirtualPLC");
		json.addProperty("version", "1.0");
		System.out.println(json.toString()); // {"name":"VirtualPLC","version":"1.0"}
	}
}