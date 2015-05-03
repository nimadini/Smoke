import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import utils.TestSuiteCutter;

class TSCutterTest{
    public static void main(String ...args){
        double[] col1 = new double[]{1, 1, 1, 0, 0, 0};
        double[] col2 = new double[]{1, 0, 0, 1, 0, 0};
        double[] col3 = new double[]{0, 1, 0, 0, 1, 0};
        double[] col4 = new double[]{0, 0, 1, 0, 0, 1};
        double[] col5 = new double[]{0, 0, 0, 0, 1, 0};
        double[][] simpleMatrix1_array = new double[][]{col1, col2, col3, col4, col5};
        Array2DRowRealMatrix simpleMatrix = new Array2DRowRealMatrix(simpleMatrix1_array);
        System.out.println(TestSuiteCutter.maxCoverage(simpleMatrix));
        double[][] simpleMatrix2_array = new double[][]{col1, col2, col3, col5};
        Array2DRowRealMatrix simpleMatrix2 = new Array2DRowRealMatrix(simpleMatrix2_array);
        System.out.println(TestSuiteCutter.maxCoverage(simpleMatrix2));


        /* test vector products */
        RealVector v1 = simpleMatrix.getColumnVector(0);
        RealVector v2 = simpleMatrix.getColumnVector(1);
        System.out.println("Product of v1 and v2 is " + TestSuiteCutter.coverage(v1,v2));
        RealVector v3 = simpleMatrix.getColumnVector(4);
        System.out.println("Product of v1 and v3 is " + TestSuiteCutter.coverage(v1,v3));

        /* test coverAndReduceTheRequirementVector*/
        System.out.println("coverAndReduceTheRequirementVector(v1, v2) = " + TestSuiteCutter.coverAndReduceTheRequirementVector(v1, v2));

        /* test the greedy cutter method */
        System.out.println("\n Now testing the greedy cutter: \n " + TestSuiteCutter.findCoverWithGreedy(simpleMatrix));
        System.out.println(TestSuiteCutter.findCoverWithGreedy(simpleMatrix2));

        /* test the HGS cutter method*/
        System.out.println("----------------------------------\n Now testing the HGS cutter:\n ");
        System.out.println(TestSuiteCutter.findCoverWithHGS(simpleMatrix));
    }
}