package agent;

import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.analysis.ControlFlow;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
                if ("TestStuff".equals(s)) {
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        CtClass cc = cp.get("TestStuff");
                        //CtMethod m = cc.getDeclaredMethod("t0");
                        System.out.println("cc.getDeclaredMethods(): " + cc.getDeclaredMethods().length);
                        String fileToWrite = "/Users/stanley/Desktop/REPO/mine.txt";
                        for (CtMethod m : cc.getDeclaredMethods()) {
                            m.addLocalVariable("elapsedTime", CtClass.longType);
                            m.insertBefore("elapsedTime = System.currentTimeMillis();");
                            m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                                + "System.out.println(\"" + m.getLongName() + ":\" + elapsedTime + \"|4:1,2,3,4\");}");

                            String content = m.getLongName() + ": \" + elapsedTime +  \"";
                            m.insertAfter("{ utils.Profiler.write(\"" + fileToWrite + "\", \"" + content + "\"); }");

                            //InstructionPrinter.print(m, System.err);
                        }

//                        ControlFlow cf = new ControlFlow(m);
//                        ControlFlow.Block[] blocks = cf.basicBlocks();

                        byte[] byteCode = cc.toBytecode();
                        cc.detach();
                        return byteCode;
                    }
                    catch (IOException | CannotCompileException  | NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }
}

