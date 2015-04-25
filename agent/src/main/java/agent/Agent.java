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
                // ASM Code
//                ClassReader reader = new ClassReader(bytes);
//                ClassWriter writer = new ClassWriter(reader, 0);
//                ClassPrinter visitor = new ClassPrinter(writer);
//                reader.accept(visitor, 0);
//                return writer.toByteArray();
// Javassist
                /*if ("other/Stuff".equals(s)) {
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        CtClass cc = cp.get("other.Stuff");
                        CtMethod m = cc.getDeclaredMethod("run");

                        ControlFlow cf = new ControlFlow(m);
                        ControlFlow.Block[] blocks = cf.basicBlocks();

                        int len = blocks.length;
                        System.out.println("LENGTH: " + len);
                        for (int i = 0; i < len; i++) {
                            ControlFlow.Block block = new ControlFlow(m).basicBlocks()[i]; //we have to re-evaluate the control flow every time we add new code
                            CodeIterator itr = m.getMethodInfo().getCodeAttribute().iterator();

                            int pos = block.position();
                            byte[] newCode=new byte[]{};
                            m.insertAt(pos, "System.out.println(\"Method Executed i\");");
                        }



                        m.addLocalVariable("blocksTracker", CtClass.);
//                        m.addLocalVariable("elapsedTime", CtClass.longType);
//                        m.insertBefore("elapsedTime = System.currentTimeMillis();");
//                        m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
//                                + "System.out.println(\"Method Executed in ms: \" + elapsedTime);}");
                        byte[] byteCode = cc.toBytecode();
                        cc.detach();
                        return byteCode;
                    } catch (BadBytecode | CannotCompileException | IOException | NotFoundException e) {
                        e.printStackTrace();
                    }
                }*/

                if ("TestStuff".equals(s)) {
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        CtClass cc = cp.get("TestStuff");
                        //CtMethod m = cc.getDeclaredMethod("t0");
                        cc.getMethods();
                        for (CtMethod m : cc.getDeclaredMethods()) {
                            String fileToWrite = "/Users/stanley/Desktop/REPO/mine.txt";
                            m.addLocalVariable("elapsedTime", CtClass.longType);
                            m.insertBefore("elapsedTime = System.currentTimeMillis();");
                            m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                                + "System.out.println(\"" + m.getLongName() + ":\" + elapsedTime + \"|4:1,2,3,4\");}");

                            String content = m.getLongName() + ": \" + elapsedTime +  \"";
                            m.insertAfter("{ utils.Profiler.write(\"" + fileToWrite + "\", \"" + content + "\"); }");
                            //byte[] byteCode = cc.toBytecode();

                            InstructionPrinter.print(m, System.err);
                        }

//                        ControlFlow cf = new ControlFlow(m);
//                        ControlFlow.Block[] blocks = cf.basicBlocks();
//
//                        m.addLocalVariable("elapsedTime", CtClass.longType);
//                        m.insertBefore("elapsedTime = System.currentTimeMillis();");
//                        m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
//                                + "System.out.println(\"" + m.getLongName() + ":\" + elapsedTime + \"|4:1,2,3,4\");}");

                        cc.detach();
                        return null;
                    }
                    catch ( CannotCompileException  | NotFoundException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        });
    }
}

