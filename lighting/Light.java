/**
 * 
 */
package lighting;

import primitives.Color;

/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class Light {
	
	//since this is artificial lighting, the intensity is the same at every point
	private Color intensity; 
	
	protected Light(Color intensity) {
		this.intensity = intensity;
	}
	
	public Color getIntensity()
	{
		return intensity;
	}
}
