import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

public class ECommerceAPITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("tghan@mail.com");
		loginRequest.setUserPassword("Aespa1711?");

		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.setContentType(ContentType.JSON).build();

		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login").then().log().all().extract()
				.response().as(LoginResponse.class);

		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
		System.out.println(loginResponse.getToken());
		System.out.println(loginResponse.getUserId());

		// Add product
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("authorization", token).build();
		RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseReq).param("productName", "Shirt")
				.param("productAddedBy", userId).param("productCategory", "fashion")
				.param("productSubCategory", "shirts").param("productPrice", "11500")
				.param("productDescription", "Addias Originals").param("productFor", "women")
				.multiPart("productImage", new File("F:\\Screenshot 2023-08-19 213909.png"));

		String AddProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product").then().log().all()
				.extract().response().asString();

		JsonPath js = new JsonPath(AddProductResponse);
		String productId = js.get("productId");
		System.out.println(productId);

		// Create order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
				.addHeader("authorization", token).setContentType(ContentType.JSON).build();

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderId(productId);

		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		orderDetailList.add(orderDetail);
		Orders orders = new Orders();
		orders.setOrders(orderDetailList);

		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);
		String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all()
				.extract().response().asString();
		System.out.println(responseAddOrder);

		// Delete product
		RequestSpecification deleteProductBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com/").addHeader("authorization", token).build();
		RequestSpecification deleteProductReq = given().log().all().spec(deleteProductBaseReq).pathParam("productId",
				productId);
		String responseDeleteProduct = deleteProductReq.when().delete("/api/ecom/product/delete-product/{productId}")
				.then().log().all().extract().response().asString();
		JsonPath jsonResponseDelete = new JsonPath(responseDeleteProduct);
		Assert.assertEquals("Product deleted successfully", jsonResponseDelete.get("message"));
	}

}
