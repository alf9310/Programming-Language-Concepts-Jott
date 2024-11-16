package testers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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

        // Capture System.err output
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errStream));

        // Call the Main.main method with the file name as a command-line argument
        String[] args = {"phase3testcases/" + test.fileName};
        try {
            msc.Jott.main(args);

            String errOutput = errStream.toString();

            // If we expect an error and errOutput contains something, the test passes
            if (test.error) {
                if (!errOutput.isEmpty()) {
                    System.out.println("\tPassed\n");
                    System.out.println(errOutput);
                    return true;
                } else {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("\tExpected an error, but none occurred.");
                    return false;
                }
            }

            // If we do not expect an error, check that errOutput is empty
            if (errOutput.isEmpty()) {
                System.out.println("\tPassed\n");
                return true;
            } else {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\tExpected no error, but got: " + errOutput);
                return false;
            }
        } catch (Exception e) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("Unknown Exception occurred.");
            e.printStackTrace();
            return false;
        } finally {
            // Restore original System.err
            System.setErr(originalErr);
        }
    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottValidateTreeTester tester = new JottValidateTreeTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for (JottValidateTreeTester.TestCase test : tester.testCases) {
            System.out.println("-------------------------------Test Number "+ numTests + "-------------------------------");
            numTests++;
            if (tester.validateTreeTest(test)) {
                passedTests++;
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}