package scene;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import geometries.Geometries;
import geometries.Intersectable;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;

/**
 * Scene is a PDS (Plain Data Structure) which means that access to all of its attributes is public 
 * @author Yaakovah, meira, Tali
 */
public class Scene {

	public String name; 
	public Color background;
	public AmbientLight ambientLight;
	public Geometries geometries;
	public LinkedList<LightSource> lights = new LinkedList<LightSource>();
	
	
	public Scene(String name) {
		this.name = name;
		this.background = Color.BLACK;
		this.ambientLight = new AmbientLight(); 
		this.geometries = new Geometries();
	}
	
	/**
	 * 
	 * @return the number of objects in the scene
	 */
	public int getSize() {return geometries.getSize();}

	
	/**
	 * setter
	 * @param color
	 * @return this, according to the builder pattern
	 */
	public Scene setBackground (Color color) { 
		background = color; 
		return this;
	}
	
	/**
	 * setter
	 * @param color
	 * @param ka = attenuation factor
	 * @return this, according to the builder pattern
	 */
	public Scene setAmbientLight (Color color , Double3 ka) {
		this.ambientLight = new AmbientLight(color, ka); 
		return this;
	}
	
	/**
	 * setter
	 * @param ambientLight
	 * @return this, according to the builder pattern
	 */
	public Scene setAmbientLight (AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}
	
	/**
	 * setter
	 * @param lights
	 * @return this, according to the builder pattern
	 */
	public Scene setLights(LinkedList<LightSource> lights) {
		this.lights = lights;
		return this;
	}
	
	/**
	 * setter
	 * @param geometries
	 * @return this, according to the builder pattern
	 */
	public Scene setGeometries(Intersectable... geometries) {
		this.geometries.add(geometries);
		return this;
	}

	/**
	 * @brief take the current geometries and make it into hierarchical list 
	 * @param distance = the max distance for geometries to be put in the same region
	 */
	public void convertFlatGeometriesToHierarchical(double distance)
	{
		//create temp list to put all the geometries in hierarchical order
		Geometries tempGeometries = new Geometries();
		
		//for each intersectable in geometries
		for(int i=0;i<getSize();++i)
		{
			//adding the intersectable to appropriate region and returning the index of that region
			int indexOfRegionAddedTo = addToGeometriesHierarchy(tempGeometries, geometries.getGeometries().get(i), distance);
			//if didn't add the intersectable to any region yet, add it to a new region
			if(indexOfRegionAddedTo == -1)
				tempGeometries.add(new Geometries(geometries.getGeometries().get(i)));
		}
		
		geometries =  tempGeometries;
	}

	/**
	 * @brief helper method for convertFlatGeometriesToHierarchical. adds an intersectable 
	 * tempGeometries in an hierarchical fashion
	 * @param tempGeometries
	 * @param i
	 * @return
	 */
	private int addToGeometriesHierarchy(Geometries tempGeometries, Intersectable i, double distance) {
		
		int indexOfLastRegionAddedTo = -1;
		//check all the geometries in tempGeometries and see if the 
		//intersectable should be part of that boundary 
		for(int j=0;j<tempGeometries.getSize();++j)
			
			//if the intersectable is in the boundary, add it that region
			if(i.withinDistance(tempGeometries.getGeometries().get(j), distance))
				{
					//if the intersectable was already added to another region
					if(indexOfLastRegionAddedTo != -1)
					{
						//add this group to the index's group
						((Geometries)(tempGeometries.getGeometries().get(indexOfLastRegionAddedTo)))
							.add(tempGeometries.getGeometries().get(j));
						//delete this group from the list - no copies
						tempGeometries.getGeometries().remove(tempGeometries.getGeometries().get(j));
						j--;
					}
					else//intersectable wasn't previously added
					{
						((Geometries)(tempGeometries.getGeometries().get(j))).add(i);
						indexOfLastRegionAddedTo = j;
					}	
				}
		return indexOfLastRegionAddedTo;
	}
	
}


