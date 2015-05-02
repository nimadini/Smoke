package utils;

public class Utility {
    public static String getTestCaseCaller(Exception e) {
        for (int i = e.getStackTrace().length - 1; i >= 0; i--) {
            StackTraceElement st = e.getStackTrace()[i];
            String callerName = st.getClassName() + '.' + st.getMethodName() + "()";
            if (utils.StatementCoverage.getStatementCoverage().isTestCase(callerName)) {
                return callerName;
            }
        }
        return null;
    }
}
