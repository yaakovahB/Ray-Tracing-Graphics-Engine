package unitTests.primitives;
import primitives.Point;
import org.junit.Test;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Yaakovah, Meira, Tali
 *
 */
public class PointTests {

	/**
	 * Test method for {@link primitives.Point#add(primitives.Vector)}.
	 * @throws Exception 
	 */
	@Test
	public void testAdd() throws Exception {
		Point p = new Point(1,2,3);
		assert(p.add(new Vector(-1, -2, -3)).equals(new Point(0, 0, 0)));	
	}

	/**
	 * Test method for {@link primitives.Point#subtract(primitives.Point)}.
	 * @throws Exception 
	 */
	@Test
	public void testSubtract() throws Exception {
		Point p = new Point(1,2,3);
		assert(new Vector(1, 1, 1).equals(new Point(2, 3, 4).subtract(p)));
	}

	/**
	 * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
	 */
	@Test
	public void testDistanceSquared() {
		Point p = new Point(1,2,3);
		assertEquals(p.distanceSquared(new Point(1,2,3)),0);
		assertEquals(p.distanceSquared(new Point(3,6,7)),36);
	}

	/**
	 * Test method for {@link primitives.Point#distance(primitives.Point)}.
	 */
	@Test
	public void testDistance() {
		Point p = new Point(1,2,3);
		assertEquals(p.distance(new Point(1,2,3)),0);
		assertEquals(p.distance(new Point(3,6,7)),6);
	}

}
