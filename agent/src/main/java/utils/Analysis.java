package utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.HashSet;
import java.util.Set;

public class Analysis {
    private TestCase[] testCases;
    private Array2DRowRealMatrix mat;
    private Set<TestCase> subSet;
    private double coverage;

    public Analysis(TestCase[] testCases, Array2DRowRealMatrix mat) {
        this.testCases = testCases;
        this.mat = mat;
        this.subSet = new HashSet<TestCase>();
        this.coverage = 0;
    }

    public TestCase[] getTestCases() {
        return testCases;
    }

    public Array2DRowRealMatrix getMatrix() {
        return mat;
    }

    public void setResult(Set<Integer> testCasesIdx) {
        Set<Integer> blocksCovered = new HashSet<Integer>();

        for (int i : testCasesIdx) {
            subSet.add(testCases[i]);
            for (int j = 0; j < mat.getColumnDimension(); j++) {
                int included = (int)(mat.getEntry(i, j));
                if (included == 1) {
                    blocksCovered.add(j);
                }
            }
        }

        if (mat.getColumnDimension() == 0) {
            return;
        }

        coverage = 1.0 * blocksCovered.size() / mat.getColumnDimension();
    }

    public double getCoverage() {
        return coverage;
    }

    public Set<TestCase> getSubSet() {
        return subSet;
    }

    public long getOriginalTotalExecTime() {
        long totalTime = 0;
        for (TestCase tc : testCases) {
            totalTime += tc.getExecutionTime();
        }
        return totalTime;
    }

    public long getSmokeTotalExecTime() {
        long totalTime = 0;
        for (TestCase tc : subSet) {
            totalTime += tc.getExecutionTime();
        }
        return totalTime;
    }
}

