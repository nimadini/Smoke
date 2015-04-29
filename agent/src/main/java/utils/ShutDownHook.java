package utils;

public class ShutDownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Inside Add Shutdown Hook");
                for (String elem : StatementCoverage.getStatementCoverage().getElems()) {
                    System.out.println("ELEM: " + elem);
                }
            }
        });
        System.out.println("Shut Down Hook Attached.");
    }
}