

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.UnknownServiceException;
import java.util.HashMap;


public class APITests {
	
	
	@Test
	private void GetListResourceTest() throws IOException
	{
		
		String baseUri = Env.GetEnvironment();
		RestAssured.baseURI=baseUri+APIPaths.GetListUsersURI;
				
		Response response = given().header("Content-Type","application/json").get();
		response.then().statusCode(200);
		ValidateGetResponse(1,"cerulean",2000,"#98B2D1","15-4020",response);
		ValidateGetResponse(3,"true red",2002,"#BF1932","19-1664",response);
	}
	
	private void ValidateGetResponse(int id, String name, int year, String color, String pantone_value, Response response)
	{
		JSONObject obj = new JSONObject(response.getBody().asString());
		JSONArray dataArray = obj.getJSONArray("data");
		for(int i=0;i<dataArray.length();i++)
		{
			JSONObject innerData = dataArray.getJSONObject(i);
			if(id==(i+1))
			{
				Assert.assertEquals(innerData.get("name"),name);
				Assert.assertEquals(innerData.get("year"),year);
				Assert.assertEquals(innerData.get("color"),color);
				Assert.assertEquals(innerData.get("pantone_value"),pantone_value);
				System.out.println("Test Log: Get List Resource:");
				System.out.println("Verified Get List response for Id|"+id+"| name:"+name+"|year:"+year
						+"|color:"+color+"|pantone_value:"+pantone_value);
			}
		}		
	}
	
	@Test
	private void PostRegisterSuccessful() throws IOException, ParseException
	{
		Response response = PostTest("Register");
		response.then().statusCode(200);	
		JSONObject responseObject = new JSONObject(response.getBody().asString());
		Assert.assertTrue(responseObject.has("id"));
		Assert.assertTrue(responseObject.has("token"));
		Assert.assertTrue((Integer)responseObject.get("id")==4);
		Assert.assertEquals(responseObject.get("token"),"QpwL5tke4Pnpja7X4");
		System.out.println("Test Log: Post successful Registration:");
		System.out.println("Post Registration response has Id: "+(Integer)responseObject.get("id")
		+" and Token: "+responseObject.get("token"));
	}	
	
	@Test
	private void PostLoginSuccessful() throws IOException, ParseException
	{
		Response response = PostTest("Login");
		response.then().statusCode(200);	
		JSONObject responseObject = new JSONObject(response.getBody().asString());
		Assert.assertTrue(responseObject.has("token"));
		Assert.assertEquals(responseObject.get("token"),"QpwL5tke4Pnpja7X4");
		System.out.println("Test Log: Post successful Login:");
		System.out.println("Post Login response has Token: "+responseObject.get("token"));
	}	
	
	private Response PostTest(String scenario) throws IOException, ParseException
	{
		FileReader reader = new FileReader(".\\DataAndConfig\\RegistrationData.json");
		JSONParser parser = new JSONParser();
		org.json.simple.JSONObject requestObj = (org.json.simple.JSONObject)parser.parse(reader);
		String baseUri = Env.GetEnvironment();
		String pathURL;
		if(scenario.equals("Login"))
		{
			pathURL=APIPaths.PostLoginURI;
		}
		else if (scenario.equals("Register"))
		{
			pathURL=APIPaths.PostRegisterURI;
		}
		else {pathURL="";System.out.println("Invalid scenario name");}
		RestAssured.baseURI=baseUri+pathURL;
		org.json.simple.JSONObject postRequest = (org.json.simple.JSONObject)parser.parse(requestObj.get(scenario).toString());
		Response response = given().header("Content-Type","application/json")
				.body(postRequest.toJSONString()).post();
		return response;		
	}
	
}
