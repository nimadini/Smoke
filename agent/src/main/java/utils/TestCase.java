package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A testcase api for our analysis purposes.
 *
 * Created by Nima Dini | April 2015
 */

public final class TestCase {
    /**
     * the test method's long name, including the package prefix
     */
    private String longName;

    /**
     * a map from the methods that are being called in this test method to
     * the blocks that are covered in those if this testcase is getting executed
     * All the method calls, directly or with any number of indirection, count!
     */
    private Map<Method, Set<Integer>> methodToBlocksCovered;

    /**
     * the execution time of this test method
     */
    private long executionTime;

    public TestCase(String completeName) {
        this.longName = completeName;
        this.methodToBlocksCovered = new HashMap<Method, Set<Integer>>();
    }

    public void setExecutionTime(long eTime) {
        this.executionTime = eTime;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public String getParentClassName() {
        return null;
    }

    public void setLongName(String name) {
        longName = name;
    }

    public String getLongName() {
        return longName;
    }

    /**
     * adds a method that is being called in this testcase
     * @param name          The long method name of the callee
     * @param totalBlocks   Total number of blocks in the callee method
     */
    public void addMethod(String name, int totalBlocks) {
        Method m = new Method(name, totalBlocks);
        if (!methodToBlocksCovered.containsKey(m)) {
            methodToBlocksCovered.put(m, new HashSet<Integer>());
        }
    }

    /**
     * adds a method that is being called in this testcase
     * @param m             The desired method
     */
    public void addMethod(Method m) {
        if (!methodToBlocksCovered.containsKey(m)) {
            methodToBlocksCovered.put(m, new HashSet<Integer>());
        }
    }

    /**
     * returns the set of blocks of method m that are being covered by this testcase
     * @param m             The desired method
     *
     * @return              the set of blocks if the method is covered by this test case
     *                      or null otherwise.
     */
    public Set<Integer> getCoveredBlocksByMethod(Method m) {
        if (!methodToBlocksCovered.containsKey(m))
            return null;

        return methodToBlocksCovered.get(m);
    }

    /**
     * returns the set of blocks of method m that are being covered by this testcase
     * @param name          The long name of the desired method
     *
     * @return              the set of blocks if the method is covered by this test case
     *                      or null otherwise.
     */
    public Set<Integer> getCoveredBlocksByMethodName(String name) {
        if (name == null)
            return null;

        Method m = new Method(name);

        if (!methodToBlocksCovered.containsKey(m))
            return null;

        return methodToBlocksCovered.get(m);
    }

    /**
     * whether a block of method m is being covered by calling this testcase
     * @param m             The method under test
     * @param blkIdx        The block of interest
     *
     * @return              true if the block is being covered or false otherwise
     */
    public boolean isBlockCoveredInMethod(Method m, int blkIdx) {
        Set<Integer> blocksCovered = getCoveredBlocksByMethod(m);
        if (blocksCovered == null) {
            return false;
        }

        return blocksCovered.contains(blkIdx);
    }

    /**
     * add a block that this test covers
     * @param m             The method which owns the block
     * @param blkIdx        The block that is getting covered
     *
     * @return              true if the operation is successful and false if the
     *                      method is not being covered with this testcase at all!
     */
    public boolean addCoveredBlock(Method m, int blkIdx) {
        if (!methodToBlocksCovered.containsKey(m))
            return false;

        Set<Integer> method = methodToBlocksCovered.get(m);
        method.add(blkIdx);
        return true;
    }

    /**
     * retrieve the methods that are being covered by this testcase
     *
     * @return              the set of methods that this testcase covers.
     *                      both direct and indirect method calls count!
     */
    public Set<Method> getCoveredMethods() {
        Set<Method> methods = new HashSet<Method>();
        for (Method m : methodToBlocksCovered.keySet()) {
            methods.add(m);
        }
        return methods;
    }

    @Override
    public String toString() {
        String res = "Test method name: " + longName + "| Time: " + executionTime + "\n";
        for (Method m : methodToBlocksCovered.keySet()) {
            res = res + m.getMethodName() + " | total blocks: " + m.getTotalBlocks() + " | covered: " + methodToBlocksCovered.get(m) + "\n";
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(TestCase.class))
            return false;

        TestCase tc = (TestCase) o;
        return tc.getLongName().equals(this.getLongName());
    }

    @Override
    public int hashCode() {
        return longName.hashCode();
    }
}