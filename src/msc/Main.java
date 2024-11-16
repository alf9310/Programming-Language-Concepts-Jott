package msc;
import java.util.ArrayList;
import provided.*;

public class Main {
    //TODO check docs for how file name should be passed to be compatible with testers
    // Also how to throw user errors
    static JottTree root;
    static SymbolTable symbolTable = new SymbolTable();
    
        public static void main(String[] args) {
            
            if (args.length != 1) {
                // System.err.println("Provide a single file name");
                return;
            }
            String filename = args[0];
            //String filename = "phase3testcases/funcCallParamInvalid.jott";
            //String filename = "phase3testcases/ifStmtReturns.jott"; 
    
            ArrayList<Token> tokens = JottTokenizer.tokenize(filename);
            if (tokens == null) {
                // System.err.println("\t\tExpected a list of tokens, but got null");
                return;
            }

            System.out.println(tokenListString(tokens));
    
            try {
                root = JottParser.parse(tokens);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        if (root == null) {
            // System.err.println("Got null instead of a JottTree!");
            return;
        }

        System.err.println(symbolTable);
        try {
            root.validateTree(symbolTable);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        // try {
        //     // root.execute();
        // } catch (Exception e) {
        //     System.err.println(e.getMessage());
        //     return;
        // }
        //System.out.printf("Interpreter succeeded for " + filename + "!!!");
    }


    // copied from tester
    private static String tokenListString(ArrayList<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            sb.append(t.getToken());
            sb.append(":");
            sb.append(t.getTokenType().toString());
            sb.append(" ");
        }
        return sb.toString();
    }

}
