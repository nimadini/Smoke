package utils;

/**
 * A method keeps required information for method under test.
 *
 * Created by Nima Dini | April 2015
 */

public final class Method {
    /**
     * the method long name, including the package prefix
     */
    private String methodName;

    /**
     * total number of basic blocks in the method
     */
    private int totalBlocks; // total number of basic blocks (from 0 to totalBlocks-1)

    public String getMethodName() {
        return methodName;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public Method(String name) {
        this.methodName = name;
    }

    public Method(String name, int totalBlocks) {
        this.methodName = name;
        this.totalBlocks = totalBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (!o.getClass().equals(this.getClass()))
            return false;

        Method m = (Method) o;
        return m.getMethodName().equals(this.methodName);
    }

    @Override
    public int hashCode() {
        return methodName.hashCode();
    }
}
