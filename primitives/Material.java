package primitives;

/**
 * 
 * @author Yaakovah, Meira, Tali
 *
 */
public class Material {

	public Double3 kD = Double3.ZERO; //diffuse

	public Double3 kS = Double3.ZERO; //specular
	
	public int shininess = 0; //shininess
	
	public Double3 kT = Double3.ZERO; //transparency coefficient
	
	public Double3 kR = Double3.ZERO; //reflection coefficient
	
	/**
	 * setter
	 * @param kD
	 * @return this, according to the builder pattern
	 */
	public Material setKD(Double3 kD) {
		this.kD = kD;
		return this;
	}
	/**
	 * setter with double
	 * @param kD
	 * @return this, according to the builder pattern
	 */
	public Material setKD(double kD) {
		this.kD = new Double3(kD);
		return this;
	}
	
	/**
	 * setter
	 * @param kS
	 * @return this, according to the builder pattern
	 */
	public Material setKS(Double3 kS) {
		this.kS = kS;
		return this;
	}
	
	/**
	 * setter 
	 * @param kS
	 * @return this, according to the builder pattern
	 */
	public Material setKS(double kS) {
		this.kS = new Double3(kS);
		return this;
	}
	
	/**
	 * setter
	 * @param Shininess
	 * @return this, according to the builder pattern
	 */
	public Material setShininess(int shininess) {
		this.shininess = shininess;
		return this;
	}
	
	/**
	 * setter
	 * @param kT transparency coeff
	 * @return this, according to the builder pattern
	 */
	public Material setKT(Double3 kT) {
		this.kT = kT;
		return this;
	}
	
	/**
	 * setter
	 * @param kT transparency coeff
	 * @return this, according to the builder pattern
	 */
	public Material setKT(double kT) {
		this.kT = new Double3(kT);
		return this;
	}
	
	/**
	 * setter
	 * @param kR reflection coeff
	 * @return this, according to the builder pattern
	 */
	public Material setKR(Double3 kR) {
		this.kR = kR;
		return this;
	}

	/**
	 * setter
	 * @param kR reflection coeff
	 * @return this, according to the builder pattern
	 */
	public Material setKR(double kR) {
		this.kR = new Double3(kR);
		return this;
	}
	
}
