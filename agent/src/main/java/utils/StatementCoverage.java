package utils;

import java.util.*;

public class StatementCoverage {
    private static StatementCoverage sc = new StatementCoverage();
    private Set<TestCase> allTestCases;

    private StatementCoverage() {
        tcToMethods = new HashMap<TestCase, Set<Method>>();
        allTestCases = new HashSet<TestCase>();
    }

    public static StatementCoverage getStatementCoverage() {
        return sc;
    }

    private final Set<String> elems = new HashSet<String>();

    private Map<TestCase, Set<Method>> tcToMethods;

    public void addElem(String elem) {
        elems.add(elem);
    }

    public Set<String> getElems() {
        return elems;
    }

    public Set<TestCase> getAllTestCases() { return allTestCases; }

    public void addTestCase(String completeName) {
        /*if (tcToBlkSet.containsKey(new TestCase(completeName))) {
            throw new RuntimeException(); // TODO
        }

        else {
            tcToBlkSet.put(new TestCase(completeName), new HashSet<Block>());
        }*/
        allTestCases.add(new TestCase(completeName));
    }

    public boolean isTestCase(String name) {
        return allTestCases.contains(new TestCase(name));
    }

    public void print() {
        System.out.println("Starting to Print:");
        for (TestCase tc : allTestCases) {
            System.out.println(tc);
        }
    }

    public TestCase getTestCaseByName(String name) {
        for (TestCase tc : allTestCases) {
            if (tc.equals(new TestCase(name))) {
                return tc;
            }
        }
        return null;
    }
}
