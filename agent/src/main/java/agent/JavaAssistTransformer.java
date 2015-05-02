package agent;

import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.analysis.ControlFlow;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import org.apache.log4j.Logger;
import utils.StatementCoverage;
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
    public JavaAssistTransformer(Instrumentation instrumentation) {
        // set the instrumentation object
        this.instrumentation = instrumentation;

        // creates a ClassPool by searching the system search path, which
        // includes the platform library, extension libraries, and CLASSPATH
        this.classPool = ClassPool.getDefault();

        // set the list of class prefixes to skip
        this.classesToSkip.add("sun.");
        this.classesToSkip.add("com.sun.");
        this.classesToSkip.add("org.apache.");
        this.classesToSkip.add("org.junit.");
        this.classesToSkip.add("org.hamcrest.");
        this.classesToSkip.add("junit.");
        this.classesToSkip.add("javax.");
        this.classesToSkip.add("java.");
        this.classesToSkip.add("utils.");
        this.classesToSkip.add("jxl.");
    }

    private void testMethodInstrumentation(CtMethod m) throws CannotCompileException {
        // add m to the list of test cases
        StatementCoverage.getStatementCoverage().addTestCase(m.getLongName());

        // instrument the test method to find its execution time
        m.addLocalVariable("elapsedTime", CtClass.longType);
        m.insertBefore("elapsedTime = System.currentTimeMillis();");

        m.insertAfter("elapsedTime = System.currentTimeMillis() - elapsedTime;");

        // store the execution time of test case in the data structure we have
        m.insertAfter("utils.StatementCoverage.getStatementCoverage().getTestCaseByName(\"" + m.getLongName() + "\").setExecutionTime(elapsedTime);");
    }

    private void methodUnderTestInstrumentation(CtClass cc, CtMethod m, Javac javac)
            throws CannotCompileException, CompileError, BadBytecode {

        // if the caller is one of our test cases, add this method name to the map<testcase, methods>
        // the goal is to keep track of which methods are being called in each testcase
        ControlFlow.Block[] blocks = new ControlFlow(m).basicBlocks();
        int blockSize = blocks.length;
        String code = "" +
                "String callerTestCaseName = utils.Utility.getTestCaseCaller(new Exception());\n" +
                "if (callerTestCaseName != null) {\n" +
                "   utils.StatementCoverage.getStatementCoverage().setCurrentBlockMetaInfo(callerTestCaseName, \"" + m.getLongName() + "\"," + blockSize + ");\n" +
                "}\n";

        m.insertBefore(code);

        // add inst to beginning of each basic block
        // for each test case find the blocks that are being executed!

        for (int i = 0; i < blockSize; i++) {
            ControlFlow.Block blk = new ControlFlow(m).basicBlocks()[i];
            int pos = blk.position(); // bytecode line number
            m.insertAt(m.getMethodInfo().getLineNumber(pos), "utils.StatementCoverage.getStatementCoverage().addCurrentBlockMetaInfo(" + i + ");");
        }
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

        try {
            CtClass cc = classPool.get(dotEncodedClassName);
            if (cc.isFrozen()) {
                return null; // the class cannot be modified anymore
            }
            Javac javac = new Javac(cc);

            // for each method declared in the class itself -> not the inherited methods
            // inherited methods would be instrumented in separately in their corresponding class
            for (CtMethod m : cc.getDeclaredMethods()) {
                if (m.getAnnotation(org.junit.Test.class) != null) { // if m is a test method
                    testMethodInstrumentation(m);
                }
                else { // if m is an actual method
                    methodUnderTestInstrumentation(cc, m, javac);
                }
            }

            byte[] byteCode = cc.toBytecode();
            cc.detach();
            return byteCode;
        }
        catch(IOException | NotFoundException | CannotCompileException | CompileError e) {
            e.printStackTrace();
            System.out.println("XEEEEEEEE");
            logger.error(e.getMessage() + "\ntransforming class: " + className + "; returning un-instrumented class", e);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("XEEEEEEEE2");
            logger.error("Unexpected error occurred: " + e.getMessage(), e);
        }

        return null;
    }
}
