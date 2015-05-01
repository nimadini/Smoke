package other;

import java.util.Arrays;

public class Stuff {
    public void run(int x) {
        if (x < 0) {
            System.out.println("x is negative");
            if (x < -10) {
                System.out.println("x is very negative");
            }
        }

        else {
            System.out.println("x is positive");
        }
    }

    public void run2() {
        int length = 100000000;
        int[] elems = new int[length];
        for (int i = 0; i < length; i++) {
            elems[i] = 100000000 - i;
        }
        Arrays.sort(elems);
    }
}
