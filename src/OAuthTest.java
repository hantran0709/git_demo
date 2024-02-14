import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		String[] coursesTitle = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		System.setProperty("webdriver.chrome.driver", "F:\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		
		OAuthTest obj = new OAuthTest();
		// Get access token
		String code = obj.getCode(driver);
		
		// Get response
//		given().queryParam("access_token", obj.getAccessToken(code))
//		.when().get("https://rahulshettyacademy.com/getCourse.php").then().assertThat().log().all();
		GetCourse response = given().queryParam("access_token", obj.getAccessToken(code)).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
		
		
		System.out.println(response.getLinkedIn());
		System.out.println(response.getInstructor());

		List<Api> apiCourses = response.getCourse().getApi();
		
		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
				response.getCourse().getMobile().get(i).getCourseTitle();
			}
		}
		
		// print all course title of webAutomation
		List<WebAutomation> webList = response.getCourse().getWebAutomation();
		ArrayList<String> webCourseTitle = new ArrayList<String>();
		for (int i = 0; i < webList.size(); i++) {
			webCourseTitle.add(webList.get(i).getCourseTitle());
		}
		
		List<String> expectedList = Arrays.asList(coursesTitle);
		
		Assert.assertTrue(webCourseTitle.equals(expectedList));
		
//		System.out.println(response);
		
	}
	
	public String getAccessToken(String code) {
		String accesTokenResponse = given().log().all().urlEncodingEnabled(false)
		.queryParam("code", code)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParam("grant_type", "authorization_code")
		.when()
		.post("https://www.googleapis.com/oauth2/v4/token")
		.asString();
		
		JsonPath js = new JsonPath(accesTokenResponse);
		String accessToken = js.getString("access_token");
		return accessToken;
	}
	
	public String getCode(WebDriver driver) throws InterruptedException {
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.id("identifierId")).sendKeys("giahantr79");
//		driver.findElement(By.id("identifierId")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
//		driver.findElement(By.name("Passwd")).sendKeys("07091999Han");
//		driver.findElement(By.name("Passwd")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
		String currentURL = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0Adeu5BWTw-LsZz8ZOQpqJXIYogtrD073SVa6N26hFN1InmF4JvCL8y5ngSxjMyJ5ONwwDQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String particialCode = currentURL.split("code=")[1];
		String code = particialCode.split("&scope")[0];
		return code;
	}

}
