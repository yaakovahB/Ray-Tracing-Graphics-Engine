package geometries;

import java.util.LinkedList;

import java.util.List;

import primitives.*;

/**
 * @author Yaakovah, Meira, Tali
 * 
 */
public class Sphere extends Geometry {
	
	final Point centre;
	final double radius;
	
	public Sphere(Point p, double r) {
		centre = p;
		radius = r;
		
		setMaxX();
		setMaxY();
		setMinX();
		setMinY();
	}
	
	@Override
	public Vector getNormal(Point point) throws Exception {
		
		Vector v ;
		v = point.subtract(centre);
		return v.normalize();
		   
	}
	
	public Point getCentre() {
		return centre;
	}

	public double getRadius() {
		return radius;
	}

	/**
	 * @brief this function finds the intersections between a ray and a sphere as per the slides from last semester
	 * @param ray = the ray that is intersecting the sphere 
	 * @returns the list of intersections btw the ray and sphere if there are any and null if there are no intersections
	 */
	@Override
	public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) throws Exception {
		
		//u = O - P0
		Vector u = centre.subtract(ray.getP0());
		
		//tm = v*u
		double tm = ray.getDir().dotProduct(u);
		double dSquared = u.lengthSquared() - tm*tm;
		double d = Math.sqrt(dSquared);
		
		//if d>=r no intersections
		if (d >= radius)
			return null;
		
		double th = Math.sqrt(radius*radius - dSquared);
		double t1 = tm + th;
		double t2= tm - th;
		
		List<GeoPoint> results = new LinkedList<GeoPoint>();
		
		if(t1 <= 0 && t2 <= 0 ) {
			return null;
		}
		
		if(t1 > 0)
			results.add(new GeoPoint(this, ray.getP0().add(ray.getDir().scale(t1))));
		
		if(t2 > 0)
			results.add(new GeoPoint(this,ray.getP0().add(ray.getDir().scale(t2))));
		
		return results;
		
	}	

	@Override
	protected double calcMaxX() {
		return centre.getX() + radius;	
	}

	@Override
	protected double calcMaxY() {
		return centre.getY() - radius;
	}

	@Override
	protected double calcMinX() {
		return centre.getX() - radius;
	}

	@Override
	protected double calcMinY() {
		return centre.getY() + radius;
	}

}
