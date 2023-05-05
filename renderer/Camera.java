package renderer;

import primitives.*;
import unitTests.primitives.VectorTests;

import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Random;

import geometries.Plane;
/**
 * @author yaakovah, meira, tali
 *
 */
public class Camera {

	private Point p0; // origin of the camera
	private Vector vTo, vUp, vRight; // axis of the camera - vTo = z , vUp = y, vRight = x
	private double distance; // distance of the camera from the viewplane
	private double  width, height ; // width and height of the viewplane
	private double  radiusAperture; //radius of the aperture (for adjusting the depth of field)
	private ImageWriter imageWriter;
	private RayTraceBase rayTraceBase;
	private Plane focalPlane; //for adjusting the depth of field
	private int threadsCount = 1; // sets the amount of threads we want to use, defaulted to 1 if not multithreading
	private double printInterval = 0.1; //sets how often we want to print the percent finished we are, when using multithreading
	private int numOfRaysSuperSampling = 0; // number of additional rays to add for supersampling. 0 if no supersampling
	private boolean CBR = false;
	public double distanceCBR = 0; //for purposes of CBR
	
	
	public Camera(Point p0, Vector vTo, Vector vUp)  {

		if(!isZero(vTo.dotProduct(vUp)))
			throw new IllegalArgumentException("The two vectors must be orthogonal");
		this.p0 = p0;
		try {
			this.vTo = vTo.normalize();
			this.vUp = vUp.normalize();
			this.vRight = (vTo.crossProduct(vUp)).normalize();
		} catch (Exception e) {
			System.out.println("Can't have a zero vector");
		}	
	}
	
	/**
	 * setter, according to the builder pattern
	 * @brief set the size of the view plane
	 * @param width = width of the view plane
	 * @param height = height of the view plane
	 * @return this (camera)
	 */
	public Camera setVPSize(double width, double height) {
		this.width = width;
		this.height = height;
		return this;
	}
	
	/**
	 * setter, according to the builder pattern
	 * @brief creates the focal plane and makes is fpdistance away
	 * @param distance distance of the focal plane
	 * @return this (camera)
	 */
	public Camera setFocalPlane(double fpdistance) throws Exception {
		if(this.distance == 0) {
			throw new Exception("Must set VPdistance first");
		}
		//focal plane should be beyond the view plane
		if(fpdistance > this.distance) {
			//creating plane that is perpendicular to vTo and distance2FocusPlane away from camera
			//thus it is parallel to the view plane
			Point centralPoint = p0.add(vTo.scale(fpdistance));
			this.focalPlane = new Plane(centralPoint, vTo);
		}
		else 
			throw new Exception("Focal plane distance must be greater than view pLane distance");
		return this;
	}
	
	public void setApertureSize(double radius) {
		this.radiusAperture = radius;
	}
	
	public void setP0(Point p0) {
		this.p0 = p0;
	}
	
	
	/**
	 * setter, according to the builder pattern
	 * @brief set the distance of the view plane to the camera, if the distance is <= 0, it automatically makes the distance 1
	 * @param distance the distance from camera to view plane
	 * @return this (camera)
	 */
	public Camera setVPDistance(double distance) {
		if(distance <= 0) {
			System.out.println("distance must be greater than zero, setting to 1");
			this.distance = 1;
		}
		this.distance = distance;
		return this;
	}
	 
	/**
	 * setter, according to the builder pattern
	 * @param imageWriter
	 * @return this (camera)
	 */
	public Camera setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	} 
		
	/**
	 * setter, according to the builder pattern
	 * @param rayTraceBase
	 * @return this (camera)
	 */
	public Camera setRayTraceBase(RayTraceBase rayTraceBase) {
		this.rayTraceBase = rayTraceBase;
		return this;
	}
	
	/**
	 * setter, according to the builder pattern
	 * @param count = the amount of threads we want to run
	 * @return this
	 */
	public Camera setMultithreading(int count)
	{
		threadsCount = count;
		return this;
	}
	
	/**
	 * setter, according to the builder pattern
	 * @param interval = the amount of time we want to wait before printing the progress
	 * @return this 
	 */
	public Camera setDebugPrint(double interval)
	{
		printInterval = interval;
		return this;
	}
	
	/**
	 * setter, according to the builder pattern
	 * @param numRays = number of rays to shoot with supersampling. will be 1 if there is no supersampling
	 * @return
	 */
	public Camera setNumOfRaysSuperSampling(int numRays)
	{
		numOfRaysSuperSampling = numRays;
		return this;
	}
	
	/**
	 * @brief sets up CBR for use when needed
	 * @param cbrInUse
	 * @param distancecbr
	 */
	public void setUpCBR(boolean cbrInUse, double distancecbr){
		CBR = cbrInUse;
		distanceCBR = distancecbr;
		rayTraceBase.scene.convertFlatGeometriesToHierarchical(distanceCBR);
	}
	
	/**
	 * @brief sets up MultiThreading for use when needed
	 * @param numOfThreads
	 * @param printInterval
	 */
	public void setUpMultiThreading(int numOfThreads, double printInterval){
		setMultithreading(numOfThreads);
		setDebugPrint(printInterval);
	}
	
	/**
	 * @brief sets up SuperSampling for use when needed
	 * @param numOfSuperSampleRays
	 * @param apertureSize
	 * @param fpDistance
	 * @throws Exception
	 */
	public void setUpSuperSampling(int numOfSuperSampleRays, double apertureSize, double fpDistance) throws Exception{
		setNumOfRaysSuperSampling(numOfSuperSampleRays);
		setApertureSize(apertureSize);
		setFocalPlane(fpDistance);
	}
	
	/**
	 * @brief find the center point of pixel on the view plane
	 * @param nX width - # of rows
	 * @param nY height - # of columns
	 * @param j index of pixel  width = columns
	 * @param i index of pixel height = rows
	 * @return the center point of the pixel found
	 */
	public Point findPixel(int nX, int nY, int j, int i) {
		
		//find the center point of the pixel
		Point pc = p0.add(vTo.scale(distance));
		
		double pixelWidth = width/nX; 
		double pixelHeight = height/nY;
			
		double yi = -((i - (nY-1)/2d)) * pixelHeight;
		double xj = ((j - (nX-1)/2d)) * pixelWidth;
			
		Point pIJ = pc;
		if(!isZero(yi))
			pIJ = pIJ.add(vUp.scale(yi));
		if(!isZero(xj))
			pIJ = pIJ.add(vRight.scale(xj));
		return pIJ;
	}
	
	/**
	 * @brief constructs ray through pixel from camera
	 * @param pIJ - pixel at the index (i, j) on the viewplane - the pixel we construct the ray through
	 * @return the constructed ray
	 */
	public Ray constructRay(Point pIJ) {
		try {
			Vector dir = pIJ.subtract(p0).normalize();
			return new Ray(p0, dir);
				
			} catch (Exception e) {
				System.out.println("Can't have zero vector");
			}
		return null;
	}
	
	/**
	 * @brief creates a list of rays within the aperture (that lies on the viewplane) 
	 * to the focal plane for super sampling 
	 * @param pixel - central point of aperture
	 * @param fpIntersection - focal point on focal ray
	 * @param numOfRays - number of sample rays
	 * @return list of rays for super sampling
	 */
	private List<Ray> apertureCreateRays(Point pixel, Point fpIntersection, int numOfRays) {
		double x,y,z = 0;
		double rangeMin = (-1)*radiusAperture;
		double rangeMax = radiusAperture;
		List<Ray> sampleRays = new LinkedList<Ray>();
		Random r = new Random();
		//here we calculate randomised vector in order to acquire a new random point within our aperture
		//when it is confirmed as within range we construct a ray from it to our focal point 
		//and add it to our list
		for(int i = 0; i < numOfRays; i++ ) {
			//only changing the x and y values bc z should always be on the same plane 
			x  = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			y = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			
			Point newPoint= pixel.add(new Vector(x,y,z));
			if(newPoint.distance(pixel) <= radiusAperture) {
				Vector dir = fpIntersection.subtract(newPoint).normalize();
				sampleRays.add(new Ray(newPoint, dir));
			}
		}
		//adding central ray to our list of sample rays
		Vector dir = fpIntersection.subtract(pixel).normalize();
		sampleRays.add(new Ray(pixel, dir));
		return sampleRays;
	}
	
	/**
	 * 
	 * @param nX = number of columns
	 * @param nY = number of rows
	 * @param colPixel
	 * @param rowPixel
	 * @throws Exception
	 */
	private void castRay(int nX, int nY, int colPixel, int rowPixel) throws Exception
	{
		Point pixel = findPixel(nX, nY, colPixel, rowPixel);
		Ray ray = constructRay(pixel);
		Color pixelColor;
		
		//check that the user wants supersampling and that the object hit by the ray
		//is in the region to do supersampling
		if(numOfRaysSuperSampling!=0)
		{
			int regionToSample = -1;//assume not using CBR in which case sample all geometries
			if(CBR)//if we are using CBR
				//if regionToSample = -1, then that means that the ray didnt intersect an object and no need for supersamping 
				regionToSample = rayTraceBase.conservativeBoundingRegion(ray);
		
			Point fpIntersection = focalPlane.findGeoIntersections(ray).get(0).point; //intersection of ray with focal plane
			
			//if not super samping bc ray didnt hit object (in the case of CBR)
			if(CBR && regionToSample == -1)
				pixelColor = this.rayTraceBase.traceRay(ray);
			else
			{
				List<Ray> superSampleRays = apertureCreateRays(pixel, fpIntersection, numOfRaysSuperSampling);//list of rays for super sampling
				pixelColor = this.rayTraceBase.traceRaySuperSample(superSampleRays, regionToSample); //averaged color
			}
		}
		else
			pixelColor = this.rayTraceBase.traceRay(ray);
		imageWriter.writePixel(colPixel,rowPixel,pixelColor);
	}
		
	/**
	 *@brief color all of the pixels
	 * @throws Exception 
	 */	
	public void renderImage() throws Exception {

		/*
		 * to loop over all the ViewPlane's pixels. For each pixel it will construct a ray and for each 
		 * ray it will calculate a color (the ray tracer will return a color). The color will be stored 
		 * in the corresponding pixel in the image using the writePixel method.
		 * */
		
		if(this.p0 == null || this.vTo == null || this.vUp == null || this.vRight == null)
			throw new MissingResourceException("missing resource", "Camera", "");
		
		if(this.imageWriter == null || this.rayTraceBase == null)
			throw new MissingResourceException("missing resource", "Camera", "");
		
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		
		if(threadsCount != 1)
		{
			Pixel.initialize(nY, nX, printInterval);
			while(threadsCount-- > 0)
			{
				new Thread(() -> {
					for(Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
						try {
							castRay(nX, nY, pixel.col, pixel.row);
						} catch (Exception e) {
							e.printStackTrace();
						}
				}).start();
			}
			Pixel.waitToFinish();
		}
		else
		{
			//go through all the pixels, row by row and column by column, and get the color and then write it to the image
			//uses one thread, as opposed to the many threads it uses in the loop in the if statement above
			Pixel.initialize(nY, nX, printInterval);
			for(int row = 0; row < nY; ++row )
				for(int col = 0; col < nX; ++col)
				{
					castRay(nX,nY,col,row);
					Pixel.pixelDone();
					Pixel.printPixel();
				}

		}
		 
	}
	
	/**
	 * @brief creates a grid of lines
	 * @param interval = the size of the spacing of the grid
	 * @param color = color of the grid
	 */	
	public void printGrid(int interval, Color color)  {
		/*
		 * creates a grid of lines as similar to what you did in the test of the first phase 
		 * (Make sure that the grid does not ruin your picture. The grid should not overwrite the picture
		 * that you are drawing.  You only want to color the pixels where the grid appears in them, leave 
		 * the other pixels alone. 
		 * */
		
		if(imageWriter == null)
			throw new MissingResourceException("missing resource", "Camera", "");
		
		int nX = imageWriter.getNx();
	    int nY = imageWriter.getNy();
	    
	    for (int row = 0; row < nY; row++) 
	      for (int col = 0; col < nX; col++) 
	        if (col % interval == 0 || row % interval == 0) 
	          imageWriter.writePixel(col, row, color);
		
	}

	/**
	 * @brief calls on imageWriters writeToImage
	 * @throws MissingResourceException
	 */	
	public void writeToImage(){
		
		if(imageWriter == null)
			throw new MissingResourceException("missing resource", "Camera", "");
		
		imageWriter.writeToImage();
	}
}
