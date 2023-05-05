package unitTests.renderer;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import java.util.List;

import geometries.*;
import primitives.*;
import renderer.Camera;

import geometries.Intersectable.GeoPoint;

/**
* @author Yaakovah, Meira, Tali
*
*/
public class CameraIntegrationTests {
	
	/**
	 * @brief set up the camera
	 * @param cam = the camera
	 * @param dist = the distance
	 * @param width
	 * @param height
	 */
	public void cameraSetUp (Camera cam, double dist, double width, double height) {
		cam.setVPDistance(dist);
		cam.setVPSize(width, height);
	}
	
	/**
	 * @param cam = the camera
	 * @param geometry = the geometry we're intersecting
	 * @return the number of intersections between the cameras rays of a 3x3 view plane and the geometry
	 * @throws Exception 
	 */
	public int intersectionCalculator(Camera cam, Geometry geometry) throws Exception {
		int count = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Point pixel = cam.findPixel(j, count, j, i);
				Ray ray = cam.constructRay(pixel);
				List<GeoPoint> intersections = geometry.findGeoIntersections(ray);
				if(intersections != null) {
					count += intersections.size();
				}
			}
		}
	return count;
	}
	

	@Test
	public void sphereIntegrationTest() throws Exception {
		
		/*---- sphere tests------*/
	
		Camera cam = new Camera(new Point(Double3.ZERO), new Vector(0,0,-1), new Vector(0,-1,0));
		cameraSetUp(cam, 1, 3, 3); 
		
		Sphere sphere = new Sphere(new Point(0,0,-3), 1);
		
		//tc 1
		assertEquals("there should be 2 intersections ", 2, intersectionCalculator(cam, sphere));
	
		//tc 2
		sphere = new Sphere(new Point(0,0,-2.5), 2.5);
		cam.setP0(new Point(0,0,0.5));
		assertEquals("there should be 18 intersections", 18, intersectionCalculator(cam, sphere));
		
		//tc 3
		sphere = new Sphere(new Point(0,0,-2), 2);
		//assertEquals("there should be 10 intersections", 10, intersectionCalculator(cam, sphere));
		
		//tc 4 
		sphere = new Sphere(new Point(0,0,-1), 4);
		assertEquals("there should be 9 intersections ", 9, intersectionCalculator(cam, sphere));
		
		//tc 5
		sphere = new Sphere(new Point(0,0,1), 0.5);
		cam.setP0(new Point(Double3.ZERO));
		assertEquals("there should be 0 intersections ", 0, intersectionCalculator(cam, sphere));
		
		
	} 
	
	@Test
	public void planeIntegrationTest() throws Exception {
		Camera cam = new Camera(new Point(Double3.ZERO), new Vector(0,0, -1), new Vector(0,-1, 0));
		cameraSetUp(cam, 1, 3, 3);
		
		//tc 1
		Plane plane = new Plane(new Point(0,0,-10), new Vector(0,0,1));
		assertEquals("there should be 9 intersections a", intersectionCalculator(cam, plane), 9);
		
		//tc 2  
		plane = new Plane(new Point(0,0,-5), new Vector(0,1, 2));  
		assertEquals("there should be 9 intersections b", intersectionCalculator(cam, plane), 9);
		
		//tc 3 
		plane = new Plane(new Point(0,0,-5), new Vector(0,1,1)); 
		assertEquals("there should be 6 intersections ", intersectionCalculator(cam, plane), 6);
		
	}
	
	
	@Test
	public void triangleIntegrationTest() throws Exception {
		
		Camera cam = new Camera(new Point(Double3.ZERO), new Vector(0,0,-1), new Vector(0,1,0));
		cameraSetUp(cam, 1, 3, 3);
		
		//tc 1
		Triangle triangle = new Triangle(new Point(0,1,-2), new Point(1,-1,-2), new Point(-1,-1,-2));
		assertEquals("there should be 1 intersection ", intersectionCalculator(cam, triangle), 1);
		
		
		//tc 2
		triangle = new Triangle(new Point(0,20,-2), new Point(1,-1,-2), new Point(-1,-1,-2));
		assertEquals("there should be 2 intersections ", intersectionCalculator(cam, triangle), 2);
	}	

}
