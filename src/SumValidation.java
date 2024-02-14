import org.testng.Assert;
import org.testng.annotations.Test;

import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void sumOfCourse() {
		JsonPath js = new JsonPath(PayLoad.coursePrice());
		int count = js.getInt("courses.size()");
		int sum = 0;
		for (int i = 0; i < count; i++) {
			int price = js.getInt("courses[" + i + "].price");
			int copies = js.getInt("courses[" + i + "].copies");
			sum = sum + price*copies;
		}
		System.out.println(sum);
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sum, purchaseAmount);
	}

}
