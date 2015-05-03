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

        // skip up to idx
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
}

class Node {
    Node next;
    Node prev;
    int elem;

    public Node(int elem) {
        this.elem = elem;
    }

    public Node() {

    }
}
