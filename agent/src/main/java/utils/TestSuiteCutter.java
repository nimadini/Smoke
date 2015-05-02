package utils;

import org.apache.commons.math3.linear.*;

import java.util.*;

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
    static HashSet<Integer> repSet;
    public static int UNINITIALIZED = -99;
    static HashSet<Integer> markedSet;     //only in HGS
    public static Set<Integer> findCoverWithGreedy(Array2DRowRealMatrix matrix){
        /*
         *  Input Matrix:
         *   Column: Block identifiers;
          *  Row:   Test case identifiers;
          *  Element: 1 for cover; 0 for no cover; (set to integer to get extensibility for cost)
          * */
        /* Step 0: Set repSet to empty */
        if(null == repSet){
            repSet = new HashSet<Integer>();
        }else{
            repSet.clear();
        }
        /* Step 1: If requirementVector is fulfilled then stop. Otherwise find a set covers Pk and maximizing the ratio Pj/cj */
        RealVector reqVector = maxCoverage(matrix);

        while(containsNonZeroElement(reqVector)){
            int bestCoverageCase = UNINITIALIZED;
            int bestCoverageCount = 0;
            for(int testCaseNum=0; testCaseNum<matrix.getRowDimension(); testCaseNum++){
                if (repSet.contains(testCaseNum)){
                    /* Omit the case already in the solution set */
                    continue;
                }else{
                    if(UNINITIALIZED == bestCoverageCase){
                        /* initialize the bestCoverageCase if it hasn't been initialized yet */
                        int thisCovCount = (int)coverage(matrix.getRowVector(testCaseNum),reqVector);
                        if (0 != thisCovCount){
                            bestCoverageCase = testCaseNum;
                            bestCoverageCount = thisCovCount;
                        }

                    }
                }
                /* Find the best coverage */
                int thisCaseCoverageCount = (int)coverage(matrix.getRowVector(testCaseNum),reqVector);
                if (thisCaseCoverageCount > bestCoverageCount){
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
                /* add the case to the solution set */
                repSet.add(bestCoverageCase);
                /* cut the covered blocks from the requirement vector */
                reqVector = coverAndReduceTheRequirementVector(matrix.getRowVector(bestCoverageCase), reqVector);
            }
        }

        return repSet;
    }



    /**
     * Returns the maximum coverage of a given matrix
     * */
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
        return resVector.getColumnVector(0);
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

    /**
     * Implementation of HGS starts here.
     * O(nt*MAX_CARD) -> O(n(n+nt)MAX_CARD)
     * */
    public static Set<Integer> findCoverWithHGS(Array2DRowRealMatrix matrix){
        /* Step 0: Set repSet to empty */
        if(null == repSet){
            repSet = new HashSet<Integer>();
        }else{
            repSet.clear();
        }
        if(null == markedSet){
            markedSet = new HashSet<Integer>();
        }else{
            markedSet.clear();
        }
        int MAX_CARD=getMaxCardinality(matrix);
        int CURRENT_CARD = 1;
        /* Step 1: find all test cases which has its covered requirement' cardinality as 1 */
        for(int i=0; i<matrix.getColumnDimension();i++){
            if (1 == getCardinality(matrix,i)){
                repSet.addAll(getTCListfromColumn(matrix,i));
            }
        }

        /* Mark all Ti containing elements in RS */
        for(int i=0; i<matrix.getColumnDimension();i++){
            if (!hasIntersection(getTCListfromColumn(matrix,i), repSet))
                markedSet.add(i);
        }

        System.out.println("Step 1: find all test cases which has its covered requirement' cardinality as 1\n repSet=" + repSet);
        /* Step 2:  */
        Set<Integer> listTC = new HashSet<>();
        while(CURRENT_CARD<=MAX_CARD){
            CURRENT_CARD++; /* Increment the current card */
            boolean existForCard = false;
            for(int i=0; i<matrix.getColumnDimension();i++){
                listTC.clear();
                if (!markedSet.contains(i) && (CURRENT_CARD == getCardinality(matrix,i))){
                    listTC.addAll(getTCListfromColumn(matrix,i));
                    existForCard = true;
                }
            }
            if (true == existForCard){
                int next_test = SelectTest(CURRENT_CARD, listTC, matrix, MAX_CARD);

                /* add this selected test into representative set */
                repSet.add(next_test);

                boolean may_reduce = false;

                /* mark Ti containing next_test*/
                for(int i=0; i<matrix.getColumnDimension();i++){
                    if (getTCListfromColumn(matrix,i).contains(next_test)){
                        markedSet.add(i);
                        if (MAX_CARD == getCardinality(matrix,i)) may_reduce = true;
                    }
                }

                /* try to reduce max_card */
                if(true == may_reduce){
                    int max = UNINITIALIZED;
                    for(int i=0; i<matrix.getColumnDimension();i++){
                        if (!markedSet.contains(i)){
                            int card = getCardinality(matrix, i);
                            if (card > max) max = card;
                        }
                    }
                    MAX_CARD = max;
                }


            }
        }
        return repSet;
    }

    /**
     * SelectTest function for HGS.
     * It's used to select the next test case to be included in the RepSet.
     * */
    private static int SelectTest(int current_card, Set<Integer> listTC,Array2DRowRealMatrix matrix,int max_card) {
        Map<Integer,Integer> counts = new HashMap<Integer, Integer>();
        /* keep track of the test that has the most */
        int maxCoverageCnt = UNINITIALIZED;

        /* foreach tc in LIST do compute COUNT[tc], the number of unmarked Tj’s of cardinality SIZE containing tc */
        for(Integer tcNum : listTC) {

            int tcCoverageCnt = 0;

            for (int colNum=0; colNum<matrix.getColumnDimension();colNum++){
                if (!markedSet.contains(colNum) && (0!=matrix.getEntry(tcNum,colNum))){
                    /* increment the count in map*/
                    if(counts.get(tcNum) == null) {
                        tcCoverageCnt = 1;
                    }
                    else {
                        tcCoverageCnt = counts.get(tcNum) + 1;
                    }

                    counts.put(tcNum, tcCoverageCnt);

                    if(tcCoverageCnt > maxCoverageCnt)
                        maxCoverageCnt = tcCoverageCnt;
                }
            }
        }

        /* Construct TESTLIST consisting of tests from listTC for which COUNT[l] is the maximum */
        SortedSet<Integer> testList = new TreeSet<Integer>();

        for(int key : counts.keySet()) {
            if(counts.get(key) == maxCoverageCnt) {
                testList.add(key);
            }
        }

        /* Recursive Logic */
        if (1 == testList.size()){
            return testList.first();
        }else if(current_card == max_card){
            /* Here should return 'any' testcase */
            /* Can be changed to return random testcase */
            return testList.first();
        }else{
            return (SelectTest(current_card+1, testList, matrix, max_card));
        }

    }

    /**
     * Get the greatest Cardinality in the Matrix
     * */
    public static int getMaxCardinality(Array2DRowRealMatrix matrix){
        int maxCard = UNINITIALIZED;
        if (null == matrix){
            System.out.println("\ngetMaxCardinality(): Matrix is null!\n");
            return 0;
        }
        for(int i=0; i<matrix.getColumnDimension();i++){
            double[] columnList = matrix.getColumn(i);
            int sum =0;
            for(double c: columnList)
                sum+=c;
            if (sum>maxCard)
                maxCard =sum;
        }

        return maxCard;
    }

    /**
     * Get the cardinality of a column */
    public static int getCardinality(Array2DRowRealMatrix matrix, int columnNum){
        if (columnNum>=matrix.getColumnDimension()) {
            System.out.println("\ngetCardinality: ERROR - columnNum>=matrix.getColumnDimension()\n");
            return UNINITIALIZED;
        }else{
            double[] columnList = matrix.getColumn(columnNum);
            int sum =0;
            for(double c: columnList)
                sum+=c;
            return sum;
        }
    }

    /**
     * Get a list of test cases which belongs to Ti
     * */
    public static Set<Integer> getTCListfromColumn(Array2DRowRealMatrix matrix, int ti){
        if (ti>=matrix.getColumnDimension()) {
            System.out.println("\ngetTCListfromColumn: ERROR - columnNum>=matrix.getColumnDimension()\n");
            return null;
        }else{
            double[] columnList = matrix.getColumn(ti);
            Set<Integer> TCList = new HashSet<>();
            for (int i=0;i<columnList.length;i++){
                if (0 != columnList[i]){
                    TCList.add(i);
                }
            }
            if (0!=TCList.size())
            return TCList;
            else
                return null;
        }
    }

    /**
     * Return whether the two sets have intersection
     */
    public static boolean hasIntersection(double[] set1, Set<Integer> set2){
        for (double i : set1){
            if (set2.contains((int)i))
                return true;
        }
        return false;
    }

    /**
     * Return whether the two sets have intersection
     */
    public static boolean hasIntersection(Set<Integer> set1, Set<Integer> set2){
        for (double i : set1){
            if (set2.contains(i))
                return true;
        }
        return false;
    }


}
