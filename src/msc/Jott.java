package msc;
import java.util.ArrayList;
import provided.*;

public class Jott {
    static JottTree root;
    static SymbolTable symbolTable = new SymbolTable();

    public static void main(String[] args) {
        
        if (args.length != 1) {
            return;
        }
        String filename = args[0];

        ArrayList<Token> tokens = JottTokenizer.tokenize(filename);
        if (tokens == null) {
            return;
        }

        try {
            root = JottParser.parse(tokens);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        if (root == null) {
            return;
        }

        try {
            root.validateTree(symbolTable);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        try {
            root.execute(symbolTable);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
