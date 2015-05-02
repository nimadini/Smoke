package utils;

import java.util.*;

public class StatementCoverage {
    private static StatementCoverage sc = new StatementCoverage();
    private final Set<TestCase> testCases;

    private StatementCoverage() {
        testCases = new HashSet<TestCase>();
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

    public TestCase getTestCaseByName(String name) {
        if (name == null)
            return null;

        TestCase targetTC = new TestCase(name);
        for (TestCase tc : testCases) {
            if (tc.equals(targetTC)) {
                return tc;
            }
        }
        return null;
    }
}