package utils;

import org.apache.commons.math3.linear.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * In this Util class we implement a TestSuiteCutter which works like
 * a knife to cut down the size of case cases.
 * <p/>
 * Given a finite set of blocks X and m subsets S1,S2, ...,Sm covering
 * these blocks, the minimum set-cover problem is to find the fewest
 * number of these subsets needed to cover as many blocks as it can
 * possibly cover.
 *
 * The greedy approximation algorithm is based on:
 * (*) Chvatal, Vasek. "A greedy heuristic for the set-covering problem."
 * Mathematics of operations research 4.3 (1979): 233-235.
 *
 * <p/>
 * (In general, the problem of selecting a minimal cardinality subset
 * of T that covers all the requirements covered by T is NP complete.)
 * <p/>
 * Created by ChenguangLiu on 4/27/15.
 */
public class TestSuiteCutter {
    static HashSet<Integer> jset;
    public static int UNINITIALIZED = -99;
    public static List<Integer> findCoverWithGreedy(Array2DRowRealMatrix matrix){
        /*
         *  Input Matrix:
         *   Column: Block identifiers;
          *  Row:   Test case identifiers;
          *  Element: 1 for cover; 0 for no cover; (set to integer to get extensibility for cost)
          * */
        /* Step 0: Set J' to empty */
        if(null == jset){
            HashSet<Integer> jset = new HashSet<Integer>();
        }else{
            jset.clear();
        }
        /* Step 1: If requirementVector is fulfilled then stop. Otherwise find a set covers Pk and maximizing the ratio Pj/cj */
        RealVector reqVector = maxCoverage(matrix);
        RealVector realVector_copy = reqVector.copy();


        while(containsNonZeroElement(reqVector)){
            int bestCoverageCase = UNINITIALIZED;
            int bestCoverageCount = 0;
            for(int testCaseNum=0; testCaseNum<matrix.getRowDimension(); testCaseNum++){
                if (jset.contains(testCaseNum)){
                    /* Omit the case already in the solution set */
                    continue;
                }else{
                    if(UNINITIALIZED == bestCoverageCase){
                        /* initialize the bestCoverageCase if it hasn't been initialized yet */
                        bestCoverageCase = testCaseNum;
                        bestCoverageCount = (int)coverage(matrix.getRowVector(testCaseNum),reqVector);
                    }
                }
                /* Find the best coverage */
                int thisCaseCoverageCount = (int)coverage(matrix.getRowVector(testCaseNum),reqVector);
                if (thisCaseCoverageCount >= bestCoverageCount){
                    /* if the current case is no worse than the previous bestCoverageCase,
                    * then set the bestCoverageCase to current case and assign the bestCoverageCount
                    * */
                    bestCoverageCase = testCaseNum;
                    bestCoverageCount = thisCaseCoverageCount;
                }
            }
            if (UNINITIALIZED == bestCoverageCase){
                System.out.println("ERROR: unable to initialize a best coverage case.");
            }else {
//                coverAndReduceTheRequirementVector();
            }
        }

        return null;
    }

    public static RealVector maxCoverage(Array2DRowRealMatrix matrix){
        System.out.println("Step 0: input matrix\n " + matrix);
        /* Step 1: create a [jx1] vector */
        Array2DRowRealMatrix covVector = new Array2DRowRealMatrix( matrix.getRowDimension(), 1);
        covVector = (Array2DRowRealMatrix)covVector.scalarAdd(1);
        System.out.println("\n Step 1: create a [jx1] vector\n " + covVector);
        /* Step 2: get the transposed matrix */
        Array2DRowRealMatrix tranMatrix = (Array2DRowRealMatrix)matrix.transpose();
        System.out.println("\n Step 2: get the transposed matrix \n" + tranMatrix);
        /* Step 3: multiply the [ixj] matrix with the [jx1] vector */
        Array2DRowRealMatrix resVector = tranMatrix.multiply(covVector);
                System.out.println("\n Step 3: multiply the [ixj] matrix with the [jx1] \n" + resVector);
        /* Step 4(obsolete); transpose the resVector and Return the [1xi] matrix as vector*/
//        resVector = (Array2DRowRealMatrix) resVector.transpose();
//        System.out.println("\n Step 4; transpose the resVector\n" + resVector + "\n");
        /* Step 5: standardize to 1 by walkInOptimizedOrder and return the [1xj] matrix as vector*/
        resVector.walkInOptimizedOrder(new StandardizeVisitor());
        RealVector res = resVector.getColumnVector(0);
        return res;
    }

    /**
     * Test if the vector contains non-zero value(s).
     * */
    public static boolean containsNonZeroElement(RealVector matrix){
        TestZeroVisitor zeroVisitor = new TestZeroVisitor();
        zeroVisitor.containsNonzero = false;
        matrix.walkInOptimizedOrder(zeroVisitor);
        return (zeroVisitor.containsNonzero);
    }

    /**
     * Check how many blocks does the test case[1xj] cover with a given BlockCoverageVector[jx1].
     * Returns the number of blocks it covers.
     * */
    public static double coverage (RealVector testcase, RealVector reqBlocks){
        return testcase.dotProduct(reqBlocks);
    }

    /**
     *  This method is used for cut the covered blocks from the requirement vector.
     *  Flip the coverage vector, and multiply it with the requirement vector
     */
    public static RealVector coverAndReduceTheRequirementVector(RealVector coverageVector, RealVector reqBlocks){
        /*flip the coverage vector*/
        coverageVector.walkInOptimizedOrder(new FlipVisitor());
        return reqBlocks.ebeMultiply(coverageVector);
    }

    /**
     * A Changing visitor to standardize the matrix to 0s and 1s.
     */
    static class StandardizeVisitor extends DefaultRealMatrixChangingVisitor {
        @Override
        public double visit(int row, int column, double value){
            if(value!=0)
                return 1;
            else return 0;
        }
    }

    /**
    *   A Preserving visitor to test if the vector only contains zero elements.
    * */
    static class TestZeroVisitor implements RealVectorPreservingVisitor {
        public static boolean containsNonzero = false;

        @Override
        public void start(int i, int i1, int i2) {

        }

        @Override
        public void visit(int i, double v) {
            if(v!=0)
                containsNonzero = true;
        }

        @Override
        public double end() {
            return 0;
        }
    }

    /**
     * A Changing visitor to flip a vector(0 to 1 and 1 to 0)
     * */
    static class FlipVisitor implements RealVectorChangingVisitor{

        @Override
        public void start(int i, int i1, int i2) {

        }

        @Override
        public double visit(int i, double v) {
            if(0==v){
                return 1;
            }else{
                return 0;
            }
        }

        @Override
        public double end() {
            return 0;
        }
    }


}
