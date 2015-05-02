package utils;

public class ShutDownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("-------------------- Smoke Analysis Started --------------------");

                StatementCoverage.getStatementCoverage().print();

                ReportGenerator repGen = new ReportGenerator(StatementCoverage.getStatementCoverage().analyze());
                repGen.setOutputFile("/Users/stanley/Desktop/tmp/report.xls");
                try {
                    repGen.write();
                    System.out.println("Please check the result file under /Users/stanley/Desktop/tmp/report.xls");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}