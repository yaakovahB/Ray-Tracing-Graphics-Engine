
package renderer;

import primitives.Util;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class RayTracerBasic extends RayTraceBase {
	
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	private static final double MIN_CALC_COLOR_K = 0.001;
	private static final double INITIAL_K = 1.0;

	public RayTracerBasic(Scene s) {
		super(s);
	}
	
	@Override
	public Color traceRay(Ray ray) throws Exception {
		/*
		 * to search intersections between the ray and the 3DModel of the scene. 
		 * If there are no intersections the color will be the background color. Otherwise, finds the closest 
		 * point to the ray's head (by using the method that we implemented in phase 3) and find the color of it
		 * */
		GeoPoint closestIntersection = findClosestIntersection(ray);
		if(closestIntersection == null)
			return scene.background;
		return calcColor(closestIntersection, ray);
				
	}
	
	@Override
	public Color traceRaySuperSample(List<Ray> rays, int region) throws Exception{
		/*
		 * to search intersections between the rays and the 3DModel of the scene. 
		 * when there are no intersections the color will be the background color. Otherwise, finds the closest 
		 * point to the ray's head (by using the method that we implemented in phase 3) and adds the color of it
		 * finally the average is taken
		 * */ 
		if (rays.size()==0)
			return scene.background;
		Color averageColor = new Color(0,0,0); //setting to black as the zeros shouldn't affect the average
		
		//loop through all rays from aperture and average the colour of the intersections
		for(int i = 0 ; i < rays.size() ; i++) {
			Ray singleRay = rays.get(i);
					
			GeoPoint closestIntersection;
			if(region == -1) //if the CBR algorithm was not requested by user
				closestIntersection = findClosestIntersection(singleRay);
			else
				//if doing CBR, we only want to find intersections with the geometries in the region
				closestIntersection = findClosestIntersectionCBR(singleRay, region);
			
			//if ray doesn't hit a point, then average in the background color
			if(closestIntersection == null)
				averageColor = averageColor.add(scene.background.reduce(rays.size()));
			else {
				
				Color c = calcColor(closestIntersection, singleRay);
				averageColor = averageColor.add(c.reduce(rays.size()));
			}
		}	
		return averageColor;
				
	} 
	
	/**
	 * @brief adds the object's color to the point's color. supports reflection and refraction
	 * @param intersection - a Geopoint that is being intersected that we will calculate the color of
	 * @param ray - the ray that is intersecting the point
	 * @param level - the level of reflecting/refracting (for the recursion)
	 * @param k - the percent of color we are taking from this geometry (for the recursion)
	 * @return the color of the intersection
	 * @throws Exception 
	 */
	public Color calcColor(GeoPoint intersection, Ray ray, int level, double k) throws Exception {
		
		Color tempEmission = intersection.geometry.getEmission();
		Color color = tempEmission.add(calcLocalEffects(intersection, ray, k)); 
		
		//stopping condition in case k never gets low enough
		if(level==1)
			return color;
		else
		{
			color = color.add(calcGlobalEffects(intersection, ray, level, k));
			return color;
		}	
	}
	
	/**
	 * @brief updates the calcColor function to add the object's color to the point's color
	 * @param intersection - a Geopoint that is being intersected that we will calculate the color of
	 * @param ray - the ray that is intersecting the point
	 * @return the color of the intersection
	 * @throws Exception 
	 */
	private Color calcColor(GeoPoint gp, Ray ray) throws Exception
	{
		return calcColor(gp , ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
	}
	
	/**
	 * @brief calculate the color of the object based on all of the local effects
	 * @param intersection - a Geopoint that is being intersected that we will calculate the color of
	 * @param ray - the ray that is intersecting the point
	 * @param k - the percent of color we are taking from this geometry
	 * @return color to add, based on local effects
	 * @throws Exception 
	 */
	private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) throws Exception {
		//direction that we are viewing the scene from
		Vector v = ray.getDir(); 
		//normal of the intersection
		Vector normal = intersection.geometry.getNormal(intersection.point);
		//dot product of the direction vector and the normal
		double nv= Util.alignZero(normal.dotProduct(v)); 
		if (nv== 0) 
			return Color.BLACK;
		
		int nShininess= intersection.geometry.getShininess();
		
		//diffuse coefficient of the geometry
		Double3 kd= intersection.geometry.getKD();
		
		//specular coefficient of the geometry
		Double3 ks= intersection.geometry.getKS();
		Color color= Color.BLACK;
		
		//add the lights 
		for (LightSource lightSource: scene.lights) {
			Vector l = lightSource.getL(intersection.point);
			double nl= Util.alignZero(normal.dotProduct(l));
			//if nl and nv are both positive or if they are both negative
			if (nl* nv> 0) { 
				double ktr = (transparency(intersection,lightSource,l, normal));
				if (ktr * k > MIN_CALC_COLOR_K ){
					Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
					color = color.add(calcDiffusive(kd, l, normal, lightIntensity), calcSpecular(ks, l, normal, v, nShininess, lightIntensity));
				}
			}
		}
		return color;
	}
	
	/**
	 * @param intersection - a Geopoint that is being intersected that we will calculate the color of
	 * @param ray - the ray that is intersecting the point
	 * @param level - the level of reflecting/refracting (for the recursion)
	 * @param k - the percent of color we are taking (for the recursion)
	 * @return color to add, based on global effects
	 * @throws Exception
	 */
	private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level, double k) throws Exception 
	{
		Color color = Color.BLACK;
		
		//calculate intersections with the reflected ray
		Ray reflectedRay = getReflectedRay(intersection, ray);
		GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
		
		//reflective coefficient of the geometry
		Double3 kr = intersection.geometry.getKR();
		//scaled by k
		Double3 kkr = kr.scale(k);
		
		//only if intersection with geometry found do we find the colour we should be adding
		if(reflectedPoint != null)
				//only add the colour if not insignificant
				if (kkr.castToDouble() > MIN_CALC_COLOR_K) 
					color = color.add(calcColor(reflectedPoint, reflectedRay, level-1, kkr.castToDouble()).scale(kr)); 
		
		//refractive (transparency) coefficient of the geometry
		Double3 kt = intersection.geometry.getKT();
		//scaled by k
		Double3 kkt = kt.scale(k);
		
		//calculate intersections with the refracted ray
		Ray refractedRay = getRefractedRay(intersection, ray);
		GeoPoint refractedPoint = findClosestIntersection(refractedRay);
		
		//only if intersection with geometry found do we find the colour we should be adding
		if(refractedPoint != null)
			//only add the colour if not insignificant
			if(kkt.castToDouble() > MIN_CALC_COLOR_K) 
				color = color.add(calcColor(refractedPoint, refractedRay, level-1, kkt.castToDouble()).scale(kt)); 
		return color;
	}

	/**
	 * 
	 * @param kd = diffusive coefficient 
	 * @param l = light direction
	 * @param normal = normal to the geometry at a given geoPoint
	 * @param lightIntensity = the color of the geometry at a given geoPoint
	 * @return how much diffusive to add
	 */
	private Color calcDiffusive(Double3 kd, Vector l, Vector normal, Color lightIntensity) {
	    double factor = Math.abs(Util.alignZero(l.dotProduct(normal)));
	    return lightIntensity.scale(kd.scale(factor));
	  }

	/**
	 * @param ks = specular coefficient
	 * @param l = light direction
	 * @param n =  normal to the geometry at a given geoPoint
	 * @param v = direction of the view
	 * @param nShininess
	 * @param lightIntensity = the color of the geometry at a given geoPoint
	 * @return how much specular to add
	 */
	private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
	    Vector r = l.subtract(n.scale(Util.alignZero(2 * l.dotProduct(n))));
		double vrmax = Math.max(0, v.scale(-1).dotProduct(r));
		double vrn = Math.pow(vrmax, nShininess);
		return lightIntensity.scale(ks.scale(vrn));
	  }
	
	/**
	 * @param geoPoint is the geopoint
	 * @param ls is the lightsource
	 * @param l is the light direction
	 * @param n is the normal
	 * @return ktr - the degree of partial shading, dictated by its transparency 
	 * @throws Exception 
	 */
	private double transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n)throws Exception {
		
		//the direction from point to light source
		Vector lightDirection = l.scale(-1); 
		
		Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);	
		
		//transparency coefficient
		double ktr = 1; 
		
		//no intersection equals no shading to take into account
		if(intersections==null) 
			return ktr;
		
		else
		{	
			//discover if the intersection found is between the lightsource and the object in question
			double distanceBtwnGpLs = ls.getDistance(lightRay.getP0());
			for (GeoPoint geo : intersections)
			{
				double tempDistance = geo.point.distance(lightRay.getP0());		
				//if it is some shading occurs - as dictated by the transparency coefficient
				if(tempDistance <= distanceBtwnGpLs)
					ktr = geo.geometry.getKT().scale(ktr).castToDouble();		
			}		
		}				
		return ktr;	
	}
	
	/**
	  * @brief calculate the reflected ray
	  * @param gpIntersection - a Geopoint that is being intersected 
	  * @param ray - the ray that is intersecting the point
	  * @return the relected ray
	  * @throws Exception
	  */
	 private Ray getReflectedRay(GeoPoint gpIntersection, Ray ray) throws Exception {
		    Vector dirRay = ray.getDir();
		    Vector normal = gpIntersection.geometry.getNormal(gpIntersection.point);
		    //get angle between ray and refkected ray
		    double dotProduct = dirRay.dotProduct(normal);
		    if (dotProduct == 0) 
		      return null;
		    Vector newRay = dirRay.subtract(normal.scale(2 * dotProduct)).normalize();
		    Point point = gpIntersection.point;
		    return new Ray(point, newRay, normal);
	  }

	 /**
	  * @brief calculate the refracted ray
	  * @param gpIntersection - a Geopoint that is being intersected 
	  * @param ray - the ray that is intersecting the point
	  * @return the refracted ray
	  * @throws Exception
	  */
	 private Ray getRefractedRay(GeoPoint gpIntersection, Ray ray) throws Exception {
		 Vector rayDir = ray.getDir();
		 Vector normal = gpIntersection.geometry.getNormal(gpIntersection.point);
		 Point point = gpIntersection.point;
		 return new Ray(point, rayDir,normal);
	  }
	 
	 /**
	  * @brief find the closest geoPoint that the ray intersects
	  * @param ray the ray that we are finding the closest intersection of
	  * @return the closest geoPoint that is intersected by the ray
	  * @throws Exception
	  */
	 private GeoPoint findClosestIntersection(Ray ray) throws Exception
	 {
		 List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
		 
		 if(intersections == null)
			 return null;
		 
		 return ray.findClosestGeoPoint(intersections);		 
		 
	 }
	 
	 /**
	  * @brief find the closest geoPoint that the ray intersects within the boundary (represented by the index)
	  * @param ray = the ray that we are finding the closest intersection of
	  * @param index = relevant region in scene's geometries
	  * @return the closest geoPoint that is intersected by the ray within the relevant region
	  * @throws Exception
	  */
	 private GeoPoint findClosestIntersectionCBR(Ray ray, int index) throws Exception
	 {	 //get only the intersections from geometries located in relevant region (indicated by index)
		 List<GeoPoint> intersections = scene.geometries.getGeometries().get(index).findGeoIntersections(ray);
		 
		 if(intersections == null)
			 return null;
		 
		 return ray.findClosestGeoPoint(intersections);		 
		 
	 }
	 
	 @Override
	 public int conservativeBoundingRegion(Ray ray) throws Exception
	 {
		
		//get the first point that the ray (going thorough a particular pixel) intersects
		GeoPoint gpIntersection = findClosestIntersection(ray);
		Point intersection; 
		
		if(gpIntersection == null)//no intersection means no geometry means no superSampling required
			return -1;
		else
			intersection = gpIntersection.point;
			
	
		double xCoordinateGP = intersection.getX();
		double yCoordinateGP = intersection.getY();
		
		//acquire relevant x and y's for comparison purposes
		double globalMaxX = scene.geometries.getMaxX();
		double globalMaxY = scene.geometries.getMaxY();
		double globalMinX = scene.geometries.getMinX();
		double globalMinY = scene.geometries.getMinY();
			
		//if point doesn't fall within the min/max boundaries of scene, return -1 
		if(!(xCoordinateGP < globalMaxX && xCoordinateGP > globalMinX
		  		&& yCoordinateGP > globalMaxY && yCoordinateGP < globalMinY))
			return -1;

		//otherwise check which boundary the ray intersects, and return the index representing that CBR
		for(int i = 0; i < scene.getSize();++i)
			if(gpIntersection.geometry.withinDistance(scene.geometries.getGeometries().get(i), 0))
				return i;
			
		
		return -1; 
	}
}
