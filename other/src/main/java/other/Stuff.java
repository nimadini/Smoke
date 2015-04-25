package other;

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

    public int run2(int x) {
        for (int i = 0; i < 20000; i++) {
            for (int j = 0; j < 10000; j++) {
                for (int k = 0; k < 10000; k++) {
                    x++;
                }
            }
        }
        if (x == 0) {
            System.out.println("");
        }

        return x;
    }
}
