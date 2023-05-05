/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 * 
 */
public interface LightSource {

	public Color getIntensity(Point p);
	public Vector getL(Point p) throws Exception; //get direction of the light 
	public double getDistance(Point point);

}
