package agent;

import utils.ShutDownHook;
import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        ShutDownHook sdh = new ShutDownHook();
        sdh.attachShutDownHook();
        inst.addTransformer(new JavaAssistTransformer(inst));
    }
}

