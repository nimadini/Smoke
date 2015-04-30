package utils;

import java.util.Set;

public final class Method {
    private String methodName;
    private int totalBlocks;

    public String getMethodName() {
        return methodName;
    }

    public int getTotalBlocks() {
        return totalBlocks;
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
