import primitives.*;
import static java.lang.System.out;
import static primitives.Util.*;
import geometries.*;
import unitTests.*;
//trying to sort pull/push/merge issues
import unitTests.primitives.*;
import unitTests.geometries.*;
import unitTests.renderer.*;
import unitTests.lighting.*;
/*
 * Submitters:

 * Meira Grafstein meiragrafstein@gmail.com 328775911
 * Tali Cohen talcohen@g.jct.ac.il 329651871
 * Yaakovah Bacharach yakovahb@gmail.com 340912518
 */
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {
	public static void main(String[] args) {
	      Result result = JUnitCore.runClasses(EnhancementTests.class);
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
		
	      System.out.println("successful? " + result.wasSuccessful());
	   }
}		
		

