/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 * distance doesnt matter because directional light is not reduced by distance
 */
public class DirectionalLight extends Light implements LightSource {

	private Vector direction;

	public DirectionalLight(Color intensity, Vector direction) throws Exception {
		super(intensity);
		this.direction = direction;
	}
	
	@Override
	public Color getIntensity(Point p) {
		return super.getIntensity();
	}

	@Override
	public Vector getL(Point p) {
		return direction.normalize();
	}

	/**
	 * @param point = the point to get the distance to
	 * @return infinity because distance doesnt matter because directional light is not reduced by distance
	 */
	public double getDistance(Point point)
	{
		return Double.POSITIVE_INFINITY;
	}
}
