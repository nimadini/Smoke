package other.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestSLList {
    @Test
    public void test0() {
        SLList l = new SLList();
        l.addFront(1);
        assertEquals(l.getSize(), 1);
    }

    @Test
    public void test1() {
        SLList l = new SLList();
        l.addNode(0, 10);
        assertEquals(l.getSize(), 1);
    }

    @Test
    public void test2() {
        SLList l = new SLList();
        l.addFront(1);
        l.addFront(1);

        assertEquals(l.countSameElems(1), 2);
    }
}
