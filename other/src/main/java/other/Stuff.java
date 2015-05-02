package other;

import java.util.Arrays;

public class Stuff {
    public void run(int x) {
        if (x < 0) {
            System.out.println("x is negative");
            if (x < -10) {
                System.out.println("x is very negative");
            }
            int y = 10;
            int[] elems = new int[y];
            for (int i = 0; i < elems.length; i++) {
                elems[i] = 1;
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
            elems[i] = length - i;
        }
        Arrays.sort(elems);
    }

    public void run3() {
        run(5);
        run2();
    }

    public int run4(int x) {
        if (x > 0) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
