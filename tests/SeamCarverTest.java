import edu.princeton.cs.algs4.Picture;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SeamCarverTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void Throw_When_Picture_Is_Null() {
        thrown.expect(IllegalArgumentException.class);
        new SeamCarver(null);
    }

    @Test
    public void CalculateEnergy_boreders() {
        Picture picture = new Picture("3x4.png");
        SeamCarver carver = new SeamCarver(picture);

        assertEquals(1000, carver.energy(0, 0), 0.01);
        assertEquals(1000, carver.energy(0, 1), 0.01);
        assertEquals(1000, carver.energy(0, 2), 0.01);
        assertEquals(1000, carver.energy(0, 3), 0.01);

        assertEquals(1000, carver.energy(1, 0), 0.01);
        assertEquals(1000, carver.energy(1, 3), 0.01);

        assertEquals(1000, carver.energy(2, 0), 0.01);
        assertEquals(1000, carver.energy(2, 1), 0.01);
        assertEquals(1000, carver.energy(2, 2), 0.01);
        assertEquals(1000, carver.energy(2, 3), 0.01);
    }

    @Test
    public void CalculateEnergy() {
        Picture picture = new Picture("3x4.png");
        SeamCarver carver = new SeamCarver(picture);

        assertEquals(Math.sqrt(52024), carver.energy(1, 2), 0.01);
        assertEquals(Math.sqrt(52225), carver.energy(1, 1), 0.01);
    }

    @Test
    public void findVerticalSeam_6x5() {
        Picture picture = new Picture("6x5.png");
        SeamCarver carver = new SeamCarver(picture);

        assertArrayEquals(new int[]{3, 4, 3, 2, 2}, carver.findVerticalSeam());
    }
    @Test
    public void findHorizontalSeam_6x5() {
        Picture picture = new Picture("6x5.png");
        SeamCarver carver = new SeamCarver(picture);

        assertArrayEquals(new int[]{2, 2, 1, 2, 1, 2}, carver.findHorizontalSeam());
    }
}
