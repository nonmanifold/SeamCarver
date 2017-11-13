import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SeamCarverTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void Throw_When_Picture_Is_Null() {
        thrown.expect(IllegalArgumentException.class);
        new SeamCarver(null);
    }

}
