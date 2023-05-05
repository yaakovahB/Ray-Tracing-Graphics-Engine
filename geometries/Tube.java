package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * 
 * @author Yaakovah, Meira, Tali
 *
 */
public class Tube extends Geometry{
	
	final Ray axisRay;
	final double radius;
	
	public Tube(Ray axisRay, double radius) {
		this.axisRay = axisRay;
		this.radius = radius;
		
		setMaxX();
		setMaxY();
		setMinX();
		setMinY();
	}

	public Ray getAxisRay()
	{
		return axisRay;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	/** 
	 * @brief method to get the nomal of the tube from a given point that lies on the tube
	 * @param point - point that lays on the body of the tube, where the normal will be from
	 * @throws Exception 
	 */
	public Vector getNormal(Point point) throws Exception {
		//t = distance = v.dotProduct(P-P0)
		double t = this.axisRay.getDir().dotProduct(this.axisRay.getP0().subtract(point));
		// o = projection of Point on cylinder's ray = P0 + v.scale(t)
		Point o = this.axisRay.getP0().add(this.axisRay.getDir().scale(t));
		return point.subtract(o).normalize();
	}

	@Override
	public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double calcMaxX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double calcMaxY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double calcMinX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double calcMinY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
