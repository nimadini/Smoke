package utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.*;

public class StatementCoverage {
    private static StatementCoverage sc = new StatementCoverage();
    private final Set<TestCase> testCases;
    private final Set<Method> allCoveredMethods;

    private StatementCoverage() {
        testCases = new HashSet<TestCase>();
        allCoveredMethods = new HashSet<Method>();
    }

    public static StatementCoverage getStatementCoverage() {
        return sc;
    }

    public static int counter = 0;

    public boolean addBlockToCoveredSet(String testCaseLongName, String methodLongName, int blockIdx) {
        //System.out.println("#######" + testCaseLongName + " " + methodLongName + " " + blockIdx);
        TestCase tc = getTestCaseByName(testCaseLongName);
        if (tc == null)
            return false;

        return tc.addCoveredBlock(new Method(methodLongName), blockIdx);
        //return true;
    }

    public void addTestCase(String completeName) {
        testCases.add(new TestCase(completeName));
    }

    public boolean isTestCase(String name) {
        return testCases.contains(new TestCase(name));
    }

    public void print() {
        System.out.println("Starting to Print:");
        for (TestCase tc : testCases) {
            System.out.println(tc);
        }
    }

    public void something(int i) {
        System.out.println("#: " + i);
    }

    public Set<Integer> currentBlockMetaInfo = null;

    public void setCurrentBlockMetaInfo(String testCaseName, String methodName, int blockSize) {
        TestCase t = getTestCaseByName(testCaseName);
        if (t == null) {
            currentBlockMetaInfo = null;
            return;
        }

        Method m = new Method(methodName, blockSize);
        allCoveredMethods.add(m);
        t.addMethod(m);

        currentBlockMetaInfo = t.getCoveredBlocksByMethodName(methodName);
    }

    public void addCurrentBlockMetaInfo(int i) {
        if (currentBlockMetaInfo != null) {
            currentBlockMetaInfo.add(i);
        }
    }

    private static final TestCase dummyTC = new TestCase("#");

    public TestCase getTestCaseByName(String name) {
        if (name == null)
            return null;

        dummyTC.setLongName(name);
        for (TestCase tc : testCases) {
            if (tc.equals(dummyTC)) {
                return tc;
            }
        }
        return null;
    }

    public Array2DRowRealMatrix genMatrix() {
        int colDim = 0;
        Map<Method, Integer> methodToBlockOffset = new HashMap<Method, Integer>();

        for (Method m : allCoveredMethods) {
            methodToBlockOffset.put(m, colDim);
            colDim += m.getTotalBlocks();
        }

        Array2DRowRealMatrix mat = new Array2DRowRealMatrix(testCases.size(), colDim);
        //TestCase[] testCasesArray = testCases.toArray();
        TestCase[] testCasesArray = new TestCase[testCases.size()];

        int l = 0;
        for (TestCase tc : testCases) {
            testCasesArray[l] = tc;
            l++;
        }

        for (int i = 0; i < testCasesArray.length; i++) {
            for (Method m : testCasesArray[i].getCoveredMethods()) {
                int baseIdx = methodToBlockOffset.get(m);
                for (int j = 0; j < m.getTotalBlocks(); j++) {
                    if (testCasesArray[i].isBlockCoveredInMethod(m, j)) {
                        mat.setEntry(i, baseIdx + j, 1);
                    }
                }
            }
        }

        matrixPrettyPrint(mat, testCasesArray, methodToBlockOffset);
        return mat;
    }

    public void matrixPrettyPrint(Array2DRowRealMatrix mat, TestCase[] testCases, Map<Method, Integer> methodToBlockOffset) {
        for (int i = 0; i < mat.getRowDimension(); i++) {
            for (int j = 0; j < mat.getColumnDimension(); j++) {
                System.out.print((int) mat.getEntry(i, j) + " ");
            }
            System.out.println();
        }

        for (TestCase testCase : testCases) {
            System.out.println(testCase.getLongName());
        }

        for (Method m : methodToBlockOffset.keySet()) {
            System.out.println(m.getMethodName() + " " + methodToBlockOffset.get(m));
        }
    }
}