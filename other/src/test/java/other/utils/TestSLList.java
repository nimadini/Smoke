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

    @Test (expected = RuntimeException.class)
    public void test3() {
        SLList l = new SLList();
        l.addNode(-1, 1);
    }

    @Test
    public void test4() {
        SLList l = new SLList();
        l.addFront(5);
        l.addFront(4);
        l.addNode(2, 4);
        assertEquals(l.countSameElems(4), 2);
        assertEquals(l.countSameElems(5), 1);
    }

    @Test
    public void test5() {
        SLList l = new SLList();
        l.addFront(5);
        l.addFront(4);
        l.addNode(2, 4);
        assertEquals(l.prettyList(), "4 5 4 ");
    }

    @Test
    public void test6() {
        SLList l = new SLList();
        l.addFront(4);
        l.addFront(5);
        l.addNode(2, 4);

        l.reverse();
        assertEquals(l.countSameElems(4), 2);
        assertEquals(l.countSameElems(5), 1);
        assertEquals(l.getSize(), 3);
    }

    @Test
    public void test7() {
        SLList l = new SLList();
        l.addFront(4);
        l.addFront(5);
        l.addNode(2, 4);

        assertEquals(l.prettyList(), "5 4 4 ");

        l.reverse();
        assertEquals(l.prettyList(), "4 4 5 ");

        l.reverse();
        assertEquals(l.prettyList(), "5 4 4 ");
    }
}
