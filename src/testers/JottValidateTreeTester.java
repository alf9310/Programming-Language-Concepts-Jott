package testers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import msc.*;

public class JottValidateTreeTester {
    ArrayList<TestCase> testCases;

    private static class TestCase {
        String testName;
        String fileName;
        boolean error;

        public TestCase(String testName, String fileName, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.error = error;
        }
    }

    private void createTestCases() {
        this.testCases = new ArrayList<>();
        testCases.add(new TestCase("Function Call Parameter Invalid", "funcCallParamInvalid.jott", true));
        testCases.add(new TestCase("Function Not Defined", "funcNotDefined.jott", true));
        testCases.add(new TestCase("Function Return in Expression", "funcReturnInExpr.jott", true));
        testCases.add(new TestCase("Function Wrong Parameter Type", "funcWrongParamType.jott", true));
        testCases.add(new TestCase("Hello World", "helloWorld.jott", false));
        testCases.add(new TestCase("If Statement with Returns", "ifStmtReturns.jott", false));
        testCases.add(new TestCase("Larger Valid Program", "largerValid.jott", false));
        testCases.add(new TestCase("Main Return Not Integer", "mainReturnNotInt.jott", true));
        testCases.add(new TestCase("Mismatched Return Type", "mismatchedReturn.jott", true));
        testCases.add(new TestCase("Missing Function Parameters", "missingFuncParams.jott", true));
        testCases.add(new TestCase("Missing Main Function", "missingMain.jott", true));
        testCases.add(new TestCase("Missing Return Statement", "missingReturn.jott", true));
        testCases.add(new TestCase("No Return in If Statement", "noReturnIf.jott", true));
        testCases.add(new TestCase("No Return in While Loop", "noReturnWhile.jott", true));
        testCases.add(new TestCase("Provided Example 1", "providedExample1.jott", false));
        testCases.add(new TestCase("Return with Identifier Type Mismatch", "returnId.jott", true));
        testCases.add(new TestCase("Valid Loop Structure", "validLoop.jott", false));
        testCases.add(new TestCase("Void Return Type", "voidReturn.jott", true));
        testCases.add(new TestCase("While Keyword Misuse", "whileKeyword.jott", true));
    }

    private boolean validateTreeTest(TestCase test) {
        System.out.println("Running Test: " + test.testName);
        String originalJottCode;
        try {
            originalJottCode = new String(
                    Files.readAllBytes(Paths.get("phase3testcases/" + test.fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Call the Main.main method with the file name as a command-line argument
        String[] args = {"phase3testcases/" + test.fileName};
        try {
            // Redirecting System.out and System.err if needed
            System.setErr(System.out);  // or any other redirection logic
            msc.Main.main(args);

            // If we expect an error, check the output (you could capture stderr output)
            if (test.error) {
                System.err.println("\tFailed Test: " + test.testName);
                return false;
            } else {
                System.out.println("\tPassed\n");
                return true;
            }

        } catch (Exception e) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("Unknown Exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottValidateTreeTester tester = new JottValidateTreeTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for (JottValidateTreeTester.TestCase test : tester.testCases) {
            numTests++;
            if (tester.validateTreeTest(test)) {
                passedTests++;
                System.out.println("\tPassed\n");
            } else {
                System.out.println("\tFailed\n");
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}