package agent;

import utils.ShutDownHook;
import java.lang.instrument.Instrumentation;

/**
 * A java agent which instruments the client code and attaches a shutdown hook.
 * The hook will be executed then the JVM wants to shutdown on user's code and
 * that's where we have gathered all the required data to run our analysis.
 *
 * Created by Nima Dini | April 2015
 */

public class Agent {
    /**
     * JVM runs this method prior to running any of the clients code.
     * This is where all the static bytecode instrumentation happens!
     *
     * @param agentArgs     Agent options will be passed to agent
     * @param inst          The instrumentation instance
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        ShutDownHook sdh = new ShutDownHook();
        sdh.attachShutDownHook();
        inst.addTransformer(new JavaAssistTransformer(inst));
    }
}

