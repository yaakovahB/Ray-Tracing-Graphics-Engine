package unitTests.geometries;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.List;
import geometries.Sphere;
import primitives.*;

import geometries.Intersectable.GeoPoint;

import geometries.Intersectable.GeoPoint;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class SphereTests {

	/**
	 * Test method for {@link geometries.Sphere#getNormal(Point)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetNormal(){
		
		// ============ Equivalence Partitions Test ==============
		try {
		Sphere s = new Sphere(new Point(0,0,0), 6);
		assertEquals("isn't returning normal", s.getNormal(new Point(2,4,4)), new Vector(1, 2, 2).normalize());
		}catch(Exception e) {System.out.print(e);};
		 
		
	}
	
	/**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
	 * @throws Exception 
     */
    @Test
    public void testFindIntersections() throws Exception {
    	
        
        Sphere sphere = new Sphere(new Point(1, 0, 0), 1d);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull("Ray's line out of sphere",
                            sphere.findGeoIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))));

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<GeoPoint> result = sphere.findGeoIntersectionsHelper(new Ray(new Point(-1, 0, 0),new Vector(3, 1, 0)));
        assertEquals("Wrong number of points", 2, result.size());
        /*if (result.get(0).getX() > result.get(1).getX()){
            result = List.of(result.get(1), result.get(0));
        assertEquals("Ray crosses sphere1", List.of(p1, p2), result);
        }*/
        
        // TC03: Ray starts inside the sphere (1 point)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, .5), new Vector(0, 0, 2)));
        assertEquals("wrong number of intersection points. Ray starts inside the sphere", result.size(),1);

        
        // TC04: Ray starts after the sphere (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 3, 4), new Vector(0, 0, 2)));
        assertNull("There should be no intersections. Ray starts after the sphere", result);

        
        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0 ,1), new Vector(1, 0, -2 )));
        assertEquals("There should be one intersection. Ray starts at sphere and goes inside", result.size(), 1);
        
        
        // TC12: Ray starts at sphere and goes outside (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, 1), new Vector(-1, 0, 1)));
        assertNull("There should be no intersections. Ray starts at sphere and goes outside", result);
        
        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, 3), new Vector(0, 0, -6)));
        assertEquals("There should be 2 intersections. Ray starts before the sphere", result.size(), 2);
        
        // TC14: Ray starts at sphere and goes inside (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point(2, 0 ,0), new Vector(-1, 0, 0 )));
        assertEquals("There should be one intersection. Ray starts at sphere and goes inside", result.size(), 1);
        
        // TC15: Ray starts inside (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, .5), new Vector(0, 0, -2.5)));
        assertEquals("There should be one intersection. Ray starts inside", result.size(), 1);

        // TC16: Ray starts at the center (1 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, 0), new Vector(5, 5, 5)));
        assertEquals("There should be one intersection.Ray starts at the center", result.size(), 1);
        
        // TC17: Ray starts at sphere and goes outside (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0 ,1), new Vector(0, 0, 10 )));
        assertNull("There should be no intersections. Ray starts at sphere and goes outside", result);    
        
        // TC18: Ray starts after sphere (0 points)
        result = sphere.findGeoIntersections(new Ray(new Point(1, 0, 10), new Vector(0, 0, 10)));
        assertNull("There should be no intersections. Ray starts after sphere", result);

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point(2, 0, -1), new Vector(0, 0, 1)));
        assertNull("There should be no intersections. Ray starts before the tangent point", result);

        // TC20: Ray starts at the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, 1)));
        assertNull("There should be no intersections. Ray starts before the tangent point", result);

        // TC21: Ray starts after the tangent point
        result = sphere.findGeoIntersections(new Ray(new Point(2, 0, 3), new Vector(0, 0, 1)));
        assertNull("There should be no intersections. Ray starts after the tangent point", result);


        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        result = sphere.findGeoIntersections(new Ray(new Point(5, 0, 0), new Vector(0, 0, 1)));
        assertNull("Ray orthogonal to sphere's diameter should not intersect", result);

    }


}

