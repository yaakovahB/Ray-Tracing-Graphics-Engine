package unitTests.geometries;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import geometries.*;
import primitives.*;

import geometries.Intersectable.GeoPoint;

//testing(3/4/22)
/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class GeometriesTests {

	@Test
    public void testAdd() throws Exception {
        Plane plane = new Plane(new Point(0, 1, 0), new Point(2, 0, 0), new Point(0, 2, 0));
        Sphere sphere = new Sphere(new Point(0, 0, 1), 1);
        Geometries geometries;
        Geometries expected;
        // ============ Equivalence Partitions Tests ==============
        // add geometry list
        geometries = new Geometries(plane);
        expected = new Geometries(plane, sphere);
        geometries.add(sphere);
        assertEquals("add geometry failed", geometries.getGeometries(), expected.getGeometries());

        // =============== Boundary Values Tests ==================
        //add empty list to geometries
        geometries = new Geometries(plane, sphere);
        geometries.add();
        assertEquals("empty list failed", geometries.getGeometries(), geometries.getGeometries());

        // add geometries to empty geometries
        geometries = new Geometries();
        expected = new Geometries(plane, sphere);
        geometries.add(plane, sphere);
        assertEquals("add to empty failed", geometries.getGeometries(), expected.getGeometries());
    }

	
	@Test
	public void testfindIntersections() throws Exception {
		List<GeoPoint> result = new ArrayList<GeoPoint>();
		Geometries intersectables = new Geometries();
		
		// =============== Boundary Values Tests ==================
		//an empty collection (BVA)
		Ray ray = new Ray(new Point(20, 20, 20), new Vector(0, 0, 2));
		result = intersectables.findGeoIntersections(ray);
		System.out.println(" no intersections - empty scene");
		assertNull("There should be no intersection - the scene is empty", result);
		
		//no ray intersects with a geometry (BVA)	
		 Plane plane = new Plane(new Point(0, 1, 0), new Point(2, 0, 0), new Point(0, 2, 0));
	     Sphere sphere = new Sphere(new Point(0, -2, 3), 1);
	     intersectables = new Geometries(sphere, plane);
	     result = intersectables.findGeoIntersections(ray);
	     System.out.println(" no intersections ");
		 assertNull("There should be no intersection", result);
		 
		//only one shape intersects (BVA)
		Ray ray1 = new Ray(new Point(0, 2, 1), new Vector(0, 0, -1));
		result = intersectables.findGeoIntersections(ray1); 
		 System.out.println("the size of the array should be 1 - " + result.size());
		 assertEquals("there should only be one intersection.", result.size(), 1);
		
		//all shapes intersects (BVA)
		 Ray ray3 = new Ray(new Point(0, -2, -1), new Vector(0, 0, 1));
		result = intersectables.findGeoIntersections(ray3);
		System.out.println("the size of the array should be 3 - " + result.size());
		assertEquals("there should be three intersections. two with sphere and one with plane", result.size(), 3);
		
		 // ============ Equivalence Partitions Tests ==============
          //some shapes but not all intersects (EP)
		 result = intersectables.findGeoIntersections(ray1);
		 System.out.println("the size of the array should be 0 < : " + result.size() + " < 3");
		 assertEquals("there should only be one intersection", result.size(), 1);
		
	}
	
}
