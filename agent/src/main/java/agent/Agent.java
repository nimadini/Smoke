package agent;

import utils.ShutDownHook;

import java.io.File;
import java.lang.instrument.Instrumentation;

public class Agent {
    public static final String fileToWrite = "/Users/stanley/Desktop/REPO/mine.txt";

    public static void premain(String agentArgs, Instrumentation inst) {
        File f = new File(fileToWrite);

        if(f.exists() && !f.isDirectory()) {
            boolean status = f.delete();
            if (!status) {
                System.out.println("ERROR IN REMOVING EXISTING FILE!");
            }
        }

        ShutDownHook sdh = new ShutDownHook();
        sdh.attachShutDownHook();

        inst.addTransformer(new JavaAssistTransformer(inst, fileToWrite));
    }
}

