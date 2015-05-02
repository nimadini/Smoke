import org.junit.Test;
import other.Stuff;

public class TestStuff {
    @Test
    public void t0() {
        Stuff stuff = new Stuff();
        stuff.run(-1);
        stuff.run2();
    }

    /*@Test
    public void t1() {
        Stuff stuff = new Stuff();
        stuff.run(10);
    }

    @Test
    public void t2() {
        Stuff stuff = new Stuff();
        stuff.run(-15);
        stuff.run(10);
    }*/

    @Test
    public void t3() {
        Stuff stuff = new Stuff();
        stuff.run2();
    }

    @Test
    public void t4() {
        Stuff stuff = new Stuff();
        stuff.run2();
    }

    @Test
    public void t5() {
        Stuff stuff = new Stuff();
        stuff.run3();
    }
}
