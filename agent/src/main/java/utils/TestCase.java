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

    public String getLongName() {
        return longName;
    }

    public void addMethod(String name, int totalBlocks) {
        Method m = new Method(name, totalBlocks);
        if (!methodToBlocksCovered.containsKey(m)) {
            methodToBlocksCovered.put(m, new HashSet<Integer>());
        }
    }

    @Override
    public String toString() {
        String res = "Test method name: " + longName + "\n";
        for (Method m : methodToBlocksCovered.keySet()) {
            res = res + m.getMethodName() + " | " + m.getTotalBlocks() + "\n";
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
