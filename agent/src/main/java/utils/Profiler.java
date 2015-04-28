package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Profiler {
    public static void write(String fileName, String content) {
        FileWriter fileWriter = null;
        BufferedWriter bf = null;

        try {
            fileWriter = new FileWriter(fileName, true);
            bf = new BufferedWriter(fileWriter);
            bf.write(content + "\n");
            bf.close();
        }

        catch(IOException e) {
            System.out.println("PROFILER ERROR IN READING INPUT FILE!");
        }
    }
}
