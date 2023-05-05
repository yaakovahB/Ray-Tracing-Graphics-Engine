/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * @author Yaakovah, Meira, Tali
 *AmbientLight is the background light
 */
public class AmbientLight extends Light { 
	
	
	/**
	 * calls on Super constructor to set the ambient light (and the intensity field) to Black
	 */
	public AmbientLight() {
		
		super(Color.BLACK);
	}

	/**
	 * @param color - the original color of the light (the intensity of the light according to RGB) - IA
	 * @param d - contains the attenuation factor of the original light - KA
	 * calls on Super constructor to do this
	 */
	public AmbientLight(Color color, Double3 d) {
		
		// IP (intensity of a point) = KA * IA
		super(color.scale(d));
	}
	
}

