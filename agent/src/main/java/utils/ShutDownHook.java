package utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The shutdown hook which runs the set cover algorithm and
 * stores the result on disc.
 *
 * Created by Nima Dini | April 2015
 */

public class ShutDownHook {
    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // uncomment to see more details during runtime analysis
                // StatementCoverage.getStatementCoverage().print();

                // generating the xml report and writing it to disc
                ApplicationContext context = new ClassPathXmlApplicationContext("smoke.xml");
                ReportGenerator rGen = (ReportGenerator) context.getBean("reportGenerator");

                rGen.setAnalysis(StatementCoverage.getStatementCoverage().analyze());
                try {
                    rGen.write();
                    System.out.println("The report is generated successfully under: " + rGen.getOutputFilePath());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}