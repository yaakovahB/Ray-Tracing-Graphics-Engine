/**
 * 
 */
package renderer;

import java.util.List;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * @author Yaakovah, meira, Tali
 *
 */
public abstract class RayTraceBase {
	
	protected Scene scene;

	
	public RayTraceBase(Scene s) {
		this.scene = s;
	}
	
	public Scene getScene() {return scene;}
	
	/**
	 * 
	 * @param ray
	 * @return color of the intersection point of the ray
	 * @throws Exception
	 */
	public abstract Color traceRay(Ray ray) throws Exception;
	
	/**
	 * 
	 * @param rays
	 * @param region = indicated where to supersample
	 * @return average of the colors at the intersection points of the rays
	 * @throws Exception
	 */
	public abstract Color traceRaySuperSample(List<Ray> rays, int region) throws Exception;
	
	/**
	 *
	 * @param ray = original ray from pixel to picture
	 * @return which CBR the ray intersects, -1 if none
	 * @throws Exception
	 */
	public abstract int conservativeBoundingRegion(Ray ray) throws Exception;

}


