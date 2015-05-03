package other.utils;

public class SLList {
    private int size;
    private Node header;

    public SLList() {
        header = new Node();
        this.size = 0;
    }

    public void addFront(int elem) {
        addNode(0, elem);
    }

    public int getSize() {
        return this.size;
    }

    public void addNode(int idx, int elem) {
        if (idx < 0) {
            throw new IllegalArgumentException();
        }

        Node cur = header;

        for (int i = 0; i < idx; i++) {
            cur = cur.next;
            if (cur == null) {
                throw new RuntimeException("index out of range!");
            }
        }

        Node newNode = new Node(elem);
        newNode.next = cur.next;

        if (cur.next != null) {
            cur.next.prev = newNode;
        }

        newNode.prev = cur;
        cur.next = newNode;

        this.size++;
    }

    public int countSameElems(int elem) {
        int count = 0;
        Node cur = header.next;
        while (cur != null) {
            if (cur.elem == elem) {
                count++;
            }
            cur = cur.next;
        }

        return count;
    }

    public String prettyList() {
        String res = "";
        Node cur = header.next;

        while (cur != null) {
            res = res + cur.elem + " ";
            cur = cur.next;
        }

        return res;
    }

    public void reverse() {
        Node cur = header.next;
        Node prev = null;

        while (cur != null) {
            cur.prev = cur.next;
            cur.next = prev;
            prev = cur;
            cur = cur.prev;
        }

        if (prev != null) {
            prev.prev = this.header;
        }

        this.header.next = prev;
    }
}

class Node {
    Node next;
    Node prev;
    int elem;

    public Node(int elem) {
        this.elem = elem;
    }

    public Node() {}
}
