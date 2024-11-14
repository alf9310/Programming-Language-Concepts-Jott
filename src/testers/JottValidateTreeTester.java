package testers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import provided.*;

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

        try {
            ArrayList<Token> tokens = JottTokenizer.tokenize("phase3testcases/" + test.fileName);
            JottTree root = JottParser.parse(tokens);

            if (!test.error && root == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a JottTree and got null");
                return false;
            } else if (test.error && root == null) {
                return true;
            } else if (test.error) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a null and got JottTree");
                return false;
            }

            // Call validateTree() on the parsed JottTree
            boolean validationPassed = root.validateTree();
            if (test.error && validationPassed) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected validation to fail, but it passed.");
                return false;
            } else if (!test.error && !validationPassed) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected validation to pass, but it failed.");
                return false;
            }

            return true;

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