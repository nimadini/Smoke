import org.junit.Test;
import other.Stuff;

public class TestStuff {
    @Test
    public void t0() {
        Stuff stuff = new Stuff();
        stuff.run2(5);
    }

    @Test
    public void t1() {
        Stuff stuff = new Stuff();
        stuff.run2(4);
    }
}
