package geometries;

import java.util.List;
import java.util.stream.Collectors;

import primitives.Point;
import primitives.Ray;

/**
 * 
 * @author Yaakovah, Meira, Tali
 * uses composite pattern, along with geometry and geometries
 */
public abstract class Intersectable {
	
	protected double maxX = 0, maxY = 0, minX = 0, minY = 0;
	
	/**
	 * @author Yaakovah, Meira, Tali
	 * includes a Geometry and a Point, so that we can reference a point on a geometry as one object
	 * this is a plane data structure, and so its attributes will all be public and when accesses them from other classes we do 
	 * not break the LOD 
	 */
	public static class GeoPoint {
		
	    public Geometry geometry;
	    public Point point;
	    
	    public GeoPoint(Geometry g, Point p)
	    {
	    	geometry = g;
	    	point = p;
	    }
	    
	    @Override
        public boolean equals(Object o) {
	    	
            if (o == this) 
                return true;

            if (!(o instanceof GeoPoint)) 
                return false;

            GeoPoint g = (GeoPoint) o;

            return g.geometry.equals(geometry) && g.point.equals(point);
        }
	    
		@Override
		public String toString() {
			return "(" + geometry.toString() + ", " + point.toString()+ ")";
		}
	
	}
	 
	/**
	 * 
	 * @param ray - a ray that may or may not intersect with the geometry. 
	 * @return - a list of all of the GeoPoints of intersection or null (in the case that there is no intersection points)
	 * @throws Exception 
	 *  calls on findGeoIntersectionsHelper to do everything
	 */
	public List<GeoPoint> findGeoIntersections(Ray ray) throws Exception
	{
		return findGeoIntersectionsHelper(ray);
	
	}
	
	/**
	 * @param ray - a ray that may or may not intersect with the geometry. 
	 * @return - a list of all of the GeoPoints of intersection or null (in the case that there is no intersection points)
	 * @throws Exception 
	 * 
	 * called on by findGeoIntersections
	 * 
	 * Note: according to NVI access should be private (and not protected), however, Java does not allow an abstract method to be private
	 */
	protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray) throws Exception;
	
	public double getMaxX() {return maxX;}
	public double getMaxY() {return maxY;}
	public double getMinX() {return minX;}
	public double getMinY() {return minY;}
	
	public void setMaxX() {maxX = calcMaxX();}
	public void setMaxY() {maxY = calcMaxY();}
	public void setMinX() {minX = calcMinX();}
	public void setMinY() {minY = calcMinY();}

	
	/**
	 * 
	 * @return the x of the rightmost side of the conservative boundary region
	 */
	protected abstract double calcMaxX();
	/**
	 * 
	 * @return the y of the bottom-most side of the conservative boundary region
	 */
	protected abstract double calcMaxY();
	
	/**
	 * 
	 * @return the x of the leftmost side of the conservative boundary region
	 */
	protected abstract double calcMinX();
	
	/**
	 * 
	 * @return the y of the top-most side of the conservative boundary region
	 */
	protected abstract double calcMinY();
	
	/**
	 * 
	 * @param in = the intersectable that we are comparing distance to
	 * @param distance = the distance we want to know if we are that amount away from 'in'
	 * @return
	 */
	public Boolean withinDistance(Intersectable in, double distance)
	{
		//adding a boarder of size 'distance' to the region of 'in', essentially
		//creating a new region
		double inMaxX = in.getMaxX() + distance;
		double inMaxY = in.getMaxY() - distance;
		double inMinX = in.getMinX() - distance;
		double inMinY = in.getMinY() + distance;
		
		//check if the x values for the two regions overlap
		boolean isXinRange = (maxX <= inMaxX && maxX >= inMinX)
				|| (minX <= inMaxX && minX >= inMinX)
				||(inMaxX <= maxX && inMaxX >= minX)
				|| (inMinX <= maxX && inMinX >= minX)? true : false;
		
		//check if the y values for the two regions overlap
		boolean isYinRange = (maxY >= inMaxY && maxY <= inMinY)
				|| (minY >= inMaxY && minY <= inMinY)
				||(inMaxY >= maxY && inMaxY <= minY)
				|| (inMaxY >= maxY && inMaxY <= minY)? true :false;
		
		//if the 'this' and 'in' are within 'distance' distance from each other, return true
		return (isYinRange && isXinRange)? true : false;
				 
	}
	
}

