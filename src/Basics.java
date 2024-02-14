import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import files.PayLoad;
import files.ReusableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// validate if Add place is working as expected
		// Add place -> Update place with new address -> Get place to validate if new address is present in response
		
		// given - all input details 
		// when - Submit the API - resource, http method
		// then - validate the response
		// content of the file to String -> content of file can convert to Byte -> Byte data to String
		
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(new String(Files.readAllBytes(Paths.get("F:\\addPlace.json")))).when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = ReusableMethods.rawToJson(response);
		
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		// Update place
		String newAddress = "Summer Walk, USA";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "  \"place_id\": \"" + placeId + "\",\r\n"
				+ "  \"address\": \"" + newAddress + "\",\r\n"
				+ "  \"key\": \"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// Get place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		JsonPath jsForGet = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = jsForGet.getString("address");
		System.out.println(actualAddress);
		
	}

}
