package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * @author Yaakovah, Meira, Tali
 */
public class Cylinder extends Tube{
	
	final double height;
	
	public Cylinder(Ray axisRay, double radius, double height) {
		super(axisRay, radius);
		this.height = height;
		
		setMaxX();
		setMaxY();
		setMinX();
		setMinY();
	}
	
	@Override
	public Vector getNormal(Point point) throws Exception {
		return super.getNormal(point);
	}
	
	/**
	 * @brief returns the normal at the top of the cylinder., we don't have to normalize it because it is already
	 * Normalised in the constructor of Ray
	 * @return the normal at the top
	 */
	public Vector getNormalTop() {
		return this.axisRay.getDir(); 
	}
	
	/**
	 * @brief returns the normal at the base of the cylinder., we don't have to normalize it because it is already
	 * Normalised in the constructor of Ray. Scales by -1 becuase we are returning the base, not the top
	 * @return the normal at the base
	 * @throws Exception
	 */
	public Vector getNormalBase() throws Exception {
		return this.axisRay.getDir().scale(-1); 
	}
	
	@Override
	public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

}

