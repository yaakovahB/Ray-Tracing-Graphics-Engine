/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.*;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class SpotLight extends PointLight {

	private Vector direction;
	
	public SpotLight(Color intensity, Point position, double kC, double kL, double kQ, Vector direction) {
		super(intensity, position, kC, kL, kQ);
		this.direction = direction.normalize();
	}

	public SpotLight(Color intensity, Point position, Vector direction) {
		super(intensity,position);
		this.direction=direction;
	}

	@Override
	public Color getIntensity(Point p) {
		return super.getIntensity(p).scale(Math.max(0,direction.normalize().dotProduct(getL(p))));
		
	}
}
