package agent;

import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.analysis.ControlFlow;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class JavaAssistTransformer implements ClassFileTransformer {
    /**
     * logger object is used for logging especially to keep track of the error causes
     */
    private static final Logger logger = Logger.getLogger(JavaAssistTransformer.class);

    /**
     * Javassist ClassPool object for loading and instrumenting classes
     */
    protected ClassPool classPool;

    /**
     * the java instrumentation object
     */
    protected Instrumentation instrumentation = null;

    /**
     * contains the name of instrumented classes to avoid multiple instrumentation of a class
     */
    protected Set<String> instrumentedClasses = new HashSet<String>();

    /**
     * holds the list of class prefixes that should not be instrumented
     */
    protected Set<String> classesToSkip = new HashSet<String>();

    /**
     * the file path to write the analysis result
     */
    protected String fileToWrite = null;

    /**
     * This method detects whether a class has been instrumented before
     * @param className     The name of the loaded class
     * @return              true if the class has already been instrumented and false otherwise
     */
    private boolean isClassInSkippedSet(String className) {
        for (String classToSkip : classesToSkip) {
            if (className.startsWith(classToSkip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new JavaAssistTransformer and sets up the Javassist environment
     *
     * @param instrumentation           The Java Instrumentation object to which we
     *                                  add ourselves as a transformer
     */
    public JavaAssistTransformer(Instrumentation instrumentation, String fileToWrite) {
        // set the instrumentation object
        this.instrumentation = instrumentation;

        // creates a ClassPool by searching the system search path, which
        // includes the platform library, extension libraries, and CLASSPATH
        this.classPool = ClassPool.getDefault();

        // set the list of class prefixes to skip
        this.classesToSkip.add("sun.");
        this.classesToSkip.add("com.sun.");
        this.classesToSkip.add("org.apache.");
        this.classesToSkip.add("javax.");
        this.classesToSkip.add("java.");

        // set the file path
        this.fileToWrite = fileToWrite;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        /*
         * the className string contains slashes instead of periods for addressing a class
         * replacing the slashes with dots makes the stack trace more human readable
         */
        String dotEncodedClassName = className.replace('/', '.');

        if (!instrumentedClasses.add(dotEncodedClassName)) {
            return null; // we don't want to instrument a class twice!
        }

        if (isClassInSkippedSet(dotEncodedClassName)) {
            return null; // we don't want to instrument undesired classes!
        }

        if ("TestStuff".equals(dotEncodedClassName)) {
            try {
                CtClass cc = classPool.get("TestStuff");
                if (cc.isFrozen()) {
                    return null; // the class cannot be modified anymore
                }
                //CtMethod m = cc.getDeclaredMethod("t0");
                //System.out.println("cc.getDeclaredMethods(): " + cc.getDeclaredMethods().length);

                for (CtMethod m : cc.getDeclaredMethods()) {
                    /*m.instrument(
                            new ExprEditor() {
                                public void edit(MethodCall m)
                                        throws CannotCompileException
                                {
                                    System.out.println(m.getClassName() + "." + m.getMethodName() + " " + m.getSignature());
                                }
                            });*/

                    m.addLocalVariable("elapsedTime", CtClass.longType);
                    m.insertBefore("elapsedTime = System.currentTimeMillis();");
                    m.insertAfter("elapsedTime = System.currentTimeMillis() - elapsedTime;");

                    String content = m.getLongName() + ": \" + elapsedTime +  \"";
                    m.insertAfter("{ utils.Profiler.write(\"" + fileToWrite + "\", \"" + content + "\"); }");
                    m.insertAfter("{ utils.StatementCoverage.getStatementCoverage().addElem(\"" + content + "\"); }");

                    //InstructionPrinter.print(m, System.err);
                }

//                        ControlFlow cf = new ControlFlow(m);
//                        ControlFlow.Block[] blocks = cf.basicBlocks();

                byte[] byteCode = cc.toBytecode();
                cc.detach(); // TODO: should be moved outside when you changed this stupid if else structure
                logger.info("successful instrumentation of class: " + className);
                return byteCode;
            }
            catch(IOException | NotFoundException | CannotCompileException e) {
                logger.error(e.getMessage() + "\ntransforming class: " + className + "; returning un-instrumented class", e);
            }
            catch(Exception e) {
                logger.error("Unexpected error occurred: " + e.getMessage(), e);
            }
        }
        else if ("other.Stuff".equals(dotEncodedClassName)) {
            try {
                CtClass cc = classPool.get("other.Stuff");
                if (cc.isFrozen()) {
                    return null; // the class cannot be modified anymore
                }
                //CtMethod m = cc.getDeclaredMethod("t0");
                //System.out.println("cc.getDeclaredMethods(): " + cc.getDeclaredMethods().length);

                for (CtMethod m : cc.getDeclaredMethods()) {
                    ControlFlow cf = new ControlFlow(m);
                    ControlFlow.Block[] blocks = cf.basicBlocks();



                    for (int i = 0; i < blocks.length; i++) {
                        m.addLocalVariable("b" + i, CtClass.booleanType);
                    }





                    //InstructionPrinter.print(m, System.err);
                }

                byte[] byteCode = cc.toBytecode();
                cc.detach(); // TODO: should be moved outside when you changed this stupid if else structure
                logger.info("successful instrumentation of class: " + className);
                return byteCode;
            }
            catch(IOException | NotFoundException | CannotCompileException | BadBytecode e) {
                System.out.println("Whooops!");
            }
        }

        return null;
    }
}
