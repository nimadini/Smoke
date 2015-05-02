package utils;

public class ShutDownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Inside Add Shutdown Hook");
                /*for (TestCase t : StatementCoverage.getStatementCoverage().getAllTestCases()) {
                    System.out.println("Test Case: " + t.getLongName() + ":: " + t.getExecutionTime());
                }*/
                StatementCoverage.getStatementCoverage().print();
                StatementCoverage.getStatementCoverage().genMatrix();
            }
        });
        System.out.println("Shut Down Hook Attached.");
    }
}