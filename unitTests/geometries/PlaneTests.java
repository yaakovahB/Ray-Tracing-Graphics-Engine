package unitTests.geometries;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.util.List;

import geometries.Plane;
import primitives.*;

import geometries.Intersectable.GeoPoint;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class PlaneTests {
    static final double delta = 0.0000001;

	/**
	 * Test method for {@link geometries.Plane#getNormal(java.awt.Point)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetNormalPoint(){

		try {
			Plane p = new Plane(new Point(1,1,1), new Vector(5,5,5));
			assertEquals("should be equal", p.getNormal(new Point(1,2,3)).length(), 1, delta);
		} catch(Exception e) {}
		
	}

	/**
	 * Test method for {@link geometries.Plane#getNormal(java.awt.Point, java.awt.Point, java.awt.Point)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetNormalPointPointPoint() {
		
		// ============ Equivalence Partitions Tests ==============
		
		//Check that the plane's vector is normalized (is of length 1)
		
		try {
			Plane p = new Plane(new Point(1,1,1), new Point(2,2,2), new Point(3,3,3));
			assertEquals(p.getNormal(new Point(1,2,3)).length(),1, delta);
		} catch(Exception e) {}
	}
	

	/**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
	 * @throws Exception 
     */
    @Test 
    public void testFindIntersections() throws Exception {
    	
    	GeoPoint predictedPoint;
        List<GeoPoint> result;
        Ray ray;
        Plane plane = new Plane(new Point(0, 0, 10), new Point(10, 0, 0), new Point(0, 10, 0));
       
        
        // ============ Equivalence Partitions Tests ==============
    	
    	// TC01: Ray intersects the plane (The Ray must be neither orthogonal nor parallel to the plane)
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        predictedPoint = new GeoPoint(plane, new Point(0, 0, 10));
        result = plane.findGeoIntersections(ray);
        assertEquals("Did not find the correct intersection point", predictedPoint, result.get(0));
        
    	//TC02: Ray does not intersect the plane (The Ray must be neither orthogonal nor parallel to the plane )
        ray = new Ray(new Point(0, 0, 11), new Vector(0, 0, 1));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection." , result);

        // =============== Boundary Values Tests ==================
        
        
        //*** Ray is parallel to the plane - Two cases: 
        
        //TC10 the ray included in the plane
        ray = new Ray(new Point(0, 0, 10), new Vector(10, 0, -10));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection because the ray is inside the plane", result);
        
		// TC11 the ray is not included in the plane
        ray = new Ray(new Point(10, 0, 10), new Vector(10, 0, -10));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection because the ray is parallel to the plane", result);
        
		//*** Ray is orthogonal to the plane - Three cases:
        
		//TC20  P0 before the plane
        
        ray = new Ray(new Point(-1,-1,-1), new Vector(-5, -5, -5));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection", result);
         
		//TC21  P0 in the plane
        ray = new Ray(new Point(0, 0, 10), new Vector(-5, -5, -5));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection.", result);

		//TC22  P0 after the plane :)
        ray = new Ray(new Point(0, 0, 11), new Vector(5, 5, 5));
        result = plane.findGeoIntersections(ray);
        assertNull("There should not be an intersection. Ray starts after the plane", result);

	
    }

	
}

