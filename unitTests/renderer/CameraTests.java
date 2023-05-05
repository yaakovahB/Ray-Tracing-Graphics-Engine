package unitTests.renderer;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;





import renderer.Camera;
import primitives.*;

/**
 * Testing Camera Class
 * 
 * @author Dan
 *
 */
public class CameraTests {
	static final Point ZERO_POINT = new Point(0, 0, 0);

	/**
	 * Test method for
	 * {@link elements.Camera#constructRay(int, int, int, int)}.
	 * @throws Exception 
	 *//*
	@Test
	public void testConstructRay() throws Exception {
		Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0)).setVPDistance(10);
		//String badRay = "Bad ray";

		// ============ Equivalence Partitions Tests ==============
		// EP01: 4X4 Inside (1,1)
		assertEquals(new Ray(ZERO_POINT, new Vector(1, -1, -10)),
				camera.setVPSize(8, 8).constructRay(4, 4, 1, 1), "badRay a");

		// =============== Boundary Values Tests ==================
		// BV01: 3X3 Center (1,1)
		assertEquals(new Ray(ZERO_POINT, new Vector(0, 0, -10)),
				camera.setVPSize(6, 6).constructRay(3, 3, 1, 1), "badRay b");

		// BV02: 3X3 Center of Upper Side (0,1)
		assertEquals(new Ray(ZERO_POINT, new Vector(0, -2, -10)),
				camera.setVPSize(6, 6).constructRay(3, 3, 1, 0), "badRay c");

		// BV03: 3X3 Center of Left Side (1,0)
		assertEquals(new Ray(ZERO_POINT, new Vector(2, 0, -10)),
				camera.setVPSize(6, 6).constructRay(3, 3, 0, 1), "badRay d");

		// BV04: 3X3 Corner (0,0)
		assertEquals(new Ray(ZERO_POINT, new Vector(2, -2, -10)),
				camera.setVPSize(6, 6).constructRay(3, 3, 0, 0), "badRay e");

		// BV05: 4X4 Corner (0,0)
		assertEquals(new Ray(ZERO_POINT, new Vector(3, -3, -10)),
				camera.setVPSize(8, 8).constructRay(4, 4, 0, 0), "badRay f");

		// BV06: 4X4 Side (0,1)
		assertEquals(new Ray(ZERO_POINT, new Vector(1, -3, -10)),
				camera.setVPSize(8, 8).constructRay(4, 4, 1, 0), "badRay g");

}*/

}

