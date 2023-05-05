package primitives;

import java.lang.Math;
/**
 * Class Point is the basic class representing a point of Euclidean Geometry in Cartesian 3-Dimensional coordinate system
 * @author Yaakovah, Meira, Tali
 */
public class Point {
	
	final Double3 xyz;

	public Point(Double3 double3){
		this.xyz = double3;
	}

	public Point(double d1, double d2, double d3){
		this.xyz = new Double3(d1, d2, d3);
	}
	
	public double getX() { return xyz.d1;}
	
	public double getY() { return xyz.d2;}
	
	public double getZ() { return xyz.d3;}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Point))
			return false;
		Point other = (Point) obj;
		return xyz.equals(other.xyz);
	}
	
	@Override
	public String toString() {
		return xyz.toString();	
	}
	
	/**
	 * @brief Sum a point and a vector
	 * @param vector right hand side operand for addition
	 * @return point that is a result of adding
	 */
	public Point add(Vector vector) {
		return new Point(vector.xyz.add(this.xyz));
	}
	
	/**
	 * @brief subtract point from vector into a new vector 
	 * @param point = right hand side operand for subtraction
	 * @return result of substraction
	 * @throws Exception 
	 */
	public Vector subtract(Point point) {
		return new Vector(this.xyz.subtract(point.xyz));
	}
	
	/**
	 * @brief Method to assist in calculating the distance between two points
	 * @param point the point we are finding the distance to
	 * @return distance squared between two points
	 */
	public double distanceSquared(Point point) {
		
		Point temp1 = new Point(this.xyz.subtract(point.xyz)); //x2-x1, y2-y1, z2-z1
		
		//x*x,y*y,z*z using double3 product function
		Point temp2 = new Point(temp1.xyz.product(temp1.xyz));
	
		double temp3 = temp2.xyz.sumElements();
		
		return Math.abs(temp3);	
	}
	
	/**
	 * @brief Method to calculate the distance between two points, using the distanceSquared function
	 * @param point the point we are finding the distance to
	 * @return distance
	 */
	public double distance(Point point) {
		return Math.sqrt(distanceSquared(point));
	}
}
