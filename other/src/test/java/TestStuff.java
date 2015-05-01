import org.junit.Test;
import other.Stuff;

public class TestStuff {
    @Test
    public void t0() {
        Stuff stuff = new Stuff();
        stuff.run(5);
        stuff.run2();
        stuff.run2();
        stuff.run2();
    }

    @Test
    public void t1() {
        Stuff stuff = new Stuff();
        stuff.run2();
    }

    @Test
    public void t2() {
        Stuff stuff = new Stuff();
        stuff.run2();
    }

    @Test
    public void t3() {
        Stuff stuff = new Stuff();
        stuff.run3();
    }
}
