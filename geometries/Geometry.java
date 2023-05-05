package geometries;

import java.util.List;

import geometries.Intersectable.GeoPoint;
import primitives.*;

/**
 * 
 * @author Yaakovah, Meira, Tali
 *uses composite pattern, along with intersectable and geometries
 */
public abstract class Geometry extends Intersectable{
	
	protected Color emission = Color.BLACK;
	private Material material = new Material();
	
	/**
	 * @param p = point on the geometry
	 * @return The vector that is perpendicular to the geometry starting at the point
	 * @throws Exception 
	 */
	public abstract Vector getNormal(Point p) throws Exception;

	public Color getEmission() 
	{
		return emission ;
	}
	
	public Material getMaterial() 
	{
		return material;
	}

	public int getShininess() 
	{
	    return material.shininess;
	}
	
	public Double3 getKD() {
	    return material.kD;
	}

	public Double3 getKS() {
		return material.kS;
	}

	public Double3 getKR() {
		return material.kR;
	}

	public Double3 getKT() {
		return material.kT;
	}
	
	/**
	 * @brief setter that uses the builder pattern to enable chaining
	 * @param c = the color that we want to set emission to
	 * @return this (according to the builder pattern)
	 */
	public Geometry setEmission(Color c) 
	{
		this.emission = c; 
		return this;
	}
	
	/**
	 * @brief setter that uses the builder pattern to enable chaining
	 * @param material = material that we are setting the objects material to
	 * @return this
	 */
	public Geometry setMaterial(Material material) {
		this.material = material;
		return this;
	}
	
	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) throws Exception
	{
		return findGeoIntersectionsHelper(ray);	
	}
	
	@Override
	protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray) throws Exception;	
	
}

