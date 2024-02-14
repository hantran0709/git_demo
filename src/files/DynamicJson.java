package files;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		// Add book with API
		String response = given().log().all().header("Content-Type", "application/json")
		.body(PayLoad.addBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
		System.out.println(js.getString("Msg"));
		System.out.println(js.getString("ID"));
//		Assert.assertEquals(js.getString("Msg"), "successfully added");
	}
	
	@Test(dataProvider = "BooksData")
	public void deleteBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		// Delete book by API
		String response = given().log().all().header("Content-Type", "application/json")
		.body(PayLoad.deleteBook(isbn, aisle))
		.when().post("/Library/DeleteBook.php")
		.then().assertThat().log().all().extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
		Assert.assertEquals(js.getString("msg"), "book is successfully deleted");
	}
	
	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		// array - a collection of element
		// multidimensional - collection of arrays
		return new Object[][] {{"wefs", "1232"}, {"qsfx", "2445"}, {"vbgh", "8745"}};
	}

	
}
