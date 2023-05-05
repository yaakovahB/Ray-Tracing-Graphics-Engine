package geometries;

import java.util.ArrayList;



import java.util.LinkedList;
import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 * defined by a point and the orthogonal normalized vector 
 */
public class Plane extends Geometry {
	
	final Point q0;
	final Vector normal;
	
	/** 
	 * @brief sets q0 and then creates two vectors and finds the cross product of them, giving us the normal to the plane
	 * we then normalize the normal
	 * 
	 * @param q0 first point to define the plane 
	 * @param q1 second point to define the plane
	 * @param q2 third point to define the plane
	 * 
	 */
	public Plane(Point q0, Point q1, Point q2) 
	{
		
		this.q0 = q0;
		
		//find normal 
		Vector vector1 = q0.subtract(q1);
		Vector vector2 = q2.subtract(q1);
		Vector crossProduct = vector2.crossProduct(vector1);
		
		this.normal = crossProduct.normalize();
		
		setMaxX();
		setMaxY();
		setMinX();
		setMinY();
	}

	public Plane(Point q0, Vector normal) 
	{
		this.q0 = q0;
		this.normal = normal.normalize();
	}
	
	public Point getPoint()
	{
		return q0;
	}

	@Override
	public Vector getNormal(Point point)  {
		
		return normal.normalize();
	}

	/**
	 *  @brief creates two vectors and finds the cross product of them, giving us the normal to the plane
	 * we then normalize the normal and return it
	 * 
	 * @param point1
	 * @param point2
	 * @param point3
	 * @return the normal to the plane
	 */
	public Vector getNormal(Point point1, Point point2, Point point3)  {
		
		Vector vector1 = point1.subtract(point2);
		Vector vector2 = point3.subtract(point2);
		Vector crossProduct = vector2.crossProduct(vector1);
		
		return crossProduct.normalize();
	}
	
	@Override
	public String toString() {
		return "q0: " + q0.toString() + "normal: " + normal.toString();
	}

	@Override
	public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		
		// using a linkedList (as opposed to arrayList) bc mainly adding
		List<GeoPoint> results = new LinkedList<GeoPoint>(); 
		
		//u = q0 - p0
		Vector u = q0.subtract(ray.getP0());
		
		//(normal*u) / (normal*v)
		double t = (normal.dotProduct(u)) / (normal.dotProduct(ray.getDir()));
		
		if(t > 0 && !Double.isInfinite(t)) {
			results.add(new GeoPoint(this, ray.getPoint(t)));
			return results;
		}
		
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

