package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TestCase {
    private String longName;
    private Map<Method, Set<Integer>> methodToBlocksCovered;
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

    public void addMethod(String name, int totalBlocks) {
        Method m = new Method(name, totalBlocks);
        if (!methodToBlocksCovered.containsKey(m)) {
            methodToBlocksCovered.put(m, new HashSet<Integer>());
        }
    }

    public void addMethod(Method m) {
        if (!methodToBlocksCovered.containsKey(m)) {
            methodToBlocksCovered.put(m, new HashSet<Integer>());
        }
    }

    public Set<Integer> getCoveredBlocksByMethod(Method m) {
        if (!methodToBlocksCovered.containsKey(m))
            return null;

        return methodToBlocksCovered.get(m);
    }

    public Set<Integer> getCoveredBlocksByMethodName(String name) {
        if (name == null)
            return null;

        Method m = new Method(name);

        if (!methodToBlocksCovered.containsKey(m))
            return null;

        return methodToBlocksCovered.get(m);
    }

    public boolean isBlockCoveredInMethod(Method m, int blkIdx) {
        Set<Integer> blocksCovered = getCoveredBlocksByMethod(m);
        if (blocksCovered == null) {
            return false;
        }

        return blocksCovered.contains(blkIdx);
    }

    public boolean addCoveredBlock(Method m, int blkIdx) {
        if (!methodToBlocksCovered.containsKey(m))
            return false;

        Set<Integer> method = methodToBlocksCovered.get(m);
        method.add(blkIdx);
        return true;
    }

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