/**
 * 
 */
package unitTests.geometries;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import geometries.Polygon;
import geometries.Tube;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class TubeTests {
	
	/**
	 * Test method for {@link geometries.Tube#Tube(...)}.
	 */
	@Test
	public void testConstructor() throws Exception {
	
		// ============ Equivalence Partitions Tests ==============
		//radius: does not equal 0
		Tube t = new Tube(new Ray(new Point(0,0,0), new Vector(1,1,1)), 1);
		assertNotEquals(t.getRadius(), 0, "bad radius for tube");
	}

	/**
	 * Test method for {@link geometries.Tube#getNormal(Point)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetNormal() throws Exception {
		
		// =============== Boundary Values Tests ==================
		// connection between the point on the body and the ray’s head creates a 90 degrees with the ray

		Tube t = new Tube(new Ray(new Point(0,0,0), new Vector(1,1,1)), 1);
		
		Vector v = new Vector(-1/Math.sqrt(2),-1/Math.sqrt(2),0);
		
		assert(v.equals(t.getNormal(new Point(0,0,1))));
	
	}

}

