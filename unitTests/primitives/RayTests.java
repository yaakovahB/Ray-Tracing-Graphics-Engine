package unitTests.primitives;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


import java.util.ArrayList;
import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class RayTests {

	/**
	 * Test method for {@link primitives.Ray#findClosestPoint(java.util.List)}.
	 * @throws Exception 
	 */
	@Test
	public void testFindClosestPoint() throws Exception {
		
		Ray ray = new Ray(new Point(1, 1, 1), new Vector(0, 0, 1));
		List<Point> list = new ArrayList<Point>();

		// =============== Boundary Values Tests ==================
		
		//BV01: an empty list
		 assertNull("There should be no point because list was empty", ray.findClosestPoint(list));
	    
		//BV02: a list where the closest point is the first point in the list
		 list.add(new Point(0,0,1));
		 list.add(new Point(0,0,2));
		 assertEquals("The closest point should be the first in the list", list.get(0), ray.findClosestPoint(list));
		 
		//BV03: a list where the closest point is the last point in the list
		 list.add(new Point(0,0.5,1));
		 assertEquals("The closest point should be the last in the list", list.get(list.size()-1), ray.findClosestPoint(list));
	
		// ============ Equivalence Partitions Tests ==============
		// EP01: the closest point to the ray�s head is found somewhere in the middle of the list
		 list.add(new Point(0,0,5));
		 assertEquals("The closest point should be in the middle of the list", list.get(2), ray.findClosestPoint(list));
	
	}

}
