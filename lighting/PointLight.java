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
public class PointLight extends Light implements LightSource {

	private Point position;
	private double kC, kL, kQ; //constant, linear and quadratic attenuation coefficents (how the distance effects the light)

	public PointLight(Color intensity, Point position, double kC, double kL, double kQ) {
		super(intensity);
		this.position = position;
		this.kC = kC;
		this.kL = kL;
		this.kQ = kQ;	
	}
	
	public PointLight(Color intensity, Point position) {
		super(intensity);
		this.position = position;
		kC = 1;
		kL = 0;
		kQ = 0;			
	}
	
	/**
	 * setter : returns this, according to the builder pattern
	 * @param kC 
	 * @return this
	 */
	public PointLight setKC(double kC) {
		this.kC = kC;
		return this;
	}
	
	/**
	 * setter : returns this, according to the builder pattern
	 * @param kL
	 * @return this
	 */
	public PointLight setKL(double kL) {
		this.kL = kL;
		return this;
	}
	
	/**
	 * setter : returns this, according to the builder pattern
	 * @param kQ
	 * @return this
	 */
	public PointLight setKQ(double kQ) {
		this.kQ = kQ;
		return this;
	}	

	@Override
	public Color getIntensity(Point p) {
		
		//returning the accurate color based on the attenuation coefficients and the distance
		double d = position.distance(p);
	    return (getIntensity().reduce(kC + (d*kL) + (kQ * d * d)));
	}

	@Override
	public Vector getL(Point p) {
		
		//if p is at the same point as the light, then there is no direction
		if ( p.equals(position))
			return null;
		
		//returning direction
		return p.subtract(position).normalize();
		
	}
	
	/**
	 * @param point = the point to get the distance to
	 * @return distance between the light and the point
	 */
	public double getDistance(Point point)
	{
		return position.distance(point);
	}

}
