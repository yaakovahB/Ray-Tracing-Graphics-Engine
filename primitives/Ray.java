package primitives;

import java.util.List;

import geometries.Plane;

import geometries.Intersectable.GeoPoint;

/**
 * Class Ray is the basic class representing a Ray of Euclidean Geometry in Cartesian 3-Dimensional coordinate system
 * @author Yaakovah, Meira, Tali 
 */
public class Ray {
	
	final Point p0;
	final Vector dir; 
	//DELTA is needed for moving the head of a ray an infinitesimal to prevent self intersection
	private static final double DELTA = 0.1; 
	
	/** Constructor to initialize Ray object with a Point and a Vector
	 * 
	 * @param point coordinate value start of ray
	 * @param vector direction of ray (normalised)
	 * @throws Exception 
	 */
	public Ray(Point point, Vector vector)  {
		this.p0 = point;
		this.dir = vector.normalize();
	}
	
	/** Constructor to initialize Ray object with a Point and two Vectors
	 * 
	 * @param point coordinate value start of ray - it is the point of the geoPoint that we will create
	 * @param direction - direction of ray (normalised)
	 * @param normal - the normal to the geometry of the geoPoint, that will be created from point
	 * @throws Exception 
	 */
	public Ray(Point point, Vector direction, Vector normal)  {
		
		double dotProduct = normal.dotProduct(direction);
		double scale;
		
		//deciding which direction to move the ray in, based on the direction of the vector
		if(dotProduct >= 0)
			scale = DELTA;
		else 
			scale = -DELTA;
		
		//moving the rays head
		Vector delta = normal.scale(scale);
		p0 = point.add(delta);
		
		dir=direction;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Ray))
			return false;
		Ray other = (Ray) obj;
		return this.p0.equals(other.p0) && this.dir.equals(other.dir); 
	}
	
	@Override
	public String toString() {
		return p0.toString() + dir.toString();
	}
	
	public Point getP0() {
		return this.p0;
	}
	
	public Vector getDir() {
		return this.dir;
	}
	
	public Point getPoint(double t)  {
		// P=P_0+t*v
		return p0.add(dir.scale(t));
	}
	
	/** 
	 * @param points - list of points
	 * @return closest point to the ray's head
	 * @throws Exception 
	 */
	public Point findClosestPoint(List<Point> points) {
		
	    return ((points == null) || (points.isEmpty())) ? null
	           : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
	} 
	
	

	/**
	 * @param points - list of Geopoints
	 * @return closest GeoPoint to the ray's head
	 * @throws Exception 
	 */
	public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
		
		if(points == null) 
			return null;
		
		double smallestDistance = Double.MAX_VALUE;
		GeoPoint closestGeoPoint = points.get(0);
	
		//go through list and if find a distance thats shorter than current shortest distance, update closest distance and closest GeoPoint
		for(GeoPoint p : points) {
			double tempDistance = this.p0.distance(p.point);
			if(tempDistance < smallestDistance) {
				smallestDistance = tempDistance;
				closestGeoPoint = p;
			}
		}
		return closestGeoPoint;
	}
	
}
