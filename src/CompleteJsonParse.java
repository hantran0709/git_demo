import files.PayLoad;
import io.restassured.path.json.JsonPath;

public class CompleteJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonPath js = new JsonPath(PayLoad.coursePrice());
		
		// Print No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		// Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		// Print Title of the first course
		String titleFirstCourse = js.getString("courses[0].title");
		System.out.println(titleFirstCourse);
		// Print All course titles and their respective Prices
		for (int i = 0; i < count; i++) {
			String title = js.getString("courses[" + i + "].title");
			int price = js.getInt("courses[" + i + "].price");
			System.out.println("Title: " + title);
			System.out.println("Price: " + price);
		}
		// Print no of copies sold by RPA Course
		for (int i = 0; i < count; i++) {
			if (js.getString("courses[" + i + "].title").equalsIgnoreCase("RPA")) {
				System.out.println("No of copies sold by RPA Course: " + js.getInt("courses[" + i + "].copies")); 
				break;
			}
		}
		// Verify if Sum of all Course prices matches with Purchase Amount
		
	
	}

}
